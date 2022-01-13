import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

class PaintingGround extends JPanel {
	public static final int PEN = 0;
	public static final int LINE = 1;
	public static final int RECTANGLE = 2;
	public static final int ROUND = 3;
	public static final int ELLIPSE = 4;
	public static final int ARC = 5;
	public static final int POLOSHAPE = 6;
	public static final int TEXT = 7;
	public static final int ERASER = 8;
	private BufferedImage image = null;
	private Graphics2D allImage;
	private Color currentColor = Color.RED;
	private boolean transparent = true;
	private BasicStroke stroke = new BasicStroke((float) 1);
	public int edges;

	
	//利用两个STACK分别存储Preview和Save的内容
	private Stack<Shape> shapes; //实际图形
	//private Stack<Shape> preview;//预览图形
	private Shape preview;
	private Color fillColor;
	private Font currentFont =new Font("等线", Font.PLAIN, 15);
	
	
	
//构造函数
	public PaintingGround() {
		setBackground(Color.WHITE);
		this.shapes = new Stack<Shape>();
		//this.preview = new Stack<Shape>();
		preview=null;
	}
//判断是用实心还是空心的,
	public void paintComponent(Graphics g) {
		g.drawImage(image, 0, 0, this);//存储图像到缓存图片中
		
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);//抗锯齿 

		for(Shape s : shapes){
			g2d.setColor(s.getColor());
			g2d.setStroke(s.getStroke());

			if (s.getShape() == LINE){
				g2d.drawLine(s.getx1(), s.gety1(), s.getx2(), s.gety2());
			}
			else if (s.getShape() == RECTANGLE){
				g2d.drawRect(s.getx1(), s.gety1(), s.getx2(), s.gety2());
				if(s.transparent == false){
					g2d.setColor(s.getColor());
					g2d.fillRect(s.getx1(), s.gety1(), s.getx2(), s.gety2());
				}
			}
			else if (s.getShape() == ROUND){
				int r = Math.min(s.getx2(),s.gety2());
				g2d.drawOval(s.getx1(), s.gety1(), r,r);
				if(s.transparent == false){
					g2d.setColor(s.getColor());
					g2d.fillOval(s.getx1(), s.gety1(),r,r);
				}
			}else if(s.getShape() == ELLIPSE) {
				g2d.drawOval(s.getx1(), s.gety1(), s.getx2(), s.gety2());
				if(s.transparent == false){
					g2d.setColor(s.getColor());
					g2d.fillOval(s.getx1(), s.gety1(), s.getx2(), s.gety2());
				}
			}else if(s.getShape() == ARC) {
				g2d.drawArc(s.getx1(), s.gety1(), s.getx2(), s.gety2(),0,s.getAngle());
				if(s.transparent == false){
					g2d.setColor(s.getColor());
					g2d.fillArc(s.getx1(), s.gety1(), s.getx2(), s.gety2(),0,s.getAngle());
				}
			}else if(s.getShape() == POLOSHAPE) {
				g2d.drawPolygon(s.getx(), s.gety(), s.getEdges());
				if(s.transparent == false){
					g2d.setColor(s.getColor());
					g2d.fillPolygon(s.getx(), s.gety(), s.getEdges());				
				}
			}else if (s.getShape() == TEXT) {
				g2d.setFont(s.getFont());
				g2d.drawString(s.getMessage(), s.getx1(), s.gety1());
			}
		}
		if(preview != null) {
			Shape s = preview;
			
			g2d.setColor(s.getColor());
			g2d.setStroke(s.getStroke());
			
			if (s.getShape() == LINE){
				g2d.drawLine(s.getx1(), s.gety1(), s.getx2(), s.gety2());
			}
			else if (s.getShape() == RECTANGLE){
				g2d.drawRect(s.getx1(), s.gety1(), s.getx2(), s.gety2());
				if(s.transparent == false){
					g2d.setColor(s.getColor());
					g2d.fillRect(s.getx1(), s.gety1(), s.getx2(), s.gety2());
				}
			}
			else if (s.getShape() == ROUND){
				int r = Math.min(s.getx2(),s.gety2());
				g2d.drawOval(s.getx1(), s.gety1(), r,r);
				if(s.transparent == false){
					g2d.setColor(s.getColor());
					g2d.fillOval(s.getx1(), s.gety1(),r,r);
				}
			}else if(s.getShape() == ELLIPSE) {
				g2d.drawOval(s.getx1(), s.gety1(), s.getx2(), s.gety2());
				if(s.transparent == false){
					g2d.setColor(s.getColor());
					g2d.fillOval(s.getx1(), s.gety1(), s.getx2(), s.gety2());
				}
			}else if(s.getShape() == ARC) {
				g2d.drawArc(s.getx1(), s.gety1(), s.getx2(), s.gety2(),0,s.getAngle());
				if(s.transparent == false){
					g2d.setColor(s.getColor());
					g2d.fillArc(s.getx1(), s.gety1(), s.getx2(), s.gety2(),0,s.getAngle());
				}
			}else if(s.getShape() == POLOSHAPE) {
				g2d.drawPolygon(s.getx(), s.gety(), s.getEdges());
				if(s.transparent == false){
					g2d.setColor(s.getColor());
					g2d.fillPolygon(s.getx(), s.gety(), s.getEdges());				
				}
			}
			else if (s.getShape() == TEXT) {
				g2d.setFont(s.getFont());
				g2d.drawString(s.getMessage(), s.getx1(), s.gety1());
			}
			preview = null;
			
		}
		
	}
	
	public void setColor(Color c){
		currentColor = c;
		allImage.setColor(c);		
	}
	public void setFillColor(Color c){
		this.fillColor = c;
	}
	public void setThickness(float f){
		stroke = new BasicStroke(f);
		allImage.setStroke(stroke);
	}
	public void setTransparency(Boolean b){
		this.transparent = b;
	}
	public void clear(boolean isFirstRun){
		allImage.setPaint(Color.white);
		allImage.fillRect(0, 0, getSize().width, getSize().height);
		if(isFirstRun)
			shapes.removeAllElements();
		repaint();
		allImage.setColor(currentColor);
	}
	
	public void drawLine(int x1, int y1, int x2,int y2,boolean isReal) {
		if(isReal)
			shapes.push(new Shape(x1, y1, x2, y2,currentColor,stroke,LINE,transparent));
		else
			preview = new Shape(x1, y1, x2, y2,currentColor,stroke,LINE,transparent);
		repaint();
	}
