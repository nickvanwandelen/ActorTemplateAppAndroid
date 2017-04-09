package adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.nick.actortemplateapp.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.FirebaseDatabase;

import domain.Actor;

/**
 * Created by Nick on 8-4-2017.
 */

public class ActorAdapter extends FirebaseRecyclerAdapter<Actor, ActorAdapter.MyViewHolder>{

    public ActorAdapter(String projectKey){
        super(Actor.class, R.layout.actor_row, MyViewHolder.class, FirebaseDatabase.getInstance().getReference().child("actors").child("projects").equalTo(projectKey));
    }

    @Override
    protected void populateViewHolder(MyViewHolder viewHolder, Actor actor, int position){
        viewHolder.actorName.setText(actor.getName());
        viewHolder.actorDescription.setText(actor.getDescription());
        viewHolder.actor = actor;
        viewHolder.actorKey = getRef(position).getKey();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView actorName, actorDescription;
        public Actor actor;
        public String actorKey;

        public MyViewHolder(View view){
            super(view);
            actorName = (TextView) view.findViewById(R.id.actorName);
            actorDescription = (TextView) view.findViewById(R.id.actorDescription);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view){

        }
    }
}
