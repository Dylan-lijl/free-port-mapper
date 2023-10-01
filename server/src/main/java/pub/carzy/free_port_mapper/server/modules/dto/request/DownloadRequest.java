package pub.carzy.free_port_mapper.server.modules.dto.request;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.bytebuddy.implementation.bind.annotation.Empty;
import pub.carzy.free_port_mapper.common.basic.BasicRequest;
import pub.carzy.validation.annotations.Trim;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @author admin
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class DownloadRequest extends BasicRequest {
    @ApiModelProperty(value = "路径",required = true)
    @NotBlank(message = "缺少文件路径")
    @Trim
    private String path;
    private Boolean removeTmp = true;
    @Trim
    private String filename;
}
