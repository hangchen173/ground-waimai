<template>
  <el-card>
    <template #header>
      <div style="display: flex; justify-content: space-between">
        <span>客户列表</span>
        <el-button type="primary" @click="dialogVisible = true">新增客户</el-button>
      </div>
    </template>
    <el-table :data="tableData" stripe>
      <el-table-column prop="id" label="ID" width="60" />
      <el-table-column prop="name" label="姓名" />
      <el-table-column prop="phone" label="电话" />
      <el-table-column prop="email" label="邮箱" />
      <el-table-column label="操作">
        <template #default="{ row }">
          <el-button type="danger" link @click="handleDelete(row.id)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
  </el-card>

  <el-dialog v-model="dialogVisible" title="新增客户">
    <el-form :model="form" label-width="80px">
      <el-form-item label="姓名"><el-input v-model="form.name" /></el-form-item>
      <el-form-item label="电话"><el-input v-model="form.phone" /></el-form-item>
      <el-form-item label="邮箱"><el-input v-model="form.email" /></el-form-item>
    </el-form>
    <template #footer>
      <el-button @click="handleSubmit" type="primary">提交</el-button>
    </template>
  </el-dialog>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { getCustomers, createCustomer, deleteCustomer } from '../api/all'
import { ElMessage } from 'element-plus'

const tableData = ref([])
const dialogVisible = ref(false)
const form = reactive({ name: '', phone: '', email: '' })

const loadData = async () => tableData.value = await getCustomers()
const handleDelete = async (id) => { await deleteCustomer(id); loadData() }
const handleSubmit = async () => {
  await createCustomer(form)
  ElMessage.success('成功')
  dialogVisible.value = false
  loadData()
}
onMounted(loadData)
</script>