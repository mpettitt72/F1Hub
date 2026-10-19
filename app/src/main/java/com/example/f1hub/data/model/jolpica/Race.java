package com.example.f1hub.data.model.jolpica;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class Race {
    @SerializedName("season")   public String season;
    @SerializedName("round")    public String round;
    @SerializedName("raceName") public String raceName;
    @SerializedName("Circuit")  public Circuit circuit;
    @SerializedName("date")     public String date;
    @SerializedName("time")     public String time;
    @SerializedName("Results")  public List<RaceResult> results;
}
