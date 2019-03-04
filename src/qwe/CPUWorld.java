package qwe;

public class CPUWorld extends World {
	private static final long serialVersionUID = 1L;

	private static final int PRIORITY_6 = 6;  //活四得分
	private static final int PRIORITY_5 = 5;  //冲四得分
	private static final int PRIORITY_4 = 4;  //活三得分
	private static final int PRIORITY_3 = 3;  //活二得分
	private static final int PRIORITY_2 = 2;  //眠三得分
	private static final int PRIORITY_1 = 1;  //眠二得分

	

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
				
				Piece anotherColor = (aiColor == Piece.B) ? Piece.W : Piece.B;
				//冲四_●●●●○
				// 1.横向
				if (!(i + 1 > COLS - 1) || !(i + 2 > COLS - 1) || !(i + 3 > COLS - 1) || !(i + 4 > COLS - 1) || !(i + 5 > COLS - 1)) {
					if (board[i + 1][j] == aiColor && board[i + 2][j] == aiColor && board[i + 3][j] == aiColor
							&& board[i + 4][j] == aiColor && board[i + 5][j] == anotherColor) {
						weight[i][j] += PRIORITY_5;
					}
				}
				//2.纵向
				if (!(j + 1 > ROWS - 1) || !(j + 2 > ROWS - 1) || !(j + 3 > ROWS - 1) || !(j + 4 > ROWS - 1) || !(j + 5 > ROWS - 1)) {
					if (board[i][j + 1] == aiColor && board[i][j + 2] == aiColor && board[i][j + 3] == aiColor
							&& board[i][j + 4] == aiColor && board[i][j + 5] == anotherColor) {
						weight[i][j] += PRIORITY_5;
					}
				}
				//3.左上至右下
				if ((!(i + 1 > COLS - 1) || !(i + 2 > COLS - 1) || !(i + 3 > COLS - 1) || !(i + 4 > COLS - 1) || !(i + 5 > COLS - 1))
						&& (!(j + 1 > ROWS - 1) || !(j + 2 > ROWS - 1) || !(j + 3 > ROWS - 1) || !(j + 4 > ROWS - 1) || !(j + 5 > ROWS - 1))) {
					if (board[i + 1][j + 1] == aiColor && board[i + 2][j + 2] == aiColor
							&& board[i + 3][j + 3] == aiColor && board[i + 4][j + 4] == aiColor && board[i + 5][j + 5] == anotherColor) {
						weight[i][j] += PRIORITY_5;
					}
				}
				//4.右上至左下
				if ((!(i - 1 < 0) || !(i - 2 < 0) || !(i - 3 < 0) || !(i - 4 < 0) || !(i - 5 < 0))
						&& (!(j + 1 > ROWS - 1) || !(j + 2 > ROWS - 1) || !(j + 3 > ROWS - 1) || !(j + 4 > ROWS - 1) || !(j + 5 > ROWS - 1))) {
					if (board[i - 1][j + 1] == aiColor && board[i - 2][j + 2] == aiColor
							&& board[i - 3][j + 3] == aiColor && board[i - 4][j + 4] == aiColor && board[i - 5][j + 5] == anotherColor) {
						weight[i][j] += PRIORITY_5;
					}
				}
				
				//冲四○●●●●_
				// 1.横向
				if (!(i - 1 < 0) || !(i - 2 < 0) || !(i - 3 < 0) || !(i - 4 < 0) || !(i - 5 < 0)) {
					if (board[i - 1][j] == aiColor && board[i - 2][j] == aiColor && board[i - 3][j] == aiColor
							&& board[i - 4][j] == aiColor && board[i - 5][j] == anotherColor) {
						weight[i][j] += PRIORITY_5;
					}
				}
				//2.纵向
				if (!(j - 1 < 0) || !(j - 2 < 0) || !(j - 3 < 0) || !(j - 4 < 0) || !(j - 5 < 0)) {
					if (board[i][j - 1] == aiColor && board[i][j - 2] == aiColor && board[i][j - 3] == aiColor
							&& board[i][j - 4] == aiColor && board[i][j - 5] == anotherColor) {
						weight[i][j] += PRIORITY_5;
					}
				}
				//3.右下至左上
				if ((!(i - 1 < 0) || !(i - 2 < 0) || !(i - 3 < 0) || !(i - 4 < 0) || !(i - 5 < 0))
						&& (!(j - 1 < 0) || !(j - 2 < 0) || !(j - 3 < 0) || !(j - 4 < 0) || !(j - 5 < 0))) {
					if (board[i - 1][j - 1] == aiColor && board[i - 2][j - 2] == aiColor
							&& board[i - 3][j - 3] == aiColor && board[i - 4][j - 4] == aiColor && board[i - 5][j - 5] == anotherColor) {
						weight[i][j] += PRIORITY_5;
					}
				}
				//4.左下至右上
				if ((!(i + 1 > COLS - 1) || !(i + 2 > COLS - 1) || !(i + 3 > COLS - 1) || !(i + 4 > COLS - 1) || !(i + 5 > COLS - 1))
						&& (!(j - 1 < 0) || !(j - 2 < 0) || !(j - 3 < 0) || !(j - 4 < 0) || !(j - 5 < 0))) {
					if (board[i + 1][j - 1] == aiColor && board[i + 2][j - 2] == aiColor
							&& board[i + 3][j - 3] == aiColor && board[i + 4][j - 4] == aiColor && board[i + 5][j - 5] == anotherColor) {
						weight[i][j] += PRIORITY_5;
					}
				}
				
				//冲四●_●●●
				// 1.横向
				if (!(i - 1 < 0) || !(i + 1 > COLS - 1) || !(i + 2 > COLS - 1) || !(i + 3 > COLS - 1)) {
					if (board[i - 1][j] == aiColor && board[i + 1][j] == aiColor && board[i + 2][j] == aiColor && board[i + 3][j] == aiColor) {
						weight[i][j] += PRIORITY_5;
					}
				}
				//2.纵向
				if (!(j - 1 < 0) || !(j + 1 > ROWS - 1) || !(j + 2 > ROWS - 1) || !(j + 3 > ROWS - 1)) {
					if (board[i][j - 1] == aiColor && board[i][j + 1] == aiColor && board[i][j + 2] == aiColor && board[i][j + 3] == aiColor) {
						weight[i][j] += PRIORITY_5;
					}
				}
				//3.左上至右下
				if ((!(i - 1 < 0) || !(i + 1 > COLS - 1) || !(i + 2 > COLS - 1) || !(i + 3 > COLS - 1))
						&& (!(j - 1 < 0) || !(j + 1 > ROWS - 1) || !(j + 2 > ROWS - 1) || !(j + 3 > ROWS - 1))) {
					if (board[i - 1][j - 1] == aiColor && board[i + 1][j + 1] == aiColor
							&& board[i + 2][j + 2] == aiColor && board[i + 3][j + 3] == aiColor) {
						weight[i][j] += PRIORITY_5;
					}
				}
				//4.右上至左下
				if ((!(i + 1 > COLS - 1) || !(i - 1 < 0) || !(i - 2 < 0) || !(i - 3 < 0))
						&& (!(j - 1 < 0) || !(j + 1 > ROWS - 1) || !(j + 2 > ROWS - 1) || !(j + 3 > ROWS - 1))) {
					if (board[i + 1][j - 1] == aiColor && board[i - 1][j + 1] == aiColor
							&& board[i - 2][j + 2] == aiColor && board[i - 3][j + 3] == aiColor) {
						weight[i][j] += PRIORITY_5;
					}
				}
				
				//冲四●●●_●
				// 1.横向
				if (!(i + 1 > COLS - 1) || !(i - 1 < 0) || !(i - 2 < 0) || !(i - 3 < 0)) {
					if (board[i + 1][j] == aiColor && board[i - 1][j] == aiColor && board[i - 2][j] == aiColor && board[i - 3][j] == aiColor) {
						weight[i][j] += PRIORITY_5;
					}
				}
				//2.纵向
				if (!(j + 1 > ROWS - 1) || !(j - 1 < 0) || !(j - 2 < 0) || !(j - 3 < 0)) {
					if (board[i][j + 1] == aiColor && board[i][j - 1] == aiColor && board[i][j - 2] == aiColor && board[i][j - 3] == aiColor) {
						weight[i][j] += PRIORITY_5;
					}
				}
				//3.右下至左上
				if ((!(i + 1 > COLS - 1) || !(i - 1 < 0) || !(i - 2 < 0) || !(i - 3 < 0))
						&& (!(j + 1 > ROWS - 1) || !(j - 1 < 0) || !(j - 2 < 0) || !(j - 3 < 0))) {
					if (board[i + 1][j + 1] == aiColor && board[i - 1][j - 1] == aiColor
							&& board[i - 2][j - 2] == aiColor && board[i - 3][j - 3] == aiColor) {
						weight[i][j] += PRIORITY_5;
					}
				}
				//4.左下至右上
				if ((!(i - 1 < 0) || !(i + 1 > COLS - 1) || !(i + 2 > COLS - 1) || !(i + 3 > COLS - 1))
						&& (!(j + 1 > ROWS - 1) || !(j - 1 < 0) || !(j - 2 < 0) || !(j - 3 < 0))) {
					if (board[i - 1][j + 1] == aiColor && board[i + 1][j - 1] == aiColor
							&& board[i + 2][j - 2] == aiColor && board[i + 3][j - 3] == aiColor) {
						weight[i][j] += PRIORITY_5;
					}
				}
				
				//冲四●●_●●
				// 1.横向
				if (!(i - 2 < 0) || !(i - 1 < 0) || !(i + 1 > COLS - 1) || !(i + 2 > COLS - 1)) {
					if (board[i - 2][j] == aiColor && board[i - 1][j] == aiColor && board[i + 1][j] == aiColor && board[i + 2][j] == aiColor) {
						weight[i][j] += PRIORITY_5;
					}
				}
				//2.纵向
				if (!(j - 2 < 0) || !(j - 1 < 0) || !(j + 1 > ROWS - 1) || !(j + 2 > ROWS - 1)) {
					if (board[i][j - 2] == aiColor && board[i][j - 1] == aiColor && board[i][j + 1] == aiColor && board[i][j + 2] == aiColor) {
						weight[i][j] += PRIORITY_5;
					}
				}
				//3.左上至右下
				if ((!(i - 2 < 0) || !(i - 1 < 0) || !(i + 1 > COLS - 1) || !(i + 2 > COLS - 1))
						&& (!(j - 2 < 0) || !(j - 1 < 0) || !(j + 1 > ROWS - 1) || !(j + 2 > ROWS - 1))) {
					if (board[i - 2][j - 2] == aiColor && board[i - 1][j - 1] == aiColor
							&& board[i + 1][j + 1] == aiColor && board[i + 2][j + 2] == aiColor) {
						weight[i][j] += PRIORITY_5;
					}
				}
				//4.右上至左下
				if ((!(i + 2 > COLS - 1) || !(i + 1 > COLS - 1) || !(i - 1 < 0) || !(i - 2 < 0))
						&& (!(j - 2 < 0) || !(j - 1 < 0) || !(j + 1 > ROWS - 1) || !(j + 2 > ROWS - 1))) {
					if (board[i + 2][j - 2] == aiColor && board[i + 1][j - 1] == aiColor
							&& board[i - 1][j + 1] == aiColor && board[i - 2][j + 2] == aiColor) {
						weight[i][j] += PRIORITY_5;
					}
				}
				
				// 活三_●●●_左边那颗
				// 1.横向
				if (!(i + 1 > COLS - 1) || !(i + 2 > COLS - 1) || !(i + 3 > COLS - 1) || !(i + 4 > COLS - 1)) {
					if (board[i + 1][j] == aiColor && board[i + 2][j] == aiColor && board[i + 3][j] == aiColor
							&& board[i + 4][j] == null) {
						weight[i][j] += PRIORITY_4;
					}
				}
				//2.纵向
				if (!(j + 1 > ROWS - 1) || !(j + 2 > ROWS - 1) || !(j + 3 > ROWS - 1) || !(j + 4 > ROWS - 1)) {
					if (board[i][j + 1] == aiColor && board[i][j + 2] == aiColor
							&& board[i][j + 3] == aiColor && board[i][j + 4] == null) {
						weight[i][j] += PRIORITY_4;
					}
				}
				//3.左上至右下
				if ((!(i + 1 > COLS - 1) || !(i + 2 > COLS - 1) || !(i + 3 > COLS - 1) || !(i + 4 > COLS - 1))
						&& (!(j + 1 > ROWS - 1) || !(j + 2 > ROWS - 1) || !(j + 3 > ROWS - 1) || !(j + 4 > ROWS - 1))) {
					if (board[i + 1][j + 1] == aiColor && board[i + 2][j + 2] == aiColor
							&& board[i + 3][j + 3] == aiColor && board[i + 4][j + 4] == null) {
						weight[i][j] += PRIORITY_4;
					}
				}
				//4.右上至左下
				if ((!(i - 1 < 0) || !(i - 2 < 0) || !(i - 3 < 0) || !(i - 4 < 0))
						&& (!(j + 1 > ROWS - 1) || !(j + 2 > ROWS - 1) || !(j + 3 > ROWS - 1) || !(j + 4 > ROWS - 1))) {
					if (board[i - 1][j + 1] == aiColor && board[i - 2][j + 2] == aiColor
							&& board[i - 3][j + 3] == aiColor && board[i - 4][j + 4] == null) {
						weight[i][j] += PRIORITY_4;
					}
				}
				
				// 活三_●●●_右边那颗
				// 1.横向
				if (!(i - 1 < 0) || !(i - 2 < 0) || !(i - 3 < 0) || !(i - 4 < 0)) {
					if (board[i - 1][j] == aiColor && board[i - 2][j] == aiColor && board[i - 3][j] == aiColor && board[i - 4][j] == null) {
						weight[i][j] += PRIORITY_4;
					}
				}
				//2.纵向
				if (!(j - 1 < 0) || !(j - 2 < 0) || !(j - 3 < 0) || !(j - 4 < 0)) {
					if (board[i][j - 1] == aiColor && board[i][j - 2] == aiColor && board[i][j - 3] == aiColor && board[i][j - 4] == null) {
						weight[i][j] += PRIORITY_4;
					}
				}
				//3.右下至左上
				if ((!(i - 1 < 0) || !(i - 2 < 0) || !(i - 3 < 0) || !(i - 4 < 0))
						&& (!(j - 1 < 0) || !(j - 2 < 0) || !(j - 3 < 0) || !(j - 4 < 0))) {
					if (board[i - 1][j - 1] == aiColor && board[i - 2][j - 2] == aiColor
							&& board[i - 3][j - 3] == aiColor && board[i - 4][j - 4] == null) {
						weight[i][j] += PRIORITY_4;
					}
				}
				//4.左下至右上
				if ((!(i + 1 > COLS - 1) || !(i + 2 > COLS - 1) || !(i + 3 > COLS - 1) || !(i + 4 > COLS - 1))
						&& (!(j - 1 < 0) || !(j - 2 < 0) || !(j - 3 < 0) || !(j - 4 < 0))) {
					if (board[i + 1][j - 1] == aiColor && board[i + 2][j - 2] == aiColor
							&& board[i + 3][j - 3] == aiColor && board[i + 4][j - 4] == null) {
						weight[i][j] += PRIORITY_4;
					}
				}
				
				// 活三_●_●●_左边那颗
				// 1.横向
				if (!(i + 1 > COLS - 1) || !(i + 2 > COLS - 1) || !(i + 3 > COLS - 1) || !(i + 4 > COLS - 1) || !(i + 5 > COLS - 1)) {
					if (board[i + 1][j] == aiColor && board[i + 2][j] == null && board[i + 3][j] == aiColor
							&& board[i + 4][j] == aiColor && board[i + 5][j] == null) {
						weight[i][j] += PRIORITY_4;
					}
				}
				//2.纵向
				if (!(j + 1 > ROWS - 1) || !(j + 2 > ROWS - 1) || !(j + 3 > ROWS - 1) || !(j + 4 > ROWS - 1) || !(j + 5 > ROWS - 1)) {
					if (board[i][j + 1] == aiColor && board[i][j + 2] == null && board[i][j + 3] == aiColor
							&& board[i][j + 4] == aiColor && board[i][j + 5] == null) {
						weight[i][j] += PRIORITY_4;
					}
				}
				//3.左上至右下
				if ((!(i + 1 > COLS - 1) || !(i + 2 > COLS - 1) || !(i + 3 > COLS - 1) || !(i + 4 > COLS - 1) || !(i + 5 > COLS - 1))
						&& (!(j + 1 > ROWS - 1) || !(j + 2 > ROWS - 1) || !(j + 3 > ROWS - 1) || !(j + 4 > ROWS - 1) || !(j + 5 > ROWS - 1))) {
					if (board[i + 1][j + 1] == aiColor && board[i + 2][j + 2] == null && board[i + 3][j + 3] == aiColor
							&& board[i + 4][j + 4] == aiColor && board[i + 5][j + 5] == null) {
						weight[i][j] += PRIORITY_4;
					}
				}
				//4.右上至左下
				if ((!(i - 1 < 0) || !(i - 2 < 0) || !(i - 3 < 0) || !(i - 4 < 0) || !(i - 5 < 0))
						&& (!(j + 1 > ROWS - 1) || !(j + 2 > ROWS - 1) || !(j + 3 > ROWS - 1) || !(j + 4 > ROWS - 1) || !(j + 5 > ROWS - 1))) {
					if (board[i - 1][j + 1] == aiColor && board[i - 2][j + 2] == null
							&& board[i - 3][j + 3] == aiColor && board[i - 4][j + 4] == aiColor && board[i - 5][j + 5] == null) {
						weight[i][j] += PRIORITY_4;
					}
				}
				
