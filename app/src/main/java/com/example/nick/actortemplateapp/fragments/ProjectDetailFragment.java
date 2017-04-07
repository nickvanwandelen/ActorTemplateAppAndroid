package com.example.nick.actortemplateapp.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.nick.actortemplateapp.R;

import domain.Project;

/**
 * Created by Nick on 7-4-2017.
 */

public class ProjectDetailFragment extends Fragment {
    Project project;
    TextView nameTextView, descriptionTextView;

    public ProjectDetailFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_project_detail, container, false);
        nameTextView = (TextView) view.findViewById(R.id.projectNameView);
        descriptionTextView = (TextView) view.findViewById(R.id.projectDescriptionView);

        if(project != null){
            nameTextView.setText(project.getName());
            descriptionTextView.setText(project.getDesrciption());
        }

        return view;
    }

    public void setProject(Project p){
        if(nameTextView == null || descriptionTextView == null){
            return;
        }

        project = p;
        nameTextView.setText(project.getName());
        descriptionTextView.setText(project.getDesrciption());
    }
}
