package app.project.wishwash.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

public class Message implements Parcelable {
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

    protected Message(Parcel in) {
        messageId = in.readString();
        messageDate = in.readString();
        message = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(messageId);
        dest.writeString(messageDate);
        dest.writeString(message);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Message> CREATOR = new Creator<Message>() {
        @Override
        public Message createFromParcel(Parcel in) {
            return new Message(in);
        }

        @Override
        public Message[] newArray(int size) {
            return new Message[size];
        }
    };

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
