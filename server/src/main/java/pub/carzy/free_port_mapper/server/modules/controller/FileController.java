package pub.carzy.free_port_mapper.server.modules.controller;

import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pub.carzy.free_port_mapper.common.api.CommonResult;
import pub.carzy.free_port_mapper.server.modules.dto.request.DownloadRequest;
import pub.carzy.free_port_mapper.server.modules.dto.request.UploadRequest;
import pub.carzy.free_port_mapper.server.modules.dto.response.UploadResponse;
import pub.carzy.free_port_mapper.server.modules.service.FileService;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * 文件管理器
 * @author admin
 */
@RestController
@RequestMapping("/file")
public class FileController {

    @Resource
    private FileService fileService;

    @RequestMapping("/upload")
    @ApiOperation("上传文件")
    public CommonResult<UploadResponse> upload(@Valid UploadRequest request) {
        UploadResponse response = fileService.upload(request);
        return CommonResult.success(response);
    }

    @RequestMapping("/download")
    @ApiOperation("下载文件")
    public void download(@Valid DownloadRequest request) {
        fileService.download(request);
    }
}
