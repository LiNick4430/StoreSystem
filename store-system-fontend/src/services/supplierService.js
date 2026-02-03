import { request } from '../api/apiClinet'
import { searchAllMap, searchMap } from '../api/supplier/supplier.api'

const SERVICE_BASE_URL = "/supplier";

/**
 * 搜尋大量供應商
 * @param {string} type 搜尋方式
 * @param {number} keyword 商品ID (number)
 * @param {number} page 頁碼
 * @param {number} size 大小
 * @returns {Promise<Object>} 該頁的資料
 */
export const searchAllSupplier = async (type, keyword, page, size) => {
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
 * 搜尋單一供應商
 * @param {string} type 搜尋方式
 * @param {string | number} keyword 供應商ID(number) 或 供應商統編(string)
 * @returns {Promise<Object>} 供應商資料
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
 * 建立新的供應商
 * @param {string} supplierName 供應商名稱
 * @param {string} taxId 供應商統編
 * @param {string} address 供應商地址
 * @param {string} phone 供應商電話
 * @returns {Promise<Object>} 供應商資料
*/
export const createSupplier = async (supplierName, taxId, address, phone) => {
  // 1. 建立基本設定檔
  const url = '/create';
  const config = {
    method: 'POST',
    body: JSON.stringify({ name: supplierName, taxId, address, phone })
  };

  // 2. 執行 API
  return request(SERVICE_BASE_URL, url, config);
};

/**
 * 更新供應商資料
 * @param {number} id 供應商ID
 * @param {string} supplierName 供應商名稱
 * @param {string} address 供應商地址
 * @param {string} phone 供應商電話
 * @returns {Promise<Object>} 供應商資料
*/
export const updateSupplier = async (id, supplierName, address, phone) => {
  // 1. 建立基本設定檔
  const url = '/update';
  const config = {
    method: 'POST',
    body: JSON.stringify({ id, name: supplierName, address, phone })
  };

  // 2. 執行 API
  return request(SERVICE_BASE_URL, url, config);
};

/**
 * 刪除供應商
 * @param {number} id 供應商ID
 * @returns {Promise<Object>} 供應商資料
*/
export const deleteSupplier = async (id) => {
  // 1. 建立基本設定檔
  const url = '/delete';
  const config = {
    method: 'POST',
    body: JSON.stringify({ id })
  };

  // 2. 執行 API
  return request(SERVICE_BASE_URL, url, config);
};