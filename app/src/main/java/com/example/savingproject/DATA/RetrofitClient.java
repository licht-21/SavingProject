package com.example.savingproject.DATA;



import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    // 10.0.2.2 maps directly to your laptop's local backend from inside the Android Emulator
    // keep note change the url to your ipv4 set up if using android usb porting
    private static final String BASE_URL = "http://127.0.0.1:5000/";
    public static RetrofitClient INSTANCE;
    private static Retrofit retrofit = null;

    public static ApiService getApiService() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit.create(ApiService.class);
    }
}