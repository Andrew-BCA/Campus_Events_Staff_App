package com.example.campuseventsstaff;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ParticipantsAdapter extends RecyclerView.Adapter<ParticipantsAdapter.ViewHolder> {

    private List<Participants> participantsList;

    public ParticipantsAdapter(List<Participants> participantsList) {
        this.participantsList = participantsList;
    }

    // Other methods...

    public void setData(List<Participants> newData) {
        this.participantsList = newData;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_participant, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Participants participant = participantsList.get(position);
        holder.bind(participant);
    }

    @Override
    public int getItemCount() {
        return participantsList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView, emailTextView, deptTextView, eventDeptTextView, eventNameTextView, mobileTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            emailTextView = itemView.findViewById(R.id.emailTextView);
            deptTextView = itemView.findViewById(R.id.deptTextView);
            eventDeptTextView = itemView.findViewById(R.id.eventDeptTextView);
            eventNameTextView = itemView.findViewById(R.id.eventNameTextView);
            mobileTextView = itemView.findViewById(R.id.mobileTextView);
        }

        public void bind(Participants participant) {
            nameTextView.setText("Name: " + participant.getName());
            emailTextView.setText("Roll_No: " + participant.getRoll());
            deptTextView.setText("Department: " + participant.getDept());
            eventDeptTextView.setText("Event Department: " + participant.getEventDept());
            eventNameTextView.setText("Event Name: " + participant.getEventName());
            mobileTextView.setText("Mobile: " + participant.getMobile());
        }
    }
}
