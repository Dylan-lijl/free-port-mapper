package pub.carzy.free_port_mapper.server;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import pub.carzy.free_port_mapper.server.modules.dto.request.ListNetworkRequest;
import pub.carzy.free_port_mapper.server.modules.dto.response.ListNetworkResponse;
import pub.carzy.free_port_mapper.server.modules.service.impl.SystemInfoServiceImpl;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.*;

// @SpringBootTest
class ServerApplicationTests {

    @Test
    void contextLoads() {
    }

    @Test
    void testGetNetwork() throws SocketException {
        Set<ListNetworkResponse> list = new LinkedHashSet<>();
        // 获取所有网络接口
        Enumeration<NetworkInterface> nifs = NetworkInterface.getNetworkInterfaces();
        while (nifs.hasMoreElements()) {
            // 获取每个网络接口
            NetworkInterface nif = nifs.nextElement();
            // 获取网卡名称
            String name = nif.getName();
            // 获取网卡显示名称
            String displayName = nif.getDisplayName();
            // 获取网卡MAC地址
            byte[] mac = nif.getHardwareAddress();
            // 转换为16进制字符串
            String macStr = "";
            if (mac != null) {
                for (int i = 0; i < mac.length; i++) {
                    macStr += String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : "");
                }
            }
            // 获取所有IP地址
            Enumeration<InetAddress> addrs = nif.getInetAddresses();
            while (addrs.hasMoreElements()) {
                InetAddress addr = addrs.nextElement();
                ListNetworkResponse response = new ListNetworkResponse();
                response.setName(name);
                response.setDescription(displayName);
                response.setMac(macStr);
                response.setHost(addr.getHostName());
                response.setIp(addr.getHostAddress());
                response.setLoopback(addr.isLoopbackAddress());
                list.add(response);
            }
        }
        System.out.println(list);
    }

}
