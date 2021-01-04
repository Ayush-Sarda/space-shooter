package com.ayush.main.screens;

import com.ayush.main.SpaceGame;
import com.ayush.main.entities.Asteroid;
import com.ayush.main.entities.Bullet;
import com.ayush.main.entities.Demon;
import com.ayush.main.entities.DemonBullet;
import com.ayush.main.entities.Explosion;
import com.ayush.main.entities.FinalLightning;
import com.ayush.main.entities.FlamedAsteroid;
import com.ayush.main.entities.HealthPowerup;
import com.ayush.main.entities.Lightning;
import com.ayush.main.tools.CollisionRect;
import com.ayush.main.tools.GameCamera;
import com.ayush.main.tools.ScrollingBackground;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;

import java.util.ArrayList;
import java.util.Random;

import static java.lang.Math.min;

public class MainGameScreen implements Screen {

    public static final int SPEED=1000;
    public static final float SKIP_ANIMATION_SPEED=0.15f;
    public static final int SHIP_WIDTH_PIXEL = 17;
    public static final int SHIP_HEIGHT_PIXEL = 32;
    public static final int SHIP_WIDTH=SHIP_WIDTH_PIXEL * 5;//102-17
    public static final int SHIP_HEIGHT=SHIP_HEIGHT_PIXEL * 5;//192-32

    public static final float ROLL_TIMER_SWITCH_TIME=0.15f;



    public static final float SHOOT_WAIT_TIME=0.14f;

    public static final float DEMON_SHOOT_MIN_WAIT_TIME=0.5f;
    public static final float DEMON_SHOOT_MAX_WAIT_TIME=3f;

    public static final float MIN_ASTEROID_SPAWN_TIME=0.3f;
    public static final float MAX_ASTEROID_SPAWN_TIME=0.6f;

    public static final float MIN_FLAMED_ASTEROID_SPAWN_TIME=5f;
    public static final float MAX_FLAMED_ASTEROID_SPAWN_TIME=6f;

    public static final float MIN_HEALTH_SPAWN_TIME=18f;
    public static final float MAX_HEALTH_SPAWN_TIME=25f;

    public static final float MIN_LIGHTNING_SPAWN_TIME=18f;
    public static final float MAX_LIGHTNING_SPAWN_TIME=25f;

    public static final float FINAL_LIGHTNING_TIMER=3f;
    public static final float FINAL_LIGHTNING_STOP_TIMER=0.5f;



    Animation[] rolls;

    float x,y;
    float srcX;
    float destX;
    private float animTimer;
    private float ANIM_TIME = 0.5f;
    float dx;


    int roll;


    float rollTimer;
    float shootTimer;
    float demonshootTimer;
    float asteroidSpawnTimer;
    float flamedAsteroidSpawnTimer;
    float healthSpawnTimer;
    float lightningTimer;
    float finalLightningTimer;
    float finalLightningStopTimer;

    public float Delta;
    float stateTime;
    public static int level;
    boolean isDemon;
    boolean nextLightningCall;
    boolean finalLightningCall;

    Random random;
    SpaceGame game;

    float timer=0;

    ArrayList<Bullet> bullets;
    ArrayList<DemonBullet> demonBullets;
    ArrayList<Asteroid> asteroids;
    ArrayList<Explosion> explosions;
    ArrayList<FlamedAsteroid> flamedAsteroids;
    ArrayList<Demon> demons;
    ArrayList<HealthPowerup> healths;
    ArrayList<Lightning> lightnings;
    ArrayList<FinalLightning> finalLightnings;

    Texture blank;
    Texture rect;

    BitmapFont scoreFont;
    int score;
    int tempScore;
    float health=1;//0 for dead
    CollisionRect playerRect;
    int offset;

    float prevX , nextX;

