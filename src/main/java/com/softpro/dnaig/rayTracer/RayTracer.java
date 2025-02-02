package com.softpro.dnaig.rayTracer;


import com.softpro.dnaig.ApplicationController;
import com.softpro.dnaig.Output;
import com.softpro.dnaig.utils.Config;
import com.softpro.dnaig.utils.Vector3D;
import javafx.application.Platform;
import javafx.concurrent.Task;

import java.io.IOException;
import java.util.Arrays;

public class RayTracer implements CallbackInterface {

    static Camera camera;
    int n = 0;
    private int threadsFinished = 0;
    private long startTime;

    public int configured = 0;

    private final Thread[] threads = new Thread[Config.getInstance().getTHREADS()];

    private final ApplicationController applicationController;

    public RayTracer(ApplicationController applicationController) {
        this.applicationController = applicationController;
    }

    public boolean configurationComplete(){
        return configured>=Config.getInstance().getTHREADS();
    }

    int current = -1;
    int threadCount = -1;
    public synchronized int getCurrent() {
        return ++current;
    }
    public synchronized int getThreadId() {
        return ++threadCount;
    }

    public void trace() throws IOException {
        startTime = System.currentTimeMillis();
        camera = CustomScene.getScene().camera;

        int work_w = Config.getInstance().getWIDTH() / Config.getInstance().getTILES();
        int work_h = Config.getInstance().getHEIGHT() / Config.getInstance().getTILES();

        for (int a = 0; a < Config.getInstance().getTHREADS(); a++) {
            Task<Void> task = new Task<>() {
                @Override
                protected Void call() throws IOException {

                    int work = getCurrent();
                    int tid = getThreadId();
                    configured++;
                    //System.out.printf("Got thread id %d\n", tid);

                    int start_x = work_w * (work % Config.getInstance().getTILES());
                    int start_y = work_h * (work / Config.getInstance().getTILES());

                    while (work < Config.getInstance().getTILES()*Config.getInstance().getTILES()) {
                        //System.out.printf("Thread %d working on task %d\n", tid, work);

                        for (int i = start_x; i < start_x + work_w; i++) {
                            //System.out.println(i);
                            //System.out.printf("Task %d: work=%d i=%d\n", tid, work, i);
                            for (int j = start_y; j < start_y + work_h; j++) {
                                //System.out.println(j);
                                //System.out.printf("Task %d: work=%d i=%d j=%d\n", tid, work, i, j);
                                // check if RayTracer thread was asked to cancel
                                if (Output.getOutput().isThreadCancelled()) {
                                    try {
                                        Thread.currentThread().join();
                                    } catch (InterruptedException e) {
                                        throw new RuntimeException(e);
                                    }
                                }

                                //System.out.printf("Task %d: work=%d i=%d j=%d no cancel\n", tid, work, i, j);

                                double u= camera.getL() + i + 0.5;
                                //u = u/200;
                                double v= camera.getT() - (j+0.5);
                                //v = v/200;
                                Vector3D s = Util.add(camera.getU().scalarMultiplication(u), camera.getV().scalarMultiplication(v), camera.getW_d_negated());
                                //Vector3D s = Util.add(camera.getU().scalarMultiplication(u), camera.getV().scalarMultiplication(v), camera.getScreen()).subtract(camera.getEye());
                                Vector3D dir = s.normalize();

                                //System.out.printf("Task %d: work=%d i=%d j=%d dir\n", tid, work, i, j);

                                n++;
                                Ray r = new Ray(camera.getEye(), dir);

                                //System.out.printf("Task %d: work=%d i=%d j=%d ray\n", tid, work, i, j);

                                int res_color = r.castPrimary(0);

                                //System.out.printf("Task %d: work=%d i=%d j=%d cast\n", tid, work, i, j);

                                Output output = Output.getOutput();

                                //System.out.printf("Task %d: work=%d i=%d j=%d output\n", tid, work, i, j);

                                try {
                                    output.setPixelTest(tid, i, j, res_color, work);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                //System.out.printf("Task %d: work=%d i=%d j=%d pixel\n", tid, work, i, j);
                            }
                        }
                        //System.out.printf("Thread %d finished work %d\n", tid, work);
                        while (!configurationComplete()){
                            System.out.println("waiting");
                        }
                        work = getCurrent();
                        start_x = work_w * (work % Config.getInstance().getTILES());
                        start_y = work_h * (work / Config.getInstance().getTILES());
                        //throw new RuntimeException("test");
                        //System.out.printf("got new work at x: %d y: %d\n", start_x, start_y);
                    }
                    return null;
                }
            };
            RTRunnable runnable = new RTRunnable(task, this);
            Thread thread = new Thread(runnable);
            threads[a] = thread;
        }
        for (Thread thread : threads) {
            System.out.println("starting thread");
            thread.start();
        }
    }

    @Override
    public void taskDone(String details) {
        threadsFinished++;

        if (threadsFinished >= Config.getInstance().getTHREADS()) {
            System.out.println("Time: " + (System.currentTimeMillis() - startTime) / 1000.0 + "s");
            Platform.runLater(() -> applicationController.callbackWhenRayTracerFinished(null));
            Output.getOutput().clearPixelTest();
        }
    }
}