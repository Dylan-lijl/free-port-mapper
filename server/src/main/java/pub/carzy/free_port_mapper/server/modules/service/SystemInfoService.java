package pub.carzy.free_port_mapper.server.modules.service;

import pub.carzy.free_port_mapper.server.modules.dto.request.ListNetworkRequest;
import pub.carzy.free_port_mapper.server.modules.dto.response.ListNetworkResponse;

import java.util.List;

/**
 * @author admin
 */
public interface SystemInfoService {
    List<ListNetworkResponse> all(ListNetworkRequest request);
}
