package qwe;

public class CPUWorld extends World {
	private static final long serialVersionUID = 1L;

	private static final int PRIORITY_6 = 6;
	private static final int PRIORITY_5 = 5;
	private static final int PRIORITY_4 = 4;
	private static final int PRIORITY_3 = 3;
	private static final int PRIORITY_2 = 2;
	private static final int PRIORITY_1 = 1;

	

	public void aiAttack(Piece aiColor) {
		int[][] weight = new int[COLS][ROWS]; // 所有位置的权值

		// 遍历棋盘计算权值
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[i].length; j++) {
				if (board[i][j] != null) {
					continue;
				}

				// 活四_●●●●_左边那颗
				// 1.横向
				if (!(i + 1 > COLS - 1) || !(i + 2 > COLS - 1) || !(i + 3 > COLS - 1) || !(i + 4 > COLS - 1) || !(i + 5 > COLS - 1)) {
					if (board[i + 1][j] == aiColor && board[i + 2][j] == aiColor && board[i + 3][j] == aiColor
							&& board[i + 4][j] == aiColor && board[i + 5][j] == null) {
						weight[i][j] += PRIORITY_6;
					}
				}
				//2.纵向
				if (!(j + 1 > ROWS - 1) || !(j + 2 > ROWS - 1) || !(j + 3 > ROWS - 1) || !(j + 4 > ROWS - 1) || !(j + 5 > ROWS - 1)) {
					if (board[i][j + 1] == aiColor && board[i][j + 2] == aiColor
							&& board[i][j + 3] == aiColor && board[i][j + 4] == aiColor && board[i][j + 5] == null) {
						weight[i][j] += PRIORITY_6;
					}
				}
				//3.左上至右下
				if ((!(i + 1 > COLS - 1) || !(i + 2 > COLS - 1) || !(i + 3 > COLS - 1) || !(i + 4 > COLS - 1) || !(i + 5 > COLS - 1))
						&& (!(j + 1 > ROWS - 1) || !(j + 2 > ROWS - 1) || !(j + 3 > ROWS - 1) || !(j + 4 > ROWS - 1) || !(j + 5 > ROWS - 1))) {
					if (board[i + 1][j + 1] == aiColor && board[i + 2][j + 2] == aiColor
							&& board[i + 3][j + 3] == aiColor && board[i + 4][j + 4] == aiColor && board[i + 5][j + 5] == null) {
						weight[i][j] += PRIORITY_6;
					}
				}
				//4.右上至左下
				if ((!(i - 1 < 0) || !(i - 2 < 0) || !(i - 3 < 0) || !(i - 4 < 0) || !(i - 5 < 0))
						&& (!(j + 1 > ROWS - 1) || !(j + 2 > ROWS - 1) || !(j + 3 > ROWS - 1) || !(j + 4 > ROWS - 1) || !(j + 5 > ROWS - 1))) {
					if (board[i - 1][j + 1] == aiColor && board[i - 2][j + 2] == aiColor
							&& board[i - 3][j + 3] == aiColor && board[i - 4][j + 4] == aiColor && board[i - 5][j + 5] == null) {
						weight[i][j] += PRIORITY_6;
					}
				}
				
				// 活四_●●●●_右边那颗
				// 1.横向
				if (!(i - 1 < 0) || !(i - 2 < 0) || !(i - 3 < 0) || !(i - 4 < 0) || !(i - 5 < 0)) {
					if (board[i - 1][j] == aiColor && board[i - 2][j] == aiColor && board[i - 3][j] == aiColor
							&& board[i - 4][j] == aiColor && board[i - 5][j] == null) {
						weight[i][j] += PRIORITY_6;
					}
				}
				//2.纵向
				if (!(j - 1 < 0) || !(j - 2 < 0) || !(j - 3 < 0) || !(j - 4 < 0) || !(j - 5 < 0)) {
					if (board[i][j - 1] == aiColor && board[i][j - 2] == aiColor && board[i][j - 3] == aiColor
							&& board[i][j - 4] == aiColor && board[i][j - 5] == null) {
						weight[i][j] += PRIORITY_6;
					}
				}
				//3.右下至左上
				if ((!(i - 1 < 0) || !(i - 2 < 0) || !(i - 3 < 0) || !(i - 4 < 0) || !(i - 5 < 0))
						&& (!(j - 1 < 0) || !(j - 2 < 0) || !(j - 3 < 0) || !(j - 4 < 0) || !(j - 5 < 0))) {
					if (board[i - 1][j - 1] == aiColor && board[i - 2][j - 2] == aiColor
							&& board[i - 3][j - 3] == aiColor && board[i - 4][j - 4] == aiColor && board[i - 5][j - 5] == null) {
						weight[i][j] += PRIORITY_6;
					}
				}
				//4.左下至右上
				if ((!(i + 1 > COLS - 1) || !(i + 2 > COLS - 1) || !(i + 3 > COLS - 1) || !(i + 4 > COLS - 1) || !(i + 5 > COLS - 1))
						&& (!(j - 1 < 0) || !(j - 2 < 0) || !(j - 3 < 0) || !(j - 4 < 0) || !(j - 5 < 0))) {
					if (board[i + 1][j - 1] == aiColor && board[i + 2][j - 2] == aiColor
							&& board[i + 3][j - 3] == aiColor && board[i + 4][j - 4] == aiColor && board[i + 5][j - 5] == null) {
						weight[i][j] += PRIORITY_6;
					}
				}
				
				//冲四_●●●●○
				
			}
		}
	}

}
