package pub.carzy.free_port_mapper.server.modules.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pub.carzy.free_port_mapper.common.api.CommonPage;
import pub.carzy.free_port_mapper.common.api.CommonResult;
import pub.carzy.free_port_mapper.common.basic.CreateResponse;
import pub.carzy.free_port_mapper.common.basic.UpdateResponse;
import pub.carzy.free_port_mapper.server.modules.dto.request.*;
import pub.carzy.free_port_mapper.server.modules.dto.response.InfoPortMappingResponse;
import pub.carzy.free_port_mapper.server.modules.dto.response.ListPortMappingResponse;
import pub.carzy.free_port_mapper.server.modules.service.PortMappingService;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * 端口映射表 前端控制器
 * @author Dylan Li
 * @since 2023-10-10
 */
@RestController
@RequestMapping("/portMapping")
@Api(tags = "端口映射表", description = "端口映射表")
public class PortMappingController {
    @Resource
    private PortMappingService portMappingService;

    @PostMapping("/list")
    @ApiOperation("端口映射表列表")
    public CommonResult<CommonPage<ListPortMappingResponse>> portMappingList(@Valid @RequestBody ListPortMappingRequest request){
            CommonPage<ListPortMappingResponse> response= portMappingService.pageList(request);
            return CommonResult.success(response);
    }

    @PostMapping("/info")
    @ApiOperation("端口映射表详情")
    public CommonResult<InfoPortMappingResponse> portMappingInfo(@Valid @RequestBody InfoPortMappingRequest request){
        InfoPortMappingResponse response= portMappingService.infoData(request);
        return CommonResult.success(response);
    }

    @PostMapping("/create")
    @ApiOperation("创建端口映射表")
    public CommonResult<CreateResponse<Integer>> portMappingCreate(@Valid @RequestBody CreatePortMappingRequest request){
        CreateResponse<Integer> response= portMappingService.createData(request);
        return CommonResult.successCreate(response);
    }

    @PostMapping("/update")
    @ApiOperation("更新端口映射表")
    public CommonResult<UpdateResponse> portMappingUpdate(@Valid @RequestBody UpdatePortMappingRequest request){
        UpdateResponse response= portMappingService.updateData(request);
        return CommonResult.successUpdate(response);
    }

    @PostMapping("/delete")
    @ApiOperation("删除端口映射表")
    public CommonResult<Void> portMappingDelete(@Valid @RequestBody DeletePortMappingRequest request) {
        portMappingService.deleteData(request);
        return CommonResult.successDelete();
    }

    @PostMapping("/state")
    @ApiOperation("更新端口映射状态")
    public CommonResult<Void> portMappingUpdateState(@Valid @RequestBody UpdatePortMappingStateRequest request) {
        portMappingService.updateStateData(request);
        return CommonResult.successUpdate();
    }

}
