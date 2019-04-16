package com.me.component_base.net;


import android.support.annotation.NonNull;
import android.text.TextUtils;


import com.me.component_base.BaseApp;
import com.me.component_base.net.exception.NetForbiddenException;
import com.me.component_base.net.exception.NetNotFoundException;
import com.me.component_base.net.exception.NetworkException;
import com.me.component_base.net.exception.ServerException;
import com.me.component_base.utils.IOUtils;
import com.me.component_base.utils.NetUtils;
import com.me.component_base.utils.SharedPreferencesUtil;

import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.concurrent.TimeUnit;

import cn.droidlover.xdroidmvp.log.XLog;
import okhttp3.Connection;
import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.internal.http.HttpHeaders;
import okio.Buffer;

/**
 * @author yang
 */
public class NetInterceptor implements Interceptor {

    /**
     * HTTP错误码
     */
    private final int FORBIDDEN_CODE = 403;
    private final int NOTFOUND_CODE = 404;
    private final int SERVICE_CODE = 500;

    private boolean logHeaders = true;
    private boolean logBody = true;

    /**
     * 业务异常
     */
    private final int BIZ_CODE = 400;

    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Request oldRequest = chain.request();

        // 添加新的参数
        HttpUrl.Builder authorizedUrlBuilder = oldRequest.url().newBuilder().scheme(oldRequest.url().scheme())
                .host(oldRequest.url().host())
                .addQueryParameter("language",
                        String.valueOf(SharedPreferencesUtil.getValue("key_language", 0)));

        // 新的请求
        Request newRequest = oldRequest.newBuilder()
                .method(oldRequest.method(), oldRequest.body())
                .url(authorizedUrlBuilder.build())
                .build();

        int netWorkState = NetUtils.getNetWorkState(BaseApp.getContext());
        // 执行请求，计算请求时间
        long startNs = System.nanoTime();
        logForRequest(newRequest, chain.connection());
        Response response = chain.proceed(newRequest);
        String header = response.header("wili-access-token");
        String token = (String) SharedPreferencesUtil.getValue("token", "");
        if (!TextUtils.isEmpty(header) && !token.equals(header)) {
            SharedPreferencesUtil.putValue("token", header);
        }

        if (netWorkState == NetUtils.NETWORK_NONE) {
            throw new NetworkException();
        }

        //请求日志拦截
        long tookMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNs);

        switch (response.code()) {
            case FORBIDDEN_CODE:
                throw new NetForbiddenException();
            case NOTFOUND_CODE:
                throw new NetNotFoundException();
            case SERVICE_CODE:
                throw new ServerException();
            default:
                break;
        }

        //响应日志拦截
        return logForResponse(response, tookMs);
    }

    private Response logForResponse(Response response, long tookMs) {
        Response.Builder builder = response.newBuilder();
        Response clone = builder.build();
        ResponseBody responseBody = clone.body();

        try {
            log("Response<-- " + clone.code() + ' ' + clone.message() + ' ' + clone.request().url() + " (" + tookMs + "ms）");
            if (logHeaders) {
                Headers headers = clone.headers();
                for (int i = 0, count = headers.size(); i < count; i++) {
                    log("\t" + headers.name(i) + ": " + headers.value(i));
                }
                log(" ");
                if (logBody && HttpHeaders.hasBody(clone)) {
                    if (responseBody == null) {
                        return response;
                    }

                    if (isPlaintext(responseBody.contentType())) {
                        byte[] bytes = IOUtils.toByteArray(responseBody.byteStream());
                        MediaType contentType = responseBody.contentType();
                        String body = new String(bytes, getCharset(contentType));
                        log("\tbody:" + body);
                        JSONObject jsonObject = new JSONObject(body);
                        JSONObject status = jsonObject.getJSONObject("status");
                        if (status.getInt("errCode") != 200) {
                            jsonObject.remove("data");
                        }
                        String responseString = jsonObject.toString();
                        responseBody = ResponseBody.create(responseBody.contentType(), responseString);
                        return response.newBuilder().body(responseBody).build();
                    } else {
                        log("\tbody: maybe [binary body], omitted!");
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            log("<-- END HTTP");
        }
        return response;
    }

    private void logForRequest(Request request, Connection connection) {
        RequestBody requestBody = request.body();
        boolean hasRequestBody = requestBody != null;
        Protocol protocol = connection != null ? connection.protocol() : Protocol.HTTP_1_1;

        try {
            String requestStartMessage = "Request--> " + request.method() + ' ' + request.url() + ' ' + protocol;
            log(requestStartMessage);

            if (logHeaders) {
                if (hasRequestBody) {
                    if (requestBody.contentType() != null) {
                        log("\tContent-Type: " + requestBody.contentType());
                    }
                    if (requestBody.contentLength() != -1) {
                        log("\tContent-Length: " + requestBody.contentLength());
                    }
                }
                Headers headers = request.headers();
                for (int i = 0, count = headers.size(); i < count; i++) {
                    String name = headers.name(i);
                    if (!"Content-Type".equalsIgnoreCase(name) && !"Content-Length".equalsIgnoreCase(name)) {
                        log("\t" + name + ": " + headers.value(i));
                    }
                }

                log(" ");
                if (logBody && hasRequestBody) {
                    if (isPlaintext(requestBody.contentType())) {
                        bodyToString(request);
                    } else {
                        log("\tbody: maybe [binary body], omitted!");
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            log("--> END " + request.method());
        }
    }

    private void log(String message) {
        XLog.d("com.wili.hpink 网络请求", message);
    }

    private static boolean isPlaintext(MediaType mediaType) {
        if (mediaType == null) {
            return false;
        }
        if (mediaType.type() != null && "text".equals(mediaType.type())) {
            return true;
        }
        String subtype = mediaType.subtype();
        if (subtype != null) {
            subtype = subtype.toLowerCase();
            return subtype.contains("x-www-form-urlencoded") || subtype.contains("json") || subtype.contains("xml") || subtype.contains("html");
        }
        return false;
    }

    private void bodyToString(Request request) {
        try {
            Request copy = request.newBuilder().build();
            RequestBody body = copy.body();
            if (body == null) {
                return;
            }
            Buffer buffer = new Buffer();
            body.writeTo(buffer);
            Charset charset = getCharset(body.contentType());
            log("\tbody:" + buffer.readString(charset));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static final Charset UTF8 = Charset.forName("UTF-8");

    private static Charset getCharset(MediaType contentType) {
        Charset charset = contentType != null ? contentType.charset(UTF8) : UTF8;
        if (charset == null) {
            charset = UTF8;
        }
        return charset;
    }
}
