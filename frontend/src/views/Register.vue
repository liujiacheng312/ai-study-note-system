<template>
  <div class="auth-page">
    <div class="auth-box">
      <h1>注册学习账号</h1>
      <el-form :model="form" label-position="top">
        <el-form-item label="用户名"><el-input v-model="form.username" /></el-form-item>
        <el-form-item label="昵称"><el-input v-model="form.nickname" /></el-form-item>
        <el-form-item label="邮箱"><el-input v-model="form.email" /></el-form-item>
        <el-form-item label="密码"><el-input v-model="form.password" type="password" show-password /></el-form-item>
        <el-button type="primary" class="full" :loading="loading" @click="submit">注册</el-button>
        <el-button class="full plain" @click="$router.push('/login')">返回登录</el-button>
      </el-form>
    </div>
  </div>
</template>

<script setup lang="ts">
import { reactive, ref } from 'vue'
import { ElMessage } from 'element-plus'
import { registerApi } from '@/api/auth'
import router from '@/router'

const loading = ref(false)
const form = reactive({ username: '', nickname: '', email: '', password: '' })

async function submit() {
  loading.value = true
  try {
    await registerApi(form)
    ElMessage.success('注册成功，请登录')
    router.push('/login')
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
  background: linear-gradient(135deg, #eef2ff, #f8fafc 45%, #ecfeff);
}
.auth-box {
  width: 420px;
  background: #fff;
  border: 1px solid #e5e7eb;
  border-radius: 8px;
  padding: 30px;
}
h1 {
  margin: 0 0 22px;
}
.full {
  width: 100%;
  margin-left: 0;
  margin-top: 10px;
}
</style>
