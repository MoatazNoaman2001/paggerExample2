package com.example.androidpaggerproject.api;

import static com.example.androidpaggerproject.Utils.Utils.API_KEY;
import static com.example.androidpaggerproject.Utils.Utils.BASE_URL;

import com.example.androidpaggerproject.Utils.Utils;
import com.example.androidpaggerproject.model.MovieResponse;

import io.reactivex.rxjava3.core.Single;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public class APIClient {
    // Define APIInterface
    static APIInterface apiInterface;

    // create retrofit instance
    public static APIInterface getAPIInterface() {
        if (apiInterface == null) {
            OkHttpClient.Builder client = new OkHttpClient.Builder();
            client.addInterceptor(chain -> {
                Request request = chain.request();
                HttpUrl httpUrl = request.url().newBuilder()
                        .addQueryParameter("api_key", API_KEY)
                        .build();
                return chain.proceed(request.newBuilder()
                        .url(httpUrl)
                        .build());
            });

            // Create retrofit instance
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client.build())
                    // Add Gson converter
                    .addConverterFactory(GsonConverterFactory.create())
                    // Add RxJava support for Retrofit
                    .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                    .build();
            // Init APIInterface
            apiInterface = retrofit.create(APIInterface.class);
        }
        return apiInterface;
    }

    //API service interface
    public interface APIInterface {
        // Define Get request with query string parameter as page number
        @GET("movie/popular")
        Single<MovieResponse> getMoviesByPage(@Query("page") int page);
    }
}
