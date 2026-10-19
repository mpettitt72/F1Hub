package com.example.f1hub.ui.standings;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.example.f1hub.data.model.jolpica.ConstructorStanding;
import com.example.f1hub.data.model.jolpica.DriverStanding;
import com.example.f1hub.data.repository.F1Repository;
import java.util.List;

public class StandingsViewModel extends ViewModel {

    private LiveData<List<DriverStanding>> driverStandings;
    private LiveData<List<ConstructorStanding>> constructorStandings;
    private final MutableLiveData<ConstructorStanding> selectedConstructor = new MutableLiveData<>();

    public LiveData<List<DriverStanding>> getDriverStandings() {
        if (driverStandings == null)
            driverStandings = F1Repository.getInstance().getDriverStandings();
        return driverStandings;
    }

    public LiveData<List<ConstructorStanding>> getConstructorStandings() {
        if (constructorStandings == null)
            constructorStandings = F1Repository.getInstance().getConstructorStandings();
        return constructorStandings;
    }

    public void selectConstructor(ConstructorStanding cs) { selectedConstructor.setValue(cs); }
    public LiveData<ConstructorStanding> getSelectedConstructor() { return selectedConstructor; }
}
