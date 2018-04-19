package com.github.nkzawa.socketio.androidchat;

/**
 * Created by cmrudi on 19/04/18.
 */

import java.util.*;
import java.math.BigInteger;

public class ECCDH {
    private BigInteger _xG = new BigInteger ("6b17d1f2e12c4247f8bce6e563a440f277037d812deb33a0f4a13945d898c296", 16);
    private BigInteger _yG = new BigInteger ("4fe342e2fe1a7f9b8ee7eb4a7c0f9e162bce33576b315ececbb6406837bf51f5", 16);

    BigInteger generatePrivateKey(BigInteger n) {
        return new BigInteger(256, new Random()).mod(n.subtract(BigInteger.ONE)).add(BigInteger.ONE);
    }

    Point generatePublicKey(Curve c) {
        return new Point(_xG, _yG);
    }

    Point additionPoint(Point p, Point q, Curve c) {
        BigInteger xr, yr;

        // BigInteger gradien = (p.getY() - q.getY()) / (p.getX() - q.getX());
        // xr = gradien.pow(2) - p.getX() - q.getX();
        // yr = gradien * (p.getX() - xr) - p.getY();

        BigInteger deltaY = p.getY().subtract(q.getY());
        BigInteger deltaX = p.getX().subtract(q.getX());

        BigInteger gradien = BigInteger.ZERO;
        if (deltaX.gcd(c.getP()) == BigInteger.ONE) {
            // BigInteger gradien = deltaX.modInverse(c.getP()).multiply(deltaY).mod(c.getP());
            gradien  = deltaY.mod(c.getP()).multiply(deltaX.modInverse(c.getP()).mod(c.getP()));
        } else if (deltaY.mod(deltaX.gcd(c.getP())) == BigInteger.ZERO) {
            gradien = deltaY.divide(deltaX).mod(c.getP());
        }


        xr = (gradien.pow(2).subtract(p.getX()).subtract(q.getX())).mod(c.getP());
        yr = (gradien.multiply((p.getX().subtract(xr))).subtract(p.getY())).mod(c.getP());

        return new Point(xr, yr);
    }

    Point doublePoint(Point p, Curve c) {
        BigInteger xr, yr;

        // int gradien = (3 * p.getX() * p.getX() + c.getA()) / (2 * p.getY());
        // xr = gradien * gradien - 2 * p.getX();
        // yr = gradien * (p.getX() - xr) - p.getY();

        // BigInteger gradien = (BigInteger.valueOf(3).multiply(p.getX()).add(c.getA())).divide(BigInteger.valueOf(2).multiply(p.getY()));
        BigInteger deltaY = BigInteger.valueOf(3).multiply(p.getX().pow(2)).add(c.getA());
        BigInteger deltaX = BigInteger.valueOf(2).multiply(p.getY());
        BigInteger gradien = BigInteger.ZERO;
        if (deltaX.gcd(c.getP()) == BigInteger.ONE) {
            // BigInteger gradien = deltaX.modInverse(c.getP()).multiply(deltaY).mod(c.getP());
            gradien  = deltaY.mod(c.getP()).multiply(deltaX.modInverse(c.getP()).mod(c.getP()));
        } else if (deltaY.mod(deltaX.gcd(c.getP())) == BigInteger.ZERO) {
            gradien = deltaY.divide(deltaX).mod(c.getP());
        }


        xr = (gradien.pow(2)).subtract(p.getX().multiply(BigInteger.valueOf(2))).mod(c.getP());
        yr = gradien.multiply((p.getX().subtract(xr))).subtract(p.getY()).mod(c.getP());
        return new Point(xr, yr);
    }

    Point multiplePoint(Point p, BigInteger k, Curve c) {
        // if (k.equals(BigInteger.ONE)) {
        //     return p;
        // } else if (k.equals(BigInteger.valueOf(2))) {
        //     return doublePoint(p, c);
        // } else {
        //     return additionPoint(doublePoint(p, c), multiplePoint(p, k.min(BigInteger.valueOf(2)), c), c);
        // }
        if (k.equals(BigInteger.ONE)) {
            return p;
        } else if (k.equals(BigInteger.valueOf(2))) {
            return doublePoint(p, c);
        } else if (k.mod(BigInteger.valueOf(2)).equals(BigInteger.ZERO)) {
            // return doublePoint(multiplePoint(p, k.divide(BigInteger.valueOf(2)), c), c);
            return multiplePoint(doublePoint(p, c), k.divide(BigInteger.valueOf(2)), c);
        } else if (k.mod(BigInteger.valueOf(2)).equals(BigInteger.ONE)) {
            return additionPoint(multiplePoint(doublePoint(p, c), k.divide(BigInteger.valueOf(2)), c), p, c);
        }
        return new Point(BigInteger.ZERO, BigInteger.ZERO);
    }

    /*public static void main(String[] args) {
        // Test awal
        System.out.println("Hello world");

        ECCDH ecc = new ECCDH();

        BigInteger _p = new BigInteger ("ffffffff00000001000000000000000000000000ffffffffffffffffffffffff", 16);
        BigInteger _a = new BigInteger ("ffffffff00000001000000000000000000000000fffffffffffffffffffffffc", 16);
        BigInteger _b = new BigInteger ("5ac635d8aa3a93e7b3ebbd55769886bc651d06b0cc53b0f63bce3c3e27d2604b", 16);

        BigInteger _xG = new BigInteger ("6b17d1f2e12c4247f8bce6e563a440f277037d812deb33a0f4a13945d898c296", 16);
        BigInteger _yG = new BigInteger ("4fe342e2fe1a7f9b8ee7eb4a7c0f9e162bce33576b315ececbb6406837bf51f5", 16);
        BigInteger _n = new BigInteger ("ffffffff00000000ffffffffffffffffbce6faada7179e84f3b9cac2fc632551", 16);

        Curve c = new Curve(_a, _b, _p);
        BigInteger privateKey = ecc.generatePrivateKey(_n);
        Point basePoint = new Point(_xG, _yG);
        Point publicKey = ecc.multiplePoint(basePoint, privateKey, c);

        System.out.println(privateKey);
        publicKey.printPoint();
    }
    */
}
