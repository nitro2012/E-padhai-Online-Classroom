package com.example.e_padhai;


import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import org.jitsi.meet.sdk.BroadcastEvent;
import android.content.BroadcastReceiver;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;

import org.jitsi.meet.sdk.JitsiMeetActivity;
        import org.jitsi.meet.sdk.JitsiMeetConferenceOptions;

        import java.net.MalformedURLException;
        import java.net.URL;
import java.util.HashMap;
import java.util.Objects;

import timber.log.Timber;

public class virtualClass extends AppCompatActivity {
    private final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            onBroadcastReceived(intent);
        }
    };
    private DatabaseReference ref;
    private Button endMeet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ref=DatabaseUtil.getDatabase().getReference().child("Classes").child(getIntent().getStringExtra("id"));
        setContentView(R.layout.activity_virtual_class);




        endMeet=findViewById(R.id.endButton);


            endMeet.setOnClickListener(v ->


                    finish()
            );


        try {

            JitsiMeetConferenceOptions options = new JitsiMeetConferenceOptions.Builder()
                    .setServerURL(new URL(""))

                    .build();


            registerForBroadcastMessages();


            joinVideoCall(getIntent().getStringExtra("id"));

        } catch (MalformedURLException e) {
            e.printStackTrace();
            joinVideoCall(getIntent().getStringExtra("id"));
        }

    }

    @Override
    protected void onDestroy() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver);

        super.onDestroy();
    }


    public void joinVideoCall(String text) {

        if (text.length() > 0) {

            JitsiMeetConferenceOptions options
                    = new JitsiMeetConferenceOptions.Builder()
                    .setRoom(text)
                    .setConfigOverride("requireDisplayName", true)
                    .build();
            JitsiMeetActivity.launch(this, options);
        }
    }

    private void registerForBroadcastMessages() {
        IntentFilter intentFilter = new IntentFilter();


        for (BroadcastEvent.Type type : BroadcastEvent.Type.values()) {
            intentFilter.addAction(type.getAction());
        }

        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver, intentFilter);
    }


    private void onBroadcastReceived(Intent intent) {
        if (intent != null) {
            BroadcastEvent event = new BroadcastEvent(intent);

            switch (event.getType()) {
                case CONFERENCE_JOINED:
                    Timber.i("Conference Joined with url%s", event.getData().get("url"));
                    break;
                case PARTICIPANT_JOINED:
                    Timber.i("Participant joined%s", event.getData().get("name"));
                    break;

                case CONFERENCE_TERMINATED: {
                    ref.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {


                            ref.child("VideoCall").setValue(false);

                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });


                }
                        break;
            }
        }

    }
}