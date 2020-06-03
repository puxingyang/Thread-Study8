package lesson8;

import java.util.concurrent.*;

public class ThreadVsThreadPool {

    public static void main(String[] args) {
        /**
         * 1.没使用线程，送快递
         * 自己送快递，再干自己的活
         */
        System.out.println("送快递到北京");
        System.out.println("送快递到上海");
        System.out.println("处理自己业务");

        /**
         * 2.使用手动创建线程，送快递
         * 雇佣两个人，让他们送快递
         * 自己同时处理自己的事
         */
        new Thread(()->{
            System.out.println("送快递到北京");
        }).start();
        new Thread(()->{
            System.out.println("送快递到上海");
        }).start();
            System.out.println("处理自己业务");

        /**
         * 3.使用JDK线程池，送快递
         */
        //创建线程池对象，开了一家快递公司，处理送快递的任务
        ThreadPoolExecutor pool = new ThreadPoolExecutor(
                4,//核心线程数：快递公司正式员工——线程
                10,//最大线程数：快递公司的总员工（正式工+临时工）——线程
                //临时工+空闲时间：正式员工数量不够处理任务时，招聘临时工，超过空闲时间就解雇
                60,//空闲时间数
                TimeUnit.SECONDS,//时间单位
                new ArrayBlockingQueue<>(1000),//阻塞队列
//                new ThreadFactory() {//匿名内部类
//                    @Override
//                    public Thread newThread(Runnable r) {//线程的工厂类：快递公司招聘标准——创建线程的方式
//                        return new Thread(r);
//                    }
//                },
                //拒绝策略：接收新的快递单但此时仓库容量不够存放
//                new ThreadPoolExecutor.AbortPolicy()
//                new ThreadPoolExecutor.CallerRunsPolicy()
//                new ThreadPoolExecutor.DiscardOldestPolicy()
                new ThreadPoolExecutor.DiscardPolicy()
        );
        pool.execute(()->{//创建一个送快递任务，把包裹送给快递公司
            System.out.println("送快递到北京");
        });
        pool.execute(()->{
            System.out.println("送快递到上海");
        });
        pool.execute(()->{
            System.out.println("干自己的事");
        });

        //单线程池：只有1个正式工，没有临时工，仓库是无边界的
        ExecutorService pool1 = Executors.newSingleThreadExecutor();
        //固定大小的线程池：只有给定数量的正式工，没有临时工，仓库是无边界的
        ExecutorService pool2 = Executors.newFixedThreadPool(4);
        //缓存的线程池：只有临时工，没有正式工，临时工数量不限，空闲时间60秒
        ExecutorService pool3 = Executors.newCachedThreadPool();
        //计划任务线程池：给定数量的正式工，没有临时工。使用自己的创建线程的方式（定时任务线程）
        ExecutorService pool4 = Executors.newScheduledThreadPool(4);

    }
}
