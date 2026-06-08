import request from './request'

export const favoriteApi = {
  add: (noteId: number) => request.post(`/api/favorites/${noteId}`),
  remove: (noteId: number) => request.delete(`/api/favorites/${noteId}`),
  my: (params: unknown) => request.get('/api/favorites/my', { params }),
  check: (noteId: number) => request.get(`/api/favorites/check/${noteId}`)
}

export const commentApi = {
  create: (data: unknown) => request.post('/api/comments', data),
  note: (noteId: number) => request.get(`/api/comments/note/${noteId}`),
  delete: (id: number) => request.delete(`/api/comments/${id}`)
}
