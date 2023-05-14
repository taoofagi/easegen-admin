package cn.iocoder.yudao.module.product.service.spu;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.iocoder.yudao.framework.common.enums.CommonStatusEnum;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.collection.CollectionUtils;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.product.controller.admin.category.vo.ProductCategoryListReqVO;
import cn.iocoder.yudao.module.product.controller.admin.sku.vo.ProductSkuCreateOrUpdateReqVO;
import cn.iocoder.yudao.module.product.controller.admin.sku.vo.ProductSkuRespVO;
import cn.iocoder.yudao.module.product.controller.admin.spu.vo.*;
import cn.iocoder.yudao.module.product.controller.app.spu.vo.AppProductSpuPageReqVO;
import cn.iocoder.yudao.module.product.convert.sku.ProductSkuConvert;
import cn.iocoder.yudao.module.product.convert.spu.ProductSpuConvert;
import cn.iocoder.yudao.module.product.dal.dataobject.category.ProductCategoryDO;
import cn.iocoder.yudao.module.product.dal.dataobject.sku.ProductSkuDO;
import cn.iocoder.yudao.module.product.dal.dataobject.spu.ProductSpuDO;
import cn.iocoder.yudao.module.product.dal.mysql.spu.ProductSpuMapper;
import cn.iocoder.yudao.module.product.enums.spu.ProductSpuStatusEnum;
import cn.iocoder.yudao.module.product.enums.spu.ProductSpuTabTypeEnum;
import cn.iocoder.yudao.module.product.service.brand.ProductBrandService;
import cn.iocoder.yudao.module.product.service.category.ProductCategoryService;
import cn.iocoder.yudao.module.product.service.property.ProductPropertyValueService;
import cn.iocoder.yudao.module.product.service.property.bo.ProductPropertyValueDetailRespBO;
import cn.iocoder.yudao.module.product.service.sku.ProductSkuService;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.getSumValue;
import static cn.iocoder.yudao.module.product.enums.ErrorCodeConstants.SPU_NOT_EXISTS;
import static cn.iocoder.yudao.module.product.enums.ErrorCodeConstants.SPU_SAVE_FAIL_CATEGORY_LEVEL_ERROR;

