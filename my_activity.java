package com.example.a11x256.frida_test;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/* loaded from: classes.dex */
public class my_activity extends AppCompatActivity {
    HttpURLConnection conn;
    TextView message_tv;
    EditText password_et;
    EditText username_et;

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.support.v7.app.AppCompatActivity,
              // android.support.v4.app.FragmentActivity,
              // android.support.v4.app.BaseFragmentActivityGingerbread, android.app.Activity
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_activity);
        this.message_tv = (TextView) findViewById(R.id.textView);
        this.username_et = (EditText) findViewById(R.id.editText);
        this.password_et = (EditText) findViewById(R.id.editText2);
        ((Button) findViewById(R.id.button)).setOnClickListener(new View.OnClickListener() { // from class:
                                                                                             // com.example.a11x256.frida_test.my_activity.1
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                my_activity.this.send_data(((Object) my_activity.this.username_et.getText()) + ":"
                        + ((Object) my_activity.this.password_et.getText()));
            }
        });
    }

    void send_data(final String data) {
        try {
            URL url = new URL("http://192.168.18.134");
            try {
                final HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);
                new Thread(new Runnable() { // from class: com.example.a11x256.frida_test.my_activity.2
                    @Override // java.lang.Runnable
                    public void run() {
                        try {
                            DataOutputStream out = new DataOutputStream(conn.getOutputStream());
                            out.writeBytes(my_activity.this.enc(data));
                            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                            final String text = in.readLine();
                            my_activity.this.runOnUiThread(new Runnable() { // from class:
                                                                            // com.example.a11x256.frida_test.my_activity.2.1
                                @Override // java.lang.Runnable
                                public void run() {
                                    ((TextView) my_activity.this.findViewById(R.id.textView)).setText(text);
                                    my_activity.this.dec(text);
                                }
                            });
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            } catch (MalformedURLException e) {
                e = e;
                e.printStackTrace();
            } catch (IOException e2) {
                e = e2;
                e.printStackTrace();
            }
        } catch (MalformedURLException e3) {
            e = e3;
        } catch (IOException e4) {
            e = e4;
        }
    }

    String enc(String data) {
        try {
            Cipher my_cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            my_cipher.init(1, new SecretKeySpec("aaaaaaaaaaaaaaaa".getBytes("UTF-8"), "AES"),
                    new IvParameterSpec("bbbbbbbbbbbbbbbb".getBytes("UTF-8")));
            byte[] x = my_cipher.doFinal(data.getBytes());
            System.out.println(new String(Base64.encode(x, 0)));
            return new String(Base64.encode(x, 0));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        } catch (InvalidAlgorithmParameterException e2) {
            e2.printStackTrace();
            return null;
        } catch (InvalidKeyException e3) {
            e3.printStackTrace();
            return null;
        } catch (NoSuchAlgorithmException e4) {
            e4.printStackTrace();
            return null;
        } catch (BadPaddingException e5) {
            e5.printStackTrace();
            return null;
        } catch (IllegalBlockSizeException e6) {
            e6.printStackTrace();
            return null;
        } catch (NoSuchPaddingException e7) {
            e7.printStackTrace();
            return null;
        }
    }

    String dec(String data) {
        try {
            byte[] decoded_data = Base64.decode(data.getBytes(), 0);
            Cipher my_cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            my_cipher.init(2, new SecretKeySpec("aaaaaaaaaaaaaaaa".getBytes("UTF-8"), "AES"),
                    new IvParameterSpec("bbbbbbbbbbbbbbbb".getBytes("UTF-8")));
            return new String(my_cipher.doFinal(decoded_data));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return "";
        } catch (InvalidAlgorithmParameterException e2) {
            e2.printStackTrace();
            return "";
        } catch (InvalidKeyException e3) {
            e3.printStackTrace();
            return "";
        } catch (NoSuchAlgorithmException e4) {
            e4.printStackTrace();
            return "";
        } catch (BadPaddingException e5) {
            e5.printStackTrace();
            return "";
        } catch (IllegalBlockSizeException e6) {
            e6.printStackTrace();
            return "";
        } catch (NoSuchPaddingException e7) {
            e7.printStackTrace();
            return "";
        }
    }
}