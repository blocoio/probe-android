package org.openobservatory.ooniprobe.di;

import android.app.Application;

import org.openobservatory.ooniprobe.client.OONIAPIClient;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class TestAppModule extends ApplicationModule {

    private static final String CLIENT_URL = "https://ams-pg.ooni.org";


    public TestAppModule(Application application) {
        super(application);
    }

    @Override
    public OONIAPIClient buildApiClient(OkHttpClient client) {
        return new Retrofit.Builder()
                .baseUrl(CLIENT_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
                .create(OONIAPIClient.class);
    }
}
