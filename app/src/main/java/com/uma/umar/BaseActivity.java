package com.uma.umar;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;

import com.uma.umar.ui.UmARInitActivity;

/**
 * Created by danieh on 6/26/17.
 */

public class BaseActivity extends AppCompatActivity {

    public void showDialogInformationException() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.app_name)
                .setMessage(R.string.information_exception)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        UmARInitActivity.restartApp(BaseActivity.this);
                    }
                })
                .show();
    }

    public void showDialogNoInternet() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.app_name)
                .setMessage(R.string.need_internet)
                .setPositiveButton(R.string.ok, null)
                .show();
    }

}
