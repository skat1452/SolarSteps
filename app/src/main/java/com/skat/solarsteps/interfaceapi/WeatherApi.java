package com.skat.solarsteps.interfaceapi;

import com.skat.solarsteps.model.WeatherCollectionResponse;

import retrofit2.Call;
import retrofit2.http.GET;


public interface WeatherApi {
        @GET("data/2.5/forecast/city?id=625144&APPID=e5cd837473d78b88cd21ef4e3e43e5dd")
        Call<WeatherCollectionResponse> getWeather ();
}
