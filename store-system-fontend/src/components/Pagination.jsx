import './Pagination.css'

// 分頁控制區
function Pagination({ page, totalPages, size, setPage, setSize }) {

  return (
    < div className='pagination-container' >
      {/* 一頁大小size  */}
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
      {/* 目前頁碼/最大頁碼 */}
      <div className='pagination-controls'>
        <button disabled={page <= 1} onClick={() => setPage(page - 1)}>上一頁</button>
        <span className='page-info'>
          目前頁碼: <strong>{page}</strong> / 總頁數: <strong>{totalPages}</strong>
        </span>
        <button disabled={page >= totalPages} onClick={() => setPage(page + 1)}>下一頁</button>
      </div>
    </div>
  )
}

export default Pagination