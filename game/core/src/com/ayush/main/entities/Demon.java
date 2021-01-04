package com.ayush.main.entities;


import com.ayush.main.SpaceGame;
import com.ayush.main.screens.MainGameScreen;
import com.ayush.main.tools.CollisionRect;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.Random;

import javax.xml.transform.Templates;

import static java.lang.Math.min;

public class Demon {
    public static final int DEMON_SPEED=500;
    public static final int DEMON_WIDTH=250;
    public static final int DEMON_HEIGHT=250;
    public static final int DEMON_POSITION=400;
    public static final int SPEED=300;
    public static final float STOP_TIMER=0.8f;

    public static float health;
    private float x;
    private float y;
    private static Texture texture;
    private static Texture healthBar;
    CollisionRect rect;

    Random random;
    boolean dir;
    public boolean stop;
    float stopTimer;
    int healthMax;
    public static boolean  isSettled;

    public Demon(){
        texture = new Texture("space_ship.png");
        healthBar = new Texture("blank.png");
        x= SpaceGame.WIDTH/2-DEMON_WIDTH/2;
        y=SpaceGame.HEIGHT;
        rect = new CollisionRect(x,y,DEMON_WIDTH,DEMON_HEIGHT);
        random = new Random();
        dir = random.nextBoolean();
        healthMax=min(MainGameScreen.level,3);
        health=1f*healthMax;
        stop=false;
    }

    public void update(float deltaTime) {
        if (y >= SpaceGame.HEIGHT - DEMON_HEIGHT - DEMON_POSITION) {
            y -= SPEED * deltaTime;
        } else {
            isSettled = true;
            if(!stop){
                if (dir) {
                    x -= SPEED * deltaTime;
                    if (x <= 0) {
                        dir = false;
                    }
                } else {
                    x += SPEED * deltaTime;
                    if (x >= SpaceGame.WIDTH - DEMON_WIDTH) {
                        dir = true;
                    }
                }
            }
            else{
                stopTimer+=deltaTime;
                if(stopTimer>=STOP_TIMER) {
                    stopTimer = 0;
                    stop = false;
                }
            }
        }
        rect.move(x,y);
    }

    public void render(SpriteBatch batch){
        if(health>=0.66){
            batch.setColor(new Color((float)(255*(1-health)/(1-0.66)),255,0,255));
        }

        else if(health>=0.33)
            batch.setColor(new Color(255,(float)(255-255*(0.66-health)/(0.66-0.33)),0,255));
        else
            batch.setColor(Color.RED);

        batch.draw(healthBar,x,y+DEMON_HEIGHT+20,DEMON_WIDTH*health/healthMax,12);

        batch.setColor(Color.WHITE);
        batch.draw(texture,x,y,DEMON_WIDTH,DEMON_HEIGHT);

    }

    public CollisionRect getCollisionRect(){
        return rect;
    }

    public float getX(){
        return this.x;
    }

    public float getY(){
        return this.y;
    }



}
