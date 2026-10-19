package com.example.f1hub.data.model.jolpica;

import com.google.gson.annotations.SerializedName;

public class Circuit {
    @SerializedName("circuitId")   public String circuitId;
    @SerializedName("circuitName") public String circuitName;
    @SerializedName("Location")    public Location location;
}