				// 活三_●_●●_中间那颗
				// 1.横向
				if (!(i - 2 < 0) || !(i - 1 < 0) || !(i + 1 > COLS - 1) || !(i + 2 > COLS - 1) || !(i + 3 > COLS - 1)) {
					if (board[i - 2][j] == null && board[i - 1][j] == aiColor && board[i + 1][j] == aiColor
							&& board[i + 2][j] == aiColor && board[i + 3][j] == null) {
						weight[i][j] += PRIORITY_4;
					}
				}
				//2.纵向
				if (!(j - 2 < 0) || !(j - 1 < 0) || !(j + 1 > ROWS - 1) || !(j + 2 > ROWS - 1) || !(j + 3 > ROWS - 1)) {
					if (board[i][j - 2] == null && board[i][j - 1] == aiColor && board[i][j + 1] == aiColor
							&& board[i][j + 2] == aiColor && board[i][j + 3] == null) {
						weight[i][j] += PRIORITY_4;
					}
				}
				//3.左上至右下
				if ((!(i - 2 < 0) || !(i - 1 < 0) || !(i + 1 > COLS - 1) || !(i + 2 > COLS - 1) || !(i + 3 > COLS - 1))
						&& (!(j - 2 < 0) || !(j - 1 < 0) || !(j + 1 > ROWS - 1) || !(j + 2 > ROWS - 1) || !(j + 3 > ROWS - 1))) {
					if (board[i - 2][j - 2] == null && board[i - 1][j - 1] == aiColor && board[i + 1][j + 1] == aiColor
							&& board[i + 2][j + 2] == aiColor && board[i + 3][j + 3] == null) {
						weight[i][j] += PRIORITY_4;
					}
				}
				//4.右上至左下
				if ((!(i + 2 > COLS - 1) || !(i + 1 > COLS - 1) || !(i - 1 < 0) || !(i - 2 < 0) || !(i - 3 < 0))
						&& (!(j - 2 < 0) || !(j - 1 < 0) || !(j + 1 > ROWS - 1) || !(j + 2 > ROWS - 1) || !(j + 3 > ROWS - 1))) {
					if (board[i + 2][j - 2] == null && board[i + 1][j - 1] == aiColor && board[i - 1][j + 1] == aiColor
							&& board[i - 2][j + 2] == aiColor && board[i - 3][j + 3] == null) {
						weight[i][j] += PRIORITY_4;
					}
				}
				
				// 活三_●_●●_右边那颗
				// 1.横向
				if (!(i - 1 < 0) || !(i - 2 < 0) || !(i - 3 < 0) || !(i - 4 < 0) || !(i - 5 < 0)) {
					if (board[i - 1][j] == aiColor && board[i - 2][j] == aiColor && board[i - 3][j] == null
							&& board[i - 4][j] == aiColor && board[i - 5][j] == null) {
						weight[i][j] += PRIORITY_4;
					}
				}
				//2.纵向
				if (!(j - 1 < 0) || !(j - 2 < 0) || !(j - 3 < 0) || !(j - 4 < 0) || !(j - 5 < 0)) {
					if (board[i][j - 1] == aiColor && board[i][j - 2] == aiColor && board[i][j - 3] == null
							&& board[i][j - 4] == aiColor && board[i][j - 5] == null) {
						weight[i][j] += PRIORITY_4;
					}
				}
				//3.右下至左上
				if ((!(i - 1 < 0) || !(i - 2 < 0) || !(i - 3 < 0) || !(i - 4 < 0) || !(i - 5 < 0))
						&& (!(j - 1 < 0) || !(j - 2 < 0) || !(j - 3 < 0) || !(j - 4 < 0) || !(j - 5 < 0))) {
					if (board[i - 1][j - 1] == aiColor && board[i - 2][j - 2] == aiColor
							&& board[i - 3][j - 3] == null && board[i - 4][j - 4] == aiColor && board[i - 5][j - 5] == null) {
						weight[i][j] += PRIORITY_4;
					}
				}
				//4.左下至右上
				if ((!(i + 1 > COLS - 1) || !(i + 2 > COLS - 1) || !(i + 3 > COLS - 1) || !(i + 4 > COLS - 1) || !(i + 5 > COLS - 1))
						&& (!(j - 1 < 0) || !(j - 2 < 0) || !(j - 3 < 0) || !(j - 4 < 0) || !(j - 5 < 0))) {
					if (board[i + 1][j - 1] == aiColor && board[i + 2][j - 2] == aiColor
							&& board[i + 3][j - 3] == null && board[i + 4][j - 4] == aiColor && board[i + 5][j - 5] == null) {
						weight[i][j] += PRIORITY_4;
					}
				}
				
				// 活三_●●_●_左边那颗
				// 1.横向
				if (!(i + 1 > COLS - 1) || !(i + 2 > COLS - 1) || !(i + 3 > COLS - 1) || !(i + 4 > COLS - 1) || !(i + 5 > COLS - 1)) {
					if (board[i + 1][j] == aiColor && board[i + 2][j] == aiColor && board[i + 3][j] == null
							&& board[i + 4][j] == aiColor && board[i + 5][j] == null) {
						weight[i][j] += PRIORITY_4;
					}
				}
				//2.纵向
				if (!(j + 1 > ROWS - 1) || !(j + 2 > ROWS - 1) || !(j + 3 > ROWS - 1) || !(j + 4 > ROWS - 1) || !(j + 5 > ROWS - 1)) {
					if (board[i][j + 1] == aiColor && board[i][j + 2] == aiColor && board[i][j + 3] == null
							&& board[i][j + 4] == aiColor && board[i][j + 5] == null) {
						weight[i][j] += PRIORITY_4;
					}
				}
				//3.左上至右下
				if ((!(i + 1 > COLS - 1) || !(i + 2 > COLS - 1) || !(i + 3 > COLS - 1) || !(i + 4 > COLS - 1) || !(i + 5 > COLS - 1))
						&& (!(j + 1 > ROWS - 1) || !(j + 2 > ROWS - 1) || !(j + 3 > ROWS - 1) || !(j + 4 > ROWS - 1) || !(j + 5 > ROWS - 1))) {
					if (board[i + 1][j + 1] == aiColor && board[i + 2][j + 2] == aiColor
							&& board[i + 3][j + 3] == null && board[i + 4][j + 4] == aiColor && board[i + 5][j + 5] == null) {
						weight[i][j] += PRIORITY_4;
					}
				}
				//4.右上至左下
				if ((!(i - 1 < 0) || !(i - 2 < 0) || !(i - 3 < 0) || !(i - 4 < 0) || !(i - 5 < 0))
						&& (!(j + 1 > ROWS - 1) || !(j + 2 > ROWS - 1) || !(j + 3 > ROWS - 1) || !(j + 4 > ROWS - 1) || !(j + 5 > ROWS - 1))) {
					if (board[i - 1][j + 1] == aiColor && board[i - 2][j + 2] == aiColor
							&& board[i - 3][j + 3] == null && board[i - 4][j + 4] == aiColor && board[i - 5][j + 5] == null) {
						weight[i][j] += PRIORITY_4;
					}
				}
				
				// 活三_●●_●_中间那颗
				// 1.横向
				if (!(i - 3 < 0) || !(i - 2 < 0) || !(i - 1 < 0) || !(i + 1 > COLS - 1) || !(i + 2 > COLS - 1)) {
					if (board[i - 3][j] == null && board[i - 2][j] == aiColor && board[i - 1][j] == aiColor
							&& board[i + 1][j] == aiColor && board[i + 2][j] == null) {
						weight[i][j] += PRIORITY_4;
					}
				}
				//2.纵向
				if (!(j - 3 < 0) || !(j - 2 < 0) || !(j - 1 < 0) || !(j + 1 > ROWS - 1) || !(j + 2 > ROWS - 1)) {
					if (board[i][j - 3] == null && board[i][j - 2] == aiColor && board[i][j - 1] == aiColor
							&& board[i][j + 1] == aiColor && board[i][j + 2] == null) {
						weight[i][j] += PRIORITY_4;
					}
				}
				//3.左上至右下
				if ((!(i - 3 < 0) || !(i - 2 < 0) || !(i - 1 < 0) || !(i + 1 > COLS - 1) || !(i + 2 > COLS - 1))
						&& (!(j - 3 < 0) || !(j - 2 < 0) || !(j - 1 < 0) || !(j + 1 > ROWS - 1) || !(j + 2 > ROWS - 1))) {
					if (board[i - 3][j - 3] == null && board[i - 2][j - 2] == aiColor && board[i - 1][j - 1] == aiColor
							&& board[i + 1][j + 1] == aiColor && board[i + 2][j + 2] == null) {
						weight[i][j] += PRIORITY_4;
					}
				}
				//4.右上至左下
				if ((!(i + 3 > COLS - 1) || !(i + 2 > COLS - 1) || !(i + 1 > COLS - 1) || !(i - 1 < 0) || !(i - 2 < 0))
						&& (!(j - 3 < 0) || !(j - 2 < 0) || !(j - 1 < 0) || !(j + 1 > ROWS - 1) || !(j + 2 > ROWS - 1))) {
					if (board[i + 3][j - 3] == null && board[i + 2][j - 2] == aiColor && board[i + 1][j - 1] == aiColor
							&& board[i - 1][j + 1] == aiColor && board[i - 2][j + 2] == null) {
						weight[i][j] += PRIORITY_4;
					}
				}
				
				// 活三_●●_●_右边那颗
				// 1.横向
				if (!(i - 1 < 0) || !(i - 2 < 0) || !(i - 3 < 0) || !(i - 4 < 0) || !(i - 5 < 0)) {
					if (board[i - 1][j] == aiColor && board[i - 2][j] == null && board[i - 3][j] == aiColor
							&& board[i - 4][j] == aiColor && board[i - 5][j] == null) {
						weight[i][j] += PRIORITY_4;
					}
				}
				//2.纵向
				if (!(j - 1 < 0) || !(j - 2 < 0) || !(j - 3 < 0) || !(j - 4 < 0) || !(j - 5 < 0)) {
					if (board[i][j - 1] == aiColor && board[i][j - 2] == null && board[i][j - 3] == aiColor
							&& board[i][j - 4] == aiColor && board[i][j - 5] == null) {
						weight[i][j] += PRIORITY_4;
					}
				}
				//3.右下至左上
				if ((!(i - 1 < 0) || !(i - 2 < 0) || !(i - 3 < 0) || !(i - 4 < 0) || !(i - 5 < 0))
						&& (!(j - 1 < 0) || !(j - 2 < 0) || !(j - 3 < 0) || !(j - 4 < 0) || !(j - 5 < 0))) {
					if (board[i - 1][j - 1] == aiColor && board[i - 2][j - 2] == null && board[i - 3][j - 3] == aiColor
							&& board[i - 4][j - 4] == aiColor && board[i - 5][j - 5] == null) {
						weight[i][j] += PRIORITY_4;
					}
				}
				//4.左下至右上
				if ((!(i + 1 > COLS - 1) || !(i + 2 > COLS - 1) || !(i + 3 > COLS - 1) || !(i + 4 > COLS - 1) || !(i + 5 > COLS - 1))
						&& (!(j - 1 < 0) || !(j - 2 < 0) || !(j - 3 < 0) || !(j - 4 < 0) || !(j - 5 < 0))) {
					if (board[i + 1][j - 1] == aiColor && board[i + 2][j - 2] == null && board[i + 3][j - 3] == aiColor
							&& board[i + 4][j - 4] == aiColor && board[i + 5][j - 5] == null) {
						weight[i][j] += PRIORITY_4;
					}
				}
				
				// 活二__●●__最左边那颗
				// 1.横向
				if (!(i + 1 > COLS - 1) || !(i + 2 > COLS - 1) || !(i + 3 > COLS - 1) || !(i + 4 > COLS - 1) || !(i + 5 > COLS - 1)) {
					if (board[i + 1][j] == null && board[i + 2][j] == aiColor && board[i + 3][j] == aiColor
							&& board[i + 4][j] == null && board[i + 5][j] == null) {
						weight[i][j] += PRIORITY_3;
					}
				}
				//2.纵向
				if (!(j + 1 > ROWS - 1) || !(j + 2 > ROWS - 1) || !(j + 3 > ROWS - 1) || !(j + 4 > ROWS - 1) || !(j + 5 > ROWS - 1)) {
					if (board[i][j + 1] == null && board[i][j + 2] == aiColor && board[i][j + 3] == aiColor
							&& board[i][j + 4] == null && board[i][j + 5] == null) {
						weight[i][j] += PRIORITY_3;
					}
				}
				//3.左上至右下
				if ((!(i + 1 > COLS - 1) || !(i + 2 > COLS - 1) || !(i + 3 > COLS - 1) || !(i + 4 > COLS - 1) || !(i + 5 > COLS - 1))
						&& (!(j + 1 > ROWS - 1) || !(j + 2 > ROWS - 1) || !(j + 3 > ROWS - 1) || !(j + 4 > ROWS - 1) || !(j + 5 > ROWS - 1))) {
					if (board[i + 1][j + 1] == null && board[i + 2][j + 2] == aiColor
							&& board[i + 3][j + 3] == aiColor && board[i + 4][j + 4] == null && board[i + 5][j + 5] == null) {
						weight[i][j] += PRIORITY_3;
					}
				}
				//4.右上至左下
				if ((!(i - 1 < 0) || !(i - 2 < 0) || !(i - 3 < 0) || !(i - 4 < 0) || !(i - 5 < 0))
						&& (!(j + 1 > ROWS - 1) || !(j + 2 > ROWS - 1) || !(j + 3 > ROWS - 1) || !(j + 4 > ROWS - 1) || !(j + 5 > ROWS - 1))) {
					if (board[i - 1][j + 1] == null && board[i - 2][j + 2] == aiColor
							&& board[i - 3][j + 3] == aiColor && board[i - 4][j + 4] == null && board[i - 5][j + 5] == null) {
						weight[i][j] += PRIORITY_3;
					}
				}
				
				// 活二__●●__左二那颗
				// 1.横向
				if (!(i - 1 < 0) || !(i + 1 > COLS - 1) || !(i + 2 > COLS - 1) || !(i + 3 > COLS - 1) || !(i + 4 > COLS - 1)) {
					if (board[i - 1][j] == null && board[i + 1][j] == aiColor && board[i + 2][j] == aiColor
							&& board[i + 3][j] == null && board[i + 4][j] == null) {
						weight[i][j] += PRIORITY_3;
					}
				}
				//2.纵向
				if (!(j - 1 < 0) || !(j + 1 > ROWS - 1) || !(j + 2 > ROWS - 1) || !(j + 3 > ROWS - 1) || !(j + 4 > ROWS - 1)) {
					if (board[i][j - 1] == null && board[i][j + 1] == aiColor
							&& board[i][j + 2] == aiColor && board[i][j + 3] == null && board[i][j + 4] == null) {
						weight[i][j] += PRIORITY_3;
					}
				}
				//3.左上至右下
				if ((!(i - 1 < 0) || !(i + 1 > COLS - 1) || !(i + 2 > COLS - 1) || !(i + 3 > COLS - 1) || !(i + 4 > COLS - 1))
						&& (!(j - 1 < 0) || !(j + 1 > ROWS - 1) || !(j + 2 > ROWS - 1) || !(j + 3 > ROWS - 1) || !(j + 4 > ROWS - 1))) {
					if (board[i - 1][j - 1] == null && board[i + 1][j + 1] == aiColor
							&& board[i + 2][j + 2] == aiColor && board[i + 3][j + 3] == null && board[i + 4][j + 4] == null) {
						weight[i][j] += PRIORITY_3;
					}
				}
				//4.右上至左下
				if ((!(i + 1 < COLS - 1) || !(i - 1 < 0) || !(i - 2 < 0) || !(i - 3 < 0) || !(i - 4 < 0))
						&& (!(j - 1 < 0) || !(j + 1 > ROWS - 1) || !(j + 2 > ROWS - 1) || !(j + 3 > ROWS - 1) || !(j + 4 > ROWS - 1))) {
					if (board[i + 1][j - 1] == null && board[i - 1][j + 1] == aiColor
							&& board[i - 2][j + 2] == aiColor && board[i - 3][j + 3] == null && board[i - 4][j + 4] == null) {
						weight[i][j] += PRIORITY_3;
					}
				}
				
