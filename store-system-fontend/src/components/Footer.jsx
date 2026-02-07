import './Footer.css'

function Footer() {
  const currentYear = new Date().getFullYear();

  return (
    <footer className="admin-footer">
      <div className="footer-left">
        <span>© {currentYear} 倉儲管理系統 v1.0.0</span>
      </div>
      <div className="footer-right">
        <span className="status-indicator">連線正常</span>
      </div>
    </footer>
  )

}

export default Footer