package Controller;

import io.javalin.Javalin;
import io.javalin.http.Context;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import Model.Account;
import Model.Message;
import Service.AccountService;
import Service.MessageService;

import java.util.List;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {
    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */

    AccountService accountService;
    MessageService messageService;

    public SocialMediaController() {
        this.accountService = new AccountService();
        this.messageService = new MessageService();
    }

    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.get("/accounts", this::getAllAccountsHandler);
        app.post("/register", this::createAccountHandler);
        app.post("/login", this::loginHandler);

        app.get("/messages", this::getAllMessagesHandler);
        app.get("/accounts/{posted_by}/messages", this::getMessagesForUserHandler);
        app.get("/messages/{message_id}", this::getMessagesByIDHandler);
        app.post("/messages", this::createMessageHandler);
        app.patch("/messages/{message_id}", this::patchMessageHandler);
        app.delete("/messages/{message_id}", this::deleteMessageByIDHandler);

        return app;
    }

    private void getAllAccountsHandler(Context context) {
        List<Account> accounts = accountService.getAllAccounts();
        context.json(accounts);
    }

    private void createAccountHandler(Context context) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(context.body(), Account.class);
        if (accountService.getAccountByUsername(account.getUsername()) != null || account.getUsername().equals("") || account.getUsername().length() < 4 || account.getPassword().length() < 4) {
            context.status(400);
        } else {
            Account addedAccount = accountService.createAccount(account);
            if (addedAccount != null) {
                context.json(mapper.writeValueAsString(addedAccount));
            } 
            else {
                context.status(500);
            }
        }
    }

    private void loginHandler(Context context) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(context.body(), Account.class);
        if(accountService.getAccountByUsername(account.getUsername()) != null){
            if(accountService.getAccountByUsername(account.getUsername()).getPassword().equals(account.getPassword())){
                context.json(mapper.writeValueAsString(accountService.getAccountByUsername(account.getUsername())));
            }
            else{
                context.status(401);
            }
        }
        else{
            context.status(401);
        }
    }

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private void getAllMessagesHandler(Context context) {
        List<Message> messages = messageService.getAllMessages();
        context.json(messages);
    }

    private void getMessagesByIDHandler(Context context){
        int message_id = Integer.parseInt(context.pathParam("message_id"));
        Message message = messageService.getMessageByID(message_id);
        if(message != null){
            context.json(messageService.getMessageByID(message_id));
        }
    }

    private void getMessagesForUserHandler(Context context){
        int posted_by = Integer.parseInt(context.pathParam("posted_by"));
        List<Message> messages = messageService.getAllMessagesFromUser(posted_by);
        context.json(messages);
    }

    private void createMessageHandler(Context context) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(context.body(), Message.class);

        if (message.getMessage_text().equals("") || message.getMessage_text().length() > 255 || accountService.getAccountByID(message.getPosted_by()) == null) {
            context.status(400);
        } 
        else {
            Message addedMessage = messageService.createMessage(message);
            if (addedMessage != null) {
                context.json(mapper.writeValueAsString(addedMessage));
            } 
            else {
                context.status(500);
            }
        }
    }

    private void patchMessageHandler(Context context) throws JsonProcessingException {
        int message_id = Integer.parseInt(context.pathParam("message_id"));
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(context.body(), Message.class);
        
        if(messageService.getMessageByID(message_id) == null || message.getMessage_text().equals("") || message.getMessage_text().length() > 255){
            context.status(400);
        }
        else{
            Message updatedMessage = messageService.updateMessage(message_id, message.getMessage_text());
            if(updatedMessage != null){
                context.json(mapper.writeValueAsString(updatedMessage));
            }
            else {
                context.status(500);
            }
        }
    }

    private void deleteMessageByIDHandler(Context context) {
        int message_id = Integer.parseInt(context.pathParam("message_id"));
        Message deletedMessage = messageService.deleteMessageById(message_id);

        if(deletedMessage != null){
            context.status(200);
            context.json(deletedMessage);
        }
    }

}