package com.dev.volleydemo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView textView;
    private EditText loginEdt, passwordEdt;

    private boolean userIsConfirmed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initViews();
    }

    private void initViews() {
        textView = findViewById(R.id.resultTxt);
        loginEdt = findViewById(R.id.loginEdt);
        passwordEdt = findViewById(R.id.passwordEdt);
        Button connectBtn = findViewById(R.id.connectBtn);
        connectBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.connectBtn) {
            userIsConfirmed = false;
            String login = loginEdt.getText().toString(), password = passwordEdt.getText().toString();
            getDataFromServer(login, password);
        }
    }

    public void getDataFromServer(final String login, final String password) {
        String url = "http://192.168.1.104/scripts/get_user.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Log.e(TAG, "response = " + response);
                        getDataFromJSONDocument(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("TAG", Log.getStackTraceString(error));
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("login", login);
                params.put("password", password);

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void getDataFromJSONDocument(String result) {
        try {
            if (!result.equals("[]")) {
                JSONObject response = new JSONObject(result);
                if (!response.isNull("User"))
                    userIsConfirmed = true;
                else {
                    Log.e("TAG", result);
                }
            }
        } catch (Exception e) {
            Log.e("TAG", Log.getStackTraceString(e));
        }

        textView.setText(userIsConfirmed ? "Vous êtes la Bienvenue :)" : "Information incorrectes :(");
    }
}
