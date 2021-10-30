package com.example.homework5;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import com.example.homework5.databinding.ActivitySecondBinding;

public class SecondActivity extends AppCompatActivity {
    private ActivitySecondBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySecondBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.buttonFinish.setOnClickListener(v -> finish());

        Intent intent = getIntent();
        String name = intent.getStringExtra("EXTRA_NAME");
        binding.textName.setText(name);
        String message = intent.getStringExtra("EXTRA_MESSAGE");
        binding.textContent.setText(message);

        byte[] byteArray = getIntent().getByteArrayExtra("EXTRA_IMAGE");
        Bitmap bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        binding.image.setImageBitmap(bitmap);
    }
}