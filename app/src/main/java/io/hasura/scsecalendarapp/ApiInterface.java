package io.hasura.scsecalendarapp;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Created by HARIHARAN on 27-06-2017.
 */

public interface ApiInterface {

    @GET(NetworkURL.SLASH)
    Call<ReqResponse> insert();

}
