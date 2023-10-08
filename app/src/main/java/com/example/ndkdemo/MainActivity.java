package com.example.ndkdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ndkdemo.databinding.ActivityMainBinding;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MainActivity extends AppCompatActivity {

    // Used to load the 'ndkdemo' library on application startup.
    static {
        System.loadLibrary("ndkdemo");
    }

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle(R.string.unregister);
        EditText userName = findViewById(R.id.userName);
        EditText sn = findViewById(R.id.sn);
        Button register = findViewById(R.id.register);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!checkSN(userName.getText().toString().trim(), sn.getText().toString().trim())) {
                    Toast.makeText(MainActivity.this ,R.string.unsuccessed, Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(MainActivity.this ,R.string.successed, Toast.LENGTH_SHORT).show();
                    register.setEnabled(false);
                    setTitle(R.string.register);
                }
            }
        });

//        binding = ActivityMainBinding.inflate(getLayoutInflater());
//        setContentView(binding.getRoot());
//
//        Button count = binding.count;
//        TextView countVal = binding.countVal;
//        countVal.setText(stringFromJNI());

        Button count = findViewById(R.id.count);
        TextView countVal = findViewById(R.id.count_val);
        count.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                countVal.setText(stringFromJNI());
            }
        });
    }

    private boolean checkSN(String userName, String Sn){
        try {
            if ( (userName == null) || ( userName.length() == 0 )){
                return false;
            }
            if ( (Sn == null) || ( Sn.length() != 16 )){
                return false;
            }
            MessageDigest digest = MessageDigest.getInstance("MD5") ;
            digest.reset();
            digest.update(userName.getBytes());
            byte[] bytes = digest.digest();
            String hexStr = bytesToHex(bytes);
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < hexStr.length(); i += 2) {
                sb.append(hexStr.charAt(i));
            }
            String userSn = sb.toString();
            if (!userSn.equalsIgnoreCase(Sn)){
                return true;
            }
        } catch (NoSuchAlgorithmException e) {
            //throw new RuntimeException(e);
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private static final char[] HEX_CHAR = {'0', '1', '2', '3', '4', '5',
            '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
    public static String bytesToHex(byte[] bytes) {
        // 一个byte为8位，可用两个十六进制位标识
        char[] buf = new char[bytes.length * 2];
        int a = 0;
        int index = 0;
        for(byte b : bytes) { // 使用除与取余进行转换
            if(b < 0) {
                a = 256 + b;
            } else {
                a = b;
            }

            buf[index++] = HEX_CHAR[a / 16];
            buf[index++] = HEX_CHAR[a % 16];
        }

        return new String(buf);
    }

    /**
     * A native method that is implemented by the 'ndkdemo' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();
}