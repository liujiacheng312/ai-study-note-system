<template>
  <el-container class="layout">
    <el-aside width="232px" class="aside">
      <div class="brand">智学 AI 笔记</div>
      <el-menu router :default-active="$route.path" class="menu">
        <el-menu-item index="/dashboard"><el-icon><DataBoard /></el-icon><span>学习看板</span></el-menu-item>
        <el-menu-item index="/notes/my"><el-icon><Notebook /></el-icon><span>我的笔记</span></el-menu-item>
        <el-menu-item index="/notes/create"><el-icon><EditPen /></el-icon><span>新建笔记</span></el-menu-item>
        <el-menu-item index="/notes/public"><el-icon><Reading /></el-icon><span>公开广场</span></el-menu-item>
        <el-menu-item index="/favorites"><el-icon><Star /></el-icon><span>收藏中心</span></el-menu-item>
        <el-menu-item index="/ai/chat"><el-icon><ChatDotRound /></el-icon><span>AI 助手</span></el-menu-item>
        <el-menu-item index="/ai/config"><el-icon><Setting /></el-icon><span>AI 配置</span></el-menu-item>
        <el-menu-item index="/statistics"><el-icon><TrendCharts /></el-icon><span>学习统计</span></el-menu-item>
        <el-sub-menu v-if="auth.isAdmin" index="/admin">
          <template #title><el-icon><Setting /></el-icon><span>后台管理</span></template>
          <el-menu-item index="/admin/users">用户管理</el-menu-item>
          <el-menu-item index="/admin/notes">笔记管理</el-menu-item>
          <el-menu-item index="/admin/categories">分类管理</el-menu-item>
          <el-menu-item index="/admin/tags">标签管理</el-menu-item>
          <el-menu-item index="/admin/announcements">公告管理</el-menu-item>
          <el-menu-item index="/admin/ai-config">AI 配置</el-menu-item>
        </el-sub-menu>
      </el-menu>
    </el-aside>
    <el-container>
      <el-header class="header">
        <div class="header-title">AI Smart Study Note System</div>
        <el-dropdown>
          <span class="user">
            <el-avatar :size="30" :src="auth.user?.avatar">{{ auth.user?.nickname?.slice(0, 1) }}</el-avatar>
            {{ auth.user?.nickname || auth.user?.username }}
          </span>
          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item @click="$router.push('/profile')">个人中心</el-dropdown-item>
              <el-dropdown-item divided @click="logout">退出登录</el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
      </el-header>
      <el-main class="main">
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup lang="ts">
import { ChatDotRound, DataBoard, EditPen, Notebook, Reading, Setting, Star, TrendCharts } from '@element-plus/icons-vue'
import { useAuthStore } from '@/store/auth'
import router from '@/router'

const auth = useAuthStore()

function logout() {
  auth.logout()
  router.push('/login')
}
</script>

<style scoped>
.layout {
  min-height: 100vh;
}
.aside {
  background: #172033;
}
.brand {
  height: 58px;
  display: flex;
  align-items: center;
  padding-left: 22px;
  color: #fff;
  font-size: 18px;
  font-weight: 700;
  border-bottom: 1px solid rgba(255,255,255,.08);
}
.menu {
  border-right: 0;
  background: transparent;
}
:deep(.el-menu-item), :deep(.el-sub-menu__title) {
  color: #cbd5e1;
}
:deep(.el-menu-item.is-active) {
  color: #fff;
  background: #2563eb;
}
.header {
  height: 58px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  background: #fff;
  border-bottom: 1px solid #e5e7eb;
}
.header-title {
  font-weight: 600;
}
.user {
  display: inline-flex;
  align-items: center;
  gap: 8px;
  cursor: pointer;
}
.main {
  background: #f5f7fb;
  padding: 20px;
}
</style>
