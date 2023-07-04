package cn.iocoder.yudao.module.promotion.util;

import cn.hutool.core.util.ObjectUtil;
import cn.iocoder.yudao.framework.common.enums.CommonStatusEnum;
import cn.iocoder.yudao.framework.common.util.collection.CollectionUtils;
import cn.iocoder.yudao.framework.common.util.date.LocalDateTimeUtils;
import cn.iocoder.yudao.module.product.api.sku.dto.ProductSkuRespDTO;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.product.enums.ErrorCodeConstants.SKU_NOT_EXISTS;

/**
 * 活动工具类
 *
 * @author 芋道源码
 */
public class PromotionUtils {

    /**
     * 根据时间，计算活动状态
     *
     * @param endTime 结束时间
     * @return 活动状态
     */
    public static Integer calculateActivityStatus(LocalDateTime endTime) {
        return LocalDateTimeUtils.beforeNow(endTime) ? CommonStatusEnum.DISABLE.getStatus() : CommonStatusEnum.ENABLE.getStatus();
    }

    public static <T> void validateProductSkuExistence(List<ProductSkuRespDTO> skus, List<T> products, Function<T, Long> func) {
        // 校验 sku 个数是否一致
        Set<Long> skuIdsSet = CollectionUtils.convertSet(products, func);
        Set<Long> skuIdsSet1 = CollectionUtils.convertSet(skus, ProductSkuRespDTO::getId);
        if (ObjectUtil.notEqual(skuIdsSet.size(), skuIdsSet1.size())) {
            throw exception(SKU_NOT_EXISTS);
        }
        // 校验 skuId 是否存在
        if (!skuIdsSet1.containsAll(skuIdsSet) || !skuIdsSet.containsAll(skuIdsSet1)) {
            throw exception(SKU_NOT_EXISTS);
        }
    }

}
