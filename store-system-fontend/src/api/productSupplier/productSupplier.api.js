export const searchAllMap = {
  all: {
    url: '/find/all/page',
    buildBody: ({ page, size }) => ({ page, size })
  },
  supplierId: {
    url: '/find/all/supplier/id/page',
    buildBody: ({ keyword, page, size }) => ({ supplierId: keyword, page, size })
  },
  supplierTaxId: {
    url: '/find/all/supplier/tax/id/page',
    buildBody: ({ keyword, page, size }) => ({ supplierTaxId: keyword, page, size })
  },
  productId: {
    url: '/find/all/product/id/page',
    buildBody: ({ keyword, page, size }) => ({ productId: keyword, page, size })
  },
  productName: {
    url: '/find/all/product/name/page',
    buildBody: ({ keyword, page, size }) => ({ productName: keyword, page, size })
  },
  productBarcode: {
    url: '/find/all/product/barcode/page',
    buildBody: ({ keyword, page, size }) => ({ productBarcode: keyword, page, size })
  },
};