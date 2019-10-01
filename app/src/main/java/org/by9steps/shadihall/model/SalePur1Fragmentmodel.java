package org.by9steps.shadihall.model;

public class SalePur1Fragmentmodel {
    private String c1num, c2date, c3acname, c4remarks, c5billamount;

    public SalePur1Fragmentmodel(String c1num, String c2date, String c3acname, String c4remarks, String c5billamount) {
        this.c1num = c1num;
        this.c2date = c2date;
        this.c3acname = c3acname;
        this.c4remarks = c4remarks;
        this.c5billamount = c5billamount;
    }

    public SalePur1Fragmentmodel() {
    }

    public String getC1num() {
        return c1num;
    }

    public void setC1num(String c1num) {
        this.c1num = c1num;
    }

    public String getC2date() {
        return c2date;
    }

    public void setC2date(String c2date) {
        this.c2date = c2date;
    }

    public String getC3acname() {
        return c3acname;
    }

    public void setC3acname(String c3acname) {
        this.c3acname = c3acname;
    }

    public String getC4remarks() {
        return c4remarks;
    }

    public void setC4remarks(String c4remarks) {
        this.c4remarks = c4remarks;
    }

    public String getC5billamount() {
        return c5billamount;
    }

    public void setC5billamount(String c5billamount) {
        this.c5billamount = c5billamount;
    }
}
