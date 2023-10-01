package pub.carzy.free_port_mapper.server.modules.dto.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import pub.carzy.free_port_mapper.common.basic.BasicResponse;

import java.util.Date;

/**
 * @author admin
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class InfoClientInfoResponse extends BasicResponse<Integer> {

    @ApiModelProperty(value = "用户名称")
    private String username;

    @ApiModelProperty(value = "密钥")
    private String secretKey;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "修改时间")
    private Date updateTime;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty("状态")
    private Integer state;
}
