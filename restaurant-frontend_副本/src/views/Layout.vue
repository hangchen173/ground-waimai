<template>
  <el-container style="height: 100vh">
    <!-- 侧边栏 -->
    <el-aside width="200px" style="background-color: #304156">
      <h3 style="color: white; text-align: center; padding: 20px 0">
        <!-- 根据角色显示不同标题 -->
        {{ userRole === 'ROLE_ADMIN' ? '餐厅后台' : '自助点餐' }}
      </h3>
      
      <el-menu
        router
        :default-active="$route.path"
        background-color="#304156"
        text-color="#fff"
        active-text-color="#409EFF"
      >
        <!-- 👇 1. 管理员可见菜单 -->
        <template v-if="userRole === 'ROLE_ADMIN'">
          <el-menu-item index="/customers">👤 客户管理</el-menu-item>
          <el-menu-item index="/restaurants">🏠 餐厅管理</el-menu-item>
        </template>
        
        <!-- 👇 2. 所有人可见菜单 -->
        <el-menu-item index="/tables">🪑 餐桌信息</el-menu-item>
        <el-menu-item index="/reservations">📅 预订管理</el-menu-item>
      </el-menu>
    </el-aside>

    <!-- 主内容区 -->
    <el-container>
      <el-header style="display: flex; justify-content: flex-end; align-items: center; border-bottom: 1px solid #eee">
        <span style="margin-right: 15px; color: #666">
          当前用户: {{ userRole === 'ROLE_ADMIN' ? '管理员' : '顾客' }}
        </span>
        <el-button type="danger" size="small" @click="logout">退出登录</el-button>
      </el-header>
      <el-main>
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { jwtDecode } from "jwt-decode"; // 确保这里引入没报错

const router = useRouter()
const userRole = ref('')

onMounted(() => {
  const token = localStorage.getItem('token')
  if (token) {
    try {
      // 🟢 关键修复：加了 try-catch，防止 Token 格式不对导致整个页面卡死
      const decoded = jwtDecode(token)
      userRole.value = decoded.role || 'ROLE_CUSTOMER'
      console.log("当前角色:", userRole.value)
    } catch (e) {
      console.error("Token 解析失败:", e)
      // 如果 Token 坏了，强制登出
      logout()
    }
  }
})

const logout = () => {
  localStorage.removeItem('token')
  router.push('/login')
}
</script>

<style>
body { margin: 0; }
</style>