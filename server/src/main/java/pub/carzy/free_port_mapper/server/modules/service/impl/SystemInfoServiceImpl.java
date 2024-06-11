package pub.carzy.free_port_mapper.server.modules.service.impl;

import org.springframework.stereotype.Service;
import pub.carzy.free_port_mapper.server.modules.dto.request.ListNetworkRequest;
import pub.carzy.free_port_mapper.server.modules.dto.response.ListNetworkResponse;
import pub.carzy.free_port_mapper.server.modules.service.SystemInfoService;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.*;

/**
 * @author admin
 */
@Service
public class SystemInfoServiceImpl implements SystemInfoService {
    public static List<ListNetworkResponse> list = null;

    static {
        list = new ArrayList<>(getListNetworkResponses());
    }

    @Override
    public List<ListNetworkResponse> all(ListNetworkRequest request) {
        if (request.getCache()) {
            if (list != null) {
                return list;
            }
        }
        synchronized (this) {
            if (list != null) {
                Set<ListNetworkResponse> all = getListNetworkResponses();
                if (all.size() > 0) {
                    list = new ArrayList<>(all);
                }
            }
        }
        return list == null ? null : new ArrayList<>(list);
    }

    public static Set<ListNetworkResponse> getListNetworkResponses() {
        Set<ListNetworkResponse> all = new LinkedHashSet<>();
        // 获取所有网络接口
        Enumeration<NetworkInterface> nifs = null;
        try {
            nifs = NetworkInterface.getNetworkInterfaces();
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
                    all.add(response);
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return all;
    }
}
