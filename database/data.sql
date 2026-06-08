USE ai_study_note;

INSERT INTO sys_role (id, role_code, role_name) VALUES
(1, 'ADMIN', '系统管理员'),
(2, 'USER', '普通用户');

INSERT INTO sys_user (id, username, password, nickname, email, avatar, status, role) VALUES
(1, 'admin', '$2b$10$UFAJXkcv2nlfCuyjGKE34OVxgxi9CJeOmR4ZRypN.P8zGK37d4YEK', '系统管理员', 'admin@example.com', 'https://api.dicebear.com/7.x/initials/svg?seed=admin', 1, 'ADMIN'),
(2, 'user', '$2b$10$UFAJXkcv2nlfCuyjGKE34OVxgxi9CJeOmR4ZRypN.P8zGK37d4YEK', '测试用户', 'user@example.com', 'https://api.dicebear.com/7.x/initials/svg?seed=user', 1, 'USER');

INSERT INTO sys_user_role (user_id, role_id) VALUES (1, 1), (2, 2);

INSERT INTO category (id, name, description, sort_order) VALUES
(1, 'Java 后端', 'Spring Boot、数据库、接口开发等后端知识', 1),
(2, '前端开发', 'Vue3、TypeScript、组件化和页面交互', 2),
(3, '数据库', 'MySQL、SQL优化、ER建模', 3),
(4, '人工智能', 'AI工具、大模型应用与提示词实践', 4),
(5, '软件工程', '需求分析、设计、测试、部署和版本管理', 5);

INSERT INTO tag (id, name, color, use_count) VALUES
(1, 'Spring Boot', '#67C23A', 2),
(2, 'Vue3', '#409EFF', 1),
(3, 'MySQL', '#E6A23C', 1),
(4, 'JWT', '#F56C6C', 1),
(5, 'AI 摘要', '#9254DE', 1),
(6, '课程复习', '#13C2C2', 1);

INSERT INTO note (id, user_id, category_id, title, content, summary, ai_key_points, ai_exam_points, ai_suggestion, status, view_count, favorite_count, comment_count) VALUES
(1, 2, 1, 'Spring Boot 分层架构学习笔记', '# Spring Boot 分层架构\n\nController 负责接收请求，Service 负责业务逻辑，Mapper 负责数据访问。统一返回结果和统一异常处理可以提高接口一致性。', '本文总结了 Spring Boot 项目常见分层结构及统一接口设计。', 'Controller、Service、Mapper 分层；Result 统一响应；异常集中处理。', 'RESTful 接口设计、JWT认证、MyBatis Plus分页。', '建议结合实际项目画出调用链并完成接口联调。', 'PUBLISHED', 35, 1, 1),
(2, 2, 4, 'AI 工具辅助学习方法', '# AI 工具辅助学习\n\n使用 AI 可以快速生成摘要、提取重点和辅助答疑，但需要人工校验结果，不能直接替代思考。', 'AI 可用于学习摘要、标签推荐和问答辅助。', 'AI是辅助工具；输出需要人工验证；适合提高复习效率。', 'AI应用场景、人机协作反思、提示词设计。', '建议保留自己的理解过程，并对AI输出进行二次整理。', 'AI_FINISHED', 12, 0, 0);

INSERT INTO note_tag (note_id, tag_id) VALUES
(1, 1), (1, 4), (2, 5), (2, 6);

INSERT INTO favorite (user_id, note_id) VALUES (1, 1);

INSERT INTO comment (note_id, user_id, parent_id, content, audit_status) VALUES
(1, 1, NULL, '这篇笔记很适合作为答辩时讲解后端结构的例子。', 1);

INSERT INTO announcement (id, title, content, status) VALUES
(1, '系统试运行通知', '智学 AI 学习笔记系统已完成基础功能开发，欢迎同学们体验笔记管理、AI 摘要和学习统计功能。', 1),
(2, '课程答辩准备提示', '请提前准备项目运行环境、GitHub 提交记录、数据库脚本和演示账号。', 1);

INSERT INTO ai_chat_record (user_id, question, answer, model_name) VALUES
(2, '如何复习 Spring Boot？', '建议从项目结构、接口设计、Spring Security、MyBatis Plus 和部署流程五个方面整理复习提纲。', 'mock-ai');

INSERT INTO ai_usage_log (user_id, note_id, ai_type, input_tokens, output_tokens, status) VALUES
(2, 1, 'SUMMARY', 120, 80, 'SUCCESS'),
(2, 2, 'TAGS', 96, 18, 'SUCCESS'),
(2, NULL, 'CHAT', 40, 36, 'SUCCESS');
