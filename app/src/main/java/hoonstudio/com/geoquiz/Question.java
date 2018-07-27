package hoonstudio.com.geoquiz;

/**
 * Created by joon on 4/5/2018.
 */

public class Question {

    private int mTextResId;
    private boolean mAnswerTrue;
    private boolean mIsCheater;

    public Question(int textResId, boolean answerTrue, boolean isCheater){
        mTextResId = textResId;
        mAnswerTrue = answerTrue;
        mIsCheater = isCheater;
    }

    public boolean isCheater() {
        return mIsCheater;
    }

    public void setCheater(boolean cheater) {
        mIsCheater = cheater;
    }

    public int getTextResId() {
        return mTextResId;
    }

    public void setTextResId(int textResId) {
        mTextResId = textResId;
    }

    public boolean isAnswerTrue() {
        return mAnswerTrue;
    }

    public void setAnswerTrue(boolean answerTrue) {
        mAnswerTrue = answerTrue;
    }
}
