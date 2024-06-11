package pub.carzy.free_port_mapper.server.modules.dto.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import pub.carzy.free_port_mapper.common.basic.BasicRequest;
import pub.carzy.free_port_mapper.common.util.ResponseMessage;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author admin
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class UpdatePasswordRequest extends BasicRequest {
    @ApiModelProperty(value = "新密码", required = true)
    @NotBlank(message = "缺少新密码")
    @NotNull(message = "缺少新密码")
    @Size(max = 32, message = ResponseMessage.STRING_EXCEEDS_LENGTH)
    private String password;
}
