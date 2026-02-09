// 匯入 CSS
import './ProductPage.css'

// 匯入 搜尋全部 的 方法
import { searchAllProduct } from '../services/productService'

// 匯入 右側 商品明細
import ProductDetailPanel from '../components/ProductDetailPanel';

// 匯入 搜尋欄位
import SearchBar from '../components/SearchBar';

// 匯入 使用分頁搜尋 + 分頁控制區
import { usePaginationSearch } from '../hooks/usePaginationSearch';
import Pagination from '../components/Pagination';

function ProductPage() {
  // 設定 使用分頁搜尋 的 對應變數(items 修改成 對應的變數名)
  const {
    items: products, totalPages, page, setPage, size, setSize,
    searchType, setSearchType, searchKeyWord, setSearchKeyWord,
    selectedItem: selectedProduct, setSelectedItem: setSelectedProduct, fetchData
  } = usePaginationSearch(searchAllProduct);

  // 新商品 的 預設條件
  const newProduct = { id: null, name: '', spec: '', price: 0, barcode: '' };

  // 設定 SearchBar 中 typeOptions 的細節
  const typeOptions = [
    { label: '全部', value: "all" },
    { label: '商品名稱', value: "name" },
    { label: '供應商ID', value: "supplier" }
  ]

  // 設定 SearchBar 中 keywordConfigs 的 對應關係
  const keywordConfigs = {
    all: { type: 'text', placeholder: '搜尋全部, 不需要關鍵字' },
    name: { type: 'text', placeholder: '請輸入關鍵字...' },
    supplier: { type: 'number', placeholder: '請輸入供應商ID...' }
  }

  // 點擊事件 -> 個別商品資料 傳入 右側的 商品明細
  const handleSelectRow = (product) => {
    setSelectedProduct(product);
  };

  // 更新事件 -> 右側商品明細 更新後 直接讓 左側重新搜尋 
  const onRefresh = async () => {
    await fetchData();
  }

  // 關閉事件 -> 用於關閉 右側的 商品明細
  const handleCloseDetail = () => {
    setSelectedProduct(null);
  }

  return (
    <>
      <div className='product-page-container'>

        {/* 左側欄位 */}
        <div className='master-section'>
          <h1>商品搜尋</h1>
          {/* 搜尋欄位 */}
          <SearchBar
            searchType={searchType}
            setSearchType={setSearchType}
            searchKeyWord={searchKeyWord}
            setSearchKeyWord={setSearchKeyWord}
            onSearch={() => {
              setPage(1);
              fetchData();
            }}
            onAdd={() => {
              setSelectedProduct(newProduct)
            }}
            addButtonText=' + 新增商品'
            typeOptions={typeOptions}
            keywordConfigs={keywordConfigs[searchType]}
          />

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
                  <tr
                    key={p.id}
                    onClick={() => handleSelectRow(p)}  // 綁定點擊事件
                    className={selectedProduct?.id === p.id ? 'active-row' : ''} // 家入選中的樣式
                    style={{ cursor: 'pointer' }}  // 提示 使用者可以點擊
                  >
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
          <Pagination
            page={page}
            totalPages={totalPages}
            size={size}
            setPage={setPage}
            setSize={setSize}
          />
        </div>

        {/* 右側：商品明細面板 */}
        <div className='detail-section'>
          {selectedProduct ? (
            <ProductDetailPanel
              product={selectedProduct}
              onRefresh={onRefresh}
              onClose={handleCloseDetail} />
          ) : (
            <div className="no-selection">請點擊商品以查看詳情</div>
          )}
        </div>

      </div>
    </>
  )
}

export default ProductPage