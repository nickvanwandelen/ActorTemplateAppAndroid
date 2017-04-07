package com.example.nick.actortemplateapp;

import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.nick.actortemplateapp.fragments.ProjectDetailFragment;
import com.example.nick.actortemplateapp.fragments.ProjectMasterFragment;
import com.example.nick.actortemplateapp.fragments.ProjectMasterFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import domain.Project;

import static com.example.nick.actortemplateapp.R.layout.activity_main;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, ProjectMasterFragment.OnFragmentInteractionListener{

    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;

    private DatabaseReference reference;

    private ArrayList<Project> projects;

    private ProjectDetailFragment detailFragment;
    private String showingProjectKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(activity_main);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        if(mFirebaseUser == null){
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }

        ((Toolbar)findViewById(R.id.toolbar)).setTitle(R.string.your_projects);

        detailFragment = (ProjectDetailFragment)getSupportFragmentManager().findFragmentById(R.id.details_frag);
        /*
        projects = new ArrayList<Project>();
        reference = FirebaseDatabase.getInstance().getReference("Users");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("MainActivity:", "Retrieving projects from Firebase");
                DataSnapshot projectSnapshot = dataSnapshot.child("Projects");
                Iterable<DataSnapshot> projectsOfUser = projectSnapshot.getChildren();

                for(DataSnapshot retrievedProject : projectsOfUser){
                    projects.add((Project)retrievedProject.getValue(Project.class));
                    Log.d("Firebase:", "Added project");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("Reading Firebase", databaseError.getMessage());
            }
        });

        Log.d("Count:", "Retrieved projects: " + projects.size());

        */
    }


    @Override
    public void onClick(View view){
        Intent navigateToProject = new Intent(this, ShowProjectActivity.class);
        startActivity(navigateToProject);
    }


    @Override
    public void onFragmentInteraction(Project project, String key) {
        detailFragment.setProject(project);
        showingProjectKey = key;
    }
}
