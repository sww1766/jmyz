package com.order.service;

import com.order.model.Product;
import com.order.model.ProductBuyGroup;
import com.order.model.ProductFw;
import com.order.model.ProductType;
import com.order.utils.ResponseData;

/**
 * @author admin
 */
public interface ProductService {
    ResponseData createProduct(Product product);
    ResponseData queryProductList(Product product);
    ResponseData queryProductDetail(Product product);

    ResponseData createProductType(ProductType productType);
    ResponseData manageProductType(ProductType productType);
    ResponseData queryProductTypeList(String productTypeName);
    ResponseData queryProductTypeDetail(ProductType productType);

    ResponseData createProductBuyGroup(ProductBuyGroup productBuyGroup);
    ResponseData queryProductBuyGroupList(Product product);
    ResponseData queryProductBuyGroupDetail(ProductBuyGroup productBuyGroup);

    ResponseData createProductFw(ProductFw productFw);
    ResponseData queryProductFwList(Product product);
    ResponseData queryProductFwDetail(ProductFw productFw);
}
