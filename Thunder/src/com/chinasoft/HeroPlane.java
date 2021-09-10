package com.chinasoft;

import java.awt.image.BufferedImage;

/**
 * @author 涂鏊飞17683866724@163.com
 * @description: TODO 玩家飞机
 * @create 2021-07-26 16:51
 */
public class HeroPlane extends FlyingObject {

    private static BufferedImage[] images;

    static {
        images = new BufferedImage[6];
        for (int i = 0; i < images.length; i++) {
            images[i] = loadImage("./img/hero" + i + ".png");
        }
    }

    //生命条数
    private int life;
    private int doubleFire; //火力 [单发/双发]

    //初始化数据
    public HeroPlane() {
//        super(97, 124);
        width = 97;
        height = 124;
        //出现坐标 背景宽度一半-玩家飞机宽度的一半
        x = 140; // 480/2-97
        y = 400;
        this.life = 3;
        this.doubleFire = 0;
    }

    public void move() {

    }

    //英雄机跟着鼠标移动
    public void moveTo(int x, int y) {
        // 飞机的中心刚好移动到鼠标下面
        this.x = x - this.width / 2;
        this.y = y - this.height / 2;
    }

    //获取英雄机图片
    int index = 0;//定义活着的下标
    int deadIndex = 2;//定义了飞机爆炸时的图片下标

    @Override
    public BufferedImage getImage() {
        if (isLife()) { //活着放的图片
            return images[index++ % 2];
        } else if (isDead()) { //死了放的图片
            BufferedImage img = images[deadIndex++];
            if (deadIndex == images.length) { //图片播放完毕后，将飞机的状态改为删除状态,随后将飞机删除
                state = REMOVE;
            }
            return img;
        }
        return null;
    }

    @Override
    public boolean outOfBounds() {
        return false;
    }


    public Bullet[] shoot() {
        int xStep = this.width / 4;
        int yStep = 20;//子弹发射的高度
        if (doubleFire > 0) { //双发
            Bullet[] bs = new Bullet[2];
            bs[0] = new Bullet(this.x + xStep, this.y - yStep);// 第一个子弹 英雄机坐标+固定宽度，英雄机y的坐标-定长y值
            bs[1] = new Bullet(this.x + 3 * xStep, this.y - yStep); // 2
            doubleFire -= 2;
            return bs;
        } else {//单发
            Bullet[] bs = new Bullet[1];
            bs[0] = new Bullet(this.x + 2 * xStep, this.y - yStep);
            return bs;
        }
    }

    // 加生命
    public void addLife() {
        this.life++;
    }

    // 减生命
    public void subtractLife() {
        this.life--;
    }

    // 获取生命值
    public int getLife() {
        return this.life;
    }

    // +火力
    public void addDoubleFire() {
        doubleFire += 20;
    }

    // 清空火力
    public void clearDoubleFire() {
        doubleFire = 0;
    }
}