//具体的实现方式
	
	public void drawText(int x,int y,Color nowColor,Font nowFont,String message) {
		this.currentColor = nowColor;
		this.currentFont = nowFont;
		shapes.push(new Shape(x,y,0, currentFont,currentColor,stroke,TEXT,message));
		repaint();
	}
	
	public void drawShape(int x1,int y1,int x2,int y2,Color nowColor,int shapeType,boolean transparent,boolean isReal) {
		this.currentColor=nowColor;
		int width = Math.abs(x2-x1);
		int height = Math.abs(y2-y1);
		if(shapeType == LINE) {
			if(isReal)
				shapes.push(new Shape(x1, y1, x2, y2,currentColor,stroke,LINE,transparent));
			else
				preview = new Shape(x1, y1, x2, y2,currentColor,stroke,LINE,transparent);
		}else if(shapeType == ARC) {
			if(x2>=x1 & y2>=y1) {
				if(isReal)
					shapes.push(new Shape(x1, y1, width, height,currentColor,stroke,shapeType,transparent,180));
				else 
					preview = new Shape(x1, y1, width, height,currentColor,stroke,shapeType,transparent,180);
			}else if(x2<x1 & y2>y1) {
				if(isReal)
					shapes.push(new Shape(x2, y1, width, height,currentColor,stroke,shapeType,transparent,180));
				else
					preview = new Shape(x2, y1, width, height,currentColor,stroke,shapeType,transparent,180);
			}else if(x2<x1 & y2<y1) {
				if(isReal)
					shapes.push(new Shape(x2, y2, width, height,currentColor,stroke,shapeType,transparent,-180));
				else
					preview = new Shape(x2, y2, width, height,currentColor,stroke,shapeType,transparent,-180);
			}else {
				if(isReal)
					shapes.push(new Shape(x1, y2, width, height,currentColor,stroke,shapeType,transparent,-180));
				else
					preview = new Shape(x1, y2, width, height,currentColor,stroke,shapeType,transparent,-180);
			}
		}else if(shapeType == POLOSHAPE) {
			int x[] = new int[6];
			int y[] = new int[6];
			if(edges == 3) {
				if(x2>=x1 & y2>=y1) {//4
					x[1]=(x1+x2)/2;y[1]=y1;
					x[2]=x1;y[2]=y2;
					x[0]=x2;y[0]=y2;
				}else if(x2<x1 & y2>y1) {//3
					x[1]=(x1+x2)/2;y[1]=y1;
					x[2]=x1;y[2]=y2;
					x[0]=x2;y[0]=y2;
				}else if(x2<x1 & y2<y1) {//2
					x[1]=(x1+x2)/2;y[1]=y2;
					x[2]=x2;y[2]=y1;
					x[0]=x1;y[0]=y1;
				}else {//1
					x[1]=(x1+x2)/2;y[1]=y2;
					x[2]=x2;y[2]=y1;
					x[0]=x1;y[0]=y1;
				}
			}else if(edges == 5) {
				if(x2>=x1 & y2>=y1) {//4
					x[0]=(x1+x2)/2;y[0]=y1;
					x[1]=x1;y[1]=(2*y1+y2)/3;
					x[4]=x2;y[4]=(2*y1+y2)/3;
					x[2]=(3*x1+x2)/4;y[2]=y2;
					x[3]=(x1+3*x2)/4;y[3]=y2;
				}else if(x2<x1 & y2>y1) {//3
					x[0]=(x1+x2)/2;y[0]=y1;
					x[4]=x1;y[4]=(2*y1+y2)/3;
					x[1]=x2;y[1]=(2*y1+y2)/3;
					x[2]=(x1+3*x2)/4;y[2]=y2;
					x[3]=(3*x1+x2)/4;y[3]=y2;
				}else if(x2<x1 & y2<y1) {//2
					x[0]=(x1+x2)/2;y[0]=y2;
					x[4]=x1;y[4]=(y1+2*y2)/3;
					x[1]=x2;y[1]=(y1+2*y2)/3;
					x[2]=(x1+3*x2)/4;y[2]=y1;
					x[3]=(3*x1+x2)/4;y[3]=y1;
				}else {//1
					x[0]=(x1+x2)/2;y[0]=y2;
					x[1]=x1;y[1]=(y1+2*y2)/3;
					x[4]=x2;y[4]=(y1+2*y2)/3;
					x[2]=(3*x1+x2)/4;y[2]=y1;
					x[3]=(x1+3*x2)/4;y[3]=y1;
				}
			}else if(edges == 6) {
					if(x2>=x1 & y2>=y1) {//4
						x[0]=(x1+x2)/2;y[0]=y1;
						x[1]=x1;y[1]=(3*y1+y2)/4;
						x[2]=x1;y[2]=(y1+3*y2)/4;
						x[3]=(x1+x2)/2;y[3]=y2;
						x[4]=x2;y[4]=(y1+3*y2)/4;
						x[5]=x2;y[5]=(3*y1+y2)/4;
					}else if(x2<x1 & y2>y1) {//3
						x[0]=(x1+x2)/2;y[0]=y1;
						x[1]=x1;y[1]=(3*y1+y2)/4;
						x[2]=x1;y[2]=(y1+3*y2)/4;
						x[3]=(x1+x2)/2;y[3]=y2;
						x[4]=x2;y[4]=(y1+3*y2)/4;
						x[5]=x2;y[5]=(3*y1+y2)/4;
					}else if(x2<x1 & y2<y1) {//2
						x[0]=(x1+x2)/2;y[0]=y2;
						x[2]=x1;y[2]=(3*y1+y2)/4;
						x[1]=x1;y[1]=(y1+3*y2)/4;
						x[3]=(x1+x2)/2;y[3]=y1;
						x[5]=x2;y[5]=(y1+3*y2)/4;
						x[4]=x2;y[4]=(3*y1+y2)/4;
					}else {//1
						x[0]=(x1+x2)/2;y[0]=y2;
						x[2]=x1;y[2]=(3*y1+y2)/4;
						x[1]=x1;y[1]=(y1+3*y2)/4;
						x[3]=(x1+x2)/2;y[3]=y1;
						x[5]=x2;y[5]=(y1+3*y2)/4;
						x[4]=x2;y[4]=(3*y1+y2)/4;
						}
			}
			if(isReal)
				shapes.push(new Shape(x,y,currentColor,stroke,shapeType,transparent,edges));
			else 
				preview = new Shape(x,y,currentColor,stroke,shapeType,transparent,edges);
		}else {
			if(x2>=x1 & y2>=y1) {
				if(isReal)
					shapes.push(new Shape(x1, y1, width, height,currentColor,stroke,shapeType,transparent));
				else 
					//preview.push(new Shape(x1, y1, width, height,currentColor,stroke,shapeType,transparent));
					preview = new Shape(x1, y1, width, height,currentColor,stroke,shapeType,transparent);
			}else if(x2<x1 & y2>y1) {
				if(isReal)
					shapes.push(new Shape(x2, y1, width, height,currentColor,stroke,shapeType,transparent));
				else
					//preview.push(new Shape(x2, y1, width, height,currentColor,stroke,shapeType,transparent));
					preview = new Shape(x2, y1, width, height,currentColor,stroke,shapeType,transparent);
			}else if(x2<x1 & y2<y1) {
				if(isReal)
					shapes.push(new Shape(x2, y2, width, height,currentColor,stroke,shapeType,transparent));
				else
					//preview.push(new Shape(x2, y2, width, height,currentColor,stroke,shapeType,transparent));
					preview = new Shape(x2, y2, width, height,currentColor,stroke,shapeType,transparent);
			}else {
				if(isReal)
					shapes.push(new Shape(x1, y2, width, height,currentColor,stroke,shapeType,transparent));
				else
					//preview.push(new Shape(x1, y2, width, height,currentColor,stroke,shapeType,transparent));
					preview = new Shape(x1, y2, width, height,currentColor,stroke,shapeType,transparent);
			}
		}
		repaint();
	}
	public void init(boolean isFirstRun) {
		image = new BufferedImage(this.getWidth(), this.getWidth(),BufferedImage.TYPE_INT_ARGB);
		allImage = image.createGraphics();
		allImage.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		clear(isFirstRun);
	}
	public void openFile(BufferedImage file,boolean isFirstRun) {
			allImage.dispose();
			init(isFirstRun);
			allImage.drawImage(file, 0, 0, this);
	}
	


	
}