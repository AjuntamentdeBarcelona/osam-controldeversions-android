package com.tempos21.versioncontrol.service;

import junit.framework.Assert;

import org.junit.Test;

public class AlertMessageServiceShould {

    @Test
    public void return_less_than_when_compare_version_1_with_1_point_0_point_1() {
        String versionInstalled = "1";
        String versionReceived = "1.0.1";

        int response = AlertMessageService.compareVersionNames(versionInstalled, versionReceived);

        Assert.assertEquals(-1, response);
    }

    @Test
    public void return_less_than_when_compare_version_1_point_0_with_1_point_0_point_1() {
        String versionInstalled = "1.0";
        String versionReceived = "1.0.1";

        int response = AlertMessageService.compareVersionNames(versionInstalled, versionReceived);

        Assert.assertEquals(-1, response);
    }

    @Test
    public void return_equal_than_when_compare_version_1_point_0_point_1_with_1_point_0_point_1() {
        String versionInstalled = "1.0.1";
        String versionReceived = "1.0.1";

        int response = AlertMessageService.compareVersionNames(versionInstalled, versionReceived);

        Assert.assertEquals(0, response);
    }

    @Test
    public void return_greater_than_when_compare_version_2_with_1_point_0_point_1() {
        String versionInstalled = "2";
        String versionReceived = "1.0.1";

        int response = AlertMessageService.compareVersionNames(versionInstalled, versionReceived);

        Assert.assertEquals(1, response);
    }

    @Test
    public void return_greater_than_when_compare_version_1_point_1_with_1_point_0_point_1() {
        String versionInstalled = "2";
        String versionReceived = "1.0.1";

        int response = AlertMessageService.compareVersionNames(versionInstalled, versionReceived);

        Assert.assertEquals(1, response);
    }

}
