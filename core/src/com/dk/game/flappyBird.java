package com.dk.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import java.util.Random;


public class flappyBird extends ApplicationAdapter {
		SpriteBatch batch;
		Texture background;
		Texture gameover;
		Texture[] birds;
		int flapState = 0;
		float birdY ;
		float velocity=0 ;
		Circle birdCircle;
		int score =0,highscore= 0;
		int scoringTube = 0;
		BitmapFont font,fonts,fonths;

	    int foranimation=6;
		int gameState =0;
		float gravity = 1;
		Texture topTube;
		Texture bottomTube;
		float gap ;
		float maxTubeOffset;
		Random randomGenerator;

		float tubeVelocity =3.5f ;


		int numberOfTubes = 4;
		float[] tubeX = new float[numberOfTubes];
		float[] tubeOffset = new float[numberOfTubes];
		float distanceBetweenTheTube;
		Rectangle[] topTubeRectangles;
		Rectangle[] bottomTubeRectangles;
		Texture playbutton,exit;


		@Override
		public void create() {
			batch = new SpriteBatch();
			background = new Texture("bg.png");
			gameover = new Texture("gameover.png");
			birdCircle = new Circle();
			birds = new Texture[2];
			birds[0] = new Texture("bird.png");
			birds[1] = new Texture("bird2.png");

			font = new BitmapFont();
			font.setColor(Color.WHITE);
			font.getData().setScale(6);
			fonts = new BitmapFont();
			fonts.setColor(Color.WHITE);
			fonts.getData().setScale(5);
			fonths = new BitmapFont();
			fonths.setColor(Color.WHITE);
			fonths.getData().setScale(6);

			topTube = new Texture("toptube.png");
			bottomTube = new Texture("bottomtube.png");

			playbutton = new Texture("playbuttont.jpg");
			exit = new Texture("exitbutton.jpg");
			maxTubeOffset = Gdx.graphics.getHeight() / 2f - gap / 2 - 100;
			randomGenerator = new Random();
			distanceBetweenTheTube = Gdx.graphics.getWidth() * 3f / 4f;
			topTubeRectangles = new Rectangle[numberOfTubes];
			bottomTubeRectangles = new Rectangle[numberOfTubes];
			startGame();


		}

		public void startGame()
		{
			birdY = Gdx.graphics.getHeight() / 2f - birds[flapState].getHeight() / 2f;
			for (int i = 0; i < numberOfTubes; i++) {
				tubeOffset[i] = (randomGenerator.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - gap - 250);

				tubeX[i] = Gdx.graphics.getWidth() / 2f - topTube.getWidth() / 2f + Gdx.graphics.getWidth() + i * distanceBetweenTheTube;

				topTubeRectangles[i] = new Rectangle();
				bottomTubeRectangles[i] = new Rectangle();

			}
		}

		@Override
		public void render() {
			batch.begin();
			if(foranimation==6){
			gap=randomGenerator.nextInt(100)+400;
			foranimation=1;}
			else foranimation++;
			batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

			if (gameState == 1) {

				if (tubeX[scoringTube] < Gdx.graphics.getWidth() / 2f) {
					Gdx.app.log("Score", String.valueOf(score));
					score++;
					if (scoringTube < numberOfTubes - 1) {
						scoringTube++;
					} else {
						scoringTube = 0;
					}
				}

				if (Gdx.input.justTouched()) {

					velocity = -20;

				}
				for (int i = 0; i < numberOfTubes; i++) {

					if (tubeX[i] < -topTube.getWidth()) {

						tubeX[i] += numberOfTubes * distanceBetweenTheTube;
						tubeOffset[i] = (randomGenerator.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - gap - 300);
					} else {

						tubeX[i] = tubeX[i] - tubeVelocity;

					}
					batch.draw(topTube, tubeX[i], Gdx.graphics.getHeight() / 2f + gap / 2 + tubeOffset[i]);
					batch.draw(bottomTube, tubeX[i], Gdx.graphics.getHeight() / 2f - gap / 2 - bottomTube.getHeight() + tubeOffset[i]);

					topTubeRectangles[i] = new Rectangle(tubeX[i], Gdx.graphics.getHeight() / 2f + gap / 2 + tubeOffset[i], topTube.getWidth(), topTube.getHeight());
					bottomTubeRectangles[i] = new Rectangle(tubeX[i], Gdx.graphics.getHeight() / 2f - gap / 2 - bottomTube.getHeight() + tubeOffset[i], bottomTube.getWidth(), bottomTube.getHeight());
				}


				if (birdY > 0) {
					velocity = velocity + gravity;
					birdY -= velocity;
				}else{

					gameState = 2;
				}
				if(birdY>Gdx.graphics.getHeight()){
					gameState=2;
				}
			} else if(gameState==0){

				if (Gdx.input.justTouched())
				{
					gameState = 1;
				}

			}else if(gameState==2)
			{
				batch.draw(gameover ,Gdx.graphics.getWidth()/2f - gameover.getWidth()/2f,Gdx.graphics.getHeight()/2f -gameover.getHeight());
                fonts.draw(batch,"Your Score is\n              "+score,Gdx.graphics.getWidth()/2f -300,Gdx.graphics.getHeight()/2f -gameover.getHeight());
				if (Gdx.input.justTouched())
				{
					gameState = 1;
					startGame();
					score =0;
					scoringTube=0;
					velocity = 0;


				}

			}

			if (flapState == 0) {
				flapState = 1;
			} else {
				flapState = 0;
			}

            if(score>=highscore){
            	highscore=score;
			}
			batch.draw(birds[flapState], Gdx.graphics.getWidth() / 2f - birds[flapState].getWidth() / 2f, birdY);
			font.draw(batch , String.valueOf(score) , 100 , Gdx.graphics.getHeight()-100);
			fonths.draw(batch,String.valueOf(highscore),Gdx.graphics.getWidth()-100,Gdx.graphics.getHeight()-100);


			batch.end();

			birdCircle.set(Gdx.graphics.getWidth() / 2f, birdY + birds[flapState].getHeight() / 2f, birds[flapState].getWidth() / 2f);
			for (int i = 0; i < numberOfTubes; i++) {
				if (Intersector.overlaps(birdCircle, topTubeRectangles[i]) || Intersector.overlaps(birdCircle, bottomTubeRectangles[i])) {
					gameState = 2;
					break;
				}
			}

		}
	}
