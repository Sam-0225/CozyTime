package com.example.sam0225.cozytime.presentation;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.sam0225.cozytime.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by Sam0225 on 2018/1/8.
 */

public class SignUpActivity extends AppCompatActivity {
	private FirebaseAuth mAuth;
	private FirebaseAuth.AuthStateListener mAuthListener;
	private String account;
	private String password;
	private TextInputLayout accoutLayout;
	private TextInputLayout passwordLayout;
	private EditText accountEdit;
	private EditText passwordEdit;
	private Button signUpBtn;
	private FirebaseUser user;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.signup_activity);
		initView();
	}
	private void initView() {
		mAuth = FirebaseAuth.getInstance();
		accountEdit = findViewById(R.id.account_edit);
		passwordEdit = findViewById(R.id.password_edit);
		accoutLayout =  findViewById(R.id.account_layout);
		passwordLayout = findViewById(R.id.password_layout);
		passwordLayout.setErrorEnabled(true);
		accoutLayout.setErrorEnabled(true);
		signUpBtn =  findViewById(R.id.signup_button);

		signUpBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String account = accountEdit.getText().toString();
				String password = passwordEdit.getText().toString();
				if(TextUtils.isEmpty(account)){
					accoutLayout.setError(getString(R.string.plz_input_accout));//提示輸入帳號
					passwordLayout.setError("");
					return;
				}
				if(TextUtils.isEmpty(password)){
					accoutLayout.setError("");
					passwordLayout.setError(getString(R.string.plz_input_pw));//提示輸入密碼
					return;
				}
				accoutLayout.setError("");
				passwordLayout.setError("");
				mAuth.createUserWithEmailAndPassword(account, password)
					.addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
						@Override
						public void onComplete(@NonNull Task<AuthResult> task) {
							if (task.isSuccessful()) {
								Toast.makeText(SignUpActivity.this, R.string.register_success, Toast.LENGTH_SHORT).show();
								Intent intent = new Intent();
								intent.setClass(SignUpActivity.this, MainActivity.class);
								startActivity(intent);
								finish();
							} else {
								Toast.makeText(SignUpActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
							}
						}
					});
			}
		});
	}
}

