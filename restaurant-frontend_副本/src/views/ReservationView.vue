<template>
  <el-card>
    <template #header>
      <div style="display: flex; justify-content: space-between">
        <span>📅 预订列表</span>
        <el-button type="primary" @click="dialogVisible = true">新增预订</el-button>
      </div>
    </template>

    <!-- 数据表格 -->
    <el-table :data="tableData" stripe>
      <el-table-column prop="id" label="ID" width="60" />
      <el-table-column prop="customerId" label="客户ID" width="80" />
      <el-table-column prop="tableId" label="餐桌ID" width="80" />
      
      <!-- 日期格式化展示 -->
      <el-table-column prop="reservationTime" label="预订时间" width="180">
        <template #default="{ row }">
          {{ new Date(row.reservationTime).toLocaleString() }}
        </template>
      </el-table-column>

      <el-table-column prop="durationMinutes" label="时长(分)" width="80" />
      <el-table-column prop="numGuests" label="人数" width="80" />
      <el-table-column prop="status" label="状态">
        <template #default="{ row }">
          <el-tag :type="row.status === 'CONFIRMED' ? 'success' : 'warning'">
            {{ row.status }}
          </el-tag>
        </template>
      </el-table-column>

      <!-- ⭐ 权限控制：只有管理员能看到操作列（或者只隐藏按钮） -->
      <el-table-column label="操作">
        <template #default="{ row }">
          <!-- 👇 核心修改：v-if 判断角色 -->
          <el-button 
            v-if="userRole === 'ROLE_ADMIN'"
            type="danger" 
            link 
            @click="handleDelete(row.id)"
          >
            取消
          </el-button>
          <!-- 如果是顾客，可以显示一个占位符或者什么都不显示 -->
          <span v-else style="color: #999; font-size: 12px;">不可操作</span>
        </template>
      </el-table-column>
    </el-table>
  </el-card>

  <!-- 新增弹窗 -->
  <el-dialog v-model="dialogVisible" title="新增预订" width="500px">
    <el-form :model="form" label-width="100px">
      
      <el-form-item label="客户ID" required>
        <el-input v-model.number="form.customerId" type="number" placeholder="查看客户列表找ID" />
      </el-form-item>
      
      <el-form-item label="餐桌ID" required>
        <el-input v-model.number="form.tableId" type="number" placeholder="查看餐桌列表找ID" />
      </el-form-item>

      <el-form-item label="预订时间" required>
        <el-date-picker
          v-model="form.reservationTime"
          type="datetime"
          placeholder="选择日期时间"
          format="YYYY-MM-DD HH:mm"
          value-format="YYYY-MM-DDTHH:mm:ss"
          style="width: 100%"
        />
      </el-form-item>

      <el-form-item label="就餐人数">
        <el-input-number v-model="form.numGuests" :min="1" />
      </el-form-item>

      <el-form-item label="时长(分钟)">
        <el-input-number v-model="form.durationMinutes" :min="30" :step="30" />
      </el-form-item>

    </el-form>
    <template #footer>
      <el-button @click="dialogVisible = false">取消</el-button>
      <el-button @click="handleSubmit" type="primary">提交</el-button>
    </template>
  </el-dialog>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { getReservations, createReservation, deleteReservation } from '../api/all'
import { ElMessage } from 'element-plus'
import { jwtDecode } from "jwt-decode" // ⭐ 核心修改：导入解码库

const tableData = ref([])
const dialogVisible = ref(false)
const userRole = ref('') // ⭐ 核心修改：存储当前用户角色

// 表单默认值
const form = reactive({
  customerId: '',
  tableId: '',
  reservationTime: '',
  numGuests: 2,
  durationMinutes: 60,
  status: 'CONFIRMED'
})

// 加载列表
const loadData = async () => {
  try {
    tableData.value = await getReservations()
  } catch (e) {
    console.error(e)
  }
}

// 删除预订
const handleDelete = async (id) => {
  if(!confirm("确定取消这个预订吗？")) return
  await deleteReservation(id)
  ElMessage.success('预订已取消')
  loadData()
}

// 提交新增
const handleSubmit = async () => {
  if(!form.customerId || !form.tableId || !form.reservationTime) {
    ElMessage.warning('请填写完整信息')
    return
  }
  
  try {
    await createReservation(form)
    ElMessage.success('预订成功！')
    dialogVisible.value = false
    loadData()
  } catch (e) {
    // 错误处理交给 axios 拦截器
  }
}

onMounted(() => {
  // ⭐ 核心修改：解析 Token 获取角色
  const token = localStorage.getItem('token')
  if (token) {
    try {
      const decoded = jwtDecode(token)
      userRole.value = decoded.role || '' // 拿到 ROLE_ADMIN 或 ROLE_CUSTOMER
    } catch (e) {
      console.error('Token 解析失败', e)
    }
  }
  
  loadData()
})
</script>