package io.github.tkaczenko.incrementalgorithms.activities;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.OnColorSelectedListener;
import com.flask.colorpicker.builder.ColorPickerClickListener;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;

import io.github.tkaczenko.incrementalgorithms.R;
import io.github.tkaczenko.incrementalgorithms.fragments.RotateDialogFragment;
import io.github.tkaczenko.incrementalgorithms.graphic.Point;
import io.github.tkaczenko.incrementalgorithms.views.DrawView;

public class DrawActivity extends AppCompatActivity implements RotateDialogFragment.OnDataSend {
    private DrawView mDrawView;
    private int mColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draw);

        mDrawView = (DrawView) findViewById(R.id.draw_view);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.rotate:
                onRotate();
                break;
            case R.id.choose_color:
                onColorChange();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void send(int centerX, int centerY, double angle) {
        mDrawView.getRotate().setRotationDegree(angle);
        mDrawView.getRotate().setCenterPoint(new Point<>((double) centerX, (double) centerY));
        mDrawView.rotate();
    }

    private void onRotate() {
        RotateDialogFragment fragment = new RotateDialogFragment();
        fragment.show(getFragmentManager(), "rotate_dialog");
    }

    private void onColorChange() {
        ColorPickerDialogBuilder
                .with(DrawActivity.this)
                .setTitle("Choose color")
                .initialColor(Color.WHITE)
                .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
                .density(12)
                .setOnColorSelectedListener(new OnColorSelectedListener() {
                    @Override
                    public void onColorSelected(int selectedColor) {
                        Toast.makeText(DrawActivity.this, "onColorSelected: 0x"
                                + Integer.toHexString(selectedColor), Toast.LENGTH_SHORT).show();
                    }
                })
                .setPositiveButton("ok", new ColorPickerClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int selectedColor, Integer[] allColors) {
                        mColor = selectedColor;
                        mDrawView.setDrawColor(mColor);
                        mDrawView.invalidate();
                    }
                })
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .build()
                .show();
    }
}
