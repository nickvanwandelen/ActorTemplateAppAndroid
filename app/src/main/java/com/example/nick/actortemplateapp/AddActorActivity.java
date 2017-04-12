package com.example.nick.actortemplateapp;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import domain.Actor;

public class AddActorActivity extends AppCompatActivity {

    private String projectKey;
    private String projectName;

    private DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_actor);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        projectKey = getIntent().getStringExtra("project_key");
        projectName = getIntent().getStringExtra("project_name");

        ((TextView) findViewById(R.id.addActor_projectTextView)).setText("Adding actor for project: " + projectName);
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
                return true;
            case R.id.action_cancel:
                handleCancel();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void handleAddActor(){
        if(((EditText)findViewById(R.id.addActorNameEditText)).getText().toString().equals("") ||
                ((EditText)findViewById(R.id.addActorDescriptionEditText)).getText().toString().equals("")   ){
            Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
        }
        else{
            reference.child("actors").orderByKey().limitToLast(1).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    int newActorID = 0;

                    for(DataSnapshot actor : dataSnapshot.getChildren()) {
                        newActorID = Integer.parseInt(actor.getKey());
                        newActorID++;
                    }

                    reference.child("actors").child("" + newActorID).child("name").setValue(((EditText)findViewById(R.id.addActorNameEditText)).getText().toString());
                    reference.child("actors").child("" + newActorID).child("description").setValue(((EditText)findViewById(R.id.addActorDescriptionEditText)).getText().toString());
                    reference.child("actors").child("" + newActorID).child("active").setValue(true);
                    reference.child("actors").child("" + newActorID).child("projectID").setValue(Integer.parseInt(projectKey));

                    finish();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

    private void handleCancel(){
        finish();
    }

}
