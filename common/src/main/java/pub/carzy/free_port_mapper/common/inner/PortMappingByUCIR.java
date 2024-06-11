package pub.carzy.free_port_mapper.common.inner;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import pub.carzy.free_port_mapper.common.basic.BasicResponse;
import pub.carzy.free_port_mapper.common.response.UserClientInfoResponse;

/**
 * 端口信息列表
 *
 * @author admin
 * @see UserClientInfoResponse
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class PortMappingByUCIR extends BasicResponse<Void> {
    @ApiModelProperty("代理名称")
    private String name;
    @ApiModelProperty("协议")
    private Integer protocol;
    @ApiModelProperty("客户端主机名")
    private String clientHost;
    @ApiModelProperty("客户端接口")
    private Integer clientPort;
    @ApiModelProperty("客户端代理")
    private String clientProxy;
    @ApiModelProperty("状态")
    private Integer state;
    @ApiModelProperty("已开启")
    private Boolean opened;
}
