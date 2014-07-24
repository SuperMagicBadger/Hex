package com.cowthegreat.hexwars.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.cowthegreat.hexwars.HexWars;
import com.cowthegreat.hexwars.controlers.CameraController;
import com.cowthegreat.hexwars.graphics.MoveAction;
import com.cowthegreat.hexwars.graphics.Unit;
import com.cowthegreat.hexwars.graphics.UnitBlock;
import com.cowthegreat.hexwars.hex.HexBuffer;
import com.cowthegreat.hexwars.hex.HexKey;
import com.cowthegreat.hexwars.hex.HexMath;
import com.cowthegreat.hexwars.hex.HexMath.Orientation;

public class TestRenderScreen implements Screen {
	public static String SCREEN_TAG = "test_render_screen";
	
	HexWars game;
	
	PerspectiveCamera cam;
	CameraController myCon;
	
	ModelBatch batch;
	ModelInstance inst;
	Environment environ;

	HexMath hm;
	HexBuffer hexBuffer;
	ShapeRenderer shapes;
	
	UnitCommandOverlay cmd;
	
	UnitBlock block;
	Unit planet;
	
	Stage ui;
	Label fpsLabel;
	
	HexKey highlight;
	
	int hexWidth, hexHeight;
	
	MoveAction mover;
	
	public TestRenderScreen(HexWars hexgame) {
		game = hexgame;
		cam = new PerspectiveCamera();
		cam.fieldOfView = 50;
		cam.viewportWidth = Gdx.graphics.getWidth();
		cam.viewportHeight = Gdx.graphics.getHeight();
		cam.position.set(10,  10,  10);
		cam.lookAt(0, 0, 0);
		cam.near = 1;
		cam.far = 500;
		cam.update();
		myCon = new CameraController(cam);
		
		batch = new ModelBatch();
		
		hexBuffer = new HexBuffer(100);
		hexBuffer.setInnerRadius(1f);
		hexBuffer.setOuterRadius(0.95f);
		
		hm = new HexMath();
		hm.o = Orientation.POINTY;
		hm.side = 5;
		
		ModelBuilder builder = new ModelBuilder();
		Model rect = builder.createBox(1, 1, 1, 
				new Material(ColorAttribute.createDiffuse(Color.GREEN)),
				Usage.Position | Usage.Normal);
		inst = new ModelInstance(rect);
		
		
		// model loading		
		block = new UnitBlock(game.miPool.getPool("untitled.g3db"), new Vector3(hm.getWidth(), 5,  hm.getHeight()));
		block.setPosition(0, 0, 0);
		block.setSlotCounts(3, 1, 2);
		block.setCount(block.maxCount());
		
		// planet
		HexKey pos = HexKey.obtainKey(1, 1);
		pos.release();
		
		// set environment
		environ = new Environment();
		environ.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
		environ.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, 0.8f, -1f, 1f));
		
		ui = new Stage();
		LabelStyle lstyle = new LabelStyle();
		lstyle.font = game.assets.get("fonts/small_font_25.fnt", BitmapFont.class);
		lstyle.fontColor = Color.WHITE;
		fpsLabel = new Label("asdf", lstyle);
		ui.addActor(fpsLabel);
		
		cmd = new UnitCommandOverlay(game);
		cmd.setPosition(0, ui.getHeight() - cmd.getHeight());
		ui.addActor(cmd);
		
		mover = new MoveAction();
		
		hexWidth = hexHeight = 10;
		shapes = new ShapeRenderer();
	}

	Vector2 v = new Vector2();
	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		
		myCon.update();
		HexKey h = hm.pixelToHex(myCon.planeSelect.x, myCon.planeSelect.z);	
		inst.transform.setToTranslation(myCon.planeSelect);
		
		if(!h.equals(highlight)){
			highlight = h;
			System.out.println(highlight);
			v = hm.hexToPixel(h, v);
			mover.reset();
			mover.setSource(block.getPosition());
			mover.setDestination(v.x, 0, v.y);
		}

		if(!mover.isComplete()){
			mover.update(delta);
			block.setPosition(mover.getPosition());
		}

		planet.update(delta);
		
		hexBuffer.begin(cam.combined);
		for (int i = 0; i < hexWidth; i++) {
			for (int j = 0; j < hexHeight; j++) {
				HexKey hk = HexKey.obtainKey();//i - (j / 2), j);
				hk.setOddR(i, j);
				hk.setOddR(i, j);
				if(hk.equals(highlight)){
					hexBuffer.draw(hk, hm, Color.BLACK, Color.GREEN);
				} else {
					hexBuffer.draw(hk, hm, Color.BLACK, Color.RED);
				}
				hk.release();
			}
		}
		hexBuffer.end();
		
		batch.begin(cam);
		batch.render(inst, environ);
		block.draw(batch, environ);
		batch.render(planet.modelInst, environ);
		batch.end();
		
		shapes.setProjectionMatrix(cam.combined);
		shapes.begin(ShapeType.Line);
		shapes.line(0, 0, 0, 10, 0, 0, Color.RED, Color.RED);
		shapes.line(0, 0, 0, 0, 10, 0, Color.BLUE, Color.BLUE);
		shapes.line(0, 0, 0, 0, 0, 10, Color.GREEN, Color.GREEN);
		shapes.end();
		
		fpsLabel.setText("fps: " + Gdx.graphics.getFramesPerSecond());
		fpsLabel.setPosition(0, 0);
		ui.act(delta);
		ui.draw();
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void show() {
		Gdx.input.setInputProcessor(myCon);
	}

	@Override
	public void hide() {

	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}

	@Override
	public void dispose() {

	}

}
