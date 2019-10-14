package com.behappy.colorplg;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ColorListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_color_list);

        initViews();
    }

    private void initViews() {
        List<List<String>> colorsList = new ArrayList<>();


        for (int i = 0; i < colorArr.length; i++) {
            List<String> colors = Arrays.asList(colorArr[i]);
            colorsList.add(colors);
        }
        RecyclerView recyclerView = findViewById(R.id.colorContainer);
        recyclerView.setAdapter(new ColorAdapter(colorsList, this));
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
    }

    private String[][] colorArr = {
            {"#E6E2AF", "#A7A37E", "#EFECCA", "#046380", "#002F2F"},
            {"#468966", "#FFF0A5", "#FFB03B", "#B64926", "#8E2800"},
            {"#FF6138", "#FFFF9D", "#BEEB9F", "#79BD8F", "#00A388"},
            {"#405952", "#9C9B7A", "#FFD393", "#FF974F", "#F54F29"}
    };
}
