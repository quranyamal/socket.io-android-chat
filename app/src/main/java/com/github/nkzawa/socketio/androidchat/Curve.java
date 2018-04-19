package com.github.nkzawa.socketio.androidchat;

/**
 * Created by cmrudi on 19/04/18.
 */
import java.math.BigInteger;

public class Curve {
    private BigInteger a = new BigInteger("1");
    private BigInteger b = new BigInteger("1");
    private BigInteger p = new BigInteger("1");

    public Curve(BigInteger a, BigInteger b, BigInteger p) {
        this.a = a;
        this.b = b;
        this.p = p;
    }

    public BigInteger getA() {
        return a;
    }

    public BigInteger getB() {
        return b;
    }

    public BigInteger getP() {
        return p;
    }

    public void printCurve() {
        System.out.println(getA() + " " + getB() + " " + getP());
    }
}