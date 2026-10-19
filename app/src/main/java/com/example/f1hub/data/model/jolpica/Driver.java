package com.example.f1hub.data.model.jolpica;

import com.google.gson.annotations.SerializedName;

public class Driver {
    @SerializedName("driverId")       public String driverId;
    @SerializedName("permanentNumber") public String permanentNumber;
    @SerializedName("code")           public String code;
    @SerializedName("givenName")      public String givenName;
    @SerializedName("familyName")     public String familyName;
    @SerializedName("dateOfBirth")    public String dateOfBirth;
    @SerializedName("nationality")    public String nationality;
}
