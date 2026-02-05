import { request } from '../api/apiClinet'
import { searchAllMap } from '../api/productSupplier/productSupplier.api'

const SERVICE_BASE_URL = "/ps";

/** 
 * 搜尋商品關聯
 * @param {string} type 搜尋方式
 * @param {string | number} keyword 供應商ID/商品ID(number) 或 供應商統編/商品名稱/商品條碼(string)
 * @param {number} page 頁碼
 * @param {number} size 大小
 * @returns {Promise<Object>} 該頁的資料
*/
export const searchAllPS = async (type, keyword, page, size) => {
  // 1. 建立基本設定檔
  const api = searchAllMap[type];
  if (!api) {
    throw new Error(`未知的搜尋類型: ${type}`);
  }

  const config = {
    method: 'POST',
    body: JSON.stringify(api.buildBody({ keyword, page, size }))
  };

  // 2. 執行 API
  return request(SERVICE_BASE_URL, api.url, config);
};

/**
 * 商品 和 供應商 建立新的關聯
 * @param {number} productId 商品ID
 * @param {number} supplierId 供應商ID
 * @param {number} defaultCost 供應商 報價
 * @returns {Promise<Object>} 商品/供應商 基本資料 和 報價關聯
 */
export const linkPS = async (productId, supplierId, defaultCost) => {
  // 1. 建立基本設定檔
  const url = '/link';
  const config = {
    method: 'POST',
    body: JSON.stringify({ productId, supplierId, defaultCost })
  };

  // 2. 執行 API
  return request(SERVICE_BASE_URL, url, config);
};

/**
 * 更新 商品 和 供應商 的報價
 * @param {number} productId 商品ID
 * @param {number} supplierId 供應商ID
 * @param {number} defaultCost 供應商 新報價
 * @returns {Promise<Object>} 商品/供應商 基本資料 和 報價關聯
 */
export const updatePSCost = async (productId, supplierId, defaultCost) => {
  // 1. 建立基本設定檔
  const url = '/update';
  const config = {
    method: 'POST',
    body: JSON.stringify({ productId, supplierId, defaultCost })
  };

  // 2. 執行 API
  return request(SERVICE_BASE_URL, url, config);
};

/**
 * 取消 商品 和 供應商 的關聯
 * @param {number} productId 商品ID
 * @param {number} supplierId 供應商ID
 * @returns {Promise<Object>} 某商品 和 某供應商 取消關聯
 */
export const unLinkPS = async (productId, supplierId) => {
  // 1. 建立基本設定檔
  const url = '/unlink';
  const config = {
    method: 'POST',
    body: JSON.stringify({ productId, supplierId })
  };

  // 2. 執行 API
  return request(SERVICE_BASE_URL, url, config);
};