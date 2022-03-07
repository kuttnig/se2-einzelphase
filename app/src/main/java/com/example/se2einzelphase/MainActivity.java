package com.example.se2einzelphase;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;

public class MainActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {
    private static final String TAG = "MainActivity";

    private EditText editTextUserInput;
    private TextView textViewServerResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextUserInput = findViewById(R.id.activity_main_user_input);
        textViewServerResponse = findViewById(R.id.activity_main_server_response);
    }

    public void showPopup(View v) {
        PopupMenu popupMenu = new PopupMenu(this, v);
        popupMenu.inflate(R.menu.menu_items);
        popupMenu.setOnMenuItemClickListener(this);
        popupMenu.show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        if (item.getItemId() == R.id.menu_item_calculation) {
            Intent startCalculationIntent = new Intent(this, CalculationActivity.class);
            startActivity(startCalculationIntent);
            return true;
        }
        return false;
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

                textViewServerResponse.post(() -> textViewServerResponse.setText(answerFromServer));
            } catch (UnknownHostException e) {
                Log.e(TAG, "Hello, I'm a log-message.");
            } catch (IOException e) {
                Log.e(TAG, "It worked on my computer.");
            }
        }).start();
    }
}
