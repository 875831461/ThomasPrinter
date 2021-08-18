ThomasPrinter
====
Bluetooth Thermal Printer For Android
-------
![](http://photocq.photo.store.qq.com/psc?/V12Wa3Ul4PEo0g/jkqgNxaPJb7RsklupiKoXbvsqI13FZVRI5eBTK1Z6.LFL6E.2Co2ikxdWdI.q9ZxRIyQMUC55I1FIrPwTX9uwD73TqaU4xx2.gUjAYKsYeI!/b&bo=ZABkAGQAZAADGD0!&rf=viewer_4)
# Need permission

```java
  <uses-permission android:name="android.permission.BLUETOOTH_ADMIN" />
  <uses-permission android:name="android.permission.BLUETOOTH" />
  <uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
  <uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />
```
## How to connect bluetooth

  1. if you want to auto connect when device online and device offline notice to you please use:
```java
      connectBluetoothDevice(Context context, String address)
      (sometimes is connect failed,so I don't recommend it)
```
  2. if you want to auto connect when device online and device offline notice to you please use:
```java
       connectBluetoothDevice(String address)
```

## How to setting page
  setPageSizeEighty is 80
  setPageSizeFiftyEight is 58