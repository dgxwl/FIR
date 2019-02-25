package qwe;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class World extends JPanel {
	private static final long serialVersionUID = 1L;
	
	public static final int COLS = 15;
	public static final int ROWS = 15;
	public static final int BLOCK_SIZE = 20;
	public static final int WIDTH = COLS * BLOCK_SIZE + 60;
	public static final int HEIGHT = ROWS * BLOCK_SIZE + 60;
	public static final int OFFSET = 20;
	
	public void addListener() {
		MouseAdapter l = new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int col = e.getX();
				int row = e.getY();
				System.out.println("col: " + (col / BLOCK_SIZE - BLOCK_SIZE / 2 + OFFSET) + ", row: " + (row / BLOCK_SIZE - BLOCK_SIZE / 2 + OFFSET));
			}
		};
		this.addMouseListener(l);
	}
	
	public void drawPiece(int col, int row, Graphics g, Color c) {
		g.setColor(c);
		g.fillOval(col * BLOCK_SIZE - 9 + OFFSET, row * BLOCK_SIZE - 9 + OFFSET, 18, 18);
	}
	
	@Override
	public void paint(Graphics g) {
		g.setColor(new Color(211, 167, 138));
		g.fillRect(0, 0, WIDTH, HEIGHT);
		g.setColor(Color.BLACK);
		for (int i = 0; i < ROWS; i++) {
			g.drawLine(0 + OFFSET, BLOCK_SIZE * i + OFFSET, ROWS * BLOCK_SIZE, BLOCK_SIZE * i + OFFSET);
		}
		for (int i = 0; i < COLS; i++) {
			g.drawLine(BLOCK_SIZE * i + OFFSET, 0 + OFFSET, BLOCK_SIZE * i + OFFSET, COLS * BLOCK_SIZE);
		}
		g.fillOval(OFFSET + BLOCK_SIZE * 7 - 3, OFFSET + BLOCK_SIZE * 7 - 3, 6, 6);
		g.fillOval(OFFSET + BLOCK_SIZE * 3 - 3, OFFSET + BLOCK_SIZE * 3 - 3, 6, 6);
		g.fillOval(OFFSET + BLOCK_SIZE * 11 - 3, OFFSET + BLOCK_SIZE * 3 - 3, 6, 6);
		g.fillOval(OFFSET + BLOCK_SIZE * 3 - 3, OFFSET + BLOCK_SIZE * 11 - 3, 6, 6);
		g.fillOval(OFFSET + BLOCK_SIZE * 11 - 3, OFFSET + BLOCK_SIZE * 11 - 3, 6, 6);
		
		drawPiece(1, 1, g, Color.WHITE);
	}
	
	public static void main(String[] args) {
		JFrame frame = new JFrame("fir");
		frame.setSize(WIDTH, HEIGHT);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		
		World world = new World();
		world.addListener();
		frame.add(world);
		
		frame.setVisible(true);
	}
}
