package com.thomasgassmann.pprog;

import com.thomasgassmann.pprog.producerconsumer.ProducerConsumer;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class MyThread implements Runnable {

    private Lock lock;

    public MyThread(Lock lock) {
        this.lock = lock;
    }

    @Override
    public void run() {

        printMessage("Entered run method...trying to lock");

        lock.lock();
        try{
            printMessage("Lock acquired");
            try{
                Thread.sleep(1000);
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        finally{
            printMessage("Lock released");
            lock.unlock();
        }

        printMessage("End of run method");
    }

    private void printMessage(String msg){
        System.out.println(Thread.currentThread().getName()+" : "+msg);
    }


}

public class Main {

    public static void main(String[] args) {
        System.out.println("== Explicit Lock ==");

        String[] myThreads = {"Thread 1","Thread 2","Thread 3","Thread 4"};

        Lock lock = new ReentrantLock(true);

        for(String threadName:myThreads) {
            new Thread(new MyThread(lock),threadName).start();
        }

    }
}
