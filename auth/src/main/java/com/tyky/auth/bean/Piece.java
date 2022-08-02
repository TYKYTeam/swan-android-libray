package com.tyky.auth.bean;

import android.graphics.RectF;

/**
 * 每个圆点所属方块
 */
public class Piece {
    int position;
    /**
     * (x,y)为圆点坐标
     */
    double x;
    double y;
    /**
     * 半径
     */
    double r;

    /**
     * 直径
     */
    double d;

    public Piece(int position, double x, double y, double d) {
        this.position = position;
        this.x = x;
        this.y = y;
        this.d = d;
        this.r = d / 2.0;
    }

    public RectF getRectF() {
        return new RectF((float) (x - r), (float)(y - r), (float)(x + r), (float)(y + r));
    }
    /**
     * 点是否在圆内
     *
     * @return
     */
    public boolean pointIsInCircle(double x1, double y1) {

        return (x - x1) * (x - x1) + (y - y1) * (y - y1) <= r*r;
    }
    /**
     * 点是否在圆内
     *
     * @return
     */
    public boolean pointIsInCircle(double x1, double y1,double tempR) {

        return (x - x1) * (x - x1) + (y - y1) * (y - y1) <= tempR*tempR;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public float getX() {
        return (float) x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public float getY() {
        return (float) y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public float getR() {
        return (float) r;
    }

    public void setR(double r) {
        this.r = r;
    }

    public double getD() {
        return d;
    }

    public void setD(double d) {
        this.d = d;
    }

    @Override
    public String toString() {
        return "Piece{" +
                "position=" + position +
                ", x=" + x +
                ", y=" + y +
                ", r=" + r +
                ", d=" + d +
                '}';
    }
}
