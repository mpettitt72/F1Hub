package com.example.f1hub.data.model.openf1;

import com.google.gson.annotations.SerializedName;

public class OpenF1Driver {
    @SerializedName("driver_number")  public int driverNumber;
    @SerializedName("full_name")      public String fullName;
    @SerializedName("name_acronym")   public String nameAcronym;
    @SerializedName("team_name")      public String teamName;
    @SerializedName("team_colour")    public String teamColour;
    @SerializedName("headshot_url")   public String headshotUrl;
    @SerializedName("country_code")   public String countryCode;
}
