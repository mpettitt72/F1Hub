package com.example.f1hub.data.model.jolpica;

import com.google.gson.annotations.SerializedName;

public class Location {
    @SerializedName("lat")      public String lat;
    @SerializedName("long")     public String lng;
    @SerializedName("locality") public String locality;
    @SerializedName("country")  public String country;
}
