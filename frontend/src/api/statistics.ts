import request from './request'

export const statisticsApi = {
  overview: () => request.get('/api/statistics/overview'),
  category: () => request.get('/api/statistics/category'),
  monthly: () => request.get('/api/statistics/monthly'),
  adminOverview: () => request.get('/api/admin/statistics/overview')
}
