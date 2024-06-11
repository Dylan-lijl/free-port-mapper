package pub.carzy.free_port_mapper.server.config;

import cn.hutool.core.io.FileUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import pub.carzy.free_port_mapper.server.util.JwtUtils;

import javax.annotation.Resource;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Set;

/**
 * @author admin
 */
@Component
@Slf4j
public class CommandRunner implements ApplicationRunner {

    @Resource
    private Set<String> blacklist;

    @Resource
    private SettingConfig settingConfig;

    @Resource
    private JwtUtils jwtUtils;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        //恢复密码文件
        File file = new File(".password").getAbsoluteFile();
        if (file.exists()) {
            String password = FileUtil.readString(file, StandardCharsets.UTF_8);
            if (password != null) {
                settingConfig.setPassword(password.trim());
                log.info("已加载密码文件...");
            }
        }
        //恢复黑名单数据
        file = new File(".blacklist").getAbsoluteFile();
        if (file.exists()) {
            List<String> list = FileUtil.readLines(file, StandardCharsets.UTF_8);
            //将还在有效期的token加入黑名单列表
            if (list != null && !list.isEmpty()) {
                for (String token : list) {
                    token = token.trim();
                    if (jwtUtils.verify(token)) {
                        blacklist.add(token);
                    }
                }
            }
            //没有黑名单就直接删除
            if (blacklist.isEmpty()) {
                file.delete();
                log.info("黑名单token数据为空,已删除该文件...");
            } else {
                // 然后将已处理好的重新写入文件当中
                FileUtil.writeLines(blacklist, file, StandardCharsets.UTF_8);
                log.info("已覆盖黑名单token数据...");
            }
        }
    }
}
