<template>
  <div class="page">
    <div class="toolbar">
      <h2>学习看板</h2>
      <el-button type="primary" @click="$router.push('/notes/create')">新建笔记</el-button>
    </div>
    <div class="stat-grid">
      <StatCard label="笔记总数" :value="overview.noteTotal || 0" />
      <StatCard label="本周新增" :value="overview.weekNewNotes || 0" />
      <StatCard label="收藏数量" :value="overview.favoriteTotal || 0" />
      <StatCard label="AI 使用次数" :value="overview.aiUsageTotal || 0" />
    </div>
    <div class="chart-grid">
      <div class="panel"><div ref="trendRef" style="height:320px"></div></div>
      <div class="panel"><div ref="categoryRef" style="height:320px"></div></div>
    </div>
    <div class="panel">
      <h3>最近学习建议</h3>
      <p class="muted">{{ advice.studyAdvice || '正在生成学习建议...' }}</p>
    </div>
  </div>
</template>

<script setup lang="ts">
import { nextTick, onMounted, reactive, ref } from 'vue'
import * as echarts from 'echarts'
import StatCard from '@/components/StatCard.vue'
import { statisticsApi } from '@/api/statistics'
import { aiApi } from '@/api/ai'

const overview = reactive<Record<string, number>>({})
const advice = reactive<Record<string, string>>({})
const trendRef = ref<HTMLDivElement>()
const categoryRef = ref<HTMLDivElement>()

onMounted(async () => {
  Object.assign(overview, (await statisticsApi.overview()).data)
  const monthly = (await statisticsApi.monthly()).data as Array<{ month: string; count: number }>
  const category = (await statisticsApi.category()).data as Array<{ name: string; value: number }>
  Object.assign(advice, (await aiApi.studyAdvice()).data)
  await nextTick()
  echarts.init(trendRef.value!).setOption({
    title: { text: '月度学习趋势', left: 0 },
    tooltip: {},
    xAxis: { type: 'category', data: monthly.map(i => i.month) },
    yAxis: { type: 'value' },
    series: [{ type: 'line', smooth: true, data: monthly.map(i => i.count), areaStyle: {} }]
  })
  echarts.init(categoryRef.value!).setOption({
    title: { text: '分类统计', left: 0 },
    tooltip: { trigger: 'item' },
    series: [{ type: 'pie', radius: ['42%', '70%'], data: category }]
  })
})
</script>
