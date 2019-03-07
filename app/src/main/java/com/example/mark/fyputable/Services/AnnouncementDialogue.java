package com.example.mark.fyputable.Services;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.example.mark.fyputable.Objects.Announcement;
import com.example.mark.fyputable.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;



/*


   ref 1: Custom Dialogue: https://www.youtube.com/watch?v=ARezg1D9Zd0&list=PLrnPJCHvNZuBkhcesO6DfdCghl6ZejVPc&index=5

  ref 2: Underline text: https://stackoverflow.com/questions/33753346/how-to-give-a-line-below-the-textview-in-android




 */



// ****** All of the code from this class comes from Reference 1 *******

public class AnnouncementDialogue extends AppCompatDialogFragment {



    TextView txtAnnounceTitle;
    TextView txtAnnounceDate;
    TextView txtAnnounceContent;
    TextView txtAnnounceName;
    String path;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    DialogInterface.OnShowListener listener;




    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        path = getArguments().getString("announcepath");

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.announcement_card, null);

        builder.setView(view).setTitle("Announcement")
                .setNegativeButton("", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {



                    }
                });

        txtAnnounceContent = view.findViewById(R.id.txtAnnounceContent);
        txtAnnounceTitle = view.findViewById(R.id.txtAnnounceTitleDetail);
        txtAnnounceDate = view.findViewById(R.id.txtAnnounceTime);
        txtAnnounceName = view.findViewById(R.id.txtAnnounceAuthor);

        txtAnnounceTitle.setText("");
        txtAnnounceDate.setText("");
        txtAnnounceName.setText("");
        txtAnnounceContent.setText("");


        //ref 1
        txtAnnounceContent.setPaintFlags(txtAnnounceContent.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);
        txtAnnounceName.setPaintFlags(txtAnnounceContent.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);
        txtAnnounceDate.setPaintFlags(txtAnnounceContent.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);


        db.document(path).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
               Announcement currentAnnouncement = documentSnapshot.toObject(Announcement.class);

                txtAnnounceContent.setText(currentAnnouncement.getMsgContent());
                txtAnnounceTitle.setText(currentAnnouncement.getTitle());
                txtAnnounceName.setText(currentAnnouncement.getAuthName());
                txtAnnounceDate.setText(currentAnnouncement.getDate());


            }
        });

        return builder.create();


    }





}
