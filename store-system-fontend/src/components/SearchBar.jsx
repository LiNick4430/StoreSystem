import './SearchBar.css'

// 搜尋欄位
function SearchBar({
  searchType,         // 搜尋類型
  setSearchType,      // 設定 搜尋類型
  searchKeyWord,      // 搜尋關鍵字
  setSearchKeyWord,   // 設定 搜尋關鍵字
  onSearch,           // 搜尋方法
  onAdd,              // 新增方法
  typeOptions,        // searchType 的 下拉式選單 中 label value 的細節
  keywordConfigs,     // searchKeyWord中 依照 searchType 對應 type placeholder 的細節
  addButtonText,      // 新增方法 的 按鈕 文字
}) {

  // 給 keywordConfigs 預設值 預防出錯
  const type = keywordConfigs?.type || 'text';
  const placeholder = keywordConfigs?.placeholder || '';

  return (
    <div className='search-bar'>
      {/* 搜尋類型 */}
      <select
        className='search-type'
        value={searchType}
        onChange={(e) => {
          const newValue = e.target.value; // 先存下新選的值
          setSearchType(newValue);
          if (newValue === 'all') {        // 使用新值進行判斷
            setSearchKeyWord("");
          }
        }}
      >
        {/* 搜尋類型 的 下拉式選單 */}
        {typeOptions.map(opt => (
          <option key={opt.value} value={opt.value}>{opt.label}</option>
        ))}
      </select>

      {/* 搜尋關鍵字 的 細節設定 */}
      <input
        disabled={searchType === 'all'} // 當搜尋方式為 'all' 時，禁用輸入框
        type={type}
        placeholder={placeholder}
        value={searchType === 'all' ? '' : searchKeyWord} // 搜尋全部時清空顯示文字
        onChange={(e) => setSearchKeyWord(e.target.value)}
        className={searchType === 'all' ? 'input-disabled' : ''}
      />

      <button onClick={onSearch}>搜尋</button>
      <button onClick={onAdd}>{addButtonText}</button>

    </div>
  )
}

export default SearchBar