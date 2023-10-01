package pub.carzy.free_port_mapper.common.basic;

import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 查询详情请求
 * @author admin
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class InfoByIdRequest<O> extends BasicRequest {
    /** 主键 */
    @NotNull(message = "缺少厂商")
    private O id;
}
