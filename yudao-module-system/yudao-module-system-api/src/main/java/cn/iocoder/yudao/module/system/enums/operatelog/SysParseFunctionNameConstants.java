package cn.iocoder.yudao.module.system.enums.operatelog;

/**
 * functionName 常量枚举
 * 方便别的模块调用
 *
 * @author HUIHUI
 */
// TODO @puhui999：这个枚举，还是放在对应的 Function 里好。主要考虑，和 Function 实现可以更近一点哈
public interface SysParseFunctionNameConstants {

    String GET_ADMIN_USER_BY_ID = "getAdminUserById"; // 获取用户信息
    String GET_DEPT_BY_ID = "getDeptById"; // 获取部门信息
    String GET_AREA = "getArea"; // 获取区域信息
    String GET_SEX = "getSex"; // 获取性别
    String GET_BOOLEAN = "getBoolean"; // 获取是否

}
