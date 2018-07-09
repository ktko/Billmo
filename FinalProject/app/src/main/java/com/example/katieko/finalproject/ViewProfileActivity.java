package com.example.katieko.finalproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


/**
 * Created by katieko on 5/1/17.
 */

public class ViewProfileActivity extends AppCompatActivity {
    private String username;
    private String balance;

    private TextView mUsernameTextView;
    private TextView mBalanceTextView;

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference usersRef = database.getReference("Users");
    private FirebaseAuth auth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_profile);

        TextView mFullNameTextView = (TextView) findViewById(R.id.fullNameTextView);
        mBalanceTextView = (TextView) findViewById(R.id.balanceTextView);
        mUsernameTextView = (TextView) findViewById(R.id.usernameTextView);
        TextView mEmailTextView = (TextView) findViewById(R.id.emailTextView);
        TextView mUIDTextView = (TextView) findViewById(R.id.uidTextView);

        // Reads from the database
        usersRef.orderByKey().equalTo(auth.getCurrentUser().getUid()).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Account account = dataSnapshot.getValue(Account.class);
                username = account.getUsername();
                double balanceNum = account.getBalance();

                // changes retrieved info
                balance = String.format("%.2f", balanceNum);

                mUsernameTextView.setText(username);
                mBalanceTextView.setText(balance);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Account account = dataSnapshot.getValue(Account.class);
                username = account.getUsername();
                double balanceNum = account.getBalance();

                // changes retrieved info
                balance = String.format("%.2f", balanceNum);

                mUsernameTextView.setText(username);
                mBalanceTextView.setText(balance);
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

        // gets information about user from Firebase authentication
        String fullName = auth.getCurrentUser().getDisplayName();
        String email = auth.getCurrentUser().getEmail();
        String uid = auth.getCurrentUser().getUid();

        mFullNameTextView.setText(fullName);
        mEmailTextView.setText(email);
        mUIDTextView.setText(uid);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.profile_menu, menu);
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
            case R.id.edit_profile_menu:
                startActivity(new Intent(getApplicationContext(), EditProfileActivity.class));
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
