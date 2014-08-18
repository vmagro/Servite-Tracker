package io.vinnie.servitetracker.android;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;

public class LauncherActivity extends Activity implements CardListener,
        View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (TrackerSharedPrefs.getUserCard() != null) {
            checkUser(TrackerSharedPrefs.getUserCard());
        } else {
            setContentView(R.layout.activity_launcher);
            findViewById(R.id.btn_go).setOnClickListener(this);
        }
    }

    private void goToApplication() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onCardScanned(String cardData) {
        TrackerSharedPrefs.setUserCard(cardData);
        checkUser(cardData);
    }

    private void checkUser(String id) {
        ParseQuery.getQuery("Student").whereEqualTo("idNumber", id).findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, ParseException e) {
                ParseObject user = parseObjects.get(0);
                if (user.getBoolean("admin")) {
                    Toast.makeText(LauncherActivity.this, "Welcome " + user.getString("firstName") + "!", Toast.LENGTH_LONG).show();
                    goToApplication();
                } else {
                    setContentView(R.layout.activity_launcher);
                    findViewById(R.id.btn_go).setOnClickListener(LauncherActivity.this);
                    Toast.makeText(LauncherActivity.this, "You are not authorized", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_go:
                onCardScanned(((EditText) findViewById(R.id.manual_card_entry)).getText().toString());
                return;
        }
    }
}
