package pub.carzy.free_port_mapper.server.modules.dto.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import pub.carzy.free_port_mapper.common.basic.CreateRequest;
import pub.carzy.free_port_mapper.server.util.ResponseMessage;
import pub.carzy.validation.annotations.Trim;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * @author admin
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class CreateClientInfoRequest extends CreateRequest {

    @ApiModelProperty(value = "用户名称", required = true)
    @NotBlank(message = "缺少用户名称")
    @Size(max = 255, message = ResponseMessage.STRING_EXCEEDS_LENGTH)
    @Trim
    private String username;

    @ApiModelProperty(value = "密钥", required = true)
    @NotBlank(message = ResponseMessage.STRING_NOT_EMPTY)
    @Trim
    private String secretKey;

    @ApiModelProperty(value = "备注")
    @Trim
    private String remark;
}
