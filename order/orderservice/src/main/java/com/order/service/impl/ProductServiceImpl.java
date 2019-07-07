package com.order.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.order.mapper.ProductBuyGroupMapper;
import com.order.mapper.ProductFwMapper;
import com.order.mapper.ProductMapper;
import com.order.mapper.ProductTypeMapper;
import com.order.model.Product;
import com.order.model.ProductBuyGroup;
import com.order.model.ProductFw;
import com.order.model.ProductType;
import com.order.service.ProductService;
import com.order.utils.ResponseData;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

@Log4j2
@Service
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product> implements ProductService {
    @Resource
    private ProductMapper productMapper;

    @Resource
    private ProductTypeMapper productTypeMapper;

    @Resource
    private ProductBuyGroupMapper productBuyGroupMapper;

    @Resource
    private ProductFwMapper productFwMapper;


    @Override
    public ResponseData createProduct(Product product) {
        product.setProductId(UUID.randomUUID().toString().replaceAll("-", ""));
        product.setProductCode(new SimpleDateFormat("yyyyMMddHHmmssSSS").
                format(new Date()) + (int) (Math.random() * 900) + 100);
        product.setRecTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        productMapper.insert(product);
        return new ResponseData<>(0, "创建产品成功");
    }

    @Override
    public ResponseData queryProductList(Product product) {
        Page<Product> productPage = new Page<>(product.getPage(), product.getPageSize());
        IPage<Product> productIPage;

        QueryWrapper<Product> productQueryWrapper = new QueryWrapper<>();
        productQueryWrapper.lambda().eq(Product::getDataStatus,1);

        if (StringUtils.isNotBlank(product.getBeginTime()) && StringUtils.isNotBlank(product.getEndTime())) {
            productQueryWrapper.lambda().between(
                    Product::getRecTime, product.getBeginTime(), product.getEndTime());
        }

        if (StringUtils.isNotBlank(product.getProductTypeIds())) {
            productQueryWrapper.lambda().in(
                    Product::getProductTypeIds, product.getProductTypeIds());
        }

        if (StringUtils.isNotBlank(product.getOrderByType())) {
            productQueryWrapper.orderByDesc(product.getOrderByType());
        } else {
            productQueryWrapper.orderByDesc(product.getRecTime());
        }

        productIPage = productMapper.selectPage(productPage, productQueryWrapper);
        return new ResponseData<>(0, productIPage);
    }

    @Override
    public ResponseData queryProductDetail(Product product) {
        QueryWrapper<Product> productQueryWrapper = new QueryWrapper<>();
        productQueryWrapper.lambda().like(Product::getProductId, product.getProductId()).or().
                like(Product::getProductCode, product.getProductCode());

        return new ResponseData<>(productMapper.selectList(productQueryWrapper).stream().findFirst().
                filter(o -> o.getProductId() != null).get());
    }

    @Override
    public ResponseData createProductType(ProductType productType) {
        productType.setProductTypeId(UUID.randomUUID().toString().replaceAll("-", ""));
        productType.setIsHot(StringUtils.isBlank(productType.getIsHot()) ? "0" : productType.getIsHot());
        productType.setDataStatus(1);
        productTypeMapper.insert(productType);
        return new ResponseData<>(0, "创建产品分类成功");
    }

    @Override
    public ResponseData manageProductType(ProductType productType) {
        //下架
        if (productType.getIsDown().equals("1")) {
            productType.setDataStatus(0);
        } else if (productType.getIsDown().equals("0")) {//上架
            productType.setDataStatus(1);
        }
        //热销
        if (productType.getIsHot().equals("1")) {
            productType.setDataStatus(1);
        }

        productTypeMapper.updateById(productType);
        return new ResponseData<>(0, "设置热销或者上下架产品分类成功");
    }

    @Override
    public ResponseData queryProductTypeList(String productTypeName) {
        QueryWrapper<ProductType> productTypeQueryWrapper = new QueryWrapper<>();
        productTypeQueryWrapper.lambda().eq(ProductType::getDataStatus, 1);
        if(StringUtils.isNotBlank(productTypeName)){
            productTypeQueryWrapper.lambda().like(ProductType::getProductTypeName,productTypeName);
        }

        return new ResponseData<>(0, productTypeMapper.selectList(productTypeQueryWrapper));
    }

    @Override
    public ResponseData queryProductTypeDetail(ProductType productType) {
        return new ResponseData<>(0, productTypeMapper.selectById(productType.getProductTypeId()));
    }

    @Override
    public ResponseData createProductBuyGroup(ProductBuyGroup productBuyGroup) {
        productBuyGroup.setProductBuyGroupId(UUID.randomUUID().toString().replaceAll("-", ""));
        productBuyGroup.setDataStatus(1);
        productBuyGroupMapper.insert(productBuyGroup);
        return new ResponseData<>(0, "创建产品购买分组成功");
    }

    @Override
    public ResponseData queryProductBuyGroupList(Product product) {
        QueryWrapper<ProductBuyGroup> productBuyGroupQueryWrapper = new QueryWrapper<>();
        productBuyGroupQueryWrapper.lambda().eq(ProductBuyGroup::getDataStatus,1);
        if (StringUtils.isNotBlank(product.getProductBuyGroupIds())) {
            productBuyGroupQueryWrapper.lambda().in(
                    ProductBuyGroup::getProductBuyGroupId, product.getProductBuyGroupIds());
        }
        return new ResponseData<>(0, productBuyGroupMapper.selectList(productBuyGroupQueryWrapper));
    }

    @Override
    public ResponseData queryProductBuyGroupDetail(ProductBuyGroup productBuyGroup) {
        return new ResponseData<>(0, productBuyGroupMapper.selectById(productBuyGroup.getProductBuyGroupId()));
    }

    @Override
    public ResponseData createProductFw(ProductFw productFw) {
        productFw.setServiceId(UUID.randomUUID().toString().replaceAll("-", ""));
        productFw.setDataStatus(1);
        productFwMapper.insert(productFw);
        return new ResponseData<>(0, "创建产品服务成功");
    }

    @Override
    public ResponseData queryProductFwList(Product product) {
        QueryWrapper<ProductFw> productFwQueryWrapper = new QueryWrapper<>();
        productFwQueryWrapper.lambda().eq(ProductFw::getDataStatus, 1);
        if (StringUtils.isNotBlank(product.getServiceIds())) {
            productFwQueryWrapper.lambda().in(
                    ProductFw::getServiceId, product.getServiceIds());
        }
        return new ResponseData<>(0, productFwMapper.selectList(productFwQueryWrapper));
    }

    @Override
    public ResponseData queryProductFwDetail(ProductFw productFw) {
        return new ResponseData<>(0, productFwMapper.selectById(productFw.getServiceId()));
    }
}
