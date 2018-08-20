package br.com.monitoramento.bruxismo.client;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Cesar on 29/03/2017.
 */
public class ServiceGenerator {

    //public static final String API_BASE_URL = "192.168.100.45/";
    //public static final String API_BASE_URL = "https://viacep.com.br/";
    public static final String API_BASE_URL = "http://192.168.4.1/";
    public static final String CLIENT_ID = "usa pra fazer autenticação";
    public static final String CLIENT_SECRET = "usa pra fazer autenticação";

    private static HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY);
    private static OkHttpClient httpClient = new OkHttpClient
            .Builder()
            .addInterceptor(interceptor)
            .connectTimeout(2, TimeUnit.MINUTES)
            .readTimeout(2, TimeUnit.MINUTES)
            .writeTimeout(2, TimeUnit.MINUTES)
            .build();


    private static Retrofit.Builder builder =
            new Retrofit.Builder()
                    .baseUrl(API_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create());

    public static <S> S createService(Class<S> serviceClass) {
        Retrofit retrofit = builder.client(httpClient).build();
        return retrofit.create(serviceClass);
    }

    // Usa esse caso precise de autenticação tipo Authorization, i.e., "Authorization *token*"
    public static <S> S createService(Class<S> serviceClass, final String token) {

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient httpClient =
                new OkHttpClient.Builder()
                        .addInterceptor(new Interceptor() {
                            @Override
                            public Response intercept(Chain chain) throws IOException {
                                Request original = chain.request();

                                Request.Builder requestBuilder = original.newBuilder()
                                        .header("Authorization",
                                                token)
                                        .method(original.method(), original.body());

                                Request request = requestBuilder.build();
                                return chain.proceed(request);
                            }
                        })
                        .addInterceptor(interceptor)
                        .connectTimeout(2, TimeUnit.MINUTES)
                        .readTimeout(2, TimeUnit.MINUTES)
                        .writeTimeout(2, TimeUnit.MINUTES)
                        .build();

        Retrofit retrofit = builder.client(httpClient).build();

        return retrofit.create(serviceClass);
    }

    public static Retrofit retrofit() {
        return builder.client(httpClient).build();
    }
}