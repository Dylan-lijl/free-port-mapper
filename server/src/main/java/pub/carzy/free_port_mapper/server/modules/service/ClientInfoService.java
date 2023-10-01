package pub.carzy.free_port_mapper.server.modules.service;

import com.baomidou.mybatisplus.extension.service.IService;
import pub.carzy.free_port_mapper.common.basic.CreateResponse;
import pub.carzy.free_port_mapper.common.basic.UpdateResponse;
import pub.carzy.free_port_mapper.server.modules.dto.request.*;
import pub.carzy.free_port_mapper.server.modules.dto.response.InfoClientInfoResponse;
import pub.carzy.free_port_mapper.server.modules.dto.response.ListClientInfoResponse;
import pub.carzy.free_port_mapper.common.api.CommonPage;
import pub.carzy.free_port_mapper.server.modules.model.ClientInfo;

/**
 * @author admin
 */
public interface ClientInfoService  extends IService<ClientInfo> {
    /**
     * 分页查询
     * @param request 请求
     * @return 查询结果
     */
    CommonPage<ListClientInfoResponse> pageList(ListClientInfoRequest request);

    /**
     * 客户信息
     * @param request 请求
     * @return 信息
     */
    InfoClientInfoResponse infoData(InfoClientInfoRequest request);

    /**
     * 创建客户信息
     * @param request 请求
     * @return id
     */
    CreateResponse<Integer> createData(CreateClientInfoRequest request);

    /**
     * 修改客户信息
     * @param request
     * @return
     */
    UpdateResponse updateData(UpdateClientInfoRequest request);

    /**
     * 更新状态
     * @param request 请求
     */
    void updateStateData(UpdateClientInfoStateRequest request);

    /**
     * 删除客户信息
     * @param request 请求
     */
    void deleteData(DeleteClientInfoRequest request);
}
