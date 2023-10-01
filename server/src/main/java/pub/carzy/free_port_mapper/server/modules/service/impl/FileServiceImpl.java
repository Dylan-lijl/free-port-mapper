package pub.carzy.free_port_mapper.server.modules.service.impl;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pub.carzy.free_port_mapper.server.config.FileConfig;
import pub.carzy.free_port_mapper.server.exces.ApplicationException;
import pub.carzy.free_port_mapper.server.modules.dto.request.DownloadRequest;
import pub.carzy.free_port_mapper.server.modules.dto.request.UploadRequest;
import pub.carzy.free_port_mapper.server.modules.dto.response.UploadResponse;
import pub.carzy.free_port_mapper.server.modules.service.FileService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

/**
 * @author admin
 */
@Service
@Slf4j
public class FileServiceImpl implements FileService {

    @Resource
    private FileConfig fileConfig;

    @Resource
    private HttpServletResponse response;

    @Override
    public UploadResponse upload(UploadRequest request) {
        String uuid = UUID.randomUUID().toString();
        MultipartFile file = request.getFile();
        String filename = fileConfig.getPrefix().replace("{timestamp}", System.currentTimeMillis() + "_")
                .replace("{uuid}", uuid + "_")
                .replace("{simple_uuid}", uuid.replace("-", "") + "_")
                + file.getOriginalFilename();
        File parentFile;
        String baseUri;
        if (StrUtil.isEmpty(request.getRouter())) {
            parentFile = fileConfig.tmpFile();
            baseUri = fileConfig.getTmp();
        } else if ("tmp".equals(request.getRouter())) {
            parentFile = fileConfig.tmpFile();
            baseUri = fileConfig.getTmp();
        } else if (fileConfig.hasOtherByName(request.getRouter())) {
            parentFile = fileConfig.otherFile(request.getRouter());
            baseUri = fileConfig.getOthers().get(request.getRouter());
        } else {
            throw new ApplicationException("未找到符合路由文件夹!");
        }
        File destFile = new File(parentFile, filename);
        if (destFile.exists()) {
            //删除
            destFile.delete();
        }
        try {
            file.transferTo(destFile);
        } catch (IOException e) {
            log.warn("保存文件异常:" + e.getMessage());
            throw new ApplicationException("保存文件异常");
        }
        return new UploadResponse(baseUri + "/" + destFile.getName());
    }

    @Override
    public void download(DownloadRequest request) {
        //替换../之类的路径
        String path = request.getPath().replace("../", "").replace("..\\\\", "");
        File file = new File(fileConfig.rootFile(), path);
        if (!file.exists()) {
            throw new ApplicationException("文件不存在!");
        }
        Path p = Paths.get(file.getAbsoluteFile().getAbsolutePath());
        try {
            response.setContentType("application/octet-stream");
            if (StrUtil.isNotBlank(request.getFilename())) {
                response.setHeader("Content-Disposition", "attachment; filename=\"" + request.getFilename() + "\"");
            }
            Files.copy(p, response.getOutputStream());
        } catch (IOException e) {
            log.warn("写出文件失败:" + e.getMessage());
            throw new ApplicationException("下载文件失败");
        }
        if (request.getRemoveTmp() && file.exists()) {
            file.delete();
        }
    }
}
