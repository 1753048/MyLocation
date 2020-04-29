package com.example.location;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.nio.channels.DatagramChannel;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Socket mSocket;
    {
        try {
            mSocket = IO.socket("http://192.168.1.7:3000");
        }
        catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }
    Button btnRegister, btnLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSocket.connect();


        btnLogin = (Button)findViewById(R.id.btnLogin);
        btnRegister = (Button)findViewById(R.id.btnRegister);

        btnLogin.setOnClickListener((View.OnClickListener) this);
        btnRegister.setOnClickListener((View.OnClickListener) this);
    }
    @Override
    public void onClick(View v) {
        if (v.getId() == btnRegister.getId()) {
            Intent myIntentA1A2 = new Intent (MainActivity.this, Register.class);
            startActivityForResult(myIntentA1A2, 1122);
        }
        else if (v.getId() == btnLogin.getId()) {
            Intent login = new Intent (MainActivity.this, Login.class);
            startActivityForResult(login, 1122);
        }
    };
}
