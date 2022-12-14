package hw5;

import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;
import java.util.function.Consumer;

public class Main {
    public static final int CARS_COUNT = 4;


    private static final Semaphore tunnelSem = new Semaphore(CARS_COUNT/2);
    private static final CyclicBarrier startWait = new CyclicBarrier(CARS_COUNT + 1);
    private static final CyclicBarrier CdlFinish = new CyclicBarrier(CARS_COUNT +1);
    private static boolean isFinish = false;

    private static final Consumer<Car> finishWin = (car -> {
        synchronized (Main.class){
            if(!isFinish){
                System.out.println(car.getName()+ "  Победил");
                isFinish= true;
            }
        }

    });

    public static void main(String[] args) {
        System.out.println("ВАЖНОЕ ОБЪЯВЛЕНИЕ >>> Подготовка!!!");
        Race race = new Race(new Road(60),
                             new Tunnel(tunnelSem),
                             new Road(40));
        Car[] cars = new Car[CARS_COUNT];
        for (int i = 0; i < cars.length; i++) {
            cars[i] = new Car(race, 20 + (int) (Math.random() * 10),
                    startWait,
                    CdlFinish,
                    finishWin);
        }
        for (int i = 0; i < cars.length; i++) {
            new Thread(cars[i]).start();

        }

        try {
            startWait.await();
        } catch (InterruptedException | BrokenBarrierException e) {
            throw new RuntimeException(e);
        }
        System.out.println("ВАЖНОЕ ОБЪЯВЛЕНИЕ >>> Гонка началась!!!");





        try {
            CdlFinish.await();
        } catch (InterruptedException | BrokenBarrierException e) {
            throw new RuntimeException(e);
        }

        System.out.println("ВАЖНОЕ ОБЪЯВЛЕНИЕ >>> Гонка закончилась!!!");
    }
}





