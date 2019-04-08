package org.by9steps.shadihall.model;

import com.orm.SugarRecord;

public class CBSetting extends SugarRecord {

    Boolean dste;
    Boolean cbId;
    Boolean debit;
    Boolean credit;
    Boolean remarks;
    Boolean amount;

    public CBSetting(Boolean dste, Boolean cbId, Boolean debit, Boolean credit, Boolean remarks, Boolean amount) {
        this.dste = dste;
        this.cbId = cbId;
        this.debit = debit;
        this.credit = credit;
        this.remarks = remarks;
        this.amount = amount;
    }

    public CBSetting() {
    }

    public Boolean getDste() {
        return dste;
    }

    public Boolean getCbId() {
        return cbId;
    }

    public Boolean getDebit() {
        return debit;
    }

    public Boolean getCredit() {
        return credit;
    }

    public Boolean getRemarks() {
        return remarks;
    }

    public Boolean getAmount() {
        return amount;
    }

    public void setDste(Boolean dste) {
        this.dste = dste;
    }

    public void setCbId(Boolean cbId) {
        this.cbId = cbId;
    }

    public void setDebit(Boolean debit) {
        this.debit = debit;
    }

    public void setCredit(Boolean credit) {
        this.credit = credit;
    }

    public void setRemarks(Boolean remarks) {
        this.remarks = remarks;
    }

    public void setAmount(Boolean amount) {
        this.amount = amount;
    }
}
