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
            case R.id.addmember_action_cancel:
                handleCancel();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void handleAddMember(){
        String testID = "TestMember";

        reference.child("members").child(testID).child("email").setValue(((EditText)findViewById(R.id.addMember_emailEditText)).getText().toString());
        reference.child("members").child(testID).child("name").setValue(((EditText)findViewById(R.id.addMember_nameEditText)).getText().toString());
        reference.child("members").child(testID).child("notes").setValue(((EditText)findViewById(R.id.addMember_notesEditText)).getText().toString());
        reference.child("members").child(testID).child("phone").setValue(((EditText)findViewById(R.id.addMember_phoneEditText)).getText().toString());
        reference.child("members").child(testID).child("picture").setValue(""); //TODO
        reference.child("members").child(testID).child("role").setValue(((EditText)findViewById(R.id.addMember_roleEditText)).getText().toString());
        reference.child("members").child(testID).child("actors").child(actorKey).setValue(true);

        finish();
    }

    private void handleCancel(){
        finish();
    }

}
