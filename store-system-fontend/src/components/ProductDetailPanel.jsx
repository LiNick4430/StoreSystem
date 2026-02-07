import { useState, useEffect } from 'react';
import './ProductDetailPanel.css'

import { createProduct, updateProduct, deleteProduct } from '../services/productService'
import { searchAllPS, linkPS, unLinkPS, updatePSCost } from '../services/productSupplierService'
import { searchAllSupplier } from '../services/supplierService';

function ProductDetailPanel({ product, onRefresh, onClose }) {
  // 將傳入的 product 轉為內部的 state
  const [formData, setFormData] = useState({ ...product });

  // 用於 暫時儲存 商品/供應商的關聯
  const [quotes, setQuotes] = useState([]);

  // 存放供應商清單的 state
  const [suppliers, setSuppliers] = useState([]);

  // 用於 新增 新關聯 的狀態/設置
  const [isAdding, setIsAdding] = useState(false);
  const [newQuote, setNewQuote] = useState({ supplierId: '', defaultCost: '' });

  // 更新報價 的 狀態
  const [editingSupplierId, setEditingSupplierId] = useState(null); // 紀錄哪一家在編輯
  const isAnyRowEditing = editingSupplierId !== null;               // 用來 鎖住其他的按鈕 使用
  const [editCost, setEditCost] = useState(""); // 紀錄編輯中的報價

  // 當左側選中的商品改變時，同步更新內部的 formData
  useEffect(() => {
    setFormData({ ...product });  // 把 商品 放入 formData
    fetchQuotes();        // product.id 改變 就找對應的供應商關係
    fetchAllSuppliers();  // 抓取所有供應商

    // 更新為預設值
    setIsAdding(false);
    setNewQuote({ supplierId: '', defaultCost: '' });
    setEditingSupplierId(null);
    setEditCost("");

  }, [product]);

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

  // 建立 新商品 
  const handleCreate = async () => {
    try {
      // 直接從 state 拿數據
      const response = await createProduct(formData.name, formData.barcode, formData.spec, formData.price);

      if (response && response.data) {
        alert("建立成功！");
        // 1. 取出 建立的新商品 (獲得ID)
        const createdProduct = response.data;
        // 2. 把 新商品 存入 暫存區
        setFormData(createdProduct);
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
      const response = await updateProduct(formData.id, formData.name, formData.spec, formData.price);

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

  // 刪除商品
  const handDelete = async () => {
    // 安全確認 避免誤點
    if (!window.confirm(`確定要刪除 商品名: ${formData.name} 條碼:(${formData.barcode}) 嗎?`)) return;

    try {
      // 直接從 state 拿數據
      const response = await deleteProduct(formData.id);

      if (response) {
        alert("商品已經刪除！");
        if (onRefresh) onRefresh();
        onClose();
      }

    } catch (error) {
      console.error(error);
      alert("刪除失敗：" + error.message);
    }
  }

  // 獲取 和商品 關聯的 (供應商)報價
  const fetchQuotes = async (idOvrride = null) => {
    // 1. 優先順序 手動傳入的 ID > 目前 Props 的 ID
    const targetId = idOvrride || product.id;

    // 當 + 建立商品 進入的時候 (product id = null) 直接回傳 空集合 
    if (targetId === null) {
      setQuotes([]);
      return;
    };

    try {
      const response = await searchAllPS('productId', targetId, 1, 100);

      if (response && response.data) {
        setQuotes(response.data.content || []);
      }

    } catch (error) {
      console.error(error);
      alert("抓取失敗：" + error.message);
    }
  }

  // 取消商品 與供應商的 關聯
  const handleUnLink = async (supplierId, supplierName) => {
    // 安全確認 避免誤點
    if (!window.confirm(`確定要取消與供應商 (${supplierName}) 報價關係嗎?`)) return;

    // 執行 API
    try {
      const response = await unLinkPS(formData.id, supplierId);

      if (response) {
        alert(response.message);
        fetchQuotes(formData.id);
      }

    } catch (error) {
      console.error(error);
      alert("抓取失敗：" + error.message);
    }
  }

  // 新增 與供應商 的關聯
  const handleAddLink = async () => {
    try {
      if (!newQuote.supplierId || !newQuote.defaultCost) {
        alert("請輸入供應商 ID 與報價");
        return;
      }

      const response = await linkPS(formData.id, newQuote.supplierId, newQuote.defaultCost);

      if (response) {
        alert("新增關聯成功！");
        setIsAdding(false); // 關閉新增列
        setNewQuote({ supplierId: '', defaultCost: '' }); // 清空輸入
        fetchQuotes(formData.id); // 重新整理下方報價列表
      }

    } catch (error) {
      console.error(error);
      alert("抓取失敗：" + error.message);
    }
  }

  // 更新 供應商 的 報價
  const handleUpdateQuote = async (supplierId) => {
    try {
      if (!editCost) {
        alert("請輸入報價金額");
        return;
      }

      const response = await updatePSCost(formData.id, supplierId, editCost);

      if (response) {
        alert("報價更新成功！");
        setEditingSupplierId(null); // 關閉編輯狀態
        fetchQuotes(formData.id); // 重新整理列表
      }

    } catch (error) {
      console.error(error);
      alert("更新失敗：" + error.message);
    }
  }

  // 搜尋目前存在的供應商
  const fetchAllSuppliers = async () => {
    try {
      // 假設供應商不會超過100家
      const response = await searchAllSupplier('all', '', 1, 100);
      if (response && response.data) {
        setSuppliers(response.data.content || []);
      }
    } catch (error) {
      console.error("獲取供應商清單失敗:", error);
    }
  }

  return (
    <div className='detail-panel-content'>
      {/* 標題與關閉按鈕容器 */}
      <div className="detail-header">
        <h2>商品明細</h2>
        <button className="close-btn" onClick={onClose}>&times;</button>
      </div>

      {/* 上半部：商品資訊表單 */}
      <div className='detail-form'>
        <div className='detail-item'>
          <label>商品名稱</label>
          <input name="name" type='text' value={formData.name} onChange={handleChange} />
        </div>
        <div className='detail-item'>
          <label>商品條碼</label>
          {formData.id ? (
            <input name="barcode" type='text' value={formData.barcode} readOnly={true} className="input-readonly" />
          ) : (
            <input name="barcode" type='text' value={formData.barcode} onChange={handleChange} />
          )}
        </div>
        <div className='detail-item'>
          <label>規格</label>
          <input name="spec" type='text' value={formData.spec} onChange={handleChange} />
        </div>
        <div className='detail-item'>
          <label>售價</label>
          <input name="price" type='number' value={formData.price} onChange={handleChange} />
        </div>
        <div className='detail-actions'>
          {formData.id ? (
            <>
              <button
                className="update-btn"
                disabled={isAdding || isAnyRowEditing}
                onClick={handleUpdate}>
                更新商品資訊
              </button>
              <button
                className="delete-btn-main"
                disabled={isAdding || isAnyRowEditing || quotes.length > 0}
                onClick={handDelete}
                title={quotes.length > 0 ? "請先取消所有供應商關聯才能刪除商品" : ""}>
                刪除此商品
              </button>
            </>
          ) : (
            <button
              className="update-btn"
              disabled={isAdding || isAnyRowEditing}
              onClick={handleCreate}>
              建立新商品
            </button>
          )}
        </div>
      </div>

      {/* 下半部：供應商與報價, 當 新增商品 時(沒有ID) 則不顯示下半 */}
      {formData.id ? (
        <>
          <h3>供應商報價關係</h3>
          <table className="quote-table">
            <thead>
              <tr>
                <th>供應商</th>
                <th>統編</th>
                <th>報價</th>
                <th>操作</th>
              </tr>
            </thead>
            <tbody>
              {quotes.length > 0 ? (
                quotes.map((q) => {
                  // 1. 在這裡定義判斷式
                  const isEditingThisRow = editingSupplierId === q.supplierId;

                  return (
                    <tr key={`${q.productId}-${q.supplierId}`}>
                      <td>{q.supplierName}</td>
                      <td>{q.supplierTaxID}</td>

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
                                onClick={() => handleUpdateQuote(q.supplierId)}>
                                儲存報價
                              </button>
                              <button
                                className="cancel-btn-sm"
                                onClick={() => setEditingSupplierId(null)}>
                                取消更新
                              </button>
                            </>
                          ) : (
                            <>
                              <button
                                className="edit-btn-sm"
                                disabled={isAnyRowEditing || isAdding}
                                onClick={() => {
                                  setEditingSupplierId(q.supplierId);
                                  setEditCost(q.defaultCost); // 把舊報價帶入 input
                                }}>
                                更新報價
                              </button>

                              <button
                                className="delete-btn-sm"
                                disabled={isAnyRowEditing || isAdding}
                                onClick={() => handleUnLink(q.supplierId, q.supplierName)}>
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
                      name="supplierId"
                      value={newQuote.supplierId}
                      onChange={handleNewQuoteChange}
                      className="inline-select"
                      disabled={isAnyRowEditing}
                    >
                      <option value="">請選擇供應商</option>
                      {(() => {
                        // 1. 先計算過濾後的清單
                        const availableSuppliers = suppliers.filter(
                          sup => !quotes.some(q => q.supplierId === sup.id)
                        );

                        // 2. 判斷長度
                        if (availableSuppliers.length === 0) {
                          return <option disabled>所有供應商皆已設定報價</option>;
                        }

                        // 3. 有資料才跑 map
                        return availableSuppliers.map(sup => (
                          <option key={sup.id} value={sup.id}>
                            {sup.name}
                          </option>
                        ));
                      })()}
                    </select>
                  </td>
                  <td className="text-muted">
                    {suppliers.filter(sup => !quotes.some(q => q.supplierId === sup.id)).length === 0
                      ? "無可選供應商"
                      : (newQuote.supplierId
                        ? suppliers.find(s => s.id === parseInt(newQuote.supplierId))?.taxId
                        : "請選擇供應商")}
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
              + 新增供應商報價
            </button>
          )}
        </>
      ) : (
        <div className="new-product-notice">
          <p>💡 請先完成「建立商品」後，再設定供應商報價關係。</p>
        </div>
      )}

    </div>
  )

}

export default ProductDetailPanel