    <template>
      <div class="page">
        <div class="toolbar"><h2>管理员用户管理</h2></div>

<div class="panel toolbar">
  <el-input v-model="query.keyword" placeholder="搜索用户" style="width:260px" @keyup.enter="load" />
  <el-button @click="load">查询</el-button>
</div>
<div class="panel">
  <el-table :data="records">
    <el-table-column prop="username" label="用户名" />
    <el-table-column prop="nickname" label="昵称" />
    <el-table-column prop="role" label="角色" width="100" />
    <el-table-column prop="status" label="状态" width="100" />
    <el-table-column label="操作" width="260">
      <template #default="{ row }">
        <el-button link type="primary" @click="change(row, true)">启用</el-button>
        <el-button link type="warning" @click="change(row, false)">禁用</el-button>
        <el-button link type="danger" @click="reset(row)">重置密码</el-button>
      </template>
    </el-table-column>
  </el-table>
  <el-pagination v-model:current-page="query.pageNo" :total="total" layout="total, prev, pager, next" @current-change="load" />
</div>

      </div>
    </template>

    <script setup lang="ts">

import { onMounted, reactive, ref } from 'vue'
import { ElMessage } from 'element-plus'
import { adminApi } from '@/api/admin'

const query = reactive({ pageNo: 1, pageSize: 10, keyword: '' })
const records = ref<any[]>([])
const total = ref(0)
async function load() {
  const res = await adminApi.users(query)
  records.value = res.data.records
  total.value = res.data.total
}
async function change(row: any, enable: boolean) {
  enable ? await adminApi.enableUser(row.id) : await adminApi.disableUser(row.id)
  ElMessage.success('操作成功')
  load()
}
async function reset(row: any) {
  await adminApi.resetPassword(row.id)
  ElMessage.success('密码已重置为 123456')
}
onMounted(load)

    </script>
