package com.example.f1hub.ui.drivers;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.example.f1hub.data.model.jolpica.Driver;
import com.example.f1hub.data.model.openf1.OpenF1Driver;
import com.example.f1hub.data.repository.F1Repository;
import java.util.Map;

public class DriversViewModel extends ViewModel {

    private LiveData<java.util.List<Driver>> drivers;
    private LiveData<Map<String, String>> headshotMap;
    private final MutableLiveData<Driver> selectedDriver = new MutableLiveData<>();
    private final MutableLiveData<OpenF1Driver> selectedHeadshot = new MutableLiveData<>();

    public LiveData<java.util.List<Driver>> getDrivers() {
        if (drivers == null) drivers = F1Repository.getInstance().getCurrentDrivers();
        return drivers;
    }

    public LiveData<Map<String, String>> getHeadshotMap() {
        if (headshotMap == null) headshotMap = F1Repository.getInstance().getAllHeadshots();
        return headshotMap;
    }

    public void selectDriver(Driver driver) {
        selectedDriver.setValue(driver);
        if (driver.permanentNumber != null) {
            try {
                int num = Integer.parseInt(driver.permanentNumber);
                F1Repository.getInstance().getHeadshot(num)
                        .observeForever(h -> selectedHeadshot.postValue(h));
            } catch (NumberFormatException ignored) { }
        }
    }

    public LiveData<Driver> getSelectedDriver() { return selectedDriver; }
    public LiveData<OpenF1Driver> getSelectedHeadshot() { return selectedHeadshot; }
}
