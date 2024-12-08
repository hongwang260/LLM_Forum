package com.example.ginshinimpact_project2_cs310;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class PublicProfileActivity extends AppCompatActivity {

    private TextView textViewPublicUsername, textViewPublicGender, textViewPublicJobTitle, textViewPublicSelfIntro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_public_profile);

        textViewPublicUsername = findViewById(R.id.textViewPublicUsername);
        textViewPublicGender = findViewById(R.id.textViewPublicGender);
        textViewPublicJobTitle = findViewById(R.id.textViewPublicJobTitle);
        textViewPublicSelfIntro = findViewById(R.id.textViewPublicSelfIntro);

        // 从Intent中获取作者信息
        String authorUsername = getIntent().getStringExtra("authorUsername");
        String authorGender = getIntent().getStringExtra("authorGender");
        String authorJobTitle = getIntent().getStringExtra("authorJobTitle");
        String authorSelfIntro = getIntent().getStringExtra("authorSelfIntro");

        // 如果这些数据为空，就给默认值（在PostDetail传入时已做判断，这里可省略或作为双重保险）
        if (authorUsername == null) authorUsername = "Unknown Author";
        if (authorGender == null) authorGender = "N/A";
        if (authorJobTitle == null) authorJobTitle = "N/A";
        if (authorSelfIntro == null) authorSelfIntro = "N/A";

        // 将数据显示到对应的TextView中
        textViewPublicUsername.setText("Username: " + authorUsername);
        textViewPublicGender.setText("Gender: " + authorGender);
        textViewPublicJobTitle.setText("Job Title: " + authorJobTitle);
        textViewPublicSelfIntro.setText("Self Intro: " + authorSelfIntro);
    }
}