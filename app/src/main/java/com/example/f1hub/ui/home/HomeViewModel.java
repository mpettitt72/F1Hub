package com.example.f1hub.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.example.f1hub.data.model.jolpica.ConstructorStanding;
import com.example.f1hub.data.model.jolpica.DriverStanding;
import com.example.f1hub.data.model.jolpica.Race;
import com.example.f1hub.data.repository.F1Repository;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class HomeViewModel extends ViewModel {

    private final MutableLiveData<Race> nextRace = new MutableLiveData<>();
    private final MutableLiveData<DriverStanding> championshipLeader = new MutableLiveData<>();
    private final MutableLiveData<ConstructorStanding> topConstructor = new MutableLiveData<>();

    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public HomeViewModel() {
        load();
    }

    private void load() {
        F1Repository repo = F1Repository.getInstance();

        repo.getCurrentSchedule().observeForever(races -> {
            if (races == null) return;
            LocalDate today = LocalDate.now();
            Race next = null;
            for (Race r : races) {
                if (r.date == null) continue;
                try {
                    LocalDate rd = LocalDate.parse(r.date, DATE_FMT);
                    if (!rd.isBefore(today)) {
                        next = r;
                        break;
                    }
                } catch (Exception ignored) { }
            }
            if (next == null && !races.isEmpty()) next = races.get(races.size() - 1);
            nextRace.postValue(next);
        });

        repo.getDriverStandings().observeForever(standings -> {
            if (standings != null && !standings.isEmpty()) {
                championshipLeader.postValue(standings.get(0));
            }
        });

        repo.getConstructorStandings().observeForever(standings -> {
            if (standings != null && !standings.isEmpty()) {
                topConstructor.postValue(standings.get(0));
            }
        });
    }

    public LiveData<Race> getNextRace() { return nextRace; }
    public LiveData<DriverStanding> getChampionshipLeader() { return championshipLeader; }
    public LiveData<ConstructorStanding> getTopConstructor() { return topConstructor; }
}
