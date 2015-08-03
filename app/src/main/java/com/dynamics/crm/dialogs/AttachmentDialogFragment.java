package com.dynamics.crm.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;

import com.dynamics.crm.R;

public class AttachmentDialogFragment extends android.support.v4.app.DialogFragment {

    Communicator communicator;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getActivity().getString(R.string.attach)).setItems(R.array.attachment_type, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                // The 'which' argument contains the index position
                // of the selected item

                // communicate to calling activity according to selected type
                if (which == 0) {

                    communicator.callback("camera");
                } else if (which == 1) {
                    communicator.callback("signature");
                }
            }
        });
        return builder.create();
    }

    public void setCommunicator(Communicator communicator) {
        this.communicator = communicator;
    }

    public interface Communicator {
        void callback(String action);
    }
}
