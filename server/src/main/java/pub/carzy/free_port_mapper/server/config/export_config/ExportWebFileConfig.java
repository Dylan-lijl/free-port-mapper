package pub.carzy.free_port_mapper.server.config.export_config;

import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.stereotype.Component;
import pub.carzy.export_file.file_export.entity.ExportRequestParam;
import pub.carzy.export_file.spring_bean.WebEnvConfig;
import pub.carzy.export_file.template.DefaultExportAopCallback;
import pub.carzy.free_port_mapper.common.api.CommonResult;
import pub.carzy.free_port_mapper.common.basic.BasicRequest;
import pub.carzy.free_port_mapper.common.basic.PageRequest;
import pub.carzy.free_port_mapper.server.config.FileConfig;
import pub.carzy.free_port_mapper.server.exces.ApplicationException;
import pub.carzy.free_port_mapper.server.modules.service.FileService;
import pub.carzy.free_port_mapper.server.util.ResponseCode;

import javax.annotation.Resource;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * @author admin
 */
@Component
public class ExportWebFileConfig extends DefaultExportAopCallback {

    @Resource
    private WebEnvConfig webEnvConfig;

    @Resource
    private FileConfig fileConfig;

    /**
     * 接口统一使用对象接收并且使用BasicRequest作为父类
     *
     * @param args 参数
     * @return 对象
     */
    @Override
    public ExportRequestParam getExportParam(Object[] args) {
        for (Object arg : args) {
            if (arg instanceof BasicRequest) {
                ExportRequestParam param = ((BasicRequest) arg).get_export();
                if (param != null) {
                    return param;
                }
            }
            if (arg instanceof ExportRequestParam) {
                return (ExportRequestParam) arg;
            }
        }
        return null;
    }

    @Override
    public void updatePageSize(Object[] args) {
        super.updatePageSize(args);
        for (Object arg : args) {
            if (arg instanceof PageRequest) {
                //更改分页为最大分页,减少访问数据库次数
                ((PageRequest) arg).setPageSize(500);
            }
        }
    }

    /**
     * 返回CommonResult对象,data放文件路径,由前端再次请求下载
     *
     * @return CommonResult对象
     */
    @Override
    public Object responseResult(ExportRequestParam exportRequestParam, ProceedingJoinPoint point, Object obj) {
        Map<String, Object> map = new HashMap<>(4);
        if (!(obj instanceof File)) {
            throw new ApplicationException(ResponseCode.SYSTEM_EXCEPTION);
        }
        File file = (File) obj;
        map.put("_type", 1);
        map.put("_uri", "/" + fileConfig.getTmp().replace("\\", "/") + "/" + file.getName());
        map.put("_name", file.getName().replace(webEnvConfig.getExport().get("prefix"), ""));
        return new CommonResult<>(ResponseCode.SUCCESS_EXPORT.getCode(), ResponseCode.SUCCESS_EXPORT.getMessage(), map);
    }
}
