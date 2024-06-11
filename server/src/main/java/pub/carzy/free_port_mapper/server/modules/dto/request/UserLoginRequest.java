package pub.carzy.free_port_mapper.server.modules.dto.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import pub.carzy.free_port_mapper.common.basic.BasicRequest;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * 登录请求类
 *
 * @author admin
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class UserLoginRequest extends BasicRequest {
    @ApiModelProperty(value = "密码", required = true)
    @NotBlank(message = "缺少密码")
    @NotNull(message = "缺少密码")
    private String password;
}
