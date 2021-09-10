package com.chinasoft;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

/**
 * @author 涂鏊飞abc123567@163.com
 * @description: TODO 飞行物父类
 * @create 2021-07-27 11:40
 */
public abstract class FlyingObject {
    int width; //宽度
    int height; //高度
    int x; //x轴坐标
    int y; //y轴坐标
    int step; //移动速度

    //定义状态常量
    public final static int LIFE = 0; // 活着
    public final static int REMOVE = 1; // 可以删除状态
    public final static int DEAD = 2; // 死掉了

    //初始状态值
    int state = LIFE; // 初始状态


    /**
     * @description: TODO 有参的父类构造
     * @author: 涂鏊飞abc123567@163.com
     * @date: 2021/7/27 15:49
     * @Param width:
     * @Param height:
     * @Param x:
     * @Param y:
     * @Param step:
     * @return: null
     **/
    public FlyingObject(int width, int height) {
        this.width = width;
        this.height = height;
        Random r = new Random();
        x = r.nextInt(World.WIDTH - width); //背景图片宽度-飞机宽度
        y = -height;
        step = 2;
    }

    public FlyingObject() {
    }


    //图片加载方法 通过文件的名字加载文件到内存
    public static BufferedImage loadImage(String fileName) {
        try {
            BufferedImage image = ImageIO.read(FlyingObject.class.getResource(fileName));
            return image;
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("图片加载异常!");
        }
    }

    //移动方法
    public abstract void move();

    //状态判断方法
    public boolean isLife() {
        return state == LIFE;
    }

    public boolean isRemove() {
        return state == REMOVE;
    }

    public boolean isDead() {
        return state == DEAD;
    }

    //如果对象已经死亡
    public void goDead() {
        state = DEAD;
    }

    //判断飞行物是否碰撞方法(敌人飞行物和子弹或者英雄机的碰撞)
    public boolean hit(FlyingObject other) {
        int x = other.x;//子弹或英雄机的x
        int y = other.y;//子弹或英雄机的y
        int x1 = this.x - other.width;//敌人的x轴-子弹或者英雄机的宽度
        int y1 = this.y - other.height;//敌人的y轴-子弹或者英雄机的高度
        int x2 = this.x + this.width;//敌人的x加上敌人的宽度
        int y2 = this.y + this.height;//敌人的y加上敌人的高度
        return x >= x1 && x <= x2 && y >= y1 && y <= y2;
    }

    //播放图片 根据各个对象自己的需求自定义图片播放特点
    public abstract BufferedImage getImage();

    //判断敌人飞机是否过了窗口最下边
    public abstract boolean outOfBounds();

    //添加一个画图方法
    public void paint(Graphics g) {
        g.drawImage(getImage(), x, y, null);
    }



}
