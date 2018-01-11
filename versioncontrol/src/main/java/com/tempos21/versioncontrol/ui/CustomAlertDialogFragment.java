package com.tempos21.versioncontrol.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.tempos21.versioncontrol.R;
import com.tempos21.versioncontrol.model.AlertMessageModel;
import com.tempos21.versioncontrol.service.AlertMessageService;

public class CustomAlertDialogFragment extends DialogFragment {

    public static final int DIALOG_WITHOUT_BUTTONS = 0;
    public static final int DIALOG_WITH_OK_BUTTON = 1;
    public static final int DIALOG_WITH_OK_AND_CANCEL_BUTTONS = 2;
    public static final int DIALOG_WITH_CANCEL_BUTTON = 3;

    private static final String EXTRA_PARCELABLE = "extra_parcelable";

    private AlertMessageService.AlertDialogListener alertDialogListener;

    public CustomAlertDialogFragment() {
        // Empty constructor required for DialogFragment
    }

    /**
     * Creates instance of CustomAlertDialogFragment(use this way instead of constructor)
     *
     * @param alertMessage message model received from response
     * @return instance of dialog fragment
     */
    public static CustomAlertDialogFragment newInstance(AlertMessageModel alertMessage) {
        CustomAlertDialogFragment frag = new CustomAlertDialogFragment();
        Bundle args = new Bundle();
        args.putParcelable(EXTRA_PARCELABLE, alertMessage);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertMessageModel alertMessage = getArguments().getParcelable(EXTRA_PARCELABLE);
        if (alertMessage == null) {
            throw new UnsupportedOperationException("AlertMessageDto could not be null.");
        }
        return createDialog(alertMessage);
    }

    /**
     * Creates AlertDialog based on alert type
     *
     * @param alertMessage message model received from response
     * @return AlertDialog
     */
    private Dialog createDialog(AlertMessageModel alertMessage) {
        int alertType = getAlertType(alertMessage);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity(), R.style.VersionControlCustomAlertDialog);
        alertDialogBuilder.setTitle(alertMessage.getTitle());
        alertDialogBuilder.setMessage(alertMessage.getMessage());
        AlertDialog alertDialog;
        switch (alertType) {
            case DIALOG_WITHOUT_BUTTONS: {
                setCancelable(false);
                alertDialog = alertDialogBuilder.create();
                break;
            }
            case DIALOG_WITH_OK_BUTTON: {
                setCancelable(false);
                setupOkButton(alertMessage, alertDialogBuilder);
                alertDialog = alertDialogBuilder.create();
                setupOkButtonListener(alertMessage, alertDialog);
                break;
            }
            case DIALOG_WITH_CANCEL_BUTTON: {
                setCancelable(false);
                setupCancelButtonListener(alertMessage, alertDialogBuilder);
                alertDialog = alertDialogBuilder.create();
                break;
            }
            case DIALOG_WITH_OK_AND_CANCEL_BUTTONS: {
                setCancelable(false);
                setupOkAndCancelButtonsListeners(alertMessage, alertDialogBuilder);
                alertDialog = alertDialogBuilder.create();
                setupOkButtonListener(alertMessage, alertDialog);
                break;
            }
            default: {
                alertDialog = alertDialogBuilder.create();
                break;
            }
        }
        return alertDialog;
    }

    /**
     * Checks alert type based on quantity of buttons
     *
     * @param alertMessage message model received from response
     * @return integer value of type
     */
    private int getAlertType(AlertMessageModel alertMessage) {
        if (alertMessage.getOkButtonTitle() != null && alertMessage.getCancelButtonTitle() != null) {
            return DIALOG_WITH_OK_AND_CANCEL_BUTTONS;
        } else if (alertMessage.getOkButtonTitle() != null) {
            return DIALOG_WITH_OK_BUTTON;
        } else if (alertMessage.getCancelButtonTitle() != null) {
            return DIALOG_WITH_CANCEL_BUTTON;
        } else {
            return DIALOG_WITHOUT_BUTTONS;
        }
    }

    /**
     * Adds ok button for dialog which is going to be created
     *
     * @param alertMessage       message model
     * @param alertDialogBuilder instance of AlertDialog.Builder
     */
    private void setupOkButton(final AlertMessageModel alertMessage, AlertDialog.Builder alertDialogBuilder) {
        alertDialogBuilder.setPositiveButton(alertMessage.getOkButtonTitle(), null);
    }

    /**
     * Adds ok button listener for dialog which is going to be created and overrides onClickListener to prevent being dismissed
     *
     * @param alertMessage message model
     * @param alertDialog  instance of AlertDialog.Builder
     */
    private void setupOkButtonListener(final AlertMessageModel alertMessage, final AlertDialog alertDialog) {
        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        openUrl(alertMessage);
                    }
                });
            }
        });
    }

    /**
     * Adds ok and cancel buttons listeners for dialog which is going to be created
     *
     * @param alertMessage       message model
     * @param alertDialogBuilder instance of AlertDialog.Builder
     */
    private void setupOkAndCancelButtonsListeners(final AlertMessageModel alertMessage, AlertDialog.Builder alertDialogBuilder) {
        alertDialogBuilder.setPositiveButton(alertMessage.getOkButtonTitle(), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                openUrl(alertMessage);
            }
        });
        alertDialogBuilder.setNegativeButton(alertMessage.getCancelButtonTitle(), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                alertDialogListener.onAlertDialogDismissed();
                dialog.dismiss();
            }
        });
    }

    /**
     * Adds cancel button listener for dialog which is going to be created
     *
     * @param alertMessage       message model
     * @param alertDialogBuilder instance of AlertDialog.Builder
     */
    private void setupCancelButtonListener(final AlertMessageModel alertMessage, AlertDialog.Builder alertDialogBuilder) {
        alertDialogBuilder.setNegativeButton(alertMessage.getCancelButtonTitle(), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                alertDialogListener.onAlertDialogDismissed();
                dialog.dismiss();
            }
        });
    }

    /**
     * Opens url in browser
     *
     * @param alertMessage message model
     */
    private void openUrl(AlertMessageModel alertMessage) {
        String url = alertMessage.getOkButtonActionURL();
        if (url == null) {
            return;
        }
        if (!url.startsWith("https://") && !url.startsWith("http://")) {
            url = "http://" + url;
        }
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(browserIntent);
    }

    public void setAlertDialogListener(AlertMessageService.AlertDialogListener alertDialogListener) {
        this.alertDialogListener = alertDialogListener;
    }
}