				// 活二__●●__右二那颗
				// 1.横向
				if (!(i - 4 < 0) || !(i - 3 < 0) || !(i - 2 < 0) || !(i - 1 < 0) || !(i + 1 > COLS - 1)) {
					if (board[i - 4][j] == null && board[i - 3][j] == null && board[i - 2][j] == aiColor
							&& board[i - 1][j] == aiColor && board[i + 1][j] == null) {
						weight[i][j] += PRIORITY_3;
					}
				}
				//2.纵向
				if (!(j - 4 < 0) || !(j - 3 < 0) || !(j - 2 < 0) || !(j - 1 < 0) || !(j + 1 > ROWS - 1)) {
					if (board[i][j - 4] == null && board[i][j - 3] == null && board[i][j - 2] == aiColor
							&& board[i][j - 1] == aiColor && board[i][j + 1] == null) {
						weight[i][j] += PRIORITY_3;
					}
				}
				//3.左上至右下
				if ((!(i - 4 < 0) || !(i - 3 < 0) || !(i - 2 < 0) || !(i - 1 < 0) || !(i + 1 > COLS - 1))
						&& (!(j - 4 < 0) || !(j - 3 < 0) || !(j - 2 < 0) || !(j - 1 < 0) || !(j + 1 > ROWS - 1))) {
					if (board[i - 4][j - 4] == null && board[i - 3][j - 3] == null
							&& board[i - 2][j - 2] == aiColor && board[i - 1][j - 1] == aiColor && board[i + 1][j + 1] == null) {
						weight[i][j] += PRIORITY_3;
					}
				}
				//4.右上至左下
				if ((!(i + 4 < COLS - 1) || !(i + 3 < COLS - 1) || !(i + 2 < COLS - 1) || !(i + 1 < COLS - 1) || !(i - 1 < 0))
						&& (!(j - 4 < 0) || !(j - 3 < 0) || !(j - 2 < 0) || !(j - 1 < 0) || !(j + 1 > ROWS - 1))) {
					if (board[i + 4][j - 4] == null && board[i + 3][j - 3] == null
							&& board[i + 2][j - 2] == aiColor && board[i + 1][j - 1] == aiColor && board[i - 1][j + 1] == null) {
						weight[i][j] += PRIORITY_3;
					}
				}
				
				// 活二__●●__最右边那颗
				// 1.横向
				if (!(i - 1 < 0) || !(i - 2 < 0) || !(i - 3 < 0) || !(i - 4 < 0) || !(i - 5 < 0)) {
					if (board[i - 1][j] == null && board[i - 2][j] == aiColor && board[i - 3][j] == aiColor
							&& board[i - 4][j] == null && board[i - 5][j] == null) {
						weight[i][j] += PRIORITY_3;
					}
				}
				//2.纵向
				if (!(j - 1 < 0) || !(j - 2 < 0) || !(j - 3 < 0) || !(j - 4 < 0) || !(j - 5 < 0)) {
					if (board[i][j - 1] == null && board[i][j - 2] == aiColor
							&& board[i][j - 3] == aiColor && board[i][j - 4] == null && board[i][j - 5] == null) {
						weight[i][j] += PRIORITY_3;
					}
				}
				//3.左上至右下
				if ((!(i - 1 < 0) || !(i - 2 < 0) || !(i - 3 < 0) || !(i - 4 < 0) || !(i - 5 < 0))
						&& (!(j - 1 < 0) || !(j - 2 < 0) || !(j - 3 < 0) || !(j - 4 < 0) || !(j - 5 < 0))) {
					if (board[i - 1][j - 1] == null && board[i - 2][j - 2] == aiColor
							&& board[i - 3][j - 3] == aiColor && board[i - 4][j - 4] == null && board[i - 5][j - 5] == null) {
						weight[i][j] += PRIORITY_3;
					}
				}
				//4.右上至左下
				if ((!(i + 1 > COLS - 1) || !(i + 2  > COLS - 1) || !(i + 3  > COLS - 1) || !(i + 4  > COLS - 1) || !(i + 5  > COLS - 1))
						&& (!(j - 1 < 0) || !(j - 2 < 0) || !(j - 3 < 0) || !(j - 4 < 0) || !(j - 5 < 0))) {
					if (board[i + 1][j - 1] == null && board[i + 2][j - 2] == aiColor
							&& board[i + 3][j - 3] == aiColor && board[i + 4][j - 4] == null && board[i + 5][j - 5] == null) {
						weight[i][j] += PRIORITY_3;
					}
				}
				
				// 活二_●_●_最左边那颗
				// 1.横向
				if (!(i + 1 > COLS - 1) || !(i + 2 > COLS - 1) || !(i + 3 > COLS - 1) || !(i + 4 > COLS - 1)) {
					if (board[i + 1][j] == aiColor && board[i + 2][j] == null && board[i + 3][j] == aiColor && board[i + 4][j] == null) {
						weight[i][j] += PRIORITY_3;
					}
				}
				//2.纵向
				if (!(j + 1 > ROWS - 1) || !(j + 2 > ROWS - 1) || !(j + 3 > ROWS - 1) || !(j + 4 > ROWS - 1)) {
					if (board[i][j + 1] == aiColor && board[i][j + 2] == null && board[i][j + 3] == aiColor && board[i][j + 4] == null) {
						weight[i][j] += PRIORITY_3;
					}
				}
				//3.左上至右下
				if ((!(i + 1 > COLS - 1) || !(i + 2 > COLS - 1) || !(i + 3 > COLS - 1) || !(i + 4 > COLS - 1))
						&& (!(j + 1 > ROWS - 1) || !(j + 2 > ROWS - 1) || !(j + 3 > ROWS - 1) || !(j + 4 > ROWS - 1))) {
					if (board[i + 1][j + 1] == aiColor && board[i + 2][j + 2] == null
							&& board[i + 3][j + 3] == aiColor && board[i + 4][j + 4] == null) {
						weight[i][j] += PRIORITY_3;
					}
				}
				//4.右上至左下
				if ((!(i - 1 < 0) || !(i - 2 < 0) || !(i - 3 < 0) || !(i - 4 < 0))
						&& (!(j + 1 > ROWS - 1) || !(j + 2 > ROWS - 1) || !(j + 3 > ROWS - 1) || !(j + 4 > ROWS - 1))) {
					if (board[i - 1][j + 1] == aiColor && board[i - 2][j + 2] == null
							&& board[i - 3][j + 3] == aiColor && board[i - 4][j + 4] == null) {
						weight[i][j] += PRIORITY_3;
					}
				}
				
				// 活二_●_●_中间那颗
				// 1.横向
				if (!(i - 2 < 0) || !(i - 1 < 0) || !(i + 1 > COLS - 1) || !(i + 2 > COLS - 1)) {
					if (board[i - 2][j] == null && board[i - 1][j] == aiColor && board[i + 1][j] == aiColor && board[i + 2][j] == null) {
						weight[i][j] += PRIORITY_3;
					}
				}
				//2.纵向
				if (!(j - 2 < 0) || !(j - 1 < 0) || !(j + 1 > ROWS - 1) || !(j + 2 > ROWS - 1)) {
					if (board[i][j - 2] == null && board[i][j - 1] == aiColor && board[i][j + 1] == aiColor && board[i][j + 2] == null) {
						weight[i][j] += PRIORITY_3;
					}
				}
				//3.左上至右下
				if ((!(i - 2 < 0) || !(i - 1 < 0) || !(i + 1 > COLS - 1) || !(i + 2 > COLS - 1))
						&& (!(j - 2 < 0) || !(j - 1 < 0) || !(j + 1 > ROWS - 1) || !(j + 2 > ROWS - 1))) {
					if (board[i - 2][j - 2] == null && board[i - 1][j - 1] == aiColor
							&& board[i + 1][j + 1] == aiColor && board[i + 2][j + 2] == null) {
						weight[i][j] += PRIORITY_3;
					}
				}
				//4.右上至左下
				if ((!(i + 2 < COLS - 1) || !(i + 1 < COLS - 1) || !(i - 1 < 0) || !(i - 2 < 0))
						&& (!(j - 2 < 0) || !(j - 1 < 0) || !(j + 1 > ROWS - 1) || !(j + 2 > ROWS - 1))) {
					if (board[i + 2][j - 2] == null && board[i + 1][j - 1] == aiColor
							&& board[i - 1][j + 1] == aiColor && board[i - 2][j + 2] == null) {
						weight[i][j] += PRIORITY_3;
					}
				}
				
				// 活二_●_●_最右边那颗
				// 1.横向
				if (!(i - 1 < 0) || !(i - 2 < 0) || !(i - 3 < 0) || !(i - 4 < 0)) {
					if (board[i - 1][j] == aiColor && board[i - 2][j] == null && board[i - 3][j] == aiColor && board[i - 4][j] == null) {
						weight[i][j] += PRIORITY_3;
					}
				}
				//2.纵向
				if (!(j - 1 < 0) || !(j - 2 < 0) || !(j - 3 < 0) || !(j - 4 < 0)) {
					if (board[i][j - 1] == aiColor && board[i][j - 2] == null && board[i][j - 3] == aiColor && board[i][j - 4] == null) {
						weight[i][j] += PRIORITY_3;
					}
				}
				//3.左上至右下
				if ((!(i - 1 < 0) || !(i - 2 < 0) || !(i - 3 < 0) || !(i - 4 < 0))
						&& (!(j - 1 < 0) || !(j - 2 < 0) || !(j - 3 < 0) || !(j - 4 < 0))) {
					if (board[i - 1][j - 1] == aiColor && board[i - 2][j - 2] == null
							&& board[i - 3][j - 3] == aiColor && board[i - 4][j - 4] == null) {
						weight[i][j] += PRIORITY_3;
					}
				}
				//4.右上至左下
				if ((!(i + 1 > COLS - 1) || !(i + 2 > COLS - 1) || !(i + 3 > COLS - 1) || !(i + 4 > COLS - 1))
						&& (!(j - 1 < 0) || !(j - 2 < 0) || !(j - 3 < 0) || !(j - 4 < 0))) {
					if (board[i + 1][j - 1] == aiColor && board[i + 2][j - 2] == null
							&& board[i + 3][j - 3] == aiColor && board[i + 4][j - 4] == null) {
						weight[i][j] += PRIORITY_3;
					}
				}
				
				// 活二_●__●_最左边那颗
				// 1.横向
				if (!(i + 1 > COLS - 1) || !(i + 2 > COLS - 1) || !(i + 3 > COLS - 1) || !(i + 4 > COLS - 1) || !(i + 5 > COLS - 1)) {
					if (board[i + 1][j] == aiColor && board[i + 2][j] == null && board[i + 3][j] == null
							&& board[i + 4][j] == aiColor && board[i + 5][j] == null) {
						weight[i][j] += PRIORITY_3;
					}
				}
				//2.纵向
				if (!(j + 1 > ROWS - 1) || !(j + 2 > ROWS - 1) || !(j + 3 > ROWS - 1) || !(j + 4 > ROWS - 1) || !(j + 5 > ROWS - 1)) {
					if (board[i][j + 1] == aiColor && board[i][j + 2] == null && board[i][j + 3] == null
							&& board[i][j + 4] == aiColor && board[i][j + 5] == null) {
						weight[i][j] += PRIORITY_3;
					}
				}
				//3.左上至右下
				if ((!(i + 1 > COLS - 1) || !(i + 2 > COLS - 1) || !(i + 3 > COLS - 1) || !(i + 4 > COLS - 1) || !(i + 5 > COLS - 1))
						&& (!(j + 1 > ROWS - 1) || !(j + 2 > ROWS - 1) || !(j + 3 > ROWS - 1) || !(j + 4 > ROWS - 1) || !(j + 5 > ROWS - 1))) {
					if (board[i + 1][j + 1] == aiColor && board[i + 2][j + 2] == null && board[i + 3][j + 3] == null
							&& board[i + 4][j + 4] == aiColor && board[i + 5][j + 5] == null) {
						weight[i][j] += PRIORITY_3;
					}
				}
				//4.右上至左下
				if ((!(i - 1 < 0) || !(i - 2 < 0) || !(i - 3 < 0) || !(i - 4 < 0) || !(i - 5 < 0))
						&& (!(j + 1 > ROWS - 1) || !(j + 2 > ROWS - 1) || !(j + 3 > ROWS - 1) || !(j + 4 > ROWS - 1) || !(j + 5 > ROWS - 1))) {
					if (board[i - 1][j + 1] == aiColor && board[i - 2][j + 2] == null && board[i - 3][j + 3] == null
							&& board[i - 4][j + 4] == aiColor && board[i - 5][j + 5] == null) {
						weight[i][j] += PRIORITY_3;
					}
				}
				
				// 活二_●__●_左二那颗
				// 1.横向
				if (!(i - 2 < 0) || !(i - 1 < 0) || !(i + 1 > COLS - 1) || !(i + 2 > COLS - 1) || !(i + 3 > COLS - 1)) {
					if (board[i - 2][j] == null && board[i - 1][j] == aiColor && board[i + 1][j] == null
							&& board[i + 2][j] == aiColor && board[i + 3][j] == null) {
						weight[i][j] += PRIORITY_3;
					}
				}
				//2.纵向
				if (!(j - 2 < 0) || !(j - 1 < 0) || !(j + 1 > ROWS - 1) || !(j + 2 > ROWS - 1) || !(j + 3 > ROWS - 1)) {
					if (board[i][j - 2] == null && board[i][j - 1] == aiColor && board[i][j + 1] == null
							&& board[i][j + 2] == aiColor && board[i][j + 3] == null) {
						weight[i][j] += PRIORITY_3;
					}
				}
				//3.左上至右下
				if ((!(i - 2 < 0) || !(i - 1 < 0) || !(i + 1 > COLS - 1) || !(i + 2 > COLS - 1) || !(i + 3 > COLS - 1))
						&& (!(j - 2 < 0) || !(j - 1 < 0) || !(j + 1 > ROWS - 1) || !(j + 2 > ROWS - 1) || !(j + 3 > ROWS - 1))) {
					if (board[i - 2][j - 2] == null && board[i - 1][j - 1] == aiColor && board[i + 1][j + 1] == null
							&& board[i + 2][j + 2] == aiColor && board[i + 3][j + 3] == null) {
						weight[i][j] += PRIORITY_3;
					}
				}
				//4.右上至左下
				if ((!(i + 2 > COLS - 1) || !(i + 1 > COLS - 1) || !(i - 1 < 0) || !(i - 2 < 0) || !(i - 3 < 0))
						&& (!(j - 2 < 0) || !(j - 1 < 0) || !(j + 1 > ROWS - 1) || !(j + 2 > ROWS - 1) || !(j + 3 > ROWS - 1))) {
					if (board[i + 2][j - 2] == null && board[i + 1][j - 1] == aiColor && board[i - 1][j + 1] == null
							&& board[i - 2][j + 2] == aiColor && board[i - 3][j + 3] == null) {
						weight[i][j] += PRIORITY_3;
					}
				}
				
