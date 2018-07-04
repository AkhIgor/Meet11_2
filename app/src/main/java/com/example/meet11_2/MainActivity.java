package com.example.meet11_2;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 *  Класс является "сервером". Класс биндится к сервису и по нажатию кнопки данные читаются из
 *  переменной сервиса  и выводится на экран.
 */
public class MainActivity extends AppCompatActivity {

    private IMyAidlInterface dataInterface;
    private TextView someText;
    private Button update_btn;

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            dataInterface = IMyAidlInterface.Stub.asInterface(iBinder);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            dataInterface = null;
        }
    };

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        someText = (TextView) findViewById(R.id.textView);
        update_btn = (Button) findViewById(R.id.updating);

        update_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               setText();
            }
        });
    }

    private void setText() {
        try {
            someText.setText(dataInterface.getNecessaryInformation());
        }
        catch (RemoteException ignored) {}
    }

    @Override
    protected void onPause() {
        super.onPause();
        unbindService(connection);
    }

    @Override
    protected void onResume() {
        super.onResume();

        Intent service = new Intent(MainActivity.this, MyService.class);
        service.setAction(IMyAidlInterface.class.getName());
        bindService(service, connection, Context.BIND_AUTO_CREATE);
    }
}
