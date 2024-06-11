package pub.carzy.free_port_mapper.server.modules.dto.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import pub.carzy.free_port_mapper.common.basic.BasicResponse;

import java.util.Date;

/**
 * 详情响应
 *
 * @author Dylan Li
 * @since 2023-10-10
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ListPortMappingResponse extends BasicResponse<Integer> {
    @ApiModelProperty(value = "代理名称")
    private String name;

    @ApiModelProperty(value = "协议,tcp:1,udp:2")
    private Integer protocol;

    @ApiModelProperty(value = "服务端主机名")
    private String serverHost;

    @ApiModelProperty(value = "服务端端口")
    private Integer serverPort;

    @ApiModelProperty(value = "客户端主机名")
    private String clientHost;

    @ApiModelProperty(value = "客户端端口")
    private Integer clientPort;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "修改时间")
    private Date updateTime;

    @ApiModelProperty(value = "用户id")
    private Integer userId;

    @ApiModelProperty(value = "客户端代理")
    private String clientProxy;

    @ApiModelProperty("状态")
    private Integer state;

}
