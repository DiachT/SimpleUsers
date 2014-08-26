package com.diacht.simpleusers.ui.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.diacht.simpleusers.R;
import com.diacht.simpleusers.ui.activity.BaseActivity;
import com.diacht.simpleusers.ui.fragment.BaseFragment;
import com.diacht.simpleusers.utils.InputFormException;

/**
 * InputPasswordDialogFragment
 * @author Tetiana Diachuk (diacht@gmail.com)
 */
public class InputPasswordDialogFragment extends DialogFragment {
    public static final String PASSWORD = "PASSWORD";
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View root = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_input_password,
                null, false);
        builder.setView(root);
        final EditText password = (EditText)root.findViewById(R.id.dialog_password);
        final String passwordReal = getArguments().getString(PASSWORD);
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    InputFormException.assertTrue(password.getText().toString().equals(
                            passwordReal), R.string.error_pass);
                    Fragment fragment = getTargetFragment();
                    if ( fragment != null && (fragment instanceof BaseFragment) ) {
                        Intent intent = new Intent();
                        fragment.onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, intent);
                    }
                } catch (InputFormException e) {
                    ((BaseActivity) getActivity()).ErrorDialog(e.getMessageResource());
                }
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {}
        });
        return builder.create();
    }

    public static InputPasswordDialogFragment newInstance(String passwordReal) {
        InputPasswordDialogFragment dialog = new InputPasswordDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putString(PASSWORD, passwordReal);
        dialog.setArguments(bundle);
        return dialog;
    }
}