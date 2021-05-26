package org.openobservatory.ooniprobe.common;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.format.DateFormat;

import org.junit.Test;
import org.openobservatory.engine.OONISession;
import org.openobservatory.ooniprobe.R;
import org.openobservatory.ooniprobe.RobolectricAbstractTest;
import org.openobservatory.ooniprobe.engine.TestEngineInterface;
import org.openobservatory.ooniprobe.test.EngineProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static androidx.preference.PreferenceManager.getDefaultSharedPreferences;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.openobservatory.ooniprobe.common.PreferenceManager.AUTORUN_COUNT;
import static org.openobservatory.ooniprobe.common.PreferenceManager.AUTORUN_DATE;
import static org.openobservatory.ooniprobe.common.PreferenceManager.DELETE_JSON_DELAY;
import static org.openobservatory.ooniprobe.common.PreferenceManager.DELETE_JSON_KEY;
import static org.openobservatory.ooniprobe.common.PreferenceManager.NOTIFICATION_DIALOG_DISABLE;
import static org.openobservatory.ooniprobe.common.PreferenceManager.SHOW_ONBOARDING;
import static org.openobservatory.ooniprobe.common.PreferenceManager.TOKEN;
import static org.openobservatory.ooniprobe.common.PreferenceManager.UUID4;

public class PreferenceManagerTest extends RobolectricAbstractTest {

    @Test
    public void testGetSetToken() {
        // Arrange
        PreferenceManager pm = build(c);
        SharedPreferences sharedPreferences = getDefaultSharedPreferences(c);

        // Act
        pm.setToken("abc");
        String value = pm.getToken();

        // Assert
        assertEquals(sharedPreferences.getString(TOKEN, ""), value);
    }

    @Test
    public void testMaxRuntime() {
        // Arrange
        PreferenceManager pm = build(c);
        SharedPreferences sharedPreferences = getDefaultSharedPreferences(c);

        sharedPreferences.edit()
                .putBoolean(c.getString(R.string.max_runtime_enabled), true)
                .putString(c.getString(R.string.max_runtime), "91")
                .apply();

        // Act
        int value = pm.getMaxRuntime();
        boolean isEnabled = pm.isMaxRuntimeEnabled();

        // Assert
        assertTrue(isEnabled);
        assertEquals(91, value);
    }

    @Test
    public void testMaxRuntimeDisabled() {
        // Arrange
        PreferenceManager pm = build(c);
        SharedPreferences sharedPreferences = getDefaultSharedPreferences(c);

        sharedPreferences.edit()
                .putBoolean(c.getString(R.string.max_runtime_enabled), false)
                .putString(c.getString(R.string.max_runtime), "91")
                .apply();

        // Act
        int value = pm.getMaxRuntime();
        boolean isEnabled = pm.isMaxRuntimeEnabled();

        // Assert
        assertFalse(isEnabled);
        assertEquals(-1, value);
    }

    @Test
    public void testMaxRuntimeFallback() {
        // Arrange
        PreferenceManager pm = build(c);
        SharedPreferences sharedPreferences = getDefaultSharedPreferences(c);

        sharedPreferences.edit()
                .putBoolean(c.getString(R.string.max_runtime_enabled), true)
                .putString(c.getString(R.string.max_runtime), "abc")
                .apply();

        // Act
        int value = pm.getMaxRuntime();
        boolean isEnabled = pm.isMaxRuntimeEnabled();

        // Assert
        assertTrue(isEnabled);
        assertEquals(90, value);
    }

    @Test
    public void testSendCrashGetSet() {
        // Arrange
        PreferenceManager pm = build(c);
        SharedPreferences sharedPreferences = getDefaultSharedPreferences(c);

        sharedPreferences.edit()
                .putBoolean(c.getString(R.string.send_crash), false)
                .apply();

        // Act
        boolean original = pm.isSendCrash();
        pm.setSendCrash(true);
        boolean updated = pm.isSendCrash();

        // Assert
        assertFalse(original);
        assertTrue(updated);
    }

    @Test
    public void testShowOnBoardingGetSet() {
        // Arrange
        PreferenceManager pm = build(c);
        SharedPreferences sharedPreferences = getDefaultSharedPreferences(c);

        sharedPreferences.edit()
                .putBoolean(SHOW_ONBOARDING, false)
                .apply();

        // Act
        boolean original = pm.isShowOnboarding();
        pm.setShowOnboarding(true);
        boolean updated = pm.isShowOnboarding();

        // Assert
        assertFalse(original);
        assertTrue(updated);
    }

