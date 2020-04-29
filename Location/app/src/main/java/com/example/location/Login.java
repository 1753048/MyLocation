package com.example.location;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;

public class Login extends AppCompatActivity implements View.OnClickListener {
    Socket mSocket;
    {
        try {
            mSocket = IO.socket("http://192.168.1.7:3000");
        }
        catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }
    Button btnLogin;
    EditText username, password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        mSocket.connect();

        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener((View.OnClickListener) this);

        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);

        mSocket.on("Server-login",onLoginResult);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == btnLogin.getId()) {
            if (username.getText().toString().trim().length() > 0 &&
                    password.getText().toString().trim().length() > 0)
            {
                mSocket.emit("client-login",username.getText().toString(),password.getText().toString());
            }
            else {
                Toast.makeText(Login.this, "Lack of information", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private Emitter.Listener onLoginResult = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject obj = (JSONObject) args[0];
                    try {
                        boolean isExist = obj.getBoolean("isExist");
                        if (!isExist) {
                            Toast.makeText(Login.this, "Username is not correct!",Toast.LENGTH_SHORT).show();
                        }
                        else {
                            boolean isCorrect = obj.getBoolean("isCorrect");
                            if (!isCorrect) {
                                Toast.makeText(Login.this, "Password is not correct", Toast.LENGTH_SHORT).show();
                            } else {
                                // chuyen toi man hinh chinh
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            });
        }
    };
}
