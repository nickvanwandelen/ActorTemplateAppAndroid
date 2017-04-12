package adapter;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.nick.actortemplateapp.R;
import com.example.nick.actortemplateapp.ShowIndividualActorActivity;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.FirebaseDatabase;

import domain.Actor;

/**
 * Created by Nick on 8-4-2017.
 */

public class ActorAdapter extends FirebaseRecyclerAdapter<Actor, ActorAdapter.MyViewHolder>{

    private String nameOfProject;

    public ActorAdapter(String projectKey, String projectName){
        super(Actor.class, R.layout.actor_row, MyViewHolder.class, FirebaseDatabase.getInstance().getReference().child("actors").orderByChild("projectID").equalTo(Integer.parseInt(projectKey)));
        nameOfProject = projectName;
    }

    @Override
    protected void populateViewHolder(MyViewHolder viewHolder, Actor actor, int position){
        viewHolder.actorName.setText(actor.getName());
        viewHolder.actorDescription.setText(actor.getDescription());
        viewHolder.actor = actor;
        viewHolder.actorKey = getRef(position).getKey();
        viewHolder.projectName = nameOfProject;
        viewHolder.checkActive();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView actorName, actorDescription;
        public Actor actor;
        public String actorKey;
        public String projectName;

        public View actorView;

        public MyViewHolder(View view){
            super(view);
            actorView = view;
            actorName = (TextView) view.findViewById(R.id.actorName);
            actorDescription = (TextView) view.findViewById(R.id.actorDescription);
            view.setOnClickListener(this);
        }

        public void checkActive(){ //check if actor is active, if false: make view invisible
            if(!actor.isActive()){
                actorView.getLayoutParams().height = 0;
            }
        }

        @Override
        public void onClick(View view){
            Intent intent = new Intent(view.getContext(), ShowIndividualActorActivity.class);
            intent.putExtra("actor_key", actorKey);
            intent.putExtra("project_name", projectName);
            view.getContext().startActivity(intent);
        }
    }
}
