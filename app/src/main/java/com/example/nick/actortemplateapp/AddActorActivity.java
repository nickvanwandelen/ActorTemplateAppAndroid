package com.example.nick.actortemplateapp;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddActorActivity extends AppCompatActivity {

    private String projectKey;

    private DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_actor);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        projectKey = getIntent().getStringExtra("project_key");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_actor_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.action_addActor:
                handleAddActor();
            case R.id.action_cancel:
                handleCancel();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void handleAddActor(){
        String testID = "TestActor";

        reference.child("actors").child(testID).child("name").setValue(((EditText)findViewById(R.id.addActorNameEditText)).getText().toString());
        reference.child("actors").child(testID).child("description").setValue(((EditText)findViewById(R.id.addActorDescriptionEditText)).getText().toString());
        reference.child("actors").child(testID).child("active").setValue(true);
        reference.child("actors").child(testID).child("projects").child(projectKey).setValue(true);

        finish();
    }

    private void handleCancel(){
        finish();
    }

}
