import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;

public class Shape {

	private int x1;
	private int x2;
	private int y1;
	private int y2;
	private int angle;
	private int[] x;
	private int[] y;
	
	private Color color;
	private BasicStroke stroke;
	private String message;
	
	public boolean transparent;
	
	private int shape;
	private Font font;
	private int edges;
	
	
	public Shape(int x1, int y1,int x2, int y2, Color color,BasicStroke stroke, int shape,boolean transparent){//∆’Õ®Õº–Œ
		this.x1 = x1;
		this.x2 = x2;
		this.y1 = y1;
		this.y2 = y2;
		this.color = color;
		this.stroke = stroke;
		this.shape = shape;
		this.transparent = transparent;
	}
	
	public Shape(int x1, int y1,int x2, int y2, Color color,BasicStroke stroke, int shape,boolean transparent,int angle){//‘≤ª° ∏¥”√angle
		this.x1 = x1;
		this.x2 = x2;
		this.y1 = y1;
		this.y2 = y2;
		this.color = color;
		this.stroke = stroke;
		this.shape = shape;
		this.transparent = transparent;
		this.angle = angle;
	}
	
	public Shape(int x1, int y1, int fontSize, Font font, Color color, BasicStroke stroke, int shape, String message){//Œƒ◊÷
		this.x1 = x1;
		this.y1 = y1;
		this.font = font;
		this.color = color;
		this.stroke = stroke;
		this.shape = shape;
		this.message = message;

	}
	
	public Shape(int[] x, int[] y, Color color,BasicStroke stroke, int shape,boolean transparent,int edges){//‘≤ª° ∏¥”√angle
		this.x=x;
		this.y=y;
		this.color = color;
		this.stroke = stroke;
		this.shape = shape;
		this.transparent = transparent;
		this.edges = edges;
	}
	public int getShape(){
		return this.shape;
	}
	public String getMessage() {
		return this.message;
	}
	public Font getFont() {
		return this.font;
	}
	public int getx1(){
		return this.x1;
	}
	public int getx2(){
		return this.x2;
	}
	public int gety1(){
		return this.y1;
	}
	public int gety2(){
		return this.y2;
	}
	public Color getColor(){
		return this.color;
	}
	public BasicStroke getStroke(){
		return this.stroke;
	}
	public boolean getTransparency(){
		return this.transparent;
	}
	public int getAngle() {
		return this.angle;
	}
	public int[] getx() {
		return this.x;
	}
	public int[] gety() {
		return this.y;
	}
	public int getEdges() {
		return this.edges;
	}
}