    public MainGameScreen(SpaceGame game){
        level=1;
        this.game=game;

        random=new Random();
        asteroidSpawnTimer = random.nextFloat() * (MAX_ASTEROID_SPAWN_TIME - MIN_ASTEROID_SPAWN_TIME) + MIN_ASTEROID_SPAWN_TIME;
        flamedAsteroidSpawnTimer = random.nextFloat() * (MAX_FLAMED_ASTEROID_SPAWN_TIME - MIN_FLAMED_ASTEROID_SPAWN_TIME) + MIN_FLAMED_ASTEROID_SPAWN_TIME;
        healthSpawnTimer = random.nextFloat() * (MAX_HEALTH_SPAWN_TIME - MIN_HEALTH_SPAWN_TIME) + MIN_HEALTH_SPAWN_TIME;
        demonshootTimer = random.nextFloat() * (DEMON_SHOOT_MAX_WAIT_TIME - DEMON_SHOOT_MIN_WAIT_TIME) + DEMON_SHOOT_MIN_WAIT_TIME;
        lightningTimer = random.nextFloat() * (MAX_LIGHTNING_SPAWN_TIME - MIN_LIGHTNING_SPAWN_TIME) + MIN_LIGHTNING_SPAWN_TIME;
        offset = random.nextInt(50);

        y=300;
        x=SpaceGame.WIDTH/2-SHIP_WIDTH/2;



        bullets = new ArrayList<Bullet>();
        asteroids = new ArrayList<Asteroid>();
        flamedAsteroids=new ArrayList<FlamedAsteroid>();
        explosions = new ArrayList<Explosion>();
        demons = new ArrayList<Demon>();
        healths = new ArrayList<HealthPowerup>();
        demonBullets = new ArrayList<DemonBullet>();
        lightnings = new ArrayList<Lightning>();
        finalLightnings = new ArrayList<FinalLightning>();

        nextLightningCall=true; finalLightningCall=false;

        scoreFont = new BitmapFont(Gdx.files.internal("fonts/score.fnt"));
        score=0;
        tempScore=0;
        blank = new Texture("blank.png");
        rect = new Texture("rectangle.png");

        playerRect = new CollisionRect(0,0,SHIP_WIDTH,SHIP_HEIGHT);

        roll=2;
        rolls =new Animation[5];

        TextureRegion[][] rollSpriteSheet = TextureRegion.split(new Texture("ship.png"),SHIP_WIDTH_PIXEL,SHIP_HEIGHT_PIXEL);
        rolls[0] = new Animation<>(SKIP_ANIMATION_SPEED,rollSpriteSheet[2]);//all left
        rolls[1] = new Animation<>(SKIP_ANIMATION_SPEED,rollSpriteSheet[1]);
        rolls[2] = new Animation<>(SKIP_ANIMATION_SPEED,rollSpriteSheet[0]);//no tilt
        rolls[3] = new Animation<>(SKIP_ANIMATION_SPEED,rollSpriteSheet[3]);
        rolls[4] = new Animation<>(SKIP_ANIMATION_SPEED,rollSpriteSheet[4]);//all right

        game.scrollingBackground.setSpeedFixed(false);
        game.scrollingBackground.setSpeed(ScrollingBackground.DEFAULT_SPEED);

    }
    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

        prevX=nextX;
        nextX=game.cam.getInputInGameWorld().x;

        ScrollingBackground.stop=isDemon;

        //health spawn
        healthSpawnTimer-=delta;
        if(healthSpawnTimer<=0){
            healthSpawnTimer = random.nextFloat() * (MAX_HEALTH_SPAWN_TIME - MIN_HEALTH_SPAWN_TIME) + MIN_HEALTH_SPAWN_TIME;
            healths.add(new HealthPowerup(random.nextInt(SpaceGame.WIDTH - HealthPowerup.WIDTH)));
        }


        //demon spawn
        if(tempScore>1000*level) {
            if (demons.isEmpty()){
                demons.add(new Demon());
                isDemon=true;
                tempScore=0;
            }
        }

        //demon bullet spawn
        if(isDemon && Demon.isSettled){
            demonshootTimer-=delta;
            if(demonshootTimer<=0){
                demonshootTimer=random.nextFloat() * (DEMON_SHOOT_MAX_WAIT_TIME - DEMON_SHOOT_MIN_WAIT_TIME) + DEMON_SHOOT_MIN_WAIT_TIME;;
                demons.get(0).stop=true;
                demonBullets.add(new DemonBullet(demons.get(0).getX()+Demon.DEMON_WIDTH/2));
            }
        }

