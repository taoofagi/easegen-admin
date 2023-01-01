package cn.iocoder.yudao.module.mp.framework.mp.core;

import cn.iocoder.yudao.module.mp.dal.dataobject.account.MpAccountDO;
import cn.iocoder.yudao.module.mp.service.fansmsgres.NullHandler;
import cn.iocoder.yudao.module.mp.service.handler.*;
import com.binarywang.spring.starter.wxjava.mp.properties.WxMpProperties;
import com.google.common.collect.Maps;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.redis.RedisTemplateWxRedisOps;
import me.chanjar.weixin.mp.api.WxMpMessageRouter;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.impl.WxMpServiceImpl;
import me.chanjar.weixin.mp.config.impl.WxMpRedisConfigImpl;
import me.chanjar.weixin.mp.constant.WxMpEventConstants;

import java.util.List;
import java.util.Map;

/**
 * 默认的 {@link MpServiceFactory} 实现类
 *
 * @author 芋道源码
 */
@Slf4j
@RequiredArgsConstructor
public class DefaultMpServiceFactory implements MpServiceFactory {

    /**
     * 微信 appId 与 WxMpService 的映射
     */
    private volatile Map<String, WxMpService> mpServices;
    /**
     * 微信 appId 与 WxMpMessageRouter 的映射
     */
    private volatile Map<String, WxMpMessageRouter> mpMessageRouters;

    private final RedisTemplateWxRedisOps redisTemplateWxRedisOps;
    private final WxMpProperties mpProperties;

    // ========== 各种 Handler ==========

    private final LogHandler logHandler;
    private final KfSessionHandler kfSessionHandler;
    private final StoreCheckNotifyHandler storeCheckNotifyHandler;
    private final MenuHandler menuHandler;
    private final NullHandler nullHandler;
    private final SubscribeHandler subscribeHandler;
    private final UnsubscribeHandler unsubscribeHandler;
    private final LocationHandler locationHandler;
    private final ScanHandler scanHandler;
    private final DefaultMessageHandler msgHandler;

    @Override
    public void init(List<MpAccountDO> list) {
        Map<String, WxMpService> mpServices = Maps.newHashMap();
        Map<String, WxMpMessageRouter> mpMessageRouters = Maps.newHashMap();
        // 处理 list
        list.forEach(account -> {
            // 构建 WxMpService 对象
            WxMpService mpService = buildMpService(account);
            mpServices.put(account.getAppId(), mpService);
            // 构建 WxMpMessageRouter 对象
            WxMpMessageRouter mpMessageRouter = buildMpMessageRouter(mpService);
            mpMessageRouters.put(account.getAppId(), mpMessageRouter);
        });

        // 设置到缓存
        this.mpServices = mpServices;
        this.mpMessageRouters = mpMessageRouters;
    }

    @Override
    public WxMpService getMpService(String appId) {
        return mpServices.get(appId);
    }

    @Override
    public WxMpMessageRouter getMpMessageRouter(String appId) {
        return mpMessageRouters.get(appId);
    }

    private WxMpService buildMpService(MpAccountDO account) {
        // 第一步，创建 WxMpRedisConfigImpl 对象
        // TODO 芋艿：需要确认下，redis key 的存储结构
        WxMpRedisConfigImpl configStorage = new WxMpRedisConfigImpl(
                redisTemplateWxRedisOps, mpProperties.getConfigStorage().getKeyPrefix());
        configStorage.setAppId(account.getAppId());
        configStorage.setSecret(account.getAppSecret());
        configStorage.setToken(account.getToken());
        configStorage.setAesKey(account.getAesKey());

        // 第二步，创建 WxMpService 对象
        WxMpService service = new WxMpServiceImpl();
        service.setWxMpConfigStorage(configStorage);
        return null;
    }

    private WxMpMessageRouter buildMpMessageRouter(WxMpService mpService) {
        final WxMpMessageRouter newRouter = new WxMpMessageRouter(mpService);
        // 记录所有事件的日志（异步执行）
        newRouter.rule().handler(logHandler).next();

        // 接收客服会话管理事件
        newRouter.rule().async(false).msgType(WxConsts.XmlMsgType.EVENT)
                .event(WxMpEventConstants.CustomerService.KF_CREATE_SESSION)
                .handler(kfSessionHandler).end();
        newRouter.rule().async(false).msgType(WxConsts.XmlMsgType.EVENT)
                .event(WxMpEventConstants.CustomerService.KF_CLOSE_SESSION)
                .handler(kfSessionHandler)
                .end();
        newRouter.rule().async(false).msgType(WxConsts.XmlMsgType.EVENT)
                .event(WxMpEventConstants.CustomerService.KF_SWITCH_SESSION)
                .handler(kfSessionHandler).end();

        // 门店审核事件
        newRouter.rule().async(false).msgType(WxConsts.XmlMsgType.EVENT)
                .event(WxMpEventConstants.POI_CHECK_NOTIFY)
                .handler(storeCheckNotifyHandler).end();

        // 自定义菜单事件
        newRouter.rule().async(false).msgType(WxConsts.XmlMsgType.EVENT)
                .event(WxConsts.MenuButtonType.CLICK).handler(menuHandler).end();

        // 点击菜单连接事件
        newRouter.rule().async(false).msgType(WxConsts.XmlMsgType.EVENT)
                .event(WxConsts.MenuButtonType.VIEW).handler(nullHandler).end();

        // 关注事件
        newRouter.rule().async(false).msgType(WxConsts.XmlMsgType.EVENT)
                .event(WxConsts.EventType.SUBSCRIBE).handler(subscribeHandler)
                .end();

        // 取消关注事件
        newRouter.rule().async(false).msgType(WxConsts.XmlMsgType.EVENT)
                .event(WxConsts.EventType.UNSUBSCRIBE)
                .handler(unsubscribeHandler).end();

        // 上报地理位置事件
        newRouter.rule().async(false).msgType(WxConsts.XmlMsgType.EVENT)
                .event(WxConsts.EventType.LOCATION).handler(locationHandler)
                .end();

        // 接收地理位置消息
        newRouter.rule().async(false).msgType(WxConsts.XmlMsgType.LOCATION)
                .handler(locationHandler).end();

        // 扫码事件
        newRouter.rule().async(false).msgType(WxConsts.XmlMsgType.EVENT)
                .event(WxConsts.EventType.SCAN).handler(scanHandler).end();

        // 默认
        newRouter.rule().async(false).handler(msgHandler).end();
        return newRouter;
    }

}
