<template>
  <div class="page">
    <div class="toolbar"><h2>{{ isEdit ? '编辑笔记' : '新建笔记' }}</h2></div>
    <div class="panel">
      <el-form :model="form" label-width="90px">
        <el-form-item label="标题"><el-input v-model="form.title" /></el-form-item>
        <el-form-item label="分类">
          <el-select v-model="form.categoryId" placeholder="选择分类" clearable>
            <el-option v-for="item in categories" :key="item.id" :label="item.name" :value="item.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="标签">
          <el-select v-model="form.tagIds" multiple placeholder="选择标签" style="width:100%">
            <el-option v-for="item in tags" :key="item.id" :label="item.name" :value="item.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="正文">
          <MdEditor v-model="form.content" style="height:480px" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :loading="saving" @click="save">保存草稿</el-button>
          <el-button v-if="isEdit" type="success" @click="submitAi">提交 AI 处理</el-button>
          <el-button @click="$router.back()">返回</el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script setup lang="ts">
import { computed, onMounted, reactive, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { MdEditor } from 'md-editor-v3'
import { metaApi, noteApi } from '@/api/note'

const route = useRoute()
const router = useRouter()
const isEdit = computed(() => Boolean(route.params.id))
const saving = ref(false)
const categories = ref<any[]>([])
const tags = ref<any[]>([])
const form = reactive<any>({ title: '', categoryId: undefined, tagIds: [], content: '# 新学习笔记\n\n' })

onMounted(async () => {
  categories.value = (await metaApi.categories()).data
  tags.value = (await metaApi.tags()).data
  if (isEdit.value) {
    const res = await noteApi.detail(Number(route.params.id))
    Object.assign(form, res.data)
  }
})

async function save() {
  saving.value = true
  try {
    const res = isEdit.value ? await noteApi.update(Number(route.params.id), form) : await noteApi.create(form)
    ElMessage.success('保存成功')
    router.push(`/notes/edit/${res.data.id}`)
  } finally {
    saving.value = false
  }
}

async function submitAi() {
  await noteApi.submitAi(Number(route.params.id))
  ElMessage.success('AI 处理完成')
  router.push('/notes/my')
}
</script>
