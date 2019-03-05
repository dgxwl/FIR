package qwe;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class CPUWorld extends World {
	private static final long serialVersionUID = 1L;

	private static final int PRIORITY_6 = 7;  //活四得分
	private static final int PRIORITY_5 = 6;  //冲四得分
	private static final int PRIORITY_4 = 5;  //活三得分
	private static final int PRIORITY_3 = 4;  //活二得分
	private static final int PRIORITY_2 = 3;  //眠三得分
	private static final int PRIORITY_1 = 2;  //眠二得分
	
	private boolean firstDrop = true;

	private int[][] cpuWeight;
	private int[][] playerWeight;     int qq, rr;
	
	public void addListener() {
		MouseAdapter l = new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (blackWin || whiteWin) {
					return;
				}
				if (nextColor != Piece.B) {  //玩家黑子,电脑还未下
					return ;
				}
				int col = (e.getX() - OFFSET + PIECE_RADIUS) / BLOCK_SIZE;
				int row = (e.getY() - OFFSET + PIECE_RADIUS) / BLOCK_SIZE;
				if (col < 0 || row < 0 || col > COLS - 1 || row > ROWS - 1) {
					return;
				}

				board[col][row] = nextColor;qq = col; rr = row;
				checkWin();
				repaint();
				
				//电脑方回合
				nextColor = (nextColor == Piece.B) ? Piece.W : Piece.B;
				cpuAction();
				checkWin();
				repaint();
			}
		};
		this.addMouseListener(l);
	}
	
	//电脑落子(白子是电脑)
	public void cpuAction() {
		cpuWeight = countWeight(Piece.W);
		playerWeight = countWeight(Piece.B);
		
		int cpuMaxScore = -1;
		int cpuMaxSocreI = -1;
		int cpuMaxSocreJ = -1;
		for (int i = 0; i < cpuWeight.length; i++) {
			for (int j = 0; j < cpuWeight[i].length; j++) {
				if (cpuWeight[i][j] > cpuMaxScore) {
					cpuMaxScore = cpuWeight[i][j];
					cpuMaxSocreI = i;
					cpuMaxSocreJ = j;
				}
			}
		}
		List<Integer> cpuMaxI = new ArrayList<>();  //记录所有最高分位置的i
		cpuMaxI.add(cpuMaxSocreI);
		List<Integer> cpuMaxJ = new ArrayList<>();  //记录所有最高分位置的j
		cpuMaxJ.add(cpuMaxSocreJ);
		for (int i = 0; i < cpuWeight.length; i++) {
			for (int j = 0; j < cpuWeight[i].length; j++) {
				if (i == cpuMaxSocreI && j == cpuMaxSocreJ) {
					continue;
				}
				
				if (cpuWeight[i][j] == cpuMaxScore) {
					cpuMaxI.add(i);
					cpuMaxJ.add(j);
				}
			}
		}
		
		int playerMaxScore = -1;
		int playerMaxSocreI = -1;
		int playerMaxSocreJ = -1;
		for (int i = 0; i < playerWeight.length; i++) {
			for (int j = 0; j < playerWeight[i].length; j++) {
				if (playerWeight[i][j] > playerMaxScore) {
					playerMaxScore = playerWeight[i][j];
					playerMaxSocreI = i;
					playerMaxSocreJ = j;
				}
			}
		}
		List<Integer> playerMaxI = new ArrayList<>();  //记录所有最高分位置的i
		playerMaxI.add(playerMaxSocreI);
		List<Integer> playerMaxJ = new ArrayList<>();  //记录所有最高分位置的j
		playerMaxJ.add(playerMaxSocreJ);System.out.println(playerMaxI);System.out.println(playerMaxJ);
		for (int i = 0; i < playerWeight.length; i++) {
			for (int j = 0; j < playerWeight[i].length; j++) {
				if (i == playerMaxSocreI && j == playerMaxSocreJ) {
					continue;
				}
				
				if (playerWeight[i][j] == playerMaxScore) {
					playerMaxI.add(i);
					playerMaxJ.add(j);
				}
			}
		}
		
		if (cpuMaxScore >= playerMaxScore) {  //进攻
			//有多个cpu最高分位置, 下其中玩家最高分位置
			if (cpuMaxI.size() > 1) {
				int maxI = 0;
				int maxJ = 0;
				int maxScore = 0;
				for (int i : cpuMaxI) {
					for (int j : cpuMaxJ) {
						maxI = i;
						maxJ = j;
						if (playerWeight[i][j] > maxScore) {
							maxScore = playerWeight[i][j];
							maxI = i;
							maxJ = j;
						}
					}
				}
				board[maxI][maxJ] = nextColor;
			} else {
				board[cpuMaxSocreI][cpuMaxSocreJ] = nextColor;
			}
		} else {  //防守
			//有多个玩家最高分位置, 下其中cpu最高分位置
			if (playerMaxI.size() > 1) {
				int maxI = 0;
				int maxJ = 0;
				int maxScore = 0;
				for (int i : playerMaxI) {
					for (int j : playerMaxJ) {
						maxI = i;
						maxJ = j;
						if (cpuWeight[i][j] > maxScore) {
							maxScore = cpuWeight[i][j];
							maxI = i;
							maxJ = j;
						}
					}
				}
				board[maxI][maxJ] = nextColor;System.out.println("呵呵col: " + maxI + ", row: " + maxJ);
			} else {
				board[playerMaxSocreI][playerMaxSocreJ] = nextColor;System.out.println("哈哈col: " + playerMaxSocreI + ", row: " + playerMaxSocreJ);
			}
		}
		
		nextColor = (nextColor == Piece.B) ? Piece.W : Piece.B;
	}

	public int[][] countWeight(Piece confirmColor) {System.out.println("qq: " + qq + "rr: " + rr + ", clor: " + board[qq][rr]);
		int[][] weight = new int[COLS][ROWS]; // 所有位置的权值

		// 遍历棋盘计算权值
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[i].length; j++) {
				if (!firstDrop && board[i][j] != null) {
					continue;
				}

				// 活四_●●●●_左边那颗
				// 1.横向
				if (!(i + 5 > COLS - 1)) {
					if (board[i + 1][j] == confirmColor && board[i + 2][j] == confirmColor && board[i + 3][j] == confirmColor
							&& board[i + 4][j] == confirmColor && board[i + 5][j] == null) {
						weight[i][j] += PRIORITY_6;
					}
				}
				//2.纵向
				if (!(j + 5 > ROWS - 1)) {
					if (board[i][j + 1] == confirmColor && board[i][j + 2] == confirmColor
							&& board[i][j + 3] == confirmColor && board[i][j + 4] == confirmColor && board[i][j + 5] == null) {
						weight[i][j] += PRIORITY_6;
					}
				}
				//3.左上至右下
				if (!(i + 5 > COLS - 1) && !(j + 5 > ROWS - 1)) {
					if (board[i + 1][j + 1] == confirmColor && board[i + 2][j + 2] == confirmColor
							&& board[i + 3][j + 3] == confirmColor && board[i + 4][j + 4] == confirmColor && board[i + 5][j + 5] == null) {
						weight[i][j] += PRIORITY_6;
					}
				}
				//4.右上至左下
				if (!(i - 5 < 0) && !(j + 5 > ROWS - 1)) {
					if (board[i - 1][j + 1] == confirmColor && board[i - 2][j + 2] == confirmColor
							&& board[i - 3][j + 3] == confirmColor && board[i - 4][j + 4] == confirmColor && board[i - 5][j + 5] == null) {
						weight[i][j] += PRIORITY_6;
					}
				}
				
				// 活四_●●●●_右边那颗
				// 1.横向
				if (!(i - 5 < 0)) {
					if (board[i - 1][j] == confirmColor && board[i - 2][j] == confirmColor && board[i - 3][j] == confirmColor
							&& board[i - 4][j] == confirmColor && board[i - 5][j] == null) {
						weight[i][j] += PRIORITY_6;
					}
				}
				//2.纵向
				if (!(j - 5 < 0)) {
					if (board[i][j - 1] == confirmColor && board[i][j - 2] == confirmColor && board[i][j - 3] == confirmColor
							&& board[i][j - 4] == confirmColor && board[i][j - 5] == null) {
						weight[i][j] += PRIORITY_6;
					}
				}
				//3.右下至左上
				if (!(i - 5 < 0) && !(j - 5 < 0)) {
					if (board[i - 1][j - 1] == confirmColor && board[i - 2][j - 2] == confirmColor
							&& board[i - 3][j - 3] == confirmColor && board[i - 4][j - 4] == confirmColor && board[i - 5][j - 5] == null) {
						weight[i][j] += PRIORITY_6;
					}
				}
				//4.左下至右上
				if (!(i + 5 > COLS - 1) && !(j - 5 < 0)) {
					if (board[i + 1][j - 1] == confirmColor && board[i + 2][j - 2] == confirmColor
							&& board[i + 3][j - 3] == confirmColor && board[i + 4][j - 4] == confirmColor && board[i + 5][j - 5] == null) {
						weight[i][j] += PRIORITY_6;
					}
				}
				
				Piece anotherColor = (confirmColor == Piece.B) ? Piece.W : Piece.B;
				//冲四_●●●●○
				// 1.横向
				if (!(i + 5 > COLS - 1)) {
					if (board[i + 1][j] == confirmColor && board[i + 2][j] == confirmColor && board[i + 3][j] == confirmColor
							&& board[i + 4][j] == confirmColor && board[i + 5][j] == anotherColor) {
						weight[i][j] += PRIORITY_5;
					}
				}
				//2.纵向
				if (!(j + 5 > ROWS - 1)) {
					if (board[i][j + 1] == confirmColor && board[i][j + 2] == confirmColor && board[i][j + 3] == confirmColor
							&& board[i][j + 4] == confirmColor && board[i][j + 5] == anotherColor) {
						weight[i][j] += PRIORITY_5;
					}
				}
				//3.左上至右下
				if (!(i + 5 > COLS - 1) && !(j + 5 > ROWS - 1)) {
					if (board[i + 1][j + 1] == confirmColor && board[i + 2][j + 2] == confirmColor
							&& board[i + 3][j + 3] == confirmColor && board[i + 4][j + 4] == confirmColor && board[i + 5][j + 5] == anotherColor) {
						weight[i][j] += PRIORITY_5;
					}
				}
				//4.右上至左下
				if (!(i - 5 < 0) && !(j + 5 > ROWS - 1)) {
					if (board[i - 1][j + 1] == confirmColor && board[i - 2][j + 2] == confirmColor
							&& board[i - 3][j + 3] == confirmColor && board[i - 4][j + 4] == confirmColor && board[i - 5][j + 5] == anotherColor) {
						weight[i][j] += PRIORITY_5;
					}
				}
				
				//冲四○●●●●_
				// 1.横向
				if (!(i - 5 < 0)) {
					if (board[i - 1][j] == confirmColor && board[i - 2][j] == confirmColor && board[i - 3][j] == confirmColor
							&& board[i - 4][j] == confirmColor && board[i - 5][j] == anotherColor) {
						weight[i][j] += PRIORITY_5;
					}
				}
				//2.纵向
				if (!(j - 5 < 0)) {
					if (board[i][j - 1] == confirmColor && board[i][j - 2] == confirmColor && board[i][j - 3] == confirmColor
							&& board[i][j - 4] == confirmColor && board[i][j - 5] == anotherColor) {
						weight[i][j] += PRIORITY_5;
					}
				}
				//3.右下至左上
				if (!(i - 5 < 0) && !(j - 5 < 0)) {
					if (board[i - 1][j - 1] == confirmColor && board[i - 2][j - 2] == confirmColor
							&& board[i - 3][j - 3] == confirmColor && board[i - 4][j - 4] == confirmColor && board[i - 5][j - 5] == anotherColor) {
						weight[i][j] += PRIORITY_5;
					}
				}
				//4.左下至右上
				if (!(i + 5 > COLS - 1) && !(j - 5 < 0)) {
					if (board[i + 1][j - 1] == confirmColor && board[i + 2][j - 2] == confirmColor
							&& board[i + 3][j - 3] == confirmColor && board[i + 4][j - 4] == confirmColor && board[i + 5][j - 5] == anotherColor) {
						weight[i][j] += PRIORITY_5;
					}
				}
				
				//冲四●_●●●
				// 1.横向
				if (!(i - 1 < 0) && !(i + 3 > COLS - 1)) {
					if (board[i - 1][j] == confirmColor && board[i + 1][j] == confirmColor && board[i + 2][j] == confirmColor && board[i + 3][j] == confirmColor) {
						weight[i][j] += PRIORITY_5;
					}
				}
				//2.纵向
				if (!(j - 1 < 0) && !(j + 3 > ROWS - 1)) {
					if (board[i][j - 1] == confirmColor && board[i][j + 1] == confirmColor && board[i][j + 2] == confirmColor && board[i][j + 3] == confirmColor) {
						weight[i][j] += PRIORITY_5;
					}
				}
				//3.左上至右下
				if ((!(i - 1 < 0) && !(i + 3 > COLS - 1)) && (!(j - 1 < 0) && !(j + 3 > ROWS - 1))) {
					if (board[i - 1][j - 1] == confirmColor && board[i + 1][j + 1] == confirmColor
							&& board[i + 2][j + 2] == confirmColor && board[i + 3][j + 3] == confirmColor) {
						weight[i][j] += PRIORITY_5;
					}
				}
				//4.右上至左下
				if ((!(i + 1 > COLS - 1) && !(i - 3 < 0)) && (!(j - 1 < 0) && !(j + 3 > ROWS - 1))) {
					if (board[i + 1][j - 1] == confirmColor && board[i - 1][j + 1] == confirmColor
							&& board[i - 2][j + 2] == confirmColor && board[i - 3][j + 3] == confirmColor) {
						weight[i][j] += PRIORITY_5;
					}
				}
				
				//冲四●●●_●
				// 1.横向
				if (!(i + 1 > COLS - 1) && !(i - 3 < 0)) {
					if (board[i + 1][j] == confirmColor && board[i - 1][j] == confirmColor
							&& board[i - 2][j] == confirmColor && board[i - 3][j] == confirmColor) {
						weight[i][j] += PRIORITY_5;
					}
				}
				//2.纵向
				if (!(j + 1 > ROWS - 1) && !(j - 3 < 0)) {
					if (board[i][j + 1] == confirmColor && board[i][j - 1] == confirmColor
							&& board[i][j - 2] == confirmColor && board[i][j - 3] == confirmColor) {
						weight[i][j] += PRIORITY_5;
					}
				}
				//3.右下至左上
				if ((!(i + 1 > COLS - 1) && !(i - 3 < 0)) && (!(j + 1 > ROWS - 1) && !(j - 3 < 0))) {
					if (board[i + 1][j + 1] == confirmColor && board[i - 1][j - 1] == confirmColor
							&& board[i - 2][j - 2] == confirmColor && board[i - 3][j - 3] == confirmColor) {
						weight[i][j] += PRIORITY_5;
					}
				}
				//4.左下至右上
				if ((!(i - 1 < 0) && (!(i + 3 > COLS - 1))) && (!(j + 1 > ROWS - 1) && !(j - 3 < 0))) {
					if (board[i - 1][j + 1] == confirmColor && board[i + 1][j - 1] == confirmColor
							&& board[i + 2][j - 2] == confirmColor && board[i + 3][j - 3] == confirmColor) {
						weight[i][j] += PRIORITY_5;
					}
				}
				
				//冲四●●_●●
				// 1.横向
				if (!(i - 2 < 0) && !(i + 2 > COLS - 1)) {
					if (board[i - 2][j] == confirmColor && board[i - 1][j] == confirmColor && board[i + 1][j] == confirmColor && board[i + 2][j] == confirmColor) {
						weight[i][j] += PRIORITY_5;
					}
				}
				//2.纵向
				if (!(j - 2 < 0) && !(j + 2 > ROWS - 1)) {
					if (board[i][j - 2] == confirmColor && board[i][j - 1] == confirmColor && board[i][j + 1] == confirmColor && board[i][j + 2] == confirmColor) {
						weight[i][j] += PRIORITY_5;
					}
				}
				//3.左上至右下
				if ((!(i - 2 < 0) && !(i + 2 > COLS - 1)) && (!(j - 2 < 0) && !(j + 2 > ROWS - 1))) {
					if (board[i - 2][j - 2] == confirmColor && board[i - 1][j - 1] == confirmColor
							&& board[i + 1][j + 1] == confirmColor && board[i + 2][j + 2] == confirmColor) {
						weight[i][j] += PRIORITY_5;
					}
				}
				//4.右上至左下
				if ((!(i + 2 > COLS - 1) && !(i - 2 < 0)) && (!(j - 2 < 0) && !(j + 2 > ROWS - 1))) {
					if (board[i + 2][j - 2] == confirmColor && board[i + 1][j - 1] == confirmColor
							&& board[i - 1][j + 1] == confirmColor && board[i - 2][j + 2] == confirmColor) {
						weight[i][j] += PRIORITY_5;
					}
				}
				
				// 活三_●●●_左边那颗
				// 1.横向
				if (!(i + 4 > COLS - 1)) {
					if (board[i + 1][j] == confirmColor && board[i + 2][j] == confirmColor && board[i + 3][j] == confirmColor
							&& board[i + 4][j] == null) {
						weight[i][j] += PRIORITY_4;
					}
				}
				//2.纵向
				if (!(j + 4 > ROWS - 1)) {
					if (board[i][j + 1] == confirmColor && board[i][j + 2] == confirmColor
							&& board[i][j + 3] == confirmColor && board[i][j + 4] == null) {
						weight[i][j] += PRIORITY_4;
					}
				}
				//3.左上至右下
				if (!(i + 4 > COLS - 1) && !(j + 4 > ROWS - 1)) {
					if (board[i + 1][j + 1] == confirmColor && board[i + 2][j + 2] == confirmColor
							&& board[i + 3][j + 3] == confirmColor && board[i + 4][j + 4] == null) {
						weight[i][j] += PRIORITY_4;
					}
				}
				//4.右上至左下
				if (!(i - 4 < 0) && !(j + 4 > ROWS - 1)) {
					if (board[i - 1][j + 1] == confirmColor && board[i - 2][j + 2] == confirmColor
							&& board[i - 3][j + 3] == confirmColor && board[i - 4][j + 4] == null) {
						weight[i][j] += PRIORITY_4;
					}
				}
				
				// 活三_●●●_右边那颗
				// 1.横向
				if (!(i - 4 < 0)) {
					if (board[i - 1][j] == confirmColor && board[i - 2][j] == confirmColor && board[i - 3][j] == confirmColor && board[i - 4][j] == null) {
						weight[i][j] += PRIORITY_4;
					}
				}
				//2.纵向
				if (!(j - 4 < 0)) {
					if (board[i][j - 1] == confirmColor && board[i][j - 2] == confirmColor && board[i][j - 3] == confirmColor && board[i][j - 4] == null) {
						weight[i][j] += PRIORITY_4;
					}
				}
				//3.右下至左上
				if (!(i - 4 < 0) && !(j - 4 < 0)) {
					if (board[i - 1][j - 1] == confirmColor && board[i - 2][j - 2] == confirmColor
							&& board[i - 3][j - 3] == confirmColor && board[i - 4][j - 4] == null) {
						weight[i][j] += PRIORITY_4;
					}
				}
				//4.左下至右上
				if (!(i + 4 > COLS - 1) && !(j - 4 < 0)) {
					if (board[i + 1][j - 1] == confirmColor && board[i + 2][j - 2] == confirmColor
							&& board[i + 3][j - 3] == confirmColor && board[i + 4][j - 4] == null) {
						weight[i][j] += PRIORITY_4;
					}
				}
				
				// 活三_●_●●_左边那颗
				// 1.横向
				if (!(i + 5 > COLS - 1)) {
					if (board[i + 1][j] == confirmColor && board[i + 2][j] == null && board[i + 3][j] == confirmColor
							&& board[i + 4][j] == confirmColor && board[i + 5][j] == null) {
						weight[i][j] += PRIORITY_4;
					}
				}
				//2.纵向
				if (!(j + 5 > ROWS - 1)) {
					if (board[i][j + 1] == confirmColor && board[i][j + 2] == null && board[i][j + 3] == confirmColor
							&& board[i][j + 4] == confirmColor && board[i][j + 5] == null) {
						weight[i][j] += PRIORITY_4;
					}
				}
				//3.左上至右下
				if (!(i + 5 > COLS - 1) && !(j + 5 > ROWS - 1)) {
					if (board[i + 1][j + 1] == confirmColor && board[i + 2][j + 2] == null && board[i + 3][j + 3] == confirmColor
							&& board[i + 4][j + 4] == confirmColor && board[i + 5][j + 5] == null) {
						weight[i][j] += PRIORITY_4;
					}
				}
				//4.右上至左下
				if (!(i - 5 < 0) && !(j + 5 > ROWS - 1)) {
					if (board[i - 1][j + 1] == confirmColor && board[i - 2][j + 2] == null
							&& board[i - 3][j + 3] == confirmColor && board[i - 4][j + 4] == confirmColor && board[i - 5][j + 5] == null) {
						weight[i][j] += PRIORITY_4;
					}
				}
				
				// 活三_●_●●_中间那颗
				// 1.横向
				if (!(i - 2 < 0) && !(i + 3 > COLS - 1)) {
					if (board[i - 2][j] == null && board[i - 1][j] == confirmColor && board[i + 1][j] == confirmColor
							&& board[i + 2][j] == confirmColor && board[i + 3][j] == null) {
						weight[i][j] += PRIORITY_4;
					}
				}
				//2.纵向
				if (!(j - 2 < 0) && !(j + 3 > ROWS - 1)) {
					if (board[i][j - 2] == null && board[i][j - 1] == confirmColor && board[i][j + 1] == confirmColor
							&& board[i][j + 2] == confirmColor && board[i][j + 3] == null) {
						weight[i][j] += PRIORITY_4;
					}
				}
				//3.左上至右下
				if ((!(i - 2 < 0) && !(i + 3 > COLS - 1)) && (!(j - 2 < 0) && !(j + 3 > ROWS - 1))) {
					if (board[i - 2][j - 2] == null && board[i - 1][j - 1] == confirmColor && board[i + 1][j + 1] == confirmColor
							&& board[i + 2][j + 2] == confirmColor && board[i + 3][j + 3] == null) {
						weight[i][j] += PRIORITY_4;
					}
				}
				//4.右上至左下
				if ((!(i + 2 > COLS - 1) && !(i - 3 < 0)) && (!(j - 2 < 0) && !(j + 3 > ROWS - 1))) {
					if (board[i + 2][j - 2] == null && board[i + 1][j - 1] == confirmColor && board[i - 1][j + 1] == confirmColor
							&& board[i - 2][j + 2] == confirmColor && board[i - 3][j + 3] == null) {
						weight[i][j] += PRIORITY_4;
					}
				}
				
				// 活三_●_●●_右边那颗
				// 1.横向
				if (!(i - 5 < 0)) {
					if (board[i - 1][j] == confirmColor && board[i - 2][j] == confirmColor && board[i - 3][j] == null
							&& board[i - 4][j] == confirmColor && board[i - 5][j] == null) {
						weight[i][j] += PRIORITY_4;
					}
				}
				//2.纵向
				if (!(j - 5 < 0)) {
					if (board[i][j - 1] == confirmColor && board[i][j - 2] == confirmColor && board[i][j - 3] == null
							&& board[i][j - 4] == confirmColor && board[i][j - 5] == null) {
						weight[i][j] += PRIORITY_4;
					}
				}
				//3.右下至左上
				if (!(i - 5 < 0) && !(j - 5 < 0)) {
					if (board[i - 1][j - 1] == confirmColor && board[i - 2][j - 2] == confirmColor
							&& board[i - 3][j - 3] == null && board[i - 4][j - 4] == confirmColor && board[i - 5][j - 5] == null) {
						weight[i][j] += PRIORITY_4;
					}
				}
				//4.左下至右上
				if (!(i + 5 > COLS - 1) && !(j - 5 < 0)) {
					if (board[i + 1][j - 1] == confirmColor && board[i + 2][j - 2] == confirmColor
							&& board[i + 3][j - 3] == null && board[i + 4][j - 4] == confirmColor && board[i + 5][j - 5] == null) {
						weight[i][j] += PRIORITY_4;
					}
				}
				
				// 活三_●●_●_左边那颗
				// 1.横向
				if (!(i + 5 > COLS - 1)) {
					if (board[i + 1][j] == confirmColor && board[i + 2][j] == confirmColor && board[i + 3][j] == null
							&& board[i + 4][j] == confirmColor && board[i + 5][j] == null) {
						weight[i][j] += PRIORITY_4;
					}
				}
				//2.纵向
				if (!(j + 5 > ROWS - 1)) {
					if (board[i][j + 1] == confirmColor && board[i][j + 2] == confirmColor && board[i][j + 3] == null
							&& board[i][j + 4] == confirmColor && board[i][j + 5] == null) {
						weight[i][j] += PRIORITY_4;
					}
				}
				//3.左上至右下
				if (!(i + 5 > COLS - 1) && !(j + 5 > ROWS - 1)) {
					if (board[i + 1][j + 1] == confirmColor && board[i + 2][j + 2] == confirmColor
							&& board[i + 3][j + 3] == null && board[i + 4][j + 4] == confirmColor && board[i + 5][j + 5] == null) {
						weight[i][j] += PRIORITY_4;
					}
				}
				//4.右上至左下
				if (!(i - 5 < 0) && !(j + 5 > ROWS - 1)) {
					if (board[i - 1][j + 1] == confirmColor && board[i - 2][j + 2] == confirmColor
							&& board[i - 3][j + 3] == null && board[i - 4][j + 4] == confirmColor && board[i - 5][j + 5] == null) {
						weight[i][j] += PRIORITY_4;
					}
				}
				
				// 活三_●●_●_中间那颗
				// 1.横向
				if (!(i - 3 < 0) && !(i + 2 > COLS - 1)) {
					if (board[i - 3][j] == null && board[i - 2][j] == confirmColor && board[i - 1][j] == confirmColor
							&& board[i + 1][j] == confirmColor && board[i + 2][j] == null) {
						weight[i][j] += PRIORITY_4;
					}
				}
				//2.纵向
				if (!(j - 3 < 0) && !(j + 2 > ROWS - 1)) {
					if (board[i][j - 3] == null && board[i][j - 2] == confirmColor && board[i][j - 1] == confirmColor
							&& board[i][j + 1] == confirmColor && board[i][j + 2] == null) {
						weight[i][j] += PRIORITY_4;
					}
				}
				//3.左上至右下
				if ((!(i - 3 < 0) && !(i + 2 > COLS - 1)) && (!(j - 3 < 0) && !(j + 2 > ROWS - 1))) {
					if (board[i - 3][j - 3] == null && board[i - 2][j - 2] == confirmColor && board[i - 1][j - 1] == confirmColor
							&& board[i + 1][j + 1] == confirmColor && board[i + 2][j + 2] == null) {
						weight[i][j] += PRIORITY_4;
					}
				}
				//4.右上至左下
				if ((!(i + 3 > COLS - 1) && !(i - 2 < 0)) && (!(j - 3 < 0) && !(j + 2 > ROWS - 1))) {
					if (board[i + 3][j - 3] == null && board[i + 2][j - 2] == confirmColor && board[i + 1][j - 1] == confirmColor
							&& board[i - 1][j + 1] == confirmColor && board[i - 2][j + 2] == null) {
						weight[i][j] += PRIORITY_4;
					}
				}
				
				// 活三_●●_●_右边那颗
				// 1.横向
				if (!(i - 5 < 0)) {
					if (board[i - 1][j] == confirmColor && board[i - 2][j] == null && board[i - 3][j] == confirmColor
							&& board[i - 4][j] == confirmColor && board[i - 5][j] == null) {
						weight[i][j] += PRIORITY_4;
					}
				}
				//2.纵向
				if (!(j - 5 < 0)) {
					if (board[i][j - 1] == confirmColor && board[i][j - 2] == null && board[i][j - 3] == confirmColor
							&& board[i][j - 4] == confirmColor && board[i][j - 5] == null) {
						weight[i][j] += PRIORITY_4;
					}
				}
				//3.右下至左上
				if (!(i - 5 < 0) && !(j - 5 < 0)) {
					if (board[i - 1][j - 1] == confirmColor && board[i - 2][j - 2] == null && board[i - 3][j - 3] == confirmColor
							&& board[i - 4][j - 4] == confirmColor && board[i - 5][j - 5] == null) {
						weight[i][j] += PRIORITY_4;
					}
				}
				//4.左下至右上
				if (!(i + 5 > COLS - 1) && !(j - 5 < 0)) {
					if (board[i + 1][j - 1] == confirmColor && board[i + 2][j - 2] == null && board[i + 3][j - 3] == confirmColor
							&& board[i + 4][j - 4] == confirmColor && board[i + 5][j - 5] == null) {
						weight[i][j] += PRIORITY_4;
					}
				}
				
				// 活二__●●__最左边那颗
				// 1.横向
				if (!(i + 5 > COLS - 1)) {
					if (board[i + 1][j] == null && board[i + 2][j] == confirmColor && board[i + 3][j] == confirmColor
							&& board[i + 4][j] == null && board[i + 5][j] == null) {
						weight[i][j] += PRIORITY_3;
					}
				}
				//2.纵向
				if (!(j + 5 > ROWS - 1)) {
					if (board[i][j + 1] == null && board[i][j + 2] == confirmColor && board[i][j + 3] == confirmColor
							&& board[i][j + 4] == null && board[i][j + 5] == null) {
						weight[i][j] += PRIORITY_3;
					}
				}
				//3.左上至右下
				if (!(i + 5 > COLS - 1) && !(j + 5 > ROWS - 1)) {
					if (board[i + 1][j + 1] == null && board[i + 2][j + 2] == confirmColor
							&& board[i + 3][j + 3] == confirmColor && board[i + 4][j + 4] == null && board[i + 5][j + 5] == null) {
						weight[i][j] += PRIORITY_3;
					}
				}
				//4.右上至左下
				if (!(i - 5 < 0) && !(j + 5 > ROWS - 1)) {
					if (board[i - 1][j + 1] == null && board[i - 2][j + 2] == confirmColor
							&& board[i - 3][j + 3] == confirmColor && board[i - 4][j + 4] == null && board[i - 5][j + 5] == null) {
						weight[i][j] += PRIORITY_3;
					}
				}
				
				// 活二__●●__左二那颗
				// 1.横向
				if (!(i - 1 < 0) && !(i + 4 > COLS - 1)) {
					if (board[i - 1][j] == null && board[i + 1][j] == confirmColor && board[i + 2][j] == confirmColor
							&& board[i + 3][j] == null && board[i + 4][j] == null) {
						weight[i][j] += PRIORITY_3;
					}
				}
				//2.纵向
				if (!(j - 1 < 0) && !(j + 4 > ROWS - 1)) {
					if (board[i][j - 1] == null && board[i][j + 1] == confirmColor
							&& board[i][j + 2] == confirmColor && board[i][j + 3] == null && board[i][j + 4] == null) {
						weight[i][j] += PRIORITY_3;
					}
				}
				//3.左上至右下
				if ((!(i - 1 < 0) && !(i + 4 > COLS - 1)) && (!(j - 1 < 0) && !(j + 4 > ROWS - 1))) {
					if (board[i - 1][j - 1] == null && board[i + 1][j + 1] == confirmColor
							&& board[i + 2][j + 2] == confirmColor && board[i + 3][j + 3] == null && board[i + 4][j + 4] == null) {
						weight[i][j] += PRIORITY_3;
					}
				}
				//4.右上至左下
				if ((!(i + 1 > COLS - 1) && !(i - 4 < 0)) && (!(j - 1 < 0) && !(j + 4 > ROWS - 1))) {
					if (board[i + 1][j - 1] == null && board[i - 1][j + 1] == confirmColor
							&& board[i - 2][j + 2] == confirmColor && board[i - 3][j + 3] == null && board[i - 4][j + 4] == null) {
						weight[i][j] += PRIORITY_3;
					}
				}
				
				// 活二__●●__右二那颗
				// 1.横向
				if (!(i - 4 < 0) && !(i + 1 > COLS - 1)) {
					if (board[i - 4][j] == null && board[i - 3][j] == null && board[i - 2][j] == confirmColor
							&& board[i - 1][j] == confirmColor && board[i + 1][j] == null) {
						weight[i][j] += PRIORITY_3;
					}
				}
				//2.纵向
				if (!(j - 4 < 0) && !(j + 1 > ROWS - 1)) {
					if (board[i][j - 4] == null && board[i][j - 3] == null && board[i][j - 2] == confirmColor
							&& board[i][j - 1] == confirmColor && board[i][j + 1] == null) {
						weight[i][j] += PRIORITY_3;
					}
				}
				//3.左上至右下
				if ((!(i - 4 < 0) && !(i + 1 > COLS - 1)) && (!(j - 4 < 0) && !(j + 1 > ROWS - 1))) {
					if (board[i - 4][j - 4] == null && board[i - 3][j - 3] == null
							&& board[i - 2][j - 2] == confirmColor && board[i - 1][j - 1] == confirmColor && board[i + 1][j + 1] == null) {
						weight[i][j] += PRIORITY_3;
					}
				}
				//4.右上至左下
				if ((!(i + 4 > COLS - 1) && !(i - 1 < 0)) && (!(j - 4 < 0) && !(j + 1 > ROWS - 1))) {
					if (board[i + 4][j - 4] == null && board[i + 3][j - 3] == null
							&& board[i + 2][j - 2] == confirmColor && board[i + 1][j - 1] == confirmColor && board[i - 1][j + 1] == null) {
						weight[i][j] += PRIORITY_3;
					}
				}
				
				// 活二__●●__最右边那颗
				// 1.横向
				if (!(i - 5 < 0)) {
					if (board[i - 1][j] == null && board[i - 2][j] == confirmColor && board[i - 3][j] == confirmColor
							&& board[i - 4][j] == null && board[i - 5][j] == null) {
						weight[i][j] += PRIORITY_3;
					}
				}
				//2.纵向
				if (!(j - 5 < 0)) {
					if (board[i][j - 1] == null && board[i][j - 2] == confirmColor
							&& board[i][j - 3] == confirmColor && board[i][j - 4] == null && board[i][j - 5] == null) {
						weight[i][j] += PRIORITY_3;
					}
				}
				//3.左上至右下
				if (!(i - 5 < 0) && !(j - 5 < 0)) {
					if (board[i - 1][j - 1] == null && board[i - 2][j - 2] == confirmColor
							&& board[i - 3][j - 3] == confirmColor && board[i - 4][j - 4] == null && board[i - 5][j - 5] == null) {
						weight[i][j] += PRIORITY_3;
					}
				}
				//4.右上至左下
				if (!(i + 5 > COLS - 1) && !(j - 5 < 0)) {
					if (board[i + 1][j - 1] == null && board[i + 2][j - 2] == confirmColor
							&& board[i + 3][j - 3] == confirmColor && board[i + 4][j - 4] == null && board[i + 5][j - 5] == null) {
						weight[i][j] += PRIORITY_3;
					}
				}
				
				// 活二_●_●_最左边那颗
				// 1.横向
				if (!(i + 4 > COLS - 1)) {
					if (board[i + 1][j] == confirmColor && board[i + 2][j] == null && board[i + 3][j] == confirmColor && board[i + 4][j] == null) {
						weight[i][j] += PRIORITY_3;
					}
				}
				//2.纵向
				if (!(j + 4 > ROWS - 1)) {
					if (board[i][j + 1] == confirmColor && board[i][j + 2] == null && board[i][j + 3] == confirmColor && board[i][j + 4] == null) {
						weight[i][j] += PRIORITY_3;
					}
				}
				//3.左上至右下
				if (!(i + 4 > COLS - 1) && !(j + 4 > ROWS - 1)) {
					if (board[i + 1][j + 1] == confirmColor && board[i + 2][j + 2] == null
							&& board[i + 3][j + 3] == confirmColor && board[i + 4][j + 4] == null) {
						weight[i][j] += PRIORITY_3;
					}
				}
				//4.右上至左下
				if (!(i - 4 < 0) && !(j + 4 > ROWS - 1)) {
					if (board[i - 1][j + 1] == confirmColor && board[i - 2][j + 2] == null
							&& board[i - 3][j + 3] == confirmColor && board[i - 4][j + 4] == null) {
						weight[i][j] += PRIORITY_3;
					}
				}
				
				// 活二_●_●_中间那颗
				// 1.横向
				if (!(i - 2 < 0) && !(i + 2 > COLS - 1)) {
					if (board[i - 2][j] == null && board[i - 1][j] == confirmColor && board[i + 1][j] == confirmColor && board[i + 2][j] == null) {
						weight[i][j] += PRIORITY_3;
					}
				}
				//2.纵向
				if (!(j - 2 < 0) && !(j + 2 > ROWS - 1)) {
					if (board[i][j - 2] == null && board[i][j - 1] == confirmColor && board[i][j + 1] == confirmColor && board[i][j + 2] == null) {
						weight[i][j] += PRIORITY_3;
					}
				}
				//3.左上至右下
				if ((!(i - 2 < 0) && !(i + 2 > COLS - 1)) && (!(j - 2 < 0) && !(j + 2 > ROWS - 1))) {
					if (board[i - 2][j - 2] == null && board[i - 1][j - 1] == confirmColor
							&& board[i + 1][j + 1] == confirmColor && board[i + 2][j + 2] == null) {
						weight[i][j] += PRIORITY_3;
					}
				}
				//4.右上至左下
				if ((!(i + 2 > COLS - 1) && !(i - 2 < 0)) && (!(j - 2 < 0) && !(j + 2 > ROWS - 1))) {
					if (board[i + 2][j - 2] == null && board[i + 1][j - 1] == confirmColor
							&& board[i - 1][j + 1] == confirmColor && board[i - 2][j + 2] == null) {
						weight[i][j] += PRIORITY_3;
					}
				}
				
				// 活二_●_●_最右边那颗
				// 1.横向
				if (!(i - 4 < 0)) {
					if (board[i - 1][j] == confirmColor && board[i - 2][j] == null && board[i - 3][j] == confirmColor && board[i - 4][j] == null) {
						weight[i][j] += PRIORITY_3;
					}
				}
				//2.纵向
				if (!(j - 4 < 0)) {
					if (board[i][j - 1] == confirmColor && board[i][j - 2] == null && board[i][j - 3] == confirmColor && board[i][j - 4] == null) {
						weight[i][j] += PRIORITY_3;
					}
				}
				//3.左上至右下
				if (!(i - 4 < 0) && !(j - 4 < 0)) {
					if (board[i - 1][j - 1] == confirmColor && board[i - 2][j - 2] == null
							&& board[i - 3][j - 3] == confirmColor && board[i - 4][j - 4] == null) {
						weight[i][j] += PRIORITY_3;
					}
				}
				//4.右上至左下
				if (!(i + 4 > COLS - 1) && !(j - 4 < 0)) {
					if (board[i + 1][j - 1] == confirmColor && board[i + 2][j - 2] == null
							&& board[i + 3][j - 3] == confirmColor && board[i + 4][j - 4] == null) {
						weight[i][j] += PRIORITY_3;
					}
				}
				
				// 活二_●__●_最左边那颗
				// 1.横向
				if (!(i + 5 > COLS - 1)) {
					if (board[i + 1][j] == confirmColor && board[i + 2][j] == null && board[i + 3][j] == null
							&& board[i + 4][j] == confirmColor && board[i + 5][j] == null) {
						weight[i][j] += PRIORITY_3;
					}
				}
				//2.纵向
				if (!(j + 5 > ROWS - 1)) {
					if (board[i][j + 1] == confirmColor && board[i][j + 2] == null && board[i][j + 3] == null
							&& board[i][j + 4] == confirmColor && board[i][j + 5] == null) {
						weight[i][j] += PRIORITY_3;
					}
				}
				//3.左上至右下
				if (!(i + 5 > COLS - 1) && !(j + 5 > ROWS - 1)) {
					if (board[i + 1][j + 1] == confirmColor && board[i + 2][j + 2] == null && board[i + 3][j + 3] == null
							&& board[i + 4][j + 4] == confirmColor && board[i + 5][j + 5] == null) {
						weight[i][j] += PRIORITY_3;
					}
				}
				//4.右上至左下
				if (!(i - 5 < 0) && !(j + 5 > ROWS - 1)) {
					if (board[i - 1][j + 1] == confirmColor && board[i - 2][j + 2] == null && board[i - 3][j + 3] == null
							&& board[i - 4][j + 4] == confirmColor && board[i - 5][j + 5] == null) {
						weight[i][j] += PRIORITY_3;
					}
				}
				
				// 活二_●__●_左二那颗
				// 1.横向
				if (!(i - 2 < 0) && !(i + 3 > COLS - 1)) {
					if (board[i - 2][j] == null && board[i - 1][j] == confirmColor && board[i + 1][j] == null
							&& board[i + 2][j] == confirmColor && board[i + 3][j] == null) {
						weight[i][j] += PRIORITY_3;
					}
				}
				//2.纵向
				if (!(j - 2 < 0) && !(j + 3 > ROWS - 1)) {
					if (board[i][j - 2] == null && board[i][j - 1] == confirmColor && board[i][j + 1] == null
							&& board[i][j + 2] == confirmColor && board[i][j + 3] == null) {
						weight[i][j] += PRIORITY_3;
					}
				}
				//3.左上至右下
				if ((!(i - 2 < 0) && !(i + 3 > COLS - 1)) && (!(j - 2 < 0) && !(j + 3 > ROWS - 1))) {
					if (board[i - 2][j - 2] == null && board[i - 1][j - 1] == confirmColor && board[i + 1][j + 1] == null
							&& board[i + 2][j + 2] == confirmColor && board[i + 3][j + 3] == null) {
						weight[i][j] += PRIORITY_3;
					}
				}
				//4.右上至左下
				if (!(i + 2 > COLS - 1) && (!(i - 3 < 0)) && (!(j - 2 < 0) && !(j + 3 > ROWS - 1))) {
					if (board[i + 2][j - 2] == null && board[i + 1][j - 1] == confirmColor && board[i - 1][j + 1] == null
							&& board[i - 2][j + 2] == confirmColor && board[i - 3][j + 3] == null) {
						weight[i][j] += PRIORITY_3;
					}
				}
				
				// 活二_●__●_右二那颗
				// 1.横向
				if (!(i - 3 < 0) && !(i + 2 > COLS - 1)) {
					if (board[i - 3][j] == null && board[i - 2][j] == confirmColor && board[i - 1][j] == null
							&& board[i + 1][j] == confirmColor && board[i + 2][j] == null) {
						weight[i][j] += PRIORITY_3;
					}
				}
				//2.纵向
				if (!(j - 3 < 0) && !(j + 2 > ROWS - 1)) {
					if (board[i][j - 3] == null && board[i][j - 2] == confirmColor && board[i][j - 1] == null
							&& board[i][j + 1] == confirmColor && board[i][j + 2] == null) {
						weight[i][j] += PRIORITY_3;
					}
				}
				//3.左上至右下
				if ((!(i - 3 < 0) && !(i + 2 > COLS - 1)) && (!(j - 3 < 0) && !(j + 2 > ROWS - 1))) {
					if (board[i - 3][j - 3] == null && board[i - 2][j - 2] == confirmColor && board[i - 1][j - 1] == null
							&& board[i + 1][j + 1] == confirmColor && board[i + 2][j + 2] == null) {
						weight[i][j] += PRIORITY_3;
					}
				}
				//4.右上至左下
				if ((!(i + 3 > COLS - 1)) && (!(i - 2 < 0)) && (!(j - 3 < 0) && !(j + 2 > ROWS - 1))) {
					if (board[i + 3][j - 3] == null && board[i + 2][j - 2] == confirmColor && board[i + 1][j - 1] == null
							&& board[i - 1][j + 1] == confirmColor && board[i - 2][j + 2] == null) {
						weight[i][j] += PRIORITY_3;
					}
				}
				
				// 活二_●__●_最右边那颗
				// 1.横向
				if (!(i - 5 < 0)) {
					if (board[i - 1][j] == confirmColor && board[i - 2][j] == null && board[i - 3][j] == null
							&& board[i - 4][j] == confirmColor && board[i - 5][j] == null) {
						weight[i][j] += PRIORITY_3;
					}
				}
				//2.纵向
				if (!(j - 5 < 0)) {
					if (board[i][j - 1] == confirmColor && board[i][j - 2] == null && board[i][j - 3] == null
							&& board[i][j - 4] == confirmColor && board[i][j - 5] == null) {
						weight[i][j] += PRIORITY_3;
					}
				}
				//3.左上至右下
				if (!(i - 5 < 0) && !(j - 5 < 0)) {
					if (board[i - 1][j - 1] == confirmColor && board[i - 2][j - 2] == null && board[i - 3][j - 3] == null
							&& board[i - 4][j - 4] == confirmColor && board[i - 5][j - 5] == null) {
						weight[i][j] += PRIORITY_3;
					}
				}
				//4.右上至左下
				if (!(i + 5 > COLS - 1) && !(j - 5 < 0)) {
					if (board[i + 1][j - 1] == confirmColor && board[i + 2][j - 2] == null && board[i + 3][j - 3] == null
							&& board[i + 4][j - 4] == confirmColor && board[i + 5][j - 5] == null) {
						weight[i][j] += PRIORITY_3;
					}
				}
				
				//眠三__●●●○ 左
				// 1.横向
				if (!(i + 5 > COLS - 1)) {
					if (board[i + 1][j] == null && board[i + 2][j] == confirmColor && board[i + 3][j] == confirmColor
							&& board[i + 4][j] == confirmColor && board[i + 5][j] == anotherColor) {
						weight[i][j] += PRIORITY_2;
					}
				}
				//2.纵向
				if (!(j + 5 > ROWS - 1)) {
					if (board[i][j + 1] == null && board[i][j + 2] == confirmColor && board[i][j + 3] == confirmColor
							&& board[i][j + 4] == confirmColor && board[i][j + 5] == anotherColor) {
						weight[i][j] += PRIORITY_2;
					}
				}
				//3.左上至右下
				if (!(i + 5 > COLS - 1) && !(j + 5 > ROWS - 1)) {
					if (board[i + 1][j + 1] == null && board[i + 2][j + 2] == confirmColor && board[i + 3][j + 3] == confirmColor
							&& board[i + 4][j + 4] == confirmColor && board[i + 5][j + 5] == anotherColor) {
						weight[i][j] += PRIORITY_2;
					}
				}
				//4.右上至左下
				if ((!(i - 5 < 0)) && !(j + 5 > ROWS - 1)) {
					if (board[i - 1][j + 1] == null && board[i - 2][j + 2] == confirmColor && board[i - 3][j + 3] == confirmColor
							&& board[i - 4][j + 4] == confirmColor && board[i - 5][j + 5] == anotherColor) {
						weight[i][j] += PRIORITY_2;
					}
				}
				
				//眠三__●●●○ 右
				// 1.横向
				if (!(i - 1 < 0) && !(i + 4 > COLS - 1)) {
					if (board[i - 1][j] == null && board[i + 1][j] == confirmColor && board[i + 2][j] == confirmColor
							&& board[i + 3][j] == confirmColor && board[i + 4][j] == anotherColor) {
						weight[i][j] += PRIORITY_2;
					}
				}
				//2.纵向
				if (!(j - 1 < 0) && !(j + 4 > ROWS - 1)) {
					if (board[i][j - 1] == null && board[i][j + 1] == confirmColor && board[i][j + 2] == confirmColor
							&& board[i][j + 3] == confirmColor && board[i][j + 4] == anotherColor) {
						weight[i][j] += PRIORITY_2;
					}
				}
				//3.左上至右下
				if ((!(i - 1 < 0) && !(i + 4 > COLS - 1)) && (!(j - 1 < 0) && !(j + 4 > ROWS - 1))) {
					if (board[i - 1][j - 1] == null && board[i + 1][j + 1] == confirmColor && board[i + 2][j + 2] == confirmColor
							&& board[i + 3][j + 3] == confirmColor && board[i + 4][j + 4] == anotherColor) {
						weight[i][j] += PRIORITY_2;
					}
				}
				//4.右上至左下
				if ((!(i + 1 > COLS - 1) && !(i - 4 < 0)) && (!(j - 1 < 0) && !(j + 4 > ROWS - 1))) {
					if (board[i + 1][j - 1] == null && board[i - 1][j + 1] == confirmColor && board[i - 2][j + 2] == confirmColor
							&& board[i - 3][j + 3] == confirmColor && board[i - 4][j + 4] == anotherColor) {
						weight[i][j] += PRIORITY_2;
					}
				}
				
				//眠三○●●●__ 左
				// 1.横向
				if (!(i - 4 < 0) && !(i + 1 > COLS - 1)) {
					if (board[i - 4][j] == anotherColor && board[i - 3][j] == confirmColor && board[i - 2][j] == confirmColor
							&& board[i - 1][j] == confirmColor && board[i + 1][j] == null) {
						weight[i][j] += PRIORITY_2;
					}
				}
				//2.纵向
				if (!(j - 4 < 0) && !(j + 1 > COLS - 1)) {
					if (board[i][j - 4] == anotherColor && board[i][j - 3] == confirmColor && board[i][j - 2] == confirmColor
							&& board[i][j - 1] == confirmColor && board[i][j + 1] == null) {
						weight[i][j] += PRIORITY_2;
					}
				}
				//3.左上至右下
				if (((!(i - 4 < 0)) && !(i + 1 > COLS - 1)) && (!(j - 4 < 0) && !(j + 1 > COLS - 1))) {
					if (board[i - 4][j - 4] == anotherColor && board[i - 3][j - 3] == confirmColor && board[i - 2][j - 2] == confirmColor
							&& board[i - 1][j - 1] == confirmColor && board[i + 1][j + 1] == null) {
						weight[i][j] += PRIORITY_2;
					}
				}
				//4.右上至左下
				if ((!(i + 4 > COLS - 1) && !(i - 1 < 0)) && (!(j - 4 < 0) && !(j + 1 > COLS - 1))) {
					if (board[i - 1][j + 1] == null && board[i + 1][j - 1] == confirmColor && board[i + 2][j - 2] == confirmColor
							&& board[i + 3][j - 3] == confirmColor && board[i + 4][j - 4] == anotherColor) {
						weight[i][j] += PRIORITY_2;
					}
				}
				
				//眠三○●●●__ 右
				// 1.横向
				if (!(i - 5 < 0)) {
					if (board[i - 1][j] == null && board[i - 2][j] == confirmColor && board[i - 3][j] == confirmColor
							&& board[i - 4][j] == confirmColor && board[i - 5][j] == anotherColor) {
						weight[i][j] += PRIORITY_2;
					}
				}
				//2.纵向
				if (!(j - 5 < 0)) {
					if (board[i][j - 1] == null && board[i][j - 2] == confirmColor && board[i][j - 3] == confirmColor
							&& board[i][j - 4] == confirmColor && board[i][j - 5] == anotherColor) {
						weight[i][j] += PRIORITY_2;
					}
				}
				//3.右下至左上
				if (!(i - 5 < 0) && !(j - 5 < 0)) {
					if (board[i - 1][j - 1] == null && board[i - 2][j - 2] == confirmColor && board[i - 3][j - 3] == confirmColor
							&& board[i - 4][j - 4] == confirmColor && board[i - 5][j - 5] == anotherColor) {
						weight[i][j] += PRIORITY_2;
					}
				}
				//4.左下至右上
				if (!(i + 5 > COLS - 1) && !(j - 5 < 0)) {
					if (board[i + 1][j - 1] == null && board[i + 2][j - 2] == confirmColor && board[i + 3][j - 3] == confirmColor
							&& board[i + 4][j - 4] == confirmColor && board[i + 5][j - 5] == anotherColor) {
						weight[i][j] += PRIORITY_2;
					}
				}
				
				//眠三_●_●●○左边那颗
				// 1.横向
				if (!(i + 5 > COLS - 1)) {
					if (board[i + 1][j] == confirmColor && board[i + 2][j] == null && board[i + 3][j] == confirmColor
							&& board[i + 4][j] == confirmColor && board[i + 5][j] == anotherColor) {
						weight[i][j] += PRIORITY_2;
					}
				}
				//2.纵向
				if (!(j + 5 > ROWS - 1)) {
					if (board[i][j + 1] == confirmColor && board[i][j + 2] == null && board[i][j + 3] == confirmColor
							&& board[i][j + 4] == confirmColor && board[i][j + 5] == anotherColor) {
						weight[i][j] += PRIORITY_2;
					}
				}
				//3.左上至右下
				if (!(i + 5 > COLS - 1) && !(j + 5 > ROWS - 1)) {
					if (board[i + 1][j + 1] == confirmColor && board[i + 2][j + 2] == null
							&& board[i + 3][j + 3] == confirmColor && board[i + 4][j + 4] == confirmColor && board[i + 5][j + 5] == anotherColor) {
						weight[i][j] += PRIORITY_2;
					}
				}
				//4.右上至左下
				if (!(i - 5 < 0) && !(j + 5 > ROWS - 1)) {
					if (board[i - 1][j + 1] == confirmColor && board[i - 2][j + 2] == confirmColor
							&& board[i - 3][j + 3] == confirmColor && board[i - 4][j + 4] == confirmColor && board[i - 5][j + 5] == anotherColor) {
						weight[i][j] += PRIORITY_2;
					}
				}
				
				//眠三_●_●●○右边那颗
				// 1.横向
				if (!(i - 2 < 0) && !(i + 3 > COLS - 1)) {
					if (board[i - 2][j] == null && board[i - 1][j] == confirmColor && board[i + 1][j] == confirmColor
							&& board[i + 2][j] == confirmColor && board[i + 3][j] == anotherColor) {
						weight[i][j] += PRIORITY_2;
					}
				}
				//2.纵向
				if (!(j - 2 < 0) && !(j + 3 > ROWS - 1)) {
					if (board[i][j - 2] == null && board[i][j - 1] == confirmColor && board[i][j + 1] == confirmColor
							&& board[i][j + 2] == confirmColor && board[i][j + 3] == anotherColor) {
						weight[i][j] += PRIORITY_2;
					}
				}
				//3.左上至右下
				if ((!(i - 2 < 0) && !(i + 3 > COLS - 1)) && (!(j - 2 < 0) && !(j + 3 > ROWS - 1))) {
					if (board[i - 2][j - 2] == null && board[i - 1][j - 1] == confirmColor && board[i + 1][j + 1] == confirmColor
							&& board[i + 2][j + 2] == confirmColor && board[i + 3][j + 3] == anotherColor) {
						weight[i][j] += PRIORITY_2;
					}
				}
				//4.右上至左下
				if ((!(i + 2 > COLS - 1) && !(i - 3 < 0)) && (!(j - 2 < 0) && !(j + 3 > ROWS - 1))) {
					if (board[i + 1][j - 1] == confirmColor && board[i + 2][j - 2] == null
							&& board[i - 1][j + 1] == confirmColor && board[i - 2][j + 2] == confirmColor && board[i - 3][j + 3] == anotherColor) {
						weight[i][j] += PRIORITY_2;
					}
				}
				
				//眠三○●●_●_左边
				// 1.横向
				if (!(i - 3 < 0) && !(i + 2 > COLS - 1)) {
					if (board[i - 3][j] == anotherColor && board[i - 2][j] == confirmColor && board[i - 1][j] == confirmColor
							&& board[i + 1][j] == confirmColor && board[i + 2][j] == null) {
						weight[i][j] += PRIORITY_2;
					}
				}
				//2.纵向
				if (!(j - 3 < 0) && !(j + 2 > ROWS - 1)) {
					if (board[i][j - 3] == anotherColor && board[i][j - 2] == confirmColor && board[i][j - 1] == confirmColor
							&& board[i][j + 1] == confirmColor && board[i][j + 2] == null) {
						weight[i][j] += PRIORITY_2;
					}
				}
				//3.左上至右下
				if ((!(i - 3 < 0) && !(i + 2 > COLS - 1)) && (!(j - 3 < 0) && !(j + 2 > ROWS - 1))) {
					if (board[i - 3][j - 3] == anotherColor && board[i - 2][j - 2] == confirmColor
							&& board[i - 1][j - 1] == confirmColor && board[i + 1][j + 1] == confirmColor && board[i + 2][j + 2] == null) {
						weight[i][j] += PRIORITY_2;
					}
				}
				//4.右上至左下
				if ((!(i + 3 > COLS - 1) && !(i - 2 < 0)) && (!(j - 3 < 0) && !(j + 2 > ROWS - 1))) {
					if (board[i + 3][j - 3] == anotherColor && board[i + 2][j - 2] == confirmColor
							&& board[i + 1][j - 1] == confirmColor && board[i - 1][j + 1] == confirmColor && board[i - 2][j + 2] == null) {
						weight[i][j] += PRIORITY_2;
					}
				}
				
				//眠三○●●_●_右边
				// 1.横向
				if (!(i - 5 < 0)) {
					if (board[i - 5][j] == anotherColor && board[i - 4][j] == confirmColor && board[i - 3][j] == confirmColor
							&& board[i - 2][j] == null && board[i - 1][j] == confirmColor) {
						weight[i][j] += PRIORITY_2;
					}
				}
				//2.纵向
				if (!(j - 5 < 0)) {
					if (board[i][j - 5] == anotherColor && board[i][j - 4] == confirmColor && board[i][j - 3] == confirmColor
							&& board[i][j - 2] == null && board[i][j - 1] == confirmColor) {
						weight[i][j] += PRIORITY_2;
					}
				}
				//3.左上至右下
				if (!(i - 5 < 0) && !(j - 5 < 0)) {
					if (board[i - 5][j - 5] == anotherColor && board[i - 4][j - 4] == confirmColor
							&& board[i - 3][j - 3] == confirmColor && board[i - 2][j - 2] == null && board[i - 1][j - 1] == confirmColor) {
						weight[i][j] += PRIORITY_2;
					}
				}
				//4.右上至左下
				if (!(i + 5 > COLS - 1) && !(j - 5 < 0)) {
					if (board[i + 5][j - 5] == anotherColor && board[i + 4][j - 4] == confirmColor
							&& board[i + 3][j - 3] == confirmColor && board[i + 2][j - 2] == null && board[i + 1][j - 1] == confirmColor) {
						weight[i][j] += PRIORITY_2;
					}
				}
				
				//眠三○●_●●_ 左边
				// 1.横向
				if (!(i - 2 < 0) && !(i + 3 > COLS - 1)) {
					if (board[i - 2][j] == anotherColor && board[i - 1][j] == confirmColor && board[i + 1][j] == confirmColor
							&& board[i + 2][j] == confirmColor && board[i + 3][j] == null) {
						weight[i][j] += PRIORITY_2;
					}
				}
				//2.纵向
				if (!(j - 2 < 0) && !(j + 3 > ROWS - 1)) {
					if (board[i][j - 2] == anotherColor && board[i][j - 1] == confirmColor && board[i][j + 1] == confirmColor
							&& board[i][j + 2] == confirmColor && board[i][j + 3] == null) {
						weight[i][j] += PRIORITY_2;
					}
				}
				//3.左上至右下
				if ((!(i - 2 < 0) && !(i + 3 > COLS - 1)) && (!(j - 2 < 0) && !(j + 3 > ROWS - 1))) {
					if (board[i - 2][j - 2] == anotherColor && board[i - 1][j - 1] == confirmColor
							&& board[i + 1][j + 1] == confirmColor && board[i + 2][j + 2] == confirmColor && board[i + 3][j + 3] == null) {
						weight[i][j] += PRIORITY_2;
					}
				}
				//4.右上至左下
				if ((!(i + 2 > COLS - 1) && !(i - 3 < 0)) && (!(j - 2 < 0) && !(j + 3 > ROWS - 1))) {
					if (board[i + 2][j - 2] == anotherColor && board[i + 1][j - 1] == confirmColor
							&& board[i - 1][j + 1] == confirmColor && board[i - 2][j + 2] == confirmColor && board[i - 3][j + 3] == null) {
						weight[i][j] += PRIORITY_2;
					}
				}
				
				//眠三○●_●●_ 右边
				// 1.横向
				if (!(i - 5 < 0)) {
					if (board[i - 5][j] == anotherColor && board[i - 4][j] == confirmColor && board[i - 3][j] == null
							&& board[i - 2][j] == confirmColor && board[i - 1][j] == confirmColor) {
						weight[i][j] += PRIORITY_2;
					}
				}
				//2.纵向
				if (!(j - 5 < 0)) {
					if (board[i][j - 5] == anotherColor && board[i][j - 4] == confirmColor && board[i][j - 3] == null
							&& board[i][j - 2] == confirmColor && board[i][j - 1] == confirmColor) {
						weight[i][j] += PRIORITY_2;
					}
				}
				//3.左上至右下
				if (!(i - 5 < 0) && !(j - 5 < 0)) {
					if (board[i - 5][j - 5] == anotherColor && board[i - 4][j - 4] == confirmColor
							&& board[i - 3][j - 3] == null && board[i - 2][j - 2] == confirmColor && board[i - 1][j - 1] == confirmColor) {
						weight[i][j] += PRIORITY_2;
					}
				}
				//4.右上至左下
				if (!(i + 5 > COLS - 1) && !(j - 5 < 0)) {
					if (board[i + 5][j - 5] == anotherColor && board[i + 4][j - 4] == confirmColor
							&& board[i + 3][j - 3] == null && board[i + 2][j - 2] == confirmColor && board[i + 1][j - 1] == confirmColor) {
						weight[i][j] += PRIORITY_2;
					}
				}
				
				//眠三_●●_●○ 左
				// 1.横向
				if (!(i + 5 > COLS - 1)) {
					if (board[i + 1][j] == confirmColor && board[i + 2][j] == confirmColor && board[i + 3][j] == null
							&& board[i + 4][j] == confirmColor && board[i + 5][j] == anotherColor) {
						weight[i][j] += PRIORITY_2;
					}
				}
				//2.纵向
				if (!(j + 5 > ROWS - 1)) {
					if (board[i][j + 1] == confirmColor && board[i][j + 2] == confirmColor && board[i][j + 3] == null
							&& board[i][j + 4] == confirmColor && board[i][j + 5] == anotherColor) {
						weight[i][j] += PRIORITY_2;
					}
				}
				//3.左上至右下
				if (!(i + 5 > COLS - 1) && !(j + 5 > ROWS - 1)) {
					if (board[i + 1][j + 1] == confirmColor && board[i + 2][j + 2] == confirmColor
							&& board[i + 3][j + 3] == null && board[i + 4][j + 4] == confirmColor && board[i + 5][j + 5] == anotherColor) {
						weight[i][j] += PRIORITY_2;
					}
				}
				//4.右上至左下
				if (!(i - 5 < 0) && !(j + 5 > ROWS - 1)) {
					if (board[i - 1][j + 1] == confirmColor && board[i - 2][j + 2] == confirmColor
							&& board[i - 3][j + 3] == null && board[i - 4][j + 4] == confirmColor && board[i - 5][j + 5] == anotherColor) {
						weight[i][j] += PRIORITY_2;
					}
				}
				
				//眠三_●●_●○ 右
				// 1.横向
				if (!(i - 3 < 0) && !(i + 2 > COLS - 1)) {
					if (board[i - 3][j] == null && board[i - 2][j] == confirmColor && board[i - 1][j] == confirmColor
							&& board[i + 1][j] == confirmColor && board[i + 2][j] == anotherColor) {
						weight[i][j] += PRIORITY_2;
					}
				}
				//2.纵向
				if (!(j - 3 < 0) && !(j + 2 > ROWS - 1)) {
					if (board[i][j - 3] == null && board[i][j - 2] == confirmColor && board[i][j - 1] == confirmColor
							&& board[i][j + 1] == confirmColor && board[i][j + 2] == anotherColor) {
						weight[i][j] += PRIORITY_2;
					}
				}
				//3.左上至右下
				if ((!(i - 3 < 0) && !(i + 2 > COLS - 1)) && (!(j - 3 < 0) && !(j + 2 > ROWS - 1))) {
					if (board[i - 3][j - 3] == null && board[i - 2][j - 2] == confirmColor && board[i - 1][j - 1] == confirmColor
							&& board[i + 1][j + 1] == confirmColor && board[i + 2][j + 2] == anotherColor) {
						weight[i][j] += PRIORITY_2;
					}
				}
				//4.右上至左下
				if ((!(i + 3 > COLS - 1) && !(i - 2 < 0)) && ((!(j - 3 < 0)) && !(j + 2 > ROWS - 1))) {
					if (board[i + 3][j - 3] == null && board[i + 2][j - 2] == confirmColor && board[i + 1][j - 1] == confirmColor
							&& board[i - 1][j + 1] == confirmColor && board[i - 2][j + 2] == anotherColor) {
						weight[i][j] += PRIORITY_2;
					}
				}
				
				//眠三●_ _●● 左
				// 1.横向
				if (!(i - 1 < 0) && !(i + 3 > COLS - 1)) {
					if (board[i - 1][j] == confirmColor && board[i + 1][j] == null && board[i + 2][j] == confirmColor && board[i + 3][j] == confirmColor) {
						weight[i][j] += PRIORITY_2;
					}
				}
				//2.纵向
				if (!(j - 1 < 0) && !(j + 3 > ROWS - 1)) {
					if (board[i][j - 1] == confirmColor && board[i][j + 1] == null && board[i][j + 2] == confirmColor && board[i][j + 3] == confirmColor) {
						weight[i][j] += PRIORITY_2;
					}
				}
				//3.左上至右下
				if ((!(i - 1 < 0) && !(i + 3 > COLS - 1)) && (!(j - 1 < 0) && !(j + 3 > ROWS - 1))) {
					if (board[i - 1][j - 1] == confirmColor && board[i + 1][j + 1] == null
							&& board[i + 2][j + 2] == confirmColor && board[i + 3][j + 3] == confirmColor) {
						weight[i][j] += PRIORITY_2;
					}
				}
				//4.右上至左下
				if ((!(i + 1 > COLS - 1) && !(i - 3 < 0)) && (!(j - 1 < 0) && !(j + 3 > ROWS - 1))) {
					if (board[i + 1][j - 1] == confirmColor && board[i - 1][j + 1] == null
							&& board[i - 2][j + 2] == confirmColor && board[i - 3][j + 3] == confirmColor) {
						weight[i][j] += PRIORITY_2;
					}
				}
				
				//眠三●_ _●● 右
				// 1.横向
				if (!(i - 2 < 0) && !(i + 2 > COLS - 1)) {
					if (board[i - 2][j] == confirmColor && board[i - 1][j] == null && board[i + 1][j] == confirmColor && board[i + 2][j] == confirmColor) {
						weight[i][j] += PRIORITY_2;
					}
				}
				//2.纵向
				if (!(j - 2 < 0) && !(j + 2 > ROWS - 1)) {
					if (board[i][j - 2] == confirmColor && board[i][j - 1] == null && board[i][j + 1] == confirmColor && board[i][j + 2] == confirmColor) {
						weight[i][j] += PRIORITY_2;
					}
				}
				//3.左上至右下
				if ((!(i - 2 < 0) && !(i + 2 > COLS - 1)) && (!(j - 2 < 0) && !(j + 2 > ROWS - 1))) {
					if (board[i - 2][j - 2] == confirmColor && board[i - 1][j - 1] == null
							&& board[i + 1][j + 1] == confirmColor && board[i + 2][j + 2] == confirmColor) {
						weight[i][j] += PRIORITY_2;
					}
				}
				//4.右上至左下
				if ((!(i + 2 > COLS - 1) && !(i - 2 < 0)) && (!(j - 2 < 0) && !(j + 2 > ROWS - 1))) {
					if (board[i + 2][j - 2] == confirmColor && board[i + 1][j - 1] == null
							&& board[i - 1][j + 1] == confirmColor && board[i - 2][j + 2] == confirmColor) {
						weight[i][j] += PRIORITY_2;
					}
				}
				
				//眠三●●_ _● 左
				// 1.横向
				if ((!(i - 2 < 0)) && !(i + 2 > COLS - 1)) {
					if (board[i - 2][j] == confirmColor && board[i - 1][j] == confirmColor && board[i + 1][j] == null && board[i + 2][j] == confirmColor) {
						weight[i][j] += PRIORITY_2;
					}
				}
				//2.纵向
				if ((!(j - 2 < 0)) && !(j + 2 > ROWS - 1)) {
					if (board[i][j - 2] == confirmColor && board[i][j - 1] == confirmColor && board[i][j + 1] == null && board[i][j + 2] == confirmColor) {
						weight[i][j] += PRIORITY_2;
					}
				}
				//3.左上至右下
				if ((!(i - 2 < 0) && !(i + 2 > COLS - 1)) && (!(j - 2 < 0) && !(j + 2 > ROWS - 1))) {
					if (board[i - 2][j - 2] == confirmColor && board[i - 1][j - 1] == confirmColor
							&& board[i + 1][j + 1] == null && board[i + 2][j + 2] == confirmColor) {
						weight[i][j] += PRIORITY_2;
					}
				}
				//4.右上至左下
				if ((!(i + 2 > COLS - 1) && !(i - 2 < 0)) && (!(j - 2 < 0) && !(j + 2 > ROWS - 1))) {
					if (board[i + 2][j - 2] == confirmColor && board[i + 1][j - 1] == confirmColor
							&& board[i - 1][j + 1] == null && board[i - 2][j + 2] == confirmColor) {
						weight[i][j] += PRIORITY_2;
					}
				}
				
				//眠三●●_ _● 右
				// 1.横向
				if (!(i - 3 < 0) && !(i + 1 > COLS - 1)) {
					if (board[i - 3][j] == confirmColor && board[i - 2][j] == confirmColor && board[i - 1][j] == null && board[i + 1][j] == confirmColor) {
						weight[i][j] += PRIORITY_2;
					}
				}
				//2.纵向
				if (!(j - 3 < 0) && !(j + 1 > ROWS - 1)) {
					if (board[i][j - 3] == confirmColor && board[i][j - 2] == confirmColor && board[i][j - 1] == null && board[i][j + 1] == confirmColor) {
						weight[i][j] += PRIORITY_2;
					}
				}
				//3.左上至右下
				if ((!(i - 3 < 0) && !(i + 1 > COLS - 1)) && (!(j - 3 < 0) && !(j + 1 > ROWS - 1))) {
					if (board[i - 3][j - 3] == confirmColor && board[i - 2][j - 2] == confirmColor
							&& board[i - 1][j - 1] == null && board[i + 1][j + 1] == confirmColor) {
						weight[i][j] += PRIORITY_2;
					}
				}
				//4.右上至左下
				if ((!(i + 3 > COLS - 1) && !(i - 1 < 0)) && (!(j - 3 < 0) && !(j + 1 > ROWS - 1))) {
					if (board[i + 3][j - 3] == confirmColor && board[i + 2][j - 2] == confirmColor
							&& board[i + 1][j - 1] == null && board[i - 1][j + 1] == confirmColor) {
						weight[i][j] += PRIORITY_2;
					}
				}
				
				//眠三●_●_● 左
				// 1.横向
				if (!(i - 1 < 0) && !(i + 3 > COLS - 1)) {
					if (board[i - 1][j] == confirmColor && board[i + 1][j] == confirmColor && board[i + 2][j] == null && board[i + 3][j] == confirmColor) {
						weight[i][j] += PRIORITY_2;
					}
				}
				//2.纵向
				if (!(j - 1 < 0) && !(j + 3 > ROWS - 1)) {
					if (board[i][j - 1] == confirmColor && board[i][j + 1] == confirmColor && board[i][j + 2] == null && board[i][j + 3] == confirmColor) {
						weight[i][j] += PRIORITY_2;
					}
				}
				//3.左上至右下
				if ((!(i - 1 < 0) && !(i + 3 > COLS - 1)) && (!(j - 1 < 0) && !(j + 3 > ROWS - 1))) {
					if (board[i - 1][j - 1] == confirmColor && board[i + 1][j + 1] == confirmColor
							&& board[i + 2][j + 2] == null && board[i + 3][j + 3] == confirmColor) {
						weight[i][j] += PRIORITY_2;
					}
				}
				//4.右上至左下
				if ((!(i + 1 > COLS - 1) && !(i - 3 < 0)) && (!(j - 1 < 0) && !(j + 3 > ROWS - 1))) {
					if (board[i + 1][j - 1] == confirmColor && board[i - 1][j + 1] == confirmColor
							&& board[i - 2][j + 2] == null && board[i - 3][j + 3] == confirmColor) {
						weight[i][j] += PRIORITY_2;
					}
				}
				
				//眠三●_●_● 右
				// 1.横向
				if (!(i - 3 < 0) && !(i + 1 > COLS - 1)) {
					if (board[i - 3][j] == confirmColor && board[i - 2][j] == null && board[i - 1][j] == confirmColor && board[i + 1][j] == confirmColor) {
						weight[i][j] += PRIORITY_2;
					}
				}
				//2.纵向
				if (!(j - 3 < 0) && !(j + 1 > ROWS - 1)) {
					if (board[i][j - 3] == confirmColor && board[i][j - 2] == null && board[i][j - 1] == confirmColor && board[i][j + 1] == confirmColor) {
						weight[i][j] += PRIORITY_2;
					}
				}
				//3.左上至右下
				if ((!(i - 3 < 0) && !(i + 1 > COLS - 1)) && (!(j - 3 < 0) && !(j + 1 > ROWS - 1))) {
					if (board[i - 3][j - 3] == confirmColor && board[i - 2][j - 2] == null
							&& board[i - 1][j - 1] == confirmColor && board[i + 1][j + 1] == confirmColor) {
						weight[i][j] += PRIORITY_2;
					}
				}
				//4.右上至左下
				if ((!(i + 3 > COLS - 1) && !(i - 1 < 0)) && (!(j - 3 < 0) && !(j + 1 > ROWS - 1))) {
					if (board[i + 3][j - 3] == confirmColor && board[i + 2][j - 2] == null
							&& board[i + 1][j - 1] == confirmColor && board[i - 1][j + 1] == confirmColor) {
						weight[i][j] += PRIORITY_2;
					}
				}
				
				//眠三○_●●●_○ 左
				// 1.横向
				if (!(i - 1 < 0) && !(i + 5 > COLS - 1)) {
					if (board[i - 1][j] == anotherColor && board[i + 1][j] == confirmColor && board[i + 2][j] == confirmColor
							&& board[i + 3][j] == confirmColor && board[i + 4][j] == null && board[i + 5][j] == anotherColor) {
						weight[i][j] += PRIORITY_2;
					}
				}
				//2.纵向
				if (!(j - 1 < 0) && !(j + 5 > ROWS - 1)) {
					if (board[i][j - 1] == anotherColor && board[i][j + 1] == confirmColor && board[i][j + 2] == confirmColor
							&& board[i][j + 3] == confirmColor && board[i][j + 4] == null && board[i][j + 5] == anotherColor) {
						weight[i][j] += PRIORITY_2;
					}
				}
				//3.左上至右下
				if ((!(i - 1 < 0) && !(i + 5 > COLS - 1)) && (!(j - 1 < 0) && !(j + 5 > ROWS - 1))) {
					if (board[i - 1][j - 1] == anotherColor && board[i + 1][j + 1] == confirmColor && board[i + 2][j + 2] == confirmColor
							&& board[i + 3][j + 3] == confirmColor && board[i + 4][j + 4] == null && board[i + 5][j + 5] == anotherColor) {
						weight[i][j] += PRIORITY_2;
					}
				}
				//4.右上至左下
				if ((!(i + 1 > COLS - 1) && !(i - 5 < 0)) && (!(j - 1 < 0) && !(j + 5 > ROWS - 1))) {
					if (board[i + 1][j - 1] == anotherColor && board[i - 1][j + 1] == confirmColor && board[i - 2][j + 2] == confirmColor
							&& board[i - 3][j + 3] == confirmColor && board[i - 4][j + 4] == null && board[i - 5][j + 5] == anotherColor) {
						weight[i][j] += PRIORITY_2;
					}
				}
				
				//眠三○_●●●_○ 右
				// 1.横向
				if ((!(i - 5 < 0)) && !(i + 1 > COLS - 1)) {
					if (board[i - 5][j] == anotherColor && board[i - 4][j] == null && board[i - 3][j] == confirmColor
							&& board[i - 2][j] == confirmColor && board[i - 1][j] == confirmColor && board[i + 1][j] == anotherColor) {
						weight[i][j] += PRIORITY_2;
					}
				}
				//2.纵向
				if (!(j - 5 < 0) && !(j + 1 > ROWS - 1)) {
					if (board[i][j - 5] == anotherColor && board[i][j - 4] == null && board[i][j - 3] == confirmColor
							&& board[i][j - 2] == confirmColor && board[i][j - 1] == confirmColor && board[i][j + 1] == anotherColor) {
						weight[i][j] += PRIORITY_2;
					}
				}
				//3.左上至右下
				if ((!(i - 5 < 0) && !(i + 1 > COLS - 1)) && (!(j - 5 < 0) && !(j + 1 > ROWS - 1))) {
					if (board[i - 5][j - 5] == anotherColor && board[i - 4][j - 4] == null && board[i - 3][j - 3] == confirmColor
							&& board[i - 2][j - 2] == confirmColor && board[i - 1][j - 1] == confirmColor && board[i + 1][j + 1] == anotherColor) {
						weight[i][j] += PRIORITY_2;
					}
				}
				//4.右上至左下
				if ((!(i + 5 > COLS - 1) && !(i - 1 < 0)) && (!(j - 5 < 0) && !(j + 1 > ROWS - 1))) {
					if (board[i + 5][j - 5] == anotherColor && board[i + 4][j - 4] == null && board[i + 3][j - 3] == confirmColor
							&& board[i + 2][j - 2] == confirmColor && board[i + 1][j - 1] == confirmColor && board[i - 1][j + 1] == anotherColor) {
						weight[i][j] += PRIORITY_2;
					}
				}
				
				//眠二○●●_ _ _ 左
				// 1.横向
				if (!(i - 3 < 0) && !(i + 2 > COLS - 1)) {
					if (board[i - 3][j] == anotherColor && board[i - 2][j] == confirmColor && board[i - 1][j] == confirmColor
							&& board[i + 1][j] == null && board[i + 2][j] == null) {
						weight[i][j] += PRIORITY_1;
					}
				}
				//2.纵向
				if (!(j - 3 < 0) && !(j + 2 > ROWS - 1)) {
					if (board[i][j - 3] == anotherColor && board[i][j - 2] == confirmColor && board[i][j - 1] == confirmColor
							&& board[i][j + 1] == null && board[i][j + 2] == null) {
						weight[i][j] += PRIORITY_1;
					}
				}
				//3.右下至左上
				if ((!(i - 3 < 0) && !(i + 2 > COLS - 1)) && (!(j - 3 < 0) && !(j + 2 > ROWS - 1))) {
					if (board[i - 3][j - 3] == anotherColor && board[i - 2][j - 2] == confirmColor && board[i - 1][j - 1] == confirmColor
							&& board[i + 1][j + 1] == null && board[i + 2][j + 2] == null) {
						weight[i][j] += PRIORITY_1;
					}
				}
				//4.左下至右上
				if ((!(i + 3 > COLS - 1) && !(i - 2 < 0)) && (!(j - 3 < 0) && !(j + 2 > ROWS - 1))) {
					if (board[i + 3][j - 3] == anotherColor && board[i + 2][j - 2] == confirmColor && board[i + 1][j - 1] == confirmColor
							&& board[i - 1][j + 1] == null && board[i - 2][j + 2] == null) {
						weight[i][j] += PRIORITY_1;
					}
				}
				
				//眠二○●●_ _ _ 中
				// 1.横向
				if (!(i - 4 < 0) && !(i + 1 > COLS - 1)) {
					if (board[i - 4][j] == anotherColor && board[i - 3][j] == confirmColor && board[i - 2][j] == confirmColor
							&& board[i - 1][j] == null && board[i + 1][j] == null) {
						weight[i][j] += PRIORITY_1;
					}
				}
				//2.纵向
				if (!(j - 4 < 0) && !(j + 1 > ROWS - 1)) {
					if (board[i][j - 4] == anotherColor && board[i][j - 3] == confirmColor && board[i][j - 2] == confirmColor
							&& board[i][j - 1] == null && board[i][j + 1] == null) {
						weight[i][j] += PRIORITY_1;
					}
				}
				//3.右下至左上
				if ((!(i - 4 < 0) && !(i + 1 > COLS - 1)) && (!(j - 4 < 0) && !(j + 1 > ROWS - 1))) {
					if (board[i - 4][j - 4] == anotherColor && board[i - 3][j - 3] == confirmColor && board[i - 2][j - 2] == confirmColor
							&& board[i - 1][j - 1] == null && board[i + 1][j + 1] == null) {
						weight[i][j] += PRIORITY_1;
					}
				}
				//4.左下至右上
				if ((!(i + 4 > COLS - 1) && !(i - 1 < 0)) && (!(j - 4 < 0) && !(j + 1 > ROWS - 1))) {
					if (board[i + 4][j - 4] == anotherColor && board[i + 3][j - 3] == confirmColor && board[i + 2][j - 2] == confirmColor
							&& board[i + 1][j - 1] == null && board[i - 1][j + 1] == null) {
						weight[i][j] += PRIORITY_1;
					}
				}
				
				//眠二○●●_ _ _ 右
				// 1.横向
				if (!(i - 5 < 0)) {
					if (board[i - 5][j] == anotherColor && board[i - 4][j] == confirmColor && board[i - 3][j] == confirmColor
							&& board[i - 2][j] == null && board[i - 1][j] == null) {
						weight[i][j] += PRIORITY_1;
					}
				}
				//2.纵向
				if (!(j - 5 < 0)) {
					if (board[i][j - 5] == anotherColor && board[i][j - 4] == confirmColor && board[i][j - 3] == confirmColor
							&& board[i][j - 2] == null && board[i][j - 1] == null) {
						weight[i][j] += PRIORITY_1;
					}
				}
				//3.右下至左上
				if (!(i - 5 < 0) && !(j - 5 < 0)) {
					if (board[i - 5][j - 5] == anotherColor && board[i - 4][j - 4] == confirmColor && board[i - 3][j - 3] == confirmColor
							&& board[i - 2][j - 2] == null && board[i - 1][j - 1] == null) {
						weight[i][j] += PRIORITY_1;
					}
				}
				//4.左下至右上
				if (!(i + 5 > COLS - 1) && !(j - 5 < 0)) {
					if (board[i + 5][j - 5] == anotherColor && board[i + 4][j - 4] == confirmColor && board[i + 3][j - 3] == confirmColor
							&& board[i + 2][j - 2] == null && board[i + 1][j - 1] == null) {
						weight[i][j] += PRIORITY_1;
					}
				}
				
				//眠二_ _ _●●○ 左
				// 1.横向
				if (!(i + 5 > COLS - 1)) {
					if (board[i + 1][j] == null && board[i + 2][j] == null && board[i + 3][j] == confirmColor
							&& board[i + 4][j] == confirmColor && board[i + 5][j] == anotherColor) {
						weight[i][j] += PRIORITY_1;
					}
				}
				//2.纵向
				if (!(j + 5 > ROWS - 1)) {
					if (board[i][j + 1] == null && board[i][j + 2] == null && board[i][j + 3] == confirmColor
							&& board[i][j + 4] == confirmColor && board[i][j + 5] == anotherColor) {
						weight[i][j] += PRIORITY_1;
					}
				}
				//3.左上至右下
				if (!(i + 5 > COLS - 1) && !(j + 5 > ROWS - 1)) {
					if (board[i + 1][j + 1] == null && board[i + 2][j + 2] == null && board[i + 3][j + 3] == confirmColor
							&& board[i + 4][j + 4] == confirmColor && board[i + 5][j + 5] == anotherColor) {
						weight[i][j] += PRIORITY_1;
					}
				}
				//4.右上至左下
				if (!(i - 5 < 0) && !(j + 5 > ROWS - 1)) {
					if (board[i - 1][j + 1] == null && board[i - 2][j + 2] == null && board[i - 3][j + 3] == confirmColor
							&& board[i - 4][j + 4] == confirmColor && board[i - 5][j + 5] == anotherColor) {
						weight[i][j] += PRIORITY_1;
					}
				}
				
				//眠二_ _ _●●○ 中
				// 1.横向
				if (!(i - 1 < 0) && !(i + 4 > COLS - 1)) {
					if (board[i - 1][j] == null && board[i + 1][j] == null && board[i + 2][j] == confirmColor
							&& board[i + 3][j] == confirmColor && board[i + 4][j] == anotherColor) {
						weight[i][j] += PRIORITY_1;
					}
				}
				//2.纵向
				if (!(j - 1 < 0) && !(j + 4 > ROWS - 1)) {
					if (board[i][j - 1] == null && board[i][j + 1] == null && board[i][j + 2] == confirmColor
							&& board[i][j + 3] == confirmColor && board[i][j + 4] == anotherColor) {
						weight[i][j] += PRIORITY_1;
					}
				}
				//3.左上至右下
				if ((!(i - 1 < 0) && !(i + 4 > COLS - 1)) && (!(j - 1 < 0) && !(j + 4 > ROWS - 1))) {
					if (board[i - 1][j - 1] == null && board[i + 1][j + 1] == null && board[i + 2][j + 2] == confirmColor
							&& board[i + 3][j + 3] == confirmColor && board[i + 4][j + 4] == anotherColor) {
						weight[i][j] += PRIORITY_1;
					}
				}
				//4.右上至左下
				if ((!(i + 1 > COLS - 1) && !(i - 4 < 0)) && (!(j - 1 < 0) && !(j + 4 > ROWS - 1))) {
					if (board[i + 1][j - 1] == null && board[i - 1][j + 1] == null && board[i - 2][j + 2] == confirmColor
							&& board[i - 3][j + 3] == confirmColor && board[i - 4][j + 4] == anotherColor) {
						weight[i][j] += PRIORITY_1;
					}
				}
				
				//眠二_ _ _●●○ 右
				// 1.横向
				if ((!(i - 2 < 0)) && !(i + 3 > COLS - 1)) {
					if (board[i - 2][j] == null && board[i - 1][j] == null && board[i + 1][j] == confirmColor
							&& board[i + 2][j] == confirmColor && board[i + 3][j] == anotherColor) {
						weight[i][j] += PRIORITY_1;
					}
				}
				//2.纵向
				if ((!(j - 2 < 0)) && !(j + 3 > ROWS - 1)) {
					if (board[i][j - 2] == null && board[i][j - 1] == null && board[i][j + 1] == confirmColor
							&& board[i][j + 2] == confirmColor && board[i][j + 3] == anotherColor) {
						weight[i][j] += PRIORITY_1;
					}
				}
				//3.左上至右下
				if ((!(i - 2 < 0) && !(i + 3 > COLS - 1)) && (!(j - 2 < 0) && !(j + 3 > ROWS - 1))) {
					if (board[i - 2][j - 2] == null && board[i - 1][j - 1] == null && board[i + 1][j + 1] == confirmColor
							&& board[i + 2][j + 2] == confirmColor && board[i + 3][j + 3] == anotherColor) {
						weight[i][j] += PRIORITY_1;
					}
				}
				//4.右上至左下
				if ((!(i + 2 > COLS - 1) && !(i - 3 < 0)) && (!(j - 2 < 0) && !(j + 3 > ROWS - 1))) {
					if (board[i + 2][j - 2] == null && board[i + 1][j - 1] == null && board[i - 1][j + 1] == confirmColor
							&& board[i - 2][j + 2] == confirmColor && board[i - 3][j + 3] == anotherColor) {
						weight[i][j] += PRIORITY_1;
					}
				}
				
				//眠二○●_ _●_ 左
				// 1.横向
				if ((!(i - 2 < 0)) && !(i + 3 > COLS - 1)) {
					if (board[i - 2][j] == anotherColor && board[i - 1][j] == confirmColor && board[i + 1][j] == null
							&& board[i + 2][j] == confirmColor && board[i + 3][j] == null) {
						weight[i][j] += PRIORITY_1;
					}
				}
				//2.纵向
				if ((!(j - 2 < 0)) && !(j + 3 > ROWS - 1)) {
					if (board[i][j - 2] == anotherColor && board[i][j - 1] == confirmColor && board[i][j + 1] == null
							&& board[i][j + 2] == confirmColor && board[i][j + 3] == null) {
						weight[i][j] += PRIORITY_1;
					}
				}
				//3.左上至右下
				if ((!(i - 2 < 0) && !(i + 3 > COLS - 1)) && (!(j - 2 < 0) && !(j + 3 > ROWS - 1))) {
					if (board[i - 2][j - 2] == anotherColor && board[i - 1][j - 1] == confirmColor && board[i + 1][j + 1] == null
							&& board[i + 2][j + 2] == confirmColor && board[i + 3][j + 3] == null) {
						weight[i][j] += PRIORITY_1;
					}
				}
				//4.右上至左下
				if ((!(i + 2 > COLS - 1) && !(i - 3 < 0)) && (!(j - 2 < 0) && !(j + 3 > ROWS - 1))) {
					if (board[i + 2][j - 2] == anotherColor && board[i + 1][j - 1] == confirmColor && board[i - 1][j + 1] == null
							&& board[i - 2][j + 2] == confirmColor && board[i - 3][j + 3] == null) {
						weight[i][j] += PRIORITY_1;
					}
				}
				
				//眠二○●_ _●_ 中
				// 1.横向
				if ((!(i - 3 < 0)) && !(i + 2 > COLS - 1)) {
					if (board[i - 3][j] == anotherColor && board[i - 2][j] == confirmColor && board[i - 1][j] == null
							&& board[i + 1][j] == confirmColor && board[i + 2][j] == null) {
						weight[i][j] += PRIORITY_1;
					}
				}
				//2.纵向
				if (!(j - 3 < 0) && !(j + 2 > ROWS - 1)) {
					if (board[i][j - 3] == anotherColor && board[i][j - 2] == confirmColor && board[i][j - 1] == null
							&& board[i][j + 1] == confirmColor && board[i][j + 2] == null) {
						weight[i][j] += PRIORITY_1;
					}
				}
				//3.左上至右下
				if ((!(i - 3 < 0) && !(i + 2 > COLS - 1)) && (!(j - 3 < 0) && !(j + 2 > ROWS - 1))) {
					if (board[i - 3][j - 3] == anotherColor && board[i - 2][j - 2] == confirmColor && board[i - 1][j - 1] == null
							&& board[i + 1][j + 1] == confirmColor && board[i + 2][j + 2] == null) {
						weight[i][j] += PRIORITY_1;
					}
				}
				//4.右上至左下
				if ((!(i + 3 > COLS - 1) && !(i - 2 < 0)) && (!(j - 3 < 0) && !(j + 2 > ROWS - 1))) {
					if (board[i + 3][j - 3] == anotherColor && board[i + 2][j - 2] == confirmColor && board[i + 1][j - 1] == null
							&& board[i - 1][j + 1] == confirmColor && board[i - 2][j + 2] == null) {
						weight[i][j] += PRIORITY_1;
					}
				}
				
				//眠二○●_ _●_ 右
				// 1.横向
				if (!(i - 5 < 0)) {
					if (board[i - 5][j] == anotherColor && board[i - 4][j] == confirmColor && board[i - 3][j] == null
							&& board[i - 2][j] == null && board[i - 1][j] == confirmColor) {
						weight[i][j] += PRIORITY_1;
					}
				}
				//2.纵向
				if (!(j - 5 < 0)) {
					if (board[i][j - 5] == anotherColor && board[i][j - 4] == confirmColor && board[i][j - 3] == null
							&& board[i][j - 2] == null && board[i][j - 1] == confirmColor) {
						weight[i][j] += PRIORITY_1;
					}
				}
				//3.左上至右下
				if ((!(i - 5 < 0)) && !(j - 5 < 0)) {
					if (board[i - 5][j - 5] == anotherColor && board[i - 4][j - 4] == confirmColor && board[i - 3][j - 3] == null
							&& board[i - 2][j - 2] == null && board[i - 1][j - 1] == confirmColor) {
						weight[i][j] += PRIORITY_1;
					}
				}
				//4.右上至左下
				if (!(i + 5 > COLS - 1) && !(j - 5 < 0)) {
					if (board[i + 5][j - 5] == anotherColor && board[i + 4][j - 4] == confirmColor && board[i + 3][j - 3] == null
							&& board[i + 2][j - 2] == null && board[i + 1][j - 1] == confirmColor) {
						weight[i][j] += PRIORITY_1;
					}
				}
				
				//眠二_●_ _●○ 左
				// 1.横向
				if (!(i + 5 > COLS - 1)) {
					if (board[i + 1][j] == confirmColor && board[i + 2][j] == null && board[i + 3][j] == null
							&& board[i + 4][j] == confirmColor && board[i + 5][j] == anotherColor) {
						weight[i][j] += PRIORITY_1;
					}
				}
				//2.纵向
				if (!(j + 5 > ROWS - 1)) {
					if (board[i][j + 1] == confirmColor && board[i][j + 2] == null && board[i][j + 3] == null
							&& board[i][j + 4] == confirmColor && board[i][j + 5] == anotherColor) {
						weight[i][j] += PRIORITY_1;
					}
				}
				//3.左上至右下
				if (!(i + 5 > COLS - 1) && !(j + 5 > ROWS - 1)) {
					if (board[i + 1][j + 1] == confirmColor && board[i + 2][j + 2] == null && board[i + 3][j + 3] == null
							&& board[i + 4][j + 4] == confirmColor && board[i + 5][j + 5] == anotherColor) {
						weight[i][j] += PRIORITY_1;
					}
				}
				//4.右上至左下
				if (!(i - 5 < 0) && !(j + 5 > ROWS - 1)) {
					if (board[i - 1][j + 1] == confirmColor && board[i - 2][j + 2] == null && board[i - 3][j + 3] == null
							&& board[i - 4][j + 4] == confirmColor && board[i - 5][j + 5] == anotherColor) {
						weight[i][j] += PRIORITY_1;
					}
				}
				
				//眠二_●_ _●○ 中
				// 1.横向
				if (!(i - 2 < 0) && !(i + 3 > COLS - 1)) {
					if (board[i - 2][j] == null && board[i - 1][j] == confirmColor && board[i + 1][j] == null
							&& board[i + 2][j] == confirmColor && board[i + 3][j] == anotherColor) {
						weight[i][j] += PRIORITY_1;
					}
				}
				//2.纵向
				if ((!(j - 2 < 0)) && !(j + 3 > ROWS - 1)) {
					if (board[i][j - 2] == null && board[i][j - 1] == confirmColor && board[i][j + 1] == null
							&& board[i][j + 2] == confirmColor && board[i][j + 3] == anotherColor) {
						weight[i][j] += PRIORITY_1;
					}
				}
				//3.左上至右下
				if ((!(i - 2 < 0) && !(i + 3 > COLS - 1)) && (!(j - 2 < 0) && !(j + 3 > ROWS - 1))) {
					if (board[i - 2][j - 2] == null && board[i - 1][j - 1] == confirmColor && board[i + 1][j + 1] == null
							&& board[i + 2][j + 2] == confirmColor && board[i + 3][j + 3] == anotherColor) {
						weight[i][j] += PRIORITY_1;
					}
				}
				//4.右上至左下
				if ((!(i + 2 > COLS - 1) && !(i - 3 < 0)) && (!(j - 2 < 0) && !(j + 3 > ROWS - 1))) {
					if (board[i + 2][j - 2] == null && board[i + 1][j - 1] == confirmColor && board[i - 1][j + 1] == null
							&& board[i - 2][j + 2] == confirmColor && board[i - 3][j + 3] == anotherColor) {
						weight[i][j] += PRIORITY_1;
					}
				}
				
				//眠二_●_ _●○ 右
				// 1.横向
				if (!(i - 3 < 0) && !(i + 2 > COLS - 1)) {
					if (board[i - 3][j] == null && board[i - 2][j] == confirmColor && board[i - 1][j] == null
							&& board[i + 1][j] == confirmColor && board[i + 2][j] == anotherColor) {
						weight[i][j] += PRIORITY_1;
					}
				}
				//2.纵向
				if (!(j - 3 < 0) && !(j + 2 > ROWS - 1)) {
					if (board[i][j - 3] == null && board[i][j - 2] == confirmColor && board[i][j - 1] == null
							&& board[i][j + 1] == confirmColor && board[i][j + 2] == anotherColor) {
						weight[i][j] += PRIORITY_1;
					}
				}
				//3.左上至右下
				if ((!(i - 3 < 0) && !(i + 2 > COLS - 1)) && (!(j - 3 < 0) && !(j + 2 > ROWS - 1))) {
					if (board[i - 3][j - 3] == null && board[i - 2][j - 2] == confirmColor && board[i - 1][j - 1] == null
							&& board[i + 1][j + 1] == confirmColor && board[i + 2][j + 2] == anotherColor) {
						weight[i][j] += PRIORITY_1;
					}
				}
				//4.右上至左下
				if ((!(i + 3 > COLS - 1) && !(i - 2 < 0)) && (!(j - 3 < 0) && !(j + 2 > ROWS - 1))) {
					if (board[i + 3][j - 3] == null && board[i + 2][j - 2] == confirmColor && board[i + 1][j - 1] == null
							&& board[i - 1][j + 1] == confirmColor && board[i - 2][j + 2] == anotherColor) {
						weight[i][j] += PRIORITY_1;
					}
				}
				
				//眠二●_ _ _● 左
				// 1.横向
				if (!(i - 1 < 0) && !(i + 3 > COLS - 1)) {
					if (board[i - 1][j] == confirmColor && board[i + 1][j] == null && board[i + 2][j] == null && board[i + 3][j] == confirmColor) {
						weight[i][j] += PRIORITY_2;
					}
				}
				//2.纵向
				if (!(j - 1 < 0) && !(j + 3 > ROWS - 1)) {
					if (board[i][j - 1] == confirmColor && board[i][j + 1] == null && board[i][j + 2] == null && board[i][j + 3] == confirmColor) {
						weight[i][j] += PRIORITY_2;
					}
				}
				//3.左上至右下
				if ((!(i - 1 < 0) && !(i + 3 > COLS - 1)) & (!(j - 1 < 0) && !(j + 3 > ROWS - 1))) {
					if (board[i - 1][j - 1] == confirmColor && board[i + 1][j + 1] == null
							&& board[i + 2][j + 2] == null && board[i + 3][j + 3] == confirmColor) {
						weight[i][j] += PRIORITY_2;
					}
				}
				//4.右上至左下
				if ((!(i + 1 > COLS - 1) && !(i - 3 < 0)) && (!(j - 1 < 0) && !(j + 3 > ROWS - 1))) {
					if (board[i + 1][j - 1] == confirmColor && board[i - 1][j + 1] == null
							&& board[i - 2][j + 2] == null && board[i - 3][j + 3] == confirmColor) {
						weight[i][j] += PRIORITY_2;
					}
				}
				
				//眠二●_ _ _● 中
				// 1.横向
				if (!(i - 2 < 0) && !(i + 2 > COLS - 1)) {
					if (board[i - 2][j] == confirmColor && board[i - 1][j] == null && board[i + 1][j] == null && board[i + 2][j] == confirmColor) {
						weight[i][j] += PRIORITY_1;
					}
				}
				//2.纵向
				if (!(j - 2 < 0) && !(j + 2 > ROWS - 1)) {
					if (board[i][j - 2] == confirmColor && board[i][j - 1] == null && board[i][j + 1] == null && board[i][j + 2] == confirmColor) {
						weight[i][j] += PRIORITY_1;
					}
				}
				//3.左上至右下
				if ((!(i - 2 < 0) && !(i + 2 > COLS - 1)) && (!(j - 2 < 0) && !(j + 2 > ROWS - 1))) {
					if (board[i - 2][j - 2] == confirmColor && board[i - 1][j - 1] == null
							&& board[i + 1][j + 1] == null && board[i + 2][j + 2] == confirmColor) {
						weight[i][j] += PRIORITY_1;
					}
				}
				//4.右上至左下
				if ((!(i + 2 > COLS - 1) && !(i - 2 < 0)) && (!(j - 2 < 0) && !(j + 2 > ROWS - 1))) {
					if (board[i + 2][j - 2] == confirmColor && board[i + 1][j - 1] == null
							&& board[i - 1][j + 1] == null && board[i - 2][j + 2] == confirmColor) {
						weight[i][j] += PRIORITY_1;
					}
				}
				
				//眠二●_ _ _● 右
				// 1.横向
				if (!(i - 3 < 0) && !(i + 1 > COLS - 1)) {
					if (board[i - 3][j] == confirmColor && board[i - 2][j] == null && board[i - 1][j] == null && board[i + 1][j] == confirmColor) {
						weight[i][j] += PRIORITY_1;
					}
				}
				//2.纵向
				if (!(j - 3 < 0) && !(j + 1 > ROWS - 1)) {
					if (board[i][j - 3] == confirmColor && board[i][j - 2] == null && board[i][j - 1] == null && board[i][j + 1] == confirmColor) {
						weight[i][j] += PRIORITY_1;
					}
				}
				//3.左上至右下
				if ((!(i - 3 < 0) && !(i + 1 > COLS - 1)) && (!(j - 3 < 0) && !(j + 1 > ROWS - 1))) {
					if (board[i - 3][j - 3] == confirmColor && board[i - 2][j - 2] == null
							&& board[i - 1][j - 1] == null && board[i + 1][j + 1] == confirmColor) {
						weight[i][j] += PRIORITY_1;
					}
				}
				//4.右上至左下
				if ((!(i + 3 > COLS - 1) && !(i - 1 < 0)) && (!(j - 3 < 0) && !(j + 1 > ROWS - 1))) {
					if (board[i + 3][j - 3] == confirmColor && board[i + 2][j - 2] == null
							&& board[i + 1][j - 1] == null && board[i - 1][j + 1] == confirmColor) {
						weight[i][j] += PRIORITY_1;
					}
				}
				
				if (firstDrop && confirmColor == Piece.B) {
					if (board[i][j] != null) {
						weight[i - 1][j - 1] = 1;
						weight[i][j - 1] = 1;
						weight[i + 1][j - 1] = 1;
						weight[i - 1][j] = 1;
						weight[i + 1][j] = 1;
						weight[i - 1][j + 1] = 1;
						weight[i][j + 1] = 1;
						weight[i + 1][j + 1] = 1;
						firstDrop = false;
					}
				}
			}
		}
		
		return weight;
	}

}
