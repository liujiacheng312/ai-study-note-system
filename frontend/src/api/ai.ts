import request from './request'

export const aiApi = {
  summary: (data: unknown) => request.post('/api/ai/summary', data),
  tags: (data: unknown) => request.post('/api/ai/tags', data),
  chat: (data: unknown) => request.post('/api/ai/chat', data),
  studyAdvice: () => request.get('/api/ai/study-advice'),
  config: () => request.get('/api/ai/config'),
  updateConfig: (data: unknown) => request.put('/api/ai/config', data)
}
