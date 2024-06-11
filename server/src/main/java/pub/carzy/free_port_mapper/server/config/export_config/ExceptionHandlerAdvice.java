package pub.carzy.free_port_mapper.server.config.export_config;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import pub.carzy.export_file.exce.ExportFileEmptyException;
import pub.carzy.export_file.exce.ExportNotSupportedException;
import pub.carzy.export_file.exce.SystemErrorException;
import pub.carzy.free_port_mapper.common.api.CommonResult;
import pub.carzy.free_port_mapper.common.util.ResponseCode;

/**
 * @author admin
 */
@ControllerAdvice
public class ExceptionHandlerAdvice {

    @ExceptionHandler({ExportFileEmptyException.class, SystemErrorException.class})
    @ResponseBody
    private CommonResult<Object> exportException() {
        return new CommonResult<>(ResponseCode.SYSTEM_EXCEPTION.getCode(), ResponseCode.SYSTEM_EXCEPTION.getMessage(), null);
    }

    @ExceptionHandler(ExportNotSupportedException.class)
    @ResponseBody
    private CommonResult<Object> exportNotSupportedException() {
        return new CommonResult<>(ResponseCode.FILE_NOT_SUPPORTED.getCode(), ResponseCode.FILE_NOT_SUPPORTED.getMessage(), null);
    }
}
