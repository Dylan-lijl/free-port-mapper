package pub.carzy.free_port_mapper.server.modules.dto.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.web.multipart.MultipartFile;
import pub.carzy.free_port_mapper.common.basic.BasicRequest;
import pub.carzy.validation.annotations.Trim;

import javax.validation.constraints.NotNull;

/**
 * @author admin
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class UploadRequest extends BasicRequest {
    @ApiModelProperty(value = "文件",required = true)
    @NotNull(message = "缺少文件")
    private MultipartFile file;
    @ApiModelProperty("路由地址")
    @Trim
    private String router = "tmp";
}
