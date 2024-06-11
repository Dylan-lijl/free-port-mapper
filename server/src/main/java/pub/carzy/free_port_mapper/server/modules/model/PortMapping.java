package pub.carzy.free_port_mapper.server.modules.model;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 端口映射表
 * </p>
 *
 * @author Dylan Li
 * @since 2023-10-10
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("port_mapping")
public class PortMapping implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 代理名称
     */
    private String name;
    /**
     * 协议,tcp:1,udp:2
     */
    private Integer protocol;
    /**
     * 服务端主机名
     */
    private String serverHost;
    /**
     * 服务端端口
     */
    private Integer serverPort;
    /**
     * 客户端主机名
     */
    private String clientHost;
    /**
     * 客户端端口
     */
    private Integer clientPort;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 修改时间
     */
    private Date updateTime;
    /**
     * 用户id
     */
    private Integer userId;
    /**
     * 客户端代理
     */
    private String clientProxy;
    /**
     * 状态
     */
    private Integer state;
}
