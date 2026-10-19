package com.example.f1hub.data.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.example.f1hub.data.api.ApiClient;
import com.example.f1hub.data.model.jolpica.ConstructorStanding;
import com.example.f1hub.data.model.jolpica.Driver;
import com.example.f1hub.data.model.jolpica.DriverStanding;
import com.example.f1hub.data.model.jolpica.JolpicaResponse;
import com.example.f1hub.data.model.jolpica.Race;
import com.example.f1hub.data.model.jolpica.StandingsList;
import com.example.f1hub.data.model.openf1.OpenF1Driver;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class F1Repository {

    private static F1Repository instance;

    public static F1Repository getInstance() {
        if (instance == null) instance = new F1Repository();
        return instance;
    }

    public LiveData<List<Race>> getCurrentSchedule() {
        MutableLiveData<List<Race>> data = new MutableLiveData<>();
        ApiClient.jolpica().getCurrentSchedule().enqueue(new Callback<JolpicaResponse>() {
            @Override
            public void onResponse(Call<JolpicaResponse> call, Response<JolpicaResponse> response) {
                if (response.isSuccessful() && response.body() != null
                        && response.body().mrData.raceTable != null) {
                    data.postValue(response.body().mrData.raceTable.races);
                } else {
                    data.postValue(null);
                }
            }
            @Override
            public void onFailure(Call<JolpicaResponse> call, Throwable t) {
                data.postValue(null);
            }
        });
        return data;
    }

    public LiveData<List<DriverStanding>> getDriverStandings() {
        MutableLiveData<List<DriverStanding>> data = new MutableLiveData<>();
        ApiClient.jolpica().getDriverStandings().enqueue(new Callback<JolpicaResponse>() {
            @Override
            public void onResponse(Call<JolpicaResponse> call, Response<JolpicaResponse> response) {
                List<StandingsList> lists = safeStandingsLists(response);
                data.postValue(lists != null && !lists.isEmpty()
                        ? lists.get(0).driverStandings : null);
            }
            @Override
            public void onFailure(Call<JolpicaResponse> call, Throwable t) {
                data.postValue(null);
            }
        });
        return data;
    }

    public LiveData<List<ConstructorStanding>> getConstructorStandings() {
        MutableLiveData<List<ConstructorStanding>> data = new MutableLiveData<>();
        ApiClient.jolpica().getConstructorStandings().enqueue(new Callback<JolpicaResponse>() {
            @Override
            public void onResponse(Call<JolpicaResponse> call, Response<JolpicaResponse> response) {
                List<StandingsList> lists = safeStandingsLists(response);
                data.postValue(lists != null && !lists.isEmpty()
                        ? lists.get(0).constructorStandings : null);
            }
            @Override
            public void onFailure(Call<JolpicaResponse> call, Throwable t) {
                data.postValue(null);
            }
        });
        return data;
    }

    public LiveData<List<Driver>> getCurrentDrivers() {
        MutableLiveData<List<Driver>> data = new MutableLiveData<>();
        ApiClient.jolpica().getCurrentDrivers().enqueue(new Callback<JolpicaResponse>() {
            @Override
            public void onResponse(Call<JolpicaResponse> call, Response<JolpicaResponse> response) {
                if (response.isSuccessful() && response.body() != null
                        && response.body().mrData.driverTable != null) {
                    data.postValue(response.body().mrData.driverTable.drivers);
                } else {
                    data.postValue(null);
                }
            }
            @Override
            public void onFailure(Call<JolpicaResponse> call, Throwable t) {
                data.postValue(null);
            }
        });
        return data;
    }

    public LiveData<List<Race>> getRaceResults(String season, String round) {
        MutableLiveData<List<Race>> data = new MutableLiveData<>();
        ApiClient.jolpica().getRaceResults(season, round).enqueue(new Callback<JolpicaResponse>() {
            @Override
            public void onResponse(Call<JolpicaResponse> call, Response<JolpicaResponse> response) {
                if (response.isSuccessful() && response.body() != null
                        && response.body().mrData.raceTable != null) {
                    data.postValue(response.body().mrData.raceTable.races);
                } else {
                    data.postValue(null);
                }
            }
            @Override
            public void onFailure(Call<JolpicaResponse> call, Throwable t) {
                data.postValue(null);
            }
        });
        return data;
    }

    public LiveData<OpenF1Driver> getHeadshot(int driverNumber) {
        MutableLiveData<OpenF1Driver> data = new MutableLiveData<>();
        ApiClient.openF1().getDriverByNumber(driverNumber, "latest")
                .enqueue(new Callback<List<OpenF1Driver>>() {
                    @Override
                    public void onResponse(Call<List<OpenF1Driver>> call,
                                           Response<List<OpenF1Driver>> response) {
                        if (response.isSuccessful() && response.body() != null
                                && !response.body().isEmpty()) {
                            data.postValue(response.body().get(0));
                        } else {
                            data.postValue(null);
                        }
                    }
                    @Override
                    public void onFailure(Call<List<OpenF1Driver>> call, Throwable t) {
                        data.postValue(null);
                    }
                });
        return data;
    }

    public LiveData<Map<String, String>> getAllHeadshots() {
        MutableLiveData<Map<String, String>> data = new MutableLiveData<>();
        ApiClient.openF1().getAllDrivers("latest")
                .enqueue(new Callback<List<OpenF1Driver>>() {
                    @Override
                    public void onResponse(Call<List<OpenF1Driver>> call,
                                           Response<List<OpenF1Driver>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            Map<String, String> map = new HashMap<>();
                            for (OpenF1Driver d : response.body()) {
                                if (d.headshotUrl != null && d.driverNumber > 0) {
                                    map.put(String.valueOf(d.driverNumber), d.headshotUrl);
                                }
                            }
                            data.postValue(map);
                        } else {
                            data.postValue(null);
                        }
                    }
                    @Override
                    public void onFailure(Call<List<OpenF1Driver>> call, Throwable t) {
                        data.postValue(null);
                    }
                });
        return data;
    }

    private List<StandingsList> safeStandingsLists(Response<JolpicaResponse> response) {
        if (!response.isSuccessful() || response.body() == null) return null;
        if (response.body().mrData.standingsTable == null) return null;
        return response.body().mrData.standingsTable.standingsLists;
    }
}
