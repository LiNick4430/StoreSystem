export const searchAllMap = {
  all: {
    url: '/find/all/page',
    buildBody: ({ page, size }) => ({ page, size })
  },
  product: {
    url: '/find/all/product/page',
    buildBody: ({ keyword, page, size }) => ({ productId: keyword, page, size })
  },
}

export const searchMap = {
  id: {
    url: '/find/id',
    buildBody: ({ keyword }) => ({ id: keyword })
  },
  barcode: {
    url: '/find/tax/id',
    buildBody: ({ keyword }) => ({ taxId: keyword })
  }
};