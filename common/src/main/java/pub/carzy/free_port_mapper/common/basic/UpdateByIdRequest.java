package pub.carzy.free_port_mapper.common.basic;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;

/**
 * @author admin
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class UpdateByIdRequest<O> extends BasicRequest {
    @NotNull(message = "缺少id")
    private O id;
}
