package io.github.tkaczenko.incrementalgorithms;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import io.github.tkaczenko.incrementalgorithms.math.ScreenConverter;
import io.github.tkaczenko.incrementalgorithms.views.DrawView;

public class DrawActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draw);
    }
}
