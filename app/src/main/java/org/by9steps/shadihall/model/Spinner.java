package org.by9steps.shadihall.model;

public class Spinner {

    String name;
    String debit;
    String credit;
    String acId;
    public Spinner(String name, String debit, String credit) {
        this.name = name;
        this.debit = debit;
        this.credit = credit;
    }

    public Spinner() {
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

    public String getAcId() {
        return acId;
    }

    public void setAcId(String acId) {
        this.acId = acId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDebit(String debit) {
        this.debit = debit;
    }

    public void setCredit(String credit) {
        this.credit = credit;
    }
}
