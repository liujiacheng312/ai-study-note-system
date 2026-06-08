# AI Smart Study Note System Backend

后端基于 Spring Boot 3、Spring Security、JWT、MyBatis Plus、MySQL 8 和 Knife4j 实现。

## 启动

1. 创建数据库并导入 `../database/schema.sql`、`../database/data.sql`
2. 修改 `src/main/resources/application.yml` 中的数据库账号密码
3. 执行：

```bash
mvn clean install
mvn spring-boot:run
```

接口文档地址：`http://localhost:8080/doc.html`
