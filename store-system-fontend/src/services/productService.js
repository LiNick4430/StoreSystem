const API_BASE_URL = "http://localhost:8080";
const SERVICE_BASE_URL = "/product";

/** 
 * 搜尋商品 
 * @param {number} page 頁碼
 * @param {number} size 大小
 * @returns {Promise<Object>} 該頁的資料
*/
export const searchAll = async (type, keyword, page, size) => {
  // 1. 建立基本設定檔
  const config = {
    method: 'POST',
    headers: {
      "Content-Type": "application/json",
    },
    credentials: 'include'
  };

  // 2. 根據 type 分流
  let url = '';

  if (type === 'all') {
    config.body = JSON.stringify({ page, size });
    url = '/find/all/page';
  } else if (type === 'name') {
    config.body = JSON.stringify({ name: keyword, page, size });
    url = '/find/all/product/name/page';
  } else if (type === 'supplier') {
    config.body = JSON.stringify({ supplierId: keyword, page, size });
    url = '/find/all/supplier/page';
  }

  // 3. 執行 API + 處理 結果
  const response = await fetch(`${API_BASE_URL}${SERVICE_BASE_URL}${url}`, config);

  let responseData = null;
  try {
    responseData = await response.json();
  } catch (e) {
    console.warn(`搜尋商品 Response Body 無法解析為 JSON 或為空。`, e);
  }

  if (!response.ok) {
    if (responseData && responseData.message) {
      throw new Error(`錯誤類型：${responseData.errorCode}, 錯誤訊息：${responseData.message}`);
    } else {
      throw new Error(`網路錯誤，無法連接伺服器或解析錯誤訊息。狀態碼: ${response.status}`);
    }
  }

  if (responseData.code !== 200) {
    throw new Error(responseData.message || "搜尋成功後，業務碼異常。");
  }

  return responseData;
};