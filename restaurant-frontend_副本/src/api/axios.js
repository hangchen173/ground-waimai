import axios from 'axios'
import router from '../router'
import { ElMessage } from 'element-plus'

const instance = axios.create({
    baseURL: '/api', // 走 Vite 代理
    timeout: 5000
})

// 1. 请求拦截器：每次发请求前，把 Token 塞进去
instance.interceptors.request.use(
    config => {
        const token = localStorage.getItem('token')
        if (token) {
            config.headers['Authorization'] = `Bearer ${token}`
        }
        return config
    },
    error => Promise.reject(error)
)

// 2. 响应拦截器：统一处理错误
instance.interceptors.response.use(
    response => response.data,
    error => {
        // 如果后端返回 401 (未授权)，强制跳回登录页
        if (error.response && error.response.status === 401) {
            ElMessage.error('登录过期，请重新登录')
            localStorage.removeItem('token')
            router.push('/login')
        } else {
            ElMessage.error(error.response?.data?.message || '请求失败')
        }
        return Promise.reject(error)
    }
)

export default instance