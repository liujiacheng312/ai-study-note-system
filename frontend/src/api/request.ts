import axios from 'axios'
import { ElMessage } from 'element-plus'
import router from '@/router'
import { getToken, removeToken } from '@/utils/auth'

export interface ApiResult<T> {
  code: number
  message: string
  data: T
  timestamp: number
}

const service = axios.create({
  baseURL: '/',
  timeout: 15000
})

service.interceptors.request.use(config => {
  const token = getToken()
  if (token) {
    config.headers.Authorization = `Bearer ${token}`
  }
  return config
})

service.interceptors.response.use(
  response => {
    const result = response.data as ApiResult<unknown>
    if (result.code === 200) {
      return result as any
    }
    ElMessage.error(result.message || '请求失败')
    if (result.code === 401) {
      removeToken()
      router.push('/login')
    }
    return Promise.reject(result)
  },
  error => {
    ElMessage.error(error.response?.data?.message || error.message || '网络异常')
    return Promise.reject(error)
  }
)

export default service
