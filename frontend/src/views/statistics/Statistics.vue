<template>
  <div class="page">
    <div class="toolbar">
      <div>
        <h2>学习统计</h2>
        <p class="muted">通过趋势图和分类占比，观察自己的学习节奏和知识结构。</p>
      </div>
    </div>

    <div class="chart-grid">
      <div class="panel chart-panel">
        <div class="panel-title">最近六个月新增笔记</div>
        <div ref="monthlyRef" class="chart"></div>
      </div>
      <div class="panel chart-panel">
        <div class="panel-title">分类占比</div>
        <div ref="categoryRef" class="chart"></div>
      </div>
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
    grid: { left: 38, right: 18, top: 22, bottom: 30 },
    xAxis: {
      type: 'category',
      data: monthly.map((i: any) => i.month),
      axisLine: { lineStyle: { color: '#d6deea' } },
      axisTick: { show: false }
    },
    yAxis: { type: 'value', splitLine: { lineStyle: { color: '#edf1f7' } } },
    tooltip: { trigger: 'axis' },
    series: [{
      type: 'bar',
      data: monthly.map((i: any) => i.count),
      barWidth: 28,
      itemStyle: { color: '#2563eb', borderRadius: [6, 6, 0, 0] }
    }]
  })
  echarts.init(categoryRef.value!).setOption({
    color: ['#2563eb', '#0f9f8f', '#d97706', '#e11d48', '#7c3aed', '#475467'],
    tooltip: { trigger: 'item' },
    series: [{ type: 'pie', radius: ['46%', '72%'], data: category }]
  })
})
</script>

<style scoped>
.chart-panel {
  padding: 18px 18px 10px;
}

.panel-title {
  color: #172033;
  font-weight: 800;
}

.chart {
  height: 360px;
}
</style>
