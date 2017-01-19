/*
 * Copyright 2017 Alexander Orlov <alexander.orlov@loxal.net>. All rights reserved.
 */

package net.loxal.quizzer.client;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import net.loxal.quizzer.client.dto.Poll;
import net.loxal.quizzer.client.dto.Vote;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class MainActivity extends Activity {
    public static final int MAX_QUESTION_IDX = 9;
    public static final int INIT_QUESTION_IDX = 0;
    static final OkHttpClient CLIENT = new OkHttpClient();
    static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final String QUIZ_SESSION = UUID.randomUUID().toString();
    private static final List<Vote> ANSWERS = new ArrayList<>();
    private static final List<Poll> POLLS = new ArrayList<>(MAX_QUESTION_IDX);
    private static Poll activeQuestion;
    private static String userId;
    private int activeQuestionIdx;
    private TextView questionText;
    private RadioGroup options;
    private Button restart;
    private Button back;
    private Button next;
    private ImageView certificateImage;
    private ProgressBar progress;
    private LinearLayout controlsContainer;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initMenu();

        {
            controlsContainer = (LinearLayout) findViewById(R.id.controlsContainer);
            restart = (Button) findViewById(R.id.restart);
            back = (Button) findViewById(R.id.back);
            next = (Button) findViewById(R.id.next);
            questionText = (TextView) findViewById(R.id.question);
            options = (RadioGroup) findViewById(R.id.options);
            certificateImage = (ImageView) findViewById(R.id.certificate);
            progress = (ProgressBar) findViewById(R.id.progress);
            userId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        }

        populateQuestions();
        if (POLLS.size() == 0) {
            Log.w(MainActivity.class.getName(), "Connectivity issues…");
        } else {
            initQuiz();
        }
    }

    private void initMenu() {
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        final ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        );
        toggle.syncState();

        final NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull final MenuItem menuItem) {
                showCertificateList();
                drawer.closeDrawers();
                return true;
            }
        });
    }

    private void populateQuestions() {
        for (int idx = INIT_QUESTION_IDX; idx <= MAX_QUESTION_IDX; idx++) {
            final Poll poll = retrieveQuestion(idx);
            POLLS.add(poll);
        }
    }

    public void next(View v) {
        if (activeQuestionIdx <= MAX_QUESTION_IDX) {
            progress.incrementProgressBy(10);
            progress.incrementSecondaryProgressBy(11); // track elapsed time here

            if (isUnanswered())
                updateGivenAnswers(answerQuestion());
            activeQuestionIdx++;
            showQuestion();
            loadAnswer();
        }
    }

    private boolean isUnanswered() {
        return activeQuestionIdx <= ANSWERS.size();
    }

    public void back(View v) {
        if (activeQuestionIdx > INIT_QUESTION_IDX) {
            activeQuestionIdx--;
            showQuestion();
            loadAnswer();
        }
    }

    public void restart(View v) {
        initQuiz();
    }

    private void initQuiz() {
        progress.setProgress(0);
        progress.setSecondaryProgress(0);
        certificateImage.setVisibility(ImageView.INVISIBLE);
        activeQuestionIdx = INIT_QUESTION_IDX;
        showQuestion();
        restart.setVisibility(View.INVISIBLE);
        controlsContainer.setVisibility(View.VISIBLE);
        ANSWERS.clear();
    }

    private void loadAnswer() {
        if (ANSWERS.size() > activeQuestionIdx) {
            Vote answer = ANSWERS.get(activeQuestionIdx);

            for (int checkedIndex : answer.getAnswers()) {
                options.check(checkedIndex);
            }
        }
    }

    private Vote answerQuestion() {
        try {
            Vote answer = new Vote(
                    QUIZ_SESSION + "-" + activeQuestionIdx,
                    QUIZ_SESSION,
                    userId,
                    activeQuestion.getId(),
                    Collections.singletonList(options.getCheckedRadioButtonId()));
            final Request request = new Request.Builder()
                    .header("Authorization", "Basic " + BuildConfig.BASIC_AUTH_BASE64)
                    .url(getResources().getString(R.string.serviceRootUrl) + getResources().getString(R.string.answersEndpoint))
                    .post(RequestBody.create(MediaType.parse("application/json; charset=utf-8"),
                            OBJECT_MAPPER.writeValueAsString(answer)))
                    .build();
            final Vote answered = new AnswerQuestionTask().execute(request).get();
            Log.i(MainActivity.class.getName(), answered.toString());

            return answered;
        } catch (JsonProcessingException | InterruptedException | ExecutionException e) {
            Log.e(MainActivity.class.getName(), e.toString());
        }
        return null;
    }

    private void updateGivenAnswers(Vote answered) {
        if (ANSWERS.size() <= activeQuestionIdx)
            ANSWERS.add(activeQuestionIdx, answered);
        else
            ANSWERS.set(activeQuestionIdx, answered);
    }

    private void clearChoice() {
        options.removeAllViews();
        options.clearCheck();
    }

    private void showQuestion() {
        if (allQuestionsAnswered()) {
            evaluateQuiz();
            return;
        }
        clearChoice();
        final Poll poll = POLLS.get(activeQuestionIdx);
        activeQuestion = poll;
        defineQuestion(poll);
    }

    private void evaluateQuiz() {
        clearChoice();
        showLocalScore();

        showCertificate(); // TODO remove this once certificate list is implemented
    }

    private boolean allQuestionsAnswered() {
        return activeQuestionIdx > MAX_QUESTION_IDX;
    }

    private void showCertificate() {
        final Context baseContext = getBaseContext();
        final Intent intent = new Intent(baseContext, CertificateActivity.class);
        intent.putExtra("session", QUIZ_SESSION);
        baseContext.startActivity(intent);
    }

    private void showCertificateList() {
        final Context baseContext = getBaseContext();
        final Intent intent = new Intent(baseContext, CertificateListActivity.class);
        intent.putExtra("user", userId);
        baseContext.startActivity(intent);
    }

    private void showLocalScore() {
        certificateImage.setVisibility(ImageView.VISIBLE);
        controlsContainer.setVisibility(View.INVISIBLE);
        restart.setVisibility(View.VISIBLE);

        int correct = 0;
        for (Vote answer : ANSWERS) {
            if (answer.getCorrect()) {
                correct++;
            }
        }

        double correctness = ((double) correct / activeQuestionIdx) * 100;

        questionText.setText(correct + " correct answers out of " + ANSWERS.size() + " possible. \n\n" +
                new DecimalFormat("#.##").format(correctness) + "% correct");
    }

    private Poll retrieveQuestion(int number) {
        final Request request = new Request.Builder()
                .header("Authorization", "Basic " + BuildConfig.BASIC_AUTH_BASE64)
                .url(getResources().getString(R.string.serviceRootUrl) + getResources().getString(R.string.questionsEndpoint) + number)
                .build();

        try {
            return new RetrievePoolTask().execute(request).get();
        } catch (Exception e) {
            Log.w(MainActivity.class.getName(), e.getMessage());
            return null;
        }
    }

    private void defineQuestion(final Poll poll) {
        if (noQuestionRetrieved(poll)) {
            showNoQuestionAvailableView();
            return;
        } else {
            questionText.setText(activeQuestionIdx + 1 + ". " + poll.getQuestion());
        }

        int id = 0;
        for (final String answerOption : poll.getOptions()) {
            if (poll.getMultipleAnswers()) {
                final CheckBox option = new CheckBox(getApplicationContext());
                option.setId(id++);
                option.setText(answerOption);
                options.addView(option);
            } else {
                final RadioButton option = new RadioButton(getApplicationContext());
                option.setId(id++);
                option.setText(answerOption);
                options.addView(option);
            }
        }
    }

    private void showNoQuestionAvailableView() {
        questionText.setText("No question available");
        back.setVisibility(View.INVISIBLE);
        next.setVisibility(View.INVISIBLE);
        progress.setVisibility(View.INVISIBLE);
    }

    private boolean noQuestionRetrieved(Poll poll) {
        return poll == null;
    }

}
