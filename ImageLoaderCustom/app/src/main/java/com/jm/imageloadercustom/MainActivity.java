package com.jm.imageloadercustom;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.jm.imageloadercustom.test.GridViewActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void goToGridViewActivity(View view) {
        Intent intent = new Intent(this, GridViewActivity.class);
        startActivity(intent);
    }
}
