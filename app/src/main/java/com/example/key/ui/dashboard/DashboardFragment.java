package com.example.key.ui.dashboard;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.key.R;

import org.w3c.dom.Text;

public class DashboardFragment extends Fragment {

    private DashboardViewModel dashboardViewModel1;
    private TextView mTextField;
    private Button countdownButton;
    private Boolean gameOver = true;
    private int local_score  = 0;
    private TextView highScore;
    private int timesPressed;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel1 =
                ViewModelProviders.of(this).get(DashboardViewModel.class);
        View root1 = inflater.inflate(R.layout.fragment_dashboard, container, false);
        final TextView textView1 = root1.findViewById(R.id.text_dashboard);
        dashboardViewModel1.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView1.setText(s);
            }
        });
        Button button = root1.findViewById(R.id.game_button);
        countdownButton = root1.findViewById(R.id.button_start);
        mTextField = root1.findViewById(R.id.countdown_text);
        highScore = root1.findViewById(R.id.high_score);

        final SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        timesPressed = sharedPref.getInt(getString(R.string.high_score_for_game), 0);

        highScore.setText("High Score: " + timesPressed);

        countdownButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new CountDownTimer(5000, 1000) {

                    public void onTick(long millisUntilFinished) {
                        gameOver = false;
                        mTextField.setText("T minus " + (millisUntilFinished / 1000 + 1) + " seconds!");
                    }

                    public void onFinish() {
                        gameOver = true;
                        mTextField.setText("Game Over! Tap START to Restart Game.");
                        if(timesPressed < local_score) {
                            SharedPreferences.Editor editor = sharedPref.edit();
                            editor.putInt(getString(R.string.high_score_for_game), local_score);
                            editor.commit();
                            highScore.setText("High Score: " + local_score);
                        }
                        local_score = 0;
                    }
                }.start();
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(gameOver == false) {
                    local_score += 1;
                    textView1.setText("Current Score: " + local_score);
                }
                else {
                   // Toast.makeText(getActivity(), "Tap START to start game", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });

        return root1;
    }
}