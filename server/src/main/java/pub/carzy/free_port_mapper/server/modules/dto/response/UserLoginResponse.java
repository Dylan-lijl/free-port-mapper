package pub.carzy.free_port_mapper.server.modules.dto.response;

import lombok.Data;
import lombok.EqualsAndHashCode;
import pub.carzy.free_port_mapper.common.basic.BasicResponse;

/**
 * 用户登录响应类
 * @author admin
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class UserLoginResponse extends BasicResponse<Integer> {
    private String token;
}
