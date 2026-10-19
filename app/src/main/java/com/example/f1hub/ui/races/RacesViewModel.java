package com.example.f1hub.ui.races;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.example.f1hub.data.model.jolpica.Race;
import com.example.f1hub.data.repository.F1Repository;
import java.util.List;

public class RacesViewModel extends ViewModel {

    private LiveData<List<Race>> races;
    private LiveData<List<Race>> raceResults;
    private final MutableLiveData<Race> selectedRace = new MutableLiveData<>();

    public LiveData<List<Race>> getRaces() {
        if (races == null) races = F1Repository.getInstance().getCurrentSchedule();
        return races;
    }

    public void selectRace(Race race) {
        selectedRace.setValue(race);
        if (race.season != null && race.round != null) {
            raceResults = F1Repository.getInstance().getRaceResults(race.season, race.round);
        }
    }

    public LiveData<Race> getSelectedRace() { return selectedRace; }

    public LiveData<List<Race>> getRaceResults() { return raceResults; }
}
