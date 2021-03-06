package com.example.nick.actortemplateapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import adapter.MemberAdapter;
import domain.Actor;

public class ShowIndividualActorActivity extends AppCompatActivity {

    private String actorKey;
    private String actorName;
    private Actor showingActor;

    private DatabaseReference reference;

    private RecyclerView recyclerView;
    private MemberAdapter adapter;

    private Menu showingMenu;

    private boolean isEditing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_individual_actor);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        reference = FirebaseDatabase.getInstance().getReference();

        Intent recievedIntent = getIntent();
        actorKey = recievedIntent.getStringExtra("actor_key");

        reference.child("actors").child(actorKey).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                showingActor = dataSnapshot.getValue(Actor.class); //retrieve selected project object
                if(showingActor == null){ //validate if selected project exists, if not: return and show error
                    Toast.makeText(getApplicationContext(), "Error reading Firebase database: Project not found", Toast.LENGTH_SHORT).show();
                    return;
                }

                actorName = showingActor.getName();

                EditText projectNameET = (EditText) findViewById(R.id.individualActorName);
                projectNameET.setText(showingActor.getName());
                projectNameET.setEnabled(false);

                EditText projectDescriptionET = (EditText) findViewById(R.id.individualActorDescription);
                projectDescriptionET.setText(showingActor.getDescription());
                projectDescriptionET.setEnabled(false);

                ((Toolbar)findViewById(R.id.toolbar)).setTitle(showingActor.getName());
                ((TextView)findViewById(R.id.showActor_ProjectTextView)).setText("Actor belongs to Project: " + getIntent().getStringExtra("project_name"));

                adapter = new MemberAdapter(actorKey, showingActor.getName());

                recyclerView = (RecyclerView) findViewById(R.id.memberRecyclerView);
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.setAdapter(adapter);


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "Error reading Firebase database", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu){
        getMenuInflater().inflate(R.menu.individual_actor_menu, menu);
        showingMenu = menu;
        String loggedInUserUid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        reference.child("users").child(loggedInUserUid).child("isAnalist").addValueEventListener(new ValueEventListener() { //Validate that logged in user is Analist, if not: disable addActor and archiveProject buttons
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                boolean analistDb = dataSnapshot.getValue(boolean.class);

                if(!analistDb){
                    menu.getItem(0).setVisible(false);
                    menu.getItem(1).setVisible(false);
                    menu.getItem(2).setVisible(false);
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
            case R.id.actor_menu_action_addmember:
                handleAddMember();
                return true;
            case R.id.actor_menu_action_edit:
                handleEditActor();
                return true;
            case R.id.actor_menu_action_archive:
                handleArchiveActor();
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    private void handleAddMember(){
        Intent intent = new Intent(this, AddMemberActivity.class);
        intent.putExtra("actor_key", actorKey);
        intent.putExtra("actor_name", actorName);
        startActivity(intent);
    }

    private void handleEditActor(){
        showingMenu.getItem(1).setVisible(isEditing);
        showingMenu.getItem(2).setVisible(isEditing);

        EditText actorNameET = (EditText) findViewById(R.id.individualActorName);
        EditText actorDescET = (EditText) findViewById(R.id.individualActorDescription);
        actorNameET.setEnabled(!isEditing);
        actorDescET.setEnabled(!isEditing);

        if(isEditing){
            if(actorNameET.getText().toString().equals("") || actorDescET.getText().toString().equals("")){
                Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
                showingMenu.getItem(1).setVisible(!isEditing);
                showingMenu.getItem(2).setVisible(!isEditing);
                actorNameET.setEnabled(isEditing);
                actorDescET.setEnabled(isEditing);
                return;
            }

            reference.child("actors").child(actorKey).child("name").setValue(actorNameET.getText().toString());
            reference.child("actors").child(actorKey).child("description").setValue(actorDescET.getText().toString());

            ((Toolbar)findViewById(R.id.toolbar)).setTitle(actorNameET.getText().toString());

            actorNameET.setBackgroundResource(android.R.color.transparent);
            actorDescET.setBackgroundResource(android.R.color.transparent);
        }
        else{
            actorNameET.setBackgroundResource(android.R.drawable.edit_text);
            actorDescET.setBackgroundResource(android.R.drawable.edit_text);
        }

        isEditing = !isEditing;
    }

    private void handleArchiveActor(){
        reference.child("actors").child(actorKey).child("active").setValue(false);
        finish();
    }

}
