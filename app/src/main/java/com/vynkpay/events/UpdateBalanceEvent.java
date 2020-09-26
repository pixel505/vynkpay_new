package com.vynkpay.events;

public class UpdateBalanceEvent {
    public Double balance;

    public UpdateBalanceEvent(Double balance) {
        this.balance = balance;
    }

    public Double getBalance() {
        return balance;
    }
}
