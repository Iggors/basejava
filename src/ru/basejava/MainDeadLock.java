package ru.basejava;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static java.lang.Thread.sleep;

public class MainDeadLock {
    private final Lock lock1 = new ReentrantLock(true);
    private final Lock lock2 = new ReentrantLock(true);

    public static void main(String[] args) {
        MainDeadLock deadlock = new MainDeadLock();
        new Thread(() -> deadlock.operation1(), "Поток №1").start();
        new Thread(() -> deadlock.operation2(), "Поток №2").start();
    }

    public void operation1() {
        lock1.lock();
        System.out.println("Блокировка №1 выполнена. Ожидается блокировка №2.");
        try {
            sleep(50);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        lock2.lock();
        System.out.println("Блокировка №2 выполнена");

        System.out.println("Выполнение операции №1.");

        lock2.unlock();
        lock1.unlock();
    }

    public void operation2() {
        lock2.lock();
        System.out.println("Блокировка №2 выполнена. Ожидается блокировка №1.");
        try {
            sleep(50);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        lock1.lock();
        System.out.println("Блокировка №1 выполнена");

        System.out.println("Выполнение операции №2.");

        lock1.unlock();
        lock2.unlock();
    }
}
