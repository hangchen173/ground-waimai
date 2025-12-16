<template>
  <el-card>
    <template #header>
      <div style="display: flex; justify-content: space-between">
        <span>🍽️ 餐桌列表</span>
        <el-button v-if="userRole === 'ROLE_ADMIN'" type="primary" @click="openCreateDialog">
          新增餐桌
        </el-button>
      </div>
    </template>

    <el-table :data="tableData" stripe>
      <el-table-column prop="id" label="ID" width="60" />
      
      <!-- 👇 核心修改：前端字典匹配 -->
      <el-table-column label="所属餐厅" min-width="120">
        <template #default="{ row }">
          <el-tag type="info">
            <!-- 拿着ID去字典里查名字，查不到就显示ID -->
            {{ restaurantMap[row.restaurantId] || ('ID: ' + row.restaurantId) }}
          </el-tag>
        </template>
      </el-table-column>
      
      <el-table-column prop="tableNumber" label="桌号" width="100">
        <template #default="{ row }">
          <span style="font-weight: bold">{{ row.tableNumber }}号桌</span>
        </template>
      </el-table-column>
      
      <el-table-column prop="capacity" label="座位数" width="100">
        <template #default="{ row }">
          {{ row.capacity }} 人座
        </template>
      </el-table-column>

      <!-- 操作列 (保持不变) -->
      <el-table-column label="操作" width="150">
        <template #default="{ row }">
          <el-button 
            v-if="userRole === 'ROLE_ADMIN'" 
            type="danger" link 
            @click="handleDelete(row.id)"
          >
            删除
          </el-button>
          <el-button 
            v-else 
            type="primary" size="small" 
            @click="handleBook(row)"
          >
            立即预订
          </el-button>
        </template>
      </el-table-column>
    </el-table>
  </el-card>

  <!-- 管理员弹窗 (保持不变) -->
  <el-dialog v-model="createDialogVisible" title="新增餐桌">
    <el-form :model="createForm" label-width="100px">
      <!-- 优化：这里也可以做成下拉选择框，而不是填ID -->
      <el-form-item label="选择餐厅">
        <el-select v-model="createForm.restaurantId" placeholder="请选择餐厅">
          <el-option
            v-for="(name, id) in restaurantMap"
            :key="id"
            :label="name"
            :value="Number(id)"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="桌号"><el-input v-model.number="createForm.tableNumber" /></el-form-item>
      <el-form-item label="容量"><el-input-number v-model="createForm.capacity" :min="1" /></el-form-item>
    </el-form>
    <template #footer>
      <el-button @click="submitCreate" type="primary">提交</el-button>
    </template>
  </el-dialog>

  <!-- 顾客预订弹窗 (保持不变) -->
  <el-dialog v-model="bookDialogVisible" title="预约定位" width="500px">
    <el-form :model="bookForm" label-width="100px">
      <el-form-item label="餐厅">
        <!-- 自动显示名字 -->
        <el-input :model-value="restaurantMap[bookForm.tableId] || '未知'" disabled />
      </el-form-item>
      <el-form-item label="桌号">
        <el-tag>{{ bookForm.tableNumber }}号桌 ({{ bookForm.capacity }}人)</el-tag>
      </el-form-item>
      <el-form-item label="您的客户ID">
        <el-input v-model.number="bookForm.customerId" placeholder="请输入您的客户ID" />
      </el-form-item>
      <el-form-item label="预订时间">
        <el-date-picker 
          v-model="bookForm.reservationTime" 
          type="datetime" 
          format="YYYY-MM-DD HH:mm"
          value-format="YYYY-MM-DDTHH:mm:ss"
        />
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button @click="submitBooking" type="primary">确认预订</el-button>
    </template>
  </el-dialog>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
// 👇 引入 getRestaurants
import { getTables, createTable, deleteTable, createReservation, getRestaurants } from '../api/all'
import { ElMessage } from 'element-plus'
import { jwtDecode } from "jwt-decode"

const tableData = ref([])
const userRole = ref('')
const restaurantMap = ref({}) // 📔 这是一个字典：{ 1: "KFC", 2: "麦当劳" }

const createDialogVisible = ref(false)
const createForm = reactive({ restaurantId: '', tableNumber: '', capacity: 4 })

const bookDialogVisible = ref(false)
const bookForm = reactive({ 
  tableId: '', 
  tableNumber: '', 
  capacity: '',
  customerId: '', 
  reservationTime: '',
  durationMinutes: 60
})

// 👇 核心逻辑：同时加载两个数据，并生成字典
const loadData = async () => {
  try {
    // 并行请求：同时去后端拿桌子和餐厅
    const [tablesRes, restaurantsRes] = await Promise.all([
      getTables(),
      getRestaurants()
    ])
    
    // 1. 保存桌子数据
    tableData.value = tablesRes
    
    // 2. 生成餐厅字典：把数组转成 Key-Value 对象
    // 原数据：[{id:1, name:"KFC"}, {id:2, name:"麦当劳"}]
    // 转换后：{ 1: "KFC", 2: "麦当劳" }
    const map = {}
    restaurantsRes.forEach(r => {
      map[r.id] = r.name
    })
    restaurantMap.value = map
    
  } catch (e) {
    console.error(e)
  }
}

onMounted(() => {
  const token = localStorage.getItem('token')
  if (token) {
    try {
      const decoded = jwtDecode(token)
      userRole.value = decoded.role || 'ROLE_CUSTOMER'
    } catch {}
  }
  loadData()
})

const openCreateDialog = () => {
  createForm.restaurantId = ''
  createForm.tableNumber = ''
  createDialogVisible.value = true
}

const submitCreate = async () => {
  if (!createForm.restaurantId) return ElMessage.warning("请选择餐厅")
  await createTable(createForm)
  ElMessage.success('餐桌创建成功')
  createDialogVisible.value = false
  loadData()
}

const handleDelete = async (id) => {
  await deleteTable(id)
  loadData()
}

const handleBook = (row) => {
  // 此时 row 里虽然没有 restaurantName，但我们可以通过 row.restaurantId 去 map 里查
  bookForm.tableId = row.id
  // bookForm.restaurantId = row.restaurantId // 记录一下ID方便查字典
  bookForm.tableNumber = row.tableNumber
  bookForm.capacity = row.capacity
  bookDialogVisible.value = true
}

const submitBooking = async () => {
  if (!bookForm.customerId || !bookForm.reservationTime) {
    ElMessage.warning('请填写完整信息')
    return
  }
  try {
    await createReservation({
      customerId: bookForm.customerId,
      tableId: bookForm.tableId,
      reservationTime: bookForm.reservationTime,
      durationMinutes: 60,
      numGuests: bookForm.capacity
    })
    ElMessage.success('预订成功！请准时就餐')
    bookDialogVisible.value = false
  } catch (e) {}
}
</script>