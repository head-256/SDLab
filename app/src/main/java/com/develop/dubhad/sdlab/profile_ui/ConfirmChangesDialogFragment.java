package com.develop.dubhad.sdlab.profile_ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import com.develop.dubhad.sdlab.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class ConfirmChangesDialogFragment extends DialogFragment {

    public interface ConfirmChangesDialogListener {
        void onDialogPositiveClick();
        void onDialogNegativeClick();
        void onDialogCancelClick();
    }
    
    private ConfirmChangesDialogListener confirmChangesDialogListener;
    
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        confirmChangesDialogListener = (ConfirmChangesDialogListener) getTargetFragment();
        
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        builder.setTitle(R.string.save_changes_title)
                .setMessage(R.string.save_profile_changes_message)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        confirmChangesDialogListener.onDialogPositiveClick();
                    }
                })
                .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        confirmChangesDialogListener.onDialogNegativeClick();
                    }
                })
                .setNeutralButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        confirmChangesDialogListener.onDialogCancelClick();
                    }
                });
        
        return builder.create();
                
    }
}
