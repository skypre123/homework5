package com.example.homework5;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.homework5.databinding.ActivityMainBinding;

import java.io.ByteArrayOutputStream;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.buttonSave.setOnClickListener(v -> startSecondActivity());
        binding.buttonPic.setOnClickListener(v -> getPhoto());

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
    }

    public void startSecondActivity() {
        Intent intent = new Intent(this, SecondActivity.class);
        String name = binding.editName.getText().toString();
        intent.putExtra("EXTRA_NAME", name);
        String message = binding.editContent.getText().toString();
        intent.putExtra("EXTRA_MESSAGE", message);

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        float scale = (float) (2014/(float)bitmap.getWidth());
        int image_w = (int) (bitmap.getWidth()*scale);
        int image_h = (int) (bitmap.getHeight()*scale);
        Bitmap resize = Bitmap.createScaledBitmap(bitmap, image_w, image_h, true);
        resize.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        intent.putExtra("EXTRA_IMAGE", byteArray);
        startActivity(intent);
    }

    private void getPhoto() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        resultLauncher.launch(intent);
    }

    private ActivityResultLauncher<String> requestPermissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
        if (isGranted) {
            Toast.makeText(this, "권한이 설정되었습니다", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "권한이 설정되지 않았습니다. 권한이 없으므로 앱을 종료합니다", Toast.LENGTH_SHORT).show();
            finish();
        }
    });
    ActivityResultLauncher<Intent> resultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        Intent data = result.getData();
        Log.i("test", "data");

        if (result.getResultCode() == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            bitmap = loadBitmap(selectedImage);
            binding.image.setImageBitmap(bitmap);
            binding.image.setVisibility(View.VISIBLE);
        }
    });
    private Bitmap loadBitmap(Uri uri) {
        String[] filePathColumn = {MediaStore.Images.Media.DATA};

        Cursor cursor = getContentResolver().query(uri, filePathColumn, null, null, null);
        cursor.moveToFirst();

        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        String picturePath = cursor.getString(columnIndex);
        cursor.close();

        return BitmapFactory.decodeFile(picturePath);
    }

}