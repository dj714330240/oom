package com.coe.oom.conf;

import com.netflix.hystrix.exception.HystrixTimeoutException;
import org.apache.commons.lang.StringUtils;
import org.springframework.cloud.netflix.zuul.filters.route.FallbackProvider;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @program: oom
 * @description: 自定义Zuul回退机制处理器。
 * @author: 邓太阳
 * @create: 2019-05-10 09:23
 **/
@Component
public class ServiceBaseFallbackProvider implements FallbackProvider {


    @Override
    public String getRoute() {
        return "*";
    }

    @Override
    public ClientHttpResponse fallbackResponse(String route, final Throwable cause) {
        if (cause instanceof HystrixTimeoutException) {
            return response(HttpStatus.GATEWAY_TIMEOUT,null);
        }
        else if(cause instanceof org.apache.http.conn.HttpHostConnectException){
            // HttpStatus internalServerError = HttpStatus.INTERNAL_SERVER_ERROR;
            // internalServerError.getReasonPhrase();
            // SimpleClientHttpRequestFactory simpleClientHttpRequestFactory = new SimpleClientHttpRequestFactory();
            // new ClientHttpResponse
            // simpleClientHttpRequestFactory
            return response(HttpStatus.INTERNAL_SERVER_ERROR,"服务名:"+route+",连接超时,出现假死,服务不可用,"+cause.getMessage());
        }
        else if(cause instanceof com.netflix.client.ClientException){
            return response(HttpStatus.INTERNAL_SERVER_ERROR,"服务名:"+route+",连接失败,没启动,服务不可用,"+cause.getMessage());
        }
        else {
            return response(HttpStatus.INTERNAL_SERVER_ERROR,"服务名:"+route+",异常描述:"+cause.getMessage());
        }
    }

    private ClientHttpResponse response(final HttpStatus status,String msg) {
        return new ClientHttpResponse() {
            @Override
            public HttpStatus getStatusCode() throws IOException {
                return status;
            }

            @Override
            public int getRawStatusCode() throws IOException {
                return status.value();
            }

            @Override
            public String getStatusText() throws IOException {
                return StringUtils.isEmpty(msg) ?status.getReasonPhrase():msg;
            }

            @Override
            public void close() {
            }

            @Override
            public InputStream getBody() throws IOException {
                return new ByteArrayInputStream((StringUtils.isEmpty(msg)?status.getReasonPhrase():msg).getBytes());
            }

            @Override
            public HttpHeaders getHeaders() {
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                return headers;
            }
        };
    }

}
