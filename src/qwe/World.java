package qwe;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.JPanel;

public abstract class World extends JPanel {
	private static final long serialVersionUID = 1L;

	public static final int COLS = 15;
	public static final int ROWS = 15;
	public static final int BLOCK_SIZE = 20;
	public static final int WIDTH = COLS * BLOCK_SIZE + 60;
	public static final int HEIGHT = ROWS * BLOCK_SIZE + 60;
	public static final int OFFSET = 20;
	public static final int STAR_RADIUS = 3;
	public static final int PIECE_RADIUS = BLOCK_SIZE / 2 - 1;

	protected static Piece nextColor = Piece.B;

	protected Piece[][] board = new Piece[COLS][ROWS];
	protected boolean blackWin = false;
	protected boolean whiteWin = false;

	public abstract void addListener();

	public void checkWin() {
		for (int i = 0; i < COLS; i++) {
			for (int j = 0; j < ROWS; j++) {
				if (board[i][j] == null) {
					continue;
				}

				if (!(i + 1 > COLS - 1) || !(i + 2 > COLS - 1) || !(i + 3 > COLS - 1) || !(i + 4 > COLS - 1)) {
					if (board[i][j] == nextColor && board[i + 1][j] == nextColor && board[i + 2][j] == nextColor
							&& board[i + 3][j] == nextColor && board[i + 4][j] == nextColor) {
						if (nextColor == Piece.B) {
							blackWin = true;
						} else {
							whiteWin = true;
						}
					}
				}

				if (!(j + 1 > ROWS - 1) || !(j + 2 > ROWS - 1) || !(j + 3 > ROWS - 1) || !(j + 4 > ROWS - 1)) {
					if (board[i][j] == nextColor && board[i][j + 1] == nextColor && board[i][j + 2] == nextColor
							&& board[i][j + 3] == nextColor && board[i][j + 4] == nextColor) {
						if (nextColor == Piece.B) {
							blackWin = true;
						} else {
							whiteWin = true;
						}
					}
				}

				if ((!(i + 1 > COLS - 1) || !(i + 2 > COLS - 1) || !(i + 3 > COLS - 1) || !(i + 4 > COLS - 1))
						&& (!(j + 1 > ROWS - 1) || !(j + 2 > ROWS - 1) || !(j + 3 > ROWS - 1) || !(j + 4 > ROWS - 1))) {
					if (board[i][j] == nextColor && board[i + 1][j + 1] == nextColor && board[i + 2][j + 2] == nextColor
							&& board[i + 3][j + 3] == nextColor && board[i + 4][j + 4] == nextColor) {
						if (nextColor == Piece.B) {
							blackWin = true;
						} else {
							whiteWin = true;
						}
					}
				}

				if ((!(i - 1 < 0) || !(i - 2 < 0) || !(i - 3 < 0) || !(i - 4 < 0))
						&& (!(j + 1 > ROWS - 1) || !(j + 2 > ROWS - 1) || !(j + 3 > ROWS - 1) || !(j + 4 > ROWS - 1))) {
					if (board[i][j] == nextColor && board[i - 1][j + 1] == nextColor && board[i - 2][j + 2] == nextColor
							&& board[i - 3][j + 3] == nextColor && board[i - 4][j + 4] == nextColor) {
						if (nextColor == Piece.B) {
							blackWin = true;
						} else {
							whiteWin = true;
						}
					}
				}
			}
		}
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
		// ********************** 画棋盘 ***************************
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
		// *******************************************************

		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[i].length; j++) {
				if (board[i][j] != null) {
					drawPiece(i, j, g, (board[i][j] == Piece.B) ? Color.BLACK : Color.WHITE);
				}
			}
		}
		
		if (blackWin) {
			g.setFont(new Font(null, 0, 50));
			g.setColor(Color.WHITE);
			g.drawString("黑子赢!", WIDTH / 2, HEIGHT / 2);
		}
		if (whiteWin) {
			g.setFont(new Font(null, 0, 50));
			g.setColor(Color.BLACK);
			g.drawString("白子赢!", WIDTH / 2, HEIGHT / 2);
		}
	}
}
