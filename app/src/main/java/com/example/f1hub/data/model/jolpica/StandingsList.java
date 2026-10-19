package com.example.f1hub.data.model.jolpica;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class StandingsList {
    @SerializedName("season")               public String season;
    @SerializedName("round")                public String round;
    @SerializedName("DriverStandings")      public List<DriverStanding> driverStandings;
    @SerializedName("ConstructorStandings") public List<ConstructorStanding> constructorStandings;
}
