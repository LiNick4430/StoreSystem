import { request } from '../api/apiClinet'
import { searchAllMap, searchMap } from '../api/product/product.api'

const SERVICE_BASE_URL = "/product";

/** 
 * 搜尋大量商品
 * @param {string} type 搜尋方式
 * @param {string | number} keyword 供應商ID(number) 或 商品名稱(string)
 * @param {number} page 頁碼
 * @param {number} size 大小
 * @returns {Promise<Object>} 該頁的資料
*/
export const searchAllProduct = async (type, keyword, page, size) => {
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
 * 搜尋單一商品
 * @param {string} type 搜尋方式
 * @param {string | number} keyword 商品ID(number) 或 商品條碼(string)
 * @returns {Promise<Object>} 商品資料
*/
export const searchProduct = async (type, keyword) => {
  // 1. 建立基本設定檔
  const api = searchMap[type];
  if (!api) {
    throw new Error(`未知的搜尋類型: ${type}`);
  }

  const config = {
    method: 'POST',
    body: JSON.stringify(api.buildBody({ keyword }))
  };

  // 2. 執行 API
  return request(SERVICE_BASE_URL, api.url, config);
};

/**
 * 建立新的商品
 * @param {string} productName 商品名稱
 * @param {string} barcode 商品條碼
 * @param {string} spec 商品規格
 * @param {number} price 商品售價
 * @returns {Promise<Object>} 商品資料
*/
export const createProduct = async (productName, barcode, spec, price) => {
  // 1. 建立基本設定檔
  const url = '/create';
  const config = {
    method: 'POST',
    body: JSON.stringify({ name: productName, barcode, spec, price })
  };

  // 2. 執行 API
  return request(SERVICE_BASE_URL, url, config);
};

/**
 * 更新 商品內容
 * @param {number} id 商品ID
 * @param {string} productName 商品名稱
 * @param {string} spec 商品規格
 * @param {number} price 商品售價
 * @returns {Promise<Object>} 商品資料
 */
export const updateProduct = async (id, productName, spec, price) => {
  // 1. 建立基本設定檔
  const url = '/update';
  const config = {
    method: 'POST',
    body: JSON.stringify({ id, name: productName, spec, price })
  };

  // 2. 執行 API
  return request(SERVICE_BASE_URL, url, config);
};

/**
 * 更新商品銷售狀態
 * @param {number} id 商品ID
 * @param {boolean} isForSale 更新後的銷售狀態 (true = 銷售中, false = 不販售)
 * @returns {Promise<Object>} 商品資料
 */
export const setProductSaleStatus = async (id, isForSale) => {
  // 1. 建立基本設定檔
  const url = '/set/sale/status';
  const config = {
    method: 'POST',
    body: JSON.stringify({ id, isForSale })
  };

  // 2. 執行 API
  return request(SERVICE_BASE_URL, url, config);
};

/**
 * 刪除商品
 * @param {number} id 商品ID
 * @returns {Promise<void>} 刪除訊息
 */
export const deleteProduct = async (id) => {
  // 1. 建立基本設定檔
  const url = '/delete';
  const config = {
    method: 'DELETE',
    body: JSON.stringify({ id })
  };

  // 2. 執行 API
  return request(SERVICE_BASE_URL, url, config);
};