package com.example.nick.actortemplateapp;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import domain.Project;

import static com.example.nick.actortemplateapp.R.layout.activity_main;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;

    private FirebaseDatabase database;
    private DatabaseReference reference;

    private ArrayList<Project> projects;

    public MainActivity(){
        projects = new ArrayList<Project>();



        database = FirebaseDatabase.getInstance();
        reference = database.getReference();

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.e("Count", "Amount of projects: " + dataSnapshot.getChildrenCount());

                projects.clear();

                for(DataSnapshot postSnapshot: dataSnapshot.getChildren()){
                    Project retrievedProject = postSnapshot.getValue(Project.class);
                    projects.add(retrievedProject);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("Reading Firebase", databaseError.getMessage());
            }
        });


    }

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

        ((Toolbar)findViewById(R.id.toolbar)).setTitle("Actor Template App");

        createProjectButtons();
    }

    private void createProjectButtons(){
        for(Project project: projects){
            Button button = new Button(this);
            button.setOnClickListener(this);
            button.setText(project.getName());
            button.setBackgroundColor(Color.CYAN);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            int margin = (int) (20 * this.getResources().getDisplayMetrics().density);
            params.setMargins(margin, margin, margin, margin);
            button.setLayoutParams(params);


            LinearLayout projectButtons = (LinearLayout)this.findViewById(R.id.allProjects);
            projectButtons.addView(button);
        }
    }

    private void getSavedProjects(){
        List<Project> allProjects = new ArrayList<Project>();





    }

    @Override
    public void onClick(View view){
        Intent navigateToProject = new Intent(this, ShowProjectActivity.class);
        startActivity(navigateToProject);
    }

}
