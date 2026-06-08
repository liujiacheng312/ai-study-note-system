# RESTful API 接口文档

## 1. 通用说明

### 1.1 基础地址

```text
http://localhost:8080
```

### 1.2 统一返回格式

```json
{
  "code": 200,
  "message": "操作成功",
  "data": {},
  "timestamp": 1780915200000
}
```

### 1.3 认证方式

除注册、登录、公开笔记、分类标签列表和公告列表外，其余接口需要携带 JWT。

```http
Authorization: Bearer <token>
```

### 1.4 分页返回格式

```json
{
  "total": 100,
  "pageNo": 1,
  "pageSize": 10,
  "records": []
}
```

## 2. 认证接口

### 2.1 用户注册

| 项目 | 内容 |
| --- | --- |
| 接口名称 | 用户注册 |
| 请求路径 | `/api/auth/register` |
| 请求方式 | POST |
| 权限要求 | 匿名 |
| 业务说明 | 创建普通用户账号 |

请求参数：

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| username | string | 是 | 用户名 |
| password | string | 是 | 密码 |
| nickname | string | 是 | 昵称 |
| email | string | 否 | 邮箱 |

请求示例：

```json
{
  "username": "student01",
  "password": "123456",
  "nickname": "学生01",
  "email": "student01@example.com"
}
```

响应示例：

```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "id": 3,
    "username": "student01",
    "nickname": "学生01",
    "role": "USER",
    "status": 1
  }
}
```

### 2.2 用户登录

| 项目 | 内容 |
| --- | --- |
| 接口名称 | 用户登录 |
| 请求路径 | `/api/auth/login` |
| 请求方式 | POST |
| 权限要求 | 匿名 |
| 业务说明 | 验证账号密码，返回 JWT |

请求示例：

```json
{
  "username": "user",
  "password": "123456"
}
```

响应示例：

```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiJ9...",
    "user": {
      "id": 2,
      "username": "user",
      "nickname": "测试用户",
      "role": "USER"
    }
  }
}
```

### 2.3 退出登录

| 项目 | 内容 |
| --- | --- |
| 请求路径 | `/api/auth/logout` |
| 请求方式 | POST |
| 权限要求 | 登录用户 |
| 业务说明 | 前端清除 token，后端返回成功 |

### 2.4 当前用户信息

| 项目 | 内容 |
| --- | --- |
| 请求路径 | `/api/auth/profile` |
| 请求方式 | GET |
| 权限要求 | 登录用户 |
| 业务说明 | 获取当前登录用户资料 |

## 3. 用户接口

### 3.1 修改个人资料

| 项目 | 内容 |
| --- | --- |
| 请求路径 | `/api/user/profile` |
| 请求方式 | PUT |
| 权限要求 | 登录用户 |

请求示例：

```json
{
  "nickname": "新的昵称",
  "email": "new@example.com",
  "avatar": "https://example.com/avatar.png"
}
```

### 3.2 修改密码

| 项目 | 内容 |
| --- | --- |
| 请求路径 | `/api/user/password` |
| 请求方式 | PUT |
| 权限要求 | 登录用户 |

请求示例：

```json
{
  "oldPassword": "123456",
  "newPassword": "654321"
}
```

## 4. 笔记接口

### 4.1 创建笔记

| 项目 | 内容 |
| --- | --- |
| 请求路径 | `/api/notes` |
| 请求方式 | POST |
| 权限要求 | USER/ADMIN |
| 业务说明 | 创建草稿笔记，默认状态 DRAFT |

请求示例：

```json
{
  "categoryId": 1,
  "title": "Spring Security 学习笔记",
  "content": "# JWT认证流程\n\n登录后获取 token，请求接口时携带 Authorization。",
  "tagIds": [1, 4]
}
```

响应示例：

```json
{
  "code": 200,
  "data": {
    "id": 10,
    "title": "Spring Security 学习笔记",
    "status": "DRAFT"
  }
}
```

### 4.2 编辑笔记

| 项目 | 内容 |
| --- | --- |
| 请求路径 | `/api/notes/{id}` |
| 请求方式 | PUT |
| 权限要求 | 笔记作者或管理员 |
| 业务说明 | 仅 DRAFT 和 AI_FINISHED 状态允许编辑 |

### 4.3 删除笔记

| 项目 | 内容 |
| --- | --- |
| 请求路径 | `/api/notes/{id}` |
| 请求方式 | DELETE |
| 权限要求 | 笔记作者或管理员 |
| 业务说明 | 设置 DELETED 状态并逻辑删除 |

### 4.4 我的笔记分页

