package cn.iocoder.yudao.userserver.modules.pay.controller.order;

import cn.iocoder.yudao.coreservice.modules.pay.service.order.PayRefundCoreService;
import cn.iocoder.yudao.coreservice.modules.pay.service.order.bo.PayRefundReqBO;
import cn.iocoder.yudao.coreservice.modules.pay.util.PaySeqUtils;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.userserver.modules.pay.controller.order.vo.PayRefundReqVO;
import cn.iocoder.yudao.userserver.modules.pay.controller.order.vo.PayRefundRespVO;
import cn.iocoder.yudao.userserver.modules.pay.convert.order.PayRefundConvert;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

import static cn.iocoder.yudao.framework.common.util.servlet.ServletUtils.getClientIP;

@Api(tags = "退款订单")
@RestController
@RequestMapping("/pay/order")
@Validated
@Slf4j
public class PayRefundController {

    @Resource
    private PayRefundCoreService payRefundCoreService;

    @PostMapping("/refund")
    @ApiOperation("提交退款订单")
    public CommonResult<PayRefundRespVO> refund(@RequestBody PayRefundReqVO reqVO){
        PayRefundReqBO reqBO = PayRefundConvert.INSTANCE.convert(reqVO);
        reqBO.setUserIp(getClientIP());
        //TODO 测试暂时模拟生成商户退款订单
        reqBO.setMerchantRefundNo(PaySeqUtils.genMerchantRefundNo());
        //reqBO.setMerchantRefundNo("MO202111210814084370000");
        return CommonResult.success( PayRefundConvert.INSTANCE.convert(payRefundCoreService.refund(reqBO)));
    }

}
