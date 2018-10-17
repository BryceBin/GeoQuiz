package com.example.bin.myapp;

public class Question {
    private int mQuestionResId;
    private int mNum;
    private boolean mIsQuestionTrue;
    private boolean mHasUserAnswered;
    private boolean mWasCheated;

    public Question(int questionId, boolean QuestionTrue,int num){
        mNum=num;
        mQuestionResId = questionId;
        mIsQuestionTrue = QuestionTrue;
        mHasUserAnswered = false;
        mWasCheated = false;
    }

    public boolean ismWasCheated() {
        return mWasCheated;
    }

    public void setmWasCheated(boolean mWasCheated) {
        this.mWasCheated = mWasCheated;
    }

    public int getmQuestionResId() {
        return mQuestionResId;
    }

    public void setmQuestionResId(int mQuestionResId) {
        this.mQuestionResId = mQuestionResId;
    }

    public boolean ismIsQuestionTrue() {
        return mIsQuestionTrue;
    }

    public void setmIsQuestionTrue(boolean mIsQuestionTrue) {
        this.mIsQuestionTrue = mIsQuestionTrue;
    }

    public boolean ismHasUserAnswered() {
        return mHasUserAnswered;
    }

    public void setmHasUserAnswered(boolean mHasUserAnswered) {
        this.mHasUserAnswered = mHasUserAnswered;
    }

    public int getmNum() {
        return mNum;
    }

    public void setmNum(int mNum) {
        this.mNum = mNum;
    }
}
