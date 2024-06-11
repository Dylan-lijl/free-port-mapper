package pub.carzy.free_port_mapper.server.config;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import pub.carzy.free_port_mapper.common.api.CommonResult;
import pub.carzy.free_port_mapper.server.exces.ApplicationException;
import pub.carzy.free_port_mapper.common.util.ResponseCode;

/**
 * @author admin
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public CommonResult<String> handleCustomException(Exception e) {
        return new CommonResult<>(ResponseCode.SYSTEM_EXCEPTION.getCode(), ResponseCode.SYSTEM_EXCEPTION.getMessage(), null);
    }

    @ExceptionHandler(ApplicationException.class)
    @ResponseBody
    public CommonResult<String> handleCustomApplicationException(ApplicationException e) {
        ResponseCode code = e.getCode();
        if (code != null) {
            return new CommonResult<>(code.getCode(), code.getMessage(), null);
        } else {
            return new CommonResult<>(ResponseCode.SYSTEM_EXCEPTION.getCode(), e.getMessage(), null);
        }
    }
}
