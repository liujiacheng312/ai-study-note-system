<template>
  <el-container class="layout">
    <el-aside width="244px" class="aside">
      <div class="brand">
        <div class="brand-mark">智</div>
        <div>
          <div class="brand-title">智学 AI 笔记</div>
          <div class="brand-subtitle">Study Note System</div>
        </div>
      </div>

      <el-scrollbar class="menu-scroll">
        <el-menu router :default-active="$route.path" class="menu">
          <div class="menu-section">学习空间</div>
          <el-menu-item index="/dashboard"><el-icon><DataBoard /></el-icon><span>学习看板</span></el-menu-item>
          <el-menu-item index="/notes/my"><el-icon><Notebook /></el-icon><span>我的笔记</span></el-menu-item>
          <el-menu-item index="/notes/create"><el-icon><EditPen /></el-icon><span>新建笔记</span></el-menu-item>
          <el-menu-item index="/notes/public"><el-icon><Reading /></el-icon><span>公开广场</span></el-menu-item>
          <el-menu-item index="/favorites"><el-icon><Star /></el-icon><span>收藏中心</span></el-menu-item>

          <div class="menu-section">智能辅助</div>
          <el-menu-item index="/ai/chat"><el-icon><ChatDotRound /></el-icon><span>AI 助手</span></el-menu-item>
          <el-menu-item index="/ai/config"><el-icon><Setting /></el-icon><span>AI 配置</span></el-menu-item>
          <el-menu-item index="/statistics"><el-icon><TrendCharts /></el-icon><span>学习统计</span></el-menu-item>

          <el-sub-menu v-if="auth.isAdmin" index="/admin">
            <template #title><el-icon><Tools /></el-icon><span>后台管理</span></template>
            <el-menu-item index="/admin/users">用户管理</el-menu-item>
            <el-menu-item index="/admin/notes">笔记管理</el-menu-item>
            <el-menu-item index="/admin/categories">分类管理</el-menu-item>
            <el-menu-item index="/admin/tags">标签管理</el-menu-item>
            <el-menu-item index="/admin/announcements">公告管理</el-menu-item>
            <el-menu-item index="/admin/ai-config">全局 AI 配置</el-menu-item>
          </el-sub-menu>
        </el-menu>
      </el-scrollbar>
    </el-aside>

    <el-container>
      <el-header class="header">
        <div>
          <div class="header-title">AI Smart Study Note System</div>
          <div class="header-subtitle">笔记整理、智能问答、复习统计一站完成</div>
        </div>
        <el-dropdown>
          <span class="user">
            <el-avatar :size="34" :src="auth.user?.avatar">{{ avatarText }}</el-avatar>
            <span class="user-name">{{ auth.user?.nickname || auth.user?.username }}</span>
          </span>
          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item @click="$router.push('/profile')">个人中心</el-dropdown-item>
              <el-dropdown-item @click="$router.push('/ai/config')">AI 配置</el-dropdown-item>
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
import { computed } from 'vue'
import { ChatDotRound, DataBoard, EditPen, Notebook, Reading, Setting, Star, Tools, TrendCharts } from '@element-plus/icons-vue'
import { useAuthStore } from '@/store/auth'
import router from '@/router'

const auth = useAuthStore()
const avatarText = computed(() => (auth.user?.nickname || auth.user?.username || 'U').slice(0, 1))

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
  position: sticky;
  top: 0;
  height: 100vh;
  color: #eef4ff;
  background:
    linear-gradient(180deg, #13202e 0%, #172433 48%, #13251f 100%);
  border-right: 1px solid rgba(255, 255, 255, .08);
}

.brand {
  height: 72px;
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 0 18px;
  border-bottom: 1px solid rgba(255, 255, 255, .08);
}

.brand-mark {
  width: 38px;
  height: 38px;
  display: grid;
  place-items: center;
  color: #0f172a;
  background: linear-gradient(135deg, #f8fafc, #7dd3fc 58%, #5eead4);
  border-radius: 8px;
  font-weight: 800;
}

.brand-title {
  font-size: 17px;
  font-weight: 800;
  letter-spacing: 0;
}

.brand-subtitle {
  margin-top: 3px;
  color: #9fb0c5;
  font-size: 12px;
}

.menu-scroll {
  height: calc(100vh - 72px);
  padding: 12px 10px 18px;
}

.menu {
  border-right: 0;
  background: transparent;
}

.menu-section {
  margin: 14px 10px 6px;
  color: #8ea1b8;
  font-size: 12px;
  font-weight: 700;
}

:deep(.el-menu-item),
:deep(.el-sub-menu__title) {
  height: 42px;
  margin: 3px 0;
  color: #cfdae7;
  border-radius: 8px;
}

:deep(.el-menu-item:hover),
:deep(.el-sub-menu__title:hover) {
  color: #fff;
  background: rgba(255, 255, 255, .08);
}

:deep(.el-menu-item.is-active) {
  color: #fff;
  background: linear-gradient(90deg, #2563eb, #0f9f8f);
  box-shadow: 0 10px 22px rgba(37, 99, 235, .24);
}

:deep(.el-sub-menu .el-menu-item) {
  min-width: 0;
  padding-left: 42px !important;
}

.header {
  height: 72px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 26px;
  background: rgba(255, 255, 255, .88);
  border-bottom: 1px solid #e6ebf3;
  backdrop-filter: blur(12px);
}

.header-title {
  color: #172033;
  font-size: 17px;
  font-weight: 800;
}

.header-subtitle {
  margin-top: 4px;
  color: #697589;
  font-size: 13px;
}

.user {
  display: inline-flex;
  align-items: center;
  gap: 10px;
  cursor: pointer;
}

.user-name {
  max-width: 160px;
  color: #243047;
  font-weight: 700;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.main {
  min-height: calc(100vh - 72px);
  padding: 24px;
  background:
    linear-gradient(180deg, rgba(255, 255, 255, .58), rgba(246, 248, 251, .5)),
    repeating-linear-gradient(135deg, rgba(37, 99, 235, .025) 0 1px, transparent 1px 12px);
}

@media (max-width: 900px) {
  .aside {
    width: 206px !important;
  }

  .header {
    padding: 0 16px;
  }

  .header-subtitle {
    display: none;
  }

  .main {
    padding: 16px;
  }
}

@media (max-width: 700px) {
  .layout {
    display: block;
  }

  .aside {
    position: relative;
    width: 100% !important;
    height: auto;
  }

  .brand {
    height: 60px;
  }

  .menu-scroll {
    height: auto;
    padding: 8px;
  }

  .menu-section {
    margin-top: 10px;
  }

  .header {
    height: auto;
    min-height: 62px;
  }

  .user-name {
    display: none;
  }

  .main {
    min-height: auto;
  }
}
</style>
