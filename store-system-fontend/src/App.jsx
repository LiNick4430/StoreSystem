import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import { useState } from 'react'

import LoginPage from './pages/LoginPage'
import DashboardLayout from './pages/DashboardLayout'

import ProductPage from './pages/ProductPage';
import SupplierPage from './pages/SupplierPage';

import './App.css'

function App() {
  const [isLoggedIn, setIsLoggedIn] = useState(false);

  return (
    <Router>
      <Routes>
        {/* 登入頁面 如果已登入 則轉到 儀表板頁面 */}
        <Route path='/login' element={
          isLoggedIn ? <Navigate to="/dashboard/home" /> : <LoginPage onLogin={() => setIsLoggedIn(true)} />
        } />

        {/* 儀表板頁面 如果未登入 則轉到 登入頁面 */}
        <Route path='/dashboard' element={
          isLoggedIn ? <DashboardLayout onLogout={() => setIsLoggedIn(false)} /> : <Navigate to='/login' />
        }>
          {/* 巢狀路由 */}
          <Route path="home" element={<div>儀錶板主頁面</div>} />
          <Route path="product" element={<ProductPage />} />
          <Route path="supplier" element={<SupplierPage />} />
          <Route path="order" element={<div>進貨單管理頁面</div>} />
        </Route>

        {/* 預設路徑 */}
        <Route path="*" element={<Navigate to="/login" />} />
      </Routes>
    </Router >
  )
}

export default App
