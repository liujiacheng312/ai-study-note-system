import { createRouter, createWebHistory } from 'vue-router'
import { useAuthStore } from '@/store/auth'

const router = createRouter({
  history: createWebHistory(),
  routes: [
    { path: '/login', component: () => import('@/views/Login.vue') },
    { path: '/register', component: () => import('@/views/Register.vue') },
    {
      path: '/',
      component: () => import('@/layout/BasicLayout.vue'),
      meta: { requiresAuth: true },
      children: [
        { path: '', redirect: '/dashboard' },
        { path: 'dashboard', component: () => import('@/views/Dashboard.vue') },
        { path: 'notes/my', component: () => import('@/views/note/MyNotes.vue') },
        { path: 'notes/create', component: () => import('@/views/note/EditNote.vue') },
        { path: 'notes/edit/:id', component: () => import('@/views/note/EditNote.vue') },
        { path: 'notes/public', component: () => import('@/views/note/PublicNotes.vue') },
        { path: 'notes/:id', component: () => import('@/views/note/NoteDetail.vue') },
        { path: 'favorites', component: () => import('@/views/favorite/Favorites.vue') },
        { path: 'ai/chat', component: () => import('@/views/ai/AiChat.vue') },
        { path: 'statistics', component: () => import('@/views/statistics/Statistics.vue') },
        { path: 'profile', component: () => import('@/views/profile/Profile.vue') },
        { path: 'admin/users', meta: { admin: true }, component: () => import('@/views/admin/UserManage.vue') },
        { path: 'admin/notes', meta: { admin: true }, component: () => import('@/views/admin/NoteManage.vue') },
        { path: 'admin/categories', meta: { admin: true }, component: () => import('@/views/admin/CategoryManage.vue') },
        { path: 'admin/tags', meta: { admin: true }, component: () => import('@/views/admin/TagManage.vue') },
        { path: 'admin/announcements', meta: { admin: true }, component: () => import('@/views/admin/AnnouncementManage.vue') },
        { path: 'admin/ai-config', meta: { admin: true }, component: () => import('@/views/admin/AiConfigManage.vue') }
      ]
    }
  ]
})

router.beforeEach((to, _from, next) => {
  const auth = useAuthStore()
  if (to.meta.requiresAuth && !auth.token) {
    next('/login')
    return
  }
  if (to.meta.admin && !auth.isAdmin) {
    next('/dashboard')
    return
  }
  next()
})

export default router
