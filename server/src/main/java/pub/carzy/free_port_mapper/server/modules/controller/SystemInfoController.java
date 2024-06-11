package pub.carzy.free_port_mapper.server.modules.controller;

import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pub.carzy.export_file.file_export.ExportMethod;
import pub.carzy.free_port_mapper.common.api.CommonResult;
import pub.carzy.free_port_mapper.server.modules.dto.request.ListNetworkRequest;
import pub.carzy.free_port_mapper.server.modules.dto.response.ListNetworkResponse;
import pub.carzy.free_port_mapper.server.modules.service.SystemInfoService;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

/**
 * @author admin
 */
@RestController
@RequestMapping("/system")
public class SystemInfoController {

    @Resource
    private SystemInfoService systemInfoService;

    @ApiOperation("网卡信息")
    @RequestMapping("/network")
    @ExportMethod(filename = "网卡信息")
    public CommonResult<List<ListNetworkResponse>> networkList(@Valid @RequestBody ListNetworkRequest request) {
        List<ListNetworkResponse> response = systemInfoService.all(request);
        return CommonResult.success(response);
    }
}
