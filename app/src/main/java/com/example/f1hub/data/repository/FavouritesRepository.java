package com.example.f1hub.data.repository;

import android.content.Context;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.example.f1hub.data.db.FavouriteEntity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import android.util.Log;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

public class FavouritesRepository {

    private static FavouritesRepository instance;

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    private MutableLiveData<List<FavouriteEntity>> driverFavsData;
    private MutableLiveData<List<FavouriteEntity>> constructorFavsData;

    private FavouritesRepository() { }

    public static FavouritesRepository getInstance(Context context) {
        if (instance == null) instance = new FavouritesRepository();
        return instance;
    }

    public static void reset() {
        instance = null;
    }

    private CollectionReference userFavs() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) return null;
        return db.collection("users").document(user.getUid()).collection("favourites");
    }

    public LiveData<List<FavouriteEntity>> getDriverFavourites() {
        if (driverFavsData == null) {
            driverFavsData = new MutableLiveData<>(Collections.emptyList());
            attachListener("DRIVER", driverFavsData);
        }
        return driverFavsData;
    }

    public LiveData<List<FavouriteEntity>> getConstructorFavourites() {
        if (constructorFavsData == null) {
            constructorFavsData = new MutableLiveData<>(Collections.emptyList());
            attachListener("CONSTRUCTOR", constructorFavsData);
        }
        return constructorFavsData;
    }

    private void attachListener(String type, MutableLiveData<List<FavouriteEntity>> target) {
        CollectionReference ref = userFavs();
        if (ref == null) { target.postValue(Collections.emptyList()); return; }

        ref.whereEqualTo("type", type)
           .addSnapshotListener((snapshot, error) -> {
               if (error != null || snapshot == null) {
                   target.postValue(Collections.emptyList());
                   return;
               }
               List<FavouriteEntity> list = new ArrayList<>();
               for (com.google.firebase.firestore.DocumentSnapshot doc : snapshot.getDocuments()) {
                   FavouriteEntity entity = doc.toObject(FavouriteEntity.class);
                   if (entity != null) list.add(entity);
               }
               target.postValue(list);
           });
    }

    public void add(FavouriteEntity entity) {
        CollectionReference ref = userFavs();
        if (ref != null) ref.document(entity.id).set(entity)
                .addOnFailureListener(e -> Log.e("FavsRepo", "write failed", e));
    }

    public void remove(FavouriteEntity entity) {
        CollectionReference ref = userFavs();
        if (ref != null) ref.document(entity.id).delete();
    }

    public void isFavourite(String id, Consumer<Boolean> callback) {
        CollectionReference ref = userFavs();
        if (ref == null) { callback.accept(false); return; }
        ref.document(id).get()
           .addOnSuccessListener(doc -> callback.accept(doc.exists()))
           .addOnFailureListener(e -> callback.accept(false));
    }
}
