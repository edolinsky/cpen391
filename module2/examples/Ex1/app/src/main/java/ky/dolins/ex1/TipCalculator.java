package ky.dolins.ex1;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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

        // Parse bill amount.
        Double billAmount;
        EditText billInput = (EditText) findViewById(R.id.billAmount_text);
        try {
            billAmount = Double.parseDouble(billInput.getText().toString());
        } catch (NullPointerException | NumberFormatException ne) {
            billAmount = 0.0D;      // Default to 0 on empty input or if unparseable.
        }

        // Parse tip amount.
        Double tipAmount;
        EditText tipInput = (EditText) findViewById(R.id.tipPercentage_text);
        try {
            tipAmount = Double.parseDouble(tipInput.getText().toString());
        } catch (NullPointerException | NumberFormatException ne) {
            tipAmount = 0.0D;       // Default to 0 not specified or if unparseable.
        }

        // Parse number of people to split bill.
        Long splitFactor;
        EditText splitInput = (EditText) findViewById(R.id.splitBill_text);
        try {
            splitFactor = Long.parseLong(splitInput.getText().toString());
        } catch (NullPointerException | NumberFormatException ne ) {
            splitFactor = 1L;   // Default to 1 person if not specified or if unparseable.
        }

        // Make sure we're not trying to divide by 0.
        if (splitFactor.equals(0L)) {

            // If we are trying to divide by 0, display error message.
            Context context = getApplicationContext();
            CharSequence text = getText(R.string.error_divide_by_zero);
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
            return;
        }

        // Calculate value of bill per person, and round up to nearest cent
        total = (billAmount * (1 + tipAmount / 100D)) / Double.valueOf(splitFactor);
        total = Math.ceil(total * 100D) / 100D;

        // Display the calculated amount to the user.
        String totalString = Currency.getInstance(locale).getSymbol() +
                String.format(locale, "%.2f", total);
        textView.setText(totalString);
    }
}
