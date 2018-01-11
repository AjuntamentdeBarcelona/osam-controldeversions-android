package com.tempos21.versioncontrol.model;

public class AlertMessageDto {

    private String version;
    private Integer comparisonMode;
    private String minSystemVersion;
    private Object title;
    private Object message;
    private Object okButtonTitle;
    private String okButtonActionURL;
    private Object cancelButtonTitle;

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

    public Object getTitle() {
        return title;
    }

    public void setTitle(Object title) {
        this.title = title;
    }

    public Object getMessage() {
        return message;
    }

    public void setMessage(Object message) {
        this.message = message;
    }

    public Object getOkButtonTitle() {
        return okButtonTitle;
    }

    public void setOkButtonTitle(Object okButtonTitle) {
        this.okButtonTitle = okButtonTitle;
    }

    public String getOkButtonActionURL() {
        return okButtonActionURL;
    }

    public void setOkButtonActionURL(String okButtonActionURL) {
        this.okButtonActionURL = okButtonActionURL;
    }

    public Object getCancelButtonTitle() {
        return cancelButtonTitle;
    }

    public void setCancelButtonTitle(Object cancelButtonTitle) {
        this.cancelButtonTitle = cancelButtonTitle;
    }

}
