package com.example.se2einzelphase;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class CalculationActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {
    private EditText editTextUserInput;
    private TextView textViewResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculation);

        editTextUserInput = findViewById(R.id.activity_calculation_user_input);
        textViewResult = findViewById(R.id.activity_calculation_result);
    }

    public void showPopup(View v) {
        PopupMenu popupMenu = new PopupMenu(this, v);
        popupMenu.inflate(R.menu.menu_items);
        popupMenu.setOnMenuItemClickListener(this);
        popupMenu.show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        if (item.getItemId() == R.id.menu_item_network) {
            Intent startNetworkIntent = new Intent(this, MainActivity.class);
            startActivity(startNetworkIntent);
            return true;
        }
        return false;
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