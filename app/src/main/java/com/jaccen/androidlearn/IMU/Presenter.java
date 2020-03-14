package com.jaccen.androidlearn.IMU;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Presenter implements MainContract.Presenter
{

    private final MainContract.View view;
    private SensorManager sensorManager;
    private Sensor sensorOrientation;
    private Sensor sensorGyroscope;
    private Sensor sensorAccelerometer;
    private Sensor sensorMagic;

    private SensorEventListener sensorOrientationEvenListener;
    private SensorEventListener sensorGyroscopeEvenListener;
    private SensorEventListener sensorAccelerometerEvenListener;
    private SensorEventListener sensorMagicEvenListener;

    String message = new String();
    DecimalFormat df = new DecimalFormat("#,##0.000");

    public Presenter(MainContract.View view, SensorManager sensorManager) {
        this.view = view;
        this.sensorManager = sensorManager;
        initSensors();
    }
    @Override
    public void registerSensorsListeners() {
        sensorManager.registerListener(sensorOrientationEvenListener,
                sensorOrientation,
                SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(sensorGyroscopeEvenListener,
                sensorGyroscope,
                SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(sensorAccelerometerEvenListener,
                sensorAccelerometer,
                SensorManager.SENSOR_DELAY_NORMAL);

        sensorManager.registerListener(sensorMagicEvenListener,
                sensorMagic,
                SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void unregisterSensorsListeners() {
        sensorManager.unregisterListener(sensorOrientationEvenListener);
        sensorManager.unregisterListener(sensorGyroscopeEvenListener);
        sensorManager.unregisterListener(sensorAccelerometerEvenListener);
        sensorManager.unregisterListener(sensorMagicEvenListener);
    }

    private void initSensors() {
        sensorOrientation = sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
        sensorGyroscope = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        sensorAccelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorMagic = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        initSensorsListeners();
    }

    private void initSensorsListeners() {
        if (sensorMagicEvenListener == null)
        {
            sensorMagicEvenListener = new SensorEventListener() {
                @Override
                public void onSensorChanged(SensorEvent sensorEvent) {
                    // Azimuth, angle between the magnetic north direction and the y-axis,
                    // around the z-axis (0 to 359). 0=North, 90=East, 180=South, 270=West
                    float zAngle = roundFloat(sensorEvent.values[0]);
                    // Pitch, rotation around x-axis (-180 to 180), with positive values
                    // when the z-axis moves toward the y-axis.
                    float xAngle = roundFloat(sensorEvent.values[1]);
                    // Roll, rotation around the y-axis (-90 to 90)
                    // increasing as the device moves clockwise.
                    float yAngle = roundFloat(sensorEvent.values[2]);

                    view.updateMagicSensorDataChanged(xAngle, yAngle, zAngle);

                    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss.SSS");
                    String str = sdf.format(new Date());
                    message = str + " Mig: " + xAngle + "," + yAngle + "," + zAngle + "\n";

                }

                @Override
                public void onAccuracyChanged(Sensor sensor, int i) {
                }
            };

        }
        if (sensorOrientationEvenListener == null) {
            sensorOrientationEvenListener = new SensorEventListener() {
                /**
                 * Sensor.TYPE_ORIENTATION event description:
                 * https://developer.android.com/reference/android/hardware/SensorEvent#sensor.type_orientation-:
                 *
                 * @param sensorEvent: Sensor.TYPE_ORIENTATION event
                 */
                @Override
                public void onSensorChanged(SensorEvent sensorEvent) {
                    // Azimuth, angle between the magnetic north direction and the y-axis,
                    // around the z-axis (0 to 359). 0=North, 90=East, 180=South, 270=West
                    float zAngle = roundFloat(sensorEvent.values[0]);
                    // Pitch, rotation around x-axis (-180 to 180), with positive values
                    // when the z-axis moves toward the y-axis.
                    float xAngle = roundFloat(sensorEvent.values[1]);
                    // Roll, rotation around the y-axis (-90 to 90)
                    // increasing as the device moves clockwise.
                    float yAngle = roundFloat(sensorEvent.values[2]);

                    view.updateOrientationSensorDataChanged(xAngle, yAngle, zAngle);

                    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss.SSS");
                    String str = sdf.format(new Date());
                    message = str + " Ori: " + zAngle + "," + xAngle + "," + yAngle + "\n";

                }

                @Override
                public void onAccuracyChanged(Sensor sensor, int i) {
                }
            };
        }
        if (sensorGyroscopeEvenListener == null) {
            sensorGyroscopeEvenListener = new SensorEventListener() {
                @Override
                public void onSensorChanged(SensorEvent sensorEvent) {
                    // round value to 2 decimal points
                    float xRotationRate = roundFloat(sensorEvent.values[0]);
                    float yRotationRate = roundFloat(sensorEvent.values[1]);
                    float zRotationRate = roundFloat(sensorEvent.values[2]);

                    view.updateGyroSensorDataChanged(xRotationRate, yRotationRate, zRotationRate);

                    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss.SSS");
                    String str = sdf.format(new Date());
                    message = str + " Gyro: " + xRotationRate + "," + yRotationRate + "," + zRotationRate + "\n";
                }

                @Override
                public void onAccuracyChanged(Sensor sensor, int i) {
                }
            };
        }
        if (sensorAccelerometerEvenListener == null) {
            sensorAccelerometerEvenListener = new SensorEventListener() {
                @Override
                public void onSensorChanged(SensorEvent sensorEvent) {
                    // round value to 2 decimal points
                    float xAcceleration = roundFloat(sensorEvent.values[0]);
                    float yAcceleration = roundFloat(sensorEvent.values[1]);
                    float zAcceleration = roundFloat(sensorEvent.values[2]);

                    view.updateAccelerationSensorDataChanged(xAcceleration, yAcceleration, zAcceleration);

                    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss.SSS");
                    String str = sdf.format(new Date());
                    message = str + " Acc:  " + xAcceleration + "," + yAcceleration + "," + zAcceleration + "\n";
                }

                @Override
                public void onAccuracyChanged(Sensor sensor, int i) {
                }
            };
        }
    }

    private float roundFloat(float value) {
        return (float) Math.round(value * 100) / 100;
    }
}