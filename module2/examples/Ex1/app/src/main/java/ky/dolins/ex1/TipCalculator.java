package ky.dolins.ex1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Currency;
import java.util.Locale;

public class TipCalculator extends AppCompatActivity {

    private Double total = 0.00;
    private TextView textView;
    private Locale locale;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tip_calculator);

        locale = Locale.getDefault();

        textView = (TextView) findViewById(R.id.result);
        String totalString = Currency.getInstance(locale).getSymbol() +
                String.format(locale, "%.2f", total);
        textView.setText(totalString);
    }

    public void Calculate(View view) {
        EditText billInput = (EditText) findViewById(R.id.billAmount);
        Double billAmount = Double.parseDouble(billInput.getText().toString());

        EditText tipInput = (EditText) findViewById(R.id.tipPercentage);
        Double tipAmount = Double.parseDouble(tipInput.getText().toString());

        EditText splitInput = (EditText) findViewById(R.id.splitBill);
        Long splitFactor = Long.parseLong(splitInput.getText().toString());

        // Calculate value of bill per person, and round up to nearest cent
        total = (billAmount * (1 + tipAmount / 100D)) / Double.valueOf(splitFactor);
        total = Math.ceil(total * 100D) / 100D;

        // Set text view
        String totalString = Currency.getInstance(locale).getSymbol() +
                String.format(locale, "%.2f", total);
        textView.setText(totalString);
    }
}
