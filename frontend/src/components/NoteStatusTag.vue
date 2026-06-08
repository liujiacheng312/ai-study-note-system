<template>
  <el-tag :type="typeMap[status] || 'info'">{{ labelMap[status] || status }}</el-tag>
</template>

<script setup lang="ts">
defineProps<{ status: string }>()

const labelMap: Record<string, string> = {
  DRAFT: '草稿',
  WAITING_AI: '等待 AI',
  AI_FINISHED: 'AI 完成',
  PUBLISHED: '已发布',
  ARCHIVED: '已归档',
  DELETED: '已删除'
}

const typeMap: Record<string, string> = {
  DRAFT: 'info',
  WAITING_AI: 'warning',
  AI_FINISHED: 'success',
  PUBLISHED: 'primary',
  ARCHIVED: 'info',
  DELETED: 'danger'
}
</script>
