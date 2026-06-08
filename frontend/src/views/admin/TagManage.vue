    <template>
      <div class="page">
        <div class="toolbar"><h2>管理员标签管理</h2></div>

<div class="panel"><el-button type="primary" @click="open()">新增标签</el-button></div>
<div class="panel">
  <el-table :data="records">
    <el-table-column prop="name" label="名称" />
    <el-table-column label="颜色"><template #default="{ row }"><el-tag :color="row.color" effect="dark">{{ row.color }}</el-tag></template></el-table-column>
    <el-table-column prop="useCount" label="使用次数" width="110" />
    <el-table-column label="操作" width="160"><template #default="{ row }"><el-button link @click="open(row)">编辑</el-button><el-button link type="danger" @click="remove(row)">删除</el-button></template></el-table-column>
  </el-table>
</div>
<el-dialog v-model="dialog" title="标签信息" width="460px">
  <el-form :model="form" label-width="80px"><el-form-item label="名称"><el-input v-model="form.name" /></el-form-item><el-form-item label="颜色"><el-color-picker v-model="form.color" /></el-form-item></el-form>
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
const form = reactive<any>({ id: undefined, name: '', color: '#409EFF' })
async function load() { records.value = (await adminApi.tags()).data }
function open(row?: any) { Object.assign(form, row || { id: undefined, name: '', color: '#409EFF' }); dialog.value = true }
async function save() { form.id ? await adminApi.updateTag(form.id, form) : await adminApi.createTag(form); ElMessage.success('保存成功'); dialog.value = false; load() }
async function remove(row: any) { await adminApi.deleteTag(row.id); ElMessage.success('删除成功'); load() }
onMounted(load)

    </script>
