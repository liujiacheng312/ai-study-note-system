    <template>
      <div class="page">
        <div class="toolbar"><h2>管理员公告管理</h2></div>

<div class="panel"><el-button type="primary" @click="open()">发布公告</el-button></div>
<div class="panel">
  <el-table :data="records">
    <el-table-column prop="title" label="标题" />
    <el-table-column prop="status" label="状态" width="100" />
    <el-table-column label="操作" width="160"><template #default="{ row }"><el-button link @click="open(row)">编辑</el-button><el-button link type="danger" @click="remove(row)">删除</el-button></template></el-table-column>
  </el-table>
</div>
<el-dialog v-model="dialog" title="公告信息" width="560px">
  <el-form :model="form" label-width="80px"><el-form-item label="标题"><el-input v-model="form.title" /></el-form-item><el-form-item label="内容"><el-input v-model="form.content" type="textarea" :rows="5" /></el-form-item><el-form-item label="状态"><el-switch v-model="form.status" :active-value="1" :inactive-value="0" /></el-form-item></el-form>
  <template #footer><el-button @click="dialog=false">取消</el-button><el-button type="primary" @click="save">保存</el-button></template>
</el-dialog>

      </div>
    </template>

    <script setup lang="ts">

import { onMounted, reactive, ref } from 'vue'
import { ElMessage } from 'element-plus'
import { adminApi } from '@/api/admin'

const records = ref<any[]>([])
const dialog = ref(false)
const form = reactive<any>({ id: undefined, title: '', content: '', status: 1 })
async function load() { records.value = (await adminApi.announcements()).data }
function open(row?: any) { Object.assign(form, row || { id: undefined, title: '', content: '', status: 1 }); dialog.value = true }
async function save() { form.id ? await adminApi.updateAnnouncement(form.id, form) : await adminApi.createAnnouncement(form); ElMessage.success('保存成功'); dialog.value = false; load() }
async function remove(row: any) { await adminApi.deleteAnnouncement(row.id); ElMessage.success('删除成功'); load() }
onMounted(load)

    </script>
