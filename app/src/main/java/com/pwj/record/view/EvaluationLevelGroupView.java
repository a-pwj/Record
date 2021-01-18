package com.pwj.record.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.pwj.record.R;

public class EvaluationLevelGroupView extends LinearLayout implements View.OnClickListener {
    public static final int INDEX_VERY_NICE = 0;
    public static final int INDEX_NORMAL = 1;
    public static final int INDEX_VERY_BAD = 2;

    private EvaluationLevelView mVeryNiceView;
    private EvaluationLevelView mNormalView;
    private EvaluationLevelView mVeryBadView;
    private int mIndex;

    private final Drawable chaozanSelDrawable = getResources().getDrawable(R.mipmap.pj_chaozhan_sel);
    private final Drawable chaozanDrawable = getResources().getDrawable(R.mipmap.pj_chaozhan);
    private final Drawable yibanSelDrawable = getResources().getDrawable(R.mipmap.pj_yiban_sel);
    private final Drawable yibanDrawable = getResources().getDrawable(R.mipmap.pj_yiban);
    private final Drawable bumanyiSelDrawable = getResources().getDrawable(R.mipmap.pj_bumanyi_sel);
    private final Drawable bumanyiDrawable = getResources().getDrawable(R.mipmap.pj_bumanyi);

    public EvaluationLevelGroupView(final Context context) {
        super(context);
        setupUi();
    }

    public EvaluationLevelGroupView(final Context context, final AttributeSet attrs) {
        super(context, attrs);
        setupUi();
    }

    public EvaluationLevelGroupView(final Context context, final AttributeSet attrs, final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setupUi();
    }

    private void setupUi() {
        final View view = LayoutInflater.from(getContext()).inflate(R.layout.widget_view_evaluation_level_group,
                this, true);
        mVeryNiceView = view.findViewById(R.id.veryNiceView);
        mVeryNiceView.setOnClickListener(this);

        mNormalView = view.findViewById(R.id.normalView);
        mNormalView.setOnClickListener(this);

        mVeryBadView = view.findViewById(R.id.veryBadView);
        mVeryBadView.setOnClickListener(this);
    }

    @Override
    public void onClick(final View v) {
        if (v == mVeryNiceView) {
            mVeryNiceView.setSelected(true);
            mVeryNiceView.setImageDrawable(chaozanSelDrawable);
            mNormalView.setSelected(false);
            mNormalView.setImageDrawable(yibanDrawable);
            mVeryBadView.setSelected(false);
            mVeryBadView.setImageDrawable(bumanyiDrawable);

            setIndex(INDEX_VERY_NICE);
        } else if (v == mNormalView) {
            mVeryNiceView.setSelected(false);
            mVeryNiceView.setImageDrawable(chaozanDrawable);
            mNormalView.setSelected(true);
            mNormalView.setImageDrawable(yibanSelDrawable);
            mVeryBadView.setSelected(false);
            mVeryBadView.setImageDrawable(bumanyiDrawable);

            setIndex(INDEX_NORMAL);
        } else if (v == mVeryBadView) {
            mVeryNiceView.setSelected(false);
            mVeryNiceView.setImageDrawable(chaozanDrawable);
            mNormalView.setSelected(false);
            mNormalView.setImageDrawable(yibanDrawable);
            mVeryBadView.setSelected(true);
            mVeryBadView.setImageDrawable(bumanyiSelDrawable);

            setIndex(INDEX_VERY_BAD);
        }
    }

    public int getIndex() {
        return mIndex;
    }

    private void setIndex(final int index) {
        mIndex = index;
    }
}
