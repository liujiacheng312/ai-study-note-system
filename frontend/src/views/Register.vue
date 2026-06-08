<template>
  <div class="auth-page">
    <section class="auth-card">
      <div class="brand-mark">智</div>
      <h1>注册学习账号</h1>
      <p>创建个人知识库，开始整理课程笔记和复习计划。</p>

      <el-form :model="form" label-position="top">
        <el-form-item label="用户名"><el-input v-model="form.username" size="large" /></el-form-item>
        <el-form-item label="昵称"><el-input v-model="form.nickname" size="large" /></el-form-item>
        <el-form-item label="邮箱"><el-input v-model="form.email" size="large" /></el-form-item>
        <el-form-item label="密码"><el-input v-model="form.password" size="large" type="password" show-password /></el-form-item>
        <el-button type="primary" size="large" class="full" :loading="loading" @click="submit">注册</el-button>
        <el-button size="large" class="full plain" @click="$router.push('/login')">返回登录</el-button>
      </el-form>
    </section>
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
  padding: 32px;
  background:
    linear-gradient(135deg, rgba(37, 99, 235, .12), transparent 34%),
    linear-gradient(315deg, rgba(15, 159, 143, .16), transparent 38%),
    #f7f9fc;
}

.auth-card {
  width: min(460px, 100%);
  padding: 40px;
  background: #fff;
  border: 1px solid #e2e8f0;
  border-radius: 8px;
  box-shadow: 0 24px 70px rgba(31, 41, 55, .16);
}

.brand-mark {
  width: 42px;
  height: 42px;
  display: grid;
  place-items: center;
  margin-bottom: 18px;
  color: #0f172a;
  background: linear-gradient(135deg, #e0f2fe, #5eead4);
  border-radius: 8px;
  font-weight: 800;
}

h1 {
  margin: 0;
  font-size: 27px;
}

p {
  margin: 8px 0 26px;
  color: #697589;
}

.full {
  width: 100%;
  margin-left: 0;
  margin-top: 10px;
}

.plain {
  margin-top: 12px;
}
</style>
