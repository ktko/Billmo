package com.example.katieko.finalproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;


/**
 * Created by katieko on 4/29/17.
 */

public class DetailActivity extends AppCompatActivity {
    private TextView mTitleTextView;
    private TextView mBillAmountTextView;
    private TextView mDescriptionTextView;
    private TextView mTipTextView;
    private TextView mSearchCodeTextView;

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference plansRef = database.getReference("Transactions");


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode == RESULT_OK) {
            this.recreate();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        mTitleTextView = (TextView) findViewById(R.id.titleTextView);
        mBillAmountTextView = (TextView) findViewById(R.id.billAmountTextView);
        mDescriptionTextView = (TextView) findViewById(R.id.descriptionTextView);
        mTipTextView = (TextView) findViewById(R.id.tipTextView);
        mSearchCodeTextView = (TextView) findViewById(R.id.searchCodeTextView);

        // received from PlansAdapter
        final Intent intent = getIntent();
        final String planID = intent.getStringExtra("plan");

        final Intent intent2 = new Intent(getApplicationContext(), PayActivity.class);

        // Read from the database
        plansRef.orderByKey().equalTo(planID).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                PaymentPlan paymentPlan = dataSnapshot.getValue(PaymentPlan.class);

                // calling and initializing different parts of the payment plan
                String title = paymentPlan.getTitle();
                double billAmountNum = paymentPlan.getBillAmount();
                double billTotal = paymentPlan.getFinalTotal();
                double tipNum = paymentPlan.getTip();
                String description = paymentPlan.getDescription();
                String searchCode = paymentPlan.getSearchCode();
                int numPayers = paymentPlan.getPayers().size();
                double userPortionNum = billTotal / numPayers;

                // altering parts of the payment plan
                String userPortion = "$" + String.format("%.2f", userPortionNum);
                String billAmount = "$" + String.format("%.2f", billAmountNum);
                String tip = String.valueOf(tipNum * 100) + "%";


                mTitleTextView.setText(title);
                mBillAmountTextView.setText(billAmount);
                mDescriptionTextView.setText(description);
                mTipTextView.setText(tip);
                mSearchCodeTextView.setText(searchCode);

                // put into the intent for later use of setting these values into TextViews
                intent2.putExtra("user portion", userPortion);
                intent2.putExtra("number people", String.valueOf(numPayers));
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        Button payButton = (Button) findViewById(R.id.pay_button);
        payButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent2.putExtra("planID", planID);

                startActivityForResult(intent2, 1);
                finish();
            }
        });
    }
}
