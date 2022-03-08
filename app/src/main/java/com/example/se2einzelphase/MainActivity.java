package com.example.se2einzelphase;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private EditText editTextUserInput;
    private TextView textViewResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextUserInput = findViewById(R.id.activity_main_user_input);
        textViewResult = findViewById(R.id.activity_main_result);
    }

    public void sendMessage(View v) {
        new Thread(() -> {
            try {
                String userInput = editTextUserInput.getText().toString();

                Socket clientSocket = new Socket("se2-isys.aau.at", 53212);
                DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
                BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                outToServer.writeBytes(userInput + "\n");
                String answerFromServer = inFromServer.readLine();
                clientSocket.close();

                textViewResult.post(() -> textViewResult.setText(answerFromServer));
            } catch (UnknownHostException e) {
                Log.e(TAG, "Hello, I'm a log-message.");
            } catch (IOException e) {
                Log.e(TAG, "It worked on my computer.");
            }
        }).start();
    }

    public void computeResult(View v) {
        new Thread(() -> {
            String userInput = editTextUserInput.getText().toString();
            String s = "There is no pair with a GCD greater than 1.";

            for (int i = 0; i < userInput.length(); i++) {
                for (int j = (i + 1); j < userInput.length(); j++) {
                    int n = Character.getNumericValue(userInput.charAt(i));
                    int k = Character.getNumericValue(userInput.charAt(j));

                    if (k > n) {
                        int tmp = n;
                        n = k;
                        k = tmp;
                    }
                    while (k != 0) {
                        int rem = n % k;
                        n = k;
                        k = rem;
                    }

                    if (n != 1) {
                        s = "Pair found!\nDigit " + Character.getNumericValue(userInput.charAt(i)) + " at index " + i
                                + "\nDigit " + Character.getNumericValue(userInput.charAt(j)) + " at index " + j
                                + "\nGCD: " + n;

                        i = userInput.length();
                        j = userInput.length();
                    }
                }
            }

            // necessary because otherwise the ide complains: Variable used in lambda expression should be final or effectively final
            String result = s;
            textViewResult.post(() -> textViewResult.setText(result));
        }).start();
    }
}