| 项目 | 内容 |
| --- | --- |
| 请求路径 | `/api/notes/my` |
| 请求方式 | GET |
| 权限要求 | 登录用户 |

请求参数：

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| pageNo | number | 否 | 页码 |
| pageSize | number | 否 | 每页条数 |
| keyword | string | 否 | 关键词 |
| categoryId | number | 否 | 分类ID |
| tagId | number | 否 | 标签ID |
| status | string | 否 | 笔记状态 |

请求示例：

```text
GET /api/notes/my?pageNo=1&pageSize=10&status=DRAFT
```

### 4.5 公开笔记分页

| 项目 | 内容 |
| --- | --- |
| 请求路径 | `/api/notes/public` |
| 请求方式 | GET |
| 权限要求 | 匿名可访问 |
| 业务说明 | 查询 PUBLISHED 状态公开笔记 |

### 4.6 笔记详情

| 项目 | 内容 |
| --- | --- |
| 请求路径 | `/api/notes/{id}` |
| 请求方式 | GET |
| 权限要求 | 公开笔记匿名可看，私有笔记仅作者或管理员 |

### 4.7 提交 AI 处理

| 项目 | 内容 |
| --- | --- |
| 请求路径 | `/api/notes/{id}/submit-ai` |
| 请求方式 | PUT |
| 权限要求 | 笔记作者 |
| 业务说明 | DRAFT 或 AI_FINISHED 可提交，生成 AI 结果后状态变为 AI_FINISHED |

### 4.8 发布笔记

| 项目 | 内容 |
| --- | --- |
| 请求路径 | `/api/notes/{id}/publish` |
| 请求方式 | PUT |
| 权限要求 | 笔记作者 |
| 业务说明 | AI_FINISHED 状态可发布为 PUBLISHED |

### 4.9 归档笔记

| 项目 | 内容 |
| --- | --- |
| 请求路径 | `/api/notes/{id}/archive` |
| 请求方式 | PUT |
| 权限要求 | 笔记作者 |
| 业务说明 | PUBLISHED 状态可归档为 ARCHIVED |

## 5. 分类接口

### 5.1 查询分类列表

| 项目 | 内容 |
| --- | --- |
| 请求路径 | `/api/categories` |
| 请求方式 | GET |
| 权限要求 | 匿名可访问 |

### 5.2 管理员新增分类

| 项目 | 内容 |
| --- | --- |
| 请求路径 | `/api/admin/categories` |
| 请求方式 | POST |
| 权限要求 | ADMIN |

请求示例：

```json
{
  "name": "操作系统",
  "description": "进程、线程、内存管理",
  "sortOrder": 10
}
```

### 5.3 管理员编辑分类

| 项目 | 内容 |
| --- | --- |
| 请求路径 | `/api/admin/categories/{id}` |
| 请求方式 | PUT |
| 权限要求 | ADMIN |

### 5.4 管理员删除分类

| 项目 | 内容 |
| --- | --- |
| 请求路径 | `/api/admin/categories/{id}` |
| 请求方式 | DELETE |
| 权限要求 | ADMIN |

## 6. 标签接口

| 接口名称 | 请求路径 | 方法 | 权限 | 说明 |
| --- | --- | --- | --- | --- |
| 标签列表 | `/api/tags` | GET | 匿名 | 查询全部标签 |
| 热门标签 | `/api/tags/hot` | GET | 匿名 | 按使用次数查询 |
| 新增标签 | `/api/admin/tags` | POST | ADMIN | 管理员新增 |
| 编辑标签 | `/api/admin/tags/{id}` | PUT | ADMIN | 管理员编辑 |
| 删除标签 | `/api/admin/tags/{id}` | DELETE | ADMIN | 管理员删除 |

请求示例：

```json
{
  "name": "MyBatis Plus",
  "color": "#67C23A"
}
```

## 7. AI 接口

### 7.1 AI 摘要

| 项目 | 内容 |
| --- | --- |
| 请求路径 | `/api/ai/summary` |
| 请求方式 | POST |
| 权限要求 | 登录用户 |
| 业务说明 | 根据标题和正文生成摘要、学习重点、考试重点和学习建议 |

请求示例：

```json
{
  "title": "MySQL 索引",
  "content": "索引可以提高查询效率，常见结构包括 B+Tree..."
}
```

响应示例：

```json
{
  "code": 200,
  "data": {
    "summary": "本笔记围绕 MySQL 索引展开...",
    "keyPoints": "B+Tree、最左前缀、覆盖索引",
    "examPoints": "索引失效场景、Explain 分析",
    "suggestion": "结合 SQL 示例进行练习"
  }
}
```

### 7.2 AI 标签推荐

