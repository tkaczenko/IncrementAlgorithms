package io.github.tkaczenko.incrementalgorithms;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.Toast;

import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.OnColorSelectedListener;
import com.flask.colorpicker.builder.ColorPickerClickListener;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;

import io.github.tkaczenko.incrementalgorithms.graphic.Point;
import io.github.tkaczenko.incrementalgorithms.views.DrawView;

public class DrawActivity extends AppCompatActivity implements RotateDialogFragment.OnDataSend {
    public DrawView drawView;
    private int mColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draw);

        drawView = (DrawView) findViewById(R.id.draw_view);
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

    private void onRotate() {
        RotateDialogFragment fragment = new RotateDialogFragment();
        fragment.show(getFragmentManager(), "rotate_dialog");

    }

    @Override
    public void someEvent(int centrerX, int centerY, double angle) {
        drawView.getmRotate().setRotationDegree(angle);
        drawView.getmRotate().setCenterPoint(new Point<Double>((double) centrerX, (double) centerY));
        drawView.rotate();
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
                        Toast.makeText(DrawActivity.this, "onColorSelected: 0x" + Integer.toHexString(selectedColor), Toast.LENGTH_SHORT);
                    }
                })
                .setPositiveButton("ok", new ColorPickerClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int selectedColor, Integer[] allColors) {
                        mColor = selectedColor;
                        drawView.setDrawColor(mColor);
                        drawView.invalidate();
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

    public int getmColor() {
        return mColor;
    }

    public void setmColor(int mColor) {
        this.mColor = mColor;
    }
}
