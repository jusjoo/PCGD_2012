package sov;

import org.lwjgl.opengl.XRandR.Screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class TextElement {
	
	private BitmapFont font;	
	private SpriteBatch textBatch;	
	private float posX;
	private float posY;
	private String text;
	private boolean visible;
	public static final String fontLocation = "assets/fonts/";
	public static enum Font {
		Font1("font1.fnt", "font1.png");
		
		String file;
		String image;
		
		Font(String file, String image) {
			this.file = file;
			this.image = image;
		}
	}
	public TextElement () {
		textBatch = new SpriteBatch();
		posX = 0;
		posY = 0;
		
		font = new BitmapFont(new FileHandle(fontLocation+Font.Font1.file), new FileHandle(fontLocation+Font.Font1.image), false);
		
		text = new String();
		
		visible = true;
		
	}
	public TextElement (float posX, float posY) {
		textBatch = new SpriteBatch();
		this.posX = posX;
		this.posY = posY;
		
		font = new BitmapFont(new FileHandle(fontLocation+Font.Font1.file), new FileHandle(fontLocation+Font.Font1.image), false);
		
		text = new String();
		
		visible = true;
		
	}
	
	public void setPosition(float x, float y) {
		posX = x;
		posY = y;
	}	
	
	public void print(String text) {		
		this.text = text;
	}
	
	public void render(SpriteBatch batch) {
		
		float x = posX;
		float y = Gdx.graphics.getHeight() - posY; 
		textBatch.begin();
		font.draw(textBatch, text, x, y);
		textBatch.end();
		
	}
	
	public void setVisibility(boolean vis) {
		visible = vis;
	}
	
	public boolean isVisible() {
		return visible;
	}

}
