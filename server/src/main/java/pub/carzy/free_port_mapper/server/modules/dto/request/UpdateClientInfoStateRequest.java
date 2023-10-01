package pub.carzy.free_port_mapper.server.modules.dto.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import pub.carzy.free_port_mapper.common.basic.UpdateByIdRequest;

import javax.validation.constraints.NotNull;

/**
 * @author admin
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class UpdateClientInfoStateRequest extends UpdateByIdRequest<Integer> {
    @ApiModelProperty(value = "状态",required = true)
    @NotNull(message = "缺少状态")
    private Integer state;
}
