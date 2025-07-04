package com.ahmadrezagh671.authdemoapp.Utilities;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.ahmadrezagh671.authdemoapp.R;

public class DialogUtil {

    public static void editNameDialog(Activity activity, EditNameDialogCallback editNameDialogCallback){
        LayoutInflater inflater = activity.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_edit_name, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setView(dialogView);
        builder.setCancelable(true);

        Button saveBTN = dialogView.findViewById(R.id.saveBTN);
        Button cancelBTN = dialogView.findViewById(R.id.cancelBTN);
        EditText editText = dialogView.findViewById(R.id.editText);
        editText.requestFocus();
        ProgressBar progressBar = dialogView.findViewById(R.id.progressBar);

        AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        dialog.show();

        saveBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editText.getText().toString().isEmpty()){
                    editText.setError("Please enter your name");
                }else {
                    saveBTN.setVisibility(INVISIBLE);
                    progressBar.setVisibility(VISIBLE);
                    editNameDialogCallback.onSubmit(editText.getText().toString(),dialog);
                }
            }
        });

        cancelBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editNameDialogCallback.onCancel(dialog);
            }
        });
    }
    public interface EditNameDialogCallback {
        void onSubmit(String input,AlertDialog dialog);
        void onCancel(AlertDialog dialog);
    }
}
