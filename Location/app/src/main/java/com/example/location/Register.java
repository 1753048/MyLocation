package com.example.location;

import androidx.appcompat.app.AppCompatActivity;

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

public class Register extends AppCompatActivity implements View.OnClickListener {
    Socket mSocket;
    {
        try {
            mSocket = IO.socket("http://192.168.1.7:3000");
        }
        catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }
    EditText username, password, retype, email;
    TextView confirm;
    Button register;
    Boolean check = false;
    Boolean isExist = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        mSocket.connect();

        mSocket.on("Server-checkExist",onCheckExistResult);
        mSocket.on("Server-createUser",onCreateUserResult);

        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        retype = (EditText) findViewById(R.id.retype);
        email = (EditText) findViewById(R.id.email);


        register = (Button)findViewById(R.id.btnRegister);
        register.setOnClickListener((View.OnClickListener) this);

    }
    @Override
    public void onClick(View v) {
        if (v.getId() == register.getId()) {
            if (username.getText().toString().trim().length() > 0 &&
                    password.getText().toString().trim().length() > 0 &&
                    retype.getText().toString().trim().length() > 0 &&
                    email.getText().toString().trim().length() > 0)
            {

                if (password.getText().toString().equals(retype.getText().toString()) == true) {
                    String data[] = new String[3];
                    data[0] = username.getText().toString();
                    data[1] = password.getText().toString();
                    data[2] = email.getText().toString();
                    mSocket.emit("client-createUser", data[0], data[1], data[2]);
                } else {
                    Toast.makeText(Register.this, "Password ! Retype", Toast.LENGTH_SHORT).show();
                }
            }
            else {
                Toast.makeText(Register.this, "Lack of information",Toast.LENGTH_SHORT).show();
            }
        }
    }

    private Emitter.Listener onCheckExistResult = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject obj = (JSONObject) args[0];
                    try {
                        boolean exist = obj.getBoolean("exist");
                        if (exist) {
                            isExist = true;
                            Toast.makeText(Register.this, "This account has existed!",Toast.LENGTH_SHORT).show();
                        }
                        else {
                            isExist = false;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            });
        }
    };

    private Emitter.Listener onCreateUserResult = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject obj = (JSONObject) args[0];
                    try {
                        boolean isSuccess    = obj.getBoolean("isSuccess");
                        if (isSuccess) {
                            Toast.makeText(Register.this, "Success",Toast.LENGTH_SHORT).show();

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            });
        }
    };
}
