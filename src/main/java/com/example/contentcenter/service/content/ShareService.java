package com.example.contentcenter.service.content;

import com.example.contentcenter.dao.content.ShareMapper;
import com.example.contentcenter.dao.messaging.RocketmqTransactionLogMapper;
import com.example.contentcenter.domain.entity.content.Share;
import com.example.contentcenter.domain.entity.messaging.RocketmqTransactionLog;
import com.example.contentcenter.feignClient.UserCenterFeignClient;
import com.example.domain.dto.content.ShareAuditDTO;
import com.example.domain.dto.content.ShareDTO;
import com.example.domain.dto.messaging.UserAddBonusMsgDTO;
import com.example.domain.dto.user.UserDTO;
import com.example.domain.enums.AuditStatusEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.apache.rocketmq.spring.support.RocketMQHeaders;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

/**
 * @program content-center
 * @description:
 * @author: xiewenhui
 * @create: 2021/03/10 22:31
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ShareService {

    private final ShareMapper shareMapper;
    private final UserCenterFeignClient userCenterFeignClient;
    private final RocketMQTemplate rocketMQTemplate;
    private final RocketmqTransactionLogMapper rocketmqTransactionLogMapper;
//    private final Source source;

    public ShareDTO findById(Integer id){
        Share share = this.shareMapper.selectByPrimaryKey(id);
        Integer userId = share.getUserId();
//        RestTemplate restTemplate = new RestTemplate();
//        List<ServiceInstance> instances = discoveryClient.getInstances("user-center");
        //获取单个实例，并抛出异常
//        String targetUrl = instances.stream()
//                .map(instance-> instance.getUri().toString() + "/users/{id}")
//                .findFirst()
//                .orElseThrow(()-> new ArrayIndexOutOfBoundsException("当前没有实例"));
//                 获取多个实例，ribbon随机
//                 List<String> targetUrls = instances.stream()
//                .map(instance-> instance.getUri().toString() + "/users/{id}")
//                .collect(Collectors.toList());
//                 int i = ThreadLocalRandom.current().nextInt(targetUrls.size());
//        log.info(targetUrls.get(i));
        UserDTO userDTO = this.userCenterFeignClient.findById(userId);
        //RestTemplate通信
//        UserDTO userDTO = this.restTemplate.getForObject(
//                "http://user-center/users/{userId}",
//                UserDTO.class,
//                userId
//        );
        //消息的装配
//        Share.builder().id(share.getId()).build();
        ShareDTO shareDTO = new ShareDTO();
        BeanUtils.copyProperties(share,shareDTO);
        shareDTO.setWxNickName(userDTO.getWxNickname());
        return  shareDTO;
    }

    public static void main(String[] args) {
        RestTemplate restTemplate = new RestTemplate();
        String forObject = restTemplate.getForObject(
                "http://localhost:9090/users/1", String.class
        );
        System.out.println(forObject);
    }

    public Share auditById(Integer id, ShareAuditDTO auditDTO) {

        Share share = this.shareMapper.selectByPrimaryKey(id);
        if (share == null) {
            throw new IllegalArgumentException("参数非法！该分享不存在！");
        }
        if (!Objects.equals("NOT_YET", share.getAuditStatus())) {
            throw new IllegalArgumentException("参数非法！该分享已审核通过或审核不通过！");
        }
        if (AuditStatusEnum.PASS.equals(auditDTO.getAuditStatusEnum())) {
//
            String transactionId = UUID.randomUUID().toString();
            this.rocketMQTemplate.sendMessageInTransaction(
                    "test-1add-bouns-group",
                    "1add-bouns",
                    MessageBuilder.withPayload(
                            UserAddBonusMsgDTO.builder()
                                    .userId(share.getUserId())
                                    .bonus(50)
                                    .build()
                    ).setHeader(RocketMQHeaders.TRANSACTION_ID, transactionId)
                     .setHeader("share_id",id)
                    .build(),auditDTO
            );
        }
//        this.rocketMQTemplate.convertAndSend("1add-bouns", UserAddBonusMsgDTO.builder()
//                .bonus(50).userId(1).build());
        return new Share();
    }

    @Transactional(rollbackFor = Exception.class)
    public void auditByIdInDB(Integer id, ShareAuditDTO auditDTO) {
        Share share = Share.builder()
                .id(id)
                .auditStatus(auditDTO.getAuditStatusEnum().toString())
                .reason(auditDTO.getReason())
                .build();
        this.shareMapper.updateByPrimaryKeySelective(share);

        // 4. 把share写到缓存
    }


    @Transactional(rollbackFor = Exception.class)
    public void auditByIdWithRocketMqLog(Integer id, ShareAuditDTO auditDTO, String transactionId) {
        this.auditByIdInDB(id, auditDTO);

        this.rocketmqTransactionLogMapper.insertSelective(
                RocketmqTransactionLog.builder()
                        .transactionId(transactionId)
                        .log("审核分享...")
                        .build()
        );
    }
}
