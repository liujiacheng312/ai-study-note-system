<template>
  <div class="page">
    <div class="toolbar"><h2>个人中心</h2></div>
    <div class="panel">
      <el-form :model="form" label-width="90px" style="max-width:620px">
        <el-form-item label="用户名"><el-input v-model="form.username" disabled /></el-form-item>
        <el-form-item label="昵称"><el-input v-model="form.nickname" /></el-form-item>
        <el-form-item label="邮箱"><el-input v-model="form.email" /></el-form-item>
        <el-form-item label="头像"><el-input v-model="form.avatar" /></el-form-item>
        <el-form-item><el-button type="primary" @click="save">保存资料</el-button></el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script setup lang="ts">
import { reactive } from 'vue'
import { ElMessage } from 'element-plus'
import { updateProfileApi } from '@/api/auth'
import { useAuthStore } from '@/store/auth'

const auth = useAuthStore()
const form = reactive<any>({ ...auth.user })
async function save() {
  const res = await updateProfileApi(form)
  auth.user = res.data
  ElMessage.success('保存成功')
}
</script>
