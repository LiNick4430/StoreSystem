import { useState, useEffect } from 'react';
import './SupplierDetailPanel.css'

import { searchAllProduct } from '../services/productService'
import { searchAllPS, updatePSCost, unLinkPS, linkPS } from '../services/productSupplierService'
import { createSupplier, updateSupplier, deleteSupplier } from '../services/supplierService';


function SupplierDetailPanel({ supplier, onRefresh, onClose }) {
  // 將傳入的 supplier 轉為內部的 state
  const [formData, setFormData] = useState({ ...supplier });

  // 用於 暫時儲存 商品/供應商的關聯
  const [quotes, setQuotes] = useState([]);

  // 存放商品清單的 state
  const [products, setProducts] = useState([]);

  // 用於 新增 新關聯 的狀態/設置
  const [isAdding, setIsAdding] = useState(false);
  const defaultNewQuote = { productId: '', defaultCost: '' };
  const [newQuote, setNewQuote] = useState(defaultNewQuote);

  // 更新報價 的 狀態
  const [editingProductId, setEditingProductId] = useState(null); // 紀錄哪一商品在編輯
  const isAnyRowEditing = editingProductId !== null;               // 用來 鎖住其他的按鈕 使用
  const [editCost, setEditCost] = useState(""); // 紀錄編輯中的報價

  // 當左側選中的商品改變時，同步更新內部的 formData
  useEffect(() => {
    setFormData({ ...supplier });  // 把 商品 放入 formData
    fetchQuotes();          // supplier.id 改變 就找對應的供應商關係
    fetchAllProducts();// 抓取所有商品

    // 更新為預設值
    setIsAdding(false);
    setNewQuote(defaultNewQuote);
    setEditingProductId(null);
    setEditCost("");

  }, [supplier]);

  // 雙向綁定 當 name 更新的時候 同步更新 formData 內 特定欄位的數值
  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData(prev => ({ ...prev, [name]: value }));
  };

  // 雙向綁定 當 name 更新的時候 同步更新 newQuote 內 特定欄位的數值
  const handleNewQuoteChange = (e) => {
    const { name, value } = e.target;
    setNewQuote(prev => ({ ...prev, [name]: value }));
  };

  // 建立 新供應商
  const handleCreate = async () => {
    try {
      // 直接從 state 拿數據
      const response = await createSupplier(formData.name, formData.taxId, formData.address, formData.phone);

      if (response && response.data) {
        alert("建立成功！");
        // 1. 取出 建立的新供應商 (獲得ID)
        const createdSupplier = response.data;
        // 2. 把 新供應商 存入 暫存區
        setFormData(createdSupplier);
        // 3. 呼叫從父組件傳下來的 onRefresh，讓左側列表也同步更新
        if (onRefresh) onRefresh();
      }

    } catch (error) {
      console.error(error);
      alert("建立失敗：" + error.message);
    }
  }

  // 更新資料 同時 呼叫 父件onRefresh 重新搜尋 
  const handleUpdate = async () => {
    try {
      // 直接從 state 拿數據
      const response = await updateSupplier(formData.id, formData.name, formData.address, formData.phone);

      if (response && response.data) {
        alert("更新成功！");
        setFormData(response.data);
        // 2. 呼叫從父組件傳下來的 onRefresh，讓左側列表也同步更新
        if (onRefresh) onRefresh();
      }

    } catch (error) {
      console.error(error);
      alert("抓取失敗：" + error.message);
    }
  };

  // 刪除供應商
  const handDelete = async () => {
    // 安全確認 避免誤點
    if (!window.confirm(`確定要刪除 供應商: ${formData.name} 統碼:(${formData.taxId}) 嗎?`)) return;

    try {
      // 直接從 state 拿數據
      const response = await deleteSupplier(formData.id);

      if (response) {
        alert("供應商已經刪除！");
        if (onRefresh) onRefresh();
        onClose();
      }

    } catch (error) {
      console.error(error);
      alert("刪除失敗：" + error.message);
    }
  }

  // 獲取 和供應商 關聯的 (商品)報價
  const fetchQuotes = async (idOvrride = null) => {
    // 1. 優先順序 手動傳入的 ID > 目前 Props 的 ID
    const targetId = idOvrride || supplier.id;

    // 當 + 建立商品 進入的時候 (supplier id = null) 直接回傳 空集合 
    if (targetId === null) {
      setQuotes([]);
      return;
    };

    try {
      const response = await searchAllPS('supplierId', targetId, 1, 100);

      if (response && response.data) {
        setQuotes(response.data.content || []);
      }

    } catch (error) {
      console.error(error);
      alert("抓取失敗：" + error.message);
    }
  }

  // 取消商品 與供應商的 關聯
  const handleUnLink = async (productId, productName, barcode) => {
    // 安全確認 避免誤點
    if (!window.confirm(`確定要取消供應 商品名(${productName}) 商品條碼(${barcode}) 報價關係嗎?`)) return;

    // 執行 API
    try {
      const response = await unLinkPS(productId, formData.id);

      if (response) {
        alert(response.message);
        fetchQuotes(formData.id);
      }

    } catch (error) {
      console.error(error);
      alert("抓取失敗：" + error.message);
    }
  }

  // 新增 供應商品
  const handleAddLink = async () => {
    try {
      if (!newQuote.productId || !newQuote.defaultCost) {
        alert("請輸入商品 ID 與報價");
        return;
      }

      const response = await linkPS(newQuote.productId, formData.id, newQuote.defaultCost);

      if (response) {
        alert("新增關聯成功！");
        setIsAdding(false); // 關閉新增列
        setNewQuote(defaultNewQuote); // 清空輸入
        fetchQuotes(formData.id); // 重新整理下方報價列表
      }

    } catch (error) {
      console.error(error);
      alert("抓取失敗：" + error.message);
    }
  }

  // 更新 商品 的 報價
  const handleUpdateQuote = async (productId) => {
    try {
      if (!editCost) {
        alert("請輸入報價金額");
        return;
      }

      const response = await updatePSCost(productId, formData.id, editCost);

      if (response) {
        alert("報價更新成功！");
        setEditingProductId(null); // 關閉編輯狀態
        fetchQuotes(formData.id); // 重新整理列表
      }

    } catch (error) {
      console.error(error);
      alert("更新失敗：" + error.message);
    }
  }

  // 搜尋目前存在的商品
  const fetchAllProducts = async () => {
    try {
      // 假設商品不會超過100種
      const response = await searchAllProduct('all', '', 1, 100);
      if (response && response.data) {
        setProducts(response.data.content || []);
      }
    } catch (error) {
      console.error("獲取供應商清單失敗:", error);
    }
  }

  return (
    <div className='detail-panel-content'>
      {/* 標題與關閉按鈕容器 */}
      <div className="detail-header">
        <h2>供應商明細</h2>
        <button className="close-btn" onClick={onClose}>&times;</button>
      </div>

      {/* 上半部：供應商資訊表單 */}
      <div className='detail-form'>
        <div className='detail-item'>
          <label>名稱</label>
          <input name="name" type='text' value={formData.name} onChange={handleChange} />
        </div>
        <div className='detail-item'>
          <label>統編</label>
          {formData.id ? (
            <input name="taxId" type='text' value={formData.taxId} readOnly={true} className="input-readonly" />
          ) : (
            <input name="taxId" type='text' value={formData.taxId} onChange={handleChange} />
          )}
        </div>
        <div className='detail-item'>
          <label>地址</label>
          <input name="address" type='text' value={formData.address} onChange={handleChange} />
        </div>
        <div className='detail-item'>
          <label>電話</label>
          <input name="phone" type='text' value={formData.phone} onChange={handleChange} />
        </div>
        <div className='detail-actions'>
          {formData.id ? (
            <>
              <button
                className="update-btn"
                disabled={isAdding || isAnyRowEditing}
                onClick={handleUpdate}>
                更新供應商資訊
              </button>
              <button
                className="delete-btn-main"
                disabled={isAdding || isAnyRowEditing || quotes.length > 0}
                onClick={handDelete}
                title={quotes.length > 0 ? "請先取消所有商品關聯才能刪除供應商" : ""}>
                刪除此供應商
              </button>
            </>
          ) : (
            <button
              className="update-btn"
              disabled={isAdding || isAnyRowEditing}
              onClick={handleCreate}>
              建立新供應商
            </button>
          )}
        </div>
      </div>

      {/* 下半部：供應商與報價, 當 新增供應商 時(沒有ID) 則不顯示下半 */}
      {formData.id ? (
        <>
          <h3>商品報價關係</h3>
          <table className="quote-table">
            <thead>
              <tr>
                <th>商品名稱</th>
                <th>商品條碼</th>
                <th>報價</th>
                <th>操作</th>
              </tr>
            </thead>
            <tbody>
              {quotes.length > 0 ? (
                quotes.map((q) => {
                  // 1. 在這裡定義判斷式
                  const isEditingThisRow = editingProductId === q.productId;

                  return (
                    <tr key={`${q.supplierId}-${q.productId}`}>
                      <td>{q.productName} {q.spec}</td>
                      <td>{q.barcode}</td>

                      {/* 2. 報價欄位切換：如果是正在編輯的那一列，顯示 input */}
                      <td>
                        {isEditingThisRow ? (
                          <input
                            type='number'
                            className='inline-input'
                            value={editCost}
                            onChange={(e) => setEditCost(e.target.value)}
                            autoFocus
                          />
                        ) : (
                          `$ ${q.defaultCost}`
                        )}
                      </td>

                      <td>
                        <div className="action-btns">
                          {/* 3. 按鈕邏輯切換 */}
                          {isEditingThisRow ? (
                            <>
                              <button
                                className="save-btn-sm"
                                onClick={() => handleUpdateQuote(q.productId)}>
                                儲存報價
                              </button>
                              <button
                                className="cancel-btn-sm"
                                onClick={() => setEditingProductId(null)}>
                                取消更新
                              </button>
                            </>
                          ) : (
                            <>
                              <button
                                className="edit-btn-sm"
                                disabled={isAnyRowEditing || isAdding}
                                onClick={() => {
                                  setEditingProductId(q.productId);
                                  setEditCost(q.defaultCost); // 把舊報價帶入 input
                                }}>
                                更新報價
                              </button>

                              <button
                                className="delete-btn-sm"
                                disabled={isAnyRowEditing || isAdding}
                                onClick={() => handleUnLink(q.productId, q.productName, q.barcode)}>
                                取消關聯
                              </button>
                            </>
                          )}
                        </div>
                      </td>
                    </tr>
                  );
                })
              ) : (
                <tr>
                  <td colSpan="4" className="empty-quote-cell">
                    目前無供應商報價
                  </td>
                </tr>
              )}

              {/* 新增列：當點擊 + 新增供應商報價 按鈕後出現 */}
              {isAdding && (
                <tr className="adding-row">
                  <td>
                    <select
                      name="productId"
                      value={newQuote.productId}
                      onChange={handleNewQuoteChange}
                      className="inline-select"
                      disabled={isAnyRowEditing}
                    >
                      <option value="">請選擇商品</option>
                      {(() => {
                        // 1. 先計算過濾後的清單
                        const availableProducts = products.filter(
                          sup => !quotes.some(q => q.productId === sup.id)
                        );

                        // 2. 判斷長度
                        if (availableProducts.length === 0) {
                          return <option disabled>已經對所有商品進行過報價</option>;
                        }

                        // 3. 有資料才跑 map
                        return availableProducts.map(sup => (
                          <option key={sup.id} value={sup.id}>
                            {sup.name} {sup.spec}
                          </option>
                        ));
                      })()}
                    </select>
                  </td>
                  <td className="text-muted">
                    {products.filter(sup => !quotes.some(q => q.productId === sup.id)).length === 0
                      ? "無可選商品"
                      : (newQuote.productId
                        ? products.find(s => s.id === parseInt(newQuote.productId))?.barcode
                        : "請選擇商品")}
                  </td>
                  <td>
                    <input
                      name="defaultCost"
                      type="number"
                      placeholder="報價"
                      value={newQuote.defaultCost}
                      onChange={handleNewQuoteChange}
                      className="inline-input"
                      disabled={isAnyRowEditing}
                    />
                  </td>
                  <td>
                    <div className="action-btns">
                      <button
                        className="save-btn-sm"
                        disabled={isAnyRowEditing}
                        onClick={handleAddLink}>
                        儲存新增
                      </button>
                      <button
                        className="cancel-btn-sm"
                        onClick={() => setIsAdding(false)}>
                        取消新增
                      </button>
                    </div>
                  </td>
                </tr>
              )}
            </tbody>
          </table>

          {!isAdding && (
            <button
              className="add-quote-btn"
              disabled={isAnyRowEditing}
              onClick={() => setIsAdding(true)}>
              + 新增商品報價
            </button>
          )}
        </>
      ) : (
        <div className="new-product-notice">
          <p>💡 請先完成「建立供應商」後，再設定商品報價關係。</p>
        </div>
      )}

    </div>
  )

}

export default SupplierDetailPanel