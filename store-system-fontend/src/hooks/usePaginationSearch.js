import { useState, useEffect } from 'react';

// 使用分頁搜尋
export function usePaginationSearch(searchService) {

  // 資料狀態
  const [items, setItems] = useState([]);
  const [totalPages, setTotalPages] = useState(0);

  // 分頁與搜尋方式
  const [page, setPage] = useState(1);
  const [size, setSize] = useState(10);
  const [searchType, setSearchType] = useState("all");
  const [searchKeyWord, setSearchKeyWord] = useState("");

  const [selectedItem, setSelectedItem] = useState(null);

  // 定義 抓取資料函式
  const fetchData = async () => {
    try {
      const response = await searchService(searchType, searchKeyWord, page, size);

      if (response && response.data) {
        setItems(response.data.content || []);
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

  return {
    items, totalPages, page, setPage, size, setSize,
    searchType, setSearchType, searchKeyWord, setSearchKeyWord,
    selectedItem, setSelectedItem, fetchData
  }
}