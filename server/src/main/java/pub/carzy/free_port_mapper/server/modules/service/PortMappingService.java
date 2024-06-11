package pub.carzy.free_port_mapper.server.modules.service;

import com.baomidou.mybatisplus.extension.service.IService;
import pub.carzy.free_port_mapper.common.api.CommonPage;
import pub.carzy.free_port_mapper.common.basic.CreateResponse;
import pub.carzy.free_port_mapper.common.basic.UpdateResponse;
import pub.carzy.free_port_mapper.server.modules.dto.inner.PortMappingByProcessor;
import pub.carzy.free_port_mapper.server.modules.dto.request.*;
import pub.carzy.free_port_mapper.server.modules.dto.response.InfoPortMappingResponse;
import pub.carzy.free_port_mapper.server.modules.dto.response.ListPortMappingResponse;
import pub.carzy.free_port_mapper.server.modules.model.PortMapping;

import java.util.List;

/**
 * 端口映射表
 *
 * @author Dylan Li
 * @since 2023-10-10
 */
public interface PortMappingService extends IService<PortMapping> {
    /**
     * 分页查询
     *
     * @param request 请求
     * @return 查询结果
     */
    CommonPage<ListPortMappingResponse> pageList(ListPortMappingRequest request);

    /**
     * 端口映射表
     *
     * @param request 请求
     * @return 信息
     */
    InfoPortMappingResponse infoData(InfoPortMappingRequest request);

    /**
     * 创建端口映射表
     *
     * @param request 请求
     * @return id
     */
    CreateResponse<Integer> createData(CreatePortMappingRequest request);

    /**
     * 修改端口映射表
     *
     * @param request 请求
     * @return 更新是否成功
     */
    UpdateResponse updateData(UpdatePortMappingRequest request);

    /**
     * 删除端口映射表
     *
     * @param request 请求
     */
    void deleteData(DeletePortMappingRequest request);

    /**
     * 更新端口映射状态
     *
     * @param request 请求
     */
    void updateStateData(UpdatePortMappingStateRequest request);

    /**
     * 根据客户id获取映射端口表
     *
     * @param userId 客户id
     * @return 端口映射表
     */
    List<PortMapping> findByUserId(Integer userId);

    /**
     * 根据用户id查询对应端口列表
     *
     * @param userId        用户id
     * @return 端口列表
     */
    default List<PortMappingByProcessor> findByProcessor(Long userId) {
        return findByProcessor(userId, false);
    }
    /**
     * 根据用户id查询对应端口列表
     *
     * @param userId        用户id
     * @param skipDisable 是否跳过禁用
     * @return 端口列表
     */
    List<PortMappingByProcessor> findByProcessor(Long userId, boolean skipDisable);

    /**
     * 根据key,host,port查询映射的代理信息
     * @param key
     * @param hostName
     * @param port
     * @param type
     * @return
     */
    String findByKeyAndHostAndPort(String key, String hostName, int port, int type);
}
