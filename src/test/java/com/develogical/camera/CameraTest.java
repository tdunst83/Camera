package com.develogical.camera;

import org.junit.Test;
import org.mockito.*;

import static org.mockito.Mockito.*;

public class CameraTest {
    @Test
    public void switchingTheCameraOnPowersUpTheSensor() {

        Sensor sensor = mock(Sensor.class);

        Camera camera = new Camera(sensor);

        camera.powerOn();
        verify(sensor).powerUp();
    }

    @Test
    public void switchingTheCameraOffPowersDownTheSensor() {
        Sensor sensor = mock(Sensor.class);

        Camera camera = new Camera(sensor);

        camera.powerOff();
        verify(sensor).powerDown();
    }


    @Test
    public void pressingShutterCopiesDataFromSensorToMemoryWhenPowersOn(){

        Sensor sensor = mock(Sensor.class);
        MemoryCard memCard = mock(MemoryCard.class);

        Camera camera = new Camera(sensor, memCard);

        camera.powerOn();
        camera.pressShutter();

        verify(memCard).write(eq(sensor.readData()), any(WriteCompleteListener.class));

    }

    @Test
    public void pressingShutterCopiesDataFromSensorToMemoryWhenPowersOff(){

        Sensor sensor = mock(Sensor.class);
        MemoryCard memCard = mock(MemoryCard.class);

        Camera camera = new Camera(sensor, memCard);

        camera.powerOff();
        camera.pressShutter();
        verify(memCard, never()).write(any(), any());
    }

    @Test
    public void dataCurrentlyWrittenNoPowerDownBySwitchingOffTheCamera(){

        Sensor sensor = mock(Sensor.class);
        MemoryCard memCard = mock(MemoryCard.class);

        Camera camera = new Camera(sensor, memCard);


        camera.powerOn();
        camera.pressShutter();
        camera.powerOff();

        verify(sensor).powerUp();
        verify(sensor, never()).powerDown();
    }

    @Test
    public void onceWritingDataCompleteTheCameraPowersDownTheSensor(){

        Sensor sensor = mock(Sensor.class);
        MemoryCard memCard = mock(MemoryCard.class);

        ArgumentCaptor<WriteCompleteListener> argument = ArgumentCaptor.forClass(WriteCompleteListener.class);

        Camera camera = new Camera(sensor, memCard);

        camera.powerOn();
        camera.pressShutter();

        verify(memCard).write(eq(sensor.readData()), argument.capture());
        verify(sensor, never()).powerDown();

        argument.getValue().writeComplete();

        verify(sensor).powerDown();
    }



}
