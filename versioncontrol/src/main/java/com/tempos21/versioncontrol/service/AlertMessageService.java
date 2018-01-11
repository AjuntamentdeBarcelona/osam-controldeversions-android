package com.tempos21.versioncontrol.service;

import com.google.gson.Gson;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.tempos21.versioncontrol.R;
import com.tempos21.versioncontrol.mapper.AlertMessageMapper;
import com.tempos21.versioncontrol.model.AlertMessageDto;
import com.tempos21.versioncontrol.model.AlertMessageModel;
import com.tempos21.versioncontrol.ui.CustomAlertDialogFragment;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;

import java.io.IOException;
import java.io.InputStream;

public final class AlertMessageService {

    private static final int COMPARISON_MODE_LT = 0;

    private static final int COMPARISON_MODE_EQ = 1;

    private static final int COMPARISON_MODE_GT = 2;

    private static final int COMPARISON_RESULT_LT = -1;

    private static final int COMPARISON_RESULT_EQ = 0;

    private static final int COMPARISON_RESULT_GT = 1;

    private AlertMessageService() {
    }

    /**
     * Creates and displays a alert dialog if it meets the required criteria
     *
     * @param context  the context of Activity
     * @param endpoint the url of web service API
     * @param language idiom of alert
     */
    public static void showMessageDialog(Context context, String endpoint, String language, AlertDialogListener listener) {
        if (context == null || !(context instanceof Activity)) {
            throw new IllegalArgumentException("Context must be instance of Activity.");
        }
        if (endpoint == null || endpoint.isEmpty()) {
            throw new IllegalArgumentException("Endpoint could not be null or empty");
        }
        String appVersionName = getAppVersionName(context);
        if (!TextUtils.isDigitsOnly(appVersionName.replace(".", ""))) {
            AlertMessageModel alertMessage = new AlertMessageModel();
            alertMessage.setTitle(context.getString(R.string.error_wrong_version_name_title));
            alertMessage.setMessage(context.getString(R.string.error_wrong_version_name_desc));
            alertMessage.setCancelButtonTitle(context.getString(R.string.error_wrong_version_name_cancel));
            showVersionControlPopUp(context, listener, alertMessage);
        } else {
            init(context, endpoint, language, listener);
        }
    }

    /**
     * Creates and displays a alert dialog if it meets the required criteria
     *
     * @param context  the context of Activity
     * @param language idiom of alert
     */
    public static void showMessageDialogJsonFile(Context context, String language, AlertDialogListener listener) {
        if (context == null || !(context instanceof Activity)) {
            throw new IllegalArgumentException("Context must be instance of Activity.");
        }
        String appVersionName = getAppVersionName(context);
        if (!TextUtils.isDigitsOnly(appVersionName.replace(".", ""))) {
            AlertMessageModel alertMessage = new AlertMessageModel();
            alertMessage.setTitle(context.getString(R.string.error_wrong_version_name_title));
            alertMessage.setMessage(context.getString(R.string.error_wrong_version_name_desc));
            alertMessage.setCancelButtonTitle(context.getString(R.string.error_wrong_version_name_cancel));
            showVersionControlPopUp(context, listener, alertMessage);
        } else {
            jsonFileInit(context, language, listener);
        }
    }

    private static void showVersionControlPopUp(Context context, AlertDialogListener listener, AlertMessageModel alertMessage) {
        if (context instanceof AppCompatActivity) {
            android.support.v4.app.FragmentManager fm = ((AppCompatActivity) context).getSupportFragmentManager();
            com.tempos21.versioncontrol.ui.v4.CustomAlertDialogFragment alertDialog =
                    com.tempos21.versioncontrol.ui.v4.CustomAlertDialogFragment.newInstance(alertMessage);
            alertDialog.setAlertDialogListener(listener);
            if (listener != null) {
                alertDialog.setAlertDialogListener(listener);
            }
            alertDialog.show(fm, "fragment_alert");
        } else {
            FragmentManager fm = ((Activity) context).getFragmentManager();
            CustomAlertDialogFragment alertDialog = CustomAlertDialogFragment.newInstance(alertMessage);
            alertDialog.setAlertDialogListener(listener);
            if (listener != null) {
                alertDialog.setAlertDialogListener(listener);
            }
            alertDialog.show(fm, "fragment_alert");
        }
    }

    /**
     * Determines should message be shown based on application version name and comparison mode
     *
     * @param alertMessage   message model received from response
     * @param appVersionName current version name installed
     *
     * @return true/false based on comparisonMode
     */
    private static boolean shouldMessageBeShown(AlertMessageModel alertMessage, String appVersionName) {
        if (alertMessage.getMinSystemVersion() != null) {
            if (compareVersionNames(alertMessage.getMinSystemVersion(), Build.VERSION.RELEASE) == COMPARISON_RESULT_GT) {
                return false;
            }
        }
        String receivedVersion = alertMessage.getVersion();
        Integer comparisonMode = alertMessage.getComparisonMode();

        if (receivedVersion == null || comparisonMode == null || appVersionName == null) {
            return false;
        }

        if (!receivedVersion.matches("[0-9]+(\\.[0-9]+)*")) {
            throw new IllegalArgumentException("Invalid version format(should be digits and dots, for example 1.2.3).");
        }

        switch (comparisonMode) {
            case COMPARISON_MODE_LT: {
                return compareVersionNames(appVersionName, receivedVersion) == COMPARISON_RESULT_LT;
            }
            case COMPARISON_MODE_EQ: {
                return compareVersionNames(appVersionName, receivedVersion) == COMPARISON_RESULT_EQ;
            }
            case COMPARISON_MODE_GT: {
                return compareVersionNames(appVersionName, receivedVersion) == COMPARISON_RESULT_GT;
            }
        }

        return false;
    }

