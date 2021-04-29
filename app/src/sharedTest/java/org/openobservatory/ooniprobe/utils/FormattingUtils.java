package org.openobservatory.ooniprobe.utils;

import android.text.format.DateFormat;

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
}
