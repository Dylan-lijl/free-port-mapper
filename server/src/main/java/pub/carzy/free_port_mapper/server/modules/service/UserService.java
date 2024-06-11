package pub.carzy.free_port_mapper.server.modules.service;

import pub.carzy.free_port_mapper.common.request.UserClientInfoRequest;
import pub.carzy.free_port_mapper.server.modules.dto.request.*;
import pub.carzy.free_port_mapper.server.modules.dto.response.UpdatePasswordResponse;
import pub.carzy.free_port_mapper.common.response.UserClientInfoResponse;
import pub.carzy.free_port_mapper.server.modules.dto.response.UserLoginResponse;

/**
 * @author admin
 */
public interface UserService {
    /**
     * 登录
     * @param request 请求
     * @return 登录token
     */
    UserLoginResponse login(UserLoginRequest request);

    /**
     * 更新密码
     * @param request 请求
     * @return 更新密码
     */
    UpdatePasswordResponse updatePassword(UpdatePasswordRequest request);

    /**
     * 登出
     * @param request 请求
     */
    void logout(UserLogoutRequest request);

    /**
     * 获取用户的客户端信息
     * @param request 请求
     * @return 客户端信息
     */
    UserClientInfoResponse clientInfo(UserClientInfoRequest request);
}
