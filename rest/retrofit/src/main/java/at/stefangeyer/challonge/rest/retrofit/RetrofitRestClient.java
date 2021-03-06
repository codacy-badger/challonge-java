package at.stefangeyer.challonge.rest.retrofit;

import at.stefangeyer.challonge.model.Credentials;
import at.stefangeyer.challonge.rest.*;
import at.stefangeyer.challonge.rest.retrofit.converter.RetrofitConverterFactory;
import at.stefangeyer.challonge.serializer.Serializer;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import okhttp3.Request;
import retrofit2.Retrofit;

import java.io.Closeable;
import java.nio.charset.Charset;
import java.util.Collections;

import static at.stefangeyer.challonge.rest.retrofit.util.RetrofitUtil.responseCount;

public class RetrofitRestClient implements RestClient, Closeable {

    private static final String BASE_URL = "https://api.challonge.com/v1/";

    private OkHttpClient httpClient;
    private boolean useHttp1_1;

    private ChallongeRetrofit challongeRetrofit;

    /**
     * Create a Retrofit rest client.
     * If HTTP 1.1 not used, it may be necessary to close the rest client using {@link #close()} in order for all
     * running threads to be terminated. See the following <a href="https://github.com/square/okhttp/issues/4029">link</a>
     * for more information.
     *
     * @param useHttp1_1 use HTTP 1.1?
     */
    public RetrofitRestClient(boolean useHttp1_1) {
        this.useHttp1_1 = useHttp1_1;
    }

    /**
     * Create a Retrofit rest client using HTTP 1.1
     */
    public RetrofitRestClient() {
        this(true);
    }

    @Override
    public void initialize(Credentials credentials, Serializer serializer) {
        OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();

        if (this.useHttp1_1) {
            httpClientBuilder.protocols(Collections.singletonList(Protocol.HTTP_1_1));
        }

        httpClientBuilder.authenticator((route, response) -> {
            String credential = okhttp3.Credentials.basic(credentials.getUsername(), credentials.getKey(),
                    Charset.forName("UTF-8"));
            // retry authentication 5 times only
            if (responseCount(response) >= 5) {
                return null;
            }
            return response.request().newBuilder().header("Authorization", credential).build();
        });

        httpClientBuilder.addInterceptor((chain -> {
            Request original = chain.request();

            Request.Builder requestBuilder = original.newBuilder()
                    .header("Accept", "application/json")
                    .method(original.method(), original.body());

            return chain.proceed(requestBuilder.build());
        }));

        this.httpClient = httpClientBuilder.build();

        RetrofitConverterFactory factory = new RetrofitConverterFactory(serializer);

        Retrofit retrofit = new Retrofit.Builder().client(this.httpClient).baseUrl(BASE_URL).addConverterFactory(factory).build();

        this.challongeRetrofit = retrofit.create(ChallongeRetrofit.class);
    }

    @Override
    public TournamentRestClient createTournamentRestClient() {
        if (this.challongeRetrofit != null) {
            return new RetrofitTournamentRestClient(this.challongeRetrofit);
        } else {
            throw new IllegalStateException("Attempted to create rest client before initialization");
        }
    }

    @Override
    public ParticipantRestClient createParticipantRestClient() {
        if (this.challongeRetrofit != null) {
            return new RetrofitParticipantRestClient(this.challongeRetrofit);
        } else {
            throw new IllegalStateException("Attempted to create rest client before initialization");
        }
    }

    @Override
    public MatchRestClient createMatchRestClient() {
        if (this.challongeRetrofit != null) {
            return new RetrofitMatchRestClient(this.challongeRetrofit);
        } else {
            throw new IllegalStateException("Attempted to create rest client before initialization");
        }
    }

    @Override
    public AttachmentRestClient createAttachmentRestClient() {
        if (this.challongeRetrofit != null) {
            return new RetrofitAttachmentRestClient(this.challongeRetrofit);
        } else {
            throw new IllegalStateException("Attempted to create rest client before initialization");
        }
    }

    @Override
    public void close() {
        // Stops all non daemon threads used for HTTP/2 which might prevent the application from stopping
        // See https://github.com/square/okhttp/issues/4029 for further information

        this.httpClient.connectionPool().evictAll();
    }
}
