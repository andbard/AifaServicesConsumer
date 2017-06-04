package com.andreabardella.aifaservicesconsumer.view;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;

public class DismissOnlyDialog extends DialogFragment {

    protected static final String DIALOG_ID = "DIALOG_ID";
    protected static final String DIALOG_TITLE = "DIALOG_TITLE";
    protected static final String DIALOG_MESSAGE = "DIALOG_MESSAGE";
    protected static final String BUTTON_TEXT = "BUTTON_TEXT";

    private int dialogId;

    public static synchronized DismissOnlyDialog newInstance(int dialogId,
                                                             String title,
                                                             String message,
                                                             String buttonText) {
        DismissOnlyDialog frag = new DismissOnlyDialog();
        Bundle args = new Bundle();
        args.putInt(DIALOG_ID, dialogId);
        args.putString(DIALOG_TITLE, title);
        args.putString(DIALOG_MESSAGE, message);
        args.putString(BUTTON_TEXT, buttonText);

        frag.setArguments(args);

        return frag;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);

        this.dialogId = getArguments().getInt(DIALOG_ID);
        String title = getArguments().getString(DIALOG_TITLE);
        String message = getArguments().getString(DIALOG_MESSAGE);
        String buttonText = getArguments().getString(BUTTON_TEXT);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton(buttonText, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dismiss();
            }
        });

        return builder.create();
    }

}
