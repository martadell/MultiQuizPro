package edu.upc.eseiaat.pma.multiquizpro;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class QuizActivity extends AppCompatActivity {

    private int ids_answers[] = {
            R.id.answer1, R.id.answer2, R.id.answer3, R.id.answer4 };
    private int correct_answer, current_question;
    private String[] all_questions;
    private boolean[] answer_is_correct;
    private int[] answer;
    private TextView text_question;
    private RadioGroup group;
    private Button btn_next, btn_prev;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        text_question = (TextView) findViewById(R.id.text_question);
        group = (RadioGroup) findViewById(R.id.answer_group);
        btn_next = (Button) findViewById(R.id.btn_next);
        btn_prev = (Button) findViewById(R.id.btn_prev);

        all_questions = getResources().getStringArray(R.array.all_questions);
        startOver();

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer();

                if (current_question < all_questions.length - 1) {
                    current_question++;
                    showQuestion();
                } else {
                    checkResults();
                }

                btn_prev.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        checkAnswer();
                        if (current_question > 0) {
                            current_question--;
                            showQuestion();
                        }
                    }
                });
            }});}

    private void startOver() {
        answer_is_correct = new boolean[all_questions.length];
        answer = new int[all_questions.length];
        for (int i = 0; i < answer.length; i++) {
            answer[i] = -1;  }

        current_question = 0;
        showQuestion();
    }

    private void checkAnswer() {
                int id = group.getCheckedRadioButtonId();
                int current_answer = -1;

                for (int i = 0; i < ids_answers.length; i++) {
                    if (ids_answers[i] == id) {
                        current_answer = i;
                    }

                    answer_is_correct[current_question] = (current_answer == correct_answer);
                    answer[current_question] = current_answer;
                }
            }

            private void showQuestion() {
                String q = all_questions[current_question];
                String[] parts = q.split(";");

                group.clearCheck();
                text_question.setText(parts[0]);

                for (int i = 0; i < ids_answers.length; i++) {
                    RadioButton rb = (RadioButton) findViewById(ids_answers[i]);
                    String current_answer = parts[i + 1];
                    if (current_answer.charAt(0) == '*') {
                        correct_answer = i;
                        current_answer = current_answer.substring(1);}

                    rb.setText(current_answer);

                    if (answer[current_question] == i) {
                        rb.setChecked(true);}}


                if (current_question == 0) {
                    btn_prev.setVisibility(View.GONE);
                } else {
                    btn_prev.setVisibility(View.VISIBLE);
                }

                if (current_question == all_questions.length - 1) {
                    btn_next.setText(R.string.check);
                } else {
                    btn_next.setText(R.string.next);
                }
            }

            private void checkResults() {
                int correct = 0, incorrect = 0, unanswered = 0;
                for (int i=0; i< all_questions.length; i++) {
                    if (answer_is_correct[i]) correct++;
                    else { if (answer[i] == -1 )unanswered++;
                        else incorrect++;}}

               String message = String.format("%s: %d\n%s: %d\n%s: %d\n", getString(R.string.correct),
                       correct, getString(R.string.incorrect), incorrect, getString(R.string.unanswered),unanswered);

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(R.string.results);
                builder.setMessage(message);
                builder.setPositiveButton(R.string.finish, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();}});
                builder.setNegativeButton(R.string.startover, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        startOver();  }});

                builder.create().show();

            }


}