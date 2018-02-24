package notify;

import java.util.List;

import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;

/**
 * MessageList
 *
 * @author Wang Guoxing
 */
@Component
public class MessageList {
    public List<String> getMessages() {
        return messages;
    }

    public void setMessages(List<String> messages) {
        this.messages = messages;
    }

    private List<String> messages = Lists.newArrayList();
}
