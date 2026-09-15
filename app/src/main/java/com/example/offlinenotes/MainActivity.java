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

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {

    private FloatingActionButton fabAddNote;

    private LinearLayout notesContainer;

    private View emptyState;

    private TextView tvConnectionStatus;
    private TextView tvSyncStatus;

    private TextView btnSync;

    private EditText etSearch;

    private SharedPreferences preferences;

    private ConnectivityManager connectivityManager;

    private ConnectivityManager.NetworkCallback networkCallback;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);


        // ==========================================
        // UI
        // ==========================================

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

        btnSync =
                findViewById(R.id.btnSync);

        etSearch =
                findViewById(R.id.etSearch);


        // ==========================================
        // LOCAL STORAGE
        // ==========================================

        preferences =
                getSharedPreferences(
                        "NotesData",
                        MODE_PRIVATE
                );


        // ==========================================
        // ADD NOTE
        // ==========================================

        fabAddNote.setOnClickListener(v -> {

            Intent intent =
                    new Intent(
                            MainActivity.this,
                            AddNoteActivity.class
                    );

            startActivity(intent);
        });


        // ==========================================
        // SYNC
        // ==========================================

        btnSync.setOnClickListener(v ->
                performSync()
        );


        // ==========================================
        // SEARCH
        // ==========================================

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


        // ==========================================
        // INITIAL STATUS
        // ==========================================

        updateConnectionStatus();

        updateSyncStatus();

        loadNotes("");


        // ==========================================
        // NETWORK MONITORING
        // ==========================================

        startNetworkMonitoring();
    }


    @Override
    protected void onResume() {

        super.onResume();

        String searchText =
                etSearch.getText()
                        .toString();

        loadNotes(searchText);

        updateConnectionStatus();

        updateSyncStatus();
    }


    // ==========================================
    // LOAD NOTES
    // ==========================================

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
                    !searchableText.contains(search)) {

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


    // ==========================================
    // NOTE CARD
    // ==========================================

    private void addNoteCard(
            String title,
            String content,
            int noteIndex
    ) {

        // ======================================
        // CARD
        // ======================================

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

        card.setLayoutParams(
                cardParams
        );


        // ======================================
        // TITLE
        // ======================================

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


        // ======================================
        // CONTENT
        // ======================================

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


        // ======================================
        // ACTION ROW
        // ======================================

        LinearLayout actionRow =
                new LinearLayout(this);

        actionRow.setOrientation(
                LinearLayout.HORIZONTAL
        );

        actionRow.setGravity(
                Gravity.CENTER_VERTICAL
        );


        // ======================================
        // EDIT BUTTON
        // ======================================

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


        // ======================================
        // DELETE BUTTON
        // ======================================

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
                deleteNote(noteIndex)
        );


        // ======================================
        // ADD BUTTONS
        // ======================================

        actionRow.addView(
                editButton
        );

        actionRow.addView(
                deleteButton
        );


        // ======================================
        // CARD CLICK
        // ======================================

        card.setOnClickListener(v -> {

            openEditNote(
                    title,
                    content,
                    noteIndex
            );
        });


        // ======================================
        // ADD TO CARD
        // ======================================

        card.addView(
                titleView
        );

        card.addView(
                contentView
        );

        card.addView(
                actionRow
        );


        notesContainer.addView(
                card
        );
    }


    // ==========================================
    // CREATE ACTION BUTTON
    // ==========================================

    private TextView createActionButton(
            String text,
            int textColor,
            int backgroundColor
    ) {

        TextView button =
                new TextView(this);


        // Text
        button.setText(text);

        button.setTextSize(14);

        button.setTextColor(
                textColor
        );

        button.setTypeface(
                null,
                Typeface.BOLD
        );


        // Alignment
        button.setGravity(
                Gravity.CENTER
        );


        // Padding
        button.setPadding(
                12,
                0,
                12,
                0
        );


        // Background
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


        // Make it clickable
        button.setClickable(true);

        button.setFocusable(true);


        return button;
    }


    // ==========================================
    // CONTENT PREVIEW
    // ==========================================

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


    // ==========================================
    // OPEN EDIT NOTE
    // ==========================================

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


    // ==========================================
    // DELETE NOTE
    // ==========================================

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
                etSearch.getText()
                        .toString();


        loadNotes(searchText);

        updateSyncStatus();


        Toast.makeText(
                this,
                "Note deleted",
                Toast.LENGTH_SHORT
        ).show();
    }


    // ==========================================
    // INTERNET CHECK
    // ==========================================

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


    // ==========================================
    // CONNECTION STATUS
    // ==========================================

    private void updateConnectionStatus() {

        if (isInternetAvailable()) {

            tvConnectionStatus.setText(
                    "● Online"
            );

        } else {

            tvConnectionStatus.setText(
                    "● Offline"
            );
        }
    }


    // ==========================================
    // SYNC STATUS
    // ==========================================

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


    // ==========================================
    // PERFORM SYNC
    // ==========================================

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


        // Simulated synchronization
        preferences.edit()
                .putBoolean(
                        "syncPending",
                        false
                )
                .apply();


        tvSyncStatus.setText(
                "● Synced"
        );


        Toast.makeText(
                this,
                "Notes synchronized successfully",
                Toast.LENGTH_SHORT
        ).show();
    }


    // ==========================================
    // NETWORK MONITORING
    // ==========================================

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

                            updateSyncStatus();
                        });
                    }
                };


        connectivityManager
                .registerDefaultNetworkCallback(
                        networkCallback
                );
    }


    // ==========================================
    // STOP NETWORK MONITORING
    // ==========================================

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