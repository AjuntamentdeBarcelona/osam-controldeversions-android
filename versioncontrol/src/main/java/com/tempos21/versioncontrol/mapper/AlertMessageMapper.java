package com.tempos21.versioncontrol.mapper;

import com.tempos21.versioncontrol.model.AlertMessageDto;
import com.tempos21.versioncontrol.model.AlertMessageModel;

import java.util.Map;

public class AlertMessageMapper {

    public AlertMessageModel dataToModel(AlertMessageDto data, String language) {
        AlertMessageModel model = new AlertMessageModel();
        if (data != null) {
            model.setVersion(data.getVersion());
            model.setComparisonMode(data.getComparisonMode());
            model.setMinSystemVersion(data.getMinSystemVersion());
            model.setOkButtonActionURL(data.getOkButtonActionURL());

            //TITLE
            if (data.getTitle() instanceof String) {
                model.setTitle((String) data.getTitle());
            } else if (data.getTitle() instanceof Map) {
                @SuppressWarnings("unchecked")
                Map<String, String> titleMap = (Map) data.getTitle();
                model.setTitle(getTextFromKey(language, titleMap));
            }
            //MESSAGE
            if (data.getMessage() instanceof String) {
                model.setMessage((String) data.getMessage());
            } else if (data.getMessage() instanceof Map) {
                @SuppressWarnings("unchecked")
                Map<String, String> messageMap = (Map) data.getMessage();
                model.setMessage(getTextFromKey(language, messageMap));
            }
            //OK BUTTON
            if (data.getOkButtonTitle() instanceof String) {
                model.setOkButtonTitle((String) data.getOkButtonTitle());
            } else if (data.getOkButtonTitle() instanceof Map) {
                @SuppressWarnings("unchecked")
                Map<String, String> okTitleMap = (Map) data.getOkButtonTitle();
                model.setOkButtonTitle(getTextFromKey(language, okTitleMap));
            }
            //CANCEL BUTTON
            if (data.getCancelButtonTitle() instanceof String) {
                model.setCancelButtonTitle((String) data.getCancelButtonTitle());
            } else if (data.getCancelButtonTitle() instanceof Map) {
                @SuppressWarnings("unchecked")
                Map<String, String> cancelTitleMap = (Map) data.getCancelButtonTitle();
                model.setCancelButtonTitle(getTextFromKey(language, cancelTitleMap));
            }
            if (data.getLegalURL() instanceof String) {
                model.setLegalURL((String) data.getLegalURL());
            } else if (data.getLegalURL() instanceof Map) {
                @SuppressWarnings("unchecked")
                Map<String, String> legalURLMap = (Map) data.getLegalURL();
                model.setLegalURL(getTextFromKey(language, legalURLMap));
            }
        }
        return model;
    }

    private String getTextFromKey(String language, Map<String, String> items) {
        String textValue;
        if (items.containsKey(language)) {
            textValue = items.get(language);
        } else {
            textValue = items.entrySet().iterator().next().getValue();
        }
        return textValue;
    }
}
