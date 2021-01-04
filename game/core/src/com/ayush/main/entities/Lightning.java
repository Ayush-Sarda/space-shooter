package com.ayush.main.entities;

import com.ayush.main.SpaceGame;
import com.ayush.main.tools.CollisionRect;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import static com.ayush.main.screens.MainGameScreen.level;
import static java.lang.Math.min;

public class Lightning {
    public static final int DEFAULT_Y=0;
    public static final int HEIGHT=SpaceGame.HEIGHT;
    public static final int WIDTH=5;

    private static Texture texture;
    CollisionRect rect;
    float x , y;

    public boolean remove = false;

    public Lightning(float x){
        this.x=x;
        this.y=DEFAULT_Y;

        this.rect = new CollisionRect(x,y,WIDTH,HEIGHT);

        if(texture == null){
            texture = new Texture("lightning.png");
        }
    }

    public void update(float deltaTime) {
        rect.move(x,y);
    }

    public void render(SpriteBatch batch){

        batch.draw(texture,x,y,WIDTH,HEIGHT);
    }

    public CollisionRect getCollisionRect(){
        return rect;
    }
}
