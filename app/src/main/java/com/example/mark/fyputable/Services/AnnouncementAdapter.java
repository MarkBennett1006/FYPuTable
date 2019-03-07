package com.example.mark.fyputable.Services;

import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mark.fyputable.Objects.Announcement;
import com.example.mark.fyputable.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;


/*
Ref 2 Recycler View + FS : https://www.youtube.com/watch?v=lAGI6jGS4vs&list=PLrnPJCHvNZuAXdWxOzsN5rgG2M4uJ8bH1&index=3

Explaination for all of the code for this adapter comes from the above video!
 */

public class AnnouncementAdapter extends FirestoreRecyclerAdapter<Announcement, AnnouncementAdapter.announceHolder> {



    onItemClickListener listener;
    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public AnnouncementAdapter(@NonNull FirestoreRecyclerOptions options) {
        super(options);
    }



    @Override
    protected void onBindViewHolder(@NonNull announceHolder holder, int position, @NonNull Announcement model) {

        holder.txtTitle.setText(model.getTitle());
        holder.txtContent.setText(model.getMsgContent());
        holder.txtTime.setText(model.getDate());

    }

    @NonNull
    @Override
    public announceHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.announce_item, viewGroup, false);
        return new announceHolder(v);
    }



    class announceHolder extends RecyclerView.ViewHolder{

        TextView txtTitle;
        TextView txtContent;
        TextView txtTime;
        CardView cView;

        public announceHolder(@NonNull View itemView) {
            super(itemView);

            txtTitle = itemView.findViewById(R.id.text_view_announcetitle);
            txtContent = itemView.findViewById(R.id.text_view_content);
            txtTime = itemView.findViewById(R.id.text_view_timestamp);
            cView = itemView.findViewById(R.id.announceCard);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && listener != null){
                        listener.onAnnounceClick(getSnapshots().getSnapshot(position), position);
                    }

                }
            });
        }
    }

    public interface onItemClickListener {
        void onAnnounceClick(DocumentSnapshot docSnap, int position);
    }
    public void setOnItemClickListener(AnnouncementAdapter.onItemClickListener listener){
        this.listener = listener;

    }



}
