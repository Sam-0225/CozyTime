package com.example.sam0225.cozytime.rest;

import com.example.sam0225.cozytime.model.Cafe;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by Sam0225 on 2018/1/9.
 */

public interface CozyApiService {
	@GET("cafes/{city}")
	Call<List<Cafe>> listCafe(@Path("city") String city);
}
