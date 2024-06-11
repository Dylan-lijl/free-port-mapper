package pub.carzy.free_port_mapper.server.modules.dto.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import pub.carzy.free_port_mapper.common.basic.UpdateByIdRequest;
import pub.carzy.free_port_mapper.common.util.ResponseMessage;
import pub.carzy.free_port_mapper.server.util.statics.PortMappingState;
import pub.carzy.validation.constraints.InNumberArray;

import javax.validation.constraints.NotNull;

/**
 * @author admin
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class UpdatePortMappingStateRequest extends UpdateByIdRequest<Integer> {
    @ApiModelProperty(value = "状态", required = true)
    @NotNull(message = "缺少状态")
    @InNumberArray(value = {PortMappingState.DISABLED, PortMappingState.ENABLED}, message = ResponseMessage.ILLEGAL_VALUE)
    private Integer state;
}
