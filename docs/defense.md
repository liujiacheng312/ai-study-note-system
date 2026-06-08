# 答辩演示稿

## 1. 项目背景

各位老师好，我的项目是《智学 AI 学习笔记系统》。本项目面向大学生和学习者，主要解决学习笔记分散、知识点难整理、复习效率低、缺少智能辅助的问题。系统围绕学习笔记生命周期进行设计，并结合 AI 摘要、AI 标签推荐、AI 问答和学习统计，帮助用户形成结构化知识库。

## 2. 技术栈介绍

后端使用 Spring Boot 3、JDK 17、Spring Security、JWT、MyBatis Plus、MySQL 8、Lombok 和 Knife4j。前端使用 Vue3、Vite、TypeScript、Vue Router、Pinia、Axios、Element Plus、ECharts 和 Markdown 编辑器。项目采用前后端分离架构，前端通过 RESTful API 调用后端服务。

## 3. GitHub 仓库展示

打开 GitHub 仓库 `ai-study-note-system`，展示项目目录结构，包括 backend、frontend、database、docs、screenshots、paper 等目录。说明项目按照课程要求整理了代码、数据库脚本、接口文档、部署文档、测试文档、AI 使用说明、论文和答辩材料。

## 4. Git 提交记录展示

进入 GitHub 的 Commits 页面，展示从初始化到后端、前端、用户模块、笔记模块、AI 模块、管理员模块、统计模块和文档模块的提交记录。说明本项目采用 main、dev、feature 分支管理，提交信息遵循 feat、fix、docs、test、chore 等规范。

## 5. 数据库表展示

打开 MySQL 客户端，展示 `ai_study_note` 数据库，说明核心表包括 sys_user、note、category、tag、favorite、comment、announcement、ai_chat_record、ai_usage_log 等。重点讲解 note 表中的状态字段和 AI 结果字段。

## 6. 启动后端项目

进入 backend 目录，执行：

```bash
mvn spring-boot:run
```

打开 Knife4j 接口文档：

```text
http://localhost:8080/doc.html
```

展示认证接口、笔记接口、AI 接口和管理员接口。

## 7. 启动前端项目

进入 frontend 目录，执行：

```bash
npm run dev
```

打开：

```text
http://localhost:5173
```

## 8. 普通用户注册登录

使用测试账号 `user / 123456` 登录。说明登录成功后后端返回 JWT，前端将 token 保存到 LocalStorage，后续请求通过 Axios 拦截器自动携带 Authorization 请求头。

## 9. 创建学习笔记

进入“新建笔记”页面，填写标题、分类、标签和 Markdown 正文。保存后笔记状态为 DRAFT，说明草稿状态可以继续编辑。

## 10. 提交 AI 摘要

点击“提交 AI 处理”，系统将笔记状态变为 WAITING_AI，AI 处理完成后变为 AI_FINISHED，并生成摘要、学习重点、考试重点和学习建议。说明当前演示使用 mock 模式，无需 API Key，也可以切换真实 API 模式。

## 11. AI 推荐标签

展示 AI 标签推荐接口，说明系统会根据标题和正文推荐学习标签，后续可以接入 OpenAI、DeepSeek、阿里云百炼、智谱 AI 或本地大模型。

## 12. 发布笔记

AI 处理完成后点击发布，笔记状态变为 PUBLISHED。进入公开笔记广场，可以看到已发布笔记。

## 13. 收藏和评论

在公开笔记详情页进行收藏和评论。说明只有已发布笔记允许收藏和评论，归档笔记不允许继续评论。

## 14. 管理员登录

退出普通用户，使用 `admin / 123456` 登录。管理员登录后左侧菜单出现后台管理模块。

## 15. 管理员管理用户和笔记

进入用户管理页面，展示用户分页、禁用、启用、重置密码。进入笔记管理页面，展示所有笔记，管理员可以删除违规笔记。

## 16. 学习统计图表

进入学习统计页面，展示笔记总数、本周新增、收藏数量、AI 使用次数、分类统计图和月度学习趋势图。说明图表使用 ECharts 实现。

## 17. 项目亮点总结

- 前后端分离，结构清晰。
- Spring Security + JWT 实现认证授权。
- RBAC 区分普通用户和管理员。
- 笔记状态流转完整，体现业务流程。
- AI 模块支持摘要、标签推荐、问答和学习建议。
- AI 支持 mock 模式和真实 API 模式。
- ECharts 实现学习统计可视化。
- GitHub 分支和提交规范完整。
- SQL、接口、部署、测试、论文和答辩文档齐全。

## 18. 结束语

本项目完整实践了软件生产实习中的需求分析、系统设计、数据库设计、前后端开发、接口联调、系统测试、部署和版本管理流程。通过该项目，我加深了对前后端分离、RESTful API、JWT 权限控制、AI 应用集成和工程化开发流程的理解。

