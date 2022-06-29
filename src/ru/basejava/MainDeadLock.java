package ru.basejava;

public class MainDeadLock {

    public static final Object RESOURCE_1 = new Object();
    public static final Object RESOURCE_2 = new Object();

    private static void monitorLock(Object resource1, Object resource2) {
        System.out.println("Попытка захватить монитор объекта " + resource1 + " потоком " + Thread.currentThread().getName());
        // Блокируем resource1
        synchronized (resource1) {
            System.out.println("Захвачен монитор объекта " + resource1 + " потоком " + Thread.currentThread().getName());
            System.out.println("Попытка захватить монитор объекта " + resource1 + " потоком " + Thread.currentThread().getName());

            //Ставим поток на паузу (симулируем некую деятельность)
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            // Блокируем resource2
            synchronized (resource2) {
                System.out.println("Захвачен монитор объекта " + resource2 + " потоком " + Thread.currentThread().getName());
            }
        }
    }

    public static void main(String[] args) {
        Thread t1 = new Thread(() -> monitorLock(RESOURCE_1, RESOURCE_2));
        Thread t2 = new Thread(() -> monitorLock(RESOURCE_2, RESOURCE_1));
        t1.start();
        t2.start();
    }
}
