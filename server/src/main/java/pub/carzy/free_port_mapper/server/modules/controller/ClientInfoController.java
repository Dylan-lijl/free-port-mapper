package pub.carzy.free_port_mapper.server.modules.controller;

import io.swagger.annotations.Api;
import pub.carzy.export_file.file_export.ExportMethod;
import pub.carzy.free_port_mapper.common.basic.CreateResponse;
import pub.carzy.free_port_mapper.common.basic.UpdateResponse;
import pub.carzy.free_port_mapper.server.modules.dto.request.*;
import pub.carzy.free_port_mapper.server.modules.dto.response.InfoClientInfoResponse;
import pub.carzy.free_port_mapper.server.modules.dto.response.ListClientInfoResponse;
import pub.carzy.free_port_mapper.server.modules.service.ClientInfoService;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pub.carzy.free_port_mapper.common.api.CommonPage;
import pub.carzy.free_port_mapper.common.api.CommonResult;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * 客户端信息
 * @author admin
 */
@RestController
@RequestMapping("/clientInfo")
@Api(tags = "客户端信息",description = "客户端信息")
public class ClientInfoController {
    @Resource
    private ClientInfoService clientInfoService;

    @PostMapping("/list")
    @ApiOperation("客户端信息列表")
    @ExportMethod(filename = "客户端信息列表")
    public CommonResult<CommonPage<ListClientInfoResponse>> clientInfoList(@Valid @RequestBody ListClientInfoRequest request) {
        CommonPage<ListClientInfoResponse> response = clientInfoService.pageList(request);
        return CommonResult.success(response);
    }

    @PostMapping("/info")
    @ApiOperation("客户端信息详情")
    public CommonResult<InfoClientInfoResponse> clientInfoInfo(@Valid @RequestBody InfoClientInfoRequest request) {
        InfoClientInfoResponse response = clientInfoService.infoData(request);
        return CommonResult.success(response);
    }

    @PostMapping("/create")
    @ApiOperation("创建客户端信息")
    public CommonResult<CreateResponse<Integer>> clientInfoCreate(@Valid @RequestBody CreateClientInfoRequest request) {
        CreateResponse<Integer> response = clientInfoService.createData(request);
        return CommonResult.successCreate(response);
    }

    @PostMapping("/update")
    @ApiOperation("更新客户端信息")
    public CommonResult<UpdateResponse> clientInfoUpdate(@Valid @RequestBody UpdateClientInfoRequest request) {
        UpdateResponse response = clientInfoService.updateData(request);
        return CommonResult.successUpdate(response);
    }

    @PostMapping("/state")
    @ApiOperation("更新客户端信息状态")
    public CommonResult<Void> clientInfoUpdateState(@Valid @RequestBody UpdateClientInfoStateRequest request) {
        clientInfoService.updateStateData(request);
        return CommonResult.successUpdate();
    }

    @PostMapping("/delete")
    @ApiOperation("删除客户端信息")
    public CommonResult<Void> clientInfoDelete(@Valid @RequestBody DeleteClientInfoRequest request) {
        clientInfoService.deleteData(request);
        return CommonResult.successDelete();
    }
}
