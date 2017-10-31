package exercise.okcupid.com.api;


import android.support.annotation.NonNull;

import java.io.IOException;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

import exercise.okcupid.com.BuildConfig;
import exercise.okcupid.com.model.CupidData;
import io.reactivex.Observable;
import okhttp3.CipherSuite;
import okhttp3.ConnectionSpec;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.TlsVersion;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

public class OkCupidService {
    public interface OkCupidApi {
        /**
         * @return {@link CupidData}
         */
        @GET("/matchSample.json")
        Observable<retrofit2.Response<CupidData>> userList();
    }

    /**
     * Init API
     *
     * @return OkCupidApi
     */
    public static OkCupidApi create() {
        Retrofit.Builder builder = new Retrofit.Builder();
        builder.baseUrl(BuildConfig.OKCUPID_BASE_URL);
        builder.addConverterFactory(GsonConverterFactory.create());
        builder.addCallAdapterFactory(RxJava2CallAdapterFactory.create());
        builder.client(http3Client());
        builder.build();
        return builder.build().create(OkCupidApi.class);
    }

    /**
     * Typically I would cache the data but : cache-control: private
     * Instead I used Realm to cache the data
     */
    private static OkHttpClient http3Client() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectionSpecs(Collections.singletonList(connectionSpec()));
        builder.connectTimeout(BuildConfig.TIMEOUT, TimeUnit.SECONDS);
        builder.writeTimeout(BuildConfig.TIMEOUT, TimeUnit.SECONDS);
        builder.readTimeout(BuildConfig.TIMEOUT, TimeUnit.SECONDS);
//        builder.addInterceptor(interceptor) // Not being used but keeping reference for future reference
        if (BuildConfig.DEBUG) {
            builder.addInterceptor(loggingInterceptor());
        }
        return builder.build();
    }

    /**
     * Log the body of the responses
     *
     * @return Logging Interceptor
     */
    private static HttpLoggingInterceptor loggingInterceptor() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.HEADERS);
//        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
//        logging.setLevel(HttpLoggingInterceptor.Level.BASIC);
        return logging;
    }

    /*
    The TLS versions configured in a connection spec are only be used if they are also enabled in
    the SSL socket
     */

    /**
     * Issue with previous API's
     * {@link ConnectionSpec}
     * <p>
     * NOTE: It is safe to add these regardless of SSL socket. They go unused if not used in TLS versions configured
     * <p>
     * "The TLS versions configured in a connection spec are only be used if they are also enabled in
     * the SSL socket"
     * <p>
     * <p>
     * Where?  Samsung Galaxy S8; Android Version 7.0
     * Error? HTTP FAILED: "javax.net.ssl.SSLHandshakeException: Handshake failed"
     * Need to do more research on why this is occurring on specific Android Device or Android Version
     *
     * @return ConnectionSpec
     */
    private static ConnectionSpec connectionSpec() {
        return new ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS)
                .tlsVersions(TlsVersion.TLS_1_2)
                .cipherSuites(
                        CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_128_GCM_SHA256,
                        CipherSuite.TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256,
                        CipherSuite.TLS_DHE_RSA_WITH_AES_128_GCM_SHA256
                )
                .build();
    }


    /**
     * Used to pass any constant Query Parameters or Path Segments
     * <p>
     * Api Constants
     * <p>
     * Not being used but keeping for future reference
     */
    @SuppressWarnings("unused")
    public static Interceptor interceptor = new Interceptor() {
        @Override
        public Response intercept(@NonNull Chain chain) throws IOException {
            Request original = chain.request();
            HttpUrl originalHttpUrl = original.url();
            HttpUrl.Builder urlBuilder = originalHttpUrl.newBuilder();
//                urlBuilder.addPathSegment("json");
//                urlBuilder.addQueryParameter("key", BuildConfig.PLACES_KEY);
            Request.Builder requestBuilder = original.newBuilder().url(urlBuilder.build());
            Request request = requestBuilder.build();
            return chain.proceed(request);
        }
    };
}
