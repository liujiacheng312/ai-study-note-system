<template>
  <div :class="['stat-card', tone]">
    <div class="top-row">
      <div class="label">{{ label }}</div>
      <div class="mark">{{ mark }}</div>
    </div>
    <div class="value">{{ value }}</div>
    <div class="desc">{{ desc || '学习数据实时更新' }}</div>
  </div>
</template>

<script setup lang="ts">
withDefaults(defineProps<{
  label: string
  value: number | string
  desc?: string
  tone?: 'blue' | 'teal' | 'amber' | 'rose'
  mark?: string
}>(), {
  tone: 'blue',
  mark: '•'
})
</script>

<style scoped>
.stat-card {
  position: relative;
  min-height: 132px;
  overflow: hidden;
  background: #fff;
  border: 1px solid #e5eaf2;
  border-radius: 8px;
  padding: 18px;
  box-shadow: 0 10px 24px rgba(31, 41, 55, .07);
}

.stat-card::before {
  content: "";
  position: absolute;
  inset: 0;
  background: linear-gradient(135deg, rgba(37, 99, 235, .10), transparent 46%);
  pointer-events: none;
}

.top-row {
  position: relative;
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 12px;
}

.label {
  color: #697589;
  font-size: 14px;
  font-weight: 700;
}

.mark {
  width: 34px;
  height: 34px;
  display: grid;
  place-items: center;
  color: #fff;
  background: #2563eb;
  border-radius: 8px;
  font-weight: 800;
}

.value {
  position: relative;
  margin-top: 18px;
  color: #101828;
  font-size: 34px;
  line-height: 1;
  font-weight: 800;
}

.desc {
  position: relative;
  margin-top: 12px;
  color: #7a8699;
  font-size: 13px;
}

.teal::before {
  background: linear-gradient(135deg, rgba(15, 159, 143, .13), transparent 46%);
}
.teal .mark {
  background: #0f9f8f;
}
.amber::before {
  background: linear-gradient(135deg, rgba(217, 119, 6, .13), transparent 46%);
}
.amber .mark {
  background: #d97706;
}
.rose::before {
  background: linear-gradient(135deg, rgba(225, 29, 72, .11), transparent 46%);
}
.rose .mark {
  background: #e11d48;
}
</style>
