import request from './axios'

// --- 客户 (Customers) ---
export const getCustomers = () => request.get('/customers')
export const createCustomer = (data) => request.post('/customers', data)
export const deleteCustomer = (id) => request.delete(`/customers/${id}`)

// --- 餐厅 (Restaurants) ---
export const getRestaurants = () => request.get('/restaurants')
export const createRestaurant = (data) => request.post('/restaurants', data)
export const deleteRestaurant = (id) => request.delete(`/restaurants/${id}`)

// --- 餐桌 (Tables) ---
export const getTables = () => request.get('/tables')
export const createTable = (data) => request.post('/tables', data)
export const deleteTable = (id) => request.delete(`/tables/${id}`)

// --- 预订 (Reservations) ---
export const getReservations = () => request.get('/reservations')
export const createReservation = (data) => request.post('/reservations', data)
export const deleteReservation = (id) => request.delete(`/reservations/${id}`)