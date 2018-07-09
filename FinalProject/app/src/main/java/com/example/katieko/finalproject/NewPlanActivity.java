package com.example.katieko.finalproject;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by katieko on 4/30/17.
 */

public class NewPlanActivity extends AppCompatActivity {
    double tip;

    /**
     *  Checks to see if an EditText is empty
     *
     *  @param editText
     *  @return a boolean that indicates whether the EditText is empty or not
     */
    private boolean isEmpty(EditText editText) {
        return editText.getText().toString().trim().length() == 0;
    }

    /**
     *  Takes the desired input from the user and uses it to make a new payment plan
     *
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_plan);

        // takes the input of the user
        final EditText titleInput = ( EditText ) findViewById( R.id.title_creator );
        final EditText billTotalInput = ( EditText ) findViewById( R.id.bill_total_creator );
        final EditText descriptionInput = ( EditText ) findViewById( R.id.description_creator );
        final RadioButton tenPercentTip = (RadioButton) findViewById(R.id.ten_percent_button);
        final RadioButton fifteenPercentTip = (RadioButton) findViewById(R.id.fifteen_percent_button);
        final RadioButton twentyPercentTip = (RadioButton) findViewById(R.id.twenty_percent_button);


        Button createButton = ( Button ) findViewById( R.id.create_plan_button );
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // boolean that checks if a radio button was checked
                boolean tipPicked = (tenPercentTip.isChecked()
                        || fifteenPercentTip.isChecked()
                        || twentyPercentTip.isChecked());

                if (!isEmpty(titleInput) && !isEmpty(billTotalInput) && !isEmpty(descriptionInput) && tipPicked) {
                    String title = titleInput.getText().toString();
                    String billAmountStr = billTotalInput.getText().toString();
                    String description = descriptionInput.getText().toString();

                    // makes the bill amount input a double
                    double billAmount = Double.parseDouble(billAmountStr);


                    // assigns the corresponding double to whichever RadioButton is chosen for tip
                    if (tenPercentTip.isChecked()) {
                        tip = 0.10;
                    } else if (fifteenPercentTip.isChecked()) {
                        tip = 0.15;
                    } else if (twentyPercentTip.isChecked()) {
                        tip = 0.20;
                    }

                    double billTotal = billAmount * (1 + tip);

                    // sending data created from new plan back to the main screen activity
                    setResult(Activity.RESULT_OK,
                            new Intent().putExtra("title", title).putExtra("bill amount", billAmountStr)
                                    .putExtra("bill with tip", billTotal).putExtra("description", description));
                    finish();

                    // writing to the Firebase database for more children under the Database Reference
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference plansRef = database.getReference("Transactions");

                    DatabaseReference newPlan = plansRef.push();

                    // sets the key of the database reference as the search code
                    String searchCode = newPlan.getKey();
                    PaymentPlan plan = new PaymentPlan(title, billAmount, billTotal, tip, description, searchCode);
                    newPlan.setValue(plan);

                    // copies the search code to clipboard after pressing create
                    ClipboardManager clipboard = ( ClipboardManager ) getSystemService( Context.CLIPBOARD_SERVICE );
                    ClipData clip = ClipData.newPlainText( "text", searchCode );
                    clipboard.setPrimaryClip( clip );
                    Toast.makeText(NewPlanActivity.this, "Code Copied to Clipboard", Toast.LENGTH_LONG).show();

                } else {
                    Toast.makeText(NewPlanActivity.this, "Please fill out all sections of the form.", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
