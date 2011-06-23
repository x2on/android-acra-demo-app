package de.felixschulze.androidacrademoapp;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.view.View.OnClickListener;
import android.view.View;

public class HelloAndroid extends Activity implements OnClickListener {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        Button button = (Button) findViewById(R.id.Button01);
        button.setOnClickListener(this);
    }

    public void onClick(View v) {
        System.out.println("Crash");
        Object nul = null;
        nul.toString();
    }
}
