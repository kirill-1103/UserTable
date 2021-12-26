package ru.krey.table.model;

import javax.persistence.*;
import java.util.Set;

@Entity
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(length = 1000)
    private String text;
    private String sender;
    private String time;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn
    private Participant recipient;

    public Message(String text,String sender,Participant recipient,String time){
        this.text=text;
        this.sender=sender;
        this.recipient=recipient;
        this.time=time;
    }

    public Message(){}

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public Participant getRecipient() {
        return recipient;
    }

    public void setRecipient(Participant recipient) {
        this.recipient = recipient;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }


}
