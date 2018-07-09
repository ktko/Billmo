package com.example.katieko.finalproject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by katieko on 4/30/17.
 */

public class JoinPlanActivity extends AppCompatActivity {
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference plansRef = database.getReference("Transactions");
    private DatabaseReference usersRef = database.getReference("Users");
    private FirebaseAuth auth = FirebaseAuth.getInstance();

    private String searchCode;


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
     *  Checks if the code entry matches created plans. Adds user into the plan on the database and
     *  vice versa if matches.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_plan);

        final EditText searchCodeInput = (EditText) findViewById(R.id.code_entry_text);

        Button addButton = (Button) findViewById(R.id.add_code_button);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isEmpty(searchCodeInput)) {
                    final String planCode = searchCodeInput.getText().toString();

                    // this helps find a specified key in the plans Database Reference
                    plansRef.orderByKey().equalTo(planCode).addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                            PaymentPlan plan = dataSnapshot.getValue(PaymentPlan.class);
                            String currUser = auth.getCurrentUser().getUid();

                            // adds the current user into the list of payers for the specified payment plan
                            searchCode = plan.getSearchCode();
                            plan.getPayers().add(currUser);
                            plansRef.child(planCode).setValue(plan);

                            // adds the chosen plan into the list of plans for the current user
                            usersRef.orderByKey().equalTo(currUser).addChildEventListener(new ChildEventListener() {
                                @Override
                                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                                    Account account = dataSnapshot.getValue(Account.class);
                                    account.getPlans().add(searchCode);
                                    usersRef.child(auth.getCurrentUser().getUid()).setValue(account);
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

                    setResult(Activity.RESULT_OK,
                            new Intent().putExtra("code", planCode));
                    finish();
                } else {
                    Toast.makeText(JoinPlanActivity.this, "Please enter a valid plan", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
