<template>
  <div class="auth-page">
    <section class="auth-panel">
      <div class="auth-card">
        <div class="brand-row">
          <div class="brand-mark">智</div>
          <div>
            <h1>智学 AI 学习笔记系统</h1>
            <p>整理知识、生成摘要、追踪复习进度。</p>
          </div>
        </div>

        <el-form :model="form" label-position="top" @keyup.enter="submit">
          <el-form-item label="用户名"><el-input v-model="form.username" size="large" /></el-form-item>
          <el-form-item label="密码"><el-input v-model="form.password" size="large" type="password" show-password /></el-form-item>
          <el-button type="primary" size="large" class="full" :loading="loading" @click="submit">登录</el-button>
          <el-button size="large" class="full plain" @click="$router.push('/register')">注册新账号</el-button>
        </el-form>

        <div class="hint">
          <span>测试账号</span>
          <strong>admin / 123456</strong>
          <strong>user / 123456</strong>
        </div>
      </div>

      <div class="preview">
        <div class="preview-top">
          <span></span><span></span><span></span>
        </div>
        <div class="preview-title">学习看板</div>
        <div class="preview-grid">
          <div>笔记总数<strong>128</strong></div>
          <div>AI 使用<strong>46</strong></div>
          <div>收藏内容<strong>21</strong></div>
        </div>
        <div class="preview-line wide"></div>
        <div class="preview-line"></div>
        <div class="preview-chart">
          <i style="height: 42%"></i>
          <i style="height: 64%"></i>
          <i style="height: 36%"></i>
          <i style="height: 82%"></i>
          <i style="height: 58%"></i>
        </div>
      </div>
    </section>
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
  padding: 32px;
  background:
    linear-gradient(135deg, rgba(37, 99, 235, .12), transparent 34%),
    linear-gradient(315deg, rgba(15, 159, 143, .16), transparent 38%),
    #f7f9fc;
}

.auth-panel {
  width: min(980px, 100%);
  min-height: 560px;
  display: grid;
  grid-template-columns: 440px 1fr;
  overflow: hidden;
  background: #fff;
  border: 1px solid #e2e8f0;
  border-radius: 8px;
  box-shadow: 0 24px 70px rgba(31, 41, 55, .16);
}

.auth-card {
  display: flex;
  flex-direction: column;
  justify-content: center;
  padding: 42px;
}

.brand-row {
  display: flex;
  gap: 14px;
  align-items: flex-start;
  margin-bottom: 30px;
}

.brand-mark {
  width: 42px;
  height: 42px;
  display: grid;
  place-items: center;
  color: #0f172a;
  background: linear-gradient(135deg, #e0f2fe, #5eead4);
  border-radius: 8px;
  font-weight: 800;
}

h1 {
  margin: 0;
  font-size: 27px;
  line-height: 1.2;
}

p {
  margin: 8px 0 0;
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

.hint {
  display: flex;
  align-items: center;
  gap: 10px;
  flex-wrap: wrap;
  margin-top: 20px;
  color: #697589;
  font-size: 13px;
}

.hint strong {
  color: #172033;
  background: #f3f6fb;
  border: 1px solid #e5eaf2;
  border-radius: 7px;
  padding: 5px 8px;
}

.preview {
  position: relative;
  padding: 38px;
  background:
    linear-gradient(160deg, #13202e 0%, #172433 52%, #12312b 100%);
  color: #fff;
}

.preview-top {
  display: flex;
  gap: 8px;
}

.preview-top span {
  width: 10px;
  height: 10px;
  border-radius: 50%;
  background: rgba(255,255,255,.44);
}

.preview-title {
  margin-top: 48px;
  font-size: 30px;
  font-weight: 800;
}

.preview-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 12px;
  margin-top: 28px;
}

.preview-grid div {
  min-height: 88px;
  padding: 14px;
  color: #cbd5e1;
  background: rgba(255, 255, 255, .08);
  border: 1px solid rgba(255, 255, 255, .12);
  border-radius: 8px;
}

.preview-grid strong {
  display: block;
  margin-top: 12px;
  color: #fff;
  font-size: 28px;
}

.preview-line {
  height: 10px;
  width: 60%;
  margin-top: 24px;
  background: rgba(255, 255, 255, .14);
  border-radius: 999px;
}

.preview-line.wide {
  width: 82%;
}

.preview-chart {
  height: 150px;
  display: flex;
  align-items: flex-end;
  gap: 14px;
  margin-top: 36px;
}

.preview-chart i {
  flex: 1;
  display: block;
  background: linear-gradient(180deg, #7dd3fc, #5eead4);
  border-radius: 7px 7px 0 0;
}

@media (max-width: 860px) {
  .auth-panel {
    grid-template-columns: 1fr;
  }

  .preview {
    display: none;
  }
}
</style>
