<template>
  <div class="page">
    <div class="toolbar"><h2>学习统计</h2></div>
    <div class="chart-grid">
      <div class="panel"><div ref="monthlyRef" style="height:360px"></div></div>
      <div class="panel"><div ref="categoryRef" style="height:360px"></div></div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { nextTick, onMounted, ref } from 'vue'
import * as echarts from 'echarts'
import { statisticsApi } from '@/api/statistics'

const monthlyRef = ref<HTMLDivElement>()
const categoryRef = ref<HTMLDivElement>()

onMounted(async () => {
  const monthly = (await statisticsApi.monthly()).data
  const category = (await statisticsApi.category()).data
  await nextTick()
  echarts.init(monthlyRef.value!).setOption({
    title: { text: '最近六个月新增笔记' },
    xAxis: { type: 'category', data: monthly.map((i: any) => i.month) },
    yAxis: { type: 'value' },
    tooltip: {},
    series: [{ type: 'bar', data: monthly.map((i: any) => i.count), itemStyle: { color: '#2563eb' } }]
  })
  echarts.init(categoryRef.value!).setOption({
    title: { text: '分类占比' },
    tooltip: { trigger: 'item' },
    series: [{ type: 'pie', radius: '70%', data: category }]
  })
})
</script>
