package com.example.ainote.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserRole {
    USER("普通用户"),
ADMIN("管理员");

    private final String label;
}
