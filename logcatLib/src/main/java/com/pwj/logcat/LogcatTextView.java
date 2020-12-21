package com.pwj.logcat;


import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.Html;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * @Author: pwj
 * @Date: 2020/7/23 17:14
 * @FileName: LogcatTextView
 * @Description: description
 */
public class LogcatTextView extends ScrollView {

    private int verboseColor, debugColor, errorColor, infoColor, warningColor, consoleColor;
    private TextView textView;

    public LogcatTextView(Context context) {
        super(context);
        init(null);
    }

    public LogcatTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public LogcatTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public LogcatTextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }

    private void init(AttributeSet attrs) {

        textView = new TextView(getContext());
        textView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        textView.setPadding(20, 20, 20, 20);
        addView(textView);

        textView.setTextColor(getContext().getResources().getColor(R.color.defaultTextColor));

        if (attrs != null) {
            TypedArray a = getContext().getTheme()
                    .obtainStyledAttributes(attrs, R.styleable.LogcatTextView, 0, 0);

            try {
                verboseColor = a.getColor(R.styleable.LogcatTextView_verboseColor, getContext().getResources()
                        .getColor(R.color.defaultVerboseColor));
                debugColor = a.getColor(R.styleable.LogcatTextView_debugColor, getContext().getResources()
                        .getColor(R.color.defaultDebugColor));
                errorColor = a.getColor(R.styleable.LogcatTextView_errorColor, getContext().getResources()
                        .getColor(R.color.defaultErrorColor));
                infoColor = a.getColor(R.styleable.LogcatTextView_infoColor, getContext().getResources()
                        .getColor(R.color.defaultInfoColor));
                warningColor = a.getColor(R.styleable.LogcatTextView_warningColor, getContext().getResources()
                        .getColor(R.color.defaultWarningColor));

                consoleColor = a.getColor(R.styleable.LogcatTextView_consoleColor, getContext().getResources()
                        .getColor(R.color.defaultConsoleColor));
            } finally {
                a.recycle();
            }
        } else {
            verboseColor = getContext().getResources().getColor(R.color.defaultVerboseColor);
            debugColor = getContext().getResources().getColor(R.color.defaultDebugColor);
            errorColor = getContext().getResources().getColor(R.color.defaultErrorColor);
            infoColor = getContext().getResources().getColor(R.color.defaultInfoColor);
            warningColor = getContext().getResources().getColor(R.color.defaultWarningColor);
            consoleColor = getContext().getResources().getColor(R.color.defaultConsoleColor);
        }

        setBackgroundColor(consoleColor);
        textView.setBackgroundColor(consoleColor);

    }

    public void refreshLogcat() {
        getLogcat();
    }

    private Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            String text = (String) msg.obj;
            if (!TextUtils.isEmpty(text)) {
                textView.setText(Html.fromHtml(text));
                invalidate();
                fullScroll(ScrollView.FOCUS_DOWN);
            }
        }
    };

    private void getLogcat() {
        Thread  thread=new Thread() {
            @Override
            public void run() {

                try {
                    while (true){
                        String processId = Integer.toString(android.os.Process.myPid());
                        String[] command = new String[]{
                                "logcat",
                                "-d",
                                "-v",
                                "imtest",
                                "threadtime"
                        };
                        Process process = Runtime.getRuntime().exec(command);
                        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));

                        StringBuilder log = new StringBuilder();
                        String line;
                        while ((line = bufferedReader.readLine()) != null) {
                            if (line.contains(processId)) {
                                int lineColor = verboseColor;

                                if (line.contains(" I ")) {
                                    lineColor = infoColor;
                                } else if (line.contains(" E ")) {
                                    lineColor = errorColor;
                                } else if (line.contains(" D ")) {
                                    lineColor = debugColor;
                                } else if (line.contains(" W ")) {
                                    lineColor = warningColor;
                                }

                                log.append("<font color=\"#" + Integer.toHexString(lineColor)
                                        .toUpperCase()
                                        .substring(2) + "\">" + line + "</font><br><br>");
                            }

                        }
                        Message msg = mHandler.obtainMessage();
                        msg.obj = log.toString();
                        mHandler.sendMessage(msg);
                        sleep(500);
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        };
        thread.start();


    }
}