        //lightning spawn
        /*timer += delta;
        if(finalLightnings.isEmpty() && timer>5f) {
            finalLightnings.add(new FinalLightning(offset));
            System.out.println("true");
        }*/


        if(level>=3 || isDemon){
            lightningTimer-=delta;
            if(lightningTimer<=0 && nextLightningCall) {
                boolean alert = true;
                if(!demons.isEmpty())
                    demons.get(0).stop = true;
                nextLightningCall = false;
                finalLightningCall=true;
                lightnings.add(new Lightning(offset));
                lightnings.add(new Lightning(offset + 130));
                lightnings.add(new Lightning(offset + 260));
                lightnings.add(new Lightning(offset + 390));
                lightnings.add(new Lightning(offset + 520));
                lightnings.add(new Lightning(offset + 650));
                lightnings.add(new Lightning(offset + 780));
                lightnings.add(new Lightning(offset + 910));
            }
            if(!nextLightningCall){
                finalLightningTimer+=delta;
                if(finalLightningTimer>=FINAL_LIGHTNING_TIMER && finalLightningCall){
                    lightnings.clear();
                    finalLightningCall=false;
                    finalLightnings.add(new FinalLightning(offset));
                    finalLightnings.add(new FinalLightning(offset+130));
                    finalLightnings.add(new FinalLightning(offset+260));
                    finalLightnings.add(new FinalLightning(offset+390));
                    finalLightnings.add(new FinalLightning(offset+520));
                    finalLightnings.add(new FinalLightning(offset+650));
                    finalLightnings.add(new FinalLightning(offset+780));
                    finalLightnings.add(new FinalLightning(offset+910));
                }
            }
        }

        //shooting
        shootTimer+=delta;
        if((isRight() || isLeft()) && shootTimer>=SHOOT_WAIT_TIME){
            shootTimer=0;
            int offset=3;
            if(roll==1 || roll==3)
                offset = 7;
            else if(roll==0 || roll==4)
                offset=11;
            //bullets.add(new Bullet(x+offset));
            //bullets.add(new Bullet(x+SHIP_WIDTH-offset));
            bullets.add(new Bullet(x+SHIP_WIDTH/2-14));
        }

        //asteroid spawn
        if(!isDemon) { //check if demon spawned then stop asteroids
            int temp = min(level, 6);
            asteroidSpawnTimer -= delta;
            if (asteroidSpawnTimer <= 0) {
                asteroidSpawnTimer = random.nextFloat() * ((MAX_ASTEROID_SPAWN_TIME - 0.05f * temp) - (MIN_ASTEROID_SPAWN_TIME - 0.05f * temp)) + (MIN_ASTEROID_SPAWN_TIME - 0.05f * temp);
                asteroids.add(new Asteroid(random.nextInt(SpaceGame.WIDTH - Asteroid.WIDTH)));
            } else {
                //flamed astetroid spawn
                if (level >= 2) {
                    flamedAsteroidSpawnTimer -= delta;
                    if (flamedAsteroidSpawnTimer <= 0) {
                        flamedAsteroidSpawnTimer = random.nextFloat() * ((MAX_FLAMED_ASTEROID_SPAWN_TIME - 0.8f * temp) - (MIN_FLAMED_ASTEROID_SPAWN_TIME - 0.8f * temp)) + (MIN_FLAMED_ASTEROID_SPAWN_TIME - 0.8f * temp);
                        flamedAsteroids.add(new FlamedAsteroid(random.nextInt(SpaceGame.WIDTH - FlamedAsteroid.WIDTH)));
                    }
                }
            }
        }

        //update demon
        ArrayList<Demon> demonsToRemove = new ArrayList<Demon>();
        for(Demon demon:demons){
            demon.update(delta);
        }

        //update asteroids
        ArrayList<Asteroid> asteroidsToRemove = new ArrayList<Asteroid>();
        for(Asteroid asteroid:asteroids){
            asteroid.update(delta);
            if(asteroid.remove)
                asteroidsToRemove.add(asteroid);
        }

        //update flamed asteroids
        ArrayList<FlamedAsteroid> flamedAsteroidsToRemove = new ArrayList<FlamedAsteroid>();
        for(FlamedAsteroid flamedAsteroid:flamedAsteroids){
            flamedAsteroid.update(delta);
            if(flamedAsteroid.remove)
                flamedAsteroidsToRemove.add(flamedAsteroid);
        }

