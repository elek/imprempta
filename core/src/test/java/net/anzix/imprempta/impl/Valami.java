package net.anzix.imprempta.impl;

import groovy.lang.Script;

public class Valami extends Script {
    private String qwe;

    public String ahoj() {
        System.out.println("ahoj");
        return "QWE";
    }

    String getQwe() {
        return qwe;
    }

    void setQwe(String qwe) {
        this.qwe = qwe;
    }

    //@Override
    public Object run() {
        System.out.println("qwe");
        return "qwe";
    }
}
