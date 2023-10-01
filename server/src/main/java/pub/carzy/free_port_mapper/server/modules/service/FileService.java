package pub.carzy.free_port_mapper.server.modules.service;

import pub.carzy.free_port_mapper.server.modules.dto.request.DownloadRequest;
import pub.carzy.free_port_mapper.server.modules.dto.request.UploadRequest;
import pub.carzy.free_port_mapper.server.modules.dto.response.UploadResponse;

/**
 * @author admin
 */
public interface FileService {
    /**
     * 上传文件
     * @param request 请求
     * @return 文件路径
     */
    UploadResponse upload(UploadRequest request);

    /**
     * 下载文件
     * @param request 请求
     */
    void download(DownloadRequest request);
}
