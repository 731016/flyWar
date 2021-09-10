package com.chinasoft;

import java.awt.image.BufferedImage;

/**
 * @author 涂鏊飞17683866724@163.com
 * @description: TODO 子弹
 * @create 2021-07-26 17:02
 */
public class Bullet extends FlyingObject {
    private static BufferedImage image;

    static {
        image = loadImage("./img/bullet.png");
    }

    public Bullet(int x, int y) {
//        super(8, 14);
        width = 8;
        height = 14;
        this.x = x;
        this.y = y;
        step = 3;
    }

    public void move() {
        this.y -= 2;
        System.out.println("子弹向上移动了" + y);
    }


    //获取子弹的图片
    @Override
    public BufferedImage getImage() {
        if (isLife()) { //活着放的图片
            return image;
        } else if (isDead()) { //死了放的图片
            state = REMOVE;
        }
        return null;
    }

    @Override
    public boolean outOfBounds() {
        return this.y <= -this.height;
    }
}
