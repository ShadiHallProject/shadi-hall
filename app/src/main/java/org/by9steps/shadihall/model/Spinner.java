package org.by9steps.shadihall.model;

public class Spinner {

    String name;
    String debit;
    String credit;

    public Spinner(String name, String debit, String credit) {
        this.name = name;
        this.debit = debit;
        this.credit = credit;
    }

    public String getName() {
        return name;
    }

    public String getDebit() {
        return debit;
    }

    public String getCredit() {
        return credit;
    }
}
