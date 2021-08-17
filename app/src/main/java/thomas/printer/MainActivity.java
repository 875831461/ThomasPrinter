package thomas.printer;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.Manifest;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.thomas.ThomasPrinterManage;

import thomas.printer.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private ThomasPrinterManage.OnThomasPrinterChangeListener mPrinterChangeListener = new ThomasPrinterManage.OnThomasPrinterChangeListener() {
        @Override
        public void onThomasPrinterChanged(@Nullable BluetoothSocket bluetoothSocket, int i) {
            Toast.makeText(MainActivity.this, "" + i, Toast.LENGTH_SHORT).show();

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main);
        ThomasPrinterManage.getInstance().registerOnThomasPrinterChangeListener(mPrinterChangeListener);
        initPermission();
    }

    private void initPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if((checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) ){
                String[] permission = new String[]{
                        Manifest.permission.ACCESS_FINE_LOCATION
                };
                //aaa
                requestPermissions(permission, 11);
            }
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null){
            BluetoothDevice device = data.getParcelableExtra(BluetoothActivity.BLUETOOTH_DEVICE);
            if (device != null){
                binding.buttonConnect.setTag(device.getAddress());
            }
        }
    }

    public void onObtainClick(View view) {
        startActivityForResult(new Intent(this,BluetoothActivity.class),1);
    }

    public void onConnectClick(View view) {
        String address = (String) binding.buttonConnect.getTag();
        System.out.println(address);
        ThomasPrinterManage.getInstance().connectBluetoothDevice(address);
    }

    public void onSendClick(View view) {

    }

    public void onCancelObserveClick(View view) {
        ThomasPrinterManage.getInstance().unregisterOnThomasPrinterChangeListener(mPrinterChangeListener);
    }
}