package cn.iocoder.yudao.module.system.controller.admin.notify;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.system.controller.admin.notify.vo.message.NotifyMessagePageReqVO;
import cn.iocoder.yudao.module.system.controller.admin.notify.vo.message.NotifyMessageRespVO;
import cn.iocoder.yudao.module.system.convert.notify.NotifyMessageConvert;
import cn.iocoder.yudao.module.system.dal.dataobject.notify.NotifyMessageDO;
import cn.iocoder.yudao.module.system.service.notify.NotifyMessageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

/**
 * 管理后台 - 站内信-消息中心
 *
 * @author xrcoder
 */
@Api(tags = "管理后台 - 站内信-消息中心")
@RestController
@RequestMapping("/system/user/notify-message")
@Validated
public class UserNotifyMessageController {

    @Resource
    private NotifyMessageService notifyMessageService;


    @GetMapping("/page")
    @ApiOperation("获得站内信分页")
    public CommonResult<PageResult<NotifyMessageRespVO>> getNotifyMessagePage(@Valid NotifyMessagePageReqVO pageVO) {
        PageResult<NotifyMessageDO> pageResult = notifyMessageService.getNotifyMessagePage(pageVO);
        return success(NotifyMessageConvert.INSTANCE.convertPage(pageResult));
    }

    @GetMapping("/latest/list")
    @ApiOperation("获得最新10站内信列表")
    public CommonResult<List<NotifyMessageRespVO>> getNotifyLatestMessageList() {
        return success(Collections.emptyList());
    }

    @GetMapping("/unread/count")
    @ApiOperation("获得未读站内信数量")
    public CommonResult<Long> getUnreadNotifyMessageCount() {
        return success(1L);
    }

    @GetMapping("/read")
    @ApiOperation("获得站内信")
    @ApiImplicitParam(name = "id", value = "编号", required = true, example = "1024", dataTypeClass = Long.class)
    public CommonResult<NotifyMessageRespVO> readNotifyMessage(@RequestParam("id") Long id) {
        NotifyMessageDO notifyMessage = notifyMessageService.getNotifyMessage(id);
        // TODO 记录消息已读。
        return success(NotifyMessageConvert.INSTANCE.convert(notifyMessage));
    }

    @GetMapping("/read/list")
    @ApiOperation("批量标记已读")
    @ApiImplicitParam(name = "ids", value = "编号列表", required = true, example = "1024,2048", dataTypeClass = List.class)
    public CommonResult<Boolean> batchUpdateNotifyMessageReadStatus(@RequestParam("ids") Collection<Long> ids) {
        return success(Boolean.TRUE);
    }

    @GetMapping("/read/all")
    @ApiOperation("所有未读消息标记已读")
    @ApiImplicitParam(name = "ids", value = "编号列表", required = true, example = "1024,2048", dataTypeClass = List.class)
    public CommonResult<Boolean> batchUpdateAllNotifyMessageReadStatus(@RequestParam("ids") Collection<Long> ids) {
        return success(Boolean.TRUE);
    }
}
