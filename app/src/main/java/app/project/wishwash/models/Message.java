package app.project.wishwash.models;

import java.util.Date;

public class Message {
    private String messageId;
    private String messageDate;
    private String message;
    private User sender;
    private User receiver;

    public Message() {
    }

    public Message(String messageId , String messageDate , String message , User sender , User receiver) {
        this.messageId = messageId;
        this.messageDate = messageDate;
        this.message = message;
        this.sender = sender;
        this.receiver = receiver;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getMessageDate() {
        return messageDate;
    }

    public void setMessageDate(String messageDate) {
        this.messageDate = messageDate;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public User getReceiver() {
        return receiver;
    }

    public void setReceiver(User receiver) {
        this.receiver = receiver;
    }
}
