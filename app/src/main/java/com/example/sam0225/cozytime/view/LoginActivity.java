package com.example.sam0225.cozytime.view;

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
import com.example.sam0225.cozytime.presenter.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Sam0225 on 2018/1/8.
 */

public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;

    @BindView(R.id.login_button)
    Button loginBtn;
    @BindView(R.id.account_edit)
    EditText accountEdit;
    @BindView(R.id.password_edit)
    EditText passwordEdit;
    @BindView(R.id.account_layout)
    TextInputLayout accoutLayout;
    @BindView(R.id.password_layout)
    TextInputLayout passwordLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        mAuth = FirebaseAuth.getInstance();
        passwordLayout.setErrorEnabled(true);
        accoutLayout.setErrorEnabled(true);
    }

    @OnClick(R.id.login_button)
    public void onButtonClick(View view) {
        String account = accountEdit.getText().toString();
        String password = passwordEdit.getText().toString();
        if (TextUtils.isEmpty(account)) {
            accoutLayout.setError(getString(R.string.plz_input_accout));//提示輸入帳號
            return;
        }
        if (TextUtils.isEmpty(password)) {
            passwordLayout.setError(getString(R.string.plz_input_pw));//提示輸入密碼
            return;
        }
        accoutLayout.setError("");
        passwordLayout.setError("");
        mAuth.signInWithEmailAndPassword(account, password)
                .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(LoginActivity.this, R.string.login_success, Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent();
                            intent.setClass(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(LoginActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

}