/**
 * 商品 SPU Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class ProductSpuServiceImpl implements ProductSpuService {

    @Resource
    private ProductSpuMapper productSpuMapper;

    @Resource
    @Lazy // 循环依赖，避免报错
    private ProductSkuService productSkuService;
    @Resource
    private ProductBrandService brandService;
    @Resource
    private ProductCategoryService categoryService;
    @Resource
    private ProductPropertyValueService productPropertyValueService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createSpu(ProductSpuCreateReqVO createReqVO) {
        // 校验分类 TODO puhui999：暂不清楚为什么只能选择第三层的结点；芋艿：改成二级分类，因为商品只能放在叶子节点级别；
        //validateCategory(createReqVO.getCategoryId());
        // 校验品牌 TODO puhui999：暂不校验，前端没有做品牌选择；芋艿：可以加下哈
        //brandService.validateProductBrand(createReqVO.getBrandId());

        List<ProductSkuCreateOrUpdateReqVO> skuSaveReqList = createReqVO.getSkus();
        // 校验 SKU
        productSkuService.validateSkuList(skuSaveReqList, createReqVO.getSpecType());
        ProductSpuDO spu = ProductSpuConvert.INSTANCE.convert(createReqVO);
        // 初始化 SPU 中 SKU 相关属性
        initSpuFromSkus(spu, skuSaveReqList);

        // 插入 SPU
        productSpuMapper.insert(spu);
        // 插入 SKU
        productSkuService.createSkuList(spu.getId(), skuSaveReqList);
        // 返回
        return spu.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateSpu(ProductSpuUpdateReqVO updateReqVO) {
        // 校验 SPU 是否存在
        validateSpuExists(updateReqVO.getId());
        // 校验分类 TODO 暂不清楚为什么只能选择第三层的结点
        //validateCategory(updateReqVO.getCategoryId());
        // 校验品牌 TODO 暂不校验，前端没有做品牌选择
        //brandService.validateProductBrand(updateReqVO.getBrandId());
        // 校验SKU
        List<ProductSkuCreateOrUpdateReqVO> skuSaveReqList = updateReqVO.getSkus();
        productSkuService.validateSkuList(skuSaveReqList, updateReqVO.getSpecType());
        // 更新 SPU
        ProductSpuDO updateObj = ProductSpuConvert.INSTANCE.convert(updateReqVO);
        initSpuFromSkus(updateObj, skuSaveReqList);
        productSpuMapper.updateById(updateObj);
        // 批量更新 SKU
        productSkuService.updateSkuList(updateObj.getId(), updateReqVO.getSkus());
    }

    /**
     * 基于 SKU 的信息，初始化 SPU 的信息
     * 主要是计数相关的字段，例如说市场价、最大最小价、库存等等
     *
     * @param spu 商品 SPU
     * @param skus 商品 SKU 数组
     */
    private void initSpuFromSkus(ProductSpuDO spu, List<ProductSkuCreateOrUpdateReqVO> skus) {
        // 断言，避免告警
        assert skus.size() > 0;
        // 获取sku单价最低的商品
        ProductSkuCreateOrUpdateReqVO vo = skus.stream().min(Comparator.comparing(ProductSkuCreateOrUpdateReqVO::getPrice)).get();
        // sku单价最低的商品的价格
        spu.setPrice(vo.getPrice());
        // sku单价最低的商品的市场价格
        spu.setMarketPrice(vo.getMarketPrice());
        // sku单价最低的商品的成本价格
        spu.setCostPrice(vo.getCostPrice());
        // sku单价最低的商品的条形码
        spu.setBarCode(vo.getBarCode());
        // 默认状态为上架
        spu.setStatus(ProductSpuStatusEnum.ENABLE.getStatus());
        // TODO 默认商品销量和浏览量为零
        spu.setSalesCount(0);
        spu.setBrowseCount(0);
        // skus库存总数
        spu.setStock(getSumValue(skus, ProductSkuCreateOrUpdateReqVO::getStock, Integer::sum));
    }

    /**
     * 校验商品分类是否合法
     *
     * @param id 商品分类编号
     */
    private void validateCategory(Long id) {
        categoryService.validateCategory(id);
        // 校验层级
        if (categoryService.getCategoryLevel(id) != 3) {
            throw exception(SPU_SAVE_FAIL_CATEGORY_LEVEL_ERROR);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteSpu(Long id) {
        // 校验存在
        validateSpuExists(id);
        // 删除 SPU
        productSpuMapper.deleteById(id);
        // 删除关联的 SKU
        productSkuService.deleteSkuBySpuId(id);
    }

    private void validateSpuExists(Long id) {
        if (productSpuMapper.selectById(id) == null) {
            throw exception(SPU_NOT_EXISTS);
        }
    }

    @Override
    public ProductSpuDO getSpu(Long id) {
        return productSpuMapper.selectById(id);
    }

    @Override
    public List<ProductSpuDO> getSpuList(Collection<Long> ids) {
        return productSpuMapper.selectBatchIds(ids);
    }

    @Override
    public List<ProductSpuDO> getSpuList() {
        return productSpuMapper.selectList();
    }

    @Override
    public PageResult<ProductSpuDO> getSpuPage(ProductSpuPageReqVO pageReqVO) {
        return productSpuMapper.selectPage(pageReqVO);
    }

    @Override
    public PageResult<ProductSpuDO> getSpuPage(AppProductSpuPageReqVO pageReqVO) {
        // 查找时，如果查找某个分类编号，则包含它的子分类。因为顶级分类不包含商品
        Set<Long> categoryIds = new HashSet<>();
        if (pageReqVO.getCategoryId() != null && pageReqVO.getCategoryId() > 0) {
            categoryIds.add(pageReqVO.getCategoryId());
            List<ProductCategoryDO> categoryChildren = categoryService.getEnableCategoryList(new ProductCategoryListReqVO()
                    .setParentId(pageReqVO.getCategoryId()).setStatus(CommonStatusEnum.ENABLE.getStatus()));
            categoryIds.addAll(CollectionUtils.convertList(categoryChildren, ProductCategoryDO::getId));
        }
        // 分页查询
        return productSpuMapper.selectPage(pageReqVO, categoryIds);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateSpuStock(Map<Long, Integer> stockIncrCounts) {
        stockIncrCounts.forEach((id, incCount) -> productSpuMapper.updateStock(id, incCount));
    }

    // TODO @puhui999：Service 尽量不做一些跟 VO 相关的拼接逻辑，目的是让 Service 更加简洁一点哈。
    @Override
    public ProductSpuDetailRespVO getSpuDetail(Long id) {
        // 获得商品 SPU
        ProductSpuDO spu = getSpu(id);
        if (spu == null) {
            throw exception(SPU_NOT_EXISTS);
        }
        ProductSpuDetailRespVO productSpuDetailRespVO = ProductSpuConvert.INSTANCE.convert03(spu);
        // 查询商品 SKU
        List<ProductSkuDO> skus = productSkuService.getSkuListBySpuId(spu.getId());
        if (CollUtil.isNotEmpty(skus)){
            // TODO @puhui999：skuVOs 更简洁一点。然后大小写要注释哈。RespVOs；因为 VO 是缩写，s 是复数
            List<ProductSkuRespVO> skuRespVoS = ProductSkuConvert.INSTANCE.convertList(skus);
            // 非多规格，不需要处理
            // TODO @puhui999：统一模型，即使是单规格，也查询下，问题不大的
            if (ObjectUtil.equal(productSpuDetailRespVO.getSpecType(), true)) {
                // 获取所有的属性值 id
                Set<Long> valueIds = skus.stream().flatMap(p -> p.getProperties().stream())
                        .map(ProductSkuDO.Property::getValueId)
                        .collect(Collectors.toSet());
                List<ProductPropertyValueDetailRespBO> valueDetailList = productPropertyValueService.getPropertyValueDetailList(valueIds);
                // TODO @puhui999：拼接的逻辑，最好查询好后，丢到 convert 里面统一处理；这样 Service or Controller 也可以更简洁；原则上，Controller 去组合；Service 写逻辑；Convert 转换
                Map<Long, String> stringMap = valueDetailList.stream().collect(Collectors.toMap(ProductPropertyValueDetailRespBO::getValueId, ProductPropertyValueDetailRespBO::getValueName));
                // 设置属性值名称
                skuRespVoS.stream().flatMap(p -> p.getProperties().stream()).forEach(item ->item.setValueName(stringMap.get(item.getValueId())));
            }
            productSpuDetailRespVO.setSkus(skuRespVoS);
        }
        return productSpuDetailRespVO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateStatus(ProductSpuUpdateStatusReqVO updateReqVO) {
        // 校验存在
        validateSpuExists(updateReqVO.getId());
        // 更新状态
        ProductSpuDO productSpuDO = productSpuMapper.selectById(updateReqVO.getId()).setStatus(updateReqVO.getStatus());
        productSpuMapper.updateById(productSpuDO);

    }

    @Override
    public Map<Integer, Long> getTabsCount() {
        // TODO @puhui999：map =》counts；尽量避免出现 map 这种命名，无命名含义哈
        Map<Integer, Long> map = new HashMap<>();
        // 查询销售中的商品数量
        map.put(ProductSpuTabTypeEnum.FOR_SALE.getType(), productSpuMapper.selectCount(ProductSpuDO::getStatus, ProductSpuStatusEnum.ENABLE.getStatus()));
        // 查询仓库中的商品数量
        map.put(ProductSpuTabTypeEnum.IN_WAREHOUSE.getType(),productSpuMapper.selectCount(ProductSpuDO::getStatus, ProductSpuStatusEnum.DISABLE.getStatus()));
        // 查询售空的商品数量
        map.put(ProductSpuTabTypeEnum.SOLD_OUT.getType(),productSpuMapper.selectCount(ProductSpuDO::getStock, 0));
        // 查询触发警戒库存的商品数量 TODO 警戒库存暂时为 10，后期需要使用常量或者数据库配置替换
        // TODO @puhui999：要有空格；, productSpuMapper
        // TODO @puhui999：Service 不要有 Mapper 的逻辑；想想咋抽象一下哈
        map.put(ProductSpuTabTypeEnum.ALERT_STOCK.getType(),productSpuMapper.selectCount(new LambdaQueryWrapperX<ProductSpuDO>().le(ProductSpuDO::getStock, 10)));
        // 查询回收站中的商品数量
        map.put(ProductSpuTabTypeEnum.RECYCLE_BIN.getType(),productSpuMapper.selectCount(ProductSpuDO::getStatus, ProductSpuStatusEnum.RECYCLE.getStatus()));
        return map;
    }

}
