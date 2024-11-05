package com.example.ginshinimpact_project2_cs310;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class PostDetail extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.post_detail);

        // Back button setup
        Button backButton = findViewById(R.id.buttonBack);
        backButton.setOnClickListener(v -> finish()); // Close this activity to go back

        // Get the data passed from HomePage
        String title = getIntent().getStringExtra("title");
        String llmKind = "Model: " + getIntent().getStringExtra("llmKind");
        String content = "Content: " + getIntent().getStringExtra("content");
        String authorNotes = "Author Notes: " + getIntent().getStringExtra("authorNotes");

        // Set the data to the TextViews
        TextView titleTextView = findViewById(R.id.textViewTitle);
        TextView llmKindTextView = findViewById(R.id.textViewLLMKind);
        TextView contentTextView = findViewById(R.id.textViewContent);
        TextView authorNotesTextView = findViewById(R.id.textViewAuthorNotes);

        titleTextView.setText(title);
        llmKindTextView.setText(llmKind);
        contentTextView.setText(content);
        authorNotesTextView.setText(authorNotes);
    }
}
