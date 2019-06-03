package encryptorapp.example.com.encryptorapp;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import okhttp3.*;
import org.json.JSONException;
import javax.crypto.*;
import java.io.IOException;
import java.security.*;
import java.security.spec.*;


public class MainActivity extends Activity {
    String username;
    String password;
    ReqBody body ;
    String postUrl= "https://reqres.in/api/users/";


    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final EditText userEditText = (EditText) findViewById(R.id.user);
        final EditText passEditText = (EditText) findViewById(R.id.pass);
        Button DESLogin = (Button) findViewById(R.id.DESButton);
        Button AESLogin = (Button) findViewById(R.id.AESButton);

        try {
            ReqBody.initKeys();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        DESLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                username = userEditText.getText().toString();
                password = passEditText.getText().toString();

                try {
                    body = new ReqBody(username,password);
                    String desBody = body.DESEncryption();
                    postRequest(postUrl,desBody);

                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                } catch (InvalidKeyException e) {
                    e.printStackTrace();
                } catch (NoSuchPaddingException e) {
                    e.printStackTrace();
                } catch (BadPaddingException e) {
                    e.printStackTrace();
                } catch (InvalidKeySpecException e) {
                    e.printStackTrace();
                } catch (IllegalBlockSizeException e) {
                    e.printStackTrace();
                }
            }
        });

        AESLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                username = userEditText.getText().toString();
                password = passEditText.getText().toString();

                try {
                    body = new ReqBody(username,password);
                    String aesBody = body.AESEncryption();
                    postRequest(postUrl,aesBody);
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                } catch (InvalidKeyException e) {
                    e.printStackTrace();
                } catch (InvalidAlgorithmParameterException e) {
                    e.printStackTrace();
                } catch (NoSuchPaddingException e) {
                    e.printStackTrace();
                } catch (BadPaddingException e) {
                    e.printStackTrace();
                } catch (IllegalBlockSizeException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    void postRequest(String postUrl,String postBody) {

        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(JSON, postBody);
        Request request = new Request.Builder()
                .url(postUrl)
                .post(body)
                .build();


        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                call.cancel();
            }

            @Override
            public void onResponse(Call call, Response response) { }
        });
    }
}