        //update demon bullets
        ArrayList<DemonBullet> demonBulletsToRemove = new ArrayList<DemonBullet>();
        for(DemonBullet demonBullet:demonBullets){
            demonBullet.update(delta);
            if(demonBullet.remove)
                demonBulletsToRemove.add(demonBullet);
        }


        //update bullets
        ArrayList<Bullet> bulletsToRemove = new ArrayList<Bullet>();
        for(Bullet bullet:bullets){
            bullet.update(delta);
            if(bullet.remove)
                bulletsToRemove.add(bullet);
        }


        //update explosions
        ArrayList<Explosion> explosionsToRemove = new ArrayList<Explosion>();
        for(Explosion explosion:explosions){
             explosion.update(delta);
             if(explosion.remove)
                 explosionsToRemove.add(explosion);
        }
        explosions.removeAll(explosionsToRemove);


        //update healths
        ArrayList<HealthPowerup> healthToRemove = new ArrayList<HealthPowerup>();
        for(HealthPowerup health:healths){
            health.update(delta);
            if(health.remove)
                healthToRemove.add(health);
        }
        healths.removeAll(healthToRemove);

        //update lightning
        if(!finalLightnings.isEmpty()){
            finalLightningStopTimer+=delta;
            for(FinalLightning finalLightning:finalLightnings){
                finalLightning.update(delta);
            }
        }
        if(finalLightningStopTimer>=FINAL_LIGHTNING_STOP_TIMER) {
            finalLightnings.clear();
            //demons.get(0).stop=false;
            lightningTimer = random.nextFloat() * (MAX_LIGHTNING_SPAWN_TIME - MIN_LIGHTNING_SPAWN_TIME) + MIN_LIGHTNING_SPAWN_TIME;
            finalLightningTimer=0;
            finalLightningStopTimer=0;
            nextLightningCall = true;
        }

        //movement
        Delta= Gdx.graphics.getDeltaTime();

        //if(x!=destX || Math.round(prevX)!=Math.round(nextX)) {
            if (isLeft()) {
                //System.out.println("left");
                dx = game.cam.getInputInGameWorld().x-x-SHIP_WIDTH/2;
            } else if (isRight()) {
                //System.out.println("right");
                dx = game.cam.getInputInGameWorld().x-x-SHIP_WIDTH/2;
            } else
                dx = 0;


            /*if(isJustLeft() && !isRight() && roll>0){
                rollTimer = 0;
                roll--;
            }*/

            /*rollTimer -= Delta;
            if(Math.abs(rollTimer) > ROLL_TIMER_SWITCH_TIME && roll>0){
                rollTimer -= ROLL_TIMER_SWITCH_TIME;
                roll--;
            }
        } else{
            if(roll < 2){
                rollTimer += Delta;
                if(Math.abs(rollTimer) > ROLL_TIMER_SWITCH_TIME && roll<4){
                    rollTimer -= ROLL_TIMER_SWITCH_TIME;
                    roll++;
                }
            }
        }*/

        //Right




        if(dx!=0 && (dx>0 && x + dx + SHIP_WIDTH < SpaceGame.WIDTH) || (dx<0 && x+dx > 0)) {
            move(dx);

        }
        update(delta);
            /*if(isJustRight() && !isLeft() && roll<4){
                rollTimer=0;
                roll++;
            }*/

            /*rollTimer += Delta;
            if(Math.abs(rollTimer) > ROLL_TIMER_SWITCH_TIME && roll<4){
                rollTimer -= ROLL_TIMER_SWITCH_TIME;
                roll++;
            }
        } else{
            if(roll > 2){
                rollTimer -= Delta;
                if(Math.abs(rollTimer) > ROLL_TIMER_SWITCH_TIME && roll>0){
                    rollTimer = 0;
                    roll--;
                }
            }
        }*/
        //update player rectangle
        playerRect.move(x,y);

