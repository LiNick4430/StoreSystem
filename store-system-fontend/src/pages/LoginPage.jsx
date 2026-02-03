import "./LoginPage.css";

function LoginPage({ onLogin }) {
  const handleLogin = (e) => {
    e.preventDefault();

    // 未來的登入邏輯

    console.log("登入按鈕被點擊了");
    onLogin();
  }

  return (
    <div className="login-container">
      <div className="login-box">
        <h1> 系統登入 </h1>
        <form onSubmit={handleLogin}>
          <div className="input-group">
            <label>帳號</label>
            <input type="text" placeholder="請輸入帳號(隨便填)" />
          </div>
          <div className="input-group">
            <label>密碼</label>
            <input type="text" placeholder="請輸入密碼(隨便填)" />
          </div>
          <button type="submit" className="login-button">
            登入系統
          </button>
        </form>
      </div>
    </div>
  );
}

export default LoginPage