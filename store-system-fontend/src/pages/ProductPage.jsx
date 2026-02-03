import { useState, useEffect } from 'react'
import './ProductPage.css'

import { searchAllProduct } from '../services/productService'

function ProductPage() {
  // 資料狀態
  const [products, setProducts] = useState([]);
  const [totalPages, setTotalPages] = useState(0);

  // 分頁與搜尋方式
  const [page, setPage] = useState(1);
  const [size, setSize] = useState(10);
  const [searchType, setSearchType] = useState("all");
  const [searchKeyWord, setSearchKeyWord] = useState("");

  // 定義 抓取資料函式
  const fetchData = async () => {
    try {
      const response = await searchAllProduct(searchType, searchKeyWord, page, size);

      if (response && response.data) {
        setProducts(response.data.content || []);
        setTotalPages(response.data.totalPages || 0);
      }
    } catch (error) {
      console.error(error);
      alert("抓取失敗：" + error.message);
    }
  }

  // 當 頁碼/大小 改變 重新型搜尋方法
  useEffect(() => {
    fetchData();
  }, [page, size])

  return (
    <>
      <div className='container'>
        <h1>商品搜尋</h1>

        {/* 搜尋欄位 */}
        <div className='search-bar'>
          <select
            className='search-type'
            value={searchType}
            onChange={(e) => {
              setSearchType(e.target.value);
              if (searchType === 'all') {
                setSearchKeyWord("");
              }
            }}
          >
            <option value="all">全部</option>
            <option value="name">商品名稱</option>
            <option value="supplier">供應商ID</option>
          </select>
          <input
            /* 當搜尋方式為 'all' 時，禁用輸入框 */
            disabled={searchType === 'all'}

            type={searchType === 'supplier' ? 'number' : 'text'}
            placeholder={searchType === 'all'
              ? '搜尋全部, 不需要關鍵字'
              : searchType === 'supplier'
                ? '請輸入供應商ID...' :
                '請輸入關鍵字...'}
            value={searchType === 'all' ? '' : searchKeyWord} // 搜尋全部時清空顯示文字
            onChange={(e) => setSearchKeyWord(e.target.value)}
            className={searchType === 'all' ? 'input-disabled' : ''}
          />
          <button onClick={() => {
            setPage(1);
            fetchData();
          }}>搜尋</button>
        </div>

        {/* 資料展示區 */}
        {products.length === 0 ? (
          /* 沒資料時顯示的區塊 */
          <div className="no-data-container">
            <p>🔍 找不到相關商品，請嘗試其他關鍵字。</p>
          </div>
        ) : (
          <table className='product-table'>
            <thead>
              <tr>
                <th>ID</th>
                <th>名稱</th>
                <th>條碼</th>
                <th>規格</th>
                <th>售價</th>
                <th>庫存量</th>
                <th>狀態</th>
              </tr>
            </thead>
            <tbody>
              {products.map(p => (
                <tr key={p.id}>
                  <td>{p.id}</td>
                  <td>{p.name}</td>
                  <td>{p.barcode}</td>
                  <td>{p.spec}</td>
                  <td>{p.price}</td>
                  <td>{p.stock}</td>
                  <td>{p.isForSale ? '銷售中' : '不販售'}</td>
                </tr>
              ))}
            </tbody>
          </table>
        )}

        {/* 分頁控制區 */}
        <div className='pagination-container'>
          {/* 左邊：一頁大小size  */}
          <div className='size-selector'>
            <select value={size} onChange={(e) => {
              setSize(Number(e.target.value));
              setPage(1);
            }}>
              <option value="5">5 筆/頁</option>
              <option value="10">10 筆/頁</option>
              <option value="20">20 筆/頁</option>
            </select>
          </div>

          {/* 右邊：目前頁碼/最大頁碼 */}
          <div className='pagination-controls'>
            <button disabled={page <= 1} onClick={() => setPage(page - 1)}>上一頁</button>
            <span className='page-info'>
              目前頁碼: <strong>{page}</strong> / 總頁數: <strong>{totalPages}</strong>
            </span>
            <button disabled={page >= totalPages} onClick={() => setPage(page + 1)}>下一頁</button>
          </div>
        </div>
      </div>
    </>
  )
}

export default ProductPage