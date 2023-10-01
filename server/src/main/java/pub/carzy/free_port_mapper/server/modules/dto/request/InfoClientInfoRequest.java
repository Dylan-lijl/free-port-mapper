package pub.carzy.free_port_mapper.server.modules.dto.request;

import lombok.Data;
import lombok.EqualsAndHashCode;
import pub.carzy.free_port_mapper.common.basic.InfoByIdRequest;

/**
 * @author admin
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class InfoClientInfoRequest extends InfoByIdRequest<Integer> {
}
