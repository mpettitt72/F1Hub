package com.example.f1hub.ui.settings;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import com.example.f1hub.R;
import com.example.f1hub.SplashActivity;
import com.example.f1hub.data.repository.FavouritesRepository;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthRecentLoginRequiredException;
import com.google.firebase.auth.FirebaseUser;

public class SettingsFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        TextView tvEmail = view.findViewById(R.id.tv_email);
        if (user != null) tvEmail.setText(user.getEmail());

        view.findViewById(R.id.btn_sign_out).setOnClickListener(v -> signOut());
        view.findViewById(R.id.btn_delete_account).setOnClickListener(v -> confirmDelete());
    }

    private void signOut() {
        FavouritesRepository.reset();
        FirebaseAuth.getInstance().signOut();
        goToSplash();
    }

    private void confirmDelete() {
        new AlertDialog.Builder(requireContext())
                .setTitle("Delete Account")
                .setMessage("This will permanently delete your account. Are you sure?")
                .setPositiveButton("Delete", (d, w) -> deleteAccount())
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void deleteAccount() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) return;

        user.delete().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                FavouritesRepository.reset();
                goToSplash();
            } else if (task.getException() instanceof FirebaseAuthRecentLoginRequiredException) {
                promptReauth();
            } else {
                Toast.makeText(getContext(),
                        task.getException() != null ? task.getException().getMessage() : "Delete failed",
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    private void promptReauth() {
        View dialogView = LayoutInflater.from(requireContext())
                .inflate(R.layout.dialog_reauth, null);

        new AlertDialog.Builder(requireContext())
                .setTitle("Confirm your password")
                .setView(dialogView)
                .setPositiveButton("Confirm", (d, w) -> {
                    TextInputEditText et = dialogView.findViewById(R.id.et_reauth_password);
                    String password = et.getText() != null ? et.getText().toString() : "";
                    reauthAndDelete(password);
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void reauthAndDelete(String password) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null || user.getEmail() == null) return;

        AuthCredential credential = EmailAuthProvider.getCredential(user.getEmail(), password);
        user.reauthenticate(credential).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                deleteAccount();
            } else {
                Toast.makeText(getContext(), "Authentication failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void goToSplash() {
        Intent intent = new Intent(requireContext(), SplashActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
