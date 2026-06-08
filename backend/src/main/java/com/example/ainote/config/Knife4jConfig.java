package com.example.ainote.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Knife4jConfig {
    @Bean
    public OpenAPI aiNoteOpenApi() {
        return new OpenAPI().info(new Info()
                .title("智学 AI 学习笔记系统接口文档")
                .description("Spring Boot + Vue3 前后端分离项目 RESTful API")
                .version("1.0.0")
                .contact(new Contact().name("AI Smart Study Note System"))
                .license(new License().name("MIT")));
    }
}
