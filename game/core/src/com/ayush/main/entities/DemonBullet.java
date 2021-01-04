package com.ayush.main.entities;

import com.ayush.main.SpaceGame;
import com.ayush.main.tools.CollisionRect;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import static com.ayush.main.screens.MainGameScreen.level;
import static java.lang.Math.min;

public class DemonBullet {
    public static final int SPEED = 2300;
    public static final int DEFAULT_Y=SpaceGame.HEIGHT-Demon.DEMON_HEIGHT-Demon.DEMON_POSITION;
    public static final int HEIGHT=30;
    public static final int WIDTH=30;

    private static Texture texture;
    CollisionRect rect;
    float x , y;

    public boolean remove = false;

    public DemonBullet(float x){
        this.x=x;
        this.y=DEFAULT_Y;

        this.rect = new CollisionRect(x,y,WIDTH,HEIGHT);

        if(texture == null){
            texture = new Texture("circular_bullet.png");
        }
    }

    public void update(float deltaTime) {
        int temp=min(level,4);
        y-=(SPEED+50*temp)*deltaTime;
        if(y <= 0){
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

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }
}
