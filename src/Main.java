import java.awt.EventQueue;

import javax.swing.JFrame;
import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.ImageIcon;

import javax.swing.JButton;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Desktop;
import javax.swing.border.BevelBorder;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JRadioButtonMenuItem;
import java.awt.Font;
import java.awt.Graphics2D;

import javax.swing.SwingConstants;
import javax.swing.JLabel;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.InputEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import javax.imageio.ImageIO;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.ButtonGroup;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.DefaultComboBoxModel;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import javax.swing.JCheckBox;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.KeyEvent;
import javax.swing.JPopupMenu;
import java.awt.Component;
import javax.swing.JSeparator;

public class Main implements ActionListener,MouseListener,MouseMotionListener {

	private JFrame frame;
	private final ButtonGroup buttonGroup = new ButtonGroup();
	boolean isFirstPoint = true;
	boolean isFirstRun = true;
	boolean notFill = true;
	int edges;
	private Color nowColor = Color.black;
	int x1,x2,y1,y2;
	int drawType = PaintingGround.PEN;
	PaintingGround paintingGround;
	JLabel nowToolLabel;
	protected JFileChooser fc;
	protected File f;
	protected File of;
	protected String textMessage;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Main window = new Main();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Main() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setTitle("\u753B\u56FE");
		frame.setBounds(390, 190, 1178, 700);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.addComponentListener(new ComponentAdapter() {//让窗口响应大小改变事件
            @Override
            public void componentResized(ComponentEvent e) {
            	if(isFirstRun)
            		isFirstRun=false;
            	else
            		paintingGround.init(false);
            	if(of!=null)
            		openFile(of,false);
            }
        });
		
		//JPanel panel = new JPanel();
		paintingGround = new PaintingGround();
		paintingGround.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		frame.getContentPane().add(paintingGround, BorderLayout.CENTER);
		
		JPopupMenu popupMenu = new JPopupMenu();
		addPopup(paintingGround, popupMenu);
		
