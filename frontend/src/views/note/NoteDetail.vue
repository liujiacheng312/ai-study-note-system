<template>
  <div class="page">
    <div class="panel">
      <div class="toolbar">
        <div>
          <h2>{{ note.title }}</h2>
          <NoteStatusTag v-if="note.status" :status="note.status" />
        </div>
        <div>
          <el-button v-if="note.status === 'PUBLISHED'" @click="favorite">收藏</el-button>
          <el-button v-if="note.status === 'PUBLISHED'" type="warning" @click="archive">归档</el-button>
        </div>
      </div>
      <el-divider />
      <MdPreview :model-value="note.content || ''" />
    </div>
    <div v-if="note.summary" class="panel">
      <h3>AI 学习辅助</h3>
      <el-descriptions :column="1" border>
        <el-descriptions-item label="摘要">{{ note.summary }}</el-descriptions-item>
        <el-descriptions-item label="学习重点">{{ note.aiKeyPoints }}</el-descriptions-item>
        <el-descriptions-item label="考试重点">{{ note.aiExamPoints }}</el-descriptions-item>
        <el-descriptions-item label="学习建议">{{ note.aiSuggestion }}</el-descriptions-item>
      </el-descriptions>
    </div>
    <div v-if="note.status === 'PUBLISHED'" class="panel">
      <h3>评论互动</h3>
      <el-input v-model="commentText" type="textarea" :rows="3" placeholder="写下你的讨论观点" />
      <el-button type="primary" style="margin-top:10px" @click="sendComment">发表评论</el-button>
      <el-timeline style="margin-top:18px">
        <el-timeline-item v-for="item in comments" :key="item.id" :timestamp="item.createTime">{{ item.content }}</el-timeline-item>
      </el-timeline>
    </div>
  </div>
</template>

<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'
import { MdPreview } from 'md-editor-v3'
import NoteStatusTag from '@/components/NoteStatusTag.vue'
import { noteApi } from '@/api/note'
import { commentApi, favoriteApi } from '@/api/social'

const route = useRoute()
const note = ref<any>({})
const comments = ref<any[]>([])
const commentText = ref('')

async function load() {
  note.value = (await noteApi.detail(Number(route.params.id))).data
  if (note.value.status === 'PUBLISHED') {
    comments.value = (await commentApi.note(note.value.id)).data
  }
}

async function favorite() {
  await favoriteApi.add(note.value.id)
  ElMessage.success('已收藏')
}

async function archive() {
  await noteApi.archive(note.value.id)
  ElMessage.success('已归档')
  load()
}

async function sendComment() {
  await commentApi.create({ noteId: note.value.id, content: commentText.value })
  commentText.value = ''
  ElMessage.success('评论成功')
  load()
}

onMounted(load)
</script>
