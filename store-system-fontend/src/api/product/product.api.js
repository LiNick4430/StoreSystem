export const searchAllMap = {
  all: {
    url: '/find/all/page',
    buildBody: ({ page, size }) => ({ page, size })
  },
  name: {
    url: '/find/all/product/name/page',
    buildBody: ({ keyword, page, size }) => ({ name: keyword, page, size })
  },
  supplier: {
    url: '/find/all/supplier/page',
    buildBody: ({ keyword, page, size }) => ({ supplierId: keyword, page, size })
  }
};

export const searchMap = {
  id: {
    url: '/find/id',
    buildBody: ({ keyword }) => ({ productId: keyword })
  },
  barcode: {
    url: '/find/barcode',
    buildBody: ({ keyword }) => ({ barcode: keyword })
  }
};