				// 活二_●__●_右二那颗
				// 1.横向
				if (!(i - 3 < 0) || !(i - 2 < 0) || !(i - 1 < 0) || !(i + 1 > COLS - 1) || !(i + 2 > COLS - 1)) {
					if (board[i - 3][j] == null && board[i - 2][j] == aiColor && board[i - 1][j] == null
							&& board[i + 1][j] == aiColor && board[i + 2][j] == null) {
						weight[i][j] += PRIORITY_3;
					}
				}
				//2.纵向
				if (!(j - 3 < 0) || !(j - 2 < 0) || !(j - 1 < 0) || !(j + 1 > ROWS - 1) || !(j + 2 > ROWS - 1)) {
					if (board[i][j - 3] == null && board[i][j - 2] == aiColor && board[i][j - 1] == null
							&& board[i][j + 1] == aiColor && board[i][j + 2] == null) {
						weight[i][j] += PRIORITY_3;
					}
				}
				//3.左上至右下
				if ((!(i - 3 < 0) || !(i - 2 < 0) || !(i - 1 < 0) || !(i + 1 > COLS - 1) || !(i + 2 > COLS - 1))
						&& (!(j - 2 < 0) || !(j - 2 < 0) || !(j - 1 < 0) || !(j + 1 > ROWS - 1) || !(j + 2 > ROWS - 1))) {
					if (board[i - 3][j - 3] == null && board[i - 2][j - 2] == aiColor && board[i - 1][j - 1] == null
							&& board[i + 1][j + 1] == aiColor && board[i + 2][j + 2] == null) {
						weight[i][j] += PRIORITY_3;
					}
				}
				//4.右上至左下
				if ((!(i + 3 > COLS - 1) || !(i + 2 > COLS - 1) || !(i + 1 > COLS - 1) || !(i - 1 < 0) || !(i - 2 < 0))
						&& (!(j - 3 < 0) || !(j - 2 < 0) || !(j - 1 < 0) || !(j + 1 > ROWS - 1) || !(j + 2 > ROWS - 1))) {
					if (board[i + 3][j - 3] == null && board[i + 2][j - 2] == aiColor && board[i + 1][j - 1] == null
							&& board[i - 1][j + 1] == aiColor && board[i - 2][j + 2] == null) {
						weight[i][j] += PRIORITY_3;
					}
				}
				
				// 活二_●__●_最右边那颗
				// 1.横向
				if (!(i - 1 < 0) || !(i - 2 < 0) || !(i - 3 < 0) || !(i - 4 < 0) || !(i - 5 < 0)) {
					if (board[i - 1][j] == aiColor && board[i - 2][j] == null && board[i - 3][j] == null
							&& board[i - 4][j] == aiColor && board[i - 5][j] == null) {
						weight[i][j] += PRIORITY_3;
					}
				}
				//2.纵向
				if (!(j - 1 < 0) || !(j - 2 < 0) || !(j - 3 < 0) || !(j - 4 < 0) || !(j - 5 < 0)) {
					if (board[i][j - 1] == aiColor && board[i][j - 2] == null && board[i][j - 3] == null
							&& board[i][j - 4] == aiColor && board[i][j - 5] == null) {
						weight[i][j] += PRIORITY_3;
					}
				}
				//3.左上至右下
				if ((!(i - 1 < 0) || !(i - 2 < 0) || !(i - 3 < 0) || !(i - 4 < 0) || !(i - 5 < 0))
						&& (!(j - 1 < 0) || !(j - 2 < 0) || !(j - 3 < 0) || !(j - 4 < 0) || !(j - 5 < 0))) {
					if (board[i - 1][j - 1] == aiColor && board[i - 2][j - 2] == null && board[i - 3][j - 3] == null
							&& board[i - 4][j - 4] == aiColor && board[i - 5][j - 5] == null) {
						weight[i][j] += PRIORITY_3;
					}
				}
				//4.右上至左下
				if ((!(i + 1 < 0) || !(i + 2 < 0) || !(i + 3 < 0) || !(i + 4 < 0) || !(i + 5 < 0))
						&& (!(j - 1 > ROWS - 1) || !(j - 2 > ROWS - 1) || !(j - 3 > ROWS - 1) || !(j - 4 > ROWS - 1) || !(j - 5 > ROWS - 1))) {
					if (board[i + 1][j - 1] == aiColor && board[i + 2][j - 2] == null && board[i + 3][j - 3] == null
							&& board[i + 4][j - 4] == aiColor && board[i + 5][j - 5] == null) {
						weight[i][j] += PRIORITY_3;
					}
				}
				
				//眠三__●●●○ 左
				// 1.横向
				if (!(i + 1 > COLS - 1) || !(i + 2 > COLS - 1) || !(i + 3 > COLS - 1) || !(i + 4 > COLS - 1) || !(i + 5 > COLS - 1)) {
					if (board[i + 1][j] == null && board[i + 2][j] == aiColor && board[i + 3][j] == aiColor
							&& board[i + 4][j] == aiColor && board[i + 5][j] == anotherColor) {
						weight[i][j] += PRIORITY_2;
					}
				}
				//2.纵向
				if (!(j + 1 > ROWS - 1) || !(j + 2 > ROWS - 1) || !(j + 3 > ROWS - 1) || !(j + 4 > ROWS - 1) || !(j + 4 > ROWS - 1)) {
					if (board[i][j + 1] == null && board[i][j + 2] == aiColor && board[i][j + 3] == aiColor
							&& board[i][j + 4] == aiColor && board[i][j + 5] == anotherColor) {
						weight[i][j] += PRIORITY_2;
					}
				}
				//3.左上至右下
				if ((!(i + 1 > COLS - 1) || !(i + 2 > COLS - 1) || !(i + 3 > COLS - 1) || !(i + 4 > COLS - 1) || !(i + 5 > COLS - 1))
						&& (!(j + 1 > ROWS - 1) || !(j + 2 > ROWS - 1) || !(j + 3 > ROWS - 1) || !(j + 4 > ROWS - 1) || !(j + 5 > ROWS - 1))) {
					if (board[i + 1][j + 1] == null && board[i + 2][j + 2] == aiColor && board[i + 3][j + 3] == aiColor
							&& board[i + 4][j + 4] == aiColor && board[i + 5][j + 5] == anotherColor) {
						weight[i][j] += PRIORITY_2;
					}
				}
				//4.右上至左下
				if ((!(i - 1 < 0) || !(i - 2 < 0) || !(i - 3 < 0) || !(i - 4 < 0) || !(i - 5 < 0))
						&& (!(j + 1 > ROWS - 1) || !(j + 2 > ROWS - 1) || !(j + 3 > ROWS - 1) || !(j + 4 > ROWS - 1) || !(j + 5 > ROWS - 1))) {
					if (board[i - 1][j + 1] == null && board[i - 2][j + 2] == aiColor && board[i - 3][j + 3] == aiColor
							&& board[i - 4][j + 4] == aiColor && board[i - 5][j + 5] == anotherColor) {
						weight[i][j] += PRIORITY_2;
					}
				}
				
				//眠三__●●●○ 右
				// 1.横向
				if (!(i - 1 < 0) || !(i + 1 > COLS - 1) || !(i + 2 > COLS - 1) || !(i + 3 > COLS - 1) || !(i + 4 > COLS - 1)) {
					if (board[i - 1][j] == null && board[i + 1][j] == aiColor && board[i + 2][j] == aiColor
							&& board[i + 3][j] == aiColor && board[i + 4][j] == anotherColor) {
						weight[i][j] += PRIORITY_2;
					}
				}
				//2.纵向
				if (!(j - 1 < 0) || !(j + 1 > ROWS - 1) || !(j + 2 > ROWS - 1) || !(j + 3 > ROWS - 1) || !(j + 4 > ROWS - 1)) {
					if (board[i][j - 1] == null && board[i][j + 1] == aiColor && board[i][j + 2] == aiColor
							&& board[i][j + 3] == aiColor && board[i][j + 4] == anotherColor) {
						weight[i][j] += PRIORITY_2;
					}
				}
				//3.左上至右下
				if ((!(i - 1 < 0) || !(i + 1 > COLS - 1) || !(i + 2 > COLS - 1) || !(i + 3 > COLS - 1) || !(i + 4 > COLS - 1))
						&& (!(j < 0) || !(j + 1 > ROWS - 1) || !(j + 2 > ROWS - 1) || !(j + 3 > ROWS - 1) || !(j + 4 > ROWS - 1))) {
					if (board[i - 1][j - 1] == null && board[i + 1][j + 1] == aiColor && board[i + 2][j + 2] == aiColor
							&& board[i + 3][j + 3] == aiColor && board[i + 4][j + 4] == anotherColor) {
						weight[i][j] += PRIORITY_2;
					}
				}
				//4.右上至左下
				if ((!(i + 1 > COLS - 1) || !(i - 1 < 0) || !(i - 2 < 0) || !(i - 3 < 0) || !(i - 4 < 0))
						&& (!(j - 1 < 0) || !(j + 1 > ROWS - 1) || !(j + 2 > ROWS - 1) || !(j + 3 > ROWS - 1) || !(j + 4 > ROWS - 1))) {
					if (board[i + 1][j - 1] == null && board[i - 1][j + 1] == aiColor && board[i - 2][j + 2] == aiColor
							&& board[i - 3][j + 3] == aiColor && board[i - 4][j + 4] == anotherColor) {
						weight[i][j] += PRIORITY_2;
					}
				}
				
				//眠三○●●●__ 左
				// 1.横向
				if (!(i - 4 < 0) || !(i - 3 < 0) || !(i - 2 < 0) || !(i - 1 < 0) || !(i + 1 > COLS - 1)) {
					if (board[i - 4][j] == anotherColor && board[i - 3][j] == aiColor && board[i - 2][j] == aiColor
							&& board[i - 1][j] == aiColor && board[i + 1][j] == null) {
						weight[i][j] += PRIORITY_2;
					}
				}
				//2.纵向
				if (!(j - 4 < 0) || !(j - 3 < 0) || !(j - 2 < 0) || !(j - 1 < 0) || !(j + 1 > COLS - 1)) {
					if (board[i][j - 4] == anotherColor && board[i][j - 3] == aiColor && board[i][j - 2] == aiColor
							&& board[i][j - 1] == aiColor && board[i][j + 1] == null) {
						weight[i][j] += PRIORITY_2;
					}
				}
				//3.左上至右下
				if ((!(i - 4 < 0) || !(i - 3 < 0) || !(i - 2 < 0) || !(i - 1 < 0) || !(i + 1 > COLS - 1))
						&& (!(j - 4 < 0) || !(j - 3 < 0) || !(j - 2 < 0) || !(j - 1 < 0) || !(j + 1 > COLS - 1))) {
					if (board[i - 4][j - 4] == anotherColor && board[i - 3][j - 3] == aiColor && board[i - 2][j - 2] == aiColor
							&& board[i - 1][j - 1] == aiColor && board[i + 1][j + 1] == null) {
						weight[i][j] += PRIORITY_2;
					}
				}
				//4.右上至左下
				if ((!(i + 4 > COLS - 1) || !(i + 3 > COLS - 1) || !(i + 2 > COLS - 1) || !(i + 1 > COLS - 1) || !(i - 1 < 0))
						&& (!(j - 4 < 0) || !(j - 3 < 0) || !(j - 2 < 0) || !(j - 1 < 0) || !(j + 1 > COLS - 1))) {
					if (board[i - 1][j + 1] == null && board[i - 2][j + 2] == aiColor && board[i - 3][j + 3] == aiColor
							&& board[i - 4][j + 4] == aiColor && board[i - 5][j + 5] == anotherColor) {
						weight[i][j] += PRIORITY_2;
					}
				}
				
				//眠三○●●●__ 右
				
				
				
				
				
				
				
				
				//眠三○●●●_
				// 1.横向
				if (!(i - 1 < 0) || !(i - 2 < 0) || !(i - 3 < 0) || !(i - 4 < 0)) {
					if (board[i - 1][j] == aiColor && board[i - 2][j] == aiColor && board[i - 3][j] == aiColor && board[i - 4][j] == anotherColor) {
						weight[i][j] += PRIORITY_2;
					}
				}
				//2.纵向
				if (!(j - 1 < 0) || !(j - 2 < 0) || !(j - 3 < 0) || !(j - 4 < 0)) {
					if (board[i][j - 1] == aiColor && board[i][j - 2] == aiColor && board[i][j - 3] == aiColor && board[i][j - 4] == anotherColor) {
						weight[i][j] += PRIORITY_2;
					}
				}
				//3.右下至左上
				if ((!(i - 1 < 0) || !(i - 2 < 0) || !(i - 3 < 0) || !(i - 4 < 0))
						&& (!(j - 1 < 0) || !(j - 2 < 0) || !(j - 3 < 0) || !(j - 4 < 0))) {
					if (board[i - 1][j - 1] == aiColor && board[i - 2][j - 2] == aiColor
							&& board[i - 3][j - 3] == aiColor && board[i - 4][j - 4] == anotherColor) {
						weight[i][j] += PRIORITY_2;
					}
				}
				//4.左下至右上
				if ((!(i + 1 > COLS - 1) || !(i + 2 > COLS - 1) || !(i + 3 > COLS - 1) || !(i + 4 > COLS - 1))
						&& (!(j - 1 < 0) || !(j - 2 < 0) || !(j - 3 < 0) || !(j - 4 < 0))) {
					if (board[i + 1][j - 1] == aiColor && board[i + 2][j - 2] == aiColor
							&& board[i + 3][j - 3] == aiColor && board[i + 4][j - 4] == anotherColor) {
						weight[i][j] += PRIORITY_2;
					}
				}
				
				//眠三_●_●●○左边那颗
				// 1.横向
				if (!(i + 1 > COLS - 1) || !(i + 2 > COLS - 1) || !(i + 3 > COLS - 1) || !(i + 4 > COLS - 1) || !(i + 5 > COLS - 1)) {
					if (board[i + 1][j] == aiColor && board[i + 2][j] == null && board[i + 3][j] == aiColor
							&& board[i + 4][j] == aiColor && board[i + 5][j] == anotherColor) {
						weight[i][j] += PRIORITY_2;
					}
				}
				//2.纵向
				if (!(j + 1 > ROWS - 1) || !(j + 2 > ROWS - 1) || !(j + 3 > ROWS - 1) || !(j + 4 > ROWS - 1) || !(j + 5 > ROWS - 1)) {
					if (board[i][j + 1] == aiColor && board[i][j + 2] == null && board[i][j + 3] == aiColor
							&& board[i][j + 4] == aiColor && board[i][j + 5] == anotherColor) {
						weight[i][j] += PRIORITY_2;
					}
				}
				//3.左上至右下
				if ((!(i + 1 > COLS - 1) || !(i + 2 > COLS - 1) || !(i + 3 > COLS - 1) || !(i + 4 > COLS - 1) || !(i + 5 > COLS - 1))
						&& (!(j + 1 > ROWS - 1) || !(j + 2 > ROWS - 1) || !(j + 3 > ROWS - 1) || !(j + 4 > ROWS - 1) || !(j + 5 > ROWS - 1))) {
					if (board[i + 1][j + 1] == aiColor && board[i + 2][j + 2] == null
							&& board[i + 3][j + 3] == aiColor && board[i + 4][j + 4] == aiColor && board[i + 5][j + 5] == anotherColor) {
						weight[i][j] += PRIORITY_2;
					}
				}
				//4.右上至左下
				if ((!(i - 1 < 0) || !(i - 2 < 0) || !(i - 3 < 0) || !(i - 4 < 0) || !(i - 5 < 0))
						&& (!(j + 1 > ROWS - 1) || !(j + 2 > ROWS - 1) || !(j + 3 > ROWS - 1) || !(j + 4 > ROWS - 1) || !(j + 5 > ROWS - 1))) {
					if (board[i - 1][j + 1] == aiColor && board[i - 2][j + 2] == aiColor
							&& board[i - 3][j + 3] == aiColor && board[i - 4][j + 4] == aiColor && board[i - 5][j + 5] == anotherColor) {
						weight[i][j] += PRIORITY_2;
					}
				}
				
				//眠三_●_●●○右边那颗
				// 1.横向
				if (!(i - 2 < 0) || !(i - 1 < 0) || !(i + 1 > COLS - 1) || !(i + 2 > COLS - 1) || !(i + 3 > COLS - 1)) {
					if (board[i - 2][j] == null && board[i - 1][j] == aiColor && board[i + 1][j] == aiColor
							&& board[i + 2][j] == aiColor && board[i + 3][j] == anotherColor) {
						weight[i][j] += PRIORITY_2;
					}
				}
				//2.纵向
				if (!(j - 2 < 0) || !(j - 1 < 0) || !(j + 1 > ROWS - 1) || !(j + 2 > ROWS - 1) || !(j + 3 > ROWS - 1)) {
					if (board[i][j - 2] == null && board[i][j - 1] == aiColor && board[i][j + 1] == aiColor
							&& board[i][j + 2] == aiColor && board[i][j + 3] == anotherColor) {
						weight[i][j] += PRIORITY_2;
					}
				}
				//3.左上至右下
				if ((!(i - 2 < 0) || !(i - 1 < 0) || !(i + 1 > COLS - 1) || !(i + 2 > COLS - 1) || !(i + 3 > COLS - 1))
						&& (!(j - 2 < 0) || !(j - 1 < 0) || !(j + 1 > ROWS - 1) || !(j + 2 > ROWS - 1) || !(j + 3 > ROWS - 1))) {
					if (board[i - 2][j - 2] == null && board[i - 1][j - 1] == aiColor && board[i + 1][j + 1] == aiColor
							&& board[i + 2][j + 2] == aiColor && board[i + 3][j + 3] == anotherColor) {
						weight[i][j] += PRIORITY_2;
					}
				}
				//4.右上至左下
				if ((!(i + 1 > COLS - 1) || !(i + 2 > COLS - 1) || !(i - 1 < 0) || !(i - 2 < 0) || !(i - 3 < 0))
						&& (!(j - 1 < 0) || !(j - 2 < 0) || !(j + 1 > ROWS - 1) || !(j + 2 > ROWS - 1) || !(j + 3 > ROWS - 1))) {
					if (board[i + 1][j - 1] == aiColor && board[i + 2][j - 2] == null
							&& board[i - 1][j + 1] == aiColor && board[i - 2][j + 2] == aiColor && board[i - 3][j + 3] == anotherColor) {
						weight[i][j] += PRIORITY_2;
					}
				}
				
