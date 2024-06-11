package pub.carzy.free_port_mapper.server.modules.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.db.handler.BeanHandler;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pub.carzy.free_port_mapper.common.api.CommonPage;
import pub.carzy.free_port_mapper.common.basic.CreateResponse;
import pub.carzy.free_port_mapper.common.basic.UpdateResponse;
import pub.carzy.free_port_mapper.protocol.ProtocolType;
import pub.carzy.free_port_mapper.protocol.entity.ConnectEntity;
import pub.carzy.free_port_mapper.server.config.EventPublisher;
import pub.carzy.free_port_mapper.server.exces.ApplicationException;
import pub.carzy.free_port_mapper.server.modules.dto.inner.PortMappingByProcessor;
import pub.carzy.free_port_mapper.server.modules.dto.request.*;
import pub.carzy.free_port_mapper.server.modules.dto.response.InfoPortMappingResponse;
import pub.carzy.free_port_mapper.server.modules.dto.response.ListPortMappingResponse;
import pub.carzy.free_port_mapper.server.modules.event.CreatePortMappingEvent;
import pub.carzy.free_port_mapper.server.modules.event.UpdatePortMappingEvent;
import pub.carzy.free_port_mapper.server.modules.mapper.PortMappingMapper;
import pub.carzy.free_port_mapper.server.modules.model.PortMapping;
import pub.carzy.free_port_mapper.server.modules.service.PortMappingService;
import pub.carzy.free_port_mapper.server.util.MybatisPageToCommonPage;
import pub.carzy.free_port_mapper.common.util.ResponseCode;
import pub.carzy.free_port_mapper.server.util.statics.PortMappingState;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * PortMapping Service实现类
 *
 * @author Dylan Li
 * @since 2023-10-10
 */
@Service
public class PortMappingServiceImpl extends ServiceImpl<PortMappingMapper, PortMapping> implements PortMappingService {

    @Resource
    private EventPublisher publisher;

    @Override
    public CommonPage<ListPortMappingResponse> pageList(ListPortMappingRequest request) {
        LambdaQueryWrapper<PortMapping> wrapper = new LambdaQueryWrapper<>();
        // 模糊查询
        if (StrUtil.isNotBlank(request.getKeyword())) {
            wrapper.and(q -> {
                q.like(PortMapping::getName, request.getKeyword());
                q.like(PortMapping::getServerHost, request.getKeyword());
                q.like(PortMapping::getClientHost, request.getKeyword());
                q.like(PortMapping::getClientProxy, request.getKeyword());
            });
        }
        if (ObjectUtil.isNotNull(request.getUserId())) {
            wrapper.eq(PortMapping::getUserId, request.getUserId());
        }
        if (ObjectUtil.isNotNull(request.getState())) {
            wrapper.eq(PortMapping::getState, request.getState());
        }
        Page<PortMapping> page = page(new Page<>(request.getPageNum(), request.getPageSize()), wrapper);
        return MybatisPageToCommonPage.convert(page, ListPortMappingResponse.class);
    }

    @Override
    public InfoPortMappingResponse infoData(InfoPortMappingRequest request) {
        PortMapping info = getById(request.getId());
        if (info == null) {
            throw new ApplicationException(ResponseCode.DATA_NOT_EXIST);
        }
        return BeanUtil.copyProperties(info, InfoPortMappingResponse.class);
    }

    @Override
    public CreateResponse<Integer> createData(CreatePortMappingRequest request) {
        PortMapping info = BeanUtil.copyProperties(request, PortMapping.class);
        //默认值
        info.setClientProxy(Optional.of(info.getClientProxy()).orElse(""));
        //默认所有网卡
        info.setServerHost(Optional.of(info.getServerHost()).orElse("0.0.0.0"));
        info.setState(PortMappingState.DISABLED);
        //创建时间
        Date now = new Date();
        info.setCreateTime(now);
        info.setUpdateTime(now);
        if (!save(info)) {
            throw new ApplicationException(ResponseCode.FAILED_TO_CREATE_DATA);
        }
        //使用多播器发布事件
        publisher.publishEvent(new CreatePortMappingEvent(this, info));
        return new CreateResponse<>(info.getId());
    }

    @Override
    public UpdateResponse updateData(UpdatePortMappingRequest request) {
        PortMapping old = getById(request.getId());
        if (old == null) {
            throw new ApplicationException(ResponseCode.DATA_NOT_EXIST);
        }
        PortMapping newObj = BeanUtil.copyProperties(request, PortMapping.class);
        updateById(newObj);
        try {
            publisher.publishEvent(new UpdatePortMappingEvent(this, old, newObj));
        }catch (Exception e){
            //同步失败
        }
        return new UpdateResponse(false);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteData(DeletePortMappingRequest request) {
        if (request.isOnly()) {
            removeById(request.first());
        } else {
            removeByIds(request.getIds());
        }
    }

    @Override
    public void updateStateData(UpdatePortMappingStateRequest request) {
        LambdaUpdateWrapper<PortMapping> wrapper = new LambdaUpdateWrapper<>();
        wrapper.set(PortMapping::getState, request.getState())
                .set(PortMapping::getUpdateTime, new Date());
        wrapper.eq(PortMapping::getId, request.getId());
        if (!update(wrapper)) {
            throw new ApplicationException(ResponseCode.FAILED_TO_UPDATE_DATA);
        }
        //发布事件
    }

    @Override
    public List<PortMapping> findByUserId(Integer userId) {
        LambdaQueryWrapper<PortMapping> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(PortMapping::getUserId, userId);
        return list(wrapper);
    }

    @Override
    public List<PortMappingByProcessor> findByProcessor(Long userId, boolean skipDisable) {
        LambdaQueryWrapper<PortMapping> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(PortMapping::getId,
                        PortMapping::getClientHost,
                        PortMapping::getServerHost,
                        PortMapping::getServerPort,
                        PortMapping::getClientPort,
                        PortMapping::getClientProxy,
                        PortMapping::getProtocol,
                        PortMapping::getState)
                .eq(PortMapping::getUserId, userId);
        if (skipDisable) {
            wrapper.eq(PortMapping::getState, PortMappingState.ENABLED);
        }
        List<PortMapping> list = list(wrapper);
        if (list.size() > 0) {
            List<PortMappingByProcessor> mappings = new ArrayList<>();
            list.forEach(item -> mappings.add(BeanUtil.copyProperties(item, PortMappingByProcessor.class)));
            return mappings;
        }
        return new ArrayList<>();
    }

    @Override
    public String findByKeyAndHostAndPort(String key, String hostName, int port, int type) {
        //todo 使用缓存
        List<PortMapping> mappings = baseMapper.findByKeyAndHostAndPort(key, hostName, port);
        if (mappings.size() > 0) {
            for (PortMapping mapping : mappings) {
                String json = JSONObject.toJSONString(BeanUtil.copyProperties(mapping, ConnectEntity.class));
                if (ProtocolType.isTcp(mapping.getProtocol()) & type == ProtocolType.TCP) {
                    return json;
                }
                if (ProtocolType.isUdp(mapping.getProtocol()) && type == ProtocolType.UDP) {
                    return json;
                }
            }
        }
        return null;
    }

}
