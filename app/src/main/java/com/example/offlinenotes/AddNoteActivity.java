package com.example.offlinenotes;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AddNoteActivity extends AppCompatActivity {

    private ImageButton btnBack;

    private TextView tvSave;

    private TextView tvScreenTitle;

    private TextView tvCharacterCount;

    private EditText etTitle;

    private EditText etContent;

    private SharedPreferences preferences;

    private boolean isEditing = false;

    private int noteIndex = -1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(
                R.layout.activity_add_note
        );


        // ==========================================
        // UI
        // ==========================================

        btnBack =
                findViewById(R.id.btnBack);

        tvSave =
                findViewById(R.id.tvSave);

        tvScreenTitle =
                findViewById(R.id.tvScreenTitle);

        tvCharacterCount =
                findViewById(R.id.tvCharacterCount);

        etTitle =
                findViewById(R.id.etTitle);

        etContent =
                findViewById(R.id.etContent);


        // ==========================================
        // LOCAL STORAGE
        // ==========================================

        preferences =
                getSharedPreferences(
                        "NotesData",
                        MODE_PRIVATE
                );


        // ==========================================
        // CHECK EDIT MODE
        // ==========================================

        if (getIntent().hasExtra(
                "noteIndex"
        )) {

            isEditing = true;


            noteIndex =
                    getIntent().getIntExtra(
                            "noteIndex",
                            -1
                    );


            String title =
                    getIntent().getStringExtra(
                            "noteTitle"
                    );


            String content =
                    getIntent().getStringExtra(
                            "noteContent"
                    );


            etTitle.setText(title);

            etContent.setText(content);


            tvScreenTitle.setText(
                    "Edit Note"
            );


        } else {

            tvScreenTitle.setText(
                    "New Note"
            );
        }


        // ==========================================
        // CHARACTER COUNT
        // ==========================================

        updateCharacterCount();


        etContent.addTextChangedListener(
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

                        updateCharacterCount();
                    }


                    @Override
                    public void afterTextChanged(
                            Editable s
                    ) {
                    }
                }
        );


        // ==========================================
        // BACK
        // ==========================================

        btnBack.setOnClickListener(v ->
                finish()
        );


        // ==========================================
        // SAVE
        // ==========================================

        tvSave.setOnClickListener(v ->
                saveNote()
        );
    }


    // ==========================================
    // CHARACTER COUNT
    // ==========================================

    private void updateCharacterCount() {

        int count =
                etContent.getText()
                        .length();


        tvCharacterCount.setText(
                count + " characters"
        );
    }


    // ==========================================
    // SAVE NOTE
    // ==========================================

    private void saveNote() {

        String title =
                etTitle.getText()
                        .toString()
                        .trim();


        String content =
                etContent.getText()
                        .toString()
                        .trim();


        // ======================================
        // VALIDATE TITLE
        // ======================================

        if (title.isEmpty()) {

            Toast.makeText(
                    this,
                    "Please enter a title",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }


        // ======================================
        // VALIDATE CONTENT
        // ======================================

        if (content.isEmpty()) {

            Toast.makeText(
                    this,
                    "Please write something",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }


        // ======================================
        // GET EXISTING NOTES
        // ======================================

        String savedNotes =
                preferences.getString(
                        "notes",
                        ""
                );


        String[] notes;


        if (savedNotes.isEmpty()) {

            notes =
                    new String[0];

        } else {

            notes =
                    savedNotes.split("##");
        }


        // ======================================
        // CREATE UPDATED NOTE
        // ======================================

        String updatedNote =
                title + "||" + content;


        // ======================================
        // EDIT EXISTING NOTE
        // ======================================

        if (isEditing &&
                noteIndex >= 0 &&
                noteIndex < notes.length) {

            notes[noteIndex] =
                    updatedNote;

        }


        // ======================================
        // CREATE NEW NOTE
        // ======================================

        else {

            String[] newNotes =
                    new String[
                            notes.length + 1
                            ];


            System.arraycopy(
                    notes,
                    0,
                    newNotes,
                    0,
                    notes.length
            );


            newNotes[
                    notes.length
                    ] = updatedNote;


            notes =
                    newNotes;
        }


        // ======================================
        // BUILD FINAL DATA
        // ======================================

        StringBuilder finalNotes =
                new StringBuilder();


        for (String note : notes) {

            if (finalNotes.length() > 0) {

                finalNotes.append(
                        "##"
                );
            }


            finalNotes.append(
                    note
            );
        }


        // ======================================
        // SAVE LOCALLY
        // ======================================

        preferences.edit()
                .putString(
                        "notes",
                        finalNotes.toString()
                )
                .putBoolean(
                        "syncPending",
                        true
                )
                .apply();


        // ======================================
        // MESSAGE
        // ======================================

        Toast.makeText(
                this,
                isEditing
                        ? "Note updated"
                        : "Note saved",
                Toast.LENGTH_SHORT
        ).show();


        finish();
    }
}