<template>
  <el-card>
    <template #header>
      <div style="display: flex; justify-content: space-between">
        <span>餐厅列表</span>
        <el-button type="primary" @click="dialogVisible = true">新增餐厅</el-button>
      </div>
    </template>
    <el-table :data="tableData" stripe>
      <el-table-column prop="id" label="ID" width="60" />
      <el-table-column prop="name" label="餐厅名" />
      <el-table-column prop="address" label="地址" />
      <el-table-column prop="phone" label="联系电话" />
      <el-table-column label="操作">
        <template #default="{ row }">
          <el-button type="danger" link @click="handleDelete(row.id)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
  </el-card>

  <el-dialog v-model="dialogVisible" title="新增餐厅">
    <el-form :model="form" label-width="80px">
      <el-form-item label="名称"><el-input v-model="form.name" /></el-form-item>
      <el-form-item label="地址"><el-input v-model="form.address" /></el-form-item>
      <el-form-item label="电话"><el-input v-model="form.phone" /></el-form-item>
    </el-form>
    <template #footer>
      <el-button @click="handleSubmit" type="primary">提交</el-button>
    </template>
  </el-dialog>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { getRestaurants, createRestaurant, deleteRestaurant } from '../api/all'
import { ElMessage } from 'element-plus'

const tableData = ref([])
const dialogVisible = ref(false)
const form = reactive({ name: '', address: '', phone: '' })

const loadData = async () => tableData.value = await getRestaurants()
const handleDelete = async (id) => { await deleteRestaurant(id); loadData() }
const handleSubmit = async () => {
  await createRestaurant(form)
  ElMessage.success('成功')
  dialogVisible.value = false
  loadData()
}
onMounted(loadData)
</script>