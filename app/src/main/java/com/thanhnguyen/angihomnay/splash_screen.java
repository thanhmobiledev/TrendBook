package com.thanhnguyen.angihomnay;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.thanhnguyen.angihomnay.Func.homekhachhang;

public class splash_screen extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = new Intent(this, homekhachhang.class);
        startActivity(intent);
        finish();
    }
}
