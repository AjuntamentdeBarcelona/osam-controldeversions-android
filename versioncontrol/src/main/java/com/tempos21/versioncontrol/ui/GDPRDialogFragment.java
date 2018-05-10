package com.tempos21.versioncontrol.ui;

import com.tempos21.versioncontrol.R;
import com.tempos21.versioncontrol.service.AlertMessageService;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.customtabs.CustomTabsIntent;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

/**
 * GDPRDialogFragment
 */
public class GDPRDialogFragment extends DialogFragment {

    public static String WEB_VIEW_URL_KEY = "WEB_VIEW_URL_KEY";

    public static String BUTTON_TEXT_KEY = "BUTTON_TEXT_KEY";

    public static String CUSTOM_TAB_BACKGROUND_COLOR_KEY = "CUSTOM_TAB_BACKGROUND_COLOR_KEY";

    public static int NO_CUSTOM_TAB = 0;

    private WebView webView;

    private Button accept;

    private android.support.v4.widget.ContentLoadingProgressBar progress;

    private AlertMessageService.GdprListener gdprListener;

    public static GDPRDialogFragment newInstance(String webViewUrl, String acceptButtonText, int customTabBackgroundColor) {
        GDPRDialogFragment gdprDialogFragment = new GDPRDialogFragment();

        Bundle bundle = new Bundle();
        bundle.putString(WEB_VIEW_URL_KEY, webViewUrl);
        bundle.putString(BUTTON_TEXT_KEY, acceptButtonText);
        bundle.putInt(CUSTOM_TAB_BACKGROUND_COLOR_KEY, customTabBackgroundColor);

        gdprDialogFragment.setArguments(bundle);

        return gdprDialogFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NO_FRAME, android.R.style.Theme_Holo_Light);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_gdpr_dialog, container, false);

        initializeUI(view);
        registerListeners();

        return view;
    }

    private void initializeUI(View view) {
        webView = view.findViewById(R.id.webView);
        accept = view.findViewById(R.id.accept);
        progress = view.findViewById(R.id.progress);

        webView.loadUrl(getWebViewUrl(getArguments()));
        accept.setText(getButtonText(getArguments()));
    }

    private void registerListeners() {
        webView.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                progress.show();
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                progress.hide();
            }

            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                if (getCustomTabToolbarBackgroundColor(getArguments()) != NO_CUSTOM_TAB) {
                    showCustomTab(request.getUrl());
                } else {
                    openExternalBrowser(request.getUrl());
                }
                return true;
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (getCustomTabToolbarBackgroundColor(getArguments()) != NO_CUSTOM_TAB) {
                    showCustomTab(Uri.parse(url));
                } else {
                    openExternalBrowser(Uri.parse(url));
                }
                return true;
            }

            private void openExternalBrowser(Uri uri) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(uri);
                startActivity(i);
            }

            private void showCustomTab(Uri uri) {
                Context context = getContext();

                if (context != null) {
                    int customTabToolbarBackgroundColor = ContextCompat
                            .getColor(context, getCustomTabToolbarBackgroundColor(getArguments()));

                    CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
                    builder.setShowTitle(true);
                    builder.setToolbarColor(customTabToolbarBackgroundColor);

                    CustomTabsIntent customTabsIntent = builder.build();
                    customTabsIntent.launchUrl(context, uri);
                }
            }
        });

        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gdprListener.onGdprAccepted();
            }
        });
    }

    public void setOnAcceptGDPRListener(final AlertMessageService.GdprListener gdprListener) {
        this.gdprListener = gdprListener;
    }

    private String getWebViewUrl(@Nullable Bundle bundle) {
        if (bundle != null) {
            if (bundle.getString(WEB_VIEW_URL_KEY) != null) {
                return bundle.getString(WEB_VIEW_URL_KEY);
            }
        }
        throw new IllegalArgumentException("This fragment MUST be called with the webview url");
    }

    private String getButtonText(@Nullable Bundle bundle) {
        if (bundle != null) {
            if (bundle.getString(BUTTON_TEXT_KEY) != null) {
                return bundle.getString(BUTTON_TEXT_KEY);
            }
        }
        throw new IllegalArgumentException("This fragment MUST be called with the accept button text");
    }

    private int getCustomTabToolbarBackgroundColor(@Nullable Bundle bundle) {
        if (bundle != null) {
            return bundle.getInt(CUSTOM_TAB_BACKGROUND_COLOR_KEY);
        }
        throw new IllegalArgumentException("This fragment MUST be called with the chrome custom tab color");
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        gdprListener.onGdprDismissed();
    }
}
