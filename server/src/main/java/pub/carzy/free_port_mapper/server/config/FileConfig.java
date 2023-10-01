package pub.carzy.free_port_mapper.server.config;

import cn.hutool.core.io.FileUtil;
import lombok.Data;
import pub.carzy.free_port_mapper.server.exces.ApplicationException;

import javax.annotation.PostConstruct;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * 文件路径配置
 *
 * @author admin
 */

@Data
public class FileConfig {
    private String root = "";
    private String tmp = "temp";
    private String prefix = "";
    private Map<String, String> others = new HashMap<>();

    @PostConstruct
    void init() {
        File file = new File(root).getAbsoluteFile();
        if (!file.exists()) {
            FileUtil.mkdir(file);
        } else if (file.isFile()) {
            throw new ApplicationException("启动失败:" + file.getAbsoluteFile().getAbsolutePath() + ",不是一个文件夹或文件夹路径!");
        }
        Map<String, String> files = new HashMap<>(others);
        files.put("tmp", tmp);
        for (String childPath : files.values()) {
            File childFile = new File(file, childPath);
            if (!childFile.exists()) {
                FileUtil.mkdir(childFile);
            } else if (childFile.isFile()) {
                throw new ApplicationException("启动失败:" + childFile.getAbsoluteFile().getAbsolutePath() + ",不是一个文件夹或文件夹路径!");
            }
        }

    }

    public File rootFile() {
        return new File(root).getAbsoluteFile();
    }

    public File tmpFile() {
        return new File(rootFile(), tmp).getAbsoluteFile();
    }

    public File otherFile(String name) {
        return others.containsKey(name) ? new File(rootFile(), others.get(name)).getAbsoluteFile() : null;
    }

    public boolean hasOtherByName(String name) {
        return others.containsKey(name);
    }
}
