package notify;

/**
 * Consumer
 *
 * @author Wang Guoxing
 */
public class Consumer extends Thread {

    private final MessageList messageList;

    public Consumer(MessageList messageList) {
        this.messageList = messageList;
    }

    @Override
    public void run() {
        while (true) {
            synchronized(messageList) {
                if(messageList.getMessages().size() == 0) {
                    try {
                        messageList.wait();
                    } catch(InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                String message = messageList.getMessages().remove(0);
                System.out.println("Consumer " + getName() + " get a message:" + message);
            }
        }
    }

    public static void main(String[] args) {
        MessageList messageList = new MessageList();
        Producer p = new Producer(messageList);
        p.start();
        new Consumer(messageList).start();
    }
}