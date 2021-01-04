package com.ayush.main.entities;

import com.ayush.main.SpaceGame;
import com.ayush.main.screens.MainGameScreen;
import com.ayush.main.tools.CollisionRect;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.Random;

import static java.lang.Math.min;

public class Asteroid {
    public static final int SPEED = 1200;
    public static final int WIDTH=60;
    public static final int HEIGHT=60;

    private static Texture texture;
    CollisionRect rect;
    float x , y;
    private int width,height;

    public boolean remove = false;

    public Asteroid(float x){
        this.x=x;
        this.y= SpaceGame.HEIGHT;
        Random random=new Random();
        int var=random.nextInt(2);
        width=WIDTH*var+2*WIDTH;
        height=HEIGHT*var+2*HEIGHT;
        this.rect = new CollisionRect(x,y,width,height);
        if(texture == null){
            texture = new Texture("asteroid_v2.jpeg");
        }
    }

    public void update(float deltaTime) {
        int temp=min(MainGameScreen.level,5);
        y-=(SPEED+50*temp)*deltaTime ;
        if(y < -HEIGHT){
            remove = true;
        }
        rect.move(x,y);
    }

    public void render(SpriteBatch batch){
        batch.draw(texture,x,y,width,height);
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
