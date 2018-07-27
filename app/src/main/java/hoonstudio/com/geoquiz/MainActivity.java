package hoonstudio.com.geoquiz;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private static final String KEY_INDEX = "index";
    private static final int REQUEST_CODE_CHEAT_ACTIVITY = 0;
    private static final String USER_IS_CHEATER = "com.bignerdranch.android.geoquiz.user_is_cheater";

    //Buttons
    private Button mTrueButton;
    private Button mFalseButton;
    private Button mNextButton;
    private Button mPrevButton;
    private Button mCheatButton;

    //TextViews
    private TextView mQuestionTextView;
    private TextView mCorrectOrFalseTextView;
    private TextView mCurrentIndexTextView;

    //Questions
    //In more complex projects, this array would be created and stored elsewhere.
    private Question[] mQuestionBank = new Question[]{
            new Question(R.string.question_africa, true, false),
            new Question(R.string.question_americas, true, false),
            new Question(R.string.question_asia, false, false),
            new Question(R.string.question_australia, false, false),
            new Question(R.string.question_mideast, true, false),
            new Question(R.string.question_oceans, true, false)
    };

    private int mCurrentIndex = 0;

    private boolean mIsCheater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: called");
        setContentView(R.layout.activity_main);

        if(savedInstanceState != null){
            mCurrentIndex = savedInstanceState.getInt(KEY_INDEX, 0);
            mIsCheater = savedInstanceState.getBoolean(USER_IS_CHEATER, false);
        }


        mTrueButton = (Button) findViewById(R.id.trueButton);
        mFalseButton = (Button) findViewById(R.id.falseButton);
        mNextButton = (Button) findViewById(R.id.nextButton);
        mPrevButton = (Button) findViewById(R.id.prevButton);
        mCheatButton = (Button) findViewById(R.id.cheatButton);

        mQuestionTextView = (TextView) findViewById(R.id.questionTextView);
        mCurrentIndexTextView = (TextView) findViewById(R.id.currentIndexTextView);
        mCorrectOrFalseTextView = (TextView) findViewById(R.id.correctOrFalseTextView);

        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mIsCheater){
                    mQuestionBank[mCurrentIndex].setCheater(true);
                }
                mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.length;
                mIsCheater = false;
                updateQuestion();
            }
        });

        mPrevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if((mCurrentIndex - 1) < 0){
                    mCurrentIndex = mQuestionBank.length - 1;
                } else {
                    mCurrentIndex = (mCurrentIndex - 1) % mQuestionBank.length;
                }

                updateQuestion();
            }
        });

        mTrueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkAnswer(true);
            }
        });

        mFalseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkAnswer(false);
            }
        });

        mCheatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheatActivity cheatActivity = new CheatActivity();
                Boolean isAnswerTrue = mQuestionBank[mCurrentIndex].isAnswerTrue();
                Intent intent = cheatActivity.newIntent(MainActivity.this, isAnswerTrue);
                startActivityForResult(intent, REQUEST_CODE_CHEAT_ACTIVITY);
            }
        });

        updateQuestion();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode != Activity.RESULT_OK){
            return;
        }

        if(requestCode == REQUEST_CODE_CHEAT_ACTIVITY){
            if(data == null){
                return;
            }
            if(!mIsCheater) {
                mIsCheater = CheatActivity.wasAnswerShown(data);
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        super.onSaveInstanceState(savedInstanceState);
        Log.i(TAG, "onSaveInstanceState");
        savedInstanceState.putInt(KEY_INDEX, mCurrentIndex);
        savedInstanceState.putBoolean(USER_IS_CHEATER, mIsCheater);
    }

    public void updateQuestion(){
        int question = mQuestionBank[mCurrentIndex].getTextResId();
        mCurrentIndexTextView.setText(Integer.toString(mCurrentIndex + 1));
        mCurrentIndexTextView.append(") ");
        mQuestionTextView.setText(question);
    }

    public void checkAnswer(boolean userPressedTrue){
        final Handler handler = new Handler();
        boolean answerIsTrue = mQuestionBank[mCurrentIndex].isAnswerTrue();
        int messageResId = 0;

        if(mIsCheater || mQuestionBank[mCurrentIndex].isCheater()){
            messageResId = R.string.judgment_toast;
        }
        else if(userPressedTrue == answerIsTrue){
            messageResId = R.string.correct_toast;
            mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.length;
            updateQuestion();

        }else {
            messageResId = R.string.false_toast;
        }
        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show();
    }
}
