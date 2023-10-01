package pub.carzy.free_port_mapper.server.modules.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 客户信息表
 * </p>
 *
 * @author Dylan Li
 * @since 2023-09-15
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("client_info")
@ApiModel(value = "ClientInfo对象", description = "客户信息表")
public class ClientInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 用户名称
     */
    private String username;

    /**
     * 密钥,不允许重复的原因是需要根据客户端密钥匹配到对应的用户
     */
    private String secretKey;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * 备注
     */
    private String remark;

    /**
     * 状态
     */
    private Integer state;


}