| 项目 | 内容 |
| --- | --- |
| 请求路径 | `/api/ai/tags` |
| 请求方式 | POST |
| 权限要求 | 登录用户 |

响应示例：

```json
{
  "code": 200,
  "data": ["MySQL", "数据库设计", "SQL优化"]
}
```

### 7.3 AI 问答助手

| 项目 | 内容 |
| --- | --- |
| 请求路径 | `/api/ai/chat` |
| 请求方式 | POST |
| 权限要求 | 登录用户 |

请求示例：

```json
{
  "question": "如何理解 JWT 认证流程？",
  "context": "用户正在复习 Spring Security"
}
```

### 7.4 AI 学习建议

| 项目 | 内容 |
| --- | --- |
| 请求路径 | `/api/ai/study-advice` |
| 请求方式 | GET |
| 权限要求 | 登录用户 |

### 7.5 查询个人 AI API 配置

| 项目 | 内容 |
| --- | --- |
| 接口名称 | 查询个人 AI API 配置 |
| 请求路径 | `/api/ai/config` |
| 请求方式 | GET |
| 权限要求 | 登录用户 |
| 业务说明 | 查询当前用户的个人 AI 配置。如果个人配置不存在、停用或真实模式未填写 Key，返回结果中的 `usingGlobalFallback` 为 `true`，表示 AI 调用会使用管理员全局配置兜底。 |

响应示例：
```json
{
  "code": 200,
  "data": {
    "scope": "USER",
    "userId": 2,
    "provider": "DeepSeek",
    "mode": "real",
    "apiBaseUrl": "https://api.deepseek.com/v1",
    "maskedApiKey": "",
    "apiKeyConfigured": false,
    "modelName": "deepseek-chat",
    "temperature": 0.30,
    "mockOnFailure": true,
    "enabled": 0,
    "usingGlobalFallback": true
  }
}
```

### 7.6 保存个人 AI API 配置

| 项目 | 内容 |
| --- | --- |
| 接口名称 | 保存个人 AI API 配置 |
| 请求路径 | `/api/ai/config` |
| 请求方式 | PUT |
| 权限要求 | 登录用户 |
| 业务说明 | 保存当前用户自己的 AI API 配置，只影响当前用户。`apiKey` 留空表示保留原 Key。 |

请求示例：
```json
{
  "provider": "DeepSeek",
  "mode": "real",
  "apiBaseUrl": "https://api.deepseek.com/v1",
  "apiKey": "sk-xxxx",
  "modelName": "deepseek-chat",
  "temperature": 0.30,
  "mockOnFailure": true,
  "enabled": 1,
  "remark": "个人学习使用"
}
```

## 8. 收藏接口

| 接口名称 | 请求路径 | 方法 | 权限 | 说明 |
| --- | --- | --- | --- | --- |
| 收藏笔记 | `/api/favorites/{noteId}` | POST | 登录用户 | 收藏公开笔记 |
| 取消收藏 | `/api/favorites/{noteId}` | DELETE | 登录用户 | 取消收藏 |
| 我的收藏 | `/api/favorites/my` | GET | 登录用户 | 分页查询收藏 |
| 判断是否已收藏 | `/api/favorites/check/{noteId}` | GET | 登录用户 | 返回 true/false |

## 9. 评论接口

### 9.1 发布评论

| 项目 | 内容 |
| --- | --- |
| 请求路径 | `/api/comments` |
| 请求方式 | POST |
| 权限要求 | 登录用户 |
| 业务说明 | 只能评论已发布笔记 |

请求示例：

```json
{
  "noteId": 1,
  "parentId": null,
  "content": "这篇笔记总结得很清晰。"
}
```

### 9.2 查询笔记评论

| 项目 | 内容 |
| --- | --- |
| 请求路径 | `/api/comments/note/{noteId}` |
| 请求方式 | GET |
| 权限要求 | 匿名可访问 |

### 9.3 删除评论

| 项目 | 内容 |
| --- | --- |
| 请求路径 | `/api/comments/{id}` |
| 请求方式 | DELETE |
| 权限要求 | 评论作者或管理员 |

### 9.4 管理员审核评论

| 项目 | 内容 |
| --- | --- |
| 请求路径 | `/api/admin/comments/{id}/audit?status=1` |
| 请求方式 | PUT |
| 权限要求 | ADMIN |

## 10. 公告接口

| 接口名称 | 请求路径 | 方法 | 权限 | 说明 |
| --- | --- | --- | --- | --- |
| 公告列表 | `/api/announcements` | GET | 匿名 | 查询已发布公告 |
| 管理员公告列表 | `/api/admin/announcements` | GET | ADMIN | 查询全部公告 |
| 新增公告 | `/api/admin/announcements` | POST | ADMIN | 发布公告 |
| 编辑公告 | `/api/admin/announcements/{id}` | PUT | ADMIN | 编辑公告 |
| 删除公告 | `/api/admin/announcements/{id}` | DELETE | ADMIN | 删除公告 |

