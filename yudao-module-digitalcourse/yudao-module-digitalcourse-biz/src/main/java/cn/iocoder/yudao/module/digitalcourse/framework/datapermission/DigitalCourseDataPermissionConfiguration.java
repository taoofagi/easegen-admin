package cn.iocoder.yudao.module.digitalcourse.framework.datapermission;

import cn.iocoder.yudao.framework.datapermission.core.rule.dept.DeptDataPermissionRuleCustomizer;
import cn.iocoder.yudao.module.digitalcourse.dal.dataobject.coursemedia.CourseMediaDO;
import cn.iocoder.yudao.module.digitalcourse.dal.dataobject.courses.CoursesDO;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * system 模块的数据权限 Configuration
 *
 * @author 芋道源码
 */
@Configuration(proxyBeanMethods = false)
public class DigitalCourseDataPermissionConfiguration {

    @Bean
    public DeptDataPermissionRuleCustomizer digitalCourseDeptDataPermissionRuleCustomizer() {
        return rule -> {
            //courses
            rule.addUserColumn(CoursesDO.class, "creator");
            //coursemedia
            rule.addUserColumn(CourseMediaDO.class, "creator");
        };
    }

}
