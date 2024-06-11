package pub.carzy.free_port_mapper.server.modules.dto.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import pub.carzy.free_port_mapper.common.basic.UpdateByIdRequest;
import pub.carzy.validation.annotations.Trim;

import javax.validation.constraints.Size;
import java.util.Date;

/**
* 修改请求
* @author Dylan Li
* @since 2023-10-10
*/
@EqualsAndHashCode(callSuper = true)
@Data
public class UpdatePortMappingRequest extends UpdateByIdRequest<Integer> {
    @ApiModelProperty(value = "代理名称")
    @Trim
    @Size(max = 255 ,message = "字符串超出指定长度")
    private String name;

    @ApiModelProperty(value = "协议,tcp:1,udp:2")
    private Integer protocol;

    @ApiModelProperty(value = "服务端主机名")
    @Trim
    @Size(max = 255 ,message = "字符串超出指定长度")
    private String serverHost;

    @ApiModelProperty(value = "服务端端口")
    private Integer serverPort;

    @ApiModelProperty(value = "客户端主机名")
    @Trim
    @Size(max = 255 ,message = "字符串超出指定长度")
    private String clientHost;

    @ApiModelProperty(value = "客户端端口")
    private Integer clientPort;

    @ApiModelProperty(value = "客户端代理")
    @Trim
    @Size(max = 255 ,message = "字符串超出指定长度")
    private String clientProxy;

}
