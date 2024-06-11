package pub.carzy.free_port_mapper.server.modules.dto.response;

import lombok.Data;
import lombok.EqualsAndHashCode;
import pub.carzy.free_port_mapper.common.basic.BasicResponse;

/**
 * @author admin
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class UpdatePasswordResponse extends BasicResponse<Void> {
    private String token;
}
