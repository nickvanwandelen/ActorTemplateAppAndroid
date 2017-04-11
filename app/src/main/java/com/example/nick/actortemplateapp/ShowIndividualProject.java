package com.example.nick.actortemplateapp;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

import adapter.ActorAdapter;
import domain.Project;

public class ShowIndividualProject extends AppCompatActivity {

    private String projectKey;
    private Project showingProject;

    private DatabaseReference reference;

    private ActorAdapter adapter;

    private RecyclerView recyclerView;

    private Menu showingMenu;
    private boolean isEditing = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_individual_project);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        reference = FirebaseDatabase.getInstance().getReference();

        Intent retrievedIntent = getIntent();
        projectKey = retrievedIntent.getStringExtra("project_key");

        reference.child("projects").child(projectKey).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                showingProject = dataSnapshot.getValue(Project.class); //retrieve selected project object
                if(showingProject == null){ //validate if selected project exists, if not: return and show error
                    Toast.makeText(getApplicationContext(), "Error reading Firebase database: Project not found", Toast.LENGTH_SHORT).show();
                    return;
                }

                EditText projectNameET = (EditText) findViewById(R.id.individualProjectName);
                projectNameET.setText(showingProject.getName());
                projectNameET.setEnabled(false);

                EditText projectDescriptionET = (EditText) findViewById(R.id.individualProjectDescription);
                projectDescriptionET.setText(showingProject.getDescription());
                projectDescriptionET.setEnabled(false);

                ((Toolbar)findViewById(R.id.toolbar)).setTitle("Project: " + showingProject.getName());

                adapter = new ActorAdapter(projectKey);

                recyclerView = (RecyclerView) findViewById(R.id.actorRecyclerView);
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
        getMenuInflater().inflate(R.menu.individual_project_menu, menu);
        showingMenu = menu;
        String loggedInUserUid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        reference.child("users").child(loggedInUserUid).child("isAnalist").addListenerForSingleValueEvent(new ValueEventListener() { //Validate that logged in user is Analist, if not: disable addActor and archiveProject buttons
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
            case R.id.action_addactor:
                handleAddActor();
                return true;
            case R.id.action_edit:
                handleEditProject();
                return true;
            case R.id.action_archive:
                handleArchiveProject();
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    private void handleAddActor(){
        Intent intent = new Intent(this, AddActorActivity.class);
        intent.putExtra("project_key", projectKey);
        startActivity(intent);
    }

    private void handleEditProject(){
        showingMenu.getItem(1).setVisible(isEditing);
        showingMenu.getItem(2).setVisible(isEditing);

        EditText projectNameET = (EditText) findViewById(R.id.individualProjectName);
        EditText projectDescET = (EditText) findViewById(R.id.individualProjectDescription);
        projectNameET.setEnabled(!isEditing);
        projectDescET.setEnabled(!isEditing);

        if(isEditing){
            reference.child("projects").child(projectKey).child("name").setValue(projectNameET.getText().toString());
            reference.child("projects").child(projectKey).child("description").setValue(projectDescET.getText().toString());
        }

        isEditing = !isEditing;
    }

    private void handleArchiveProject(){
        reference.child("projects").child(projectKey).child("active").setValue(false);
        finish();
    }



}
