package com.example.katieko.finalproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.ArrayList;

/**
 * Created by katieko on 4/29/17.
 */

public class MainScreenActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private PlansAdapter mPlansAdapter;

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference plansRef = database.getReference("Transactions");
    private DatabaseReference usersRef = database.getReference("Users");
    private FirebaseAuth auth = FirebaseAuth.getInstance();

    private Account currUser;
    private ArrayList<PaymentPlan> plans;
    private ArrayList<String> planIDs;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView(R.layout.activity_mainscreen);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        usersRef.orderByKey().equalTo(auth.getCurrentUser().getUid()).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                currUser = dataSnapshot.getValue(Account.class);
                getPaymentPlans();
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

    public void getPaymentPlans(){
        if (currUser.getPlans() != null){
            plans = new ArrayList<>();
            planIDs = new ArrayList<>();

            for (int i = 0; i < currUser.getPlans().size(); i++) {
                final int currIndex = i;

                plansRef.orderByKey().equalTo(currUser.getPlans().get(i)).addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        planIDs.add(dataSnapshot.getKey());
                        plans.add(dataSnapshot.getValue(PaymentPlan.class));
                        if (currIndex == currUser.getPlans().size() - 1) {
                            mPlansAdapter = new PlansAdapter(plans);
                            mRecyclerView.setAdapter(mPlansAdapter);
                        }
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
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.mainscreen_menu, menu);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode == RESULT_OK) {
            this.recreate();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.create_plan_menu:
                startActivity(new Intent(getApplicationContext(), NewPlanActivity.class));
                return true;
            case R.id.join_menu:
                startActivityForResult(new Intent(getApplicationContext(), JoinPlanActivity.class), 1);
                return true;
            case R.id.view_profile_menu:
                startActivity(new Intent(getApplicationContext(), ViewProfileActivity.class));
                return true;
            case R.id.sign_out_menu:
                AuthUI.getInstance().signOut(this);
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
