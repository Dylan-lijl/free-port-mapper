package pub.carzy.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author admin
 */
@Component
@Slf4j
public class ApplicationManager implements CommandLineRunner {

    private CountDownLatch countDownLatch = new CountDownLatch(1);
    /**
     * 是否正常退出
     */
    private AtomicBoolean normal = new AtomicBoolean(true);

    @Override
    public void run(String... args) throws Exception {
        Thread thread = new Thread(() -> {
            try {
                log.info("守护进程已启动...");
                countDownLatch.await();
                normal.set(true);
            } catch (InterruptedException e) {
                e.printStackTrace();
                normal.set(false);
            } finally {
                shutdown();
            }
        });
        thread.setName("Application Survival Manager");
        thread.start();
    }

    @PreDestroy
    void destroy() {
        countDownLatch.countDown();
    }

    void shutdown() {
        log.info("应用正在关闭中...");
    }
}
