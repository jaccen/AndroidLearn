package com.jaccen.androidlearn.IMU;

import androidx.appcompat.app.AppCompatActivity;

public class IMUActivity extends AppCompatActivity implements MainContract.View{
    @Override
    public void updateOrientationSensorDataChanged(float xAngle, float yAngle, float zAngle) {

    }

    @Override
    public void updateGyroSensorDataChanged(float xRotationRate, float yRotationRate, float zRotationRate) {

    }

    @Override
    public void updateAccelerationSensorDataChanged(float xAcceleration, float yAcceleration, float zAcceleration) {

    }

    @Override
    public void updateMagicSensorDataChanged(float xMagic, float yMagic, float zMagic) {

    }
}