				//眠三○●●_●_左边
				// 1.横向
				if (!(i - 3 < 0) || !(i - 2 < 0) || !(i - 1 < 0) || !(i + 1 > COLS - 1) || !(i + 2 > COLS - 1)) {
					if (board[i - 3][j] == anotherColor && board[i - 2][j] == aiColor && board[i - 1][j] == aiColor
							&& board[i + 1][j] == aiColor && board[i + 2][j] == null) {
						weight[i][j] += PRIORITY_2;
					}
				}
				//2.纵向
				if (!(j - 3 < 0) || !(j - 2 < 0) || !(j - 1 < 0) || !(j + 1 > ROWS - 1) || !(j + 2 > ROWS - 1)) {
					if (board[i][j - 3] == anotherColor && board[i][j - 2] == aiColor && board[i][j - 1] == aiColor
							&& board[i][j + 1] == aiColor && board[i][j + 2] == null) {
						weight[i][j] += PRIORITY_2;
					}
				}
				//3.左上至右下
				if ((!(i - 3 < 0) || !(i - 2 < 0) || !(i - 1 < 0) || !(i + 1 > COLS - 1) || !(i + 2 > COLS - 1))
						&& (!(j - 3 < 0) || !(j - 2 < 0) || !(j - 1 < 0) || !(j + 1 > ROWS - 1) || !(j + 2 > ROWS - 1))) {
					if (board[i - 3][j - 3] == anotherColor && board[i - 2][j - 2] == aiColor
							&& board[i - 1][j - 1] == aiColor && board[i + 1][j + 1] == aiColor && board[i + 2][j + 2] == null) {
						weight[i][j] += PRIORITY_2;
					}
				}
				//4.右上至左下
				if ((!(i + 3 > COLS - 1) || !(i + 2 > COLS - 1) || !(i + 1 > COLS - 1) || !(i - 1 < 0) || !(i - 2 < 0))
						&& (!(j - 3 < 0) || !(j - 2 < 0) || !(j - 1 < 0) || !(j + 1 > ROWS - 1) || !(j + 2 > ROWS - 1))) {
					if (board[i + 3][j - 3] == anotherColor && board[i + 2][j - 2] == aiColor
							&& board[i + 1][j - 1] == aiColor && board[i - 1][j + 1] == aiColor && board[i - 2][j + 2] == null) {
						weight[i][j] += PRIORITY_2;
					}
				}
				
				//眠三○●●_●_右边
				// 1.横向
				if (!(i - 5 < 0) || !(i - 4 < 0) || !(i - 3 < 0) || !(i - 2 < 0) || !(i - 1 < 0)) {
					if (board[i - 5][j] == anotherColor && board[i - 4][j] == aiColor && board[i - 3][j] == aiColor
							&& board[i - 2][j] == null && board[i - 1][j] == aiColor) {
						weight[i][j] += PRIORITY_2;
					}
				}
				//2.纵向
				if (!(j - 5 < 0) || !(j - 4 < 0) || !(j - 3 < 0) || !(j - 2 < 0) || !(j - 1 < 0)) {
					if (board[i][j - 5] == anotherColor && board[i][j - 4] == aiColor && board[i][j - 3] == aiColor
							&& board[i][j - 2] == null && board[i][j - 1] == aiColor) {
						weight[i][j] += PRIORITY_2;
					}
				}
				//3.左上至右下
				if ((!(i - 5 < 0) || !(i - 4 < 0) || !(i - 3 < 0) || !(i - 2 < 0) || !(i - 1 < 0))
						&& (!(j - 5 < 0) || !(j - 4 < 0) || !(j - 3 < 0) || !(j - 2 < 0) || !(j - 1 < 0))) {
					if (board[i - 5][j - 5] == anotherColor && board[i - 4][j - 4] == aiColor
							&& board[i - 3][j - 3] == aiColor && board[i - 2][j - 2] == null && board[i - 1][j - 1] == aiColor) {
						weight[i][j] += PRIORITY_2;
					}
				}
				//4.右上至左下
				if ((!(i + 5 > COLS - 1) || !(i + 4 > COLS - 1) || !(i + 3 > COLS - 1) || !(i + 2 > COLS - 1) || !(i + 1 > COLS - 1))
						&& (!(j - 5 < 0) || !(j - 4 < 0) || !(j - 3 < 0) || !(j - 2 < 0) || !(j - 1 < 0))) {
					if (board[i + 5][j - 5] == anotherColor && board[i + 4][j - 4] == aiColor
							&& board[i + 3][j - 3] == aiColor && board[i + 2][j - 2] == null && board[i + 1][j - 1] == aiColor) {
						weight[i][j] += PRIORITY_2;
					}
				}
				
				//眠三○●_●●_ 左边
				// 1.横向
				if (!(i - 2 < 0) || !(i - 1 < 0) || !(i + 1 > COLS - 1) || !(i + 2 > COLS - 1) || !(i + 3 > COLS - 1)) {
					if (board[i - 2][j] == anotherColor && board[i - 1][j] == aiColor && board[i + 1][j] == aiColor
							&& board[i + 2][j] == aiColor && board[i + 3][j] == null) {
						weight[i][j] += PRIORITY_2;
					}
				}
				//2.纵向
				if (!(j - 2 < 0) || !(j - 1 < 0) || !(j + 1 > ROWS - 1) || !(j + 2 > ROWS - 1) || !(j + 3 > ROWS - 1)) {
					if (board[i][j - 2] == anotherColor && board[i][j - 1] == aiColor && board[i][j + 1] == aiColor
							&& board[i][j + 2] == aiColor && board[i][j + 3] == null) {
						weight[i][j] += PRIORITY_2;
					}
				}
				//3.左上至右下
				if ((!(i - 2 < 0) || !(i - 1 < 0) || !(i + 1 > COLS - 1) || !(i + 2 > COLS - 1) || !(i + 3 > COLS - 1))
						&& (!(j - 2 < 0) || !(j - 1 < 0) || !(j + 1 > ROWS - 1) || !(j + 2 > ROWS - 1) || !(j + 3 > ROWS - 1))) {
					if (board[i - 2][j - 2] == anotherColor && board[i - 1][j - 1] == aiColor
							&& board[i + 1][j + 1] == aiColor && board[i + 2][j + 2] == aiColor && board[i + 3][j + 3] == null) {
						weight[i][j] += PRIORITY_2;
					}
				}
				//4.右上至左下
				if ((!(i + 2 > COLS - 1) || !(i + 1 > COLS - 1) || !(i - 1 < 0) || !(i - 2 < 0) || !(i - 3 < 0))
						&& (!(j - 2 < 0) || !(j - 1 < 0) || !(j + 1 > ROWS - 1) || !(j + 2 > ROWS - 1) || !(j + 3 > ROWS - 1))) {
					if (board[i + 2][j - 2] == anotherColor && board[i + 1][j - 1] == aiColor
							&& board[i - 1][j + 1] == aiColor && board[i - 2][j + 2] == aiColor && board[i - 3][j + 3] == null) {
						weight[i][j] += PRIORITY_2;
					}
				}
				
				//眠三○●_●●_ 右边
				// 1.横向
				if (!(i - 5 < 0) || !(i - 4 < 0) || !(i - 3 < 0) || !(i - 2 < 0) || !(i - 1 < 0)) {
					if (board[i - 5][j] == anotherColor && board[i - 4][j] == aiColor && board[i - 3][j] == null
							&& board[i - 2][j] == aiColor && board[i - 1][j] == aiColor) {
						weight[i][j] += PRIORITY_2;
					}
				}
				//2.纵向
				if (!(j - 5 < 0) || !(j - 4 < 0) || !(j - 3 < 0) || !(j - 2 < 0) || !(j - 1 < 0)) {
					if (board[i][j - 5] == anotherColor && board[i][j - 4] == aiColor && board[i][j - 3] == null
							&& board[i][j - 2] == aiColor && board[i][j - 1] == aiColor) {
						weight[i][j] += PRIORITY_2;
					}
				}
				//3.左上至右下
				if ((!(i - 5 < 0) || !(i - 4 < 0) || !(i - 3 < 0) || !(i - 2 < 0) || !(i - 1 < 0))
						&& (!(j - 5 < 0) || !(j - 4 < 0) || !(j - 3 < 0) || !(j - 2 < 0) || !(j - 1 < 0))) {
					if (board[i - 5][j - 5] == anotherColor && board[i - 4][j - 4] == aiColor
							&& board[i - 3][j - 3] == null && board[i - 2][j - 2] == aiColor && board[i - 1][j - 1] == aiColor) {
						weight[i][j] += PRIORITY_2;
					}
				}
				//4.右上至左下
				if ((!(i + 5 > COLS - 1) || !(i + 4 > COLS - 1) || !(i + 3 > COLS - 1) || !(i + 2 > COLS - 1) || !(i + 1 > COLS - 1))
						&& (!(j - 5 < 0) || !(j - 4 < 0) || !(j - 3 < 0) || !(j - 2 < 0) || !(j - 1 < 0))) {
					if (board[i + 5][j - 5] == anotherColor && board[i + 4][j - 4] == aiColor
							&& board[i + 3][j - 3] == null && board[i + 2][j - 2] == aiColor && board[i + 1][j - 1] == aiColor) {
						weight[i][j] += PRIORITY_2;
					}
				}
				
				//眠三_●●_●○ 左
				// 1.横向
				if (!(i + 1 > COLS - 1) || !(i + 2 > COLS - 1) || !(i + 3 > COLS - 1) || !(i + 4 > COLS - 1) || !(i + 5 > COLS - 1)) {
					if (board[i + 1][j] == aiColor && board[i + 2][j] == aiColor && board[i + 3][j] == null
							&& board[i + 4][j] == aiColor && board[i + 5][j] == anotherColor) {
						weight[i][j] += PRIORITY_2;
					}
				}
				//2.纵向
				if (!(j + 1 > ROWS - 1) || !(j + 2 > ROWS - 1) || !(j + 3 > ROWS - 1) || !(j + 4 > ROWS - 1) || !(j + 5 > ROWS - 1)) {
					if (board[i][j + 1] == aiColor && board[i][j + 2] == aiColor && board[i][j + 3] == null
							&& board[i][j + 4] == aiColor && board[i][j + 5] == anotherColor) {
						weight[i][j] += PRIORITY_2;
					}
				}
				//3.左上至右下
				if ((!(i + 1 > COLS - 1) || !(i + 2 > COLS - 1) || !(i + 3 > COLS - 1) || !(i + 4 > COLS - 1) || !(i + 5 > COLS - 1))
						&& (!(j + 1 > ROWS - 1) || !(j + 2 > ROWS - 1) || !(j + 3 > ROWS - 1) || !(j + 4 > ROWS - 1) || !(j + 5 > ROWS - 1))) {
					if (board[i + 1][j + 1] == aiColor && board[i + 2][j + 2] == aiColor
							&& board[i + 3][j + 3] == null && board[i + 4][j + 4] == aiColor && board[i + 5][j + 5] == anotherColor) {
						weight[i][j] += PRIORITY_2;
					}
				}
				//4.右上至左下
				if ((!(i - 1 < 0) || !(i - 2 < 0) || !(i - 3 < 0) || !(i - 4 < 0) || !(i - 5 < 0))
						&& (!(j + 1 > ROWS - 1) || !(j + 2 > ROWS - 1) || !(j + 3 > ROWS - 1) || !(j + 4 > ROWS - 1) || !(j + 5 > ROWS - 1))) {
					if (board[i - 1][j + 1] == aiColor && board[i - 2][j + 2] == aiColor
							&& board[i - 3][j + 3] == null && board[i - 4][j + 4] == aiColor && board[i - 5][j + 5] == anotherColor) {
						weight[i][j] += PRIORITY_2;
					}
				}
				
				//眠三_●●_●○ 右
				// 1.横向
				if (!(i - 3 < 0) || !(i - 2 < 0) || !(i - 1 < 0) || !(i + 1 > COLS - 1) || !(i + 2 > COLS - 1)) {
					if (board[i - 3][j] == null && board[i - 2][j] == aiColor && board[i - 1][j] == aiColor
							&& board[i + 1][j] == aiColor && board[i + 2][j] == anotherColor) {
						weight[i][j] += PRIORITY_2;
					}
				}
				//2.纵向
				if (!(j - 3 < 0) || !(j - 2 < 0) || !(j - 1 < 0) || !(j + 1 > ROWS - 1) || !(j + 2 > ROWS - 1)) {
					if (board[i][j - 3] == null && board[i][j - 2] == aiColor && board[i][j - 1] == aiColor
							&& board[i][j + 1] == aiColor && board[i][j + 2] == anotherColor) {
						weight[i][j] += PRIORITY_2;
					}
				}
				//3.左上至右下
				if ((!(i - 3 < 0) || !(i - 2 < 0) || !(i - 1 < 0) || !(i + 1 > COLS - 1) || !(i + 2 > COLS - 1))
						&& (!(j - 3 < 0) || !(j - 2 < 0) || !(j - 1 < 0) || !(j + 1 > ROWS - 1) || !(j + 2 > ROWS - 1))) {
					if (board[i - 3][j - 3] == null && board[i - 2][j - 2] == aiColor && board[i - 1][j - 1] == aiColor
							&& board[i + 1][j + 1] == aiColor && board[i + 2][j + 2] == anotherColor) {
						weight[i][j] += PRIORITY_2;
					}
				}
				//4.右上至左下
				if ((!(i + 3 > COLS - 1) || !(i + 2 > COLS - 1) || !(i + 1 > COLS - 1) || !(i - 1 < 0) || !(i - 2 < 0))
						&& (!(j - 3 < 0) || !(j - 2 < 0) || !(j - 1 < 0) || !(j + 1 > ROWS - 1) || !(j + 2 > ROWS - 1))) {
					if (board[i + 3][j - 3] == null && board[i + 2][j - 2] == aiColor && board[i + 1][j - 1] == aiColor
							&& board[i - 1][j + 1] == aiColor && board[i - 2][j + 2] == anotherColor) {
						weight[i][j] += PRIORITY_2;
					}
				}
				
				//眠三●_ _●● 左
				// 1.横向
				if (!(i - 1 < 0) || !(i + 1 > COLS - 1) || !(i + 2 > COLS - 1) || !(i + 3 > COLS - 1)) {
					if (board[i - 1][j] == aiColor && board[i + 1][j] == null && board[i + 2][j] == aiColor && board[i + 3][j] == aiColor) {
						weight[i][j] += PRIORITY_2;
					}
				}
				//2.纵向
				if (!(j - 1 < 0) || !(j + 1 > ROWS - 1) || !(j + 2 > ROWS - 1) || !(j + 3 > ROWS - 1)) {
					if (board[i][j - 1] == aiColor && board[i][j + 1] == null && board[i][j + 2] == aiColor && board[i][j + 3] == aiColor) {
						weight[i][j] += PRIORITY_2;
					}
				}
				//3.左上至右下
				if ((!(i - 1 < 0) || !(i + 1 > COLS - 1) || !(i + 2 > COLS - 1) || !(i + 3 > COLS - 1))
						&& (!(j - 1 < 0) || !(j + 1 > ROWS - 1) || !(j + 2 > ROWS - 1) || !(j + 3 > ROWS - 1))) {
					if (board[i - 1][j - 1] == aiColor && board[i + 1][j + 1] == null
							&& board[i + 2][j + 2] == aiColor && board[i + 3][j + 3] == aiColor) {
						weight[i][j] += PRIORITY_2;
					}
				}
				//4.右上至左下
				if ((!(i + 1 > COLS - 1) || !(i - 1 < 0) || !(i - 2 < 0) || !(i - 3 < 0))
						&& (!(j - 1 < 0) || !(j + 1 > ROWS - 1) || !(j + 2 > ROWS - 1) || !(j + 3 > ROWS - 1))) {
					if (board[i + 1][j - 1] == aiColor && board[i - 1][j + 1] == null
							&& board[i - 2][j + 2] == aiColor && board[i - 3][j + 3] == aiColor) {
						weight[i][j] += PRIORITY_2;
					}
				}
				
