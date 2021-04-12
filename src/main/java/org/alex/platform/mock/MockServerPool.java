package org.alex.platform.mock;

import org.alex.platform.exception.BusinessException;
import org.mockserver.integration.ClientAndServer;

import java.util.Vector;

public class MockServerPool {

    private static class SinglePool {
        private static final Vector<ClientAndServer> INSTANCE = new Vector<>();
    }

    public static Vector<ClientAndServer> getInstance() {
        return SinglePool.INSTANCE;
    }

    /**
     * 启动MockServer
     * @param port 端口
     * @return ClientAndServer
     * @throws BusinessException 端口被占用
     */
    public static ClientAndServer start(Integer port) throws BusinessException {
        Vector<ClientAndServer> instance = MockServerPool.getInstance();
        ClientAndServer mockServerClient;
        try {
            mockServerClient = new ClientAndServer(port);
        } catch (Exception e) {
            throw new BusinessException("Mock Server启动失败，请更换端口重试");
        }
        instance.add(mockServerClient);
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
        Vector<ClientAndServer> instance = MockServerPool.getInstance();
        ClientAndServer mockServerClient;
        try {
            mockServerClient = new ClientAndServer(remoteHost, remotePort, port);
        } catch (Exception e) {
            throw new BusinessException("Mock Server启动失败，请更换端口重试");
        }
        instance.add(mockServerClient);
        return mockServerClient;
    }

    /**
     * 判断端口是否已启用MockServer
     * @param port 端口
     * @return boolean
     */
    public static boolean isRunning(Integer port) {
        Vector<ClientAndServer> instance = MockServerPool.getInstance();
        for(ClientAndServer server : instance) {
            if (server != null) {
                if (server.isRunning() && server.getPort().equals(port)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 停止指定端口的MockServer
     * @param port 端口
     */
    public static void stop(Integer port) {
        Vector<ClientAndServer> instance = MockServerPool.getInstance();
        for(ClientAndServer server : instance) {
            if (server != null) {
                if (server.isRunning() && server.getPort().equals(port)) {
                    server.stop();
                    instance.remove(server);
                }
            }
        }
    }

    /**
     * 清空所有MockSever
     */
    public static void clear() {
        Vector<ClientAndServer> instance = MockServerPool.getInstance();
        for (ClientAndServer server : instance) {
            if (server != null && server.isRunning()) {
                server.close();
            }
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
        Vector<ClientAndServer> instance = MockServerPool.getInstance();
        if (isRunning(port)) {
            for (ClientAndServer server : instance) {
                if (server != null) {
                    if (server.isRunning() && server.getPort().equals(port)) {
                        return server;
                    }
                } else {
                    return start(port);
                }
            }
        }
        return start(port);
    }
}
