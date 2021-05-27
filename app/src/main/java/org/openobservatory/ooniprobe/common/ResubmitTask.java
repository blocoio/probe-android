package org.openobservatory.ooniprobe.common;

import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.VisibleForTesting;

import com.raizlabs.android.dbflow.sql.language.Where;

import org.openobservatory.engine.LoggerArray;
import org.openobservatory.engine.OONISession;
import org.openobservatory.ooniprobe.BuildConfig;
import org.openobservatory.ooniprobe.R;
import org.openobservatory.ooniprobe.activity.AbstractActivity;
import org.openobservatory.ooniprobe.domain.GetResults;
import org.openobservatory.ooniprobe.domain.MeasurementsManager;
import org.openobservatory.ooniprobe.model.database.Measurement;
import org.openobservatory.ooniprobe.model.database.Measurement_Table;
import org.openobservatory.ooniprobe.test.EngineProvider;

import java.util.List;

import javax.inject.Inject;

import localhost.toolkit.os.NetworkProgressAsyncTask;

public class ResubmitTask<A extends AbstractActivity> extends NetworkProgressAsyncTask<A, Integer, Boolean> {
    protected Integer totUploads;
    protected Integer errors;
    protected LoggerArray logger;

    protected Dependencies d = new Dependencies();

    @VisibleForTesting
    protected boolean publishProgress = true;

    /**
     * Use this class to resubmit a measurement, use result_id and measurement_id to filter list of value
     * {@code new MKCollectorResubmitTask(activity).execute(@Nullable result_id, @Nullable measurement_id);}
     *
     * @param activity from which this task are executed
     */
    public ResubmitTask(A activity) {
        super(activity, true, false);
        activity.getComponent().serviceComponent().inject(d);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        A activity = getActivity();
        if (activity != null)
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    /**
     * this method is invoked when the {@code execute()} method is called
     *
     * @param params [0] is result_id. is nullable and is used to restrict measurement retrieve on a specific result.
     *               [1] is measurement_id. is nullable and is used to restrict measurement retrieve on a specific measurement.
     * @return true if success, false otherwise
     */
    @Override
    protected Boolean doInBackground(Integer... params) {
        logger = new LoggerArray();
        errors = 0;
        if (params.length != 2)
            throw new IllegalArgumentException("MKCollectorResubmitTask requires 2 nullable params: result_id, measurement_id");
        Where<Measurement> msmQuery = Measurement.selectUploadable();
        if (params[0] != null) {
            msmQuery.and(Measurement_Table.result_id.eq(params[0]));
        }
        if (params[1] != null) {
            msmQuery.and(Measurement_Table.id.eq(params[1]));
        }
        //Get a list of measurements with report file
        List<Measurement> measurements = Measurement.withReport(getActivity(), msmQuery);
        totUploads = measurements.size();
        try {
            OONISession session = EngineProvider.get().newSession(
                    EngineProvider.get().getDefaultSessionConfig(
                            getActivity(), BuildConfig.SOFTWARE_NAME, BuildConfig.VERSION_NAME, logger)
            );
            // Updating resources with no timeout because we don't know for sure how much
            // it will take to download them and choosing a timeout may prevent the operation
            // to ever complete. (Ideally the user should be able to interrupt the process
            // and there should be no timeout here.)
            session.maybeUpdateResources(session.newContext());
            for (int i = 0; i < measurements.size(); i++) {
                A activity = getActivity();
                if (activity ==  null)
                    break;
                String paramOfParam = activity.getString(R.string.paramOfParam, Integer.toString(i + 1), Integer.toString(measurements.size()));
                if (publishProgress) {
                    publishProgress(activity.getString(R.string.Modal_ResultsNotUploaded_Uploading, paramOfParam));
                }
                Measurement m = measurements.get(i);
                m.result.load();
                if(!d.measurementsManager.reSubmit(m, session)){
                    errors++;
                }
            }
        }
       catch (Exception e){
           e.printStackTrace();
           ThirdPartyServices.logException(e);
           return false;
       }
        return errors == 0;
    }

    @Override
    protected void onPostExecute(Boolean result) {
        super.onPostExecute(result);
        A activity = getActivity();
        if (activity != null && result) {
            Toast.makeText(activity, activity.getString(R.string.Toast_ResultsUploaded), Toast.LENGTH_SHORT).show();
            activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }
    }

    public static class Dependencies {
        @Inject
        public MeasurementsManager measurementsManager;

        @Inject
        public GetResults getResults;
    }
}
