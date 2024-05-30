package com.example.mylap.api;

import android.content.Context;
import android.util.Log;
import com.example.mylap.services.ApiService;

import java.io.IOException;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.converter.gson.GsonConverterFactory;

public class ConfigApi {
    private ApiService apiService;
    private String authToken;
    private Context context;

    public ConfigApi() {
//        this.authToken = SharedPreferencesUtils.getStringToSharedPreferences(context, "token");
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(getInterceptor())
                .build();
        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl("https://learn-for-ever-server.onrender.com/")
                .baseUrl("http://10.144.14.10:3006/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
        this.apiService = retrofit.create(ApiService.class);
    }

    private Interceptor getInterceptor() {
        return new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request original = chain.request();
                Request.Builder requestBuilder = original.newBuilder()
                        .header("Authorization", "Bearer " + authToken); // Thêm token vào header
                Request request = requestBuilder.build();
                Response response = chain.proceed(request);

                if (response.code() == 401) {
                    Log.d("TAG", "error api:  401");
//                    SharedPreferencesUtils.removeStringToSharedPreferences(context, "userId");
//                    SharedPreferencesUtils.removeStringToSharedPreferences(context, "token");
//                    Intent intent = new Intent(context, LoginActivity.class);
//                    context.startActivity(intent);
                }

                return response;
            }
        };
    }

    public ApiService getApiService() {
        return apiService;
    }
}
