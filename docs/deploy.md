# 项目部署文档

## 1. 环境要求

| 软件 | 版本要求 |
| --- | --- |
| JDK | 17 或 21 |
| Maven | 3.8+ |
| Node.js | 18+ |
| MySQL | 8.x |
| Git | 2.x |
| Nginx | 可选，用于前端静态资源部署 |

## 2. 数据库部署

登录 MySQL：

```bash
mysql -uroot -p
```

创建数据库：

```sql
CREATE DATABASE ai_study_note DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

导入脚本：

```sql
source database/schema.sql;
source database/data.sql;
```

## 3. 后端配置

进入后端目录：

```bash
cd backend
```

修改 `src/main/resources/application.yml`：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/ai_study_note?useUnicode=true&characterEncoding=utf8&serverTimezone=Asia/Shanghai&useSSL=false&allowPublicKeyRetrieval=true
    username: root
    password: root
```

AI 默认使用 mock 模式，无需 API Key：

```yaml
ainote:
  ai:
    mode: mock
```

如果需要真实 API 调用，可配置 OpenAI 兼容接口：

```yaml
ainote:
  ai:
    mode: real
    api-base-url: https://api.openai.com/v1
    api-key: sk-xxxx
    model-name: gpt-4o-mini
```

## 4. 后端启动

方式一：开发模式启动。

```bash
mvn clean install
mvn spring-boot:run
```

方式二：Jar 包启动。

```bash
mvn clean package -DskipTests
java -jar target/ai-study-note-backend.jar
```

后端默认端口：`8080`。

接口文档地址：

```text
http://localhost:8080/doc.html
```

## 5. 前端启动

进入前端目录：

```bash
cd frontend
npm install
npm run dev
```

前端访问地址：

```text
http://localhost:5173
```

## 6. 前端打包

```bash
npm run build
```

打包产物位于：

```text
frontend/dist
```

## 7. Nginx 部署

将 `frontend/dist` 下的文件复制到 Nginx 静态目录，例如 `/usr/share/nginx/html/ai-study-note`。

示例配置：

```nginx
server {
    listen 80;
    server_name localhost;

    root /usr/share/nginx/html/ai-study-note;
    index index.html;

    location / {
        try_files $uri $uri/ /index.html;
    }

    location /api/ {
        proxy_pass http://127.0.0.1:8080/api/;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
    }
}
```

重启 Nginx：

```bash
nginx -s reload
```

## 8. 常见问题

| 问题 | 解决方法 |
| --- | --- |
| 端口占用 | 修改 `application.yml` 的 `server.port` 或 Vite `server.port` |
| 数据库连接失败 | 检查 MySQL 是否启动、账号密码是否正确、数据库是否已导入 |
| 跨域问题 | 后端已配置 CORS，生产环境建议通过 Nginx 反向代理统一域名 |
| 登录 token 失效 | 重新登录，或调整 `ainote.jwt.expire-minutes` |
| AI API Key 未配置 | 使用默认 mock 模式即可正常演示 |
| Maven 命令不存在 | 安装 Maven 3.8+ 并配置 PATH |
| npm install 慢 | 可切换国内镜像源或使用 pnpm/npm 缓存 |

