package com.pwj.record.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.pwj.record.R;


public final class EvaluationDialog extends Dialog implements View.OnClickListener {

    private EvaluationLevelGroupView mLevelGroup;
    private EditText mEvaluationText;
    private ImageView closeDialogImageView;
    private TextView mDialogButtonConfirm;
    private ClickListener mClickListenerConfirm;
    private ClickListener mClickListenerDecline;

    private TextView mVerySatisfied;
    private TextView mVeryCareful;

    public EvaluationDialog(final Context context) {
        this(context, R.style.DialogTheme);
    }

    public EvaluationDialog(final Context context, final int themeResId) {
        super(context, themeResId);
    }

    @Override
    protected final void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.dialog_evaluation);

        mLevelGroup = findViewById(R.id.levelGroup);
        mEvaluationText = findViewById(R.id.evaluationText);
        closeDialogImageView = findViewById(R.id.close_dialog);
        mDialogButtonConfirm = findViewById(R.id.dialogButtonConfirm);

        if (mClickListenerConfirm != null) {
            mDialogButtonConfirm.setOnClickListener(this);
        }

        if (mClickListenerDecline != null) {
            closeDialogImageView.setOnClickListener(this);
        }

        mVerySatisfied = findViewById(R.id.verySatisfied);
        mVerySatisfied.setOnClickListener(this);

        mVeryCareful = findViewById(R.id.veryCareful);
        mVeryCareful.setOnClickListener(this);
    }

    @Override
    public void onClick(final View v) {
        final int level = mLevelGroup.getIndex();
        final String message = mEvaluationText.getText().toString();
        if (v == mDialogButtonConfirm) {
            if (mClickListenerConfirm != null) {
                mClickListenerConfirm.onClick(this, level, message);
            }
        } else if (v == closeDialogImageView) {
            if (mClickListenerDecline != null) {
                mClickListenerDecline.onClick(this, level, message);
            }
        } else if (v == mVerySatisfied || v == mVeryCareful) {
            mEvaluationText.setText(((TextView) v).getText().toString());
        }
    }

    public void setPositiveListener(final ClickListener listener) {
        mClickListenerConfirm = listener;
    }

    public void setNegativeListener(final ClickListener listener) {
        mClickListenerDecline = listener;
    }

    public interface ClickListener {
        void onClick(EvaluationDialog dialog, int level, String message);
    }
}
