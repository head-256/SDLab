package com.develop.dubhad.sdlab.rss_ui;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.develop.dubhad.sdlab.R;
import com.develop.dubhad.sdlab.authentication.Authentication;
import com.develop.dubhad.sdlab.rss.FeedCacheManager;
import com.develop.dubhad.sdlab.util.KeyboardUtil;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class EditFeedUrlDialogFragment extends DialogFragment {

    public interface EditFeedDialogListener {
        void onDialogPositiveClick(String feedUrl);
    }
    
    private EditFeedDialogListener editFeedDialogListener;
    
    private TextInputEditText feedUrlEdit;
    private TextInputLayout feedUrlLayout;
    
    private String oldUrlValue;
    
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        editFeedDialogListener = (EditFeedDialogListener) getTargetFragment(); 
        
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());

        View viewInflated = LayoutInflater.from(requireContext()).inflate(R.layout.edit_feed_url_layout, 
                (ViewGroup) getView(), false);
        
        feedUrlEdit = viewInflated.findViewById(R.id.feed_url);
        String login = Authentication.getCurrentUser().getLogin();
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences(login, Context.MODE_PRIVATE);
        oldUrlValue = sharedPreferences.getString("feedUrl", "");
        feedUrlEdit.setText(oldUrlValue);
        feedUrlEdit.setSelectAllOnFocus(true);
        feedUrlEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    KeyboardUtil.showKeyboard(requireActivity());
                }
            }
        });
        feedUrlLayout = viewInflated.findViewById(R.id.feed_url_layout);
        builder.setView(viewInflated);
        
        builder.setTitle("Enter valid feed URL")
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
        
        return builder.create();
    }

    @Override
    public void onResume() {
        super.onResume();
        
        final AlertDialog alertDialog = (AlertDialog) getDialog();
        
        if (alertDialog != null) {
            Button positiveButton = alertDialog.getButton(Dialog.BUTTON_POSITIVE);
            positiveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    feedUrlLayout.setError(null);
                    String input = feedUrlEdit.getText().toString();
                    if (!input.startsWith("http://") && !input.startsWith("https://")) {
                        input = "https://".concat(input);
                    }
                    if (!Patterns.WEB_URL.matcher(input).matches()) {
                        feedUrlLayout.setError("Invalid URL");
                    }
                    else {
                        KeyboardUtil.hideKeyboard(requireActivity(), feedUrlEdit);
                        if (!oldUrlValue.equals(input)) {
                            FeedCacheManager.clearCache(requireActivity());
                        }
                        editFeedDialogListener.onDialogPositiveClick(input);
                        alertDialog.dismiss();
                    }
                }
            });
        }
    }

    @Override
    public void onDestroyView() {
        KeyboardUtil.hideKeyboard(requireActivity());
        super.onDestroyView();
    }
}
