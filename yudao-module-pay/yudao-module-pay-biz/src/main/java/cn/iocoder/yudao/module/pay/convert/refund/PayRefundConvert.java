package cn.iocoder.yudao.module.pay.convert.refund;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.pay.api.refund.dto.PayRefundCreateReqDTO;
import cn.iocoder.yudao.module.pay.api.refund.dto.PayRefundRespDTO;
import cn.iocoder.yudao.module.pay.controller.admin.refund.vo.*;
import cn.iocoder.yudao.module.pay.dal.dataobject.refund.PayRefundDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Mapper
public interface PayRefundConvert {

    PayRefundConvert INSTANCE = Mappers.getMapper(PayRefundConvert.class);

    PayRefundDO convert(PayRefundCreateReqVO bean);

    PayRefundDO convert(PayRefundUpdateReqVO bean);

    PayRefundRespVO convert(PayRefundDO bean);

    /**
     * 退款订单 DO 转 退款详情订单 VO
     *
     * @param bean 退款订单 DO
     * @return 退款详情订单 VO
     */
    PayRefundDetailsRespVO refundDetailConvert(PayRefundDO bean);

    /**
     * 退款订单DO 转 分页退款条目VO
     *
     * @param bean 退款订单DO
     * @return 分页退款条目VO
     */
    PayRefundPageItemRespVO pageItemConvert(PayRefundDO bean);

    List<PayRefundRespVO> convertList(List<PayRefundDO> list);

    PageResult<PayRefundRespVO> convertPage(PageResult<PayRefundDO> page);

    /**
     * 退款订单DO 转 导出excel VO
     *
     * @param bean 退款订单DO
     * @return 导出 excel VO
     */
    default PayRefundExcelVO excelConvert(PayRefundDO bean) {
        if (bean == null) {
            return null;
        }

        PayRefundExcelVO payRefundExcelVO = new PayRefundExcelVO();

        payRefundExcelVO.setId(bean.getId());
        payRefundExcelVO.setTradeNo(bean.getNo());
        payRefundExcelVO.setMerchantOrderId(bean.getMerchantOrderId());
        // TODO 芋艿：晚点在改
//        payRefundExcelVO.setMerchantRefundNo(bean.getMerchantRefundNo());
        payRefundExcelVO.setNotifyUrl(bean.getNotifyUrl());
        payRefundExcelVO.setStatus(bean.getStatus());
        payRefundExcelVO.setReason(bean.getReason());
        payRefundExcelVO.setUserIp(bean.getUserIp());
        payRefundExcelVO.setChannelOrderNo(bean.getChannelOrderNo());
        payRefundExcelVO.setChannelRefundNo(bean.getChannelRefundNo());
        payRefundExcelVO.setSuccessTime(bean.getSuccessTime());
        payRefundExcelVO.setCreateTime(bean.getCreateTime());

        BigDecimal multiple = new BigDecimal(100);
        payRefundExcelVO.setPayPrice(BigDecimal.valueOf(bean.getPayPrice())
                .divide(multiple, 2, RoundingMode.HALF_UP).toString());
        payRefundExcelVO.setRefundPrice(BigDecimal.valueOf(bean.getRefundPrice())
                .divide(multiple, 2, RoundingMode.HALF_UP).toString());

        return payRefundExcelVO;
    }

    PayRefundDO convert(PayRefundCreateReqDTO bean);

    PayRefundRespDTO convert02(PayRefundDO bean);

}
