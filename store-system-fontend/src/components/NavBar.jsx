import { NavLink, useNavigate } from 'react-router-dom';

import './NavBar.css'

function NavBar({ onLogout }) {

  const navigate = useNavigate();

  const handleLogoutClick = () => {
    // 登出邏輯
    onLogout();

    // 強制導回 登入頁
    navigate('/login');
  };

  return (
    <nav className='navbar'>
      <div className='nav-logo'>倉儲管理系統</div>
      <div className='nav-link'>
        {/* end 屬性確保只有完全匹配 /dashboard/product 時 才會亮起 */}
        <NavLink to="/dashboard/product" className={({ isActive }) => isActive ? 'nav-item active' : 'nav-item'}>
          商品管理
        </NavLink>

        <NavLink to="/dashboard/supplier" className={({ isActive }) => isActive ? 'nav-item active' : 'nav-item'}>
          供應商管理
        </NavLink>

        <NavLink to="/dashboard/order" className={({ isActive }) => isActive ? 'nav-item active' : 'nav-item'}>
          進貨單
        </NavLink>
      </div>
      <div className='nav-user'>
        <span className='user-name'>管理員 A</span>
        <button onClick={handleLogoutClick} className='logout-btn'>登出</button>
      </div>
    </nav>
  )

}

export default NavBar