<template>
  <div class="page">
    <div class="dashboard-head">
      <div>
        <h2>学习看板</h2>
        <p>快速掌握笔记沉淀、收藏互动和 AI 辅助学习情况。</p>
      </div>
      <div class="actions">
        <el-button @click="$router.push('/ai/chat')">AI 问答</el-button>
        <el-button type="primary" @click="$router.push('/notes/create')">新建笔记</el-button>
      </div>
    </div>

    <div class="stat-grid">
      <StatCard label="笔记总数" :value="overview.noteTotal || 0" desc="累计沉淀的学习内容" tone="blue" mark="记" />
      <StatCard label="本周新增" :value="overview.weekNewNotes || 0" desc="本周新建笔记数量" tone="teal" mark="周" />
      <StatCard label="收藏数量" :value="overview.favoriteTotal || 0" desc="高价值内容收藏" tone="amber" mark="藏" />
      <StatCard label="AI 使用次数" :value="overview.aiUsageTotal || 0" desc="摘要、标签和问答调用" tone="rose" mark="AI" />
    </div>

    <div class="chart-grid">
      <div class="panel chart-panel">
        <div class="panel-title">月度学习趋势</div>
        <div ref="trendRef" class="chart"></div>
      </div>
      <div class="panel chart-panel">
        <div class="panel-title">分类分布</div>
        <div ref="categoryRef" class="chart"></div>
      </div>
    </div>

    <div class="panel advice-panel">
      <div class="advice-icon">AI</div>
      <div>
        <h3>最近学习建议</h3>
        <p class="muted">{{ advice.studyAdvice || '正在生成学习建议...' }}</p>
      </div>
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
    grid: { left: 36, right: 18, top: 22, bottom: 28 },
    tooltip: { trigger: 'axis' },
    xAxis: {
      type: 'category',
      boundaryGap: false,
      data: monthly.map(i => i.month),
      axisLine: { lineStyle: { color: '#d6deea' } },
      axisTick: { show: false }
    },
    yAxis: {
      type: 'value',
      splitLine: { lineStyle: { color: '#edf1f7' } }
    },
    series: [{
      type: 'line',
      smooth: true,
      symbolSize: 8,
      data: monthly.map(i => i.count),
      lineStyle: { color: '#2563eb', width: 3 },
      itemStyle: { color: '#2563eb' },
      areaStyle: { color: 'rgba(37, 99, 235, .12)' }
    }]
  })
  echarts.init(categoryRef.value!).setOption({
    tooltip: { trigger: 'item' },
    color: ['#2563eb', '#0f9f8f', '#d97706', '#e11d48', '#7c3aed', '#475467'],
    series: [{
      type: 'pie',
      radius: ['48%', '72%'],
      center: ['50%', '52%'],
      label: { color: '#475467' },
      data: category
    }]
  })
})
</script>

<style scoped>
.dashboard-head {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 18px;
  padding: 22px;
  color: #fff;
  background: linear-gradient(120deg, #172033, #1d4ed8 56%, #0f9f8f);
  border-radius: 8px;
  box-shadow: 0 18px 44px rgba(31, 41, 55, .16);
}

.dashboard-head h2 {
  color: #fff;
}

.dashboard-head p {
  margin: 8px 0 0;
  color: rgba(255,255,255,.78);
}

.actions {
  display: flex;
  gap: 10px;
}

.chart-panel {
  padding: 18px 18px 10px;
}

.panel-title {
  color: #172033;
  font-weight: 800;
}

.chart {
  height: 320px;
}

.advice-panel {
  display: flex;
  gap: 16px;
  align-items: flex-start;
}

.advice-icon {
  width: 44px;
  height: 44px;
  display: grid;
  place-items: center;
  flex: 0 0 auto;
  color: #fff;
  background: linear-gradient(135deg, #2563eb, #0f9f8f);
  border-radius: 8px;
  font-weight: 800;
}

@media (max-width: 760px) {
  .dashboard-head {
    align-items: stretch;
    flex-direction: column;
  }

  .actions {
    flex-wrap: wrap;
  }
}
</style>
