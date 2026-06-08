package com.example.ainote.controller;

import com.example.ainote.common.Result;
import com.example.ainote.dto.PasswordUpdateRequest;
import com.example.ainote.dto.UserProfileUpdateRequest;
import com.example.ainote.service.SysUserService;
import com.example.ainote.vo.UserVO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
    private final SysUserService sysUserService;

    @PutMapping("/profile")
    public Result<UserVO> updateProfile(@Valid @RequestBody UserProfileUpdateRequest request) {
        return Result.success(sysUserService.updateProfile(request));
    }

    @PutMapping("/password")
    public Result<Void> updatePassword(@Valid @RequestBody PasswordUpdateRequest request) {
        sysUserService.updatePassword(request);
        return Result.success();
    }
}
