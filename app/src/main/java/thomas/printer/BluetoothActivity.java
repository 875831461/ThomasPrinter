package thomas.printer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.SimpleAdapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import thomas.printer.databinding.ActivityBluetoothBinding;

public class BluetoothActivity extends AppCompatActivity {

    private BluetoothReceiver mBluetoothReceiver;
    private ActivityBluetoothBinding binding;
    private ArrayAdapter<BluetoothDevice> mAdapter;
    public static final String BLUETOOTH_DEVICE = "BLUETOOTH_DEVICE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
            actionBar.setDisplayHomeAsUpEnabled(true);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_bluetooth);
        initBroadcast();
        initView();
        startBluetoothDiscovery();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (BluetoothAdapter.getDefaultAdapter().isDiscovering()){
            BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
        }
        unregisterReceiver(mBluetoothReceiver);
    }

    private void initView() {
        mAdapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1);
        mAdapter.setNotifyOnChange(true);
        binding.lvDevices.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                revealDialog(position);
            }
        });
        binding.lvDevices.setAdapter(mAdapter);
    }

    private void revealDialog(int position) {
        final BluetoothDevice bluetoothDevice = mAdapter.getItem(position);
        if (bluetoothDevice != null){
            AlertDialog.Builder builder = new AlertDialog.Builder(this)
                    .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent();
                            intent.putExtra(BLUETOOTH_DEVICE,bluetoothDevice);
                            setResult(RESULT_OK,intent);
                            finish();
                        }
                    })
                    .setTitle(bluetoothDevice.getName());
            builder.create().show();
        }
    }

    private void initBroadcast() {
        mBluetoothReceiver = new BluetoothReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothDevice.ACTION_FOUND);//发现蓝牙动作
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);//搜索结束动作
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);//搜素开始动作
        registerReceiver(mBluetoothReceiver,filter);
    }

    /**
     * Bluetooth BroadcastReceiver in order to get bluetooth devices
     */
    private class BluetoothReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction() != null){
                actionState(intent,intent.getAction());
            }

        }

        private void actionState(Intent intent,String action) {
            BluetoothDevice device;
            switch (action){
                case BluetoothAdapter.ACTION_DISCOVERY_STARTED:
                    System.out.println("开始查询蓝牙设备");
                    break;
                case BluetoothAdapter.ACTION_DISCOVERY_FINISHED:
                    System.out.println("结束查询蓝牙设备");
                    break;
                // when found device
                case BluetoothDevice.ACTION_FOUND:
                    device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    if (device != null){
                        System.out.println(device.getName());
                        System.out.println(device.getAddress());
                        mAdapter.add(device);
                    }
                    break;
            }
        }
    }


    private void startBluetoothDiscovery() {
        // 未打开蓝牙，才需要打开蓝牙
        if (!BluetoothAdapter.getDefaultAdapter().enable()){
            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            // 设置蓝牙可见性，最多300秒   可注释掉
            intent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
            startActivityForResult(intent, 10);
        }else {
            BluetoothAdapter.getDefaultAdapter().startDiscovery();
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

}