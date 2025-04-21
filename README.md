# QQ市场调研系统 - AI智能客服版（前后端分离）
---
## 🧠 项目简介

**QQ市场调研系统** 是一款融合了**营销管理、销售管理和客户跟进**等功能的 CRM 系统，致力于通过信息化、数字化手段提升企业客户关系管理效率。

当前项目基于前后端分离架构，主要包含两个子模块：

- `dlyk-front`：前端 Vue3 项目（已打包部署）
- `dlyk`：后端 SpringBoot 项目（包含核心业务逻辑）

---

## ✨ 项目特色

- ✅ 多模型 AI 客服（支持 DashScope、OpenAI 等）
- ✅ 智能向量召回聊天上下文
- ✅ 聊天界面美观（仿 ChatGPT 风格，左右气泡分离）
- ✅ 前后端完全分离，支持独立部署

---

## 🧱 技术栈说明

### 🔹 前端

- Vue 3 + Composition API
- Element Plus UI 组件库
- Axios 接口请求工具
- ECharts 图表库（可拓展用于调研可视化）
- HTML5 / CSS3 / JavaScript

### 🔹 后端

- Spring Boot 框架
- Spring Security 权限控制
- MyBatis + MySQL 数据管理
- Redis 缓存 + Spring Data Redis
- JWT 鉴权机制
- EasyExcel（导入导出 Excel）
- HiKariCP（默认数据库连接池）

---

## 📦 项目依赖与开发工具

- 构建工具：Maven
- 数据库：MySQL 8+
- 缓存中间件：Redis 6+
- 部署环境：Windows 
- 开发工具推荐：
    - IntelliJ IDEA（后端）
    - WebStorm（前端）
    - Apifox（接口调试）

---

## 🧪 演示功能（已实现）

- 登录 / 注销
- 用户管理、权限管理
- 客户线索跟踪管理
- 数据导出导入
- 多模型 AI 智能问答
- 聊天历史多会话切换


