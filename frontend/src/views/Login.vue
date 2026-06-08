<template>
  <div class="auth-page">
    <div class="auth-box">
      <h1>智学 AI 学习笔记系统</h1>
      <p>使用课程账号登录，继续整理你的学习知识库。</p>
      <el-form :model="form" label-position="top" @keyup.enter="submit">
        <el-form-item label="用户名"><el-input v-model="form.username" /></el-form-item>
        <el-form-item label="密码"><el-input v-model="form.password" type="password" show-password /></el-form-item>
        <el-button type="primary" class="full" :loading="loading" @click="submit">登录</el-button>
        <el-button class="full plain" @click="$router.push('/register')">注册新账号</el-button>
      </el-form>
      <div class="hint">测试账号：admin / 123456，user / 123456</div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { reactive, ref } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { useAuthStore } from '@/store/auth'

const router = useRouter()
const auth = useAuthStore()
const loading = ref(false)
const form = reactive({ username: 'user', password: '123456' })

async function submit() {
  loading.value = true
  try {
    await auth.login(form)
    ElMessage.success('登录成功')
    router.push('/dashboard')
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.auth-page {
  min-height: 100vh;
  display: grid;
  place-items: center;
  background: linear-gradient(135deg, #e0f2fe 0%, #f8fafc 45%, #ecfdf5 100%);
}
.auth-box {
  width: 420px;
  background: #fff;
  border: 1px solid #e5e7eb;
  border-radius: 8px;
  padding: 30px;
  box-shadow: 0 20px 45px rgba(15, 23, 42, .12);
}
h1 {
  margin: 0 0 8px;
  font-size: 26px;
}
p {
  margin: 0 0 24px;
  color: #64748b;
}
.full {
  width: 100%;
  margin-left: 0;
  margin-top: 10px;
}
.plain {
  margin-top: 12px;
}
.hint {
  margin-top: 16px;
  color: #64748b;
  font-size: 13px;
}
</style>
