public class TestDemo2 {
    public static void main(String[] args) throws Exception{
        Thread t1 = new Thread(()->{
            try{
                //Thread.currentThread().sleep(1000);
                synchronized (TestDemo2.class) {
                    System.out.println("t1 start waiting");
                    //wait() method must be called from TestDemo2.class instance, because we are synchronized on it, and this will put t1 into waiting
                    TestDemo2.class.wait();
                    //when got notified by main thread, t1 will continue executing after successfully acquiring the monitor of TestDemo2.class
                    System.out.println("t1 wake up");
                    System.out.println("t1 is done");
                }
                //this will further make t1 to sleep for 10s, so you can see after 10s, the process will exit even if you comment out the line t1.join(), t1.join() only make sure we block Main thread until t1 finished, so it only affects the print sequence of "Bye"
                Thread.currentThread().sleep(10000);
            }catch (Exception ex){}
        });
        t1.start();
        Thread t2 = new Thread(()->{
            try{
                //Thread.currentThread().sleep(1000);
                synchronized (TestDemo2.class) {
                    System.out.println("t2 start waiting");
                    //this will put t2 into waiting
                    TestDemo2.class.wait();
                    //when got notified, t2 will continue executing after successfully acquiring the monitor of TestDemo2.class
                    System.out.println("t2 wake up");
                    System.out.println("t2 is done");
                    //t2 finished and released the monitor of TestDemo2.class
                }
            }catch (Exception ex){}
        });
        t2.start();
        //put Main thread to sleep in order to give time for t1 and t2 to acquire monitor and put themselves into wait state
        Thread.currentThread().sleep(5000);
        try{
            //we need to use this synchronized block in order to let Main thread to acquire the monitor of TestDemo2.class
            synchronized (TestDemo2.class){
                System.out.println("notify all");
                //this notifyAll() method will wake up all the waiting thread, in this case is t1 and t2, so later after the main thread release the monitor, t1 and t2 can compete for the monitor
                TestDemo2.class.notifyAll();
                //after exit this synchronized block,the monitor will be released, and t1 and t2 can start acquiring for the monitor because they were already waken up
            }

        }catch(Exception ex){};
        //this join statement will make sure Main thread print "Bye" after t1 finished all its work
        t1.join();//try to comment this out to see what happened to the "Bye" Statement

        System.out.println("Bye");
    }
}
