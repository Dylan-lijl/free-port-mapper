package pub.carzy.free_port_mapper.common.basic;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author admin
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class CreateResponse<T> extends BasicResponse<T>{
    public CreateResponse() {
    }

    public CreateResponse(T id) {
        super(id);
    }
}
