# GitHub 版本管理文档

## 1. 仓库信息

- 仓库名称：`ai-study-note-system`
- 仓库说明：基于 Spring Boot + Vue3 的 AI 学习笔记系统，支持笔记管理、AI 摘要、AI 标签推荐、AI 问答和学习统计。
- 建议仓库地址：`https://github.com/你的GitHub用户名/ai-study-note-system`

## 2. 分支说明

| 分支 | 说明 |
| --- | --- |
| main | 稳定版本，用于最终提交 |
| dev | 开发主分支 |
| feature/backend-init | 后端初始化 |
| feature/frontend-init | 前端初始化 |
| feature/user-auth | 用户登录注册模块 |
| feature/note-module | 笔记管理模块 |
| feature/ai-module | AI 功能模块 |
| feature/admin-module | 管理员模块 |
| feature/statistics | 统计模块 |
| feature/deploy-docs | 部署和文档模块 |

## 3. 提交规范

| 类型 | 说明 |
| --- | --- |
| init | 项目初始化 |
| feat | 新增功能 |
| fix | 修复问题 |
| docs | 文档修改 |
| style | 代码格式调整 |
| refactor | 代码重构 |
| test | 测试相关 |
| chore | 构建或依赖调整 |

提交示例：

```bash
git commit -m "feat: 完成用户注册登录和 JWT 认证"
```

## 4. GitHub 提交流程

### 第一阶段：初始化仓库

```bash
git init
git add README.md .gitignore LICENSE
git commit -m "init: 初始化 AI 学习笔记系统仓库"
git branch -M main
git remote add origin https://github.com/你的GitHub用户名/ai-study-note-system.git
git push -u origin main
```

### 第二阶段：创建开发分支

```bash
git checkout -b dev
git push -u origin dev
```

### 第三阶段：后端初始化

```bash
git checkout -b feature/backend-init
git add backend database
git commit -m "feat: 完成后端 Spring Boot 基础框架"
git push -u origin feature/backend-init
git checkout dev
git merge feature/backend-init
git push origin dev
```

### 第四阶段：前端初始化

```bash
git checkout -b feature/frontend-init
git add frontend
git commit -m "feat: 完成前端 Vue3 基础框架"
git push -u origin feature/frontend-init
git checkout dev
git merge feature/frontend-init
git push origin dev
```

### 第五阶段：用户模块

```bash
git checkout -b feature/user-auth
git add backend frontend
git commit -m "feat: 完成用户注册登录和 JWT 认证"
git push -u origin feature/user-auth
git checkout dev
git merge feature/user-auth
git push origin dev
```

### 第六阶段：笔记模块

```bash
git checkout -b feature/note-module
git add backend frontend
git commit -m "feat: 完成学习笔记管理和状态流转功能"
git push -u origin feature/note-module
git checkout dev
git merge feature/note-module
git push origin dev
```

### 第七阶段：AI 模块

```bash
git checkout -b feature/ai-module
git add backend frontend docs/ai-usage.md
git commit -m "feat: 完成 AI 摘要、标签推荐和问答功能"
git push -u origin feature/ai-module
git checkout dev
git merge feature/ai-module
git push origin dev
```

### 第八阶段：管理员模块

```bash
git checkout -b feature/admin-module
git add backend frontend
git commit -m "feat: 完成管理员后台管理功能"
git push -u origin feature/admin-module
git checkout dev
git merge feature/admin-module
git push origin dev
```

### 第九阶段：统计模块

```bash
git checkout -b feature/statistics
git add backend frontend
git commit -m "feat: 完成学习统计和数据看板功能"
git push -u origin feature/statistics
git checkout dev
git merge feature/statistics
git push origin dev
```

### 第十阶段：文档和部署

```bash
git checkout -b feature/deploy-docs
git add docs paper README.md
git commit -m "docs: 完成项目文档和部署说明"
git push -u origin feature/deploy-docs
git checkout dev
git merge feature/deploy-docs
git push origin dev
```

### 第十一阶段：合并正式版本

```bash
git checkout main
git merge dev
git tag v1.0.0
git push origin main
git push origin v1.0.0
```

## 5. 主要提交记录

| 序号 | 提交信息 | 功能说明 | 分支 | 时间 |
| --- | --- | --- | --- | --- |
| 1 | init: 初始化 AI 学习笔记系统仓库 | 创建项目基本结构 | main | 2026-06-08 |
| 2 | feat: 完成后端 Spring Boot 基础框架 | 创建后端项目、配置依赖、统一返回 | feature/backend-init | 2026-06-08 |
| 3 | feat: 完成前端 Vue3 基础框架 | 创建前端项目、路由、Pinia、Axios | feature/frontend-init | 2026-06-08 |
| 4 | feat: 完成用户注册登录和 JWT 认证 | 实现认证、权限控制、登录拦截 | feature/user-auth | 2026-06-08 |
| 5 | feat: 完成学习笔记管理和状态流转功能 | 实现笔记生命周期 | feature/note-module | 2026-06-08 |
| 6 | feat: 完成 AI 摘要、标签推荐和问答功能 | 实现 mock AI 和真实 API 兼容设计 | feature/ai-module | 2026-06-08 |
| 7 | feat: 完成管理员后台管理功能 | 用户、笔记、分类、标签、公告管理 | feature/admin-module | 2026-06-08 |
| 8 | feat: 完成学习统计和数据看板功能 | 首页统计、分类图表、月度趋势 | feature/statistics | 2026-06-08 |
| 9 | docs: 完成项目文档和部署说明 | API、数据库、部署、测试、AI说明 | feature/deploy-docs | 2026-06-08 |
| 10 | chore: 完成项目最终整理 | 合并 dev 到 main，创建 v1.0.0 | main | 2026-06-08 |

## 6. 版本标签

| 标签 | 说明 |
| --- | --- |
| v1.0.0 | 课程结课提交版本 |

## 7. GitHub 截图说明

答辩时建议准备以下截图：

- GitHub 仓库首页
- 分支列表
- Pull Request 或 merge 记录
- Commit 提交记录
- Release 或 tag 页面
- README 展示页面

