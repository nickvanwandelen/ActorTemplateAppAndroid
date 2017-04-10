package com.example.nick.actortemplateapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import domain.Member;

public class ShowIndividualMemberActivity extends AppCompatActivity {

    private String memberKey;
    private Member showingMember;

    private DatabaseReference reference;

    private Menu showingMenu;

    private boolean isEditing = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_individual_member);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        reference = FirebaseDatabase.getInstance().getReference();

        Intent retrievedIntent = getIntent();
        memberKey = retrievedIntent.getStringExtra("member_key");

        reference.child("members").child(memberKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                showingMember = dataSnapshot.getValue(Member.class);
                if(showingMember == null){
                    Toast.makeText(getApplicationContext(), "Error reading Firebase database: Member not found", Toast.LENGTH_SHORT).show();
                    return;
                }

                EditText memberEmailET = (EditText) findViewById(R.id.showMember_emailEditText);
                memberEmailET.setText(showingMember.getEmail());
                memberEmailET.setEnabled(false);

                EditText memberNameET = (EditText) findViewById(R.id.showMember_nameEditText);
                memberNameET.setText(showingMember.getName());
                memberNameET.setEnabled(false);

                EditText memberNotesET = (EditText) findViewById(R.id.showMember_notesEditText);
                memberNotesET.setText(showingMember.getNotes());
                memberNotesET.setEnabled(false);

                EditText memberPhoneET = (EditText) findViewById(R.id.showMember_phoneEditText);
                memberPhoneET.setText(showingMember.getPhone());
                memberPhoneET.setEnabled(false);

                EditText memberRoleET = (EditText) findViewById(R.id.showMember_roleEditText);
                memberRoleET.setText(showingMember.getRole());
                memberRoleET.setEnabled(false);

                ((Toolbar)findViewById(R.id.toolbar)).setTitle(showingMember.getName());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "Error reading Firebase database", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu){
        getMenuInflater().inflate(R.menu.individual_member_menu, menu);
        showingMenu = menu;
        String loggedInUserUid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        reference.child("users").child(loggedInUserUid).child("isAnalist").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                boolean isAnalist = dataSnapshot.getValue(boolean.class);

                if(!isAnalist){
                    menu.getItem(0).setVisible(false);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case R.id.member_menu_action_edit:
                handleEditMember();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void handleEditMember(){
        EditText memberEmailET = (EditText) findViewById(R.id.showMember_emailEditText);
        EditText memberNameET = (EditText) findViewById(R.id.showMember_nameEditText);
        EditText memberNotesET = (EditText) findViewById(R.id.showMember_notesEditText);
        EditText memberPhoneET = (EditText) findViewById(R.id.showMember_phoneEditText);
        EditText memberRoleET = (EditText) findViewById(R.id.showMember_roleEditText);

        memberEmailET.setEnabled(!isEditing);
        memberNameET.setEnabled(!isEditing);
        memberNotesET.setEnabled(!isEditing);
        memberPhoneET.setEnabled(!isEditing);
        memberRoleET.setEnabled(!isEditing);

        if(isEditing){
            reference.child("members").child(memberKey).child("email").setValue(memberEmailET.getText().toString());
            reference.child("members").child(memberKey).child("name").setValue(memberNameET.getText().toString());
            reference.child("members").child(memberKey).child("notes").setValue(memberNotesET.getText().toString());
            reference.child("members").child(memberKey).child("phone").setValue(memberPhoneET.getText().toString());
            reference.child("members").child(memberKey).child("role").setValue(memberRoleET.getText().toString());
        }

        isEditing = !isEditing;
    }

}
