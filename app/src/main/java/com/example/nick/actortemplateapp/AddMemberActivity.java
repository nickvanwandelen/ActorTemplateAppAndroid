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
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AddMemberActivity extends AppCompatActivity {

    private String actorKey;

    private DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_member);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        actorKey = getIntent().getStringExtra("actor_key");

        ((TextView)findViewById(R.id.addMember_actorTextView)).setText("Adding member for actor: " + getIntent().getStringExtra("actor_name"));

        reference = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_member_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.addmember_action_addMember:
                handleAddMember();
                return true;
            case R.id.addmember_action_cancel:
                handleCancel();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void handleAddMember(){
        int newActorID = 0;

        if(((EditText)findViewById(R.id.addMember_emailEditText)).getText().toString().equals("") ||
                ((EditText)findViewById(R.id.addMember_nameEditText)).getText().toString().equals("") ||
                ((EditText)findViewById(R.id.addMember_roleEditText)).getText().toString().equals("") ||
                ((EditText)findViewById(R.id.addMember_phoneEditText)).getText().toString().equals("") ||
                ((EditText)findViewById(R.id.addMember_notesEditText)).getText().toString().equals("")){
            Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
        }
        else{
            reference.child("members").limitToLast(1).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    int newMemberID = 0;

                    for(DataSnapshot actor : dataSnapshot.getChildren()) {
                        newMemberID = Integer.parseInt(actor.getKey());
                        newMemberID++;
                    }

                    reference.child("members").child("" + newMemberID).child("email").setValue(((EditText)findViewById(R.id.addMember_emailEditText)).getText().toString());
                    reference.child("members").child("" + newMemberID).child("name").setValue(((EditText)findViewById(R.id.addMember_nameEditText)).getText().toString());
                    reference.child("members").child("" + newMemberID).child("notes").setValue(((EditText)findViewById(R.id.addMember_notesEditText)).getText().toString());
                    reference.child("members").child("" + newMemberID).child("phone").setValue(((EditText)findViewById(R.id.addMember_phoneEditText)).getText().toString());
                    reference.child("members").child("" + newMemberID).child("picture").setValue("");
                    reference.child("members").child("" + newMemberID).child("role").setValue(((EditText)findViewById(R.id.addMember_roleEditText)).getText().toString());
                    reference.child("members").child("" + newMemberID).child("actorID").setValue(Integer.parseInt(actorKey));

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
