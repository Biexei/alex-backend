package org.alex.platform.mock;

import org.alex.platform.exception.BusinessException;
import org.mockserver.integration.ClientAndServer;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

public class MockServerPool {

    private static class SinglePool {
        private static final HashMap<String, ClientAndServer> INSTANCE = new HashMap<>(16);
    }

    public static HashMap<String, ClientAndServer> getInstance() {
        return SinglePool.INSTANCE;
    }

    /**
     * 启动MockServer
     * @param port 端口
     * @return ClientAndServer
     * @throws BusinessException 端口被占用
     */
    public static ClientAndServer start(Integer port) throws BusinessException {
        if (port == null) {
            throw new BusinessException("端口号不能为空");
        }
        HashMap<String, ClientAndServer> instance = MockServerPool.getInstance();
        ClientAndServer mockServerClient;
        try {
            mockServerClient = new ClientAndServer(port);
        } catch (Exception e) {
            throw new BusinessException("启动失败，请更换端口重试");
        }
        instance.put(port.toString(), mockServerClient);
        return mockServerClient;
    }

    /**
     * 启动MockServer
     * @param remoteHost 远程主机地址
     * @param remotePort 远程端口
     * @param port 端口
     * @return ClientAndServer
     * @throws BusinessException 端口被占用
     */
    public static ClientAndServer start(String remoteHost, Integer remotePort, Integer port) throws BusinessException {
        if (port == null) {
            throw new BusinessException("端口号不能为空");
        }
        if (remoteHost == null) {
            throw new BusinessException("转发地址不能为空");
        } else {
            if (!remoteHost.matches("[a-zA-z0-9.]+")) {
                throw new BusinessException("转发地址格式错误");
            }
        }
        if (remotePort == null) {
            throw new BusinessException("转发端口不能为空");
        }
        HashMap<String, ClientAndServer> instance = MockServerPool.getInstance();
        ClientAndServer mockServerClient;
        try {
            mockServerClient = new ClientAndServer(remoteHost, remotePort, port);
        } catch (Exception e) {
            throw new BusinessException("启动失败，请更换端口重试");
        }
        instance.put(port.toString(), mockServerClient);
        return mockServerClient;
    }

    /**
     * 判断端口是否已启用MockServer
     * @param port 端口
     * @return boolean
     */
    public static boolean isRunning(Integer port) {
        if (port == null) {
            throw new IllegalArgumentException("端口号不能为空");
        }
        HashMap<String, ClientAndServer> instance = MockServerPool.getInstance();
        ClientAndServer hit = instance.get(port.toString());
        if (hit == null) {
            return false;
        } else {
            return hit.getPort().equals(port);
        }
    }

    /**
     * 停止指定端口的MockServer
     * @param port 端口
     */
    public static void stop(Integer port) {
        if (port == null) {
            throw new IllegalArgumentException("端口号不能为空");
        }
        HashMap<String, ClientAndServer> instance = MockServerPool.getInstance();
        ClientAndServer hit = instance.get(port.toString());
        if (hit != null) {
            if (isRunning(port)) {
                hit.stop(true);
                instance.remove(port.toString());
            }
        }
    }

    /**
     * 清空所有MockSever
     */
    public static void clear() {
        HashMap<String, ClientAndServer> instance = MockServerPool.getInstance();
        for(Map.Entry<String, ClientAndServer> entry : instance.entrySet()) {
            ClientAndServer mockServer = entry.getValue();
            mockServer.stop(true);
        }
        instance.clear();
    }

    /**
     * 获取一个mock sever, 端口已经启动则直接返回；否则创建再返回
     * @param port 端口
     * @return ClientAndServer
     * @throws BusinessException 端口被占用
     */
    public static ClientAndServer get(Integer port) throws BusinessException {
        HashMap<String, ClientAndServer> instance = MockServerPool.getInstance();
        if (isRunning(port)) {
            ClientAndServer hit = instance.get(port.toString());
            InetSocketAddress remoteAddress = hit.getRemoteAddress();
            if (remoteAddress == null) {
                return hit;
            } else {
                hit.stop(true);
                return start(port);
            }
        } else {
            return start(port);
        }
    }

    /**
     * 获取一个mock sever, 端口已经启动则直接返回；否则创建再返回
     * @param remoteHost 远程主机地址
     * @param remotePort 远程端口
     * @param port 端口
     * @return ClientAndServer
     * @throws BusinessException 端口被占用
     */
    public static ClientAndServer get(String remoteHost, Integer remotePort, Integer port) throws BusinessException {
        HashMap<String, ClientAndServer> instance = MockServerPool.getInstance();
        if (isRunning(port)) {
            ClientAndServer hit = instance.get(port.toString());
            InetSocketAddress remoteAddress = hit.getRemoteAddress();
            if (remoteAddress != null) {
                return hit;
            } else {
                hit.stop(true);
                return start(remoteHost, remotePort, port);
            }
        } else {
            return start(remoteHost, remotePort, port);
        }
    }
}
