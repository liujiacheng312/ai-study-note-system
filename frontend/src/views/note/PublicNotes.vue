<template>
  <div class="page">
    <div class="toolbar"><h2>公开笔记广场</h2><el-input v-model="query.keyword" placeholder="搜索公开笔记" style="width:260px" @keyup.enter="load" /></div>
    <div class="panel">
      <el-table :data="records" v-loading="loading">
        <el-table-column prop="title" label="标题" min-width="240" />
        <el-table-column prop="categoryName" label="分类" width="140" />
        <el-table-column prop="viewCount" label="浏览" width="90" />
        <el-table-column prop="favoriteCount" label="收藏" width="90" />
        <el-table-column label="标签" min-width="180">
          <template #default="{ row }"><el-tag v-for="tag in row.tagNames" :key="tag" style="margin-right:6px">{{ tag }}</el-tag></template>
        </el-table-column>
        <el-table-column label="操作" width="120"><template #default="{ row }"><el-button link type="primary" @click="$router.push(`/notes/${row.id}`)">查看</el-button></template></el-table-column>
      </el-table>
      <el-pagination v-model:current-page="query.pageNo" :total="total" layout="total, prev, pager, next" @current-change="load" />
    </div>
  </div>
</template>

<script setup lang="ts">
import { onMounted, reactive, ref } from 'vue'
import { noteApi } from '@/api/note'

const query = reactive({ pageNo: 1, pageSize: 10, keyword: '' })
const records = ref<any[]>([])
const total = ref(0)
const loading = ref(false)
async function load() {
  loading.value = true
  try {
    const res = await noteApi.publicNotes(query)
    records.value = res.data.records
    total.value = res.data.total
  } finally {
    loading.value = false
  }
}
onMounted(load)
</script>
