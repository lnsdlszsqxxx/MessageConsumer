package famulei.us;

import com.amazonaws.util.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.jms.*;

//Java JMS: MessageListener
public class SQSMessageListener implements MessageListener {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Override
    public void onMessage(Message message) {
        try {
            handleMessage(message);
            message.acknowledge(); //tell the backend, message will be deleted from SQS.
            logger.info("Acknowledged message " + message.getJMSMessageID());
        }
        catch (JMSException e) {
            logger.error("Error processing message: " + e.getMessage());
        }
    }

    private void handleMessage(Message message) throws JMSException {
        logger.info("Got message " + message.getJMSMessageID());
        if (message instanceof TextMessage) {
            TextMessage txtMessage = (TextMessage)message;
            logger.info("Content: " + txtMessage.getText());
        }
        else if (message instanceof BytesMessage){
            BytesMessage byteMessage = (BytesMessage)message;
            byte[] bytes = new byte[(int)byteMessage.getBodyLength()];
            byteMessage.readBytes(bytes);
            logger.info("Content: " +  Base64.encodeAsString(bytes));
        }
        else if (message instanceof ObjectMessage) {
            ObjectMessage objMessage = (ObjectMessage) message;
            logger.info("Content: " + objMessage.getObject());
        }
    }
}
