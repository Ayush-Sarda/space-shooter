package com.ayush.main.entities;

import com.ayush.main.SpaceGame;
import com.ayush.main.tools.CollisionRect;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class FinalLightning {
    public static final int DEFAULT_Y=SpaceGame.HEIGHT;
    public static final int HEIGHT=SpaceGame.HEIGHT;
    public static final int WIDTH=5;
    public static final int SPEED=13000;

    private static Texture texture;
    CollisionRect rect;
    float x , y;
    int height;

    public boolean remove = false;

    public FinalLightning(float x){
        this.x=x;
        this.y=DEFAULT_Y;
        height=0;
        this.rect = new CollisionRect(x,y,WIDTH,HEIGHT);

        if(texture == null){
            texture = new Texture("final_lightning.png");
        }
    }

    public void update(float deltaTime) {
        height+=SPEED*deltaTime;
        y-=SPEED*deltaTime;
        if(height>=HEIGHT)
            height=HEIGHT;
        if(y<0)
            y=0;
        rect.height=height;
        rect.move(x,y);
    }

    public void render(SpriteBatch batch){

        batch.draw(texture,x,y,WIDTH,height);
    }

    public CollisionRect getCollisionRect(){
        return rect;
    }
}
