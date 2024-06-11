package pub.carzy.free_port_mapper.server.modules.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pub.carzy.free_port_mapper.common.api.CommonResult;
import pub.carzy.free_port_mapper.common.request.UserClientInfoRequest;
import pub.carzy.free_port_mapper.server.modules.dto.request.*;
import pub.carzy.free_port_mapper.server.modules.dto.response.UpdatePasswordResponse;
import pub.carzy.free_port_mapper.common.response.UserClientInfoResponse;
import pub.carzy.free_port_mapper.server.modules.dto.response.UserLoginResponse;
import pub.carzy.free_port_mapper.server.modules.service.UserService;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * @author admin
 */
@RestController
@Api(value = "用户控制器",description = "用户控制器")
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;

    @PostMapping("/login")
    @ApiOperation("用户登录")
    public CommonResult<UserLoginResponse> userLogin(@Valid @RequestBody UserLoginRequest request) {
        UserLoginResponse response = userService.login(request);
        return CommonResult.success(response);
    }

    @PostMapping("/logout")
    @ApiOperation("用户登出")
    public CommonResult<Void> userLogout(@Valid @RequestBody UserLogoutRequest request) {
        userService.logout(request);
        return CommonResult.success(null,"登出成功!");
    }

    @PostMapping("/updatePassword")
    @ApiOperation("更新密码")
    public CommonResult<UpdatePasswordResponse> userUpdatePassword(@Valid @RequestBody UpdatePasswordRequest request) {
        UpdatePasswordResponse response = userService.updatePassword(request);
        return CommonResult.successUpdate(response);
    }

    @PostMapping("/clientInfo")
    @ApiOperation("获取客户端信息")
    public CommonResult<UserClientInfoResponse> userClientInfo(@Valid @RequestBody UserClientInfoRequest request) {
        UserClientInfoResponse response = userService.clientInfo(request);
        return CommonResult.success(response);
    }
}
