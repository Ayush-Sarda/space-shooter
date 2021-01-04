package com.ayush.main.entities;

import com.ayush.main.SpaceGame;
import com.ayush.main.tools.CollisionRect;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import static com.ayush.main.screens.MainGameScreen.level;
import static java.lang.Math.min;

public class Bullet {
    public static final int SPEED = 1600;
    public static final int DEFAULT_Y=320;
    public static final int HEIGHT=58;
    public static final int WIDTH=28;

    private static Texture texture;
    CollisionRect rect;
    float x , y;

    public boolean remove = false;

    public Bullet(float x){
        this.x=x;
        this.y=DEFAULT_Y;

        this.rect = new CollisionRect(x,y,WIDTH,HEIGHT);

        if(texture == null){
            texture = new Texture("bullet_v2.png");
        }
    }

    public void update(float deltaTime) {
        int temp=min(level,4);
        y+=(SPEED+50*temp)*deltaTime;
        if(y> SpaceGame.HEIGHT){
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


}
