package pub.carzy.free_port_mapper.server.modules.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.transaction.annotation.Transactional;
import pub.carzy.free_port_mapper.common.basic.CreateResponse;
import pub.carzy.free_port_mapper.common.basic.UpdateResponse;
import pub.carzy.free_port_mapper.server.exces.ApplicationException;
import pub.carzy.free_port_mapper.server.modules.dto.request.*;
import pub.carzy.free_port_mapper.server.modules.dto.response.InfoClientInfoResponse;
import pub.carzy.free_port_mapper.server.modules.dto.response.ListClientInfoResponse;
import pub.carzy.free_port_mapper.server.modules.service.ClientInfoService;
import org.springframework.stereotype.Service;
import pub.carzy.free_port_mapper.common.api.CommonPage;
import pub.carzy.free_port_mapper.server.modules.mapper.ClientInfoMapper;
import pub.carzy.free_port_mapper.server.modules.model.ClientInfo;
import pub.carzy.free_port_mapper.server.util.MybatisPageToCommonPage;
import pub.carzy.free_port_mapper.server.util.ResponseCode;
import pub.carzy.free_port_mapper.server.util.statics.ClientInfoState;

import java.util.Date;
import java.util.Optional;

/**
 * @author admin
 */
@Service
public class ClientInfoServiceImpl extends ServiceImpl<ClientInfoMapper, ClientInfo> implements ClientInfoService {

    @Override
    public CommonPage<ListClientInfoResponse> pageList(ListClientInfoRequest request) {
        LambdaQueryWrapper<ClientInfo> wrapper = new LambdaQueryWrapper<>();
        //模糊查询
        if (ObjectUtils.isNotEmpty(request.getKeyword())) {
            wrapper.and(q -> q.like(ClientInfo::getUsername, request.getKeyword())
                    .or().like(ClientInfo::getRemark, request.getKeyword())
            );
        }
        //根据用户名排序
        wrapper.orderByAsc(ClientInfo::getUsername);
        Page<ClientInfo> page = page(new Page<>(request.getPageNum(), request.getPageSize()), wrapper);
        //转换成目标bean
        return MybatisPageToCommonPage.convert(page, ListClientInfoResponse.class);
    }

    @Override
    public InfoClientInfoResponse infoData(InfoClientInfoRequest request) {
        ClientInfo info = getById(request.getId());
        if (info == null) {
            throw new ApplicationException(ResponseCode.DATA_NOT_EXIST);
        }
        return BeanUtil.copyProperties(info, InfoClientInfoResponse.class);
    }

    @Override
    public CreateResponse<Integer> createData(CreateClientInfoRequest request) {
        //检查用户名是否重复
        if (count(new LambdaQueryWrapper<ClientInfo>().eq(ClientInfo::getSecretKey, request.getSecretKey())) > 0) {
            throw new ApplicationException(ResponseCode.DUPLICATE_CLIENT_SECRET_KEY);
        }
        ClientInfo info = BeanUtil.copyProperties(request, ClientInfo.class);
        info.setRemark(Optional.ofNullable(info.getRemark()).filter(ObjectUtil::isNull).orElse(""));
        info.setState(ClientInfoState.NORMAL);
        Date now = new Date();
        info.setCreateTime(now);
        info.setUpdateTime(now);
        if (!save(info)) {
            throw new ApplicationException(ResponseCode.FAILED_TO_CREATE_DATA);
        }
        return new CreateResponse<>(info.getId());
    }

    @Override
    public UpdateResponse updateData(UpdateClientInfoRequest request) {
        LambdaUpdateWrapper<ClientInfo> wrapper = new LambdaUpdateWrapper<>();
        if (StrUtil.isNotBlank(request.getUsername())) {
            wrapper.set(ClientInfo::getUsername, request.getUsername());
        }
        if (StrUtil.isNotBlank(request.getSecretKey())) {
            //先判断密钥是否重复
            if (count(new LambdaQueryWrapper<ClientInfo>()
                    .ne(ClientInfo::getId, request.getId())
                    .eq(ClientInfo::getSecretKey, request.getSecretKey())) > 0) {
                throw new ApplicationException(ResponseCode.DUPLICATE_CLIENT_SECRET_KEY);
            }
            wrapper.set(ClientInfo::getSecretKey, request.getSecretKey());
        }
        if (StrUtil.isNotBlank(request.getRemark())) {
            wrapper.set(ClientInfo::getRemark, request.getRemark());
        }
        wrapper.set(ClientInfo::getUpdateTime, new Date());
        wrapper.eq(ClientInfo::getId, request.getId());
        //更新数据
        if (!update(wrapper)) {
            throw new ApplicationException(ResponseCode.FAILED_TO_UPDATE_DATA);
        }
        return new UpdateResponse(true);
    }

    @Override
    public void updateStateData(UpdateClientInfoStateRequest request) {
        //更新状态
        LambdaUpdateWrapper<ClientInfo> wrapper = new LambdaUpdateWrapper<>();
        wrapper.set(ClientInfo::getState, request.getState())
                .set(ClientInfo::getUpdateTime, new Date());
        wrapper.eq(ClientInfo::getId, request.getId());
        if (!update(wrapper)) {
            throw new ApplicationException(ResponseCode.FAILED_TO_UPDATE_DATA);
        }
        //根据状态发送不同的事件 todo
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteData(DeleteClientInfoRequest request) {
        if (request.isOnly()) {
            removeById(request.first());
        } else {
            removeByIds(request.getIds());
        }
    }
}
