package com.example.sam0225.cozytime.presentation;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.sam0225.cozytime.R;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by Sam0225 on 2018/1/7.
 */

public class LandingPage extends AppCompatActivity {
	private Button login;
	private Button signUp;
	private FirebaseAuth mAuth;
	private static LandingPage lInstance;
	private static final int RC_SIGN_IN = 123;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		lInstance = this;
		try {
			FirebaseApp.initializeApp(this);
		}
		catch (Exception e) {
		}
		mAuth = FirebaseAuth.getInstance();
		FirebaseUser user = mAuth.getCurrentUser();
		if(user == null){
			setContentView(R.layout.landing_page);
//			startActivityForResult(
//				AuthUI.getInstance()
//					.createSignInIntentBuilder()
//					.setAvailableProviders(
//						Arrays.asList(new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build(),
//							new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build(),
//							new AuthUI.IdpConfig.Builder(AuthUI.FACEBOOK_PROVIDER).build()))
//					.build(),
//				RC_SIGN_IN);
// https://firebase.google.com/docs/auth/android/firebaseui?hl=zh-cn
// https://github.com/firebase/FirebaseUI-Android/blob/master/auth/README.md改天繼續完成
			login =  findViewById(R.id.login);
			signUp =  findViewById(R.id.sign_up);

			login.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent();
					intent.setClass(LandingPage.this, LoginActivity.class);
					startActivity(intent);

				}
			});
			signUp.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent();
					intent.setClass(LandingPage.this, SignUpActivity.class);
					startActivity(intent);
				}
			});
		} else {
			Intent intent = new Intent();
			intent.setClass(LandingPage.this, LoginActivity.class);
			startActivity(intent);
		}
	}
}