    @Test
    public void testAskNotificationDialog() {
        // Arrange
        PreferenceManager pm = build(c);
        SharedPreferences sharedPreferences = getDefaultSharedPreferences(c);

        sharedPreferences.edit()
                .putBoolean(NOTIFICATION_DIALOG_DISABLE, false)
                .apply();

        // Act
        boolean original = pm.isAskNotificationDialogDisabled();
        pm.disableAskNotificationDialog();
        boolean updated = pm.isAskNotificationDialogDisabled();

        // Assert
        assertFalse(original);
        assertTrue(updated);
    }

    @Test
    public void testIsDarkTheme() {
        // Arrange
        PreferenceManager pm = build(c);
        SharedPreferences sharedPreferences = getDefaultSharedPreferences(c);

        sharedPreferences.edit()
                .putBoolean(c.getString(R.string.theme_enabled), true)
                .apply();

        // Act
        boolean value = pm.isDarkTheme();

        // Assert
        assertTrue(value);
    }

    @Test
    public void testNotificationsFromDialog() {
        // Arrange
        PreferenceManager pm = build(c);
        SharedPreferences sharedPreferences = getDefaultSharedPreferences(c);

        sharedPreferences.edit()
                .putBoolean(NOTIFICATION_DIALOG_DISABLE, false)
                .apply();

        // Act
        boolean original = pm.isAskNotificationDialogDisabled();
        pm.disableAskNotificationDialog();
        boolean updated = pm.isAskNotificationDialogDisabled();

        // Assert
        assertFalse(original);
        assertTrue(updated);
    }

    @Test
    public void testIsUploadResults() {
        // Arrange
        PreferenceManager pm = build(c);
        SharedPreferences sharedPreferences = getDefaultSharedPreferences(c);

        sharedPreferences.edit()
                .putBoolean(c.getString(R.string.upload_results), false)
                .apply();

        // Act
        boolean value = pm.isUploadResults();

        // Assert
        assertFalse(value);
    }

    @Test
    public void testIsDebugLogs() {
        // Arrange
        PreferenceManager pm = build(c);
        SharedPreferences sharedPreferences = getDefaultSharedPreferences(c);

        sharedPreferences.edit()
                .putBoolean(c.getString(R.string.debugLogs), false)
                .apply();

        // Act
        boolean value = pm.isDebugLogs();

        // Assert
        assertFalse(value);
    }

    @Test
    public void testAbstractTests() {
        // Arrange
        PreferenceManager pm = build(c);
        SharedPreferences sharedPreferences = getDefaultSharedPreferences(c);

        sharedPreferences.edit()
                .putBoolean(c.getString(R.string.test_telegram), false)
                .putBoolean(c.getString(R.string.test_whatsapp), false)
                .putBoolean(c.getString(R.string.test_facebook_messenger), false)
                .putBoolean(c.getString(R.string.test_signal), false)
                .putBoolean(c.getString(R.string.test_psiphon), false)
                .putBoolean(c.getString(R.string.test_tor), false)
                .putBoolean(c.getString(R.string.test_riseupvpn), false)
                .putBoolean(c.getString(R.string.run_http_invalid_request_line), false)
                .putBoolean(c.getString(R.string.run_http_header_field_manipulation), false)
                .putBoolean(c.getString(R.string.run_ndt), false)
                .putBoolean(c.getString(R.string.run_dash), false)
                .apply();

        // Act // Assert
        assertFalse(pm.isTestTelegram());
        assertFalse(pm.isTestWhatsapp());
        assertFalse(pm.isTestFacebookMessenger());
        assertFalse(pm.isTestSignal());
        assertFalse(pm.isTestPsiphon());
        assertFalse(pm.isTestTor());
        assertFalse(pm.isTestRiseupVPN());
        assertFalse(pm.isRunHttpInvalidRequestLine());
        assertFalse(pm.isRunHttpHeaderFieldManipulation());
        assertFalse(pm.isRunNdt());
        assertFalse(pm.isRunDash());
    }

    @Test
    public void testEnabledCategoryArr() {
        // Arrange
        PreferenceManager pm = build(c);
        SharedPreferences sharedPreferences = getDefaultSharedPreferences(c);

        ArrayList<String> list = new ArrayList<>();
        for (String key : c.getResources().getStringArray(R.array.CategoryCodes)) {
            sharedPreferences.edit().putBoolean(key, true).apply();
            list.add(key);
        }

        sharedPreferences.edit()
                .putBoolean(list.get(0), false)
                .putBoolean(list.get(1), false)
                .apply();

        list.remove(0);
        list.remove(0);

        // Act
        int count = pm.countEnabledCategory();
        List<String> categories = pm.getEnabledCategoryArr();

        // Assert
        assertEquals(count, list.size());
        assertArrayEquals(categories.toArray(), list.toArray());
    }

    @Test
    public void testCanCallDeleteJson() {
        // Arrange
        PreferenceManager pm = build(c);
        SharedPreferences sharedPreferences = getDefaultSharedPreferences(c);

        sharedPreferences.edit()
                .putLong(DELETE_JSON_KEY, 0L)
                .apply();

        boolean defaultValue = pm.canCallDeleteJson();

        sharedPreferences.edit()
                .putLong(DELETE_JSON_KEY, DELETE_JSON_DELAY + 1L)
                .apply();

        // Act
        boolean original = pm.canCallDeleteJson();
        pm.setLastCalled();
        boolean updated = pm.canCallDeleteJson();

        // Assert
        assertTrue(defaultValue);
        assertTrue(original);
        assertFalse(updated);
    }

    @Test
    public void testGetOrGenerateUUID4() {
        // Arrange
        PreferenceManager pm = build(c);
        SharedPreferences sharedPreferences = getDefaultSharedPreferences(c);

        OONISession mockSession = mock(OONISession.class);
        EngineProvider.engineInterface = new TestEngineInterface(mockSession);

        // Act
        String original = pm.getOrGenerateUUID4();

        sharedPreferences.edit()
                .putString(UUID4, "abc")
                .apply();

        String updated = pm.getOrGenerateUUID4();

        // Assert
        assertEquals("UUID4", original);
        assertEquals("abc", updated);
    }

    @Test
    public void testAppOpenCount() {
        // Arrange
        PreferenceManager pm = build(c);
        SharedPreferences sharedPreferences = getDefaultSharedPreferences(c);

        sharedPreferences.edit()
                .putBoolean("XXXX", false)
                .apply();

        // Act
        pm.setAppOpenCount(8L);
        long original = pm.getAppOpenCount();
        pm.incrementAppOpenCount();
        pm.incrementAppOpenCount();
        long updated = pm.getAppOpenCount();

        // Assert
        assertEquals(original, 8L);
        assertEquals(updated, 10L);
    }

    @Test
    public void testAutomaticTest() {
        // Arrange
        PreferenceManager pm = build(c);
        SharedPreferences sharedPreferences = getDefaultSharedPreferences(c);

        sharedPreferences.edit()
                .putBoolean(c.getString(R.string.automated_testing_enabled), true)
                .apply();

        // Act
        boolean original = pm.isAutomaticTestEnabled();
        pm.disableAutomaticTest();
        boolean updated = pm.isAutomaticTestEnabled();

        // Assert
        assertTrue(original);
        assertFalse(updated);
    }

    @Test
    public void testTestWifiOnly() {
        // Arrange
        PreferenceManager pm = build(c);
        SharedPreferences sharedPreferences = getDefaultSharedPreferences(c);

        sharedPreferences.edit()
                .putBoolean(c.getString(R.string.automated_testing_wifionly), false)
                .apply();

        // Act
        boolean value = pm.testWifiOnly();

        // Assert
        assertFalse(value);
    }

    @Test
    public void testTestChargingOnly() {
        // Arrange
        PreferenceManager pm = build(c);
        SharedPreferences sharedPreferences = getDefaultSharedPreferences(c);

        sharedPreferences.edit()
                .putBoolean(c.getString(R.string.automated_testing_charging), false)
                .apply();

        // Act
        boolean value = pm.testChargingOnly();

        // Assert
        assertFalse(value);
    }

    @Test
    public void testAutorun() {
        // Arrange
        PreferenceManager pm = build(c);
        SharedPreferences sharedPreferences = getDefaultSharedPreferences(c);

        sharedPreferences.edit()
                .putLong(AUTORUN_COUNT, 5L)
                .apply();

        // Act
        pm.incrementAutorun();
        long value = pm.getAutorun();

        // Assert
        assertEquals(6L, value);
    }

    @Test
    public void testAutorunDate() {
        // Arrange
        PreferenceManager pm = build(c);
        SharedPreferences sharedPreferences = getDefaultSharedPreferences(c);
        String updatedDate = DateFormat.format(DateFormat.getBestDateTimePattern(Locale.getDefault(), "yMdHm"), System.currentTimeMillis()).toString();

        sharedPreferences.edit()
                .putLong(AUTORUN_DATE, 0L)
                .apply();

        // Act
        String original = pm.getAutorunDate();
        pm.updateAutorunDate();
        String updated = pm.getAutorunDate();

        // Assert
        assertEquals(c.getString(R.string.Dashboard_Overview_LastRun_Never), original);
        assertEquals(updatedDate, updated);
    }

    private PreferenceManager build(Context c) {
        return new PreferenceManager(c);
    }
}