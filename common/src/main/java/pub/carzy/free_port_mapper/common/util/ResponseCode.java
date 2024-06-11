package pub.carzy.free_port_mapper.common.util;

/**
 * @author admin
 */
@SuppressWarnings("unchecked")
public enum ResponseCode {
    SUCCEEDED(200, "请求成功"),
    SUCCESS_EXPORT(204, "生成导出文件成功"),
    NOT_LOGGED_IN(403, "未登录或登录已过期!"),
    FAILED(500, "请求失败!"),
    SYSTEM_EXCEPTION(600, "系统异常!"),
    DATA_NOT_EXIST(700, "对应的数据不存在!"),
    FAILED_TO_CREATE_DATA(701, "新增数据失败!"),
    FAILED_TO_UPDATE_DATA(702, "更新数据失败!"),
    UNREALIZED(703, "该功能未实现,请后续关注!"),
    FILE_NOT_SUPPORTED(704, "不支持该文件类型!"),
    DUPLICATE_CLIENT_SECRET_KEY(801, "客户端密钥重复!"),
    PASSWORD_ERROR(802, "密码校验失败!"),
    PASSWORD_UPDATE_ERROR(803, "修改密码失败!"),
    LOGOUT_ERROR(804, "登出失败!"),
    NOT_FOUND_CLIENT_INFO(850, "未找到该客户!"),
    DISABLED_BY_CLIENT_INFO(851,"该客户已被禁用!" ),
    FAILED_TO_GENERATE_UNIQUE_IDENTITY(852,"生成唯一标识失败!" )
    ;
    /**
     * 状态码
     */
    private Integer code;
    /**
     * 信息
     */
    private String message;

    ResponseCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
