package com.pwj.record.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.pwj.record.R;


public class EvaluationLevelView extends ConstraintLayout {
    private static final int TRANSITION_DURATION = 200;

    private Drawable mImageDrawable;
    private CharSequence mLabelText;
    private boolean mSelected;
    private ImageView mImageView;
//    private ImageView mStatusView;

    public EvaluationLevelView(final Context context) {
        super(context);
        this.init(null);
        setupUi();
    }

    public EvaluationLevelView(final Context context,final AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
        setupUi();
    }

    public EvaluationLevelView(final Context context,final AttributeSet attrs, final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
        setupUi();
    }

    private void init( final AttributeSet attrs) {
        if (attrs != null) {
            final TypedArray a = getContext().getTheme().obtainStyledAttributes(attrs,
                    R.styleable.EvaluationLevelView, 0, 0);

            try {
                mImageDrawable = a.getDrawable(R.styleable.EvaluationLevelView_sj_imageDrawable);
                mLabelText = a.getString(R.styleable.EvaluationLevelView_sj_label_text);
                mSelected = a.getBoolean(R.styleable.EvaluationLevelView_sj_selected, false);
            } finally {
                a.recycle();
            }
        }
    }

    private void setupUi() {
        final View view = LayoutInflater.from(getContext()).inflate(R.layout.widget_view_evaluation_level,
                this, true);
        mImageView = view.findViewById(R.id.imageView);
        mImageView.setBackground(mImageDrawable);

        final TextView label = view.findViewById(R.id.label);
        label.setText(mLabelText);

//        mStatusView = view.findViewById(R.id.statusView);
//        mStatusView.setBackgroundResource(mSelected ? R.mipmap.selected : R.mipmap.deselected);
    }

    public void setImageDrawable(final Drawable drawable) {
        mImageDrawable = drawable;
        if (mImageView != null) {
            mImageView.setBackground(mImageDrawable);
        }
    }

    public void setImageResouce(final int resouce){

    }

    public boolean isSelected() {
        return mSelected;
    }

    public void setSelected(final boolean selected) {
        if (mSelected != selected) {
            mSelected = selected;
//            if (mStatusView != null) {
//                final TransitionDrawable drawable = getTransitionDrawable(selected);
//                mStatusView.setBackground(drawable);
//                drawable.startTransition(TRANSITION_DURATION);
//            }
        }
    }

    private TransitionDrawable getTransitionDrawable(final boolean selected) {
        final Drawable[] drawables;

        final Drawable selectedDrawable = getResources().getDrawable(R.mipmap.selected);
        final Drawable deselectedDrawable = getResources().getDrawable(R.mipmap.deselected);

        if (selected) {
            drawables = new Drawable[]{deselectedDrawable, selectedDrawable};
        } else {
            drawables = new Drawable[]{selectedDrawable, deselectedDrawable};
        }

        return new TransitionDrawable(drawables);
    }
}
