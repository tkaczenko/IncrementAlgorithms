package io.github.tkaczenko.incrementalgorithms;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.Locale;

/**
 * Created by tkaczenko on 10.10.16.
 */

public class RotateDialogFragment extends DialogFragment
        implements SeekBar.OnSeekBarChangeListener, View.OnClickListener {
    private double mRotation = 0.0;
    private int mRotationCX = 0;
    private int mRotationCY = 0;

    public interface OnDataSend {
        public void someEvent(int centerX, int centerY, double angle);
    }

    OnDataSend send;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        return dialog;
    }

    @Override
    public void onStart() {
        super.onStart();

        Dialog dialog = getDialog();
        if (dialog != null) {
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            send = (OnDataSend) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement onSomeListenerInterface");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().setTitle("Rotate");
        View v = inflater.inflate(R.layout.rotate_dialog, null);
        updateViews(v);

        SeekBar sbRotation = ((SeekBar) v.findViewById(R.id.sbRotation));
        if (sbRotation != null) {
            sbRotation.setOnSeekBarChangeListener(this);
        }
        Button btnOk = (Button) v.findViewById(R.id.btnOk);
        if (btnOk != null) {
            btnOk.setOnClickListener(this);
        }
        return v;
    }


    public void updateViews(View v) {
        EditText etRCX = ((EditText) v.findViewById(R.id.etRCX));
        EditText etRCY = ((EditText) v.findViewById(R.id.etRCY));
        SeekBar sbRotation = ((SeekBar) v.findViewById(R.id.sbRotation));
        TextView tvRotation = ((TextView) v.findViewById(R.id.tvRotation));
        if (etRCX != null) {
            etRCX.setText(String.valueOf(mRotationCX));
        }
        if (etRCY != null) {
            etRCY.setText(String.valueOf(mRotationCY));
        }
        if (sbRotation != null) {
            sbRotation.setProgress((int) (mRotation * 10));
        }
        if (tvRotation != null) {
            tvRotation.setText(String.format(Locale.getDefault(), "%.1f", mRotation));
        }
    }

    private void updateState() {
        EditText etRCX = ((EditText) getView().findViewById(R.id.etRCX));
        EditText etRCY = ((EditText) getView().findViewById(R.id.etRCY));
        SeekBar sbRotation = ((SeekBar) getView().findViewById(R.id.sbRotation));
        if (etRCX != null) {
            mRotationCX = Integer.parseInt(etRCX.getText().toString());
        }
        if (etRCY != null) {
            mRotationCY = Integer.parseInt(etRCY.getText().toString());
        }
        if (sbRotation != null) {
            mRotation = sbRotation.getProgress() / 10.0;
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
        mRotation = seekBar.getProgress() / 10;
        TextView tvRotation = (TextView) getView().findViewById(R.id.tvRotation);
        if (tvRotation != null) {
            tvRotation.setText(String.format(Locale.getDefault(), "%.1f", mRotation));
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
    }

    @Override
    public void onClick(View view) {
        updateState();
        if (view.getId() == R.id.btnOk) {
            send.someEvent(mRotationCX, mRotationCY, mRotation);
        }
        dismiss();
    }
}
