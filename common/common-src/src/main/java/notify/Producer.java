package notify;

/**
 * Producer
 *
 * @author Wang Guoxing
 */
public class Producer extends Thread {

    private final MessageList messageList;

    public Producer(MessageList messageList) {
        this.messageList = messageList;
    }

    @Override
    public void run() {
        try {
            while (true) {
                Thread.sleep(3000);
                String msg = String.valueOf(System.currentTimeMillis());
                synchronized(messageList) {
                    messageList.getMessages().add(msg);
                    messageList.notify();
                    //这里只能是notify而不能是notifyAll，否则remove(0)会报java.lang.IndexOutOfBoundsException: Index: 0, Size: 0
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}