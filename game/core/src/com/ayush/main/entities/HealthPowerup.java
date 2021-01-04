package com.ayush.main.entities;

import com.ayush.main.SpaceGame;
import com.ayush.main.screens.MainGameScreen;
import com.ayush.main.tools.CollisionRect;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import static java.lang.Math.min;

public class HealthPowerup {
    public static final int WIDTH=80;
    public static final int HEIGHT=80;
    public static final int SPEED=200;


    private float x;
    private float y;
    public boolean remove;

    Texture texture;

    CollisionRect rect;

    public HealthPowerup(float x){
        this.x=x;
        this.y= SpaceGame.HEIGHT;
        rect = new CollisionRect(x,y,WIDTH,HEIGHT);
        texture = new Texture("health.png");
    }

    public void update(float deltaTime) {

        y-=SPEED*deltaTime ;
        if(y < -HEIGHT){
            remove = true;
        }
        rect.move(x,y);
    }

    public void render(SpriteBatch batch){
        batch.draw(texture,x,y,WIDTH,HEIGHT);
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
