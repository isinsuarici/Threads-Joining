package ceng.estu.edu;

import java.util.*;

public class Node extends Thread{
    String name;
    ArrayList<Node> waitFor=new ArrayList<>();
    ArrayList<Node> whoIsWaitingForMe=new ArrayList<>();
    boolean isFinished=false;
    public Object lock=new Object();

    Random random = new Random();
    int numOfWaitFor;
    public Node(String name ) {
        this.name=name;
    }


    public String waitforuyazdir(ArrayList<Node> waitFor ){
        String x="";
        for(Node n:waitFor){
            x+=n.name+" ";
        }
        return x;
    }

    public void workIfYouHaveNoConditionOtherwisePlsWait(){

            while (numOfWaitFor>0 ) {
                synchronized (lock) {
                    System.out.println("Node" + this.name + " is waiting for Node " + this.waitforuyazdir(this.waitFor));
                    try {
                        lock.wait(waitFor.size()*2000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
             perform();
    }
    public void unlock(/*Node node*/){ //diğer threadlerin buraya gelip locku açması lazım
        //synchronized (lock) {
            for (Node n : this.whoIsWaitingForMe) {
                n.numOfWaitFor--;
                if (n.numOfWaitFor == 0 && !isFinished) {
                    n.lock.notify();

                }
            }
   //    }

    }
    public void perform() {
        if (!this.isFinished) {
            System.out.println("Node" + this.name + " being started");
            try {
                Thread.sleep(random.nextInt(2000));
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println("Node" +this.name + " is completed");
            this.isFinished=true;
            unlock();

        }
    }
    @Override
    public void run() {
        workIfYouHaveNoConditionOtherwisePlsWait();
    }

}
