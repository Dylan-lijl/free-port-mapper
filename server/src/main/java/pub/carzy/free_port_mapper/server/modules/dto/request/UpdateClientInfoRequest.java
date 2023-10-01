package pub.carzy.free_port_mapper.server.modules.dto.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import pub.carzy.free_port_mapper.common.basic.UpdateByIdRequest;
import pub.carzy.validation.annotations.Trim;

/**
 * @author admin
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class UpdateClientInfoRequest extends UpdateByIdRequest<Integer> {
    @ApiModelProperty(value = "用户名称")
    @Trim
    private String username;

    @ApiModelProperty(value = "密钥")
    @Trim
    private String secretKey;

    @ApiModelProperty(value = "备注")
    @Trim
    private String remark;

}
