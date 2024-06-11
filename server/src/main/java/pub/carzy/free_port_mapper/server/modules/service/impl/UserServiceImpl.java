package pub.carzy.free_port_mapper.server.modules.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pub.carzy.free_port_mapper.common.request.UserClientInfoRequest;
import pub.carzy.free_port_mapper.server.config.SettingConfig;
import pub.carzy.free_port_mapper.server.exces.ApplicationException;
import pub.carzy.free_port_mapper.common.inner.PortMappingByUCIR;
import pub.carzy.free_port_mapper.server.modules.dto.request.*;
import pub.carzy.free_port_mapper.server.modules.dto.response.UpdatePasswordResponse;
import pub.carzy.free_port_mapper.common.response.UserClientInfoResponse;
import pub.carzy.free_port_mapper.server.modules.dto.response.UserLoginResponse;
import pub.carzy.free_port_mapper.server.modules.model.ClientInfo;
import pub.carzy.free_port_mapper.server.modules.model.PortMapping;
import pub.carzy.free_port_mapper.server.modules.service.ClientInfoService;
import pub.carzy.free_port_mapper.server.modules.service.PortMappingService;
import pub.carzy.free_port_mapper.server.modules.service.UserService;
import pub.carzy.free_port_mapper.server.util.HttpThreadLocal;
import pub.carzy.free_port_mapper.server.util.JwtUtils;
import pub.carzy.free_port_mapper.common.util.ResponseCode;
import pub.carzy.free_port_mapper.server.util.statics.ClientInfoState;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * @author admin
 */
@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Resource
    private SettingConfig settingConfig;

    @Resource
    private Set<String> blacklist;

    @Resource
    private JwtUtils jwtUtils;

    @Resource
    private ClientInfoService clientInfoService;

    @Resource
    private PortMappingService portMappingService;

    @Resource
    private Map<Integer, ClientInfo> loginMap;

    @Override
    public UserLoginResponse login(UserLoginRequest request) {
        if (!request.getPassword().equals(settingConfig.getPassword())) {
            throw new ApplicationException(ResponseCode.PASSWORD_ERROR);
        }
        //颁布token
        String token = jwtUtils.getToken(new HashMap<>(2), settingConfig.getDuration());
        UserLoginResponse response = new UserLoginResponse();
        response.setToken(token);
        return response;
    }

    @Override
    public UpdatePasswordResponse updatePassword(UpdatePasswordRequest request) {
        String password = request.getPassword();
        //将新密码写到文件中
        File file = new File(".password").getAbsoluteFile();
        if (file.exists()) {
            if (!file.delete()) {
                log.warn("删除文件失败:" + file.getAbsoluteFile().getAbsolutePath());
            }
        }
        try {
            if (!file.createNewFile()) {
                log.warn("创建文件失败:" + file.getAbsolutePath());
            }
        } catch (IOException e) {
            throw new ApplicationException(ResponseCode.PASSWORD_UPDATE_ERROR);
        }
        FileUtil.writeBytes(password.getBytes(StandardCharsets.UTF_8), file.getAbsoluteFile());
        String t = HttpThreadLocal.TOKEN.get();
        if (StrUtil.isEmpty(t)) {
            throw new ApplicationException(ResponseCode.NOT_LOGGED_IN);
        }
        //将当前token追加到黑名单中
        try {
            writeBlacklistToken(t);
        } catch (IOException e) {
            throw new ApplicationException(ResponseCode.PASSWORD_UPDATE_ERROR);
        }
        blacklist.add(t);
        //重新生成token并注销当前token
        String token = jwtUtils.getToken(new HashMap<>(2), settingConfig.getDuration());
        UpdatePasswordResponse response = new UpdatePasswordResponse();
        response.setToken(token);
        settingConfig.setPassword(password);
        return response;
    }

    private void writeBlacklistToken(String t) throws IOException {
        File file = new File(".blacklist").getAbsoluteFile();
        if (!file.exists()) {
            if (!file.createNewFile()) {
                log.warn("创建文件失败:" + file.getAbsolutePath());
            }
        }
        //追加黑名单
        FileUtil.appendLines(Collections.singleton(t), file, StandardCharsets.UTF_8);
    }

    @Override
    public void logout(UserLogoutRequest request) {
        String token = HttpThreadLocal.TOKEN.get();
        if (StrUtil.isEmpty(token)) {
            // throw new ApplicationException(ResponseCode.NOT_LOGGED_IN);
            return;
        }
        //将token列入黑名单
        try {
            writeBlacklistToken(token);
        } catch (IOException e) {
            // throw new ApplicationException(ResponseCode.LOGOUT_ERROR);
            return;
        }
        //添加到缓存当中
        blacklist.add(token);
    }

    @Override
    public UserClientInfoResponse clientInfo(UserClientInfoRequest request) {
        //根据密钥获取数据
        String secretKey = request.getSecretKey();
        ClientInfo clientInfo = clientInfoService.findBySecretKey(secretKey);
        if (clientInfo == null) {
            throw new ApplicationException(ResponseCode.NOT_FOUND_CLIENT_INFO);
        }
        if (clientInfo.getState() == ClientInfoState.DISABLED) {
            throw new ApplicationException(ResponseCode.DISABLED_BY_CLIENT_INFO);
        }
        UserClientInfoResponse response = new UserClientInfoResponse();
        response.setId(clientInfo.getId());
        //根据客户id获取端口映射表
        List<PortMapping> portMappings = portMappingService.findByUserId(clientInfo.getId());
        portMappings.forEach(item -> {
            PortMappingByUCIR portMapping = BeanUtil.copyProperties(item, PortMappingByUCIR.class);
            //设置是否已开启状态 todo
            response.getMappings().add(portMapping);
        });
        //运算hash值来当id
        response.setId(hashToId(secretKey));
        //绑定对象
        loginMap.put(response.getId(), clientInfo);
        //暂时先不用缓存
        return response;
    }

    private Integer hashToId(String secretKey) {
        int count = 0;
        while (count <= 10) {
            count++;
            int hash = System.identityHashCode(secretKey + System.currentTimeMillis());
            if (!loginMap.containsKey(hash)) {
                return hash;
            }
        }
        throw new ApplicationException(ResponseCode.FAILED_TO_GENERATE_UNIQUE_IDENTITY);
    }
}
