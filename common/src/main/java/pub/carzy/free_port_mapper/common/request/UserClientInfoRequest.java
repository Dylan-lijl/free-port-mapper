package pub.carzy.free_port_mapper.common.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import pub.carzy.free_port_mapper.common.basic.BasicRequest;
import pub.carzy.free_port_mapper.common.util.ResponseMessage;

import javax.validation.constraints.NotBlank;

/**
 * @author admin
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class UserClientInfoRequest extends BasicRequest {
    @ApiModelProperty(value = "密钥", required = true)
    @NotBlank(message = ResponseMessage.STRING_NOT_EMPTY)
    private String secretKey;
}
