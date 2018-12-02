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

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class EditFeedUrlDialogFragment extends DialogFragment {

    @Nullable
    private EditFeedDialogListener editFeedDialogListener;

    private TextInputEditText feedUrlEdit;
    private TextInputLayout feedUrlLayout;
    
    @Nullable
    private String oldUrlValue;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        editFeedDialogListener = (EditFeedDialogListener) getTargetFragment();

        View viewInflated = LayoutInflater.from(requireContext()).inflate(R.layout.edit_feed_url_layout,
                (ViewGroup) getView(), false);

        feedUrlEdit = viewInflated.findViewById(R.id.feed_url);
        feedUrlLayout = viewInflated.findViewById(R.id.feed_url_layout);

        String login = Authentication.getCurrentUser().getLogin();
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences(login, Context.MODE_PRIVATE);
        oldUrlValue = sharedPreferences.getString(getString(R.string.feed_url_key), "");

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

        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setView(viewInflated)
                .setTitle(getString(R.string.edit_feed_url_dialog_title))
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
        Button positiveButton = alertDialog.getButton(Dialog.BUTTON_POSITIVE);
        positiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmUrlEdit(alertDialog);
            }
        });
    }

    @Override
    public void onDestroyView() {
        KeyboardUtil.hideKeyboard(requireActivity());
        super.onDestroyView();
    }

    private void confirmUrlEdit(@NonNull AlertDialog alertDialog) {
        feedUrlLayout.setError(null);

        String input = String.valueOf(feedUrlEdit.getText());
        if (!input.startsWith("http://") && !input.startsWith("https://")) {
            input = "https://".concat(input);
        }
        if (!Patterns.WEB_URL.matcher(input).matches()) {
            feedUrlLayout.setError(getString(R.string.invalid_url_error));
        } else {
            KeyboardUtil.hideKeyboard(requireActivity(), feedUrlEdit);

            if (!Objects.requireNonNull(oldUrlValue).equals(input)) {
                FeedCacheManager.clearCache(requireActivity());
            }

            Objects.requireNonNull(editFeedDialogListener).onDialogPositiveClick(input);
            alertDialog.dismiss();
        }
    }

    public interface EditFeedDialogListener {
        void onDialogPositiveClick(String feedUrl);
    }
}
