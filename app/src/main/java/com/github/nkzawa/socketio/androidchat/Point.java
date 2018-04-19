package com.github.nkzawa.socketio.androidchat;

/**
 * Created by cmrudi on 19/04/18.
 */

import java.math.BigInteger;

public class Point {
    private BigInteger x;
    private BigInteger y;
    private static final String SEPARATOR = "#";

    public Point() {
        this.x = BigInteger.ZERO;
        this.y = BigInteger.ZERO;
    }

    public Point(String stringPoint) {
        String[] arrayPoint = stringPoint.split(SEPARATOR);
        this.x = new BigInteger(arrayPoint[0]);
        this.y = new  BigInteger(arrayPoint[1]);
    }

    public Point(BigInteger x, BigInteger y) {
        this.x = x;
        this.y = y;
    }

    public BigInteger getX() {
        return x;
    }

    public BigInteger getY() {
        return y;
    }

    public void printPoint() {
        System.out.println(getX() + " " + getY());
    }

    public String toString() {
        return x.toString() + SEPARATOR + y.toString();
    }
}