				//眠三●_ _●● 右
				// 1.横向
				if (!(i - 2 < 0) || !(i - 1 < 0) || !(i + 1 > COLS - 1) || !(i + 2 > COLS - 1)) {
					if (board[i - 2][j] == aiColor && board[i - 1][j] == null && board[i + 1][j] == aiColor && board[i + 2][j] == aiColor) {
						weight[i][j] += PRIORITY_2;
					}
				}
				//2.纵向
				if (!(j - 2 < 0) || !(j - 1 < 0) || !(j + 1 > ROWS - 1) || !(j + 2 > ROWS - 1)) {
					if (board[i][j - 2] == aiColor && board[i][j - 1] == null && board[i][j + 1] == aiColor && board[i][j + 2] == aiColor) {
						weight[i][j] += PRIORITY_2;
					}
				}
				//3.左上至右下
				if ((!(i - 2 < 0) || !(i - 1 < 0) || !(i + 1 > COLS - 1) || !(i + 2 > COLS - 1))
						&& (!(j - 2 < 0) || !(j - 1 < 0) || !(j + 1 > ROWS - 1) || !(j + 2 > ROWS - 1))) {
					if (board[i - 2][j - 2] == aiColor && board[i - 1][j - 1] == null
							&& board[i + 1][j + 1] == aiColor && board[i + 2][j + 2] == aiColor) {
						weight[i][j] += PRIORITY_2;
					}
				}
				//4.右上至左下
				if ((!(i + 2 > COLS - 1) || !(i + 1 > COLS - 1) || !(i - 1 < 0) || !(i - 2 < 0))
						&& (!(j - 2 < 0) || !(j - 1 < 0) || !(j + 1 > ROWS - 1) || !(j + 2 > ROWS - 1))) {
					if (board[i + 2][j - 2] == aiColor && board[i + 1][j - 1] == null
							&& board[i - 1][j + 1] == aiColor && board[i - 2][j + 2] == aiColor) {
						weight[i][j] += PRIORITY_2;
					}
				}
				
				//眠三●●_ _● 左
				// 1.横向
				if (!(i - 2 < 0) || !(i - 1 < 0) || !(i + 1 > COLS - 1) || !(i + 2 > COLS - 1)) {
					if (board[i - 2][j] == aiColor && board[i - 1][j] == aiColor && board[i + 1][j] == null && board[i + 2][j] == aiColor) {
						weight[i][j] += PRIORITY_2;
					}
				}
				//2.纵向
				if (!(j - 2 < 0) || !(j - 1 < 0) || !(j + 1 > ROWS - 1) || !(j + 2 > ROWS - 1)) {
					if (board[i][j - 2] == aiColor && board[i][j - 1] == aiColor && board[i][j + 1] == null && board[i][j + 2] == aiColor) {
						weight[i][j] += PRIORITY_2;
					}
				}
				//3.左上至右下
				if ((!(i - 2 < 0) || !(i - 1 < 0) || !(i + 1 > COLS - 1) || !(i + 2 > COLS - 1))
						&& (!(j - 2 < 0) || !(j - 1 < 0) || !(j + 1 > ROWS - 1) || !(j + 2 > ROWS - 1))) {
					if (board[i - 2][j - 2] == aiColor && board[i - 1][j - 1] == aiColor
							&& board[i + 1][j + 1] == null && board[i + 2][j + 2] == aiColor) {
						weight[i][j] += PRIORITY_2;
					}
				}
				//4.右上至左下
				if ((!(i + 2 > COLS - 1) || !(i + 1 > COLS - 1) || !(i - 1 < 0) || !(i - 2 < 0))
						&& (!(j - 2 < 0) || !(j - 1 < 0) || !(j + 1 > ROWS - 1) || !(j + 2 > ROWS - 1))) {
					if (board[i + 2][j - 2] == aiColor && board[i + 1][j - 1] == aiColor
							&& board[i - 1][j + 1] == null && board[i - 2][j + 2] == aiColor) {
						weight[i][j] += PRIORITY_2;
					}
				}
				
				//眠三●●_ _● 右
				// 1.横向
				if (!(i - 3 < 0) || !(i - 2 < 0) || !(i - 1 < 0) || !(i + 1 > COLS - 1)) {
					if (board[i - 3][j] == aiColor && board[i - 2][j] == aiColor && board[i - 1][j] == null && board[i + 1][j] == aiColor) {
						weight[i][j] += PRIORITY_2;
					}
				}
				//2.纵向
				if (!(j - 3 < 0) || !(j - 2 < 0) || !(j - 1 < 0) || !(j + 1 > ROWS - 1)) {
					if (board[i][j - 3] == aiColor && board[i][j - 2] == aiColor && board[i][j - 1] == null && board[i][j + 1] == aiColor) {
						weight[i][j] += PRIORITY_2;
					}
				}
				//3.左上至右下
				if ((!(i - 3 < 0) || !(i - 2 < 0) || !(i - 1 < 0) || !(i + 1 > COLS - 1))
						&& (!(j - 3 < 0) || !(j - 2 < 0) || !(j - 1 < 0) || !(j + 1 > ROWS - 1))) {
					if (board[i - 3][j - 3] == aiColor && board[i - 2][j - 2] == aiColor
							&& board[i - 1][j - 1] == null && board[i + 1][j + 1] == aiColor) {
						weight[i][j] += PRIORITY_2;
					}
				}
				//4.右上至左下
				if ((!(i + 3 > COLS - 1) || !(i + 2 > COLS - 1) || !(i + 1 > COLS - 1) || !(i - 1 < 0))
						&& (!(j - 3 < 0) || !(j - 2 < 0) || !(j - 1 < 0) || !(j + 1 > ROWS - 1))) {
					if (board[i + 3][j - 3] == aiColor && board[i + 2][j - 2] == aiColor
							&& board[i + 1][j - 1] == null && board[i - 1][j + 1] == aiColor) {
						weight[i][j] += PRIORITY_2;
					}
				}
				
				//眠三●_●_● 左
				// 1.横向
				if (!(i - 1 < 0) || !(i + 1 > COLS - 1) || !(i + 2 > COLS - 1) || !(i + 3 > COLS - 1)) {
					if (board[i - 1][j] == aiColor && board[i + 1][j] == aiColor && board[i + 2][j] == null && board[i + 3][j] == aiColor) {
						weight[i][j] += PRIORITY_2;
					}
				}
				//2.纵向
				if (!(j - 1 < 0) || !(j + 1 > ROWS - 1) || !(j + 2 > ROWS - 1) || !(j + 3 > ROWS - 1)) {
					if (board[i][j - 1] == aiColor && board[i][j + 1] == aiColor && board[i][j + 2] == null && board[i][j + 3] == aiColor) {
						weight[i][j] += PRIORITY_2;
					}
				}
				//3.左上至右下
				if ((!(i - 1 < 0) || !(i + 1 > COLS - 1) || !(i + 2 > COLS - 1) || !(i + 3 > COLS - 1))
						&& (!(j - 1 < 0) || !(j + 1 > ROWS - 1) || !(j + 2 > ROWS - 1) || !(j + 3 > ROWS - 1))) {
					if (board[i - 1][j - 1] == aiColor && board[i + 1][j + 1] == aiColor
							&& board[i + 2][j + 2] == null && board[i + 3][j + 3] == aiColor) {
						weight[i][j] += PRIORITY_2;
					}
				}
				//4.右上至左下
				if ((!(i + 1 > COLS - 1) || !(i - 1 < 0) || !(i - 2 < 0) || !(i - 3 < 0))
						&& (!(j - 1 < 0) || !(j + 1 > ROWS - 1) || !(j + 2 > ROWS - 1) || !(j + 3 > ROWS - 1))) {
					if (board[i + 1][j - 1] == aiColor && board[i - 1][j + 1] == aiColor
							&& board[i - 2][j + 2] == null && board[i - 3][j + 3] == aiColor) {
						weight[i][j] += PRIORITY_2;
					}
				}
				
				//眠三●_●_● 右
				// 1.横向
				if (!(i - 3 < 0) || !(i - 2 < 0) || !(i - 1 < 0) || !(i + 1 > COLS - 1)) {
					if (board[i - 3][j] == aiColor && board[i - 2][j] == null && board[i - 1][j] == aiColor && board[i + 1][j] == aiColor) {
						weight[i][j] += PRIORITY_2;
					}
				}
				//2.纵向
				if (!(j - 3 < 0) || !(j - 2 < 0) || !(j - 1 < 0) || !(j + 1 > ROWS - 1)) {
					if (board[i][j - 3] == aiColor && board[i][j - 2] == null && board[i][j - 1] == aiColor && board[i][j + 1] == aiColor) {
						weight[i][j] += PRIORITY_2;
					}
				}
				//3.左上至右下
				if ((!(i - 3 < 0) || !(i - 2 < 0) || !(i - 1 < 0) || !(i + 1 > COLS - 1))
						&& (!(j - 3 < 0) || !(j - 2 < 0) || !(j - 1 < 0) || !(j + 1 > ROWS - 1))) {
					if (board[i - 3][j - 3] == aiColor && board[i - 2][j - 2] == null
							&& board[i - 1][j - 1] == aiColor && board[i + 1][j + 1] == aiColor) {
						weight[i][j] += PRIORITY_2;
					}
				}
				//4.右上至左下
				if ((!(i + 3 > COLS - 1) || !(i + 2 > COLS - 1) || !(i + 1 > COLS - 1) || !(i - 1 < 0))
						&& (!(j - 3 < 0) || !(j - 2 < 0) || !(j - 1 < 0) || !(j + 1 > ROWS - 1))) {
					if (board[i + 3][j - 3] == aiColor && board[i + 2][j - 2] == null
							&& board[i + 1][j - 1] == aiColor && board[i - 1][j + 1] == aiColor) {
						weight[i][j] += PRIORITY_2;
					}
				}
				
				//眠三○_●●●_○ 左
				// 1.横向
				if (!(i - 1 < 0) || !(i + 1 > COLS - 1) || !(i + 2 > COLS - 1) || !(i + 3 > COLS - 1) || !(i + 4 > COLS - 1) || !(i + 5 > COLS - 1)) {
					if (board[i - 1][j] == anotherColor && board[i + 1][j] == aiColor && board[i + 2][j] == aiColor
							&& board[i + 3][j] == aiColor && board[i + 4][j] == null && board[i + 5][j] == anotherColor) {
						weight[i][j] += PRIORITY_2;
					}
				}
				//2.纵向
				if (!(j - 1 < 0) || !(j + 1 > ROWS - 1) || !(j + 2 > ROWS - 1) || !(j + 3 > ROWS - 1) || !(j + 4 > ROWS - 1) || !(j + 5 > ROWS - 1)) {
					if (board[i][j - 1] == anotherColor && board[i][j + 1] == aiColor && board[i][j + 2] == aiColor
							&& board[i][j + 3] == aiColor && board[i][j + 4] == null && board[i][j + 5] == anotherColor) {
						weight[i][j] += PRIORITY_2;
					}
				}
				//3.左上至右下
				if ((!(i - 1 < 0) || !(i + 1 > COLS - 1) || !(i + 2 > COLS - 1) || !(i + 3 > COLS - 1) || !(i + 4 > COLS - 1) || !(i + 5 > COLS - 1))
				&& (!(j - 1 < 0) || !(j + 1 > ROWS - 1) || !(j + 2 > ROWS - 1) || !(j + 3 > ROWS - 1) || !(j + 4 > ROWS - 1) || !(j + 5 > ROWS - 1))) {
					if (board[i - 1][j - 1] == anotherColor && board[i + 1][j + 1] == aiColor && board[i + 2][j + 2] == aiColor
							&& board[i + 3][j + 3] == aiColor && board[i + 4][j + 4] == null && board[i + 5][j + 5] == anotherColor) {
						weight[i][j] += PRIORITY_2;
					}
				}
				//4.右上至左下
				if ((!(i + 1 > COLS - 1) || !(i - 1 < 0) || !(i - 2 < 0) || !(i - 3 < 0) || !(i - 4 < 0) || !(i - 5 < 0))
				&& (!(j - 1 < 0) || !(j + 1 > ROWS - 1) || !(j + 2 > ROWS - 1) || !(j + 3 > ROWS - 1) || !(j + 4 > ROWS - 1) || !(j + 5 > ROWS - 1))) {
					if (board[i + 1][j - 1] == anotherColor && board[i - 1][j + 1] == aiColor && board[i - 2][j + 2] == aiColor
							&& board[i - 3][j + 3] == aiColor && board[i - 4][j + 4] == null && board[i - 5][j + 5] == anotherColor) {
						weight[i][j] += PRIORITY_2;
					}
				}
				
				//眠三○_●●●_○ 右
				// 1.横向
				if (!(i - 5 < 0) || !(i - 4 < 0) || !(i - 3 < 0) || !(i - 2 < 0) || !(i - 1 < 0) || !(i + 1 > COLS - 1)) {
					if (board[i - 5][j] == anotherColor && board[i - 4][j] == null && board[i - 3][j] == aiColor
							&& board[i - 2][j] == aiColor && board[i - 1][j] == aiColor && board[i + 1][j] == anotherColor) {
						weight[i][j] += PRIORITY_2;
					}
				}
				//2.纵向
				if (!(j - 5 < 0) || !(j - 4 < 0) || !(j - 3 < 0) || !(j - 2 < 0) || !(j - 1 < 0) || !(j + 1 > ROWS - 1)) {
					if (board[i][j - 5] == anotherColor && board[i][j - 4] == null && board[i][j - 3] == aiColor
							&& board[i][j - 2] == aiColor && board[i][j - 1] == aiColor && board[i][j + 1] == anotherColor) {
						weight[i][j] += PRIORITY_2;
					}
				}
				//3.左上至右下
				if ((!(i - 5 < 0) || !(i - 4 < 0) || !(i - 3 < 0) || !(i - 2 < 0) || !(i - 1 < 0) || !(i + 1 > COLS - 1))
						&& (!(j - 5 < 0) || !(j - 4 < 0) || !(j - 3 < 0) || !(j - 2 < 0) || !(j - 1 < 0) || !(j + 1 > ROWS - 1))) {
					if (board[i - 5][j - 5] == anotherColor && board[i - 4][j - 4] == null && board[i - 3][j - 3] == aiColor
							&& board[i - 2][j - 2] == aiColor && board[i - 1][j - 1] == aiColor && board[i + 1][j + 1] == anotherColor) {
						weight[i][j] += PRIORITY_2;
					}
				}
				//4.右上至左下
				if ((!(i + 5 > COLS - 1) || !(i + 4 > COLS - 1) || !(i + 3 > COLS - 1) || !(i + 2 > COLS - 1) || !(i + 1 > COLS - 1) || !(i - 1 < 0))
						&& (!(j - 5 < 0) || !(j - 4 < 0) || !(j - 3 < 0) || !(j - 2 < 0) || !(j - 1 < 0) || !(j + 1 > ROWS - 1))) {
					if (board[i + 5][j - 5] == anotherColor && board[i + 4][j - 4] == null && board[i + 3][j - 3] == aiColor
							&& board[i + 2][j - 2] == aiColor && board[i + 1][j - 1] == aiColor && board[i - 1][j + 1] == anotherColor) {
						weight[i][j] += PRIORITY_2;
					}
				}
				
