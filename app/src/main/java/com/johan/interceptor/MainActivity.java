package com.johan.interceptor;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.login_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
        // 自定义空提示
        EditText passwordView = findViewById(R.id.password_view);
        passwordView.setTag(R.id.check_empty_tip, "请输入6位密码");
    }

    @CheckEmpty({R.id.user_name_view, R.id.password_view})
    public void login() {
        /** 利用Aspectj，AOP代替以下代码
        EditText usernameView = findViewById(R.id.user_name_view);
        if (usernameView.length() == 0) {
            Toast.makeText(this, usernameView.getHint(), Toast.LENGTH_SHORT).show();
            return;
        }
        EditText passwordView = findViewById(R.id.password_view);
        if (passwordView.length() == 0) {
            Toast.makeText(this, passwordView.getHint(), Toast.LENGTH_SHORT).show();
            return;
        }
         */
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }

}
