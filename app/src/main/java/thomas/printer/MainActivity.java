package thomas.printer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.Manifest;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import com.thomas.ThermalPrinter;
import com.thomas.ThomasPrinterManage;
import com.thomas.factory.Alignment;
import com.thomas.factory.FontSize;

import java.io.UnsupportedEncodingException;
import java.util.Vector;

import thomas.printer.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    /**
     * register this listener will known printer connect or send state
     */
    private ThomasPrinterManage.OnThomasPrinterChangeListener mPrinterChangeListener = new ThomasPrinterManage.OnThomasPrinterChangeListener() {
        /**
         * when you connect or send data it will tell you
         * @param bluetoothSocket bluetoothSocket
         * @param state printer state
         */
        @Override
        public void onThomasPrinterChanged(@Nullable BluetoothSocket bluetoothSocket, int state) {
            switch (state){
                case ThomasPrinterManage.STATE_DISCONNECTED:
                    System.out.println(getString(R.string.disconnect));
                    break;
                case ThomasPrinterManage.STATE_CONNECTING:
                    System.out.println(getString(R.string.connecting));
                    break;
                case ThomasPrinterManage.STATE_CONNECTED:
                    System.out.println(getString(R.string.connected));
                    break;
                case ThomasPrinterManage.STATE_CONNECTED_FAILED:
                    System.out.println(getString(R.string.connect_failed));
                    break;
                case ThomasPrinterManage.STATE_ADDRESS_ERROR:
                    System.out.println(getString(R.string.address_error));
                    break;
                case ThomasPrinterManage.STATE_NO_DEVICE:
                    System.out.println(getString(R.string.no_device));
                    break;
                case ThomasPrinterManage.STATE_SEND_SUCCESS:
                    System.out.println(getString(R.string.send_success));
                    break;
                case ThomasPrinterManage.STATE_SEND_FAILED:
                    System.out.println(getString(R.string.send_failed));
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main);
        ThomasPrinterManage.getInstance().registerOnThomasPrinterChangeListener(mPrinterChangeListener);
        initPermission();
    }

    /**
     * After Android API 26, you will not be able to search for Bluetooth devices without this location permission
     */
    private void initPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if((checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) ){
                String[] permission = new String[]{
                        Manifest.permission.ACCESS_FINE_LOCATION
                };
                requestPermissions(permission, 11);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // do something here when permission is refuse or ....
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
        ThomasPrinterManage.getInstance().connectBluetoothDevice(address);
    }

    public void onSendClick(View view) {
        Vector<Byte> data = new Vector<>();
        ThermalPrinter thermalPrinter = new ThermalPrinter();
        thermalPrinter.setCommand(data);
        thermalPrinter.initPrint();
        // according to your paper size
        thermalPrinter.setPageSizeEighty();
        //thermalPrinter.setPageSizeFiftyEight();
        thermalPrinter.setWhiteOnBlack();
        thermalPrinter.setAlignment(Alignment.CENTER);
        thermalPrinter.setFontSize(FontSize.LARGER);
        thermalPrinter.setText(getTextByte("White On Black"));
        thermalPrinter.cancelWhiteOnBlack();
        thermalPrinter.printAndFeedLine();
        thermalPrinter.putText(getTextByte("content"), FontSize.NORMAL, Alignment.CENTER,false);
        thermalPrinter.printAndFeedLine();
        thermalPrinter.putText(getTextByte("content"), FontSize.NORMAL, Alignment.RIGHT,true);
        thermalPrinter.printAndFeedLine();
        thermalPrinter.putText(getTextByte("content"), FontSize.BIG, Alignment.RIGHT,false);
        thermalPrinter.printAndFeedLine();
        thermalPrinter.putText(getTextByte("content"), FontSize.LARGER, Alignment.LEFT,false);
        thermalPrinter.printAndFeedLine();
        thermalPrinter.setUnderline(true);
        thermalPrinter.setText(getTextByte("content"));
        thermalPrinter.setUnderline(false);
        thermalPrinter.printAndFeedLine();
        thermalPrinter.putTextColumn(getTextByte("column one"), getTextByte("column two"),FontSize.NORMAL);
        thermalPrinter.putTextColumn(getTextByte("column one"), getTextByte("column two"),FontSize.NORMAL);
        thermalPrinter.putTextColumn(getTextByte("column one"), getTextByte("column two"),FontSize.BIG);
        thermalPrinter.putTextColumn(getTextByte("column one"), getTextByte("column two"),FontSize.NORMAL);
        // not useful for XML images
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.smart);
        thermalPrinter.putBitmap(bitmap);
        thermalPrinter.printBarCode("12345684",Alignment.CENTER,3,80);
        // this need printer support
        thermalPrinter.printQrCode(Alignment.CENTER,3,48,"thomas printer");
        thermalPrinter.setCutPaper();
        // if you connect more than one devices you can use this method
        ThomasPrinterManage.getInstance().writeSendDataAllDevice(data);
    }

    public void onCancelObserveClick(View view) {
        ThomasPrinterManage.getInstance().unregisterOnThomasPrinterChangeListener(mPrinterChangeListener);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ThomasPrinterManage.getInstance().unregisterOnThomasPrinterChangeListener(mPrinterChangeListener);
        // When you exit the application, close the accept print task
        // But the tasks before the shutdown will be executed
        ThomasPrinterManage.getInstance().stopAcceptPrintTask();
    }

    private byte[] getTextByte(@NonNull String content) {
        byte[] bytes = new byte[0];
        try {
            bytes = content.getBytes("GB18030");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return bytes;
    }
}