package qwe;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class VsWorld extends World {
	private static final long serialVersionUID = 1L;

	public void addListener() {
		MouseAdapter l = new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (blackWin || whiteWin) {
					return;
				}
				int col = (e.getX() - OFFSET + PIECE_RADIUS) / BLOCK_SIZE;
				int row = (e.getY() - OFFSET + PIECE_RADIUS) / BLOCK_SIZE;
				if (col > COLS - 1 || row > ROWS - 1) {
					return;
				}

				board[col][row] = nextColor;
				checkWin();
				repaint();
				nextColor = (nextColor == Piece.B) ? Piece.W : Piece.B;
			}
		};
		this.addMouseListener(l);
	}
}