				//眠二○●●_ _ _ 左
				// 1.横向
				if (!(i - 3 < 0) || !(i - 2 < 0) || !(i - 1 < 0) || !(i + 1 > COLS - 1) || !(i + 2 > COLS - 1)) {
					if (board[i - 3][j] == anotherColor && board[i - 2][j] == aiColor && board[i - 1][j] == aiColor
							&& board[i + 1][j] == null && board[i + 2][j] == null) {
						weight[i][j] += PRIORITY_1;
					}
				}
				//2.纵向
				if (!(j - 3 < 0) || !(j - 2 < 0) || !(j - 1 < 0) || !(j + 1 > ROWS - 1) || !(j + 2 > ROWS - 1)) {
					if (board[i][j - 3] == anotherColor && board[i][j - 2] == aiColor && board[i][j - 1] == aiColor
							&& board[i][j + 1] == null && board[i][j + 2] == null) {
						weight[i][j] += PRIORITY_1;
					}
				}
				//3.右下至左上
				if ((!(i - 3 < 0) || !(i - 2 < 0) || !(i - 1 < 0) || !(i + 1 > COLS - 1) || !(i + 2 > COLS - 1))
						&& (!(j - 3 < 0) || !(j - 2 < 0) || !(j - 1 < 0) || !(j + 1 > ROWS - 1) || !(j + 2 > ROWS - 1))) {
					if (board[i - 3][j - 3] == anotherColor && board[i - 2][j - 2] == aiColor && board[i - 1][j - 1] == aiColor
							&& board[i + 1][j + 1] == null && board[i + 2][j + 2] == null) {
						weight[i][j] += PRIORITY_1;
					}
				}
				//4.左下至右上
				if ((!(i + 3 > COLS - 1) || !(i + 2 > COLS - 1) || !(i + 1 > COLS - 1) || !(i - 1 < 0) || !(i - 2 < 0))
						&& (!(j - 3 < 0) || !(j - 2 < 0) || !(j - 1 < 0) || !(j + 1 > ROWS - 1) || !(j + 2 > ROWS - 1))) {
					if (board[i + 3][j - 3] == anotherColor && board[i + 2][j - 2] == aiColor && board[i + 1][j - 1] == aiColor
							&& board[i - 1][j + 1] == null && board[i - 2][j + 2] == null) {
						weight[i][j] += PRIORITY_1;
					}
				}
				
				//眠二○●●_ _ _ 中
				// 1.横向
				if (!(i - 4 < 0) || !(i - 3 < 0) || !(i - 2 < 0) || !(i - 1 < 0) || !(i + 1 > COLS - 1)) {
					if (board[i - 4][j] == anotherColor && board[i - 3][j] == aiColor && board[i - 2][j] == aiColor
							&& board[i - 1][j] == null && board[i + 1][j] == null) {
						weight[i][j] += PRIORITY_1;
					}
				}
				//2.纵向
				if (!(j - 4 < 0) || !(j - 3 < 0) || !(j - 2 < 0) || !(j - 1 < 0) || !(j + 1 > ROWS - 1)) {
					if (board[i][j - 4] == anotherColor && board[i][j - 3] == aiColor && board[i][j - 2] == aiColor
							&& board[i][j - 1] == null && board[i][j + 1] == null) {
						weight[i][j] += PRIORITY_1;
					}
				}
				//3.右下至左上
				if ((!(i - 4 < 0) || !(i - 3 < 0) || !(i - 2 < 0) || !(i - 1 < 0) || !(i + 1 > COLS - 1))
						&& (!(j - 4 < 0) || !(j - 3 < 0) || !(j - 2 < 0) || !(j - 1 < 0) || !(j + 1 > ROWS - 1))) {
					if (board[i - 4][j - 4] == anotherColor && board[i - 3][j - 3] == aiColor && board[i - 2][j - 2] == aiColor
							&& board[i - 1][j - 1] == null && board[i + 1][j + 1] == null) {
						weight[i][j] += PRIORITY_1;
					}
				}
				//4.左下至右上
				if ((!(i + 4 > COLS - 1) || !(i + 3 > COLS - 1) || !(i + 2 > COLS - 1) || !(i + 1 > COLS - 1) || !(i - 1 < 0))
						&& (!(j - 4 < 0) || !(j - 3 < 0) || !(j - 2 < 0) || !(j - 1 < 0) || !(j + 1 > ROWS - 1))) {
					if (board[i + 4][j - 4] == anotherColor && board[i + 3][j - 3] == aiColor && board[i + 2][j - 2] == aiColor
							&& board[i + 1][j - 1] == null && board[i - 1][j + 1] == null) {
						weight[i][j] += PRIORITY_1;
					}
				}
				
				//眠二○●●_ _ _ 右
				// 1.横向
				if (!(i - 5 < 0) || !(i - 4 < 0) || !(i - 3 < 0) || !(i - 2 < 0) || !(i - 1 < 0)) {
					if (board[i - 5][j] == anotherColor && board[i - 4][j] == aiColor && board[i - 3][j] == aiColor
							&& board[i - 2][j] == null && board[i - 1][j] == null) {
						weight[i][j] += PRIORITY_1;
					}
				}
				//2.纵向
				if (!(j - 5 < 0) || !(j - 4 < 0) || !(j - 3 < 0) || !(j - 2 < 0) || !(j - 1 < 0)) {
					if (board[i][j - 5] == anotherColor && board[i][j - 4] == aiColor && board[i][j - 3] == aiColor
							&& board[i][j - 2] == null && board[i][j - 1] == null) {
						weight[i][j] += PRIORITY_1;
					}
				}
				//3.右下至左上
				if ((!(i - 5 < 0) || !(i - 4 < 0) || !(i - 3 < 0) || !(i - 2 < 0) || !(i - 1 < 0))
						&& (!(j - 5 < 0) || !(j - 4 < 0) || !(j - 3 < 0) || !(j - 2 < 0) || !(j - 1 < 0))) {
					if (board[i - 5][j - 5] == anotherColor && board[i - 4][j - 4] == aiColor && board[i - 3][j - 3] == aiColor
							&& board[i - 2][j - 2] == null && board[i - 1][j - 1] == null) {
						weight[i][j] += PRIORITY_1;
					}
				}
				//4.左下至右上
				if ((!(i + 4 > COLS - 1) || !(i + 4 > COLS - 1) || !(i + 3 > COLS - 1) || !(i + 2 > COLS - 1) || !(i + 1 > COLS - 1))
						&& (!(j - 5 < 0) || !(j - 4 < 0) || !(j - 3 < 0) || !(j - 2 < 0) || !(j - 1 < 0))) {
					if (board[i + 5][j - 5] == anotherColor && board[i + 4][j - 4] == aiColor && board[i + 3][j - 3] == aiColor
							&& board[i + 2][j - 2] == null && board[i + 1][j - 1] == null) {
						weight[i][j] += PRIORITY_1;
					}
				}
				
				//眠二_ _ _●●○ 左
				// 1.横向
				if (!(i + 1 > COLS - 1) || !(i + 2 > COLS - 1) || !(i + 3 > COLS - 1) || !(i + 4 > COLS - 1) || !(i + 5 > COLS - 1)) {
					if (board[i + 1][j] == null && board[i + 2][j] == null && board[i + 3][j] == aiColor
							&& board[i + 4][j] == aiColor && board[i + 5][j] == anotherColor) {
						weight[i][j] += PRIORITY_1;
					}
				}
				//2.纵向
				if (!(j + 1 > ROWS - 1) || !(j + 2 > ROWS - 1) || !(j + 3 > ROWS - 1) || !(j + 4 > ROWS - 1) || !(j + 5 > ROWS - 1)) {
					if (board[i][j + 1] == null && board[i][j + 2] == null && board[i][j + 3] == aiColor
							&& board[i][j + 4] == aiColor && board[i][j + 5] == anotherColor) {
						weight[i][j] += PRIORITY_1;
					}
				}
				//3.左上至右下
				if ((!(i + 1 > COLS - 1) || !(i + 2 > COLS - 1) || !(i + 3 > COLS - 1) || !(i + 4 > COLS - 1) || !(i + 5 > COLS - 1))
						&& (!(j + 1 > ROWS - 1) || !(j + 2 > ROWS - 1) || !(j + 3 > ROWS - 1) || !(j + 4 > ROWS - 1) || !(j + 5 > ROWS - 1))) {
					if (board[i + 1][j + 1] == null && board[i + 2][j + 2] == null && board[i + 3][j + 3] == aiColor
							&& board[i + 4][j + 4] == aiColor && board[i + 5][j + 5] == anotherColor) {
						weight[i][j] += PRIORITY_1;
					}
				}
				//4.右上至左下
				if ((!(i - 1 < 0) || !(i - 2 < 0) || !(i - 3 < 0) || !(i - 4 < 0) || !(i - 5 < 0))
						&& (!(j + 1 > ROWS - 1) || !(j + 2 > ROWS - 1) || !(j + 3 > ROWS - 1) || !(j + 4 > ROWS - 1) || !(j + 5 > ROWS - 1))) {
					if (board[i - 1][j + 1] == null && board[i - 2][j + 2] == null && board[i - 3][j + 3] == aiColor
							&& board[i - 4][j + 4] == aiColor && board[i - 5][j + 5] == anotherColor) {
						weight[i][j] += PRIORITY_1;
					}
				}
				
				//眠二_ _ _●●○ 中
				// 1.横向
				if (!(i - 1 < 0) || !(i + 1 > COLS - 1) || !(i + 2 > COLS - 1) || !(i + 3 > COLS - 1) || !(i + 4 > COLS - 1)) {
					if (board[i - 1][j] == null && board[i + 1][j] == null && board[i + 2][j] == aiColor
							&& board[i + 3][j] == aiColor && board[i + 4][j] == anotherColor) {
						weight[i][j] += PRIORITY_1;
					}
				}
				//2.纵向
				if (!(j - 1 < 0) || !(j + 1 > ROWS - 1) || !(j + 2 > ROWS - 1) || !(j + 3 > ROWS - 1) || !(j + 4 > ROWS - 1)) {
					if (board[i][j - 1] == null && board[i][j + 1] == null && board[i][j + 2] == aiColor
							&& board[i][j + 3] == aiColor && board[i][j + 4] == anotherColor) {
						weight[i][j] += PRIORITY_1;
					}
				}
				//3.左上至右下
				if ((!(i - 1 < 0) || !(i + 1 > COLS - 1) || !(i + 2 > COLS - 1) || !(i + 3 > COLS - 1) || !(i + 4 > COLS - 1))
						&& (!(j - 1 < 0) || !(j + 1 > ROWS - 1) || !(j + 2 > ROWS - 1) || !(j + 3 > ROWS - 1) || !(j + 4 > ROWS - 1))) {
					if (board[i - 1][j - 1] == null && board[i + 1][j + 1] == null && board[i + 2][j + 2] == aiColor
							&& board[i + 3][j + 3] == aiColor && board[i + 4][j + 4] == anotherColor) {
						weight[i][j] += PRIORITY_1;
					}
				}
				//4.右上至左下
				if ((!(i + 1 > COLS - 1) || !(i - 1 < 0) || !(i - 2 < 0) || !(i - 3 < 0) || !(i - 4 < 0))
						&& (!(j - 1 < 0) || !(j + 1 > ROWS - 1) || !(j + 2 > ROWS - 1) || !(j + 3 > ROWS - 1) || !(j + 4 > ROWS - 1))) {
					if (board[i + 1][j - 1] == null && board[i - 1][j + 1] == null && board[i - 2][j + 2] == aiColor
							&& board[i - 3][j + 3] == aiColor && board[i - 4][j + 4] == anotherColor) {
						weight[i][j] += PRIORITY_1;
					}
				}
				
				//眠二_ _ _●●○ 右
				// 1.横向
				if (!(i - 2 < 0) || !(i - 1 < 0) || !(i + 1 > COLS - 1) || !(i + 2 > COLS - 1) || !(i + 3 > COLS - 1)) {
					if (board[i - 2][j] == null && board[i - 1][j] == null && board[i + 1][j] == aiColor
							&& board[i + 2][j] == aiColor && board[i + 3][j] == anotherColor) {
						weight[i][j] += PRIORITY_1;
					}
				}
				//2.纵向
				if (!(j - 2 < 0) || !(j - 1 < 0) || !(j + 1 > ROWS - 1) || !(j + 2 > ROWS - 1) || !(j + 3 > ROWS - 1)) {
					if (board[i][j - 2] == null && board[i][j - 1] == null && board[i][j + 1] == aiColor
							&& board[i][j + 2] == aiColor && board[i][j + 3] == anotherColor) {
						weight[i][j] += PRIORITY_1;
					}
				}
				//3.左上至右下
				if ((!(i - 2 < 0) || !(i - 1 < 0) || !(i + 1 > COLS - 1) || !(i + 2 > COLS - 1) || !(i + 3 > COLS - 1))
						&& (!(j - 2 < 0) || !(j - 1 < 0) || !(j + 1 > ROWS - 1) || !(j + 2 > ROWS - 1) || !(j + 3 > ROWS - 1))) {
					if (board[i - 2][j - 2] == null && board[i - 1][j - 1] == null && board[i + 1][j + 1] == aiColor
							&& board[i + 2][j + 2] == aiColor && board[i + 3][j + 3] == anotherColor) {
						weight[i][j] += PRIORITY_1;
					}
				}
				//4.右上至左下
				if ((!(i + 2 > COLS - 1) || !(i + 1 > COLS - 1) || !(i - 1 < 0) || !(i - 2 < 0) || !(i - 3 < 0))
						&& (!(j - 2 < 0) || !(j - 1 < 0) || !(j + 1 > ROWS - 1) || !(j + 2 > ROWS - 1) || !(j + 3 > ROWS - 1))) {
					if (board[i + 2][j - 2] == null && board[i + 1][j - 1] == null && board[i - 1][j + 1] == aiColor
							&& board[i - 2][j + 2] == aiColor && board[i - 3][j + 3] == anotherColor) {
						weight[i][j] += PRIORITY_1;
					}
				}
				
				//眠二○●_ _●_ 左
				// 1.横向
				if (!(i - 2 < 0) || !(i - 1 < 0) || !(i + 1 > COLS - 1) || !(i + 2 > COLS - 1) || !(i + 3 > COLS - 1)) {
					if (board[i - 2][j] == anotherColor && board[i - 1][j] == aiColor && board[i + 1][j] == null
							&& board[i + 2][j] == aiColor && board[i + 3][j] == null) {
						weight[i][j] += PRIORITY_1;
					}
				}
				//2.纵向
				if (!(j - 2 < 0) || !(j - 1 < 0) || !(j + 1 > ROWS - 1) || !(j + 2 > ROWS - 1) || !(j + 3 > ROWS - 1)) {
					if (board[i][j - 2] == anotherColor && board[i][j - 1] == aiColor && board[i][j + 1] == null
							&& board[i][j + 2] == aiColor && board[i][j + 3] == null) {
						weight[i][j] += PRIORITY_1;
					}
				}
				//3.左上至右下
				if ((!(i - 2 < 0) || !(i - 1 < 0) || !(i + 1 > COLS - 1) || !(i + 2 > COLS - 1) || !(i + 3 > COLS - 1))
						&& (!(j - 2 < 0) || !(j - 1 < 0) || !(j + 1 > ROWS - 1) || !(j + 2 > ROWS - 1) || !(j + 3 > ROWS - 1))) {
					if (board[i - 2][j - 2] == anotherColor && board[i - 1][j - 1] == aiColor && board[i + 1][j + 1] == null
							&& board[i + 2][j + 2] == aiColor && board[i + 3][j + 3] == null) {
						weight[i][j] += PRIORITY_1;
					}
				}
				//4.右上至左下
				if ((!(i + 2 > COLS - 1) || !(i + 1 > COLS - 1) || !(i - 1 < 0) || !(i - 2 < 0) || !(i - 3 < 0))
						&& (!(j - 2 < 0) || !(j - 1 < 0) || !(j + 1 > ROWS - 1) || !(j + 2 > ROWS - 1) || !(j + 3 > ROWS - 1))) {
					if (board[i + 2][j - 2] == anotherColor && board[i + 1][j - 1] == aiColor && board[i - 1][j + 1] == null
							&& board[i - 2][j + 2] == aiColor && board[i - 3][j + 3] == null) {
						weight[i][j] += PRIORITY_1;
					}
				}
				
