package pub.carzy.free_port_mapper.common.response;

import lombok.Data;
import lombok.EqualsAndHashCode;
import pub.carzy.free_port_mapper.common.basic.BasicResponse;
import pub.carzy.free_port_mapper.common.inner.PortMappingByUCIR;

import java.util.ArrayList;
import java.util.List;

/**
 * @author admin
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class UserClientInfoResponse extends BasicResponse<Integer> {
    private List<PortMappingByUCIR> mappings = new ArrayList<>();
    private Integer id;
}
