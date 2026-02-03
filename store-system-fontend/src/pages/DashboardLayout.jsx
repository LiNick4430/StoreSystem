import { Outlet } from 'react-router-dom';

import NavBar from '../components/NavBar'
import Footer from '../components/Footer'

function DashboardLayout({ onLogout }) {

  return (
    <div className="admin-container">
      <NavBar onLogout={onLogout} /> {/* 這裡面會有 Link 指向 /dashboard/product 等 */}

      <main className="admin-main-content">
        {/* 這裡會根據網址，自動載入 ProductPage 或其他內容 */}
        <Outlet />
      </main>

      <Footer />
    </div>
  );

}

export default DashboardLayout