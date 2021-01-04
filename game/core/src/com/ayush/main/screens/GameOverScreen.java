package com.ayush.main.screens;

import com.ayush.main.SpaceGame;
import com.ayush.main.tools.ScrollingBackground;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.utils.Align;

public class GameOverScreen implements Screen {

    private static final int BANNER_HEIGHT = 500;
    private static final int BANNER_WIDTH = 900;

    SpaceGame game;
    int score,highscore;

    Texture gameOverBanner;
    BitmapFont scoreFont;

    public GameOverScreen(SpaceGame game,int score){
        this.game=game;
        this.score=score;

        Preferences prefs = Gdx.app.getPreferences("spacegame");
        this.highscore = prefs.getInteger("highscore",0);

        if(highscore<score){
            prefs.putInteger("highscore",score);
            prefs.flush();
        }

        gameOverBanner = new Texture("game_over.png");
        scoreFont = new BitmapFont(Gdx.files.internal("fonts/score.fnt"));
        //scoreFont.setColor(Color.GRAY);

        game.scrollingBackground.setSpeedFixed(true);
        game.scrollingBackground.setSpeed(ScrollingBackground.DEFAULT_SPEED);

    }
    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.batch.begin();

        game.scrollingBackground.updateAndRender(delta,game.batch);
        game.batch.setColor(Color.RED);
        game.batch.draw(gameOverBanner,SpaceGame.WIDTH/2-BANNER_WIDTH/2,SpaceGame.HEIGHT-BANNER_HEIGHT-25,BANNER_WIDTH,BANNER_HEIGHT);
        game.batch.setColor(Color.WHITE);

        scoreFont.getData().setScale(1.5f,2.5f);

        GlyphLayout scoreLayout = new GlyphLayout(scoreFont,"Score: \n"+score, Color.WHITE,0, Align.left,false);
        GlyphLayout highscoreLayout = new GlyphLayout(scoreFont,"Highscore: \n"+highscore, Color.WHITE,0, Align.left,false);


        scoreFont.draw(game.batch,scoreLayout,SpaceGame.WIDTH/2-scoreLayout.width/2,SpaceGame.HEIGHT-BANNER_HEIGHT-25*2);
        scoreFont.draw(game.batch,highscoreLayout,SpaceGame.WIDTH/2-highscoreLayout.width/2,SpaceGame.HEIGHT-BANNER_HEIGHT-scoreLayout.height-25*4);

        float touchX=game.cam.getInputInGameWorld().x; float touchY=SpaceGame.HEIGHT-game.cam.getInputInGameWorld().y;

        GlyphLayout tryAgainLayout = new GlyphLayout(scoreFont,"Try Again");
        GlyphLayout mainMenuLayout = new GlyphLayout(scoreFont,"Main Menu");


        float tryAgainX = SpaceGame.WIDTH/2 - tryAgainLayout.width/2;
        float tryAgainY = SpaceGame.HEIGHT/2 - tryAgainLayout.height/2;
        float mainMenuX = SpaceGame.WIDTH/2 - mainMenuLayout.width/2;
        float mainMenuY = SpaceGame.HEIGHT/2 - mainMenuLayout.height/2-tryAgainLayout.height-80;

        if(touchX>= tryAgainX && touchX<tryAgainX+tryAgainLayout.width && touchY>= tryAgainY-tryAgainLayout.height && touchY<tryAgainY)
            tryAgainLayout.setText(scoreFont,"Try Again",Color.YELLOW,0,Align.left,false);
        if(touchX>= mainMenuX && touchX<mainMenuX+mainMenuLayout.width && touchY>= mainMenuY-mainMenuLayout.height && touchY<mainMenuY)
            mainMenuLayout.setText(scoreFont,"Main Menu",Color.YELLOW,0,Align.left,false);


        // pressed
        if(Gdx.input.justTouched()){
            //try again
            if(touchX>tryAgainX && touchX<tryAgainX+tryAgainLayout.width && touchY>tryAgainY-tryAgainLayout.height && touchY<tryAgainY){
                this.dispose();
                game.batch.end();
                game.setScreen(new MainGameScreen(game));
                return;
            }

            //main menu
            if(touchX>mainMenuX && touchX<mainMenuX+mainMenuLayout.width && touchY>mainMenuY-mainMenuLayout.height && touchY<mainMenuY){
                this.dispose();
                game.batch.end();
                game.setScreen(new MainMenuScreen(game));
                return;
            }
        }

        scoreFont.getData().setScale(1.7f,3f);

        //draw buttons
        scoreFont.draw(game.batch,tryAgainLayout,tryAgainX,tryAgainY);
        scoreFont.draw(game.batch,mainMenuLayout,mainMenuX,mainMenuY);




        game.batch.end();
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
