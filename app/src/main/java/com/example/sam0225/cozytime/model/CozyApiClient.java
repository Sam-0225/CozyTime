package com.example.sam0225.cozytime.model;

import android.content.Context;

import com.example.sam0225.cozytime.rest.CozyApiService;

import retrofit2.Retrofit;

/**
 * Created by Sam0225 on 2018/1/9.
 */

public class CozyApiClient {
	private Context context;

	public static CozyApiClient getInstance(Context context) {
		return new CozyApiClient(context);
	}

	public CozyApiClient(Context context) {
		this.context = context;
	}

	public Retrofit retrofit = new Retrofit.Builder().baseUrl("").build();
	CozyApiService service = retrofit.create(CozyApiService.class);
}
