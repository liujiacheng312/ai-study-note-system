import request from './request'

export const adminApi = {
  users: (params: unknown) => request.get('/api/admin/user/page', { params }),
  disableUser: (id: number) => request.put(`/api/admin/user/${id}/disable`),
  enableUser: (id: number) => request.put(`/api/admin/user/${id}/enable`),
  resetPassword: (id: number) => request.put(`/api/admin/user/${id}/reset-password`),
  notes: (params: unknown) => request.get('/api/admin/notes/page', { params }),
  deleteNote: (id: number) => request.delete(`/api/admin/notes/${id}`),
  categories: () => request.get('/api/admin/categories'),
  createCategory: (data: unknown) => request.post('/api/admin/categories', data),
  updateCategory: (id: number, data: unknown) => request.put(`/api/admin/categories/${id}`, data),
  deleteCategory: (id: number) => request.delete(`/api/admin/categories/${id}`),
  tags: () => request.get('/api/admin/tags'),
  createTag: (data: unknown) => request.post('/api/admin/tags', data),
  updateTag: (id: number, data: unknown) => request.put(`/api/admin/tags/${id}`, data),
  deleteTag: (id: number) => request.delete(`/api/admin/tags/${id}`),
  announcements: () => request.get('/api/admin/announcements'),
  createAnnouncement: (data: unknown) => request.post('/api/admin/announcements', data),
  updateAnnouncement: (id: number, data: unknown) => request.put(`/api/admin/announcements/${id}`, data),
  deleteAnnouncement: (id: number) => request.delete(`/api/admin/announcements/${id}`),
  comments: (params: unknown) => request.get('/api/admin/comments/page', { params }),
  auditComment: (id: number, status: number) => request.put(`/api/admin/comments/${id}/audit`, null, { params: { status } }),
  aiConfig: () => request.get('/api/admin/ai-config'),
  updateAiConfig: (data: unknown) => request.put('/api/admin/ai-config', data)
}
