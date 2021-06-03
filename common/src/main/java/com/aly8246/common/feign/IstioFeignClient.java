package com.aly8246.common.feign;

import feign.Client;
import feign.Request;
import feign.Response;
import org.springframework.util.StringUtils;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSocketFactory;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.Map;

public class IstioFeignClient extends Client.Default {

    private final String port;

    public IstioFeignClient(SSLSocketFactory sslContextFactory, HostnameVerifier hostnameVerifier, String port) {
        super(sslContextFactory, hostnameVerifier);
        this.port = port;
    }

    @Override
    public Response execute(Request request, Request.Options options) throws IOException {
        return super.execute(resetRequest(request), options);
    }

    public Request resetRequest(Request request){
        String scheme;
        //获取到url
        URI asUri = URI.create(request.url());
        String host = asUri.getHost();

        /**
         * 获取到server中的host 和 port
         * String host = server.getHost();
         * int port = server.getPort();
         * String scheme = server.getScheme();
         */
        scheme = asUri.getScheme();

        URI newURI;
        try {
            StringBuilder sb = new StringBuilder();
            sb.append(scheme).append("://");
            if (!StringUtils.isEmpty(asUri.getRawUserInfo())) {
                sb.append(asUri.getRawUserInfo()).append("@");
            }
            sb.append(host);
            if (!StringUtils.isEmpty(port)) {
                sb.append(":").append(port);
            }
            sb.append(asUri.getRawPath());
            if (!StringUtils.isEmpty(asUri.getRawQuery())) {
                sb.append("?").append(asUri.getRawQuery());
            }
            if (!StringUtils.isEmpty(asUri.getRawFragment())) {
                sb.append("#").append(asUri.getRawFragment());
            }
            newURI = new URI(sb.toString());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        Request.HttpMethod httpMethod = request.httpMethod();
        byte[] body = request.body();
        Charset charset = request.charset();
        Map<String, Collection<String>> headers = request.headers();

        return Request.create(httpMethod, newURI.toString(), headers, body, charset);
    }
}
