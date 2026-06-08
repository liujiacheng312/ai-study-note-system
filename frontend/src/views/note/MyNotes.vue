<template>
  <div class="page">
    <div class="toolbar">
      <h2>我的笔记</h2>
      <el-button type="primary" @click="$router.push('/notes/create')">新建笔记</el-button>
    </div>
    <div class="panel toolbar">
      <el-input v-model="query.keyword" placeholder="搜索标题或正文" style="width:260px" clearable />
      <el-select v-model="query.status" placeholder="状态" style="width:160px" clearable>
        <el-option label="草稿" value="DRAFT" />
        <el-option label="AI完成" value="AI_FINISHED" />
        <el-option label="已发布" value="PUBLISHED" />
        <el-option label="已归档" value="ARCHIVED" />
      </el-select>
      <el-button @click="load">查询</el-button>
    </div>
    <div class="panel">
      <el-table :data="records" v-loading="loading">
        <el-table-column prop="title" label="标题" min-width="220" />
        <el-table-column prop="categoryName" label="分类" width="130" />
        <el-table-column label="状态" width="120"><template #default="{ row }"><NoteStatusTag :status="row.status" /></template></el-table-column>
        <el-table-column prop="updateTime" label="更新时间" width="190" />
        <el-table-column label="操作" width="290" fixed="right">
          <template #default="{ row }">
            <el-button link type="primary" @click="$router.push(`/notes/${row.id}`)">详情</el-button>
            <el-button link type="primary" @click="$router.push(`/notes/edit/${row.id}`)">编辑</el-button>
            <el-button link type="success" @click="submitAi(row.id)">AI处理</el-button>
            <el-button link type="warning" @click="publish(row.id)">发布</el-button>
            <el-button link type="danger" @click="remove(row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination v-model:current-page="query.pageNo" v-model:page-size="query.pageSize" :total="total" layout="total, prev, pager, next" @current-change="load" />
    </div>
  </div>
</template>

<script setup lang="ts">
import { onMounted, reactive, ref } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { noteApi } from '@/api/note'
import NoteStatusTag from '@/components/NoteStatusTag.vue'

const loading = ref(false)
const records = ref<any[]>([])
const total = ref(0)
const query = reactive({ pageNo: 1, pageSize: 10, keyword: '', status: '' })

async function load() {
  loading.value = true
  try {
    const res = await noteApi.myNotes(query)
    records.value = res.data.records
    total.value = res.data.total
  } finally {
    loading.value = false
  }
}

async function submitAi(id: number) {
  await noteApi.submitAi(id)
  ElMessage.success('AI 处理完成')
  load()
}

async function publish(id: number) {
  await noteApi.publish(id)
  ElMessage.success('发布成功')
  load()
}

async function remove(id: number) {
  await ElMessageBox.confirm('确认删除该笔记？', '提示')
  await noteApi.delete(id)
  ElMessage.success('删除成功')
  load()
}

onMounted(load)
</script>