        //check for collisions
        //asteroid
        for(Bullet bullet:bullets){
            for(Asteroid asteroid:asteroids){
                if(bullet.getCollisionRect().collidesWith(asteroid.getCollisionRect())){
                    bulletsToRemove.add(bullet);
                    asteroidsToRemove.add(asteroid);
                    explosions.add(new Explosion(asteroid.getX(),asteroid.getY()));
                    score+=30;//10
                    tempScore+=30;//10
                }
            }
        }
        bullets.removeAll(bulletsToRemove);

        //flamed asteroid
        for(Bullet bullet:bullets){
            for(FlamedAsteroid flamedAsteroid:flamedAsteroids){
                if(bullet.getCollisionRect().collidesWith(flamedAsteroid.getCollisionRect())){
                    bulletsToRemove.add(bullet);
                    flamedAsteroidsToRemove.add(flamedAsteroid);
                    explosions.add(new Explosion(flamedAsteroid.getX(),flamedAsteroid.getY()));
                    score+=50;
                    tempScore+=50;
                }
            }
        }
        bullets.removeAll(bulletsToRemove);

        //demon
        float healthDecrement=(float)(0.06-0.01*Math.min(5,level));
        for(Bullet bullet:bullets){
            for(Demon demon:demons){
                if(bullet.getCollisionRect().collidesWith(demon.getCollisionRect())){
                    bulletsToRemove.add(bullet);
                    demon.health-=healthDecrement;
                    if(demon.health<=0){
                        explosions.add(new Explosion(demon.getX(),demon.getY()));
                        demonsToRemove.add(demon);
                        isDemon=false;
                        level++;
                        tempScore=0;
                    }
                    score+=10;
                    tempScore+=10;
                }
            }
        }
        bullets.removeAll(bulletsToRemove);
        demons.removeAll(demonsToRemove);

        //asteroid collision with player
        for(Asteroid asteroid:asteroids){
            if(asteroid.getCollisionRect().collidesWith(playerRect)){
                asteroidsToRemove.add(asteroid);
                health-=0.1;

                if(health<=0){
                    this.dispose();
                    game.setScreen(new GameOverScreen(game,score));
                    return;
                }
            }
        }
        asteroids .removeAll(asteroidsToRemove);

        //flamed asteroid collision with player
        for(FlamedAsteroid flamedAsteroid:flamedAsteroids){
            if(flamedAsteroid.getCollisionRect().collidesWith(playerRect)){
                flamedAsteroidsToRemove.add(flamedAsteroid);
                health-=0.2;

                if(health<=0){
                    this.dispose();
                    game.setScreen(new GameOverScreen(game,score));
                    return;
                }
            }
        }
        flamedAsteroids .removeAll(flamedAsteroidsToRemove);

        //collision of player with health
        for(HealthPowerup health:healths){
            if(health.getCollisionRect().collidesWith(playerRect)){
                healthToRemove.add(health);
                this.health+=0.4;
                if(this.health>1)
                    this.health=1;
            }
        }
        healths.removeAll(healthToRemove);

        //collision of demon bullet with player
        for(DemonBullet demonBullet:demonBullets){
            if(demonBullet.getCollisionRect().collidesWith(playerRect)){
                demonBulletsToRemove.add(demonBullet);
                this.health-=0.4;

                if(health<=0){
                    this.dispose();
                    game.setScreen(new GameOverScreen(game,score));
                    return;
                }
            }
        }
        demonBullets.removeAll(demonBulletsToRemove);

        //bullet with bullet
        for(DemonBullet demonBullet:demonBullets){
            for(Bullet bullet:bullets)
                if(demonBullet.getCollisionRect().collidesWith(bullet.getCollisionRect())){
                    demonBulletsToRemove.add(demonBullet);
                    bulletsToRemove.add(bullet);
                    explosions.add(new Explosion(demonBullet.getX(),demonBullet.getY()));
                }
        }
        demonBullets.removeAll(demonBulletsToRemove);
        bullets.removeAll(bulletsToRemove);

        //lightning strike with player
        for(FinalLightning finalLightning:finalLightnings){
            if(finalLightning.getCollisionRect().collidesWith(playerRect)) {
                health = 0;
                this.dispose();
                game.setScreen(new GameOverScreen(game,score));
                return;
            }
        }

