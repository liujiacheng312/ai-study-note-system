import request from './request'

export const noteApi = {
  create: (data: unknown) => request.post('/api/notes', data),
  update: (id: number, data: unknown) => request.put(`/api/notes/${id}`, data),
  delete: (id: number) => request.delete(`/api/notes/${id}`),
  detail: (id: number) => request.get(`/api/notes/${id}`),
  myNotes: (params: unknown) => request.get('/api/notes/my', { params }),
  publicNotes: (params: unknown) => request.get('/api/notes/public', { params }),
  submitAi: (id: number) => request.put(`/api/notes/${id}/submit-ai`),
  publish: (id: number) => request.put(`/api/notes/${id}/publish`),
  archive: (id: number) => request.put(`/api/notes/${id}/archive`)
}

export const metaApi = {
  categories: () => request.get('/api/categories'),
  tags: () => request.get('/api/tags'),
  hotTags: () => request.get('/api/tags/hot')
}
