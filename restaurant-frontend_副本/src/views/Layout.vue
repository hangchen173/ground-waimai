<template>
  <el-container style="height: 100vh">
    <!-- 侧边栏 -->
    <el-aside width="200px" style="background-color: #304156">
      <h3 style="color: white; text-align: center; padding: 20px 0">餐厅管理系统</h3>
      <el-menu
        router
        :default-active="$route.path"
        background-color="#304156"
        text-color="#fff"
        active-text-color="#409EFF"
      >
        <el-menu-item index="/customers">👤 客户管理</el-menu-item>
        <el-menu-item index="/restaurants">🏠 餐厅管理</el-menu-item>
        <el-menu-item index="/tables">🪑 餐桌管理</el-menu-item>
        <el-menu-item index="/reservations">📅 预订管理</el-menu-item>
      </el-menu>
    </el-aside>

    <!-- 主内容区 -->
    <el-container>
      <el-header style="display: flex; justify-content: flex-end; align-items: center; border-bottom: 1px solid #eee">
        <el-button type="danger" size="small" @click="logout">退出登录</el-button>
      </el-header>
      <el-main>
        <!-- 子页面在这里渲染 -->
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup>
import { useRouter } from 'vue-router'
const router = useRouter()
const logout = () => {
  localStorage.removeItem('token')
  router.push('/login')
}
</script>

<style>
body { margin: 0; }
</style>