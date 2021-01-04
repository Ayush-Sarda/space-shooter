package com.ayush.main;

import com.ayush.main.screens.GameOverScreen;
import com.ayush.main.screens.MainGameScreen;
import com.ayush.main.screens.MainMenuScreen;
import com.ayush.main.tools.GameCamera;
import com.ayush.main.tools.ScrollingBackground;
import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.StretchViewport;

public class SpaceGame extends Game {
	public static int WIDTH=1080;
	public static int HEIGHT=2340;
	public static boolean IS_MOBILE = false;

	public SpriteBatch batch;
	public ScrollingBackground scrollingBackground;

	public GameCamera cam;

	
	@Override
	public void create () {

		batch = new SpriteBatch();

		cam=new GameCamera(WIDTH,HEIGHT);

		this.scrollingBackground=new ScrollingBackground();
        this.setScreen(new MainMenuScreen(this));

        if(Gdx.app.getType() == Application.ApplicationType.Android || Gdx.app.getType() == Application.ApplicationType.iOS)
        	IS_MOBILE=true;

	}

	@Override
	public void render () {
		batch.setProjectionMatrix(cam.combined());
		super.render();

	}
	
	@Override
	public void dispose () {

	}

	@Override
	public void resize(int width, int height) {
		cam.update(width, height);
		super.resize(width, height);
	}
}