        stateTime += delta;

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //draw objects
        game.batch.begin();

        game.scrollingBackground.updateAndRender(delta,game.batch);

        scoreFont.getData().setScale(1.5f,2.5f);
        GlyphLayout scoreLayout = new GlyphLayout(scoreFont,""+score);
        scoreFont.draw(game.batch, scoreLayout, SpaceGame.WIDTH/2-scoreLayout.width/2,SpaceGame.HEIGHT-scoreLayout.height-10);

        for(Lightning lightning:lightnings){
            lightning.render(game.batch);
        }

        //game.batch.setColor(Color.GRAY);
        for(FinalLightning finalLightning:finalLightnings){
            finalLightning.render(game.batch);
        }
        //game.batch.setColor(Color.WHITE);

        for(Asteroid asteroid:asteroids){
            asteroid.render(game.batch);
        }

        for(FlamedAsteroid flamedAsteroid:flamedAsteroids){
            flamedAsteroid.render(game.batch);
        }

        for(Bullet bullet:bullets){
            bullet.render(game.batch);
        }

        for(Explosion explosion:explosions){
            explosion.render(game.batch);
        }

        for(Demon demon:demons){
            demon.render(game.batch);
        }

        for(HealthPowerup health:healths){
            health.render(game.batch);
        }

        for(DemonBullet demonBullet:demonBullets){
            demonBullet.render(game.batch);
        }



        if(!lightnings.isEmpty()){
            scoreFont.getData().setScale(2f,3f);
            GlyphLayout AlertLayout = new GlyphLayout(scoreFont,"Alert");
            scoreFont.draw(game.batch,AlertLayout,SpaceGame.WIDTH/2-AlertLayout.width/2,SpaceGame.HEIGHT/2);
        }


        //draw health
        if(health>=0.66){
            game.batch.setColor(new Color((float)(255*(1-health)/(1-0.66)),255,0,255));
        }
        else if(health>=0.33)
            game.batch.setColor(new Color(255,(float)(255-255*(0.66-health)/(0.66-0.33)),0,255));
        else
            game.batch.setColor(Color.RED);

        game.batch.draw(blank,0,0,SpaceGame.WIDTH*health,10);

        game.batch.setColor(Color.WHITE);
        game.batch.draw((TextureRegion) rolls[roll].getKeyFrame(stateTime,true), x, y,SHIP_WIDTH,SHIP_HEIGHT);

        //game.batch.draw(rect,x,y,SHIP_WIDTH,SHIP_HEIGHT);

        game.batch.end();
    }

    public boolean isRight(){
        return Gdx.input.isKeyPressed(Input.Keys.RIGHT) || (Gdx.input.isTouched() && Math.round(game.cam.getInputInGameWorld().x) > Math.round(x+SHIP_WIDTH/2));
    }

    public boolean isLeft(){
        return Gdx.input.isKeyPressed(Input.Keys.LEFT) || (Gdx.input.isTouched() && Math.round(game.cam.getInputInGameWorld().x) < Math.round(x+SHIP_WIDTH/2));
    }

    public boolean isJustRight(){
        return Gdx.input.isKeyJustPressed(Input.Keys.RIGHT) || (Gdx.input.justTouched() && game.cam.getInputInGameWorld().x >= x+SHIP_WIDTH/2);
    }

    public boolean isJustLeft(){
        return Gdx.input.isKeyJustPressed(Input.Keys.LEFT) || (Gdx.input.justTouched() && game.cam.getInputInGameWorld().x < x+SHIP_WIDTH/2);
    }

    public void move(float dx){
        if (x + dx + SHIP_WIDTH > SpaceGame.WIDTH)
            x = SpaceGame.WIDTH - SHIP_WIDTH;
        if(x + dx < 0)
            x = 0;
        initializeMove(x,dx);
    }

    public void update(float delta){
        animTimer += delta;
        x = Interpolation.linear.apply(srcX,destX,animTimer/ANIM_TIME);
        if(animTimer > ANIM_TIME) {
            animTimer=0;
            destX=srcX;
            return;
        }
    }

    public void initializeMove(float oldX , float dx){
        srcX = oldX;
        destX = oldX+dx;
        x = oldX;
    }



    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
