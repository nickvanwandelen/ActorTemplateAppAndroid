package com.example.nick.actortemplateapp.fragments;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.nick.actortemplateapp.R;

import domain.Project;

/**
 * Created by Nick on 7-4-2017.
 */

public class ProjectMasterFragment extends Fragment {
    private RecyclerView recyclerView;
    private ProjectAdapter adapter;
    private OnFragmentInteractionListener listener;

    public ProjectMasterFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_project_master, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        adapter = new ProjectAdapter(listener);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listener = (OnFragmentInteractionListener) context;
    }

    public interface OnFragmentInteractionListener{
        void onFragmentInteraction(Project project, String key);
    }
}
