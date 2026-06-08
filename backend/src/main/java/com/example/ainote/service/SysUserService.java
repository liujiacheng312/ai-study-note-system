package com.example.ainote.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.ainote.dto.LoginRequest;
import com.example.ainote.dto.PasswordUpdateRequest;
import com.example.ainote.dto.RegisterRequest;
import com.example.ainote.dto.UserProfileUpdateRequest;
import com.example.ainote.entity.SysUser;
import com.example.ainote.vo.LoginVO;
import com.example.ainote.vo.UserVO;

public interface SysUserService extends IService<SysUser> {
    UserVO register(RegisterRequest request);

    LoginVO login(LoginRequest request, String ip, String userAgent);

    UserVO profile();

    UserVO updateProfile(UserProfileUpdateRequest request);

    void updatePassword(PasswordUpdateRequest request);

    void changeStatus(Long id, Integer status);

    void resetPassword(Long id);
}