## 11. 统计接口

| 接口名称 | 请求路径 | 方法 | 权限 | 返回说明 |
| --- | --- | --- | --- | --- |
| 用户概览统计 | `/api/statistics/overview` | GET | 登录用户 | 笔记总数、本周新增、收藏数、AI使用次数 |
| 分类统计 | `/api/statistics/category` | GET | 登录用户 | 分类名称和笔记数量 |
| 月度趋势 | `/api/statistics/monthly` | GET | 登录用户 | 最近六个月笔记新增数量 |
| 管理员概览统计 | `/api/admin/statistics/overview` | GET | ADMIN | 全站统计 |

## 12. 管理员接口

| 接口名称 | 请求路径 | 方法 | 权限 | 说明 |
| --- | --- | --- | --- | --- |
| 用户分页 | `/api/admin/user/page` | GET | ADMIN | 查看所有用户 |
| 禁用用户 | `/api/admin/user/{id}/disable` | PUT | ADMIN | 禁用普通用户 |
| 启用用户 | `/api/admin/user/{id}/enable` | PUT | ADMIN | 启用用户 |
| 重置密码 | `/api/admin/user/{id}/reset-password` | PUT | ADMIN | 重置为 123456 |
| 笔记分页 | `/api/admin/notes/page` | GET | ADMIN | 查看所有笔记 |
| 删除违规笔记 | `/api/admin/notes/{id}` | DELETE | ADMIN | 删除笔记 |
| 评论分页 | `/api/admin/comments/page` | GET | ADMIN | 查看评论 |
| 评论审核 | `/api/admin/comments/{id}/audit` | PUT | ADMIN | status=0/1/2 |
| 操作日志分页 | `/api/admin/operation-logs/page` | GET | ADMIN | 查看后台操作日志 |
| 登录日志分页 | `/api/admin/login-logs/page` | GET | ADMIN | 查看登录成功和失败记录 |

### 12.1 查询全局 AI API 配置

| 项目 | 内容 |
| --- | --- |
| 接口名称 | 查询全局 AI API 配置 |
| 请求路径 | `/api/admin/ai-config` |
| 请求方式 | GET |
| 权限要求 | ADMIN |
| 业务说明 | 查询管理员维护的全局 AI 供应商、调用模式、接口地址、模型名称和降级策略。API Key 只返回脱敏结果，不返回明文。普通用户未启用个人配置时会使用该全局配置。 |

响应示例：
```json
{
  "code": 200,
  "data": {
    "id": 1,
    "provider": "DeepSeek",
    "mode": "real",
    "apiBaseUrl": "https://api.deepseek.com/v1",
    "maskedApiKey": "sk-****abcd",
    "apiKeyConfigured": true,
    "modelName": "deepseek-chat",
    "temperature": 0.30,
    "mockOnFailure": true,
    "enabled": 1,
    "remark": "答辩演示使用"
  }
}
```

### 12.2 保存全局 AI API 配置

| 项目 | 内容 |
| --- | --- |
| 接口名称 | 保存全局 AI API 配置 |
| 请求路径 | `/api/admin/ai-config` |
| 请求方式 | PUT |
| 权限要求 | ADMIN |
| 业务说明 | 更新管理员全局真实 AI 调用配置。`apiKey` 留空表示保留原 Key；填写新 Key 后会覆盖旧 Key。 |

请求示例：
```json
{
  "provider": "DeepSeek",
  "mode": "real",
  "apiBaseUrl": "https://api.deepseek.com/v1",
  "apiKey": "sk-xxxx",
  "modelName": "deepseek-chat",
  "temperature": 0.30,
  "mockOnFailure": true,
  "enabled": 1,
  "remark": "真实 AI 问答演示配置"
}
```

响应示例：
```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "provider": "DeepSeek",
    "mode": "real",
    "apiBaseUrl": "https://api.deepseek.com/v1",
    "maskedApiKey": "sk-****xxxx",
    "apiKeyConfigured": true,
    "modelName": "deepseek-chat",
    "mockOnFailure": true,
    "enabled": 1
  }
}
```

## 13. 错误码

| code | 说明 |
| --- | --- |
| 200 | 操作成功 |
| 400 | 请求参数错误 |
| 401 | 未登录或 token 无效 |
| 403 | 无权限访问 |
| 404 | 资源不存在 |
| 5001 | 业务处理失败 |
| 500 | 系统异常 |