    /**
     * Gets application version name
     *
     * @param context the context of Activity
     *
     * @return version number or null if it's not present
     */
    private static String getAppVersionName(Context context) {
        String versionName = null;
        PackageInfo packageInfo;
        try {
            packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            versionName = packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionName;
    }

    /**
     * Compares two String version names
     *
     * @param appVersionName      application version name
     * @param receivedVersionName received version name
     *
     * @return integer value based on comparison(-1 for lt, 0 for eq, 1 for gt)
     */
    static int compareVersionNames(String appVersionName, String receivedVersionName) {
        String[] appVersionParts = appVersionName.split("\\.");
        String[] receivedVersionParts = receivedVersionName.split("\\.");

        int length = Math.max(appVersionParts.length, receivedVersionParts.length);

        for (int i = 0; i < length; i++) {
            String appVersionPart = i < appVersionParts.length ? appVersionParts[i] : "0";
            String receivedVersionPart = i < receivedVersionParts.length ? receivedVersionParts[i] : "0";
            if (appVersionPart.compareTo(receivedVersionPart) < 0) {
                return COMPARISON_RESULT_LT;
            }
            if (appVersionPart.compareTo(receivedVersionPart) > 0) {
                return COMPARISON_RESULT_GT;
            }
        }
        return COMPARISON_RESULT_EQ;
    }

    /**
     * Init method which makes get call to remote web service
     *
     * @param context  the context of Activity
     * @param endpoint the url of the web service API
     */
    private static void init(final Context context, final String endpoint, final String language,
            final AlertDialogListener listener) {
        try {
            final OkHttpClient client = new OkHttpClient();
            final Request request = new Request.Builder().url(endpoint).build();
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {
                    runOnUiThread(e, listener);
                }

                @Override
                public void onResponse(final Response response) throws IOException {
                    if (!response.isSuccessful()) {
                        runOnUiThread(new IOException("Unexpected code " + response), listener);
                    } else {
                        AlertMessageDto alertMessageDto;
                        try {
                            alertMessageDto = new Gson().fromJson(response.body().string(), AlertMessageDto.class);
                        } catch (Exception e) {
                            runOnUiThread(e, listener);
                            return;
                        }
                        AlertMessageModel alertMessageModel = new AlertMessageMapper().dataToModel(alertMessageDto, language);
                        String appVersionName = getAppVersionName(context);
                        boolean updateNeeded = false;
                        if (AlertMessageService.shouldMessageBeShown(alertMessageModel, appVersionName)) {
                            showVersionControlPopUp(context, listener, alertMessageModel);
                            updateNeeded = true;
                        }
                        runOnUiThread(updateNeeded, listener);
                    }
                }
            });
        } catch (final Exception e) {
            runOnUiThread(e, listener);
        }
    }

    @SuppressWarnings("all")
    private static void jsonFileInit(Context context, final String language, final AlertDialogListener listener) {
        String json = null;
        try {
            InputStream is = context.getAssets().open("versionChecker.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        AlertMessageDto alertMessage = new Gson().fromJson(json, AlertMessageDto.class);
        AlertMessageModel alertMessageModel = new AlertMessageMapper().dataToModel(alertMessage, language);
        showVersionControlPopUp(context, listener, alertMessageModel);
        runOnUiThread(true, listener);
    }

    /**
     * Creates mock dialog based on dialog type instead of url
     *
     * @param context    the context of activity
     * @param dialogType int value of dialog type
     */
    public static void showMockMessageDialog(Context context, int dialogType, String language,
            final AlertDialogListener listener) {
        AlertMessageDto alertMessageMock = new AlertMessageDto();
        alertMessageMock.setVersion("3.5.1");
        alertMessageMock.setComparisonMode(0);
        alertMessageMock.setTitle("Your title goes here");
        alertMessageMock.setMessage("Your message goes here");
        if (dialogType == CustomAlertDialogFragment.DIALOG_WITH_OK_BUTTON) {
            alertMessageMock.setOkButtonTitle("Ok");
            alertMessageMock.setOkButtonActionURL("http://atos.net");
        } else if (dialogType == CustomAlertDialogFragment.DIALOG_WITH_OK_AND_CANCEL_BUTTONS) {
            alertMessageMock.setOkButtonTitle("Ok");
            alertMessageMock.setCancelButtonTitle("Cancel");
            alertMessageMock.setOkButtonActionURL("http://atos.net");
        } else if (dialogType == CustomAlertDialogFragment.DIALOG_WITH_CANCEL_BUTTON) {
            alertMessageMock.setCancelButtonTitle("Cancel");
        }
        AlertMessageModel alertMessageModel = new AlertMessageMapper().dataToModel(alertMessageMock, language);
        showVersionControlPopUp(context, listener, alertMessageModel);
    }

    private static void runOnUiThread(final Exception e, final AlertDialogListener listener) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                listener.onFailure(e);
            }
        });
    }

    private static void runOnUiThread(final boolean updateNeeded, final AlertDialogListener listener) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                listener.onSuccess(updateNeeded);
            }
        });
    }

    public interface AlertDialogListener {
        void onFailure(Exception e);

        void onSuccess(boolean updateNeeded);

        void onAlertDialogDismissed();
    }
}
