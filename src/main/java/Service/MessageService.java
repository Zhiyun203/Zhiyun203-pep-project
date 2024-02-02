package Service;

import Model.Message;
import DAO.MessageDAO;

import java.util.List;

public class MessageService {

    private MessageDAO messageDAO;

    public MessageService(){
        messageDAO = new MessageDAO();
    }

    public MessageService(MessageDAO messageDAO){
        this.messageDAO = messageDAO;
    }

    public List<Message> getAllMessages() {
        return messageDAO.getAllMessages();
    }

    public Message getMessageByID(int message_id){
        return messageDAO.getMessageByID(message_id);
    }

    public List<Message> getAllMessagesFromUser(int posted_by) {
        return messageDAO.getAllMessagesFromUser(posted_by);
    }

    public Message createMessage(Message message) {
        messageDAO.createMessage(message);
        return message;
    }

    public Message updateMessage(int message_id, String message_text){
        Message message = messageDAO.updateMessage(message_id, message_text);
        return message;
    }

    public Message deleteMessageById(int message_id){
        return messageDAO.deleteMessageById(message_id);
    }
}
