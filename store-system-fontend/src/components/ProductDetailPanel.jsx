import { useState, useEffect } from 'react';
import './ProductDetailPanel.css'

import { updateProduct } from '../services/productService'
import { searchAllPS, linkPS, unLinkPS } from '../services/productSupplierService'
import { searchAllSupplier } from '../services/supplierService';

function ProductDetailPanel({ product, onRefresh, onClose }) {
  // 將傳入的 product 轉為內部的 state
  const [formData, setFormData] = useState({ ...product });

  // 用於 暫時儲存 商品/供應商的關聯
  const [quotes, setQuotes] = useState([]);

  // 用於 新增 新關聯 的狀態/設置
  const [isAdding, setIsAdding] = useState(false);
  const [newQuote, setNewQuote] = useState({ supplierId: '', defaultCost: '' });

  // 存放供應商清單的 state
  const [suppliers, setSuppliers] = useState([]);

  // 當左側選中的商品改變時，同步更新內部的 formData
  useEffect(() => {
    setFormData({ ...product });  // 把 商品 放入 formData
    fetchQuotes();        // product.id 改變 就找對應的供應商關係
    fetchAllSuppliers();  // 抓取所有供應商
    setIsAdding(false);   // 更新為預設值
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

  // 獲取 和商品 關聯的 (供應商)報價
  const fetchQuotes = async () => {
    try {
      const response = await searchAllPS('productId', product.id, 1, 100);

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
      const response = await unLinkPS(product.id, supplierId);

      if (response) {
        alert(response.message);
        fetchQuotes();
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

      const response = await linkPS(product.id, newQuote.supplierId, newQuote.defaultCost);

      if (response) {
        alert("新增關聯成功！");
        setIsAdding(false); // 關閉新增列
        setNewQuote({ supplierId: '', defaultCost: '' }); // 清空輸入
        fetchQuotes(); // 重新整理下方報價列表
      }

    } catch (error) {
      console.error(error);
      alert("抓取失敗：" + error.message);
    }
  }

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
          <input name="barcode" type='text' value={formData.barcode} readOnly={true} className="input-readonly" />
        </div>
        <div className='detail-item'>
          <label>規格</label>
          <input name="spec" type='text' value={formData.spec} onChange={handleChange} />
        </div>
        <div className='detail-item'>
          <label>售價</label>
          <input name="price" type='number' value={formData.price} onChange={handleChange} />
        </div>
        <button className="update-btn" onClick={handleUpdate}>更新商品資訊</button>
      </div>

      {/* 下半部：供應商與報價 */}
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
            quotes.map((q) => (
              <tr key={`${q.productId}-${q.supplierId}`}>
                <td>{q.supplierName}</td>
                <td>{q.supplierTaxID}</td>
                <td>$ {q.defaultCost}</td>
                <td>
                  <button
                    className="delete-btn-sm"
                    onClick={() => handleUnLink(q.supplierId, q.supplierName)}>
                    取消
                  </button>
                </td>
              </tr>
            ))
          ) : (
            <tr>
              <td colSpan="4" className="empty-quote-cell">
                目前無供應商報價
              </td>
            </tr>
          )}

          {/* 新增列：當點擊按鈕後出現 */}
          {isAdding && (
            <tr className="adding-row">
              <td>
                <select
                  name="supplierId"
                  value={newQuote.supplierId}
                  onChange={handleNewQuoteChange}
                  className="inline-select"
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
                />
              </td>
              <td>
                <div className="action-btns">
                  <button className="save-btn-sm" onClick={handleAddLink}>儲存</button>
                  <button className="cancel-btn-sm" onClick={() => setIsAdding(false)}>取消</button>
                </div>
              </td>
            </tr>
          )}
        </tbody>
      </table>

      {!isAdding && (
        <button className="add-quote-btn" onClick={() => setIsAdding(true)}>
          + 新增供應商報價
        </button>
      )}

    </div>
  )

}

export default ProductDetailPanel