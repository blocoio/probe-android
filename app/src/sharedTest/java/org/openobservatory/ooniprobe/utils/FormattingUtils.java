package org.openobservatory.ooniprobe.utils;

import android.text.format.DateFormat;

import org.openobservatory.ooniprobe.model.database.Measurement;

import java.text.DecimalFormat;
import java.util.Date;
import java.util.Locale;

public class FormattingUtils {

    public static String formatStartTime(Date date) {
        return DateFormat.format(
                DateFormat.getBestDateTimePattern(Locale.getDefault(), "yMdHm"),
                date
        ).toString();
    }

    public static String formatRunTime(Double runtime) {
        return new DecimalFormat("#0.00").format(runtime);
    }

    public static String formatBootstrap(Double bootstrapTime) {
        return String.format("%.2f s", bootstrapTime);
    }

    public static String getFormattedBridges(Measurement measurement) {
        return String.format("%1$s/%2$s OK", measurement.getTestKeys().obfs4_accessible, measurement.getTestKeys().obfs4_total);
    }

    public static String getFormattedAuthorities(Measurement measurement) {
        return String.format("%1$s/%2$s OK", measurement.getTestKeys().or_port_dirauth_accessible, measurement.getTestKeys().or_port_dirauth_total);
    }
}
