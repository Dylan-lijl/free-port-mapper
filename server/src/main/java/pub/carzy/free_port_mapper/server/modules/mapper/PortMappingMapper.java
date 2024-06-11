package pub.carzy.free_port_mapper.server.modules.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import pub.carzy.free_port_mapper.server.modules.model.PortMapping;

import java.util.List;

/**
 * <p>
 * 端口映射表 Mapper 接口
 * </p>
 *
 * @author Dylan Li
 * @since 2023-10-10
 */
public interface PortMappingMapper extends BaseMapper<PortMapping> {

    List<PortMapping> findByKeyAndHostAndPort(@Param("key") String key, @Param("host") String hostName, @Param("port") Integer port);
}
