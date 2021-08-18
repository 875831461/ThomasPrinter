ThomasPrinter
====
Bluetooth Thermal Printer For Android
-------

# 80
![](http://m.qpic.cn/psc?/V12Wa3Ul4PEo0g/45NBuzDIW489QBoVep5mcQMxHQFIyWiSpXc6J1etK8STPNY1OChohDnc1K.RajEca2wl99BfCpntO7Z*Qnxtzo37rByZPmoIDovww3GTYVc!/b&bo=QAZVCMAP0AsBGTE!&rf=viewer_4)

# 58
![](http://m.qpic.cn/psc?/V12Wa3Ul4PEo0g/45NBuzDIW489QBoVep5mcSAcZxGHTxeEaMSHLc3DtKakMuDKDjNsVNgEo903cbsn9o02Uken34UYZaycu4wgbxes1PKYWk6VRP9Ip*qedfA!/b&bo=QAZVCMAP0AsBGTE!&rf=viewer_4)
# Need permission

```java
  <uses-permission android:name="android.permission.BLUETOOTH_ADMIN" />
  <uses-permission android:name="android.permission.BLUETOOTH" />
  <uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
  <uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />
```
## How to connect bluetooth

  1. if you want to auto connect when device online and device offline notice to you please use:
      (sometimes is connect failed,so I don't recommend it)
```java
      connectBluetoothDevice(Context context, String address)
```
  2. normal connect method:
```java
       connectBluetoothDevice(String address)
```

## How to setting page
  setPageSizeEighty is 80
  setPageSizeFiftyEight is 58