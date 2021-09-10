package com.chinasoft;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * @author 涂鏊飞17683866724@163.com
 * @description: TODO 天空
 * @create 2021-07-26 16:51
 */
public class Sky extends FlyingObject {

    private static BufferedImage images;

    static {
        images = loadImage("./img/background.png");
    }

    //两张背景，坐标y值相反
    private int y1;

    public Sky() {
        //super(480, 850);
        width = World.WIDTH;
        height = World.HEIGHT;
        x = 0;
        y = 0;
        step = 1;
        y1 = -this.height;
    }

    public void move() {
        this.y += step;
        this.y1 += step;
        if (y >= this.height) {
            y = -this.height;
        }
        if (y1 >= this.height) {
            y1 = -this.height;
        }
        System.out.println("天空移动了" + y1);
    }

    @Override
    public BufferedImage getImage() {
        return images;
    }

    @Override
    public boolean outOfBounds() {
        return false;
    }

    //天空重写paint()
    @Override
    public void paint(Graphics g) {
        g.drawImage(getImage(), x, y, null);
        g.drawImage(getImage(), x, y1, null);
    }

}
