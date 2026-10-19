package com.example.f1hub.data.api;

import com.example.f1hub.data.model.openf1.OpenF1Driver;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface OpenF1Api {

    @GET("drivers")
    Call<List<OpenF1Driver>> getDriverByNumber(
            @Query("driver_number") int driverNumber,
            @Query("session_key") String sessionKey);

    @GET("drivers")
    Call<List<OpenF1Driver>> getAllDrivers(@Query("session_key") String sessionKey);
}
