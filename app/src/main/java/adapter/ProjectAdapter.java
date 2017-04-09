package adapter;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Adapter;
import android.widget.TextView;

import com.example.nick.actortemplateapp.R;
import com.example.nick.actortemplateapp.ShowIndividualProject;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.FirebaseDatabase;

import domain.Project;

/**
 * Created by Nick on 8-4-2017.
 */

public class ProjectAdapter extends FirebaseRecyclerAdapter<Project, ProjectAdapter.ViewHolder> {

    public ProjectAdapter(){
        super(Project.class, R.layout.project_row, ProjectAdapter.ViewHolder.class, FirebaseDatabase.getInstance().getReference().child("projects").orderByChild("active").equalTo(true));
    }

    @Override
    protected void populateViewHolder(ViewHolder viewHolder, Project project, int position){
        viewHolder.name.setText(project.getName());
        viewHolder.description.setText(project.getDescription());
        viewHolder.project = project;
        viewHolder.key = getRef(position).getKey();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public TextView name, description;
        public Project project;
        public String key;

        public ViewHolder(View view){
            super(view);
            name = (TextView) view.findViewById(R.id.projectName);
            description = (TextView) view.findViewById(R.id.projectDescription);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view){
            Intent intent = new Intent(view.getContext(), ShowIndividualProject.class);
            intent.putExtra("project_key", key);
            view.getContext().startActivity(intent);
        }
    }
}
