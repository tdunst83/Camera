package com.develogical.camera;

public class Camera {

    Sensor sensor;
    MemoryCard memCard;
    boolean status_on;
    boolean writing;

    public Camera(Sensor sensor) {
        this.sensor = sensor;
        this.status_on = false;
        this.writing = false;
    }

    public Camera(Sensor sensor, MemoryCard memCard) {
        this.sensor = sensor;
        this.memCard = memCard;
        this.status_on = false;
        this.writing = false;
    }

    public void pressShutter() {

        if(this.status_on) {
            this.writing = true;
            byte[] data = this.sensor.readData();

            this.memCard.write(data, new WriteCompleteListener() {

                @Override
                public void writeComplete() {
                    Camera.this.writing = false;
                    Camera.this.powerOff();

                }
            });

        }

    }

    public void powerOn() {
        sensor.powerUp();
        this.status_on=true;
    }

    public void powerOff() {
        if (!this.writing) {
            sensor.powerDown();
        }
       this.status_on=false;
    }
}

