package com.jpmc.midascore;

import com.jpmc.midascore.entity.UserRecord;
import jakarta.persistence.*;

@Entity
public class TransactionRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private double amount;

    private double incentive;

    @ManyToOne
    private UserRecord sender;

    @ManyToOne
    private UserRecord recipient;

    public TransactionRecord() {}

    public TransactionRecord(double amount, UserRecord sender, UserRecord recipient, double incentive) {
        this.amount = amount;
        this.sender = sender;
        this.recipient = recipient;
        this.incentive = incentive;
    }

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public UserRecord getSender() {
        return sender;
    }

    public void setSender(UserRecord sender) {
        this.sender = sender;
    }

    public UserRecord getRecipient() {
        return recipient;
    }

    public void setRecipient(UserRecord recipient) {
        this.recipient = recipient;
    }

    public double getIncentive() {
        return incentive;
    }

    public void setIncentive(double incentive) {
        this.incentive = incentive;
    }
}
