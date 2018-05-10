package com.tempos21.versioncontrol.model;

import android.os.Parcel;
import android.os.Parcelable;

public class AlertMessageModel implements Parcelable {

    public static final Parcelable.Creator<AlertMessageModel> CREATOR = new Parcelable.Creator<AlertMessageModel>() {

        @Override
        public AlertMessageModel createFromParcel(Parcel source) {
            return new AlertMessageModel(source);
        }

        @Override
        public AlertMessageModel[] newArray(int size) {
            return new AlertMessageModel[size];
        }
    };

    private String version;

    private Integer comparisonMode;

    private String minSystemVersion;

    private String title;

    private String message;

    private String okButtonTitle;

    private String okButtonActionURL;

    private String cancelButtonTitle;

    public AlertMessageModel() {
    }

    private AlertMessageModel(Parcel in) {
        version = in.readString();
        comparisonMode = in.readInt();
        minSystemVersion = in.readString();
        okButtonActionURL = in.readString();
        title = in.readString();
        message = in.readString();
        okButtonTitle = in.readString();
        cancelButtonTitle = in.readString();
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Integer getComparisonMode() {
        return comparisonMode;
    }

    public void setComparisonMode(Integer comparisonMode) {
        this.comparisonMode = comparisonMode;
    }

    public String getMinSystemVersion() {
        return minSystemVersion;
    }

    public void setMinSystemVersion(String minSystemVersion) {
        this.minSystemVersion = minSystemVersion;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getOkButtonTitle() {
        return okButtonTitle;
    }

    public void setOkButtonTitle(String okButtonTitle) {
        this.okButtonTitle = okButtonTitle;
    }

    public String getOkButtonActionURL() {
        return okButtonActionURL;
    }

    public void setOkButtonActionURL(String okButtonActionURL) {
        this.okButtonActionURL = okButtonActionURL;
    }

    public String getCancelButtonTitle() {
        return cancelButtonTitle;
    }

    public void setCancelButtonTitle(String cancelButtonTitle) {
        this.cancelButtonTitle = cancelButtonTitle;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(version);
        dest.writeInt(comparisonMode);
        dest.writeString(minSystemVersion);
        dest.writeString(okButtonActionURL);
        dest.writeString(title);
        dest.writeString(message);
        dest.writeString(okButtonTitle);
        dest.writeString(cancelButtonTitle);
    }

}
