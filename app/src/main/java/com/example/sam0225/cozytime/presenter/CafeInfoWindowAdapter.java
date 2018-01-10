package com.example.sam0225.cozytime.presenter;

import android.view.View;
import android.widget.TextView;

import com.example.sam0225.cozytime.R;
import com.example.sam0225.cozytime.model.Cafe;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.iarcuschin.simpleratingbar.SimpleRatingBar;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Sam0225 on 2018/1/9.
 */

public class CafeInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {
	private static final String TAG = "CafeInfoWindowAdapter";

	@BindView(R.id.tv_title)
	TextView title;
	@BindView(R.id.tv_address)
	TextView address;
	@BindView(R.id.view_wifi)
	View wifi;
	@BindView(R.id.view_seat)
	View seat;
	@BindView(R.id.view_quiet)
	View quiet;
	@BindView(R.id.view_tasty)
	View tasty;
	@BindView(R.id.view_cheap)
	View cheap;
	@BindView(R.id.view_music)
	View music;
	@BindView(R.id.tv_website)
	TextView website;

	private final View mView;
	private final HashMap<String, Cafe> dataList;

	public CafeInfoWindowAdapter(View view, HashMap<String, Cafe> data) {
		mView = view;
		dataList = data;
		ButterKnife.bind(this, mView);

	}

	@Override
	public View getInfoWindow(Marker marker) {
		return null;
	}

	@Override
	public View getInfoContents(Marker marker) {
		Cafe mCafe = dataList.get(marker.getId());
		title.setText(mCafe.getName());
		address.setText(mCafe.getAddress());
		// wifi
		((TextView) wifi.findViewById(R.id.tv_name)).setText(R.string.wifi);
		((SimpleRatingBar) wifi.findViewById(R.id.srb_value)).setRating(mCafe.getWifi());
		// seat
		((TextView) seat.findViewById(R.id.tv_name)).setText(R.string.seat);
		((SimpleRatingBar) seat.findViewById(R.id.srb_value)).setRating(mCafe.getSeat());
		// quiet
		((TextView) quiet.findViewById(R.id.tv_name)).setText(R.string.quiet);
		((SimpleRatingBar) quiet.findViewById(R.id.srb_value)).setRating(mCafe.getQuiet());
		// tasty
		((TextView) tasty.findViewById(R.id.tv_name)).setText(R.string.tasty);
		((SimpleRatingBar) tasty.findViewById(R.id.srb_value)).setRating(mCafe.getTasty());
		// cheap
		((TextView) cheap.findViewById(R.id.tv_name)).setText(R.string.cheap);
		((SimpleRatingBar) cheap.findViewById(R.id.srb_value)).setRating(mCafe.getCheap());
		// music
		((TextView) music.findViewById(R.id.tv_name)).setText(R.string.music);
		((SimpleRatingBar) music.findViewById(R.id.srb_value)).setRating(mCafe.getMusic());
		website.setText(mCafe.getUrl());

		return mView;
	}
}
