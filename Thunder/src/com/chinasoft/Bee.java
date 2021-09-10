package com.chinasoft;

import java.awt.image.BufferedImage;
import java.util.Random;

/**
 * @author 涂鏊飞17683866724@163.com
 * @description: TODO 蜜蜂
 * @create 2021-07-26 17:01
 */
public class Bee extends FlyingObject implements Award {
    private int xStep; //x轴移动速度
    private int yStep; //y轴移动速度
    private static BufferedImage[] images;

    static {
        images = new BufferedImage[5];
        for (int i = 0; i < images.length; i++) {
            images[i] = loadImage("./img/bee" + i + ".png");
        }
    }

    private int awardType;
    public Bee() {
        super(60, 50);
        this.xStep = 1;
        this.yStep = 2;
        //定义小蜜蜂类型
        Random random=new Random();
        awardType=random.nextInt(2);
    }

    //小蜜蜂在移动
    public void move() {
        //斜着移动
        this.x += xStep;
        this.y += yStep;
        //碰撞到边缘时，改变
        if (x <= 0 || x >= World.WIDTH - this.width) {
            xStep *= -1;
        }
        System.out.println("小蜜蜂在斜着移动了：" + x);
        System.out.println("小蜜蜂在斜着移动了：" + y);
    }


    //小蜜蜂获取图片
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

    //小蜜蜂越界
    @Override
    public boolean outOfBounds() {
        return this.y >= World.HEIGHT;
    }


    //获取奖励类型的方法
    @Override
    public int getType() {
        return awardType;
    }
}
