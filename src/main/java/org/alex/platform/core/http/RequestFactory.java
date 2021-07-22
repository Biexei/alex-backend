package org.alex.platform.core.http;

import org.alex.platform.mapper.HttpSettingMapper;
import org.alex.platform.pojo.HttpSettingDTO;
import org.alex.platform.pojo.HttpSettingVO;
import org.apache.http.HttpHost;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.security.KeyStore;
import java.util.List;

@Component
public class RequestFactory {
    @Autowired
    HttpSettingMapper httpSettingMapper;

    private static final int DEFAULT_CONNECT_TIMEOUT = 30 * 1000;
    private static final int DEFAULT_READ_TIMEOUT = 30 * 1000;

    /**
     * 支持http https 底层基于HttpsURLConnection
     * @return SimpleClientHttpRequestFactory
     */
    @Deprecated
    public SimpleClientHttpRequestFactory byURLConnection() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory() {
            @Override
            protected void prepareConnection(HttpURLConnection connection, String httpMethod) {
                try {
                    if (!(connection instanceof HttpsURLConnection)) {
                        super.prepareConnection(connection, httpMethod);
                    }
                    if (connection instanceof HttpsURLConnection) {
                        KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
                        // 信任任何链接
                        TrustStrategy anyTrustStrategy = (x509Certificates, s) -> true;
                        SSLContext ctx = SSLContexts.custom().useTLS().loadTrustMaterial(trustStore, anyTrustStrategy).build();
                        ((HttpsURLConnection) connection).setSSLSocketFactory(ctx.getSocketFactory());
                        HttpsURLConnection httpsConnection = (HttpsURLConnection) connection;
                        super.prepareConnection(httpsConnection, httpMethod);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        Proxy proxy = proxy4HttpURLConnection();
        if (proxy != null) {
            factory.setProxy(proxy);
        }
        factory.setReadTimeout(readTimeout());
        factory.setConnectTimeout(connectTimeout());
        return factory;
    }

    /**
     * 支持http https 底层基于HttpClient
     * @return HttpComponentsClientHttpRequestFactory
     */
    public HttpComponentsClientHttpRequestFactory byHttpClient() {
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
        SSLContext sslContext = null;
        try {
            sslContext = SSLContexts.custom().loadTrustMaterial(null, (x509Certificates, s) -> true).build();
        } catch (Exception e) {
            e.printStackTrace();
        }
        HttpClientBuilder builder = HttpClients.custom();
        builder.setProxy(proxy4HttpClient());
        builder.setSSLContext(sslContext).setSSLHostnameVerifier(new NoopHostnameVerifier());
        CloseableHttpClient closeableHttpClient = builder.build();
        factory.setHttpClient(closeableHttpClient);
        factory.setReadTimeout(readTimeout());
        factory.setConnectTimeout(connectTimeout());
        return factory;
    }

    private HttpHost proxy4HttpClient() {
        HttpSettingVO proxy = null;
        HttpSettingDTO httpSettingDTO = new HttpSettingDTO();
        httpSettingDTO.setStatus((byte)0);
        httpSettingDTO.setType((byte)0);
        List<HttpSettingVO> list = httpSettingMapper.selectHttpSetting(httpSettingDTO);
        if (!list.isEmpty()) {
            proxy = list.get(0);
        }
        if (proxy != null) {
            return HttpHost.create(proxy.getValue());
        }
        return null;
    }

    /**
     * 基于HttpsURLConnection的代理
     * @return Proxy
     */
    private Proxy proxy4HttpURLConnection() {
        HttpSettingVO proxy = null;
        HttpSettingDTO httpSettingDTO = new HttpSettingDTO();
        httpSettingDTO.setStatus((byte)0);
        httpSettingDTO.setType((byte)0);
        List<HttpSettingVO> list = httpSettingMapper.selectHttpSetting(httpSettingDTO);
        if (!list.isEmpty()) {
            proxy = list.get(0);
        }
        if (proxy != null) {
            String[] domain = proxy.getValue().split(":");
            String host = domain[0];
            int port = Integer.parseInt(domain[1]);
            return new Proxy(Proxy.Type.HTTP, new InetSocketAddress(host, port));
        }
        return null;
    }


    /**
     * 获取connectTimeout超时时常单位秒
     * @return 秒
     */
    private Integer connectTimeout() {
        Integer connectTime;
        HttpSettingDTO httpSettingDTO = new HttpSettingDTO();
        httpSettingDTO.setStatus((byte)0);
        httpSettingDTO.setType((byte)3);
        List<HttpSettingVO> list = httpSettingMapper.selectHttpSetting(httpSettingDTO);
        if (!list.isEmpty()) {
            String value = list.get(0).getValue();
            try {
                connectTime =  Integer.parseInt(value);
            } catch (NumberFormatException e) {
                connectTime = null;
            }
        } else {
            connectTime = null;
        }
        if (connectTime == null) {
            return DEFAULT_CONNECT_TIMEOUT;
        } else if (connectTime < 0) {
            return DEFAULT_CONNECT_TIMEOUT;
        } else {
            return connectTime;
        }
    }

    /**
     * 获取ReadTimeout超时时常单位秒
     * @return 秒
     */
    private Integer readTimeout() {
        Integer readTime;
        HttpSettingDTO httpSettingDTO = new HttpSettingDTO();
        httpSettingDTO.setStatus((byte)0);
        httpSettingDTO.setType((byte)4);
        List<HttpSettingVO> list = httpSettingMapper.selectHttpSetting(httpSettingDTO);
        if (!list.isEmpty()) {
            String value = list.get(0).getValue();
            try {
                readTime =  Integer.parseInt(value);
            } catch (NumberFormatException e) {
                readTime = null;
            }
        } else {
            readTime = null;
        }
        if (readTime == null) {
            return DEFAULT_READ_TIMEOUT;
        } else if (readTime < 0) {
            return DEFAULT_READ_TIMEOUT;
        } else {
            return readTime;
        }
    }
}
