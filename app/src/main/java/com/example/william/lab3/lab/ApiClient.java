package com.example.william.lab3.lab;
//import com.example.william.lab3.lab.Pais;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

//import rest.example.com.rest.model.Team;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

public class ApiClient {
    private static CountryInterface countryService;

    public static CountryInterface getCountryClient() {
        if (countryService == null) {
            Gson gson = new GsonBuilder()
                    .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                    .create();
            Retrofit restAdapter = new Retrofit.Builder()
                    .baseUrl("https://restcountries.eu")
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();

            countryService = restAdapter.create(CountryInterface.class);
        }

        return countryService;
    }

    public interface CountryInterface {
        @GET("/rest/v1/all")
        Call<List<Pais>> getPais();
    }
}