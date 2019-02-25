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
	public static final int STAR_RADIUS = 3;
	public static final int PIECE_RADIUS = 9;
	
	public static PieceColor nextColor = PieceColor.B;

	public void addListener() {
		MouseAdapter l = new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int col = (e.getX() - OFFSET + 9) / BLOCK_SIZE;
				int row = (e.getY() - OFFSET + 9) / BLOCK_SIZE;
				if (col > 14 || row > 14) {
					return ;
				}
				System.out.println("col: " + col + ", row: " + row);
			}
		};
		this.addMouseListener(l);
	}

	// 画星
	public void drawStar(int col, int row, Graphics g) {
		g.fillOval(OFFSET + BLOCK_SIZE * col - STAR_RADIUS, OFFSET + BLOCK_SIZE * row - STAR_RADIUS, STAR_RADIUS * 2,
				STAR_RADIUS * 2);
	}

	// 画棋子
	public void drawPiece(int col, int row, Graphics g, Color c) {
		g.setColor(c);
		g.fillOval(col * BLOCK_SIZE - PIECE_RADIUS + OFFSET, row * BLOCK_SIZE - PIECE_RADIUS + OFFSET, PIECE_RADIUS * 2,
				PIECE_RADIUS * 2);
	}

	@Override
	public void paint(Graphics g) {
		//********************** 画棋盘 ***************************
		g.setColor(new Color(211, 167, 138));
		g.fillRect(0, 0, WIDTH, HEIGHT);
		g.setColor(Color.BLACK);
		for (int i = 0; i < ROWS; i++) {
			g.drawLine(0 + OFFSET, BLOCK_SIZE * i + OFFSET, ROWS * BLOCK_SIZE, BLOCK_SIZE * i + OFFSET);
		}
		for (int i = 0; i < COLS; i++) {
			g.drawLine(BLOCK_SIZE * i + OFFSET, 0 + OFFSET, BLOCK_SIZE * i + OFFSET, COLS * BLOCK_SIZE);
		}
		drawStar(7, 7, g);
		drawStar(3, 3, g);
		drawStar(11, 3, g);
		drawStar(3, 11, g);
		drawStar(11, 11, g);
		//*******************************************************

		drawPiece(1, 1, g, Color.WHITE);
	}

	public static void main(String[] args) {
		JFrame frame = new JFrame("fir");
		frame.setSize(WIDTH, HEIGHT);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);

		World world = new World();
		world.addListener();
		frame.add(world);

		frame.setVisible(true);
	}
}
