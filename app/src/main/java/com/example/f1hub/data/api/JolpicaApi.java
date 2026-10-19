package com.example.f1hub.data.api;

import com.example.f1hub.data.model.jolpica.JolpicaResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface JolpicaApi {

    @GET("current.json")
    Call<JolpicaResponse> getCurrentSchedule();

    @GET("current/driverStandings.json")
    Call<JolpicaResponse> getDriverStandings();

    @GET("current/constructorStandings.json")
    Call<JolpicaResponse> getConstructorStandings();

    @GET("current/drivers.json")
    Call<JolpicaResponse> getCurrentDrivers();

    @GET("{season}/{round}/results.json")
    Call<JolpicaResponse> getRaceResults(@Path("season") String season, @Path("round") String round);
}
