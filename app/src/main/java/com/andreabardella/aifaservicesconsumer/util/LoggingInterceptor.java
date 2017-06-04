package com.andreabardella.aifaservicesconsumer.util;

import java.io.IOException;
import java.nio.charset.Charset;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import timber.log.Timber;

public class LoggingInterceptor implements Interceptor {
    private static final Charset UTF8 = Charset.forName("UTF-8");

    String requestBodyStr;
    String responseBodyStr;

    @Override public Response intercept(Chain chain) throws IOException {
        requestBodyStr = null;
        responseBodyStr = null;

        // Get the request
        Request request = chain.request();

        // Perform logging work with the request
        RequestBody requestBody = request.body();
        if (requestBody != null) {
            Buffer buffer = new Buffer();
            requestBody.writeTo(buffer);
            MediaType contentType = requestBody.contentType();
            if (contentType != null) {
                contentType.charset(UTF8);
            }
            requestBodyStr = buffer.readString(UTF8);
        }

        long t1 = System.nanoTime();
        Timber.v("Sending request %s on %s%n%s body: %n%s",
                request.url(), chain.connection(), request.headers(),
                requestBodyStr);

        // NotaBene: call proceed against the chain and get the response
        Response response = chain.proceed(request);

        // Perform logging work with response
        ResponseBody responseBody = response.body();
        if (responseBody != null) {
            BufferedSource bufferedSource = responseBody.source();
            bufferedSource.request(Long.MAX_VALUE); // Buffer the entire body
            Buffer buffer = bufferedSource.buffer();
            MediaType contentType = responseBody.contentType();
            if (contentType != null) {
                contentType.charset(UTF8);
            }
            if (responseBody.contentLength() != 0) {
                responseBodyStr = buffer.clone().readString(UTF8);
            }
        }

        long t2 = System.nanoTime();
        Timber.v("Received response for %s (status code %d) in %.1fms%n%s body: %n%s",
                response.request().url(), response.code(), (t2 - t1) / 1e6d, response.headers(),
                responseBodyStr);

        return response;
    }
}