package pub.carzy.free_port_mapper.server.modules.dto.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import pub.carzy.free_port_mapper.common.basic.CreateRequest;
import pub.carzy.validation.annotations.Trim;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
* 新增请求
* @author Dylan Li
* @since 2023-10-10
*/
@EqualsAndHashCode(callSuper = true)
@Data
public class CreatePortMappingRequest extends CreateRequest {
    @ApiModelProperty(value = "代理名称" ,required = true )
    @NotBlank(message = "缺少代理名称")
    @Trim
    @Size(max = 255 ,message = "字符串超出指定长度")
    private String name;

    @ApiModelProperty(value = "协议,tcp:0,udp:1" ,required = true )
    @NotNull(message = "缺少协议,tcp:0,udp:1")
    private Integer protocol;

    @ApiModelProperty(value = "服务端主机名" ,required = true )
    @Trim
    @Size(max = 255 ,message = "字符串超出指定长度")
    private String serverHost;

    @ApiModelProperty(value = "服务端端口" ,required = true )
    @NotNull(message = "缺少服务端端口")
    private Integer serverPort;

    @ApiModelProperty(value = "客户端主机名" ,required = true )
    @NotBlank(message = "缺少客户端主机名")
    @Trim
    @Size(max = 255 ,message = "字符串超出指定长度")
    private String clientHost;

    @ApiModelProperty(value = "客户端端口" ,required = true )
    @NotNull(message = "缺少客户端端口")
    private Integer clientPort;

    @ApiModelProperty(value = "用户id" ,required = true )
    @NotNull(message = "缺少用户id")
    private Integer userId;

    @ApiModelProperty(value = "客户端代理" ,required = true )
    @Trim
    @Size(max = 255 ,message = "字符串超出指定长度")
    private String clientProxy;

}
