package adapter;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Adapter;
import android.widget.TextView;

import com.example.nick.actortemplateapp.R;
import com.example.nick.actortemplateapp.ShowIndividualMemberActivity;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.FirebaseDatabase;

import domain.Actor;
import domain.Member;

/**
 * Created by Nick on 10-4-2017.
 */

public class MemberAdapter extends FirebaseRecyclerAdapter<Member, MemberAdapter.ViewHolder> {

    public MemberAdapter(String projectKey){
        super(Member.class, R.layout.member_row, MemberAdapter.ViewHolder.class, FirebaseDatabase.getInstance().getReference().child("members"));
    }

    @Override
    protected void populateViewHolder(ViewHolder viewHolder, Member model, int position) {
        viewHolder.memberName.setText(model.getName());
        viewHolder.memberRole.setText(model.getRole());
        viewHolder.member = model;
        viewHolder.memberKey = getRef(position).getKey();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView memberName, memberRole;
        public Member member;
        public String memberKey;

        public ViewHolder(View view){
            super(view);
            memberName = (TextView) view.findViewById(R.id.member_recyclerview_memberName);
            memberRole = (TextView) view.findViewById(R.id.member_recyclerview_memberRole);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view){
            Intent intent = new Intent(view.getContext(), ShowIndividualMemberActivity.class);
            intent.putExtra("member_key", memberKey);
            view.getContext().startActivity(intent);
        }
    }
}
