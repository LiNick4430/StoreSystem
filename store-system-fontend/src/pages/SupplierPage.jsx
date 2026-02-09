// 匯入 CSS
import './SupplierPage.css'

// 匯入 搜尋全部 的 方法
import { searchAllSupplier } from '../services/supplierService'

// 匯入 右側 供應商明細
import SupplierDetailPanel from '../components/SupplierDetailPanel'

// 匯入 搜尋欄位
import SearchBar from '../components/SearchBar';

// 匯入 使用分頁搜尋 + 分頁控制區
import { usePaginationSearch } from '../hooks/usePaginationSearch';
import Pagination from '../components/Pagination';

function SupplierPage() {
  // 設定 使用分頁搜尋 的 對應變數(items 修改成 對應的變數名)
  const {
    items: suppliers, totalPages, page, setPage, size, setSize,
    searchType, setSearchType, searchKeyWord, setSearchKeyWord,
    selectedItem: selectedSupplier, setSelectedItem: setSelectedSupplier, fetchData

  } = usePaginationSearch(searchAllSupplier);

  // 新供應商 的 預設資料
  const newSupplier = { id: null, name: '', taxId: '', address: '', phone: '' };

  // 設定 SearchBar 中 typeOptions 的細節
  const typeOptions = [
    { label: '全部', value: "all" },
    { label: '商品ID', value: "product" }
  ]

  // 設定 SearchBar 中 keywordConfigs 的 對應關係
  const keywordConfigs = {
    all: { type: 'text', placeholder: '搜尋全部, 不需要關鍵字' },
    product: { type: 'number', placeholder: '請輸入商品ID...' }
  }

  // 點擊事件 -> 個別供應商資料 傳入 右側的 供應商明細
  const handleSelectRow = (supplier) => {
    setSelectedSupplier(supplier);
  };

  // 更新事件 -> 右側供應商明細 更新後 直接讓 左側重新搜尋 
  const onRefresh = async () => {
    await fetchData();
  }

  // 關閉事件 -> 用於關閉 右側的 供應商明細
  const handleCloseDetail = () => {
    setSelectedSupplier(null);
  }

  return (
    <>
      <div className='supplier-page-container'>

        {/* 左側欄位 */}
        <div className='master-section'>
          <h1>供應商搜尋</h1>
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
              setSelectedSupplier(newSupplier)
            }}
            addButtonText=' + 新增供應商'
            typeOptions={typeOptions}
            keywordConfigs={keywordConfigs[searchType]}
          />

          {/* 資料展示區 */}
          {suppliers.length === 0 ? (
            /* 沒資料時顯示的區塊 */
            <div className="no-data-container">
              <p>🔍 找不到相關商品，請嘗試其他關鍵字。</p>
            </div>
          ) : (
            <table className='supplier-table'>
              <thead>
                <tr>
                  <th>ID</th>
                  <th>名稱</th>
                  <th>統編</th>
                  <th>地址</th>
                  <th>電話</th>
                  <th>供應數量</th>
                </tr>
              </thead>
              <tbody>
                {suppliers.map(s => (
                  <tr
                    key={s.id}
                    onClick={() => handleSelectRow(s)}  // 綁定點擊事件
                    className={selectedSupplier?.id === s.id ? 'active-row' : ''} // 家入選中的樣式
                    style={{ cursor: 'pointer' }}  // 提示 使用者可以點擊
                  >
                    <td>{s.id}</td>
                    <td>{s.name}</td>
                    <td>{s.taxId}</td>
                    <td>{s.address}</td>
                    <td>{s.phone}</td>
                    <td>{s.productQty ? s.productQty : 0} 項商品</td>
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

        {/* 右側：供應商明細面板 */}
        <div className='detail-section'>
          {selectedSupplier ? (
            <SupplierDetailPanel
              supplier={selectedSupplier}
              onRefresh={onRefresh}
              onClose={handleCloseDetail} />
          ) : (
            <div className="no-selection">請點擊供應商以查看詳情</div>
          )}
        </div>

      </div>
    </>
  )

}

export default SupplierPage