package cn.jimoos.service.impl;

import cn.jimoos.common.exception.BussException;
import cn.jimoos.dao.ProductCategoryMapper;
import cn.jimoos.dao.ProductMapper;
import cn.jimoos.dao.ProductTagMapper;
import cn.jimoos.dao.RProductTagMapper;
import cn.jimoos.dto.ProductTagDto;
import cn.jimoos.entity.ProductEntity;
import cn.jimoos.error.ProductError;
import cn.jimoos.factory.ProductFactory;
import cn.jimoos.form.product.*;
import cn.jimoos.model.Product;
import cn.jimoos.model.ProductCategory;
import cn.jimoos.model.ProductTag;
import cn.jimoos.repository.ProductRepository;
import cn.jimoos.service.ProductService;
import cn.jimoos.utils.http.Page;
import cn.jimoos.vo.ProductSkuVO;
import cn.jimoos.vo.ProductVO;
import lombok.SneakyThrows;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author :keepcleargas
 * @date :2021-03-31 11:32.
 */
@Service
public class ProductServiceImpl implements ProductService {
    @Resource
    ProductFactory productFactory;
    @Resource
    ProductRepository productRepository;
    @Resource
    ProductMapper productMapper;
    @Resource
    ProductCategoryMapper productCategoryMapper;
    @Resource
    RProductTagMapper rProductTagMapper;
    @Resource
    ProductTagMapper productTagMapper;

    @SneakyThrows
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ProductVO addFullProduct(BeProductForm beProductForm) {
        ProductEntity productEntity = productFactory.create(beProductForm);
        //绑定tags
        productEntity.attachTags(beProductForm.getTagIds());
        //新建信息
        productRepository.save(productEntity);
        //更新sku数据
        List<BeProductForm.SkuInput> skuInputs = beProductForm.getSkus();

        if (!CollectionUtils.isEmpty(skuInputs)) {
            //添加 sku
            productEntity.addSkus(skuInputs);
            productRepository.saveSkus(productEntity);
        }
        return productEntity.toVO();
    }

    @Override
    public ProductVO saveProductInfo(BeProductForm beProductForm) {
        ProductEntity productEntity = productFactory.create(beProductForm);
        //绑定tags
        productEntity.attachTags(beProductForm.getTagIds());
        //新建信息
        productRepository.save(productEntity);
        return productEntity.toVO();
    }

    @Override
    public List<ProductSkuVO> saveSkus(BeProductSkusForm beProductSkusForm) throws BussException {
        ProductEntity productEntity = productRepository.byId(beProductSkusForm.getProductId());

        if (!CollectionUtils.isEmpty(beProductSkusForm.getSkuInputs())) {
            if (beProductSkusForm.isBatchUpdate()) {
                productEntity.addSkus(beProductSkusForm.getSkuInputs());
                productRepository.saveSkus(productEntity);
            } else {
                BeProductForm.SkuInput skuInput = beProductSkusForm.getSkuInputs().get(0);
                ProductEntity.SkuEntity skuEntity = new ProductEntity.SkuEntity(productEntity, skuInput);
                skuEntity.addAttrMaps(skuInput.getAttrs());
                productRepository.updateOneSku(skuEntity);
                List<ProductSkuVO> productSkuVOs = new ArrayList<>();
                productSkuVOs.add(fromSkuEntity(skuEntity));
                return productSkuVOs;
            }
        }
        return productEntity.getProductSkuVos();
    }


    @Override
    public ProductVO getOne(Long productId) throws BussException {
        ProductEntity productEntity = productRepository.byId(productId);
        return productEntity.toVO();
    }

    @Override
    public Page<ProductVO> query(BeProductQueryForm form) {
        long count = productMapper.queryTableCount(form.toQueryMap());

        if (count > 0) {
            List<Product> products = productMapper.queryTable(form.toQueryMap());

            List<Long> productIds = products.stream().map(Product::getId).collect(Collectors.toList());
            List<Long> categoryIds = products.stream().map(Product::getCategoryId).collect(Collectors.toList());

            List<ProductCategory> productCategories = productCategoryMapper.findByIdIn(categoryIds);
            Map<Long, ProductCategory> idToProductCategoryMap = productCategories.stream().collect(Collectors.toMap(productCategory -> productCategory.getId(), productCategory -> productCategory));

            List<ProductTagDto> productTagDtos = productTagMapper.findByProductIdIn(productIds);
            Map<Long, List<ProductTagDto>> idToTagListMap
                    = productTagDtos.stream().collect(Collectors.groupingBy(ProductTagDto::getProductId));


            return Page.create(count, products.stream().map(product -> {
                ProductVO productVO = new ProductVO();
                BeanUtils.copyProperties(product, productVO);
                productVO.setCategory(idToProductCategoryMap.get(product.getCategoryId()));
                productVO.setTags(idToTagListMap.get(product.getId()).stream().map(ProductTag.class::cast).collect(Collectors.toList()));
                return productVO;
            }).collect(Collectors.toList()));
        }
        return Page.empty();
    }

    @Override
    public List<ProductSkuVO> skus(Long productId) throws BussException {
        ProductEntity productEntity = productRepository.byId(productId);
        return productEntity.getProductSkuVos();
    }

    @Override
    public void up(BeProductStatusForm statusForm) throws BussException {
        ProductEntity productEntity = productRepository.byId(statusForm.getProductId());

        if (!productEntity.hasAnySkus()) {
            throw new BussException(ProductError.PRODUCT_SKU_LOSE);
        }
        productEntity.up();

        productRepository.save(productEntity);
    }

    @Override
    public void down(BeProductStatusForm statusForm) throws BussException {
        ProductEntity productEntity = productRepository.byId(statusForm.getProductId());
        productEntity.down(statusForm.getReason());

        productRepository.save(productEntity);
    }

    @Override
    public void delete(BeProductDeleteForm beProductDeleteForm) throws BussException {
        ProductEntity productEntity = productRepository.byId(beProductDeleteForm.getProductId());
        productEntity.softDelete();

        productRepository.delete(productEntity);
    }

    private ProductSkuVO fromSkuEntity(ProductEntity.SkuEntity skuEntity) {
        ProductSkuVO productSkuVO = new ProductSkuVO();
        productSkuVO.setPrice(skuEntity.getPrice());
        productSkuVO.setShowPrice(skuEntity.getShowPrice());
        productSkuVO.setId(skuEntity.getId());
        productSkuVO.setCover(skuEntity.getCover());

        productSkuVO.setAttrs(productRepository.findAttrMapBySkuId(skuEntity.getId()));
        return productSkuVO;
    }
}