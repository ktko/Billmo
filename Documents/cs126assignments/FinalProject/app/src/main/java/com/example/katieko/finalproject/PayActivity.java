package com.example.katieko.finalproject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by katieko on 5/2/17.
 */

public class PayActivity extends AppCompatActivity {
    private TextView mUserPortionTextView;
    private TextView mNumPeopleTextView;

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference plansRef = database.getReference("Transactions");
    private DatabaseReference usersRef = database.getReference("Users");
    private FirebaseAuth auth = FirebaseAuth.getInstance();

    public static double calculateBillPortion(double billTotal, int numPayers) {
        return billTotal / numPayers;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);

        // takes intent extras and sets them into the TextViews
        mUserPortionTextView = (TextView) findViewById(R.id.userPortionTextView);
        mNumPeopleTextView = (TextView) findViewById(R.id.numPeopleTextView);

        Intent intent = getIntent();
        final String planID = intent.getStringExtra("planID");
        String userPortion = intent.getStringExtra("user portion");
        String numPayers = intent.getStringExtra("number people");

        mUserPortionTextView.setText(userPortion);
        mNumPeopleTextView.setText(numPayers);


        Button payButton = (Button) findViewById(R.id.payButton);
        payButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                plansRef.orderByKey().equalTo(planID).addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        final PaymentPlan paymentPlan = dataSnapshot.getValue(PaymentPlan.class);
                        final String code = paymentPlan.getSearchCode();
                        double billTotal = paymentPlan.getFinalTotal();
                        int numPayers = paymentPlan.getPayers().size();

                        // changed parts of original payment plan
                        final double userPortion = calculateBillPortion(billTotal, numPayers);
                        final double remainingTotal = billTotal - userPortion;

                        String currUser = auth.getCurrentUser().getUid();
                        usersRef.orderByKey().equalTo(currUser).addChildEventListener(new ChildEventListener() {
                            @Override
                            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                                Account account = dataSnapshot.getValue(Account.class);
                                double balance = account.getBalance();
                                double remainingBalance = balance - userPortion;

                                // if the user pays and then has $0 in their account, a reminder will appear
                                if (userPortion == balance) {
                                    paymentPlan.setTotalAmount(remainingTotal);
                                    plansRef.child(code).setValue(paymentPlan);

                                    account.setBalance(remainingBalance);
                                    usersRef.child(auth.getCurrentUser().getUid()).setValue(account);
                                    Toast.makeText(PayActivity.this,
                                            "Please Put More Money In Your Account",
                                            Toast.LENGTH_LONG).show();

                                // if the user doesn't have enough money, a reminder will appear
                                } else if (userPortion > balance) {
                                    Toast.makeText(PayActivity.this,
                                            "Please Deposit More Money Before Making a Payment",
                                            Toast.LENGTH_LONG).show();

                                } else {
                                    paymentPlan.setTotalAmount(remainingTotal);
                                    plansRef.child(code).setValue(paymentPlan);

                                    account.setBalance(remainingBalance);
                                    usersRef.child(auth.getCurrentUser().getUid()).setValue(account);

                                }

                                setResult(Activity.RESULT_OK,
                                        new Intent().putExtra("balance", remainingBalance));
                                finish();
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
            }
        });
    }
}
