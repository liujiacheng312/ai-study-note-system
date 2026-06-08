    <template>
      <div class="page">
        <div class="toolbar"><h2>管理员笔记管理</h2></div>

<div class="panel">
  <el-table :data="records">
    <el-table-column prop="title" label="标题" />
    <el-table-column prop="userId" label="作者ID" width="100" />
    <el-table-column label="状态" width="130"><template #default="{ row }"><NoteStatusTag :status="row.status" /></template></el-table-column>
    <el-table-column label="操作" width="120"><template #default="{ row }"><el-button link type="danger" @click="remove(row)">删除</el-button></template></el-table-column>
  </el-table>
  <el-pagination v-model:current-page="query.pageNo" :total="total" layout="total, prev, pager, next" @current-change="load" />
</div>

      </div>
    </template>

    <script setup lang="ts">

import { onMounted, reactive, ref } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { adminApi } from '@/api/admin'
import NoteStatusTag from '@/components/NoteStatusTag.vue'

const query = reactive({ pageNo: 1, pageSize: 10 })
const records = ref<any[]>([])
const total = ref(0)
async function load() {
  const res = await adminApi.notes(query)
  records.value = res.data.records
  total.value = res.data.total
}
async function remove(row: any) {
  await ElMessageBox.confirm('确认删除违规笔记？', '提示')
  await adminApi.deleteNote(row.id)
  ElMessage.success('删除成功')
  load()
}
onMounted(load)

    </script>
