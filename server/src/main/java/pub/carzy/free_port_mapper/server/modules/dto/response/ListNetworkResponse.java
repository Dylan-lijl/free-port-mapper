package pub.carzy.free_port_mapper.server.modules.dto.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import pub.carzy.free_port_mapper.common.basic.BasicResponse;

/**
 * @author admin
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ListNetworkResponse extends BasicResponse<Void> {
    @ApiModelProperty("名称")
    private String name;
    @ApiModelProperty("描述")
    private String description;
    @ApiModelProperty("MAC地址")
    private String mac;
    @ApiModelProperty("主机名")
    private String host;
    @ApiModelProperty("IP地址")
    private String ip;
    @ApiModelProperty("回送地址")
    private Boolean loopback;

}
