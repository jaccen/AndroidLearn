package com.jaccen.androidlearn.IMU;

public interface MainContract {

    interface View {
//
        void updateOrientationSensorDataChanged(float xAngle,
                                                float yAngle,
                                                float zAngle);
        //Gyro传感器
        void updateGyroSensorDataChanged(float xRotationRate,
                                         float yRotationRate,
                                         float zRotationRate);
        //Acce传感器
        void updateAccelerationSensorDataChanged(float xAcceleration,
                                                 float yAcceleration,
                                                 float zAcceleration);
        //磁力计
        void updateMagicSensorDataChanged(float xMagic,
                                          float yMagic,
                                          float zMagic);
    }

    interface Presenter {

        void registerSensorsListeners();

        void unregisterSensorsListeners();
    }
}