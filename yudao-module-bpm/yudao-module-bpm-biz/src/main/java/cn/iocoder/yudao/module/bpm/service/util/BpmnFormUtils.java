package cn.iocoder.yudao.module.bpm.service.util;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import cn.iocoder.yudao.framework.common.util.json.JsonUtils;
import cn.iocoder.yudao.module.bpm.enums.definition.BpmFieldPermissionEnum;
import com.fasterxml.jackson.core.type.TypeReference;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static cn.iocoder.yudao.module.bpm.framework.flowable.core.enums.SimpleModelConstants.FIELD_ATTRIBUTE;

/**
 *  Bpmn 流程表单相关工具方法
 *
 * @author jason
 */
public class BpmnFormUtils {
    private static final String CREATE_FORM_DISPLAY_ATTRIBUTE = "display";
    private static final String CREATE_FORM_DISABLED_ATTRIBUTE = "disabled";

    /**
     * 修改 form-create 表单组件字段权限规则： 包括可编辑、只读、隐藏规则
     * @param fields 字段规则
     * @param fieldsPermission 字段权限
     * @return 修改权限后的字段规则
     */
    public static List<String> changeCreateFormFiledPermissionRule(List<String> fields, Map<String,Integer> fieldsPermission) {
        if ( CollUtil.isEmpty(fields)  || MapUtil.isEmpty(fieldsPermission)) {
            return fields;
        }
        List<String> afterChangedFields = new ArrayList<>(fields.size());
        fields.forEach( f-> {
            Map<String, Object> fieldMap = JsonUtils.parseObject(f, new TypeReference<>() {});
            String field = ObjUtil.defaultIfNull(fieldMap.get(FIELD_ATTRIBUTE), Object::toString, "");
            if (StrUtil.isEmpty(field) || !fieldsPermission.containsKey(field)) {
                afterChangedFields.add(f);
                return;
            }
            BpmFieldPermissionEnum fieldPermission = BpmFieldPermissionEnum.valueOf(fieldsPermission.get(field));
            Assert.notNull(fieldPermission, "字段权限不匹配");
            if (BpmFieldPermissionEnum.HIDE == fieldPermission) {
                fieldMap.put(CREATE_FORM_DISPLAY_ATTRIBUTE, Boolean.FALSE);
            } else if (BpmFieldPermissionEnum.EDITABLE == fieldPermission){
                Map<String, Object> props =  MapUtil.get(fieldMap, "props", new cn.hutool.core.lang.TypeReference<>() {});
                if (props == null) {
                    props = MapUtil.newHashMap();
                    fieldMap.put("props", props);
                }
                props.put(CREATE_FORM_DISABLED_ATTRIBUTE, Boolean.FALSE);
            } else if (BpmFieldPermissionEnum.READONLY == fieldPermission) {
                Map<String, Object> props =  MapUtil.get(fieldMap, "props", new cn.hutool.core.lang.TypeReference<>() {});
                if (props == null) {
                    props = MapUtil.newHashMap();
                    fieldMap.put("props", props);
                }
                props.put(CREATE_FORM_DISABLED_ATTRIBUTE, Boolean.TRUE);
            }
            afterChangedFields.add(JsonUtils.toJsonString(fieldMap));
        });
        return afterChangedFields;
    }
}
