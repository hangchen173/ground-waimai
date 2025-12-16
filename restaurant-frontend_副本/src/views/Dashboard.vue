<template>
  <div class="dashboard">
    <el-container>
      <el-header style="background-color: #409EFF; color: white; display: flex; align-items: center; justify-content: space-between;">
        <span>餐厅管理后台</span>
        <el-button type="danger" size="small" @click="logout">退出</el-button>
      </el-header>
      <el-main>
        <el-card>
          <template #header>
            <div style="display: flex; justify-content: space-between; align-items: center;">
              <span>客户列表 (Customers)</span>
              <el-button type="primary" @click="fetchData">刷新数据</el-button>
            </div>
          </template>
          
          <el-table :data="tableData" stripe style="width: 100%">
            <el-table-column prop="id" label="ID" width="80" />
            <el-table-column prop="name" label="姓名" />
            <el-table-column prop="phone" label="电话" />
            <el-table-column prop="email" label="邮箱" />
          </el-table>
        </el-card>
      </el-main>
    </el-container>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import request from '../api/axios'
import { useRouter } from 'vue-router'

const router = useRouter()
const tableData = ref([])

const fetchData = async () => {
  try {
    // 请求受保护的接口
    const res = await request.get('/customers')
    tableData.value = res
  } catch (e) {
    console.error(e)
  }
}

const logout = () => {
  localStorage.removeItem('token')
  router.push('/login')
}

// 页面加载时自动获取数据
onMounted(() => {
  fetchData()
})
</script>

<style>
body {
  margin: 0;
}
</style>