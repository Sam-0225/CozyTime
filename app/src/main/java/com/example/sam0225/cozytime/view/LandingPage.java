package com.example.sam0225.cozytime.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.sam0225.cozytime.R;
import com.example.sam0225.cozytime.presenter.LoginActivity;
import com.example.sam0225.cozytime.presenter.SignUpActivity;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Sam0225 on 2018/1/7.
 */

public class LandingPage extends AppCompatActivity {
	private FirebaseAuth mAuth;
	private static LandingPage lInstance;
	private static final int RC_SIGN_IN = 123;

    @BindView(R.id.login)
    Button login;
    @BindView(R.id.sign_up)
    Button signUp;
	@BindView(R.id.express_login)
    Button expressLogin;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		lInstance = this;
		try {
			FirebaseApp.initializeApp(this);
		} catch (Exception e) {
		}
		mAuth = FirebaseAuth.getInstance();
		FirebaseUser user = mAuth.getCurrentUser();

        setContentView(R.layout.landing_page);
        ButterKnife.bind(this);


	}



	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == RC_SIGN_IN) {
			IdpResponse response = IdpResponse.fromResultIntent(data);

			if (resultCode == RESULT_OK) {
				// Successfully signed in
				FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
				// ...
			} else {
				// Sign in failed, check response for error code
				// ...
			}
		}
	}

    @OnClick({R.id.express_login, R.id.login, R.id.sign_up})
    public void onButtonClick(View view) {
        Intent intent = new Intent();
        switch (view.getId()){
            case R.id.express_login:
                // https://firebase.google.com/docs/auth/android/firebaseui?hl=zh-cn
                // https://github.com/firebase/FirebaseUI-Android/blob/master/auth/README.md改天繼續完成
                // Choose authentication providers
                List<AuthUI.IdpConfig> providers = Arrays.asList(
                        new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build(),
                        new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build(),
                        new AuthUI.IdpConfig.Builder(AuthUI.FACEBOOK_PROVIDER).build());
                // Create and launch sign-in intent 實作email、fb、google登入
                startActivityForResult(
                        AuthUI.getInstance()
                                .createSignInIntentBuilder()
                                .setAvailableProviders(providers)
                                .setLogo(R.mipmap.ic_launcher_round_250)  // Set logo
                                .setTheme(R.style.CozyTimeTheme)      // Set theme
                                .build(),
                        RC_SIGN_IN);
                break;
            case R.id.login:
                intent.setClass(LandingPage.this, LoginActivity.class);
                startActivity(intent);
                break;
            case R.id.sign_up:
                intent.setClass(LandingPage.this, SignUpActivity.class);
                startActivity(intent);
                break;
        }
    }

}
