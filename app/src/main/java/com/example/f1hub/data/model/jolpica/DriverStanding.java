package com.example.f1hub.data.model.jolpica;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class DriverStanding {
    @SerializedName("position")     public String position;
    @SerializedName("points")       public String points;
    @SerializedName("wins")         public String wins;
    @SerializedName("Driver")       public Driver driver;
    @SerializedName("Constructors") public List<Constructor> constructors;
}
