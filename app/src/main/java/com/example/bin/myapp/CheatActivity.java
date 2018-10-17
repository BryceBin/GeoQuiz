package com.example.bin.myapp;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Locale;

public class CheatActivity extends AppCompatActivity {

    private boolean mIsAnswerTrue;
    private boolean mIsUserCheated;
    private Button mCheatButton;
    private TextView mShowAnswerTextView;
    private String s;

    private static final String EXTRA_ANSWER_IS_TRUE = "com.example.bin.myapp.cheatActivity.answer_is_true";
    private static final String EXTRA_ANSER_IS_SHOWN = "com.example.bin.myapp.cheatActivity.answer_shown";
    private static final String USER_IS_CHEATED = "mIsUserCheated";
    private static final String ANSWER_TEXT = "mShowAnswerTextView";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cheat);
        mShowAnswerTextView = findViewById(R.id.answer_text_view);

        if(savedInstanceState!=null){
            mIsUserCheated = savedInstanceState.getBoolean(USER_IS_CHEATED,false);
            if(mIsUserCheated){
                setAnswerShownResult(true);
            }
            s = savedInstanceState.getString(ANSWER_TEXT);
            mShowAnswerTextView.setText(String.format(Locale.CHINA,"the answer is %s",s));
        }

        mIsAnswerTrue = getIntent().getBooleanExtra(EXTRA_ANSWER_IS_TRUE,false);//获取当前题目的答案

        mCheatButton = findViewById(R.id.show_answer_button);
        mCheatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                s = mIsAnswerTrue?getResources().getString(R.string.true_button):getResources().getString(R.string.false_button);
                mShowAnswerTextView.setText(String.format(Locale.CHINA,"the answer is %s",s));
                setAnswerShownResult(true);
                mIsUserCheated = true;
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(USER_IS_CHEATED,mIsUserCheated);
        outState.putString(ANSWER_TEXT,s);

    }

    public static Intent newIntent(Context packageContext, boolean answerIsTrue){
        Intent intent = new Intent(packageContext,CheatActivity.class);
        intent.putExtra(EXTRA_ANSWER_IS_TRUE,answerIsTrue);
        return intent;
    }

    public void setAnswerShownResult(boolean isAnswerShown){//告诉MainActivity用户是否cheat了
        Intent intent = new Intent();
        intent.putExtra(EXTRA_ANSER_IS_SHOWN,isAnswerShown);
        setResult(RESULT_OK,intent);
    }

    public static boolean getAnswerIsShown(Intent result){
        return result.getBooleanExtra(EXTRA_ANSER_IS_SHOWN, false);
    }

}
