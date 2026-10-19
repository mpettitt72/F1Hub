package com.example.f1hub.data.model.jolpica;

import com.google.gson.annotations.SerializedName;

public class MRData {
    @SerializedName("RaceTable")      public RaceTable raceTable;
    @SerializedName("StandingsTable") public StandingsTable standingsTable;
    @SerializedName("DriverTable")    public DriverTable driverTable;
}