				//眠二○●_ _●_ 中
				// 1.横向
				if (!(i - 3 < 0) || !(i - 2 < 0) || !(i - 1 < 0) || !(i + 1 > COLS - 1) || !(i + 2 > COLS - 1)) {
					if (board[i - 3][j] == anotherColor && board[i - 2][j] == aiColor && board[i - 1][j] == null
							&& board[i + 1][j] == aiColor && board[i + 2][j] == null) {
						weight[i][j] += PRIORITY_1;
					}
				}
				//2.纵向
				if (!(j - 3 < 0) || !(j - 2 < 0) || !(j - 1 < 0) || !(j + 1 > ROWS - 1) || !(j + 2 > ROWS - 1)) {
					if (board[i][j - 3] == anotherColor && board[i][j - 2] == aiColor && board[i][j - 1] == null
							&& board[i][j + 1] == aiColor && board[i][j + 2] == null) {
						weight[i][j] += PRIORITY_1;
					}
				}
				//3.左上至右下
				if ((!(i - 3 < 0) || !(i - 2 < 0) || !(i - 1 < 0) || !(i + 1 > COLS - 1) || !(i + 2 > COLS - 1))
						&& (!(j - 3 < 0) || !(j - 2 < 0) || !(j - 1 < 0) || !(j + 1 > ROWS - 1) || !(j + 2 > ROWS - 1))) {
					if (board[i - 3][j - 3] == anotherColor && board[i - 2][j - 2] == aiColor && board[i - 1][j - 1] == null
							&& board[i + 1][j + 1] == aiColor && board[i + 2][j + 2] == null) {
						weight[i][j] += PRIORITY_1;
					}
				}
				//4.右上至左下
				if ((!(i + 3 > COLS - 1) || !(i + 2 > COLS - 1) || !(i + 1 > COLS - 1) || !(i - 1 < 0) || !(i - 2 < 0))
						&& (!(j - 3 < 0) || !(j - 2 < 0) || !(j - 1 < 0) || !(j + 1 > ROWS - 1) || !(j + 2 > ROWS - 1))) {
					if (board[i + 3][j - 3] == anotherColor && board[i + 2][j - 2] == aiColor && board[i + 1][j - 1] == null
							&& board[i - 1][j + 1] == aiColor && board[i - 2][j + 2] == null) {
						weight[i][j] += PRIORITY_1;
					}
				}
				
				//眠二○●_ _●_ 右
				// 1.横向
				if (!(i - 5 < 0) || !(i - 4 < 0) || !(i - 3 < 0) || !(i - 2 < 0) || !(i - 1 < 0)) {
					if (board[i - 5][j] == anotherColor && board[i - 4][j] == aiColor && board[i - 3][j] == null
							&& board[i - 2][j] == null && board[i - 1][j] == aiColor) {
						weight[i][j] += PRIORITY_1;
					}
				}
				//2.纵向
				if (!(j - 5 < 0) || !(j - 4 < 0) || !(j - 3 < 0) || !(j - 2 < 0) || !(j - 1 < 0)) {
					if (board[i][j - 5] == anotherColor && board[i][j - 4] == aiColor && board[i][j - 3] == null
							&& board[i][j - 2] == null && board[i][j - 1] == aiColor) {
						weight[i][j] += PRIORITY_1;
					}
				}
				//3.左上至右下
				if ((!(i - 5 < 0) || !(i - 4 < 0) || !(i - 3 < 0) || !(i - 2 < 0) || !(i - 1 < 0))
						&& (!(j - 5 < 0) || !(j - 4 < 0) || !(j - 3 < 0) || !(j - 2 < 0) || !(j - 1 < 0))) {
					if (board[i - 5][j - 5] == anotherColor && board[i - 4][j - 4] == aiColor && board[i - 3][j - 3] == null
							&& board[i - 2][j - 2] == null && board[i - 1][j - 1] == aiColor) {
						weight[i][j] += PRIORITY_1;
					}
				}
				//4.右上至左下
				if ((!(i + 5 > COLS - 1) || !(i + 4 > COLS - 1) || !(i + 3 > COLS - 1) || !(i + 2 > COLS - 1) || !(i + 1 > COLS - 1))
						&& (!(j - 5 < 0) || !(j - 4 < 0) || !(j - 3 < 0) || !(j - 2 < 0) || !(j - 1 < 0))) {
					if (board[i + 5][j - 5] == anotherColor && board[i + 4][j - 4] == aiColor && board[i + 3][j - 3] == null
							&& board[i + 2][j - 2] == null && board[i + 1][j - 1] == aiColor) {
						weight[i][j] += PRIORITY_1;
					}
				}
				
				//眠二_●_ _●○ 左
				// 1.横向
				if (!(i + 1 > COLS - 1) || !(i + 2 > COLS - 1) || !(i + 3 > COLS - 1) || !(i + 4 > COLS - 1) || !(i + 5 > COLS - 1)) {
					if (board[i + 1][j] == aiColor && board[i + 2][j] == null && board[i + 3][j] == null
							&& board[i + 4][j] == aiColor && board[i + 5][j] == anotherColor) {
						weight[i][j] += PRIORITY_1;
					}
				}
				//2.纵向
				if (!(j + 1 > ROWS - 1) || !(j + 2 > ROWS - 1) || !(j + 3 > ROWS - 1) || !(j + 4 > ROWS - 1) || !(j + 5 > ROWS - 1)) {
					if (board[i][j + 1] == aiColor && board[i][j + 2] == null && board[i][j + 3] == null
							&& board[i][j + 4] == aiColor && board[i][j + 5] == anotherColor) {
						weight[i][j] += PRIORITY_1;
					}
				}
				//3.左上至右下
				if ((!(i + 1 > COLS - 1) || !(i + 2 > COLS - 1) || !(i + 3 > COLS - 1) || !(i + 4 > COLS - 1) || !(i + 5 > COLS - 1))
						&& (!(j + 1 > ROWS - 1) || !(j + 2 > ROWS - 1) || !(j + 3 > ROWS - 1) || !(j + 4 > ROWS - 1) || !(j + 5 > ROWS - 1))) {
					if (board[i + 1][j + 1] == aiColor && board[i + 2][j + 2] == null && board[i + 3][j + 3] == null
							&& board[i + 4][j + 4] == aiColor && board[i + 5][j + 5] == anotherColor) {
						weight[i][j] += PRIORITY_1;
					}
				}
				//4.右上至左下
				if ((!(i - 1 < 0) || !(i - 2 < 0) || !(i - 3 < 0) || !(i - 4 < 0) || !(i - 5 < 0))
						&& (!(j + 1 > ROWS - 1) || !(j + 2 > ROWS - 1) || !(j + 3 > ROWS - 1) || !(j + 4 > ROWS - 1) || !(j + 5 > ROWS - 1))) {
					if (board[i - 1][j + 1] == aiColor && board[i - 2][j + 2] == null && board[i - 3][j + 3] == null
							&& board[i - 4][j + 4] == aiColor && board[i - 5][j + 5] == anotherColor) {
						weight[i][j] += PRIORITY_1;
					}
				}
				
				//眠二_●_ _●○ 中
				// 1.横向
				if (!(i - 2 < 0) || !(i - 1 < 0) || !(i + 1 > COLS - 1) || !(i + 2 > COLS - 1) || !(i + 3 > COLS - 1)) {
					if (board[i - 2][j] == null && board[i - 1][j] == aiColor && board[i + 1][j] == null
							&& board[i + 2][j] == aiColor && board[i + 3][j] == anotherColor) {
						weight[i][j] += PRIORITY_1;
					}
				}
				//2.纵向
				if (!(j - 2 < 0) || !(j - 1 < 0) || !(j + 1 > ROWS - 1) || !(j + 2 > ROWS - 1) || !(j + 3 > ROWS - 1)) {
					if (board[i][j - 2] == null && board[i][j - 1] == aiColor && board[i][j + 1] == null
							&& board[i][j + 2] == aiColor && board[i][j + 3] == anotherColor) {
						weight[i][j] += PRIORITY_1;
					}
				}
				//3.左上至右下
				if ((!(i - 2 < 0) || !(i - 1 < 0) || !(i + 1 > COLS - 1) || !(i + 2 > COLS - 1) || !(i + 3 > COLS - 1))
						&& (!(j - 2 < 0) || !(j - 1 < 0) || !(j + 1 > ROWS - 1) || !(j + 2 > ROWS - 1) || !(j + 3 > ROWS - 1))) {
					if (board[i - 2][j - 2] == null && board[i - 1][j - 1] == aiColor && board[i + 1][j + 1] == null
							&& board[i + 2][j + 2] == aiColor && board[i + 3][j + 3] == anotherColor) {
						weight[i][j] += PRIORITY_1;
					}
				}
				//4.右上至左下
				if ((!(i + 2 > COLS - 1) || !(i + 1 > COLS - 1) || !(i - 1 < 0) || !(i - 2 < 0) || !(i - 3 < 0))
						&& (!(j - 2 < 0) || !(j - 1 < 0) || !(j + 1 > ROWS - 1) || !(j + 2 > ROWS - 1) || !(j + 3 > ROWS - 1))) {
					if (board[i + 2][j - 2] == null && board[i + 1][j - 1] == aiColor && board[i - 1][j + 1] == null
							&& board[i - 2][j + 2] == aiColor && board[i - 3][j + 3] == anotherColor) {
						weight[i][j] += PRIORITY_1;
					}
				}
				
				//眠二_●_ _●○ 右
				// 1.横向
				if (!(i - 3 < 0) || !(i - 2 < 0) || !(i - 1 < 0) || !(i + 1 > COLS - 1) || !(i + 2 > COLS - 1)) {
					if (board[i - 3][j] == null && board[i - 2][j] == aiColor && board[i - 1][j] == null
							&& board[i + 1][j] == aiColor && board[i + 2][j] == anotherColor) {
						weight[i][j] += PRIORITY_1;
					}
				}
				//2.纵向
				if (!(j - 3 < 0) || !(j - 2 < 0) || !(j - 1 < 0) || !(j + 1 > ROWS - 1) || !(j + 2 > ROWS - 1)) {
					if (board[i][j - 3] == null && board[i][j - 2] == aiColor && board[i][j - 1] == null
							&& board[i][j + 1] == aiColor && board[i][j + 2] == anotherColor) {
						weight[i][j] += PRIORITY_1;
					}
				}
				//3.左上至右下
				if ((!(i - 3 < 0) || !(i - 2 < 0) || !(i - 1 < 0) || !(i + 1 > COLS - 1) || !(i + 2 > COLS - 1))
						&& (!(j - 3 < 0) || !(j - 2 < 0) || !(j - 1 < 0) || !(j + 1 > ROWS - 1) || !(j + 2 > ROWS - 1))) {
					if (board[i - 3][j - 3] == null && board[i - 2][j - 2] == aiColor && board[i - 1][j - 1] == null
							&& board[i + 1][j + 1] == aiColor && board[i + 2][j + 2] == anotherColor) {
						weight[i][j] += PRIORITY_1;
					}
				}
				//4.右上至左下
				if ((!(i + 3 > COLS - 1) || !(i + 2 > COLS - 1) || !(i + 1 > COLS - 1) || !(i - 1 < 0) || !(i - 2 < 0))
						&& (!(j - 3 < 0) || !(j - 2 < 0) || !(j - 1 < 0) || !(j + 1 > ROWS - 1) || !(j + 2 > ROWS - 1))) {
					if (board[i + 3][j - 3] == null && board[i + 2][j - 2] == aiColor && board[i + 1][j - 1] == null
							&& board[i - 1][j + 1] == aiColor && board[i - 2][j + 2] == anotherColor) {
						weight[i][j] += PRIORITY_1;
					}
				}
				
				//眠二●_ _ _● 左
				// 1.横向
				if (!(i - 1 < 0) || !(i + 1 > COLS - 1) || !(i + 2 > COLS - 1) || !(i + 3 > COLS - 1)) {
					if (board[i - 1][j] == aiColor && board[i + 1][j] == null && board[i + 2][j] == null && board[i + 3][j] == aiColor) {
						weight[i][j] += PRIORITY_2;
					}
				}
				//2.纵向
				if (!(j - 1 < 0) || !(j + 1 > ROWS - 1) || !(j + 2 > ROWS - 1) || !(j + 3 > ROWS - 1)) {
					if (board[i][j - 1] == aiColor && board[i][j + 1] == null && board[i][j + 2] == null && board[i][j + 3] == aiColor) {
						weight[i][j] += PRIORITY_2;
					}
				}
				//3.左上至右下
				if ((!(i - 1 < 0) || !(i + 1 > COLS - 1) || !(i + 2 > COLS - 1) || !(i + 3 > COLS - 1))
						&& (!(j - 1 < 0) || !(j + 1 > ROWS - 1) || !(j + 2 > ROWS - 1) || !(j + 3 > ROWS - 1))) {
					if (board[i - 1][j - 1] == aiColor && board[i + 1][j + 1] == null
							&& board[i + 2][j + 2] == null && board[i + 3][j + 3] == aiColor) {
						weight[i][j] += PRIORITY_2;
					}
				}
				//4.右上至左下
				if ((!(i + 1 > COLS - 1) || !(i - 1 < 0) || !(i - 2 < 0) || !(i - 3 < 0))
						&& (!(j - 1 < 0) || !(j + 1 > ROWS - 1) || !(j + 2 > ROWS - 1) || !(j + 3 > ROWS - 1))) {
					if (board[i + 1][j - 1] == aiColor && board[i - 1][j + 1] == null
							&& board[i - 2][j + 2] == null && board[i - 3][j + 3] == aiColor) {
						weight[i][j] += PRIORITY_2;
					}
				}
				
				//眠二●_ _ _● 中
				// 1.横向
				if (!(i - 2 < 0) || !(i - 1 < 0) || !(i + 1 > COLS - 1) || !(i + 2 > COLS - 1)) {
					if (board[i - 2][j] == aiColor && board[i - 1][j] == null && board[i + 1][j] == null && board[i + 2][j] == aiColor) {
						weight[i][j] += PRIORITY_1;
					}
				}
				//2.纵向
				if (!(j - 2 < 0) || !(j - 1 < 0) || !(j + 1 > ROWS - 1) || !(j + 2 > ROWS - 1)) {
					if (board[i][j - 2] == aiColor && board[i][j - 1] == null && board[i][j + 1] == null && board[i][j + 2] == aiColor) {
						weight[i][j] += PRIORITY_1;
					}
				}
				//3.左上至右下
				if ((!(i - 2 < 0) || !(i - 1 < 0) || !(i + 1 > COLS - 1) || !(i + 2 > COLS - 1))
						&& (!(j - 2 < 0) || !(j - 1 < 0) || !(j + 1 > ROWS - 1) || !(j + 2 > ROWS - 1))) {
					if (board[i - 2][j - 2] == aiColor && board[i - 1][j - 1] == null
							&& board[i + 1][j + 1] == null && board[i + 2][j + 2] == aiColor) {
						weight[i][j] += PRIORITY_1;
					}
				}
				//4.右上至左下
				if ((!(i + 2 > COLS - 1) || !(i + 1 > COLS - 1) || !(i - 1 < 0) || !(i - 2 < 0))
						&& (!(j - 2 < 0) || !(j - 1 < 0) || !(j + 1 > ROWS - 1) || !(j + 2 > ROWS - 1))) {
					if (board[i + 2][j - 2] == aiColor && board[i + 1][j - 1] == null
							&& board[i - 1][j + 1] == null && board[i - 2][j + 2] == aiColor) {
						weight[i][j] += PRIORITY_1;
					}
				}
				
				//眠二●_ _ _● 右
				// 1.横向
				if (!(i - 3 < 0) || !(i - 2 < 0) || !(i - 1 < 0) || !(i + 1 > COLS - 1)) {
					if (board[i - 3][j] == aiColor && board[i - 2][j] == null && board[i - 1][j] == null && board[i + 1][j] == aiColor) {
						weight[i][j] += PRIORITY_1;
					}
				}
				//2.纵向
				if (!(j - 3 < 0) || !(j - 2 < 0) || !(j - 1 < 0) || !(j + 1 > ROWS - 1)) {
					if (board[i][j - 3] == aiColor && board[i][j - 2] == null && board[i][j - 1] == null && board[i][j + 1] == aiColor) {
						weight[i][j] += PRIORITY_1;
					}
				}
				//3.左上至右下
				if ((!(i - 3 < 0) || !(i - 2 < 0) || !(i - 1 < 0) || !(i + 1 > COLS - 1))
						&& (!(j - 3 < 0) || !(j - 2 < 0) || !(j - 1 < 0) || !(j + 1 > ROWS - 1))) {
					if (board[i - 3][j - 3] == aiColor && board[i - 2][j - 2] == null
							&& board[i - 1][j - 1] == null && board[i + 1][j + 1] == aiColor) {
						weight[i][j] += PRIORITY_1;
					}
				}
				//4.右上至左下
				if ((!(i + 3 > COLS - 1) || !(i + 2 > COLS - 1) || !(i + 1 > COLS - 1) || !(i - 1 < 0))
						&& (!(j - 3 < 0) || !(j - 2 < 0) || !(j - 1 < 0) || !(j + 1 > ROWS - 1))) {
					if (board[i + 3][j - 3] == aiColor && board[i + 2][j - 2] == null
							&& board[i + 1][j - 1] == null && board[i - 1][j + 1] == aiColor) {
						weight[i][j] += PRIORITY_1;
					}
				}
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				
			}
		}
	}

}
