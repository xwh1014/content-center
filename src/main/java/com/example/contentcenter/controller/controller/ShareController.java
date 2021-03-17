package com.example.contentcenter.controller.controller;

import com.example.contentcenter.service.content.ShareService;
import com.example.domain.dto.content.ShareDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @program content-center
 * @description:
 * @author: xiewenhui
 * @create: 2021/03/10 23:11
 */
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RequestMapping("/shares")
public class ShareController {
    private  final ShareService shareService;

    @GetMapping("/{id}")
    public ShareDTO findById(@PathVariable Integer id){
        return this.shareService.findById(id);
    }
}
