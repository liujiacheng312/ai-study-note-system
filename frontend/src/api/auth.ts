import request from './request'

export const loginApi = (data: { username: string; password: string }) =>
  request.post('/api/auth/login', data)

export const registerApi = (data: { username: string; password: string; nickname: string; email?: string }) =>
  request.post('/api/auth/register', data)

export const profileApi = () => request.get('/api/auth/profile')

export const updateProfileApi = (data: unknown) => request.put('/api/user/profile', data)

export const updatePasswordApi = (data: unknown) => request.put('/api/user/password', data)
