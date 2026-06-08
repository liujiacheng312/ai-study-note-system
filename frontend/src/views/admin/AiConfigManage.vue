<template>
  <div class="page">
    <div class="toolbar">
      <h2>AI API 配置</h2>
      <el-tag :type="form.enabled === 1 ? 'success' : 'info'">
        {{ form.enabled === 1 ? '已启用' : '已停用' }}
      </el-tag>
    </div>

    <div class="panel config-panel">
      <el-alert
        title="API Key 只在保存时提交，查询时会脱敏显示。留空 API Key 表示保留原配置。"
        type="info"
        :closable="false"
        show-icon
      />

      <el-form :model="form" label-width="120px" class="config-form">
        <el-form-item label="供应商模板">
          <el-select v-model="selectedPreset" placeholder="选择供应商模板" @change="applyPreset">
            <el-option label="DeepSeek" value="deepseek" />
            <el-option label="OpenAI" value="openai" />
            <el-option label="阿里云百炼" value="aliyun" />
            <el-option label="智谱 AI" value="zhipu" />
            <el-option label="本地模型" value="local" />
          </el-select>
        </el-form-item>
        <el-form-item label="供应商名称">
          <el-input v-model="form.provider" placeholder="DeepSeek / OpenAI / 本地模型" />
        </el-form-item>
        <el-form-item label="调用模式">
          <el-segmented v-model="form.mode" :options="modeOptions" />
        </el-form-item>
        <el-form-item label="接口地址">
          <el-input v-model="form.apiBaseUrl" placeholder="https://api.deepseek.com/v1" />
        </el-form-item>
        <el-form-item label="模型名称">
          <el-input v-model="form.modelName" placeholder="deepseek-chat" />
        </el-form-item>
        <el-form-item label="API Key">
          <el-input v-model="form.apiKey" type="password" show-password placeholder="留空则保留原 Key" />
          <div class="hint">
            当前状态：{{ currentKeyLabel }}
          </div>
        </el-form-item>
        <el-form-item label="生成温度">
          <el-slider v-model="form.temperature" :min="0" :max="1" :step="0.05" show-input />
        </el-form-item>
        <el-form-item label="失败降级">
          <el-switch
            v-model="form.mockOnFailure"
            active-text="失败时使用 mock"
            inactive-text="失败时报错"
          />
        </el-form-item>
        <el-form-item label="是否启用">
          <el-switch v-model="form.enabled" :active-value="1" :inactive-value="0" />
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="form.remark" type="textarea" :rows="3" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :loading="saving" @click="save">保存配置</el-button>
          <el-button @click="load">重新加载</el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script setup lang="ts">
import { computed, onMounted, reactive, ref } from 'vue'
import { ElMessage } from 'element-plus'
import { adminApi } from '@/api/admin'

const selectedPreset = ref('')
const saving = ref(false)
const currentMaskedKey = ref('')
const currentKeyConfigured = ref(false)

const form = reactive({
  provider: 'DeepSeek',
  mode: 'real',
  apiBaseUrl: 'https://api.deepseek.com/v1',
  apiKey: '',
  modelName: 'deepseek-chat',
  temperature: 0.3,
  mockOnFailure: true,
  enabled: 1,
  remark: ''
})

const modeOptions = [
  { label: '真实 API', value: 'real' },
  { label: '模拟返回', value: 'mock' }
]

const presets: Record<string, Partial<typeof form>> = {
  deepseek: { provider: 'DeepSeek', apiBaseUrl: 'https://api.deepseek.com/v1', modelName: 'deepseek-chat' },
  openai: { provider: 'OpenAI', apiBaseUrl: 'https://api.openai.com/v1', modelName: 'gpt-4o-mini' },
  aliyun: { provider: '阿里云百炼', apiBaseUrl: 'https://dashscope.aliyuncs.com/compatible-mode/v1', modelName: 'qwen-turbo' },
  zhipu: { provider: '智谱 AI', apiBaseUrl: 'https://open.bigmodel.cn/api/paas/v4', modelName: 'glm-4' },
  local: { provider: '本地模型', apiBaseUrl: 'http://127.0.0.1:11434/v1', modelName: 'qwen2.5' }
}

const currentKeyLabel = computed(() => {
  if (form.apiKey) return '将更新为新输入的 Key'
  if (currentKeyConfigured.value) return `已配置 ${currentMaskedKey.value}`
  return '未配置'
})

function applyPreset(value: string) {
  Object.assign(form, presets[value])
}

async function load() {
  const res = await adminApi.aiConfig()
  Object.assign(form, {
    provider: res.data.provider,
    mode: res.data.mode,
    apiBaseUrl: res.data.apiBaseUrl,
    apiKey: '',
    modelName: res.data.modelName,
    temperature: Number(res.data.temperature || 0.3),
    mockOnFailure: Boolean(res.data.mockOnFailure),
    enabled: res.data.enabled,
    remark: res.data.remark || ''
  })
  currentMaskedKey.value = res.data.maskedApiKey || ''
  currentKeyConfigured.value = Boolean(res.data.apiKeyConfigured)
}

async function save() {
  saving.value = true
  try {
    const payload = { ...form }
    const res = await adminApi.updateAiConfig(payload)
    form.apiKey = ''
    currentMaskedKey.value = res.data.maskedApiKey || ''
    currentKeyConfigured.value = Boolean(res.data.apiKeyConfigured)
    ElMessage.success('AI API 配置已保存')
  } finally {
    saving.value = false
  }
}

onMounted(load)
</script>

<style scoped>
.config-form {
  max-width: 760px;
  margin-top: 18px;
}
.config-panel {
  background:
    linear-gradient(180deg, rgba(255,255,255,.96), rgba(249,251,253,.96));
}
.hint {
  margin-top: 6px;
  color: #64748b;
  font-size: 13px;
}
</style>
