package com.ayush.main.screens;

import com.ayush.main.SpaceGame;
import com.ayush.main.tools.ScrollingBackground;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;

public class MainMenuScreen implements Screen {
    private static final int PLAY_BUTTON_WIDTH=690,PLAY_BUTTON_HEIGHT=300;
    private static final int EXIT_BUTTON_WIDTH=600,EXIT_BUTTON_HEIGHT=300;
    private static int PLAY_Y=800;
    private static int EXIT_Y=450;
    private static final int LOGO_WIDTH=1000;
    private static final int LOGO_HEIGHT=800;
    private static final int LOGO_Y=1500;

    Texture play_button_active;
    Texture play_button_inactive;
    Texture exit_button_active;
    Texture exit_button_inactive;
    Texture logo;

    SpaceGame game;

    public MainMenuScreen(final SpaceGame game){
        this.game=game;
        play_button_active=new Texture("play_button_active.png");
        play_button_inactive=new Texture("play_button_inactive.png");
        exit_button_active=new Texture("exit_button_active.png");
        exit_button_inactive=new Texture("exit_button_inactive.png");
        logo = new Texture("logo.png");


        game.scrollingBackground.setSpeedFixed(true);
        game.scrollingBackground.setSpeed(ScrollingBackground.DEFAULT_SPEED);

        final MainMenuScreen mainMenuScreen = this;

        Gdx.input.setInputProcessor(new InputAdapter(){
            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                int PLAY_X=SpaceGame.WIDTH/2-PLAY_BUTTON_WIDTH/2;
                int EXIT_X=SpaceGame.WIDTH/2-EXIT_BUTTON_WIDTH/2;
                //play button
                if(game.cam.getInputInGameWorld().x<=PLAY_X+PLAY_BUTTON_WIDTH && game.cam.getInputInGameWorld().x>=PLAY_X  && SpaceGame.HEIGHT-game.cam.getInputInGameWorld().y<=PLAY_Y+PLAY_BUTTON_HEIGHT && SpaceGame.HEIGHT-game.cam.getInputInGameWorld().y>=PLAY_Y){
                    mainMenuScreen.dispose();
                    game.setScreen(new MainGameScreen(game));
                }
                //exit button
                if(game.cam.getInputInGameWorld().x<=EXIT_X+EXIT_BUTTON_WIDTH && game.cam.getInputInGameWorld().x>=EXIT_X  && SpaceGame.HEIGHT-game.cam.getInputInGameWorld().y<=EXIT_Y+EXIT_BUTTON_HEIGHT && SpaceGame.HEIGHT-game.cam.getInputInGameWorld().y>=EXIT_Y){
                    mainMenuScreen.dispose();
                    Gdx.app.exit();
                }

                return super.touchUp(screenX, screenY, pointer, button);
            }
        });



    }
    @Override
    public void show() {


    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.15f, 0.15f, 0.3f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.batch.begin();

        game.scrollingBackground.updateAndRender(delta,game.batch);

        int PLAY_X=SpaceGame.WIDTH/2-PLAY_BUTTON_WIDTH/2;
        int EXIT_X=SpaceGame.WIDTH/2-EXIT_BUTTON_WIDTH/2;
        if(game.cam.getInputInGameWorld().x<=PLAY_X+PLAY_BUTTON_WIDTH && game.cam.getInputInGameWorld().x>=PLAY_X  && SpaceGame.HEIGHT-game.cam.getInputInGameWorld().y<=PLAY_Y+PLAY_BUTTON_HEIGHT && SpaceGame.HEIGHT-game.cam.getInputInGameWorld().y>=PLAY_Y){
            game.batch.draw(play_button_active,PLAY_X,PLAY_Y,PLAY_BUTTON_WIDTH,PLAY_BUTTON_HEIGHT);
        }
        else{
            game.batch.draw(play_button_inactive,PLAY_X,PLAY_Y,PLAY_BUTTON_WIDTH,PLAY_BUTTON_HEIGHT);
        }
        if(game.cam.getInputInGameWorld().x<=EXIT_X+EXIT_BUTTON_WIDTH && game.cam.getInputInGameWorld().x>=EXIT_X  && SpaceGame.HEIGHT-game.cam.getInputInGameWorld().y<=EXIT_Y+EXIT_BUTTON_HEIGHT && SpaceGame.HEIGHT-game.cam.getInputInGameWorld().y>=EXIT_Y){
            game.batch.draw(exit_button_active,EXIT_X,EXIT_Y,EXIT_BUTTON_WIDTH,EXIT_BUTTON_HEIGHT);
        }
        else{
            game.batch.draw(exit_button_inactive,EXIT_X,EXIT_Y,EXIT_BUTTON_WIDTH,EXIT_BUTTON_HEIGHT);
        }

        game.batch.draw(logo,SpaceGame.WIDTH/2-LOGO_WIDTH/2,LOGO_Y,LOGO_WIDTH,LOGO_HEIGHT);

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
        Gdx.input.setInputProcessor(null);
    }
}
