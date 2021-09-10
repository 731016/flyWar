package com.chinasoft;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @author 涂鏊飞17683866724@163.com
 * @description: TODO 窗口类 继承JPanel，使用画布
 * @create 2021-07-26 16:50
 */
public class World extends JPanel {
    public final static int WIDTH = 400;//宽
    public final static int HEIGHT = 700;//高

    public final static int START = 0;//启动状态
    public final static int RUNNING = 1;//运行状态
    public final static int PAUSE = 2;//暂停状态
    public final static int GAMEOVER = 3; //结束
    private int state = START; // 定义游戏状态值

    private HeroPlane heroPlane = new HeroPlane();
    private Sky sky = new Sky();
    //飞机对象数组
//    private AirPlane[] airPlane = {};
//    private BigPlane[] bigPlane = {};
    //蜜蜂对象数组
//    private Bee[] bee = {};
    //子弹对象数组
    private Bullet[] bullet = {};
    //敌机对象数组，包含小飞机，大飞机，小蜜蜂
    private FlyingObject[] enemies = {};

    private static BufferedImage start; // 启动
    private static BufferedImage gameover; // 暂停
    private static BufferedImage pause; // 暂停

    //加载启动，暂停，游戏结束图片
    static {
        start = FlyingObject.loadImage("./img/start.png");
        gameover = FlyingObject.loadImage("./img/gameover.png");
        pause = FlyingObject.loadImage("./img/pause.png");
    }

    public static void main(String[] args) {
        World world = new World();
        JFrame jFrame = new JFrame("飞机大战");
        jFrame.add(world);
        jFrame.setSize(World.WIDTH, World.HEIGHT); //设置尺寸
        jFrame.setLocationRelativeTo(null); //设置窗口位置 居中
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//设置程序根据窗口关闭时关闭
        jFrame.setVisible(true); //设置窗口可见
        jFrame.setResizable(false);//设置禁止窗口缩放
        world.startUp(); //启动
    }

    /**
     * @description: TODO 启动程序相关
     * @author: 涂鏊飞abc123567@163.com
     * @date: 2021/7/28 14:49
     * @return: void
     **/
    public void startUp() {
        //鼠标监听器
        MouseAdapter m = new MouseAdapter() {
            //点击
            @Override
            public void mouseClicked(MouseEvent e) {
                switch (state) {
                    case START: // 是开始状态，将状态改为运行
                        state = RUNNING;
                        break;
                    case GAMEOVER: //是结束，将状态改为开始
                        sky = new Sky();
                        heroPlane = new HeroPlane();
                        enemies = new FlyingObject[0];
                        bullet = new Bullet[0];
                        score = 0;
                        state = START;
                        break;
                }
            }

            //进入
            @Override
            public void mouseEntered(MouseEvent e) {
                //将状态置换
                if (state == PAUSE) { //暂停并运行
                    state = RUNNING;
                }
            }

            //移出
            @Override
            public void mouseExited(MouseEvent e) {
                if (state == RUNNING) { //运行并暂停
                    state = PAUSE;
                }
            }

            //移动
            @Override
            public void mouseMoved(MouseEvent e) {
                if (state == RUNNING) {
                    int x = e.getX();
                    int y = e.getY();
                    heroPlane.moveTo(x, y); // 英雄机移动到对应位置
                }
            }
        };
        this.addMouseListener(m); // 鼠标监听事件
        this.addMouseMotionListener(m);

        Timer timer = new Timer();
        long time = 10L;
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (state == RUNNING) {
                    //敌机入场
                    enterAction();
                    //敌机移动
                    moveAction();
                    //子弹出现位置
                    shootAction();
                    //飞机越界
                    outOfBoundsAction();
                    //子弹和敌人碰撞
                    bulletBang();
                    //英雄机碰撞
                    heroPlaneBang();
                    //检查游戏是否结束
                    checkGameOverAction();
                }
                //页面重绘
                repaint();
            }
        }, time, time);// 定时器方法
    }


    @Override
    public void paint(Graphics g) {
        sky.paint(g);
        heroPlane.paint(g);
        for (int i = 0; i < enemies.length; i++) {
            enemies[i].paint(g);
        }
        for (int i = 0; i < bullet.length; i++) {
            bullet[i].paint(g);
        }
        //画分数
        g.drawString("SCORE:" + score, 10, 25);
        g.drawString("LIFE：" + heroPlane.getLife(), 10, 45);
        switch (state){
            case START: // 画启动
                g.drawImage(start,0,0,null);
                break;
            case PAUSE: // 画暂停
                g.drawImage(pause,0,0,null);
                break;
            case GAMEOVER: //画游戏结束
                g.drawImage(gameover,0,0,null);
                break;
        }

    }

    //敌机入场方法
    //将创建出来的敌机对象放入到数组中
    //每10ms 生成1个飞机对象 太快了！！ 要降低出现速度
    //在10ms中生产出来的数量降低
    int index = 0;

    private void enterAction() {
        index++;
        if (index % 40 == 0) {//每400ms生产1个
            FlyingObject obj = nextOne();//将生产的飞机追加到数组中
            //数组扩容
            enemies = Arrays.copyOf(enemies, enemies.length + 1);
            enemies[enemies.length - 1] = obj;
        }
    }

    //创建一些敌机 包含小飞机，大飞机，小蜜蜂
    //返回值为父类
    private FlyingObject nextOne() {
        //各个敌机出现的比率
        //50%小飞机 35%大飞机 15%小蜜蜂
        //随机数生成
        // 0-50
        // 51-70
        // 71-99
        Random random = new Random();
        int rand = random.nextInt(100);
        if (rand <= 50) {
            return new AirPlane();
        } else if (rand <= 70) {
            return new BigPlane();
        } else {
            return new Bee();
        }
    }

    //所有飞机物移动方法
    private void moveAction() {
        sky.move(); // 天空移动
        for (int i = 0; i < bullet.length; i++) {
            bullet[i].move(); // 子弹移动
        }
        for (int i = 0; i < enemies.length; i++) {
            enemies[i].move(); // 向上造型
        }
    }

    // 子弹发射位置 需要降低子弹发射速度
    int shootIndex = 0;

    private void shootAction() {
        shootIndex++;
        if (shootIndex % 30 == 0) { // 300ms发射一颗子弹
            Bullet[] bs = heroPlane.shoot();
            bullet = Arrays.copyOf(bullet, bullet.length + bs.length);
            System.arraycopy(bs, 0, bullet, bullet.length - bs.length, bs.length);
        }
    }

    //飞机越界处理方法 小飞机 大飞机 小蜜蜂 子弹
    //删除已经越界的元素
    private void outOfBoundsAction() {
        int index = 0; // 记录存活下来的元素下标
        // 定义一个飞行物同等长度的数组，将屏幕中可用的元素存储
        FlyingObject[] enemiesFlying = new FlyingObject[enemies.length];
        for (int i = 0; i < enemies.length; i++) {
            FlyingObject obj = enemies[i];
            // 判断元素是否越界和处于删除状态
            if (!obj.outOfBounds() && !obj.isRemove()) {
                enemiesFlying[index] = obj; //未删除就添加
                index++;
            }
        }
        //缩容数组，保留活着的元素
        enemies = Arrays.copyOf(enemiesFlying, index);
        //处理子弹
        index = 0;
        Bullet[] bulletFlying = new Bullet[bullet.length];
        for (int i = 0; i < bullet.length; i++) {
            Bullet b = bullet[i];
            if (!b.outOfBounds() && !b.isRemove()) {
                bulletFlying[index] = b;
                index++;
            }
        }
        //缩容数组，保留活着的元素
        bullet = Arrays.copyOf(bulletFlying, index);
    }

    //子弹碰撞
    int score = 0; // 分数


    private void bulletBang() {
        //找出所有子弹
        for (int i = 0; i < bullet.length; i++) {
            //获取子弹
            Bullet b = bullet[i];
            for (int j = 0; j < enemies.length; j++) {
                //获取敌人
                FlyingObject obj = enemies[j];
                //判断子弹和飞行物是否活着,判断是否发生碰撞
                if (b.isLife() && obj.isLife() && obj.hit(b)) {
                    obj.goDead(); // 发生碰撞后状态修改
                    b.goDead();
                    //判断分数
                    if (obj instanceof Enemy) {
                        Enemy e = (Enemy) obj;
                        score += e.getScore();
                    }
                    if (obj instanceof Award) { // 小蜜蜂
                        Award bee = (Award) obj;
                        int awardType = bee.getType(); // 获取小蜜蜂类型
                        //添加对应奖励，加生命，加分数
                        switch (awardType) {
                            case Award.LIFE:
                                heroPlane.addLife();
                                break;
                            case Award.DOUBLE_FIRE:
                                heroPlane.addDoubleFire();
                                break;
                        }
                    }
                }
            }
        }
    }

    //检查英雄机的碰撞
    private void heroPlaneBang() {
        for (int i = 0; i < enemies.length; i++) {
            FlyingObject obj = enemies[i];
            if (obj.isLife() && obj.hit(heroPlane)) {
                obj.goDead();
                heroPlane.clearDoubleFire(); // 清空火力
                heroPlane.subtractLife(); /// 生命值减1
            }
        }
    }

    //检查游戏是否结束
    private void checkGameOverAction() {
        if (heroPlane.getLife() <= 0) {
            heroPlane.goDead();
            //运行状态更改
            state = GAMEOVER;
        }
    }

}
