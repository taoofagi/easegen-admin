package cn.iocoder.yudao.module.trade.enums;

/**
 * 通知模板枚举类
 *
 * @author HUIHUI
 */
public interface MessageTemplateConstants {

    //======================= 短信消息模版 =======================

    String ORDER_DELIVERY = "order_delivery"; // 短信模版编号

    String BROKERAGE_WITHDRAW_AUDIT_APPROVE = "brokerage_withdraw_audit_approve"; // 佣金提现（审核通过）
    String BROKERAGE_WITHDRAW_AUDIT_REJECT = "brokerage_withdraw_audit_reject"; // 佣金提现（审核不通过）

    //======================= 小程序订阅消息模版 =======================

    String DELIVERY_ORDER = "订单发货通知";

}
