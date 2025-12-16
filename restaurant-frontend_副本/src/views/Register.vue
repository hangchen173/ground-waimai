<template>
  <div class="login-container">
    <el-card class="login-card">
      <template #header>
        <h2>注册新用户</h2>
      </template>
      <el-form :model="form" label-width="80px">
        <el-form-item label="用户名">
          <el-input v-model="form.username" placeholder="请输入用户名" />
        </el-form-item>
        <el-form-item label="密码">
          <el-input v-model="form.password" type="password" placeholder="请输入密码" show-password />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleRegister" :loading="loading" style="width: 100%">注册并登录</el-button>
        </el-form-item>
        <div style="text-align: right">
          <el-button link type="primary" @click="$router.push('/login')">已有账号？去登录</el-button>
        </div>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { register } from '../api/all' // 引入刚才写的 API

const router = useRouter()
const loading = ref(false)
const form = reactive({ username: '', password: '' })

const handleRegister = async () => {
  if(!form.username || !form.password) {
    ElMessage.warning("请输入账号密码")
    return
  }
  
  loading.value = true
  try {
    // 1. 调用注册接口
    const res = await register(form)
    
    // 2. 注册成功后，后端直接返回了 Token，我们直接存起来算自动登录
    localStorage.setItem('token', res.token)
    
    ElMessage.success('注册成功，欢迎！')
    
    // 3. 跳转到主页
    router.push('/')
  } catch (e) {
    // 错误处理 (比如用户名已存在) 交给 axios 拦截器了
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.login-container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  background-color: #f0f2f5;
}
.login-card {
  width: 400px;
}
h2 {
  text-align: center;
  margin: 0;
}
</style>