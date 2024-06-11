package pub.carzy.free_port_mapper.server.config.export_config;

import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.stereotype.Component;
import pub.carzy.export_file.file_export.actuator.AbstractExportActuator;
import pub.carzy.export_file.file_export.actuator.ExportActuator;
import pub.carzy.export_file.file_export.actuator.ExportActuatorParam;
import pub.carzy.export_file.file_export.entity.ExportRequestParam;
import pub.carzy.export_file.file_export.suppers.AbstractExportFiler;
import pub.carzy.free_port_mapper.common.api.CommonResult;
import pub.carzy.free_port_mapper.server.exces.ApplicationException;
import pub.carzy.free_port_mapper.common.util.ResponseCode;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 处理common类型数据
 *
 * @author admin
 */
@Component
public class CommonExport extends AbstractExportFiler {
    @Override
    public boolean match(ExportRequestParam param, Object data) {
        return super.match(param, data) && data instanceof CommonResult;
    }

    @Override
    public int getOrder() {
        return super.getOrder() - ExportType.COMMON;
    }

    @Override
    protected ExportActuator createExportActuator(Object data, ProceedingJoinPoint point, ExportActuatorParam actuatorParam) {
        return new AbstractExportActuator(data, point, actuatorParam, getActuatorConfig()) {

            @Override
            protected List<List<Object>> transformContent() {
                List<List<Object>> list = new ArrayList<>();
                if (data == null) {
                    return null;
                }
                CommonResult<?> result = (CommonResult<?>) data;
                Object data = result.getData();
                if (data == null) {
                    return null;
                }
                if (data instanceof Map) {
                    // Map类型,没有这样的业务,不实现
                    throw new ApplicationException(ResponseCode.UNREALIZED);
                } else if (data instanceof Iterable) {
                    //可迭代对象
                    Iterator<?> iterator = ((Iterable<?>) data).iterator();
                    while (iterator.hasNext()) {
                        List<Object> line = new ArrayList<>();
                        Object next = iterator.next();
                        parseAndPutObjectValue(line, next);
                        if (line.size() > 0) {
                            list.add(line);
                        }
                    }
                } else {
                    //对象类型
                    List<Object> line = new ArrayList<>();
                    parseAndPutObjectValue(line, data);
                    if (line.size() <= 0) {
                        return null;
                    }
                    list.add(line);
                }
                return list.size() > 0 ? list : null;
            }
        };
    }
}
