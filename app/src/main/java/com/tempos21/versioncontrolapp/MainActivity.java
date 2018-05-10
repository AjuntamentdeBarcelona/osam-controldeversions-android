package com.tempos21.versioncontrolapp;

import com.tempos21.versioncontrol.service.AlertMessageService;
import com.tempos21.versioncontrol.ui.CustomAlertDialogFragment;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import static com.tempos21.versioncontrol.ui.GDPRDialogFragment.NO_CUSTOM_TAB;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String LANGUAGE_TEST = "cat";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.button_main_activity).setOnClickListener(this);
        findViewById(R.id.button1_main_activity).setOnClickListener(this);
        findViewById(R.id.button2_main_activity).setOnClickListener(this);
        findViewById(R.id.button3_main_activity).setOnClickListener(this);
        findViewById(R.id.button4_main_activity).setOnClickListener(this);
        findViewById(R.id.button5_main_activity).setOnClickListener(this);
        findViewById(R.id.button6_main_activity).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_main_activity: {
                showMessageDialog();
                break;
            }
            case R.id.button1_main_activity: {
                showMockMessageDialog(CustomAlertDialogFragment.DIALOG_WITH_OK_BUTTON);
                break;
            }
            case R.id.button2_main_activity: {
                showMockMessageDialog(CustomAlertDialogFragment.DIALOG_WITH_OK_AND_CANCEL_BUTTONS);
                break;
            }
            case R.id.button3_main_activity: {
                showMockMessageDialog(CustomAlertDialogFragment.DIALOG_WITHOUT_BUTTONS);
                break;
            }
            case R.id.button4_main_activity: {
                showMockMessageDialog(CustomAlertDialogFragment.DIALOG_WITH_CANCEL_BUTTON);
                break;
            }
            case R.id.button5_main_activity: {
                showMessageDialogJsonFile();
                break;
            }
            case R.id.button6_main_activity: {
                showGdprDialog();
                break;
            }
        }
    }

    private void showMockMessageDialog(int dialogType) {
        AlertMessageService.showMockMessageDialog(this, dialogType, LANGUAGE_TEST, new AlertMessageService.AlertDialogListener() {
            @Override
            public void onFailure(final Exception e) {
                e.printStackTrace();
                Toast.makeText(MainActivity.this, "Network error " + e.getMessage(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onSuccess(boolean updateNeeded) {

            }

            @Override
            public void onAlertDialogDismissed() {

            }
        });
    }

    private void showMessageDialogJsonFile() {
        AlertMessageService.showMessageDialogJsonFile(this,
                LANGUAGE_TEST, new AlertMessageService.AlertDialogListener() {
                    @Override
                    public void onFailure(final Exception e) {
                        e.printStackTrace();
                        Toast.makeText(MainActivity.this, "Network error " + e.getMessage(), Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onSuccess(boolean updateNeeded) {

                    }

                    @Override
                    public void onAlertDialogDismissed() {

                    }
                });
    }

    private void showMessageDialog() {
        AlertMessageService.showMessageDialog(this,
                "http://www.bcn.cat/mobil/apps/controlVersions/bustiaciutadana/versionControl_android.json",
                LANGUAGE_TEST, new AlertMessageService.AlertDialogListener() {
                    @Override
                    public void onFailure(final Exception e) {
                        e.printStackTrace();
                        Toast.makeText(MainActivity.this, "Network error " + e.getMessage(), Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onSuccess(boolean updateNeeded) {

                    }

                    @Override
                    public void onAlertDialogDismissed() {

                    }
                });
    }

    private void showGdprDialog() {
        AlertMessageService.startGdpr(this,
                "https://api.myjson.com/bins/vu9ie",
                "Aceptar",
                NO_CUSTOM_TAB,
                new AlertMessageService.GdprListener() {
                    @Override
                    public void onFailure(Exception e) {
                        Toast.makeText(MainActivity.this, "Error " + e.getMessage(), Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onGdprAccepted() {
                        Toast.makeText(MainActivity.this, "GDPR Accepted!! :) ", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onGdprDismissed() {
                        Toast.makeText(MainActivity.this, "Dismissed", Toast.LENGTH_LONG).show();
                    }
                });
    }
}
