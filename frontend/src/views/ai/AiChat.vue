<template>
  <div class="page">
    <div class="toolbar">
      <div>
        <h2>AI 问答助手</h2>
        <p class="muted">结合课程笔记进行知识点解释、考试重点梳理和复习建议生成。</p>
      </div>
      <el-button @click="$router.push('/ai/config')">配置 API</el-button>
    </div>

    <div class="panel chat-panel">
      <div class="messages">
        <div v-for="(item, index) in messages" :key="index" :class="['message', item.role]">
          <div class="avatar">{{ item.role === 'user' ? '我' : 'AI' }}</div>
          <div class="bubble">
            <p>{{ item.content }}</p>
            <span v-if="item.modelName" class="model-name">Model: {{ item.modelName }}</span>
          </div>
        </div>
      </div>

      <div class="composer">
        <el-input
          v-model="question"
          type="textarea"
          :rows="3"
          resize="none"
          placeholder="输入学习问题，例如：如何理解 JWT 认证流程？"
        />
        <el-button type="primary" :loading="loading" @click="send">发送</el-button>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { aiApi } from '@/api/ai'

interface ChatMessage {
  role: 'user' | 'ai'
  content: string
  modelName?: string
}

const question = ref('')
const loading = ref(false)
const messages = ref<ChatMessage[]>([
  { role: 'ai', content: '你好，我可以帮你总结知识点、提取考试重点，也可以回答课程复习问题。' }
])

async function send() {
  if (!question.value.trim()) return
  const q = question.value
  messages.value.push({ role: 'user', content: q })
  question.value = ''
  loading.value = true
  try {
    const res = await aiApi.chat({
      question: q,
      context: messages.value.slice(-6).map(i => `${i.role}:${i.content}`).join('\n')
    })
    messages.value.push({ role: 'ai', content: res.data.answer, modelName: res.data.modelName })
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.chat-panel {
  min-height: calc(100vh - 190px);
  display: flex;
  flex-direction: column;
  padding: 0;
  overflow: hidden;
}

.messages {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 16px;
  padding: 22px;
  background:
    linear-gradient(180deg, #ffffff, #f8fafc);
}

.message {
  display: flex;
  gap: 10px;
  max-width: 820px;
}

.message.user {
  align-self: flex-end;
  flex-direction: row-reverse;
}

.avatar {
  width: 34px;
  height: 34px;
  display: grid;
  place-items: center;
  flex: 0 0 auto;
  color: #fff;
  background: #172033;
  border-radius: 8px;
  font-weight: 800;
}

.message.user .avatar {
  background: #2563eb;
}

.bubble {
  padding: 13px 15px;
  background: #fff;
  border: 1px solid #e5eaf2;
  border-radius: 8px;
  box-shadow: 0 8px 18px rgba(31, 41, 55, .06);
}

.message.user .bubble {
  color: #fff;
  background: linear-gradient(135deg, #2563eb, #1d4ed8);
  border-color: transparent;
}

.bubble p {
  margin: 0;
  line-height: 1.75;
  white-space: pre-wrap;
}

.model-name {
  display: inline-block;
  margin-top: 9px;
  color: #64748b;
  font-size: 12px;
}

.message.user .model-name {
  color: rgba(255,255,255,.75);
}

.composer {
  display: grid;
  grid-template-columns: 1fr 108px;
  gap: 12px;
  align-items: end;
  padding: 16px;
  background: #fff;
  border-top: 1px solid #e5eaf2;
}

@media (max-width: 700px) {
  .composer {
    grid-template-columns: 1fr;
  }
}
</style>
