package com.example.offlinenotes;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private FloatingActionButton fabAddNote;
    private LinearLayout notesContainer;
    private View emptyState;

    private TextView tvConnectionStatus;
    private TextView tvSyncStatus;
    private TextView tvLastSynced;
    private TextView btnSync;

    private EditText etSearch;

    private SharedPreferences preferences;

    private ConnectivityManager connectivityManager;
    private ConnectivityManager.NetworkCallback networkCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        fabAddNote =
                findViewById(R.id.fabAddNote);

        notesContainer =
                findViewById(R.id.notesContainer);

        emptyState =
                findViewById(R.id.emptyState);

        tvConnectionStatus =
                findViewById(R.id.tvConnectionStatus);

        tvSyncStatus =
                findViewById(R.id.tvSyncStatus);

        tvLastSynced =
                findViewById(R.id.tvLastSynced);

        btnSync =
                findViewById(R.id.btnSync);

        etSearch =
                findViewById(R.id.etSearch);

        preferences =
                getSharedPreferences(
                        "NotesData",
                        MODE_PRIVATE
                );

        fabAddNote.setOnClickListener(v -> {

            Intent intent =
                    new Intent(
                            MainActivity.this,
                            AddNoteActivity.class
                    );

            startActivity(intent);
        });

        btnSync.setOnClickListener(v ->
                performSync()
        );

        etSearch.addTextChangedListener(
                new TextWatcher() {

                    @Override
                    public void beforeTextChanged(
                            CharSequence s,
                            int start,
                            int count,
                            int after
                    ) {
                    }

                    @Override
                    public void onTextChanged(
                            CharSequence s,
                            int start,
                            int before,
                            int count
                    ) {

                        loadNotes(
                                s.toString()
                        );
                    }

                    @Override
                    public void afterTextChanged(
                            Editable s
                    ) {
                    }
                }
        );

        updateConnectionStatus();

        updateSyncStatus();

        updateLastSynced();

        loadNotes("");

        startNetworkMonitoring();
    }

    @Override
    protected void onResume() {

        super.onResume();

        String searchText =
                etSearch.getText().toString();

        loadNotes(searchText);

        updateConnectionStatus();

        updateSyncStatus();

        updateLastSynced();
    }

    private void loadNotes(
            String searchText
    ) {

        notesContainer.removeAllViews();

        String savedNotes =
                preferences.getString(
                        "notes",
                        ""
                );

        if (savedNotes.isEmpty()) {

            emptyState.setVisibility(
                    View.VISIBLE
            );

            ((TextView) emptyState).setText(
                    "No notes yet.\nTap + to create your first note."
            );

            return;
        }

        String[] notes =
                savedNotes.split("##");

        boolean foundNotes = false;

        String search =
                searchText
                        .trim()
                        .toLowerCase();

        for (int i = 0;
             i < notes.length;
             i++) {

            String[] parts =
                    notes[i].split(
                            "\\|\\|",
                            2
                    );

            if (parts.length < 2) {
                continue;
            }

            String title =
                    parts[0];

            String content =
                    parts[1];

            String searchableText =
                    (title + " " + content)
                            .toLowerCase();

            if (!search.isEmpty() &&
                    !searchableText.contains(
                            search
                    )) {

                continue;
            }

            foundNotes = true;

            addNoteCard(
                    title,
                    content,
                    i
            );
        }

        if (foundNotes) {

            emptyState.setVisibility(
                    View.GONE
            );

        } else {

            emptyState.setVisibility(
                    View.VISIBLE
            );

            ((TextView) emptyState).setText(
                    "No matching notes found."
            );
        }
    }

    private void addNoteCard(
            String title,
            String content,
            int noteIndex
    ) {

        LinearLayout card =
                new LinearLayout(this);

        card.setOrientation(
                LinearLayout.VERTICAL
        );

        card.setBackgroundResource(
                R.drawable.note_card_background
        );

        LinearLayout.LayoutParams cardParams =
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );

        cardParams.setMargins(
                0,
                0,
                0,
                14
        );

        card.setLayoutParams(cardParams);

        TextView titleView =
                new TextView(this);

        titleView.setText(title);

        titleView.setTextSize(18);

        titleView.setTextColor(
                Color.rgb(
                        23,
                        32,
                        51
                )
        );

        titleView.setTypeface(
                null,
                Typeface.BOLD
        );

        TextView contentView =
                new TextView(this);

        contentView.setText(
                getContentPreview(content)
        );

        contentView.setTextSize(14);

        contentView.setTextColor(
                Color.rgb(
                        115,
                        123,
                        140
                )
        );

        contentView.setPadding(
                0,
                8,
                0,
                14
        );

        LinearLayout actionRow =
                new LinearLayout(this);

        actionRow.setOrientation(
                LinearLayout.HORIZONTAL
        );

        actionRow.setGravity(
                Gravity.CENTER_VERTICAL
        );

        TextView editButton =
                createActionButton(
                        "Edit",
                        Color.rgb(
                                49,
                                94,
                                251
                        ),
                        Color.rgb(
                                238,
                                242,
                                255
                        )
                );

        LinearLayout.LayoutParams editParams =
                new LinearLayout.LayoutParams(
                        0,
                        46
                );

        editParams.weight = 1;

        editParams.setMargins(
                0,
                0,
                6,
                0
        );

        editButton.setLayoutParams(
                editParams
        );

        editButton.setOnClickListener(v ->
                openEditNote(
                        title,
                        content,
                        noteIndex
                )
        );

        TextView deleteButton =
                createActionButton(
                        "Delete",
                        Color.rgb(
                                214,
                                69,
                                69
                        ),
                        Color.rgb(
                                255,
                                241,
                                241
                        )
                );

        LinearLayout.LayoutParams deleteParams =
                new LinearLayout.LayoutParams(
                        0,
                        46
                );

        deleteParams.weight = 1;

        deleteParams.setMargins(
                6,
                0,
                0,
                0
        );

        deleteButton.setLayoutParams(
                deleteParams
        );

        deleteButton.setOnClickListener(v ->
                showDeleteConfirmation(
                        title,
                        noteIndex
                )
        );

        actionRow.addView(
                editButton
        );

        actionRow.addView(
                deleteButton
        );

        card.setOnClickListener(v ->
                openEditNote(
                        title,
                        content,
                        noteIndex
                )
        );

        card.addView(titleView);

        card.addView(contentView);

        card.addView(actionRow);

        notesContainer.addView(card);
    }

    private TextView createActionButton(
            String text,
            int textColor,
            int backgroundColor
    ) {

        TextView button =
                new TextView(this);

        button.setText(text);

        button.setTextSize(14);

        button.setTextColor(
                textColor
        );

        button.setTypeface(
                null,
                Typeface.BOLD
        );

        button.setGravity(
                Gravity.CENTER
        );

        button.setPadding(
                12,
                0,
                12,
                0
        );

        GradientDrawable background =
                new GradientDrawable();

        background.setColor(
                backgroundColor
        );

        background.setCornerRadius(
                12
        );

        button.setBackground(
                background
        );

        button.setClickable(true);

        button.setFocusable(true);

        return button;
    }

    private String getContentPreview(
            String content
    ) {

        if (content.length() <= 120) {

            return content;
        }

        return content.substring(
                0,
                120
        ) + "...";
    }

    private void openEditNote(
            String title,
            String content,
            int noteIndex
    ) {

        Intent intent =
                new Intent(
                        MainActivity.this,
                        AddNoteActivity.class
                );

        intent.putExtra(
                "noteIndex",
                noteIndex
        );

        intent.putExtra(
                "noteTitle",
                title
        );

        intent.putExtra(
                "noteContent",
                content
        );

        startActivity(intent);
    }

    private void showDeleteConfirmation(
            String title,
            int noteIndex
    ) {

        AlertDialog dialog =
                new AlertDialog.Builder(this)
                        .setTitle("Delete Note?")
                        .setMessage(
                                "Are you sure you want to delete \""
                                        + title
                                        + "\"?"
                        )
                        .setNegativeButton(
                                "Cancel",
                                null
                        )
                        .setPositiveButton(
                                "Delete",
                                (dialogInterface, which) ->
                                        deleteNote(
                                                noteIndex
                                        )
                        )
                        .create();

        dialog.show();
    }

    private void deleteNote(
            int noteIndex
    ) {

        String savedNotes =
                preferences.getString(
                        "notes",
                        ""
                );

        if (savedNotes.isEmpty()) {
            return;
        }

        String[] notes =
                savedNotes.split("##");

        if (noteIndex < 0 ||
                noteIndex >= notes.length) {

            return;
        }

        StringBuilder updatedNotes =
                new StringBuilder();

        for (int i = 0;
             i < notes.length;
             i++) {

            if (i == noteIndex) {
                continue;
            }

            if (updatedNotes.length() > 0) {

                updatedNotes.append(
                        "##"
                );
            }

            updatedNotes.append(
                    notes[i]
            );
        }

        preferences.edit()
                .putString(
                        "notes",
                        updatedNotes.toString()
                )
                .putBoolean(
                        "syncPending",
                        true
                )
                .apply();

        String searchText =
                etSearch.getText().toString();

        loadNotes(searchText);

        updateSyncStatus();

        Toast.makeText(
                this,
                "Note deleted",
                Toast.LENGTH_SHORT
        ).show();
    }

    private boolean isInternetAvailable() {

        ConnectivityManager cm =
                (ConnectivityManager)
                        getSystemService(
                                CONNECTIVITY_SERVICE
                        );

        if (cm == null) {
            return false;
        }

        Network network =
                cm.getActiveNetwork();

        if (network == null) {
            return false;
        }

        NetworkCapabilities capabilities =
                cm.getNetworkCapabilities(
                        network
                );

        return capabilities != null &&
                capabilities.hasCapability(
                        NetworkCapabilities
                                .NET_CAPABILITY_INTERNET
                );
    }

    private void updateConnectionStatus() {

        if (isInternetAvailable()) {

            tvConnectionStatus.setText(
                    "● Online"
            );

            tvConnectionStatus.setTextColor(
                    Color.rgb(
                            76,
                            175,
                            80
                    )
            );

        } else {

            tvConnectionStatus.setText(
                    "● Offline"
            );

            tvConnectionStatus.setTextColor(
                    Color.rgb(
                            214,
                            69,
                            69
                    )
            );
        }
    }

    private void updateSyncStatus() {

        boolean pending =
                preferences.getBoolean(
                        "syncPending",
                        false
                );

        boolean online =
                isInternetAvailable();

        if (!online && pending) {

            tvSyncStatus.setText(
                    "● Offline — Waiting for connection"
            );

        } else if (pending) {

            tvSyncStatus.setText(
                    "● Pending Sync"
            );

        } else {

            tvSyncStatus.setText(
                    "● Synced"
            );
        }
    }

    private void updateLastSynced() {

        String lastSynced =
                preferences.getString(
                        "lastSynced",
                        ""
                );

        if (lastSynced.isEmpty()) {

            tvLastSynced.setText(
                    "Last synced: Never"
            );

        } else {

            tvLastSynced.setText(
                    "Last synced: " + lastSynced
            );
        }
    }

    private void performSync() {

        if (!isInternetAvailable()) {

            Toast.makeText(
                    this,
                    "No internet connection",
                    Toast.LENGTH_SHORT
            ).show();

            updateSyncStatus();

            return;
        }

        boolean pending =
                preferences.getBoolean(
                        "syncPending",
                        false
                );

        if (!pending) {

            Toast.makeText(
                    this,
                    "Everything is already synced",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        String currentTime =
                new SimpleDateFormat(
                        "dd MMM yyyy, hh:mm a",
                        Locale.getDefault()
                ).format(
                        new Date()
                );

        preferences.edit()
                .putBoolean(
                        "syncPending",
                        false
                )
                .putString(
                        "lastSynced",
                        currentTime
                )
                .apply();

        tvSyncStatus.setText(
                "● Synced"
        );

        updateLastSynced();

        Toast.makeText(
                this,
                "Notes synchronized successfully",
                Toast.LENGTH_SHORT
        ).show();
    }

    private void startNetworkMonitoring() {

        connectivityManager =
                (ConnectivityManager)
                        getSystemService(
                                CONNECTIVITY_SERVICE
                        );

        if (connectivityManager == null) {
            return;
        }

        networkCallback =
                new ConnectivityManager.NetworkCallback() {

                    @Override
                    public void onAvailable(
                            Network network
                    ) {

                        runOnUiThread(() -> {

                            tvConnectionStatus.setText(
                                    "● Online"
                            );

                            tvConnectionStatus.setTextColor(
                                    Color.rgb(
                                            76,
                                            175,
                                            80
                                    )
                            );

                            updateSyncStatus();
                        });
                    }

                    @Override
                    public void onLost(
                            Network network
                    ) {

                        runOnUiThread(() -> {

                            tvConnectionStatus.setText(
                                    "● Offline"
                            );

                            tvConnectionStatus.setTextColor(
                                    Color.rgb(
                                            214,
                                            69,
                                            69
                                    )
                            );

                            updateSyncStatus();
                        });
                    }
                };

        connectivityManager
                .registerDefaultNetworkCallback(
                        networkCallback
                );
    }

    private void stopNetworkMonitoring() {

        if (connectivityManager != null &&
                networkCallback != null) {

            connectivityManager
                    .unregisterNetworkCallback(
                            networkCallback
                    );

            networkCallback = null;
        }
    }

    @Override
    protected void onDestroy() {

        stopNetworkMonitoring();

        super.onDestroy();
    }
}