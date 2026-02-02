const API_BASE_URL = "http://localhost:8080";

/** 
 * 通用請求工具
 * @param {string} servicePath - 伺服器路徑
 * @param {string} endPoint - API 端點
 * @param {RequestInit} [config] - 追加的 設定檔
*/
export const request = async (servicePath, endPoint, config = {}) => {
  // 合併 預設的 設定檔
  const finalConfig = {
    ...config,
    headers: {
      "Content-Type": "application/json",
      ...config.headers,
    },
    credentials: 'include'
  }

  // 執行 API
  const response = await fetch(`${API_BASE_URL}${servicePath}${endPoint}`, finalConfig);

  // 嘗試 回應 解析
  let responseData = null;
  try {
    responseData = await response.json();
  } catch (e) {
    console.warn(`Response Body 無法解析為 JSON 或為空。`, e);
  }

  // 1. 處理 HTTP 狀態碼錯誤 (4xx, 5xx)
  if (!response.ok) {
    if (responseData && responseData.message) {
      throw new Error(`錯誤類型：${responseData.errorCode}, 錯誤訊息：${responseData.message}`);
    } else {
      throw new Error(`網路錯誤，無法連接伺服器或解析錯誤訊息。狀態碼: ${response.status}`);
    }
  }

  // 2. 處理後端業務邏輯錯誤 (如果 code 不是 200)
  if (responseData.code !== 200) {
    throw new Error(responseData.message || "搜尋成功後，業務碼異常。");
  }

  return responseData;  // 回傳包含 data, message, code 的物件
}