package com.example.savingproject.DATA;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    // Emulator: http://10.0.2.2:5000/
    // Physical device (Wi-Fi): http://YOUR_PC_IPV4:5000/
    // USB + adb reverse: http://127.0.0.1:5000/ via adb reverse tcp:5000 tcp:5000
    private static final String BASE_URL = "http://10.0.2.2:5000/";
    private static Retrofit retrofit = null;

    public static ApiService getApiService() {
        if (retrofit == null) {
            Interceptor authInterceptor = chain -> {
                Request.Builder builder = chain.request().newBuilder();
                SessionManager session = SessionManager.getInstance();
                if (session != null && session.getUserId() > 0) {
                    builder.header("X-User-Id", String.valueOf(session.getUserId()));
                }
                return chain.proceed(builder.build());
            };

            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(authInterceptor)
                    .build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit.create(ApiService.class);
    }
}
