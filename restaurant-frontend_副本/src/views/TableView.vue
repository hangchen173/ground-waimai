<template>
  <el-card>
    <template #header>
      <div style="display: flex; justify-content: space-between">
        <span>餐桌列表</span>
        <el-button type="primary" @click="dialogVisible = true">新增餐桌</el-button>
      </div>
    </template>
    <el-table :data="tableData" stripe>
      <el-table-column prop="id" label="ID" width="60" />
      <el-table-column prop="restaurantId" label="所属餐厅ID" />
      <el-table-column prop="tableNumber" label="桌号" />
      <el-table-column prop="capacity" label="容量(人)" />
      <el-table-column label="操作">
        <template #default="{ row }">
          <el-button type="danger" link @click="handleDelete(row.id)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
  </el-card>

  <el-dialog v-model="dialogVisible" title="新增餐桌">
    <el-form :model="form" label-width="100px">
      <!-- 为了快，直接填ID，不做下拉选择了 -->
      <el-form-item label="餐厅ID"><el-input v-model.number="form.restaurantId" type="number" /></el-form-item>
      <el-form-item label="桌号"><el-input v-model.number="form.tableNumber" type="number" /></el-form-item>
      <el-form-item label="容量"><el-input v-model.number="form.capacity" type="number" /></el-form-item>
    </el-form>
    <template #footer>
      <el-button @click="handleSubmit" type="primary">提交</el-button>
    </template>
  </el-dialog>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { getTables, createTable, deleteTable } from '../api/all'
import { ElMessage } from 'element-plus'

const tableData = ref([])
const dialogVisible = ref(false)
const form = reactive({ restaurantId: '', tableNumber: '', capacity: 4, available: true })

const loadData = async () => tableData.value = await getTables()
const handleDelete = async (id) => { await deleteTable(id); loadData() }
const handleSubmit = async () => {
  await createTable(form)
  ElMessage.success('成功')
  dialogVisible.value = false
  loadData()
}
onMounted(loadData)
</script>