package pub.carzy.free_port_mapper.common.basic;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author admin
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class UpdateResponse extends BasicResponse {
    private Boolean success = false;

    public UpdateResponse(Boolean success) {
        this.success = success;
    }

    public UpdateResponse() {
    }
}
