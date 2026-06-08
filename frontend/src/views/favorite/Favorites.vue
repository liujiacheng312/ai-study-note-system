<template>
  <div class="page">
    <div class="toolbar"><h2>收藏中心</h2></div>
    <div class="panel">
      <el-table :data="records" v-loading="loading">
        <el-table-column prop="title" label="标题" />
        <el-table-column prop="categoryName" label="分类" width="140" />
        <el-table-column label="操作" width="150"><template #default="{ row }"><el-button link type="primary" @click="$router.push(`/notes/${row.id}`)">查看</el-button></template></el-table-column>
      </el-table>
    </div>
  </div>
</template>

<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { favoriteApi } from '@/api/social'

const records = ref<any[]>([])
const loading = ref(false)
onMounted(async () => {
  loading.value = true
  try {
    records.value = (await favoriteApi.my({ pageNo: 1, pageSize: 20 })).data.records
  } finally {
    loading.value = false
  }
})
</script>
