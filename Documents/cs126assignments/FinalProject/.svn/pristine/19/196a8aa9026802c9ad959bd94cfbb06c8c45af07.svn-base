package com.example.katieko.finalproject;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @RunWith(AndroidJUnit4.class)
    public class dbTest {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Transactions");

        Account katie;
        PaymentPlan pizzaPlan;
        List<PaymentPlan> plans = new ArrayList<>();

        @Before
        public void onCreate() {
            katie = new Account("Katie Ko", "katieko", "katieko98@gmail.com", "cs126", 200.00);
            pizzaPlan = new PaymentPlan("Pizza Bill", 12.50,
                    "The pizza bill that needs to be split from our girls night hangout!");
            plans.add(pizzaPlan);
        }

        @Test
        public void useAppContext() throws Exception {
            // Context of the app under test.
            Context appContext = InstrumentationRegistry.getTargetContext();

            assertEquals("com.example.katieko.billmofinal", appContext.getPackageName());
        }

        @Test
        public void writeTransactionsObject() throws Exception {
            final CountDownLatch writeSignal = new CountDownLatch(1);

            myRef.push().setValue(katie, plans).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    writeSignal.countDown();
                }
            });

            writeSignal.await(10, TimeUnit.SECONDS);
        }

        @Test
        public void readAccountUsername() throws Exception {
            final CountDownLatch writeSignal = new CountDownLatch(1);

            DatabaseReference katieRef = database.getReference("Transactions/**key**/username");
            katieRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String katieUsername = dataSnapshot.getValue(String.class);
                    assertEquals("katieko", katieUsername);
                    writeSignal.countDown();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.w("TEST", "loadPost:onCancelled", databaseError.toException());
                }
            });

            writeSignal.await(10, TimeUnit.SECONDS);
        }

        @Test
        public void readPaymentPlanDescription() throws Exception {
            final CountDownLatch writeSignal = new CountDownLatch(1);

            DatabaseReference planDescriptionRef = database.getReference("Transactions/**key**/description");
            planDescriptionRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String planDescription = dataSnapshot.getValue(String.class);
                    assertEquals("The pizza bill that needs to be split from our girls night hangout!", planDescription);
                    writeSignal.countDown();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.w("TEST", "loadPost:onCancelled", databaseError.toException());
                }
            });

            writeSignal.await(10, TimeUnit.SECONDS);
        }
    };
}
