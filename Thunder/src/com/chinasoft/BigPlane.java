package com.chinasoft;

import java.awt.image.BufferedImage;
import java.util.Random;

/**
 * @author 涂鏊飞17683866724@163.com
 * @description: TODO 大飞机
 * @create 2021-07-26 17:01
 */
public class BigPlane extends FlyingObject implements Enemy{
    private static BufferedImage[] images;

    static {
        images = new BufferedImage[5];
        for (int i = 0; i < images.length; i++) {
            images[i] = loadImage("./img/bigplane" + i + ".png");
        }
    }

    public BigPlane() {
        //调用父类的有参构造
        super(69, 99);
    }

    //大飞机在移动
    public void move() {
        this.y += step;//向下移动
        System.out.println("大飞机在向下移动了" + y);
    }


    /**
     * @description: TODO 大飞机获取图片方法
     * @author: 涂鏊飞abc123567@163.com
     * @date: 2021/7/29 16:24
     * @return: java.awt.image.BufferedImage
     **/
    int index = 1;//定义了飞机爆炸时的图片下标

    @Override
    public BufferedImage getImage() {
        if (isLife()) { //活着放的图片
            return images[0];
        } else if (isDead()) { //死了放的图片
            BufferedImage img = images[index++];
            if (index == images.length) { //图片播放完毕后，将飞机的状态改为删除状态,随后将飞机删除
                state = REMOVE;
            }
            return img;
        }
        return null;
    }

    //大飞机越界
    @Override
    public boolean outOfBounds() {
        return this.y >= World.HEIGHT;
    }

    @Override
    public int getScore() {
        return 3;
    }
}
