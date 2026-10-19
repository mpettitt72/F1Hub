package com.example.f1hub.data.model.jolpica;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class RaceTable {
    @SerializedName("season") public String season;
    @SerializedName("Races")  public List<Race> races;
}
