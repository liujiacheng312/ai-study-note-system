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

AI 模块推荐通过页面配置。普通用户登录后进入“AI 配置”维护个人 API；管理员登录后进入“后台管理 - AI 配置”维护全局 API。系统调用顺序为：优先使用当前用户个人配置，个人配置未启用或真实模式未填写 Key 时使用管理员全局配置。

配置页面可以维护：

- 供应商：DeepSeek、OpenAI、阿里云百炼、智谱 AI、本地模型等
- 调用模式：真实 API 或模拟返回
- 接口地址：OpenAI-compatible `/chat/completions` 网关地址
- 模型名称：如 `deepseek-chat`、`gpt-4o-mini`、`qwen-turbo`
- API Key：保存时写入数据库，查询时脱敏显示
- 失败降级：真实 API 调用失败后是否自动使用 mock 演示回答

数据库首次没有全局配置记录时，后端会根据以下环境变量自动创建一条管理员全局配置；如果没有配置 API Key，会自动降级为 mock 演示回答，保证系统仍可运行：

```yaml
ainote:
  ai:
    mode: ${AINOTE_AI_MODE:real}
    api-base-url: ${AINOTE_AI_API_BASE_URL:https://api.deepseek.com/v1}
    api-key: ${AINOTE_AI_API_KEY:}
    model-name: ${AINOTE_AI_MODEL_NAME:deepseek-chat}
    mock-on-failure: ${AINOTE_AI_MOCK_ON_FAILURE:true}
```

首次初始化真实 AI 默认配置的 PowerShell 示例：

```powershell
$env:SPRING_DATASOURCE_PASSWORD="你的MySQL密码"
$env:AINOTE_AI_MODE="real"
$env:AINOTE_AI_API_BASE_URL="https://api.deepseek.com/v1"
$env:AINOTE_AI_API_KEY="你的DeepSeek API Key"
$env:AINOTE_AI_MODEL_NAME="deepseek-chat"
java -jar target/ai-study-note-backend.jar --server.port=8081
```

OpenAI 示例：

```powershell
$env:AINOTE_AI_API_BASE_URL="https://api.openai.com/v1"
$env:AINOTE_AI_API_KEY="你的OpenAI API Key"
$env:AINOTE_AI_MODEL_NAME="gpt-4o-mini"
```

如果希望真实 AI 调用失败时直接报错，而不是降级 mock，可以在管理员后台关闭“失败降级”，或在首次初始化时设置：

```powershell
$env:AINOTE_AI_MOCK_ON_FAILURE="false"
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
| AI API Key 未配置 | 普通用户可进入“AI 配置”填写个人 Key，管理员可进入后台“AI 配置”填写全局 Key；都未配置时系统会降级为 mock 演示回答 |
| Maven 命令不存在 | 安装 Maven 3.8+ 并配置 PATH |
| npm install 慢 | 可切换国内镜像源或使用 pnpm/npm 缓存 |
