package com.example.mark.fyputable;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;

/*
Ref 2 Recycler View + FS : https://www.youtube.com/watch?v=lAGI6jGS4vs&list=PLrnPJCHvNZuAXdWxOzsN5rgG2M4uJ8bH1&index=3
 */


public class entryAdapter extends FirestoreRecyclerAdapter<Entry, entryAdapter.entryHolder>{

    onItemClickListener listener;

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public entryAdapter(@NonNull FirestoreRecyclerOptions<Entry> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull entryHolder holder, int position, @NonNull Entry model) {
     holder.txtModuleName.setText(model.getModuleName());
     holder.txtLocation.setText(model.getBuilding() +  " " + model.getRoom());
     holder.txtTime.setText(model.getStartTime());
     String debuggingDateProblem = model.getDate();
     holder.txtEnd.setText("Ends at: " + model.getEndTime());

     if (model.getClassType().equals("T")) {

         holder.cView.setCardBackgroundColor(Color.parseColor("#ffe9c6"));

     }
    }

    @NonNull
    @Override
    public entryHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.entry_item, parent, false);
        return new entryHolder(v);
    }

    class entryHolder extends RecyclerView.ViewHolder{
         TextView txtModuleName;
         TextView txtLocation;
         TextView txtTime;
         TextView txtEnd;
         CardView cView;

        public entryHolder(@NonNull View itemView) {
            super(itemView);
            txtModuleName = itemView.findViewById(R.id.text_view_title);
            txtLocation = itemView.findViewById(R.id.text_view_location);
            txtTime = itemView.findViewById(R.id.text_view_time);
            cView = itemView.findViewById(R.id.cardView);
            txtEnd = itemView.findViewById(R.id.text_view_end);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && listener != null){
                      listener.onEntryClick(getSnapshots().getSnapshot(position), position);
                    }

                }
            });

        }
    }

    public interface onItemClickListener {
        void onEntryClick(DocumentSnapshot docSnap, int position);
    }
    public void setOnItemClickListener(onItemClickListener listener){
        this.listener = listener;

    }
}
