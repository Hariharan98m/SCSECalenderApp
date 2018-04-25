package io.hasura.scsecalendarapp;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by HARIHARAN on 27-06-2017.
 */

public class AuthApiManager {

    private ApiInterface apiInterface;
    private Context context;

    public AuthApiManager(Context context) {
        this.context = context;
    }

    private void createApiInterface(){
        HttpLoggingInterceptor interceptor= new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        OkHttpClient client=new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .connectTimeout(1, TimeUnit.MINUTES)
                .readTimeout(1, TimeUnit.MINUTES)
                .writeTimeout(1, TimeUnit.MINUTES)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(NetworkURL.BASE)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client)
                .build();

        apiInterface = retrofit.create(ApiInterface.class);
    }

    public ApiInterface getApiInterface(){

        if(apiInterface==null){
            createApiInterface();
        }
        return apiInterface;
    }
}
