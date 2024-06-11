package pub.carzy.free_port_mapper.server.modules.dto.request;

import lombok.Data;
import lombok.EqualsAndHashCode;
import pub.carzy.free_port_mapper.common.basic.InfoByIdRequest;

/**
* 查询详情请求
* @author Dylan Li
* @since 2023-10-10
*/
@EqualsAndHashCode(callSuper = true)
@Data
public class InfoPortMappingRequest extends InfoByIdRequest<Integer> {
}
