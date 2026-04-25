package com.example.ranklifts_java.api;

import android.content.Context;
import android.content.SharedPreferences;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class AuthInterceptor implements Interceptor {
    private static String authToken = null;

    public static void setAuthToken(String token) {
        authToken = token;
    }

    public static void clearAuthToken() {
        authToken = null;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request original = chain.request();
        Request.Builder requestBuilder = original.newBuilder();

        // Add Authorization header if token exists
        if (authToken != null && !authToken.isEmpty()) {
            requestBuilder.header("Authorization", "Bearer " + authToken);
            System.out.println("AuthInterceptor: Added Authorization header");
        }

        requestBuilder.header("Content-Type", "application/json");
        Request request = requestBuilder.build();

        return chain.proceed(request);
    }
}