package pub.carzy.free_port_mapper.server.util;

/**
 * @author admin
 */
@SuppressWarnings("unchecked")
public enum ResponseCode {
    SUCCEEDED(200, "请求成功"),
    SUCCESS_EXPORT(204, "生成导出文件成功"),
    FAILED(500, "请求失败!"),
    SYSTEM_EXCEPTION(600, "系统异常!"),
    DATA_NOT_EXIST(700, "对应的数据不存在!"),
    FAILED_TO_CREATE_DATA(701, "新增数据失败!"),
    FAILED_TO_UPDATE_DATA(702, "更新数据失败!"),
    UNREALIZED(703, "该功能未实现,请后续关注!"),
    FILE_NOT_SUPPORTED(704, "不支持该文件类型!"),
    DUPLICATE_CLIENT_SECRET_KEY(801, "客户端密钥重复!"),
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