		JMenuItem openPopMenu = new JMenuItem("\u6253\u5F00\u6587\u4EF6");
		openPopMenu.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (fc.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
					f = fc.getSelectedFile();
					of = f;
					openFile(f,true);
				}
			}
		});
		popupMenu.add(openPopMenu);
		
		JMenuItem savePopMenu = new JMenuItem("\u4FDD\u5B58\u6587\u4EF6");
		savePopMenu.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (fc.showSaveDialog(frame) == JFileChooser.APPROVE_OPTION) {
					f = new File(fc.getSelectedFile() + ".png");									
					try {
						saveFile(f);
						JOptionPane.showMessageDialog(frame, "图像保存成功！","保存文件",JOptionPane.INFORMATION_MESSAGE);
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}
		});
		popupMenu.add(savePopMenu);
		
		JMenuItem exitPopMenu = new JMenuItem("\u5173\u95ED\u7A0B\u5E8F");
		exitPopMenu.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int res=JOptionPane.showConfirmDialog(frame, "关闭前是否保存当前图像？", "请确认", JOptionPane.YES_NO_OPTION);
                if(res==JOptionPane.YES_OPTION){
                	if (fc.showSaveDialog(frame) == JFileChooser.APPROVE_OPTION) {
    					f = new File(fc.getSelectedFile() + ".png");									
    					try {
    						saveFile(f);
    						frame.dispose();
    					} catch (IOException e1) {
    						// TODO Auto-generated catch block
    						e1.printStackTrace();
    					}
    				}
                	of=null;
                }else {
                	frame.dispose();
                }
			}
		});
		popupMenu.add(exitPopMenu);
		
		JSeparator separator = new JSeparator();
		popupMenu.add(separator);
		
		JMenuItem versionPopMenu = new JMenuItem("\u7248\u672C\u4FE1\u606F");
		versionPopMenu.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ImageIcon icon = new ImageIcon("image/p_icon.jpg");//实例化ImageIcon 对象
				icon.setImage(icon.getImage().getScaledInstance(80, 80,Image.SCALE_DEFAULT));//可以用下面三句代码来代替
				JOptionPane.showMessageDialog(frame, " Java绘图\n 作者：CrayonDing \n Blog：mcper.cn","作者信息",JOptionPane.PLAIN_MESSAGE , icon);
			}
		});
		popupMenu.add(versionPopMenu);
		
		JPanel toolPanel = new JPanel();
		frame.getContentPane().add(toolPanel, BorderLayout.WEST);
		
		JPanel panel_1 = new JPanel();
		
		JPanel panel_2 = new JPanel();
		
		JMenuBar menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);
		
		JMenu FileMenu = new JMenu("File");
		menuBar.add(FileMenu);
		
		fc = new JFileChooser(new File("."));
		fc.setFileFilter(new FileNameExtensionFilter("图片文件", "jpg", "png"));
		
		JMenuItem OpenMenuItem = new JMenuItem("打开已有图像");
		OpenMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (fc.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
					f = fc.getSelectedFile();
					of = f;
					openFile(f,true);
				}
			}
		});
		OpenMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_MASK));
		FileMenu.add(OpenMenuItem);
		
		JMenuItem NewMenuItem = new JMenuItem("新建图像");
		NewMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int res=JOptionPane.showConfirmDialog(frame, "新建图像前是否保存当前图像？", "请确认", JOptionPane.YES_NO_OPTION);
                if(res==JOptionPane.YES_OPTION){
                	if (fc.showSaveDialog(frame) == JFileChooser.APPROVE_OPTION) {
    					f = new File(fc.getSelectedFile() + ".png");									
    					try {
    						saveFile(f);
    						JOptionPane.showMessageDialog(frame, "图像保存成功！","保存文件",JOptionPane.INFORMATION_MESSAGE);
    					} catch (IOException e1) {
    						// TODO Auto-generated catch block
    						e1.printStackTrace();
    					}
    				}
                	of=null;
                }else if(res==JOptionPane.NO_OPTION){
                    paintingGround.init(true);
                }else {
                	return;
                }
			}
		});
		NewMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_MASK));
		FileMenu.add(NewMenuItem);
		
		
		JMenuItem SaveMenuItem = new JMenuItem("保存当前图像");
		SaveMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (fc.showSaveDialog(frame) == JFileChooser.APPROVE_OPTION) {
					f = new File(fc.getSelectedFile() + ".png");									
					try {
						saveFile(f);
						JOptionPane.showMessageDialog(frame, "图像保存成功！","保存文件",JOptionPane.INFORMATION_MESSAGE);
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}
		});
		SaveMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK));
		FileMenu.add(SaveMenuItem);
		
		JMenuItem ExitMenuItem = new JMenuItem("关闭程序");
		ExitMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.dispose();
			}
		});
		ExitMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, InputEvent.CTRL_MASK));
		FileMenu.add(ExitMenuItem);
		
		JMenu EditMenu = new JMenu("Edit");
		buttonGroup.add(EditMenu);
		menuBar.add(EditMenu);
		
		JRadioButtonMenuItem PrintChoiceMenu = new JRadioButtonMenuItem("\u9F20\u6807\u753B\u56FE");
		PrintChoiceMenu.setSelected(true);
		PrintChoiceMenu.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				drawType = PaintingGround.PEN;
				nowToolLabel.setText("画笔");
				clearPos();
			}
		});
		buttonGroup.add(PrintChoiceMenu);
		EditMenu.add(PrintChoiceMenu);
		
		JRadioButtonMenuItem TextChoiceItem = new JRadioButtonMenuItem("\u8F93\u5165\u6587\u5B57");
		TextChoiceItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String text = JOptionPane.showInputDialog(frame,"请输入文字内容","文字工具",JOptionPane.INFORMATION_MESSAGE); 
				if(text == null)
					return;
				else
					textMessage = text;
				drawType = PaintingGround.TEXT;
				nowToolLabel.setText("文字");
				clearPos();
			}
		});
		buttonGroup.add(TextChoiceItem);
		EditMenu.add(TextChoiceItem);
		
		JMenu HelpMenu = new JMenu("About");
		menuBar.add(HelpMenu);
		
		JMenuItem HelpMenuItem = new JMenuItem("\u5E2E\u52A9");
		HelpMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Desktop desktop = Desktop.getDesktop();
				try {
					desktop.browse(new URI("http://mcper.cn/"));
				} catch (IOException | URISyntaxException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		HelpMenu.add(HelpMenuItem);
		
		JMenuItem AuthorMenuItem = new JMenuItem("\u4F5C\u8005\u4FE1\u606F");
		AuthorMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ImageIcon icon = new ImageIcon("image/p_icon.jpg");//实例化ImageIcon 对象
				icon.setImage(icon.getImage().getScaledInstance(80, 80,Image.SCALE_DEFAULT));//可以用下面三句代码来代替
				JOptionPane.showMessageDialog(frame, " 作者：CrayonDing \n Blog：mcper.cn","作者信息",JOptionPane.PLAIN_MESSAGE , icon);
			}
		});
		HelpMenu.add(AuthorMenuItem);
		
		JButton blackColorButton = new JButton("\u221A");
		blackColorButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(drawType != PaintingGround.ERASER)
					nowColor = Color.BLACK;
				nowToolLabel.setForeground(nowColor);
			}
		});
		blackColorButton.setToolTipText("\u9ED1\u8272");
		blackColorButton.setForeground(Color.BLACK);
		blackColorButton.setBackground(Color.BLACK);
		
		JButton redColorButton = new JButton("\u221A");
		redColorButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(drawType != PaintingGround.ERASER)
					nowColor = Color.RED;
				nowToolLabel.setForeground(nowColor);
			}
		});
		redColorButton.setToolTipText("\u7EA2\u8272");
		redColorButton.setForeground(Color.RED);
		redColorButton.setBackground(Color.RED);
		
		JButton greenColorButton = new JButton("\u7EFF\u8272");
		greenColorButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(drawType != PaintingGround.ERASER)
					nowColor = Color.GREEN;
				nowToolLabel.setForeground(nowColor);
			}
		});
		greenColorButton.setToolTipText("\u7EFF\u8272");
		greenColorButton.setForeground(Color.GREEN);
		greenColorButton.setBackground(Color.GREEN);
		
		JButton orangeColorButton = new JButton("\u6A59\u8272");
		orangeColorButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(drawType != PaintingGround.ERASER)
					nowColor = Color.ORANGE;
				nowToolLabel.setForeground(nowColor);
			}
		});
		orangeColorButton.setToolTipText("\u6A59\u8272");
		orangeColorButton.setForeground(Color.ORANGE);
		orangeColorButton.setBackground(Color.ORANGE);
		
		JButton whiteColorButton = new JButton("\u767D\u8272");
		whiteColorButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(drawType != PaintingGround.ERASER)
					nowColor = Color.WHITE;
				nowToolLabel.setForeground(nowColor);
			}
		});
		whiteColorButton.setToolTipText("\u767D\u8272");
		whiteColorButton.setForeground(Color.WHITE);
		whiteColorButton.setBackground(Color.WHITE);
		
		JButton yellowColorButton = new JButton("\u3002");
		yellowColorButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(drawType != PaintingGround.ERASER)
					nowColor = Color.YELLOW;
				nowToolLabel.setForeground(nowColor);
			}
		});
		yellowColorButton.setToolTipText("\u9EC4\u8272");
		yellowColorButton.setForeground(Color.YELLOW);
		yellowColorButton.setBackground(Color.YELLOW);
		
		JButton blueColorButton = new JButton("\u3002");
		blueColorButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(drawType != PaintingGround.ERASER)
					nowColor = Color.BLUE;
				nowToolLabel.setForeground(nowColor);
			}
		});
		blueColorButton.setToolTipText("\u84DD\u8272");
		blueColorButton.setForeground(Color.BLUE);
		blueColorButton.setBackground(Color.BLUE);
		
		JButton pinkColorButton = new JButton("\u3002");
		pinkColorButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(drawType != PaintingGround.ERASER)
					nowColor = Color.PINK;
				nowToolLabel.setForeground(nowColor);
			}
		});
		pinkColorButton.setToolTipText("\u7C89\u8272");
		pinkColorButton.setForeground(Color.PINK);
		pinkColorButton.setBackground(Color.PINK);
		
		JComboBox thickList = new JComboBox();
		thickList.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				 float content = Float.parseFloat(thickList.getSelectedItem().toString());
				 paintingGround.setThickness(content);
			}
		});
		thickList.setModel(new DefaultComboBoxModel(new String[] {"1", "2", "3", "4", "5", "6", "7", "8"}));
		
		JCheckBox fillCheckBox = new JCheckBox("\u586B\u5145");
		fillCheckBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent  e) {
				notFill = !fillCheckBox.isSelected();
			}
		});
		fillCheckBox.setFont(new Font("等线", Font.PLAIN, 14));
		fillCheckBox.setHorizontalAlignment(SwingConstants.CENTER);
		GroupLayout gl_panel_2 = new GroupLayout(panel_2);
		gl_panel_2.setHorizontalGroup(
			gl_panel_2.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_panel_2.createSequentialGroup()
					.addGap(15)
					.addGroup(gl_panel_2.createParallelGroup(Alignment.LEADING)
						.addComponent(fillCheckBox, 0, 68, Short.MAX_VALUE)
						.addComponent(thickList, 0, 68, Short.MAX_VALUE)
						.addGroup(gl_panel_2.createSequentialGroup()
							.addComponent(blackColorButton, GroupLayout.PREFERRED_SIZE, 31, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(redColorButton, GroupLayout.PREFERRED_SIZE, 31, Short.MAX_VALUE))
						.addGroup(gl_panel_2.createSequentialGroup()
							.addComponent(yellowColorButton, GroupLayout.PREFERRED_SIZE, 31, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(greenColorButton, 0, 0, Short.MAX_VALUE))
						.addGroup(gl_panel_2.createSequentialGroup()
							.addComponent(blueColorButton, GroupLayout.PREFERRED_SIZE, 31, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(orangeColorButton, 0, 0, Short.MAX_VALUE))
						.addGroup(gl_panel_2.createSequentialGroup()
							.addComponent(pinkColorButton, GroupLayout.PREFERRED_SIZE, 31, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(whiteColorButton, 0, 0, Short.MAX_VALUE)))
					.addGap(15))
		);
		gl_panel_2.setVerticalGroup(
			gl_panel_2.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_2.createSequentialGroup()
					.addGap(5)
					.addGroup(gl_panel_2.createParallelGroup(Alignment.BASELINE)
						.addComponent(blackColorButton)
						.addComponent(redColorButton))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panel_2.createParallelGroup(Alignment.BASELINE)
						.addComponent(yellowColorButton)
						.addComponent(greenColorButton))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panel_2.createParallelGroup(Alignment.BASELINE)
						.addComponent(blueColorButton)
						.addComponent(orangeColorButton))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panel_2.createParallelGroup(Alignment.BASELINE)
						.addComponent(pinkColorButton)
						.addComponent(whiteColorButton))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(thickList, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(fillCheckBox)
					.addContainerGap(71, Short.MAX_VALUE))
		);
		panel_2.setLayout(gl_panel_2);
		
		nowToolLabel = new JLabel("\u76F4\u7EBF");
		nowToolLabel.setFont(new Font("等线", Font.BOLD, 15));
		nowToolLabel.setHorizontalAlignment(SwingConstants.CENTER);
		
		JButton lineButton = new JButton("直线");
		lineButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				drawType = PaintingGround.LINE;
				nowToolLabel.setText("直线");
				PrintChoiceMenu.setSelected(true);
				nowColor = nowToolLabel.getForeground();
				clearPos();
			}
		});
		lineButton.setFont(new Font("等线", Font.PLAIN, 15));
		
		JButton rectangleButton = new JButton("矩形");
		rectangleButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				drawType = PaintingGround.RECTANGLE;
				nowToolLabel.setText("矩形");
				PrintChoiceMenu.setSelected(true);
				nowColor = nowToolLabel.getForeground();
				clearPos();

			}
		});
		rectangleButton.setFont(new Font("等线", Font.PLAIN, 15));
		
		JButton roundButton = new JButton("圆形");
		roundButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				drawType = PaintingGround.ROUND;
				nowToolLabel.setText("圆形");
				PrintChoiceMenu.setSelected(true);
				nowColor = nowToolLabel.getForeground();
				clearPos();

			}
		});
		roundButton.setFont(new Font("等线", Font.PLAIN, 15));
		
		JButton ovalButton = new JButton("椭圆");
		ovalButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				drawType = PaintingGround.ELLIPSE;
				nowToolLabel.setText("椭圆");
				PrintChoiceMenu.setSelected(true);
				nowColor = nowToolLabel.getForeground();
				clearPos();

			}
		});
		ovalButton.setFont(new Font("等线", Font.PLAIN, 15));
		
		JButton arcButton = new JButton("圆弧");
		arcButton.setFont(new Font("等线", Font.PLAIN, 15));
		arcButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				drawType = PaintingGround.ARC;
				nowToolLabel.setText("圆弧");
				PrintChoiceMenu.setSelected(true);
				nowColor = nowToolLabel.getForeground();
				clearPos();

			}
		});
		
		JButton polygonButton = new JButton("\u591A\u8FB9\u5F62");
		polygonButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Object[] possibleValues = { "三角形", "五边形", "六边形" };
				Object selectedValue = JOptionPane.showInputDialog(null, "请选择要绘制的多边形", "多边形绘制",	JOptionPane.INFORMATION_MESSAGE, null,	possibleValues, possibleValues[0]);

				if(selectedValue == null)
					return;
				else if(selectedValue.equals("三角形"))
					paintingGround.edges=3;
				else if(selectedValue.equals("五边形"))
					paintingGround.edges=5;
				else if(selectedValue.equals("六边形"))
					paintingGround.edges=6;
				
				drawType = PaintingGround.POLOSHAPE;
				nowToolLabel.setText(selectedValue.toString());
				nowColor = nowToolLabel.getForeground();
				PrintChoiceMenu.setSelected(true);
				clearPos();
			}
		});
		polygonButton.setFont(new Font("等线", Font.PLAIN, 15));
		
		JButton penButton = new JButton("画笔");
		penButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				drawType = PaintingGround.PEN;
				nowToolLabel.setText("画笔");
				PrintChoiceMenu.setSelected(true);
				nowColor = nowToolLabel.getForeground();
				clearPos();
			}
		});
		penButton.setFont(new Font("等线", Font.PLAIN, 15));
		
		JButton clearButton = new JButton("清屏");
		clearButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				paintingGround.clear(true);
				nowColor = nowToolLabel.getForeground();
			}
		});
		clearButton.setFont(new Font("等线", Font.PLAIN, 15));
		
		JButton ereaseButton = new JButton("橡皮");
		ereaseButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				drawType = PaintingGround.ERASER;
				nowColor = Color.white;
				nowToolLabel.setText("橡皮");
				PrintChoiceMenu.setSelected(true);
				clearPos();
			}
		});
		ereaseButton.setFont(new Font("等线", Font.PLAIN, 15));
		
		
		GroupLayout gl_panel_1 = new GroupLayout(panel_1);
		gl_panel_1.setHorizontalGroup(
			gl_panel_1.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_panel_1.createSequentialGroup()
					.addGroup(gl_panel_1.createParallelGroup(Alignment.TRAILING)
						.addGroup(Alignment.LEADING, gl_panel_1.createSequentialGroup()
							.addContainerGap()
							.addComponent(nowToolLabel, GroupLayout.DEFAULT_SIZE, 77, Short.MAX_VALUE))
						.addGroup(gl_panel_1.createSequentialGroup()
							.addGap(10)
							.addGroup(gl_panel_1.createParallelGroup(Alignment.TRAILING)
								.addComponent(ereaseButton, GroupLayout.DEFAULT_SIZE, 111, Short.MAX_VALUE)
								.addComponent(penButton, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 111, Short.MAX_VALUE)
								.addComponent(arcButton, GroupLayout.DEFAULT_SIZE, 111, Short.MAX_VALUE)
								.addComponent(ovalButton, GroupLayout.DEFAULT_SIZE, 111, Short.MAX_VALUE)
								.addComponent(polygonButton, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 111, Short.MAX_VALUE)
								.addComponent(roundButton, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 111, Short.MAX_VALUE)
								.addComponent(rectangleButton, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 111, Short.MAX_VALUE)
								.addComponent(lineButton, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 111, Short.MAX_VALUE)
								.addComponent(clearButton, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 111, Short.MAX_VALUE))))
					.addGap(71))
		);
		gl_panel_1.setVerticalGroup(
			gl_panel_1.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_1.createSequentialGroup()
					.addGap(10)
					.addComponent(penButton)
					.addGap(10)
					.addComponent(lineButton)
					.addGap(10)
					.addComponent(rectangleButton)
					.addGap(10)
					.addComponent(roundButton)
					.addGap(10)
					.addComponent(ovalButton)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(arcButton)
					.addGap(10)
					.addComponent(polygonButton)
					.addGap(10)
					.addComponent(clearButton, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE)
					.addGap(10)
					.addComponent(ereaseButton, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE)
					.addGap(10)
					.addComponent(nowToolLabel, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
					.addGap(51))
		);
		panel_1.setLayout(gl_panel_1);
		GroupLayout gl_toolPanel = new GroupLayout(toolPanel);
		gl_toolPanel.setHorizontalGroup(
			gl_toolPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_toolPanel.createSequentialGroup()
					.addGap(10)
					.addGroup(gl_toolPanel.createParallelGroup(Alignment.LEADING)
						.addComponent(panel_2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(panel_1, 0, 0, Short.MAX_VALUE))
					.addContainerGap())
		);
		gl_toolPanel.setVerticalGroup(
			gl_toolPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_toolPanel.createSequentialGroup()
					.addComponent(panel_1, GroupLayout.PREFERRED_SIZE, 383, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(panel_2, GroupLayout.PREFERRED_SIZE, 298, GroupLayout.PREFERRED_SIZE)
					.addContainerGap())
		);
		toolPanel.setLayout(gl_toolPanel);
		
		
		frame.setVisible(true);
		paintingGround.init(true);

		
		
		//添加鼠标触发事件
				paintingGround.addMouseListener(new MouseAdapter() {
					public void mouseReleased(MouseEvent evn) {
						isFirstPoint = true;
						switch(drawType) {
						case PaintingGround.LINE:
						case PaintingGround.RECTANGLE:
						case PaintingGround.ROUND:
						case PaintingGround.ELLIPSE:
						case PaintingGround.ARC:
						case PaintingGround.POLOSHAPE:
							paintingGround.drawShape(x1,y1,x2,y2,nowColor,drawType,notFill,isFirstPoint);
							break;
						case PaintingGround.TEXT:
							x2 = evn.getX();
							y2 = evn.getY();
							paintingGround.drawText(x2, y2, nowColor, new Font("等线", Font.PLAIN, 15), textMessage);
						default:
							break;
						}
					}
					@Override
					public void mouseEntered(MouseEvent e) {
						Cursor customCursor;
						if(drawType == PaintingGround.TEXT) {
							customCursor = Toolkit.getDefaultToolkit().createCustomCursor(new ImageIcon("image/text.png").getImage(),new Point(10,31), "TEXT");
						}else if(drawType == PaintingGround.ERASER) {
							customCursor = Toolkit.getDefaultToolkit().createCustomCursor(new ImageIcon("image/eraser.png").getImage(),new Point(10,31), "ERASER");
						}else{
							customCursor = Toolkit.getDefaultToolkit().createCustomCursor(new ImageIcon("image/pencil.png").getImage(),new Point(0,31), "PENCIL");
						}
						paintingGround.setCursor(customCursor);
					}
					@Override
					public void mouseExited(MouseEvent e) {
						paintingGround.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
					}
				});
		//对鼠标的输入进行判断并调用画图程序
				paintingGround.addMouseMotionListener(new MouseMotionAdapter() {
					public void mouseDragged(MouseEvent evn) {
						x2 = evn.getX();
						y2 = evn.getY();
						if(isFirstPoint) {
							x1 = evn.getX();
							y1 = evn.getY();
							isFirstPoint = false;
						}
						switch(drawType) {
						case PaintingGround.LINE:
						case PaintingGround.RECTANGLE:
						case PaintingGround.ROUND:
						case PaintingGround.ELLIPSE:
						case PaintingGround.ARC:
						case PaintingGround.POLOSHAPE:
							paintingGround.drawShape(x1,y1,x2,y2,nowColor,drawType,notFill,false);
							break;
						case PaintingGround.PEN:
						case PaintingGround.ERASER:
							paintingGround.drawShape(x1,y1,x2,y2,nowColor,PaintingGround.LINE,notFill,true);
							x1 = x2;
							y1 = y2;
							break;
						default:
							break;
						}
					}
				});	
	}
	protected void clearPos() {
		x1 = 0;
		x2 = 0;
		y1 = 0;
		y2 = 0;
		isFirstPoint = true;
	}
	
	private void openFile(File f,boolean isFirstRun) {
		try {
			paintingGround.openFile(ImageIO.read(f),isFirstRun);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


	}
	private void saveFile(File f) throws IOException {
		BufferedImage im = getPanel(paintingGround);
		ImageIO.write(im, "png", f);
	}
	private BufferedImage getPanel(JPanel panel){
	    int w = panel.getWidth()-3;
	    int h = panel.getHeight()-3;
	    BufferedImage bi = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
	    Graphics2D g = bi.createGraphics();
	    g.translate(-3, -3);
	    panel.print(g);
	    return bi;
	}

	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
			}
			public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
			}
			public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
			}
			public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
			}
			public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
			}
			public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
			}
			public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
			}
			public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
			}
	private static void addPopup(Component component, final JPopupMenu popup) {
		component.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				if (e.isPopupTrigger()) {
					showMenu(e);
				}
			}
			public void mouseReleased(MouseEvent e) {
				if (e.isPopupTrigger()) {
					showMenu(e);
				}
			}
			private void showMenu(MouseEvent e) {
				popup.show(e.getComponent(), e.getX(), e.getY());
			}
		});
	}
}
