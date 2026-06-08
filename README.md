# 智学 AI 学习笔记系统

AI Smart Study Note System 是一个基于 Spring Boot + Vue3 的前后端分离课程实训项目，面向大学生和学习者，提供学习笔记管理、AI 摘要、AI 标签推荐、AI 问答助手、收藏评论、学习统计和管理员后台管理功能。

## 技术栈

- 后端：Spring Boot 3.x、JDK 17、Spring Security、JWT、MyBatis Plus、MySQL 8、Lombok、Maven、Knife4j
- 前端：Vue3、Vite、TypeScript、Vue Router、Pinia、Axios、Element Plus、ECharts、md-editor-v3
- 工程：RESTful API、统一返回结果、统一异常处理、分页查询、RBAC 权限控制、GitHub 版本管理

## 功能模块

- 用户模块：注册、登录、JWT认证、个人资料、修改密码、管理员禁用/启用用户
- 笔记模块：草稿、AI处理、发布、归档、删除、公开广场、分类标签检索
- AI 模块：笔记摘要、标签推荐、学习重点、考试重点、学习建议、问答助手、个人 API 配置
- 互动模块：收藏、取消收藏、评论、回复、评论审核
- 管理后台：用户、笔记、分类、标签、公告、评论和统计管理
- 学习统计：笔记总数、本周新增、收藏数、AI使用次数、分类统计、月度趋势

## 项目结构

```text
ai-study-note-system
├── backend
├── frontend
├── database
├── docs
├── screenshots
├── paper
├── README.md
├── .gitignore
└── LICENSE
```

## 数据库说明

数据库名：`ai_study_note`。

```bash
mysql -uroot -p
source database/schema.sql;
source database/data.sql;
```

测试账号：

| 角色 | 用户名 | 密码 |
| --- | --- | --- |
| 管理员 | admin | 123456 |
| 普通用户 | user | 123456 |

## 后端启动

```bash
cd backend
mvn clean install
mvn spring-boot:run
```

接口文档：`http://localhost:8080/doc.html`

真实 AI 问答支持两级配置：普通用户登录后进入“AI 配置”维护自己的 API Key；管理员登录 `admin / 123456` 后进入“后台管理 - AI 配置”维护全局默认配置。系统调用时优先使用当前用户的个人配置，个人配置未启用或未填写 Key 时使用管理员全局配置。

系统支持 OpenAI-compatible `/chat/completions` 格式，已内置 DeepSeek、OpenAI、阿里云百炼、智谱 AI 和本地模型网关常用模板。API Key 查询时会脱敏显示；留空保存表示保留原 Key。

也可以在首次初始化默认配置时通过环境变量预置。以 DeepSeek 为例：

```powershell
$env:SPRING_DATASOURCE_PASSWORD="你的MySQL密码"
$env:AINOTE_AI_MODE="real"
$env:AINOTE_AI_API_BASE_URL="https://api.deepseek.com/v1"
$env:AINOTE_AI_API_KEY="你的DeepSeek API Key"
$env:AINOTE_AI_MODEL_NAME="deepseek-chat"
java -jar target/ai-study-note-backend.jar --server.port=8081
```

AI 问答页会显示实际模型名称；没有配置 Key 或真实调用失败且开启降级时，会显示 `mock-ai` 并使用演示回答。

## 前端启动

```bash
cd frontend
npm install
npm run dev
```

访问地址：`http://localhost:5173`

## 系统截图

截图建议在项目运行后替换到 `screenshots/` 目录：

- `screenshots/login.png`
- `screenshots/dashboard.png`
- `screenshots/note-list.png`
- `screenshots/note-edit.png`
- `screenshots/ai-chat.png`
- `screenshots/admin.png`

## Git 提交说明

提交遵循 `feat/fix/docs/style/refactor/test/chore/init` 规范。完整版本管理方案见 `docs/git-log.md`。

## 项目亮点

- 前后端分离架构清晰，接口统一采用 RESTful 风格
- Spring Security + JWT 实现登录认证和 RBAC 权限控制
- 笔记状态流转覆盖草稿、AI处理、发布、归档、删除完整生命周期
- AI 模块支持普通用户个人 API、管理员全局 API、mock 模式和 OpenAI 兼容真实 API 模式
- ECharts 学习统计图表适合课程答辩展示
- 文档、SQL、测试用例、部署说明、论文和答辩材料齐全

## 作者信息

课程：软件生产实习  
项目：智学 AI 学习笔记系统  
版本：v1.0.0
