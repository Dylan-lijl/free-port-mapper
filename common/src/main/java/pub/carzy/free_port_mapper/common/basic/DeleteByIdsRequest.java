package pub.carzy.free_port_mapper.common.basic;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author admin
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class DeleteByIdsRequest<T> extends BasicRequest {
    @ApiModelProperty(value = "主键集合",required = true)
    @NotEmpty(message = "缺少ids")
    @NotNull(message = "缺少ids")
    private List<T> ids;

    public boolean isOnly() {
        return ids.size() == 1;
    }

    public T first(){
        return ids.get(0);
    }
}
