import { defineStore } from 'pinia'
import { loginApi, profileApi } from '@/api/auth'
import { getStoredUser, getToken, removeToken, setStoredUser, setToken } from '@/utils/auth'

export interface UserInfo {
  id: number
  username: string
  nickname: string
  email?: string
  avatar?: string
  role: 'USER' | 'ADMIN'
  status: number
}

export const useAuthStore = defineStore('auth', {
  state: () => ({
    token: getToken() || '',
    user: getStoredUser<UserInfo>() as UserInfo | null
  }),
  getters: {
    isAdmin: state => state.user?.role === 'ADMIN'
  },
  actions: {
    async login(payload: { username: string; password: string }) {
      const res = await loginApi(payload)
      this.token = res.data.token
      this.user = res.data.user
      setToken(this.token)
      setStoredUser(this.user)
    },
    async loadProfile() {
      const res = await profileApi()
      this.user = res.data
      setStoredUser(this.user)
    },
    logout() {
      this.token = ''
      this.user = null
      removeToken()
    }
  }
})
