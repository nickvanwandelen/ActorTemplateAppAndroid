package com.example.nick.actortemplateapp.fragments;

import android.support.annotation.Keep;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.TextView;

import com.example.nick.actortemplateapp.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;

import domain.Project;

/**
 * Created by Nick on 7-4-2017.
 */

public class ProjectAdapter extends FirebaseRecyclerAdapter<Project, ProjectAdapter.ProjectViewHolder> {

    static ProjectMasterFragment.OnFragmentInteractionListener theListener;

    public ProjectAdapter(ProjectMasterFragment.OnFragmentInteractionListener listener) {
        super(Project.class, R.layout.project_row, ProjectAdapter.ProjectViewHolder.class,
                FirebaseDatabase.getInstance().getReference().child("Users").child("Projects"));
        theListener = listener;
    }


    @Override
    protected void populateViewHolder(ProjectViewHolder viewHolder, Project model, int position) {
        viewHolder.projectName.setText(model.getName());
        viewHolder.projectDescription.setText(model.getDesrciption());
        viewHolder.project = model;
        viewHolder.key = getRef(position).getKey();
    }

    public static class ProjectViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView projectName, projectDescription;
        public Project project;
        public String key;

        public ProjectViewHolder(View view) {
            super(view);
            projectName = (TextView) view.findViewById(R.id.projectName);
            projectDescription = (TextView) view.findViewById(R.id.projectDescription);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            theListener.onFragmentInteraction(project, key);
        }
    }
}
