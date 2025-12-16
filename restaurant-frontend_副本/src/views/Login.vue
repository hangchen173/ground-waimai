<template>
  <div class="login-container">
    <el-card class="login-card">
      <template #header>
        <h2>餐厅管理系统</h2>
      </template>
      <el-form :model="form" label-width="80px">
        <el-form-item label="用户名">
          <el-input v-model="form.username" placeholder="admin" />
        </el-form-item>
        <el-form-item label="密码">
          <el-input v-model="form.password" type="password" placeholder="password" show-password />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleLogin" :loading="loading" style="width: 100%">登录</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import request from '../api/axios'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'

const router = useRouter()
const loading = ref(false)
const form = ref({
  username: '',
  password: ''
})

const handleLogin = async () => {
  if(!form.value.username || !form.value.password) {
    ElMessage.warning("请输入账号密码")
    return
  }
  
  loading.value = true
  try {
    // 发送登录请求
    const res = await request.post('/auth/login', form.value)
    // 保存 Token 到浏览器
    localStorage.setItem('token', res.token)
    ElMessage.success('登录成功')
    // 跳转到首页
    router.push('/')
  } catch (e) {
    console.error(e)
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