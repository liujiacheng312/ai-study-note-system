<template>
  <div class="page">
    <div class="toolbar">
      <h2>AI 问答助手</h2>
      <el-tag type="success" effect="plain">支持真实大模型</el-tag>
    </div>
    <div class="panel chat-panel">
      <div class="messages">
        <div v-for="(item, index) in messages" :key="index" :class="['message', item.role]">
          <strong>{{ item.role === 'user' ? '我' : 'AI' }}</strong>
          <p>{{ item.content }}</p>
          <span v-if="item.modelName" class="model-name">Model: {{ item.modelName }}</span>
        </div>
      </div>
      <div class="composer">
        <el-input v-model="question" type="textarea" :rows="3" placeholder="输入学习问题，例如：如何理解 JWT 认证流程？" />
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
  { role: 'ai', content: '你好，我可以调用真实大模型帮你总结知识点、提取考试重点，也可以回答课程复习问题。' }
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
  min-height: calc(100vh - 160px);
  display: flex;
  flex-direction: column;
}
.messages {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 12px;
}
.message {
  max-width: 760px;
  padding: 12px 14px;
  border-radius: 8px;
  border: 1px solid #e5e7eb;
}
.message.user {
  margin-left: auto;
  background: #eff6ff;
}
.message.ai {
  background: #f8fafc;
}
.message p {
  margin: 6px 0 0;
  line-height: 1.7;
}
.model-name {
  display: inline-block;
  margin-top: 8px;
  color: #64748b;
  font-size: 12px;
}
.composer {
  display: grid;
  grid-template-columns: 1fr 110px;
  gap: 12px;
  align-items: end;
  margin-top: 18px;
}
</style>
