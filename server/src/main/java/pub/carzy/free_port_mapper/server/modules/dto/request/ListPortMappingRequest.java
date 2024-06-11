package pub.carzy.free_port_mapper.server.modules.dto.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import pub.carzy.free_port_mapper.common.basic.PageRequest;

/**
* 查询列表请求
* @author Dylan Li
* @since 2023-10-10
*/
@EqualsAndHashCode(callSuper = true)
@Data
public class ListPortMappingRequest extends PageRequest {
    @ApiModelProperty("用户id")
    private Integer userId;
    @ApiModelProperty("状态")
    private Integer state;
}
