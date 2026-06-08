# 数据库设计说明

## 1. 数据库基本信息

- 数据库名称：`ai_study_note`
- 数据库版本：MySQL 8
- 字符集：`utf8mb4`
- 排序规则：`utf8mb4_unicode_ci`

## 2. 数据表清单

| 表名 | 中文说明 | 主要用途 |
| --- | --- | --- |
| sys_user | 系统用户表 | 保存用户账号、密码、角色和状态 |
| sys_role | 系统角色表 | 保存 ADMIN、USER 等角色 |
| sys_user_role | 用户角色关联表 | 用户与角色多对多关系 |
| category | 分类表 | 笔记分类管理 |
| tag | 标签表 | 笔记标签管理和热门标签统计 |
| note | 笔记表 | 保存笔记正文、AI 结果和状态 |
| note_tag | 笔记标签关联表 | 笔记与标签多对多关系 |
| favorite | 收藏表 | 用户收藏公开笔记 |
| comment | 评论表 | 笔记评论和回复 |
| announcement | 公告表 | 管理员发布系统公告 |
| ai_config | AI 配置表 | 保存管理员全局 AI 配置和普通用户个人 AI 配置 |
| ai_chat_record | AI 问答记录表 | 保存用户问题和 AI 回答 |
| ai_usage_log | AI 使用日志表 | 统计 AI 调用类型和次数 |
| operation_log | 操作日志表 | 保存后台操作记录 |
| login_log | 登录日志表 | 保存登录成功和失败记录 |

## 3. 核心字段说明

### sys_user

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| id | BIGINT | 用户ID |
| username | VARCHAR(50) | 用户名，唯一 |
| password | VARCHAR(100) | BCrypt 加密密码 |
| nickname | VARCHAR(50) | 昵称 |
| email | VARCHAR(100) | 邮箱 |
| avatar | VARCHAR(255) | 头像地址 |
| status | TINYINT | 1 启用，0 禁用 |
| role | VARCHAR(20) | USER 或 ADMIN |

### note

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| id | BIGINT | 笔记ID |
| user_id | BIGINT | 作者用户ID |
| category_id | BIGINT | 分类ID |
| title | VARCHAR(150) | 笔记标题 |
| content | MEDIUMTEXT | Markdown 正文 |
| summary | TEXT | AI 摘要 |
| ai_key_points | TEXT | AI 学习重点 |
| ai_exam_points | TEXT | AI 考试重点 |
| ai_suggestion | TEXT | AI 学习建议 |
| status | VARCHAR(30) | 笔记状态 |
| view_count | INT | 浏览数 |
| favorite_count | INT | 收藏数 |
| comment_count | INT | 评论数 |

## 4. ER 图说明

系统 ER 关系可描述如下：

- 一个用户可以创建多篇笔记，`sys_user.id` 与 `note.user_id` 是一对多关系。
- 一个分类可以包含多篇笔记，`category.id` 与 `note.category_id` 是一对多关系。
- 一篇笔记可以绑定多个标签，一个标签也可以绑定多篇笔记，二者通过 `note_tag` 形成多对多关系。
- 一个用户可以收藏多篇笔记，一篇笔记也可以被多个用户收藏，二者通过 `favorite` 形成多对多关系。
- 一篇公开笔记可以有多条评论，`note.id` 与 `comment.note_id` 是一对多关系。
- 一个用户可以产生多条 AI 问答记录和 AI 使用日志。
- `ai_config.scope=GLOBAL,user_id=0` 表示管理员全局配置；`scope=USER,user_id=用户ID` 表示普通用户个人配置。AI 调用时优先使用个人配置，个人配置不可用时使用全局配置。

## 5. 外键关系

| 外键 | 关联 |
| --- | --- |
| sys_user_role.user_id | sys_user.id |
| sys_user_role.role_id | sys_role.id |
| note.user_id | sys_user.id |
| note.category_id | category.id |
| note_tag.note_id | note.id |
| note_tag.tag_id | tag.id |
| favorite.user_id | sys_user.id |
| favorite.note_id | note.id |
| comment.note_id | note.id |
| comment.user_id | sys_user.id |
| ai_chat_record.user_id | sys_user.id |
| ai_usage_log.user_id | sys_user.id |

## 6. 初始化数据

`database/data.sql` 中包含：

- 管理员账号：admin / 123456
- 普通用户账号：user / 123456
- 测试分类：Java 后端、前端开发、数据库、人工智能、软件工程
- 测试标签：Spring Boot、Vue3、MySQL、JWT、AI 摘要、课程复习
- 测试笔记、测试公告、收藏评论和 AI 使用日志
