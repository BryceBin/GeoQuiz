package com.example.bin.myapp;

import android.content.Intent;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private TextView mQuestionTextView;
    private TextView mCountTextView;
    private Button mTrueButton;
    private Button mFalseButton;
    private Button mPreButton;
    private Button mNextButton;
    private Button mCheatButton;
    private boolean mIsCheated;

    private static final String TAG = "MainActivity";
    private static final String KEY_INDEX = "mQuestionIndex";
    private static final String COUNT_INDEX = "mUserAnswerCount";
    private static final String USER_IS_CHEATED = "mIsCheated";
    private static final String CORRECT_COUNT_INDEX = "mUserAnswerCorrectCount";
    private static final int REQUEST_CODE_CHEAT = 0;//当有多个activity时，用来判断消息回馈方

    private static Question mQuestionsBank[] = new Question[]{
            new Question(R.string.question_africa,false,1),
            new Question(R.string.question_americas,true,2),
            new Question(R.string.question_asia,true,3),
            new Question(R.string.question_australia,true,4),
            new Question(R.string.question_mideast,false,5),
            new Question(R.string.question_oceans,true,6),
    };

    private int mQuestionIndex = 0;
    private int mUserAnswerCount = 0;
    private int mUserAnswerCorrectCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(savedInstanceState != null){
            mQuestionIndex = savedInstanceState.getInt(KEY_INDEX,0);
            mUserAnswerCount = savedInstanceState.getInt(COUNT_INDEX,0);
            mUserAnswerCorrectCount = savedInstanceState.getInt(CORRECT_COUNT_INDEX,0);
            mIsCheated = savedInstanceState.getBoolean(USER_IS_CHEATED,false);
        }

        mQuestionTextView = findViewById(R.id.question_textView);
        mQuestionTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toNextQuestion();
            }
        });

        mCountTextView = findViewById(R.id.count_textView);



        mTrueButton = findViewById(R.id.true_button);
        mTrueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mUserAnswerCount++;
                checkAnswer(true);
                setButtonClickable(false);
                mQuestionsBank[mQuestionIndex].setmHasUserAnswered(true);
                if(mUserAnswerCount==mQuestionsBank.length){
                    showScore();
                }
            }
        });

        mFalseButton = findViewById(R.id.false_button);
        mFalseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mUserAnswerCount++;
                checkAnswer(false);
                setButtonClickable(false);
                mQuestionsBank[mQuestionIndex].setmHasUserAnswered(true);
                if(mUserAnswerCount==mQuestionsBank.length){
                    showScore();
                }
            }
        });

        mPreButton = findViewById(R.id.pre_button);
        mPreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mQuestionIndex = (mQuestionIndex-1+mQuestionsBank.length)%mQuestionsBank.length;
                mIsCheated = mQuestionsBank[mQuestionIndex].ismWasCheated();
                checkButtonClickable();
                updateQuestion();
            }
        });

        mNextButton = findViewById(R.id.next_button);
        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toNextQuestion();
            }
        });

        mCheatButton = findViewById(R.id.cheat_button);
        mCheatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean answerIsTrue = mQuestionsBank[mQuestionIndex].ismIsQuestionTrue();
                Intent intent = CheatActivity.newIntent(MainActivity.this,answerIsTrue);
                Log.i(TAG, "onClick: go to cheatActivity");
                startActivityForResult(intent,REQUEST_CODE_CHEAT);
            }
        });

        updateQuestion();

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.i(TAG, "onSaveInstanceState: save data here before activity been crashed");
        outState.putInt(KEY_INDEX,mQuestionIndex);
        outState.putInt(COUNT_INDEX,mUserAnswerCount);
        outState.putInt(CORRECT_COUNT_INDEX,mUserAnswerCorrectCount);
        outState.putBoolean(USER_IS_CHEATED,mIsCheated);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {//处理从CheatActivity返回的数据
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode!=RESULT_OK){
            return;
        }
        if(requestCode == REQUEST_CODE_CHEAT){
            if(data==null){
                return;
            }
            mIsCheated = CheatActivity.getAnswerIsShown(data);
            mQuestionsBank[mQuestionIndex].setmWasCheated(mIsCheated);
        }
    }

    public void showScore(){
        mPreButton.setClickable(false);
        mNextButton.setClickable(false);
        Toast.makeText(MainActivity.this,String.format(Locale.CHINA,"your score is %.2f",(double)mUserAnswerCorrectCount/mQuestionsBank.length),Toast.LENGTH_LONG).show();
    }

    public void checkButtonClickable(){
        //若此问题曾被回答过，则禁用两回答按钮，否则可正常回答。
        if(mQuestionsBank[mQuestionIndex].ismHasUserAnswered()){
            setButtonClickable(false);
        }
        else{
            setButtonClickable(true);
        }
    }

    public void setButtonClickable(boolean isButtonClickable){
        mTrueButton.setEnabled(isButtonClickable);
        mFalseButton.setEnabled(isButtonClickable);
    }

    public void updateQuestion(){
        int questionId = mQuestionsBank[mQuestionIndex].getmQuestionResId();
        mQuestionTextView.setText(questionId);
        mCountTextView.setText(String.format(Locale.CHINA,"%d of %d",mQuestionsBank[mQuestionIndex].getmNum(),mQuestionsBank.length));
        checkButtonClickable();
    }

    public void checkAnswer(boolean userPressed){
        boolean isQuestionTrue = mQuestionsBank[mQuestionIndex].ismIsQuestionTrue();
        int msgId;
        if(mIsCheated){//若用户cheat了就显示相关警告信息
            msgId = R.string.judgment_toast;
        }
        else{
            if(isQuestionTrue==userPressed){
                msgId = R.string.correct_toast;
                mUserAnswerCorrectCount++;//记录用户回答正确总数
            }
            else{
                msgId = R.string.incorrect_toast;
            }
        }

        Toast.makeText(MainActivity.this,msgId,Toast.LENGTH_SHORT).show();
    }

    public void toNextQuestion(){
        mQuestionIndex = (mQuestionIndex+1)%mQuestionsBank.length;//更新当前问题索引值
        mIsCheated = mQuestionsBank[mQuestionIndex].ismWasCheated();//判断当前问题是否被偷看过答案
        checkButtonClickable();//根据问题是否被回答过设置按钮是否可用
        updateQuestion();
    }


}
