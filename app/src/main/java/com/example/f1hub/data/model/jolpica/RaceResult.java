package com.example.f1hub.data.model.jolpica;

import com.google.gson.annotations.SerializedName;

public class RaceResult {
    @SerializedName("position")    public String position;
    @SerializedName("points")      public String points;
    @SerializedName("laps")        public String laps;
    @SerializedName("Driver")      public Driver driver;
    @SerializedName("Constructor") public Constructor constructor;
    @SerializedName("Time")        public RaceTime time;
    @SerializedName("status")      public String status;

    public static class RaceTime {
        @SerializedName("time") public String time;
    }
}
