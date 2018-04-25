package io.hasura.scsecalendarapp;



import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by HARIHARAN on 27-06-2017.
 */

public abstract class CustomResponseListener<T> implements Callback<T> {

    public abstract void onSuccessfulResponse(T response);

    public abstract void onFailureResponse(String errorResponse);

    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        if(response.isSuccessful()){
            Log.i("response", response.toString());
            onSuccessfulResponse(response.body());
        }
        else{
            Log.i("responsePrint", response.toString());
            try {
                String errorMessage = response.errorBody().string();
                try{
                    ReqResponse errorResponse = new Gson().fromJson(errorMessage, ReqResponse.class);
                    onFailureResponse("error");
                }catch (JsonSyntaxException jsonSyntaxException){
                    jsonSyntaxException.printStackTrace();
                    onFailureResponse("error");
                }
            } catch (IOException e) {
                e.printStackTrace();
                onFailureResponse("error");
            }
        }
    }

    @Override
    public void onFailure(Call<T> call, Throwable t) {
        onFailureResponse("error");
    }
}