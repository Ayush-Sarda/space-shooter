package com.ayush.main.tools;

import com.ayush.main.SpaceGame;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import static com.ayush.main.screens.MainGameScreen.level;
import static java.lang.Math.min;

public class ScrollingBackground {

    public static final int DEFAULT_SPEED=400;
    public static final int MAX_SPEED=1000;
    public static final int ACCELERATION=100;
    public static final int GOAL_REACH_ACCELEARATION=200;


    Texture image;
    float y1,y2;
    int speed;
    int goalSpeed;
    float imageScale;
    boolean speedFixed;
    public static boolean stop;

    public ScrollingBackground(){
        image = new Texture("stars_background.png");

        speed=0;
        goalSpeed=DEFAULT_SPEED;
        imageScale=SpaceGame.WIDTH/image.getWidth();
        speedFixed=true;

        y1=0;
        y2=image.getHeight();

        stop=false;
    }

    public void updateAndRender(float deltaTime, SpriteBatch batch){
        if(!stop){
            if(!speedFixed){
                int temp=min(level,10);
                goalSpeed=MAX_SPEED+temp*30;
                if(speed<goalSpeed){
                    speed+=ACCELERATION*deltaTime;
                    //speed+=GOAL_REACH_ACCELEARATION*deltaTime;
                    if(speed>goalSpeed)
                        speed=goalSpeed;
                } else if(speed>goalSpeed){
                    //speed-=GOAL_REACH_ACCELEARATION*deltaTime;
                    speed-=ACCELERATION*deltaTime;
                    if(speed<goalSpeed)
                        speed=goalSpeed;
                }
            } else{
                speed=goalSpeed;
            }
            y1-=speed*deltaTime;
            y2-=speed*deltaTime;

            //image reached at the bottom of screem
            if(y1+image.getHeight()*imageScale<=0)
                y1=y2+image.getHeight()*imageScale;

            if(y2+image.getHeight()*imageScale<=0)
                y2=y1+image.getHeight()*imageScale;
        }

        batch.draw(image,0,y1, SpaceGame.WIDTH,image.getHeight()*imageScale);
        batch.draw(image,0,y2, SpaceGame.WIDTH,image.getHeight()*imageScale);

    }



    public void setSpeed(int goalSpeed){
        this.goalSpeed=goalSpeed;
    }

    public void setSpeedFixed(boolean speedFixed){
        this.speedFixed=speedFixed;
    }





}
