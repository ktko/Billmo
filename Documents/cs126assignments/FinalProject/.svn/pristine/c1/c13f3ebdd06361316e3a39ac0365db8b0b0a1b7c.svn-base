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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by katieko on 5/1/17.
 */

public class EditProfileActivity extends AppCompatActivity {

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
     *  Keeps the info taken from Firebase AuthUI constant in profile and changes username and balance.
     *  Stores user's profile info into a reference in Firebase Realtime Database
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        final FirebaseAuth auth = FirebaseAuth.getInstance();

        final EditText editUsernameInput = (EditText) findViewById(R.id.usernameEditor);
        final EditText editBalanceInput = (EditText) findViewById(R.id.balanceEditor);

        Button saveButton = (Button) findViewById(R.id.save_button);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isEmpty(editUsernameInput) && !isEmpty(editBalanceInput)) {
                    String name = auth.getCurrentUser().getDisplayName();
                    String username = editUsernameInput.getText().toString();
                    String email = auth.getCurrentUser().getEmail();
                    String balanceStr = editBalanceInput.getText().toString();
                    String uid = auth.getCurrentUser().getUid();

                    // altering one part of the retrieved profile info
                    double newBalance = Double.parseDouble(balanceStr);

                    Account account = new Account(name, username, email, newBalance, uid);
                    DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("Users").child(uid);
                    usersRef.setValue(account);

                    setResult(Activity.RESULT_OK,
                            new Intent().putExtra("name", name));
                    finish();
                } else {
                    Toast.makeText(EditProfileActivity.this,
                            "Please fill out all sections when editing profile",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
