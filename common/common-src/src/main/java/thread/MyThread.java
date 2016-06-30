/*
 * Copyright (C) 2015 Baidu, Inc. All Rights Reserved.
 */
package thread;

/**
 * Created by wangguoxing on 15-11-16.
 * 卖票例子
 */
//class MyThread implements Runnable{
//    private int tickets = 10;
//    public void run(){
//        while(true){
//            if(tickets > 0){
//                System.out.println(Thread.currentThread().getName() +
//                        " is saling ticket " + tickets--);
//            }
//        }
//    }
//}
/*
上面的程序中，创建了四个线程，每个线程调用的是同一个ThreadTest对象中的run()方法，访问的是同一个对象中的变量（tickets）的实例，
这个程序满足了我们的需求。在Windows上可以启动多个记事本程序一样，也就是多个进程使用同一个记事本程序代码。
*/


class MyThread extends Thread{
    private int ticket = 10;
    public void run(){
        while(true){
            if(ticket > 0){
                System.out.println(Thread.currentThread().getName() +
                        "is saling ticket" + ticket--);
            }
        }
    }

    public static void main(String[] args) throws Exception
    {
//        MyThread mt1 = new MyThread();
//        MyThread mt2 = new MyThread();
//        MyThread mt3 = new MyThread();
//        mt1.start();//每个线程都各卖了10张，共卖了30张票
//        mt2.start();//但实际只有10张票，每个线程都卖自己的票
//        mt3.start();//没有达到资源共享

        //同一个mt，但是在Thread中就不可以，如果用同一个实例化对象mt，就会出现异常
        MyThread mt = new MyThread();
        new Thread(mt).start();
        new Thread(mt).start();
    }

}
/*
从结果上看每个票号都被打印了四次，即四个线程各自卖各自的100张票，而不去卖共同的100张票。
这种情况是怎么造成的呢？我们需要的是，多个线程去处理同一个资源，一个资源只能对应一个对象，在上面的程序中，
我们创建了四个ThreadTest对象，就等于创建了四个资源，每个资源都有100张票，每个线程都在独自处理各自的资源。
*/




/*
可见，实现Runnable接口相对于继承Thread类来说，有如下显著的好处：

(1)适合多个相同程序代码的线程去处理同一资源的情况，把虚拟CPU（线程）同程序的代码，数据有效的分离，较好地体现了面向对象的设计思想。

(2)可以避免由于Java的单继承特性带来的局限。我们经常碰到这样一种情况，即当我们要将已经继承了某一个类的子类放入多线程中，由于一个类不能同时有两个父类，所以不能用继承Thread类的方式，那么，这个类就只能采用实现Runnable接口的方式了。

(3)有利于程序的健壮性，代码能够被多个线程共享，代码与数据是独立的。当多个线程的执行代码来自同一个类的实例时，即称它们共享相同的代码。多个线程操作相同的数据，与它们的代码无关。当共享访问相同的对象是，即它们共享相同的数据。当线程被构造时，需要的代码和数据通过一个对象作为构造函数实参传递进去，这个对象就是一个实现了Runnable接口的类的实例。

 */