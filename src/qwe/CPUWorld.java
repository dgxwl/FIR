package qwe;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class CPUWorld extends World {
	private static final long serialVersionUID = 1L;

	private static final int PRIORITY_7 = 1000;  //活四得分
	private static final int PRIORITY_6 = 999;  //冲四得分
	private static final int PRIORITY_5 = 400;  //活三得分
	private static final int PRIORITY_4 = 51;  //活二得分(靠近已有棋子)
	private static final int PRIORITY_3 = 50;  //活二得分(远离已有棋子)
	private static final int PRIORITY_2 = 10;  //眠三得分
	private static final int PRIORITY_1 = 2;  //眠二得分
	
	private boolean firstDrop = true;

	private int[][] cpuWeight;
	private int[][] playerWeight;
	
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

				board[col][row] = nextColor;
				checkWin();
				repaint();
				nextColor = (nextColor == Piece.B) ? Piece.W : Piece.B;
				
				//电脑方回合
				cpuAction();
				checkWin();
				repaint();
				nextColor = (nextColor == Piece.B) ? Piece.W : Piece.B;
			}
		};
		this.addMouseListener(l);
	}
	
	//电脑落子(白子是电脑)
	public void cpuAction() {
		cpuWeight = countWeight(Piece.W);
		playerWeight = countWeight(Piece.B);
		
		//打桩
		for (int i = 0; i < cpuWeight.length; i++) {
			for (int j = 0; j < cpuWeight[i].length; j++) {
				System.out.println("cpuWeight :  (" + i + ", " + j + ") ====> " + cpuWeight[i][j]);
			}
		}
		for (int i = 0; i < playerWeight.length; i++) {
			for (int j = 0; j < playerWeight[i].length; j++) {
				System.out.println("playerWeight :  (" + i + ", " + j + ") ====> " + playerWeight[i][j]);
			}
		}
		/////////////////////////////////////////////////////////////////
		
		Max cpuMax = new Max();
		for (int i = 0; i < cpuWeight.length; i++) {
			for (int j = 0; j < cpuWeight[i].length; j++) {
				if (cpuWeight[i][j] > cpuMax.score) {
					cpuMax.score = cpuWeight[i][j];
					cpuMax.i = i;
					cpuMax.j = j;
				}
			}
		}
		List<Place> cpuMaxPlaces = new ArrayList<>();
		cpuMaxPlaces.add(new Place(cpuMax.i, cpuMax.j));
		
		for (int i = 0; i < cpuWeight.length; i++) {
			for (int j = 0; j < cpuWeight[i].length; j++) {
				if (i == cpuMax.i && j == cpuMax.j) {
					continue;
				}
				if (cpuWeight[i][j] == cpuMax.score) {
					cpuMaxPlaces.add(new Place(i, j));
				}
			}
		}
		
		Max playerMax = new Max();
		for (int i = 0; i < playerWeight.length; i++) {
			for (int j = 0; j < playerWeight[i].length; j++) {
				if (playerWeight[i][j] > playerMax.score) {
					playerMax.score = playerWeight[i][j];
					playerMax.i = i;
					playerMax.j = j;
				}
			}
		}
		List<Place> playerMaxPlaces = new ArrayList<>();
		playerMaxPlaces.add(new Place(playerMax.i, playerMax.j));
		
		for (int i = 0; i < playerWeight.length; i++) {
			for (int j = 0; j < playerWeight[i].length; j++) {
				if (i == playerMax.i && j == playerMax.j) {
					continue;
				}
				if (playerWeight[i][j] == playerMax.score) {
					playerMaxPlaces.add(new Place(i, j));
				}
			}
		}
		
		if (cpuMax.score >= playerMax.score) {  //atk
			//找电脑方最高分之中玩家最高分位置
			for (Place place : cpuMaxPlaces) {
				if (cpuWeight[place.i][place.j] > cpuMax.score) {
					cpuMax.score = cpuWeight[place.i][place.j];
					cpuMax.i = place.i;
					cpuMax.j = place.j;
				}
			}
			board[cpuMax.i][cpuMax.j] = nextColor;System.out.println("attack: (" + cpuMax.i + ", " + cpuMax.j + "), cpuWeight: " + cpuWeight[cpuMax.i][cpuMax.j] + ", " + "playerWeight: " + playerWeight[cpuMax.i][cpuMax.j] + "cpuMaxScore: " + cpuMax.score + ", playerMaxScore: " + playerMax.score);
		} else {  //def
			//找玩家方最高分之中电脑最高分位置
			for (Place place : playerMaxPlaces) {
				if (playerWeight[place.i][place.j] > playerMax.score) {
					playerMax.score = playerWeight[place.i][place.j];
					playerMax.i = place.i;
					playerMax.j = place.j;
				}
			}
			board[playerMax.i][playerMax.j] = nextColor;System.out.println("defence: (" + playerMax.i + ", " + playerMax.j + "), cpuWeight: " + cpuWeight[playerMax.i][playerMax.j] + ", " + "playerWeight: " + playerWeight[playerMax.i][playerMax.j] + "cpuMaxScore: " + cpuMax.score + ", playerMaxScore: " + playerMax.score);
		}
	}

	public int[][] countWeight(Piece confirmColor) {
		int[][] weight = new int[COLS][ROWS]; // 所有位置的权值

		// 遍历棋盘计算权值
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[i].length; j++) {
				if (!firstDrop && board[i][j] != null) {
					weight[i][j] = -1;
					continue;
				}

				// 活四_●●●●_左边那颗
				// 1.横向
				if (i + 5 < COLS) {
					if (board[i + 1][j] == confirmColor && board[i + 2][j] == confirmColor && board[i + 3][j] == confirmColor
							&& board[i + 4][j] == confirmColor && board[i + 5][j] == null) {
						weight[i][j] += PRIORITY_7;
					}
				}
				//2.纵向
				if (j + 5 < ROWS) {
					if (board[i][j + 1] == confirmColor && board[i][j + 2] == confirmColor
							&& board[i][j + 3] == confirmColor && board[i][j + 4] == confirmColor && board[i][j + 5] == null) {
						weight[i][j] += PRIORITY_7;
					}
				}
				//3.左上至右下
				if (i + 5 < COLS && j + 5 < ROWS) {
					if (board[i + 1][j + 1] == confirmColor && board[i + 2][j + 2] == confirmColor
							&& board[i + 3][j + 3] == confirmColor && board[i + 4][j + 4] == confirmColor && board[i + 5][j + 5] == null) {
						weight[i][j] += PRIORITY_7;
					}
				}
				//4.右上至左下
				if (i - 5 >= 0 && j + 5 < ROWS) {
					if (board[i - 1][j + 1] == confirmColor && board[i - 2][j + 2] == confirmColor
							&& board[i - 3][j + 3] == confirmColor && board[i - 4][j + 4] == confirmColor && board[i - 5][j + 5] == null) {
						weight[i][j] += PRIORITY_7;
					}
				}
				
				// 活四_●●●●_右边那颗
				// 1.横向
				if (i - 5 >= 0) {
					if (board[i - 1][j] == confirmColor && board[i - 2][j] == confirmColor && board[i - 3][j] == confirmColor
							&& board[i - 4][j] == confirmColor && board[i - 5][j] == null) {
						weight[i][j] += PRIORITY_7;
					}
				}
				//2.纵向
				if (j - 5 >= 0) {
					if (board[i][j - 1] == confirmColor && board[i][j - 2] == confirmColor && board[i][j - 3] == confirmColor
							&& board[i][j - 4] == confirmColor && board[i][j - 5] == null) {
						weight[i][j] += PRIORITY_7;
					}
				}
				//3.右下至左上
				if (i - 5 >= 0 && j - 5 >= 0) {
					if (board[i - 1][j - 1] == confirmColor && board[i - 2][j - 2] == confirmColor
							&& board[i - 3][j - 3] == confirmColor && board[i - 4][j - 4] == confirmColor && board[i - 5][j - 5] == null) {
						weight[i][j] += PRIORITY_7;
					}
				}
				//4.左下至右上
				if (i + 5 < COLS && j - 5 >= 0) {
					if (board[i + 1][j - 1] == confirmColor && board[i + 2][j - 2] == confirmColor
							&& board[i + 3][j - 3] == confirmColor && board[i + 4][j - 4] == confirmColor && board[i + 5][j - 5] == null) {
						weight[i][j] += PRIORITY_7;
					}
				}
				
				Piece anotherColor = (confirmColor == Piece.B) ? Piece.W : Piece.B;
				//冲四_●●●●○
				// 1.横向
				if (i + 5 < COLS) {
					if (board[i + 1][j] == confirmColor && board[i + 2][j] == confirmColor && board[i + 3][j] == confirmColor
							&& board[i + 4][j] == confirmColor && board[i + 5][j] == anotherColor) {
						weight[i][j] += PRIORITY_6;
					}
				}
				//2.纵向
				if (j + 5 < ROWS) {
					if (board[i][j + 1] == confirmColor && board[i][j + 2] == confirmColor && board[i][j + 3] == confirmColor
							&& board[i][j + 4] == confirmColor && board[i][j + 5] == anotherColor) {
						weight[i][j] += PRIORITY_6;
					}
				}
				//3.左上至右下
				if (i + 5 < COLS && j + 5 < ROWS) {
					if (board[i + 1][j + 1] == confirmColor && board[i + 2][j + 2] == confirmColor
							&& board[i + 3][j + 3] == confirmColor && board[i + 4][j + 4] == confirmColor && board[i + 5][j + 5] == anotherColor) {
						weight[i][j] += PRIORITY_6;
					}
				}
				//4.右上至左下
				if (i - 5 >= 0 && j + 5 < ROWS) {
					if (board[i - 1][j + 1] == confirmColor && board[i - 2][j + 2] == confirmColor
							&& board[i - 3][j + 3] == confirmColor && board[i - 4][j + 4] == confirmColor && board[i - 5][j + 5] == anotherColor) {
						weight[i][j] += PRIORITY_6;
					}
				}
				
				//冲四○●●●●_
				// 1.横向
				if (i - 5 >= 0) {
					if (board[i - 1][j] == confirmColor && board[i - 2][j] == confirmColor && board[i - 3][j] == confirmColor
							&& board[i - 4][j] == confirmColor && board[i - 5][j] == anotherColor) {
						weight[i][j] += PRIORITY_6;
					}
				}
				//2.纵向
				if (j - 5 >= 0) {
					if (board[i][j - 1] == confirmColor && board[i][j - 2] == confirmColor && board[i][j - 3] == confirmColor
							&& board[i][j - 4] == confirmColor && board[i][j - 5] == anotherColor) {
						weight[i][j] += PRIORITY_6;
					}
				}
				//3.右下至左上
				if (i - 5 >= 0 && j - 5 >= 0) {
					if (board[i - 1][j - 1] == confirmColor && board[i - 2][j - 2] == confirmColor
							&& board[i - 3][j - 3] == confirmColor && board[i - 4][j - 4] == confirmColor && board[i - 5][j - 5] == anotherColor) {
						weight[i][j] += PRIORITY_6;
					}
				}
				//4.左下至右上
				if (i + 5 < COLS && j - 5 >= 0) {
					if (board[i + 1][j - 1] == confirmColor && board[i + 2][j - 2] == confirmColor
							&& board[i + 3][j - 3] == confirmColor && board[i + 4][j - 4] == confirmColor && board[i + 5][j - 5] == anotherColor) {
						weight[i][j] += PRIORITY_6;
					}
				}
				
				//冲四●_●●●
				// 1.横向
				if (i - 1 >= 0 && i + 3 < COLS) {
					if (board[i - 1][j] == confirmColor && board[i + 1][j] == confirmColor && board[i + 2][j] == confirmColor && board[i + 3][j] == confirmColor) {
						weight[i][j] += PRIORITY_6;
					}
				}
				//2.纵向
				if (j - 1 >= 0 && j + 3 < ROWS) {
					if (board[i][j - 1] == confirmColor && board[i][j + 1] == confirmColor && board[i][j + 2] == confirmColor && board[i][j + 3] == confirmColor) {
						weight[i][j] += PRIORITY_6;
					}
				}
				//3.左上至右下
				if ((i - 1 >= 0 && i + 3 < COLS) && (j - 1 >= 0 && j + 3 < ROWS)) {
					if (board[i - 1][j - 1] == confirmColor && board[i + 1][j + 1] == confirmColor
							&& board[i + 2][j + 2] == confirmColor && board[i + 3][j + 3] == confirmColor) {
						weight[i][j] += PRIORITY_6;
					}
				}
				//4.右上至左下
				if ((i + 1 < COLS && i - 3 >= 0) && (j - 1 >= 0 && j + 3 < ROWS)) {
					if (board[i + 1][j - 1] == confirmColor && board[i - 1][j + 1] == confirmColor
							&& board[i - 2][j + 2] == confirmColor && board[i - 3][j + 3] == confirmColor) {
						weight[i][j] += PRIORITY_6;
					}
				}
				
				//冲四●●●_●
				// 1.横向
				if (i + 1 < COLS && i - 3 >= 0) {
					if (board[i + 1][j] == confirmColor && board[i - 1][j] == confirmColor
							&& board[i - 2][j] == confirmColor && board[i - 3][j] == confirmColor) {
						weight[i][j] += PRIORITY_6;
					}
				}
				//2.纵向
				if (j + 1 < ROWS && j - 3 >= 0) {
					if (board[i][j + 1] == confirmColor && board[i][j - 1] == confirmColor
							&& board[i][j - 2] == confirmColor && board[i][j - 3] == confirmColor) {
						weight[i][j] += PRIORITY_6;
					}
				}
				//3.右下至左上
				if ((i + 1 < COLS && i - 3 >= 0) && (j + 1 < ROWS && j - 3 >= 0)) {
					if (board[i + 1][j + 1] == confirmColor && board[i - 1][j - 1] == confirmColor
							&& board[i - 2][j - 2] == confirmColor && board[i - 3][j - 3] == confirmColor) {
						weight[i][j] += PRIORITY_6;
					}
				}
				//4.左下至右上
				if ((i - 1 >= 0 && (i + 3 < COLS)) && (j + 1 < ROWS && j - 3 >= 0)) {
					if (board[i - 1][j + 1] == confirmColor && board[i + 1][j - 1] == confirmColor
							&& board[i + 2][j - 2] == confirmColor && board[i + 3][j - 3] == confirmColor) {
						weight[i][j] += PRIORITY_6;
					}
				}
				
				//冲四●●_●●
				// 1.横向
				if (i - 2 >= 0 && i + 2 < COLS) {
					if (board[i - 2][j] == confirmColor && board[i - 1][j] == confirmColor && board[i + 1][j] == confirmColor && board[i + 2][j] == confirmColor) {
						weight[i][j] += PRIORITY_6;
					}
				}
				//2.纵向
				if (j - 2 >= 0 && j + 2 < ROWS) {
					if (board[i][j - 2] == confirmColor && board[i][j - 1] == confirmColor && board[i][j + 1] == confirmColor && board[i][j + 2] == confirmColor) {
						weight[i][j] += PRIORITY_6;
					}
				}
				//3.左上至右下
				if ((i - 2 >= 0 && i + 2 < COLS) && (j - 2 >= 0 && j + 2 < ROWS)) {
					if (board[i - 2][j - 2] == confirmColor && board[i - 1][j - 1] == confirmColor
							&& board[i + 1][j + 1] == confirmColor && board[i + 2][j + 2] == confirmColor) {
						weight[i][j] += PRIORITY_6;
					}
				}
				//4.右上至左下
				if ((i + 2 < COLS && i - 2 >= 0) && (j - 2 >= 0 && j + 2 < ROWS)) {
					if (board[i + 2][j - 2] == confirmColor && board[i + 1][j - 1] == confirmColor
							&& board[i - 1][j + 1] == confirmColor && board[i - 2][j + 2] == confirmColor) {
						weight[i][j] += PRIORITY_6;
					}
				}
				
				// 活三_●●●_左边那颗
				// 1.横向
				if (i + 4 < COLS) {
					if (board[i + 1][j] == confirmColor && board[i + 2][j] == confirmColor && board[i + 3][j] == confirmColor
							&& board[i + 4][j] == null) {
						weight[i][j] += PRIORITY_5;
					}
				}
				//2.纵向
				if (j + 4 < ROWS) {
					if (board[i][j + 1] == confirmColor && board[i][j + 2] == confirmColor
							&& board[i][j + 3] == confirmColor && board[i][j + 4] == null) {
						weight[i][j] += PRIORITY_5;
					}
				}
				//3.左上至右下
				if (i + 4 < COLS && j + 4 < ROWS) {
					if (board[i + 1][j + 1] == confirmColor && board[i + 2][j + 2] == confirmColor
							&& board[i + 3][j + 3] == confirmColor && board[i + 4][j + 4] == null) {
						weight[i][j] += PRIORITY_5;
					}
				}
				//4.右上至左下
				if (i - 4 >= 0 && j + 4 < ROWS) {
					if (board[i - 1][j + 1] == confirmColor && board[i - 2][j + 2] == confirmColor
							&& board[i - 3][j + 3] == confirmColor && board[i - 4][j + 4] == null) {
						weight[i][j] += PRIORITY_5;
					}
				}
				
				// 活三_●●●_右边那颗
				// 1.横向
				if (i - 4 >= 0) {
					if (board[i - 1][j] == confirmColor && board[i - 2][j] == confirmColor && board[i - 3][j] == confirmColor && board[i - 4][j] == null) {
						weight[i][j] += PRIORITY_5;
					}
				}
				//2.纵向
				if (j - 4 >= 0) {
					if (board[i][j - 1] == confirmColor && board[i][j - 2] == confirmColor && board[i][j - 3] == confirmColor && board[i][j - 4] == null) {
						weight[i][j] += PRIORITY_5;
					}
				}
				//3.右下至左上
				if (i - 4 >= 0 && j - 4 >= 0) {
					if (board[i - 1][j - 1] == confirmColor && board[i - 2][j - 2] == confirmColor
							&& board[i - 3][j - 3] == confirmColor && board[i - 4][j - 4] == null) {
						weight[i][j] += PRIORITY_5;
					}
				}
				//4.左下至右上
				if (i + 4 < COLS && j - 4 >= 0) {
					if (board[i + 1][j - 1] == confirmColor && board[i + 2][j - 2] == confirmColor
							&& board[i + 3][j - 3] == confirmColor && board[i + 4][j - 4] == null) {
						weight[i][j] += PRIORITY_5;
					}
				}
				
				// 活三_●_●●_左边那颗
				// 1.横向
				if (i + 5 < COLS) {
					if (board[i + 1][j] == confirmColor && board[i + 2][j] == null && board[i + 3][j] == confirmColor
							&& board[i + 4][j] == confirmColor && board[i + 5][j] == null) {
						weight[i][j] += PRIORITY_5;
					}
				}
				//2.纵向
				if (j + 5 < ROWS) {
					if (board[i][j + 1] == confirmColor && board[i][j + 2] == null && board[i][j + 3] == confirmColor
							&& board[i][j + 4] == confirmColor && board[i][j + 5] == null) {
						weight[i][j] += PRIORITY_5;
					}
				}
				//3.左上至右下
				if (i + 5 < COLS && j + 5 < ROWS) {
					if (board[i + 1][j + 1] == confirmColor && board[i + 2][j + 2] == null && board[i + 3][j + 3] == confirmColor
							&& board[i + 4][j + 4] == confirmColor && board[i + 5][j + 5] == null) {
						weight[i][j] += PRIORITY_5;
					}
				}
				//4.右上至左下
				if (i - 5 >= 0 && j + 5 < ROWS) {
					if (board[i - 1][j + 1] == confirmColor && board[i - 2][j + 2] == null
							&& board[i - 3][j + 3] == confirmColor && board[i - 4][j + 4] == confirmColor && board[i - 5][j + 5] == null) {
						weight[i][j] += PRIORITY_5;
					}
				}
				
				// 活三_●_●●_中间那颗
				// 1.横向
				if (i - 2 >= 0 && i + 3 < COLS) {
					if (board[i - 2][j] == null && board[i - 1][j] == confirmColor && board[i + 1][j] == confirmColor
							&& board[i + 2][j] == confirmColor && board[i + 3][j] == null) {
						weight[i][j] += PRIORITY_5;
					}
				}
				//2.纵向
				if (j - 2 >= 0 && j + 3 < ROWS) {
					if (board[i][j - 2] == null && board[i][j - 1] == confirmColor && board[i][j + 1] == confirmColor
							&& board[i][j + 2] == confirmColor && board[i][j + 3] == null) {
						weight[i][j] += PRIORITY_5;
					}
				}
				//3.左上至右下
				if ((i - 2 >= 0 && i + 3 < COLS) && (j - 2 >= 0 && j + 3 < ROWS)) {
					if (board[i - 2][j - 2] == null && board[i - 1][j - 1] == confirmColor && board[i + 1][j + 1] == confirmColor
							&& board[i + 2][j + 2] == confirmColor && board[i + 3][j + 3] == null) {
						weight[i][j] += PRIORITY_5;
					}
				}
				//4.右上至左下
				if ((i + 2 < COLS && i - 3 >= 0) && (j - 2 >= 0 && j + 3 < ROWS)) {
					if (board[i + 2][j - 2] == null && board[i + 1][j - 1] == confirmColor && board[i - 1][j + 1] == confirmColor
							&& board[i - 2][j + 2] == confirmColor && board[i - 3][j + 3] == null) {
						weight[i][j] += PRIORITY_5;
					}
				}
				
				// 活三_●_●●_右边那颗
				// 1.横向
				if (i - 5 >= 0) {
					if (board[i - 1][j] == confirmColor && board[i - 2][j] == confirmColor && board[i - 3][j] == null
							&& board[i - 4][j] == confirmColor && board[i - 5][j] == null) {
						weight[i][j] += PRIORITY_5;
					}
				}
				//2.纵向
				if (j - 5 >= 0) {
					if (board[i][j - 1] == confirmColor && board[i][j - 2] == confirmColor && board[i][j - 3] == null
							&& board[i][j - 4] == confirmColor && board[i][j - 5] == null) {
						weight[i][j] += PRIORITY_5;
					}
				}
				//3.右下至左上
				if (i - 5 >= 0 && j - 5 >= 0) {
					if (board[i - 1][j - 1] == confirmColor && board[i - 2][j - 2] == confirmColor
							&& board[i - 3][j - 3] == null && board[i - 4][j - 4] == confirmColor && board[i - 5][j - 5] == null) {
						weight[i][j] += PRIORITY_5;
					}
				}
				//4.左下至右上
				if (i + 5 < COLS && j - 5 >= 0) {
					if (board[i + 1][j - 1] == confirmColor && board[i + 2][j - 2] == confirmColor
							&& board[i + 3][j - 3] == null && board[i + 4][j - 4] == confirmColor && board[i + 5][j - 5] == null) {
						weight[i][j] += PRIORITY_5;
					}
				}
				
				// 活三_●●_●_左边那颗
				// 1.横向
				if (i + 5 < COLS) {
					if (board[i + 1][j] == confirmColor && board[i + 2][j] == confirmColor && board[i + 3][j] == null
							&& board[i + 4][j] == confirmColor && board[i + 5][j] == null) {
						weight[i][j] += PRIORITY_5;
					}
				}
				//2.纵向
				if (j + 5 < ROWS) {
					if (board[i][j + 1] == confirmColor && board[i][j + 2] == confirmColor && board[i][j + 3] == null
							&& board[i][j + 4] == confirmColor && board[i][j + 5] == null) {
						weight[i][j] += PRIORITY_5;
					}
				}
				//3.左上至右下
				if (i + 5 < COLS && j + 5 < ROWS) {
					if (board[i + 1][j + 1] == confirmColor && board[i + 2][j + 2] == confirmColor
							&& board[i + 3][j + 3] == null && board[i + 4][j + 4] == confirmColor && board[i + 5][j + 5] == null) {
						weight[i][j] += PRIORITY_5;
					}
				}
				//4.右上至左下
				if (i - 5 >= 0 && j + 5 < ROWS) {
					if (board[i - 1][j + 1] == confirmColor && board[i - 2][j + 2] == confirmColor
							&& board[i - 3][j + 3] == null && board[i - 4][j + 4] == confirmColor && board[i - 5][j + 5] == null) {
						weight[i][j] += PRIORITY_5;
					}
				}
				
				// 活三_●●_●_中间那颗
				// 1.横向
				if (i - 3 >= 0 && i + 2 < COLS) {
					if (board[i - 3][j] == null && board[i - 2][j] == confirmColor && board[i - 1][j] == confirmColor
							&& board[i + 1][j] == confirmColor && board[i + 2][j] == null) {
						weight[i][j] += PRIORITY_5;
					}
				}
				//2.纵向
				if (j - 3 >= 0 && j + 2 < ROWS) {
					if (board[i][j - 3] == null && board[i][j - 2] == confirmColor && board[i][j - 1] == confirmColor
							&& board[i][j + 1] == confirmColor && board[i][j + 2] == null) {
						weight[i][j] += PRIORITY_5;
					}
				}
				//3.左上至右下
				if ((i - 3 >= 0 && i + 2 < COLS) && (j - 3 >= 0 && j + 2 < ROWS)) {
					if (board[i - 3][j - 3] == null && board[i - 2][j - 2] == confirmColor && board[i - 1][j - 1] == confirmColor
							&& board[i + 1][j + 1] == confirmColor && board[i + 2][j + 2] == null) {
						weight[i][j] += PRIORITY_5;
					}
				}
				//4.右上至左下
				if ((i + 3 < COLS && i - 2 >= 0) && (j - 3 >= 0 && j + 2 < ROWS)) {
					if (board[i + 3][j - 3] == null && board[i + 2][j - 2] == confirmColor && board[i + 1][j - 1] == confirmColor
							&& board[i - 1][j + 1] == confirmColor && board[i - 2][j + 2] == null) {
						weight[i][j] += PRIORITY_5;
					}
				}
				
				// 活三_●●_●_右边那颗
				// 1.横向
				if (i - 5 >= 0) {
					if (board[i - 1][j] == confirmColor && board[i - 2][j] == null && board[i - 3][j] == confirmColor
							&& board[i - 4][j] == confirmColor && board[i - 5][j] == null) {
						weight[i][j] += PRIORITY_5;
					}
				}
				//2.纵向
				if (j - 5 >= 0) {
					if (board[i][j - 1] == confirmColor && board[i][j - 2] == null && board[i][j - 3] == confirmColor
							&& board[i][j - 4] == confirmColor && board[i][j - 5] == null) {
						weight[i][j] += PRIORITY_5;
					}
				}
				//3.右下至左上
				if (i - 5 >= 0 && j - 5 >= 0) {
					if (board[i - 1][j - 1] == confirmColor && board[i - 2][j - 2] == null && board[i - 3][j - 3] == confirmColor
							&& board[i - 4][j - 4] == confirmColor && board[i - 5][j - 5] == null) {
						weight[i][j] += PRIORITY_5;
					}
				}
				//4.左下至右上
				if (i + 5 < COLS && j - 5 >= 0) {
					if (board[i + 1][j - 1] == confirmColor && board[i + 2][j - 2] == null && board[i + 3][j - 3] == confirmColor
							&& board[i + 4][j - 4] == confirmColor && board[i + 5][j - 5] == null) {
						weight[i][j] += PRIORITY_5;
					}
				}
				
				// 活二__●●__最左边那颗
				// 1.横向
				if (i + 5 < COLS) {
					if (board[i + 1][j] == null && board[i + 2][j] == confirmColor && board[i + 3][j] == confirmColor
							&& board[i + 4][j] == null && board[i + 5][j] == null) {
						weight[i][j] += PRIORITY_3;
					}
				}
				//2.纵向
				if (j + 5 < ROWS) {
					if (board[i][j + 1] == null && board[i][j + 2] == confirmColor && board[i][j + 3] == confirmColor
							&& board[i][j + 4] == null && board[i][j + 5] == null) {
						weight[i][j] += PRIORITY_3;
					}
				}
				//3.左上至右下
				if (i + 5 < COLS && j + 5 < ROWS) {
					if (board[i + 1][j + 1] == null && board[i + 2][j + 2] == confirmColor
							&& board[i + 3][j + 3] == confirmColor && board[i + 4][j + 4] == null && board[i + 5][j + 5] == null) {
						weight[i][j] += PRIORITY_3;
					}
				}
				//4.右上至左下
				if (i - 5 >= 0 && j + 5 < ROWS) {
					if (board[i - 1][j + 1] == null && board[i - 2][j + 2] == confirmColor
							&& board[i - 3][j + 3] == confirmColor && board[i - 4][j + 4] == null && board[i - 5][j + 5] == null) {
						weight[i][j] += PRIORITY_3;
					}
				}
				
				// 活二__●●__左二那颗
				// 1.横向
				if (i - 1 >= 0 && i + 4 < COLS) {
					if (board[i - 1][j] == null && board[i + 1][j] == confirmColor && board[i + 2][j] == confirmColor
							&& board[i + 3][j] == null && board[i + 4][j] == null) {
						weight[i][j] += PRIORITY_4;
					}
				}
				//2.纵向
				if (j - 1 >= 0 && j + 4 < ROWS) {
					if (board[i][j - 1] == null && board[i][j + 1] == confirmColor
							&& board[i][j + 2] == confirmColor && board[i][j + 3] == null && board[i][j + 4] == null) {
						weight[i][j] += PRIORITY_4;
					}
				}
				//3.左上至右下
				if ((i - 1 >= 0 && i + 4 < COLS) && (j - 1 >= 0 && j + 4 < ROWS)) {
					if (board[i - 1][j - 1] == null && board[i + 1][j + 1] == confirmColor
							&& board[i + 2][j + 2] == confirmColor && board[i + 3][j + 3] == null && board[i + 4][j + 4] == null) {
						weight[i][j] += PRIORITY_4;
					}
				}
				//4.右上至左下
				if ((i + 1 < COLS && i - 4 >= 0) && (j - 1 >= 0 && j + 4 < ROWS)) {
					if (board[i + 1][j - 1] == null && board[i - 1][j + 1] == confirmColor
							&& board[i - 2][j + 2] == confirmColor && board[i - 3][j + 3] == null && board[i - 4][j + 4] == null) {
						weight[i][j] += PRIORITY_4;
					}
				}
				
				// 活二__●●__右二那颗
				// 1.横向
				if (i - 4 >= 0 && i + 1 < COLS) {
					if (board[i - 4][j] == null && board[i - 3][j] == null && board[i - 2][j] == confirmColor
							&& board[i - 1][j] == confirmColor && board[i + 1][j] == null) {
						weight[i][j] += PRIORITY_4;
					}
				}
				//2.纵向
				if (j - 4 >= 0 && j + 1 < ROWS) {
					if (board[i][j - 4] == null && board[i][j - 3] == null && board[i][j - 2] == confirmColor
							&& board[i][j - 1] == confirmColor && board[i][j + 1] == null) {
						weight[i][j] += PRIORITY_4;
					}
				}
				//3.左上至右下
				if ((i - 4 >= 0 && i + 1 < COLS) && (j - 4 >= 0 && j + 1 < ROWS)) {
					if (board[i - 4][j - 4] == null && board[i - 3][j - 3] == null
							&& board[i - 2][j - 2] == confirmColor && board[i - 1][j - 1] == confirmColor && board[i + 1][j + 1] == null) {
						weight[i][j] += PRIORITY_4;
					}
				}
				//4.右上至左下
				if ((i + 4 < COLS && i - 1 >= 0) && (j - 4 >= 0 && j + 1 < ROWS)) {
					if (board[i + 4][j - 4] == null && board[i + 3][j - 3] == null
							&& board[i + 2][j - 2] == confirmColor && board[i + 1][j - 1] == confirmColor && board[i - 1][j + 1] == null) {
						weight[i][j] += PRIORITY_4;
					}
				}
				
				// 活二__●●__最右边那颗
				// 1.横向
				if (i - 5 >= 0) {
					if (board[i - 1][j] == null && board[i - 2][j] == confirmColor && board[i - 3][j] == confirmColor
							&& board[i - 4][j] == null && board[i - 5][j] == null) {
						weight[i][j] += PRIORITY_3;
					}
				}
				//2.纵向
				if (j - 5 >= 0) {
					if (board[i][j - 1] == null && board[i][j - 2] == confirmColor
							&& board[i][j - 3] == confirmColor && board[i][j - 4] == null && board[i][j - 5] == null) {
						weight[i][j] += PRIORITY_3;
					}
				}
				//3.左上至右下
				if (i - 5 >= 0 && j - 5 >= 0) {
					if (board[i - 1][j - 1] == null && board[i - 2][j - 2] == confirmColor
							&& board[i - 3][j - 3] == confirmColor && board[i - 4][j - 4] == null && board[i - 5][j - 5] == null) {
						weight[i][j] += PRIORITY_3;
					}
				}
				//4.右上至左下
				if (i + 5 < COLS && j - 5 >= 0) {
					if (board[i + 1][j - 1] == null && board[i + 2][j - 2] == confirmColor
							&& board[i + 3][j - 3] == confirmColor && board[i + 4][j - 4] == null && board[i + 5][j - 5] == null) {
						weight[i][j] += PRIORITY_3;
					}
				}
				
				// 活二_●_●_最左边那颗
				// 1.横向
				if (i + 4 < COLS) {
					if (board[i + 1][j] == confirmColor && board[i + 2][j] == null && board[i + 3][j] == confirmColor && board[i + 4][j] == null) {
						weight[i][j] += PRIORITY_4;
					}
				}
				//2.纵向
				if (j + 4 < ROWS) {
					if (board[i][j + 1] == confirmColor && board[i][j + 2] == null && board[i][j + 3] == confirmColor && board[i][j + 4] == null) {
						weight[i][j] += PRIORITY_4;
					}
				}
				//3.左上至右下
				if (i + 4 < COLS && j + 4 < ROWS) {
					if (board[i + 1][j + 1] == confirmColor && board[i + 2][j + 2] == null
							&& board[i + 3][j + 3] == confirmColor && board[i + 4][j + 4] == null) {
						weight[i][j] += PRIORITY_4;
					}
				}
				//4.右上至左下
				if (i - 4 >= 0 && j + 4 < ROWS) {
					if (board[i - 1][j + 1] == confirmColor && board[i - 2][j + 2] == null
							&& board[i - 3][j + 3] == confirmColor && board[i - 4][j + 4] == null) {
						weight[i][j] += PRIORITY_4;
					}
				}
				
				// 活二_●_●_中间那颗
				// 1.横向
				if (i - 2 >= 0 && i + 2 < COLS) {
					if (board[i - 2][j] == null && board[i - 1][j] == confirmColor && board[i + 1][j] == confirmColor && board[i + 2][j] == null) {
						weight[i][j] += PRIORITY_4;
					}
				}
				//2.纵向
				if (j - 2 >= 0 && j + 2 < ROWS) {
					if (board[i][j - 2] == null && board[i][j - 1] == confirmColor && board[i][j + 1] == confirmColor && board[i][j + 2] == null) {
						weight[i][j] += PRIORITY_4;
					}
				}
				//3.左上至右下
				if ((i - 2 >= 0 && i + 2 < COLS) && (j - 2 >= 0 && j + 2 < ROWS)) {
					if (board[i - 2][j - 2] == null && board[i - 1][j - 1] == confirmColor
							&& board[i + 1][j + 1] == confirmColor && board[i + 2][j + 2] == null) {
						weight[i][j] += PRIORITY_4;
					}
				}
				//4.右上至左下
				if ((i + 2 < COLS && i - 2 >= 0) && (j - 2 >= 0 && j + 2 < ROWS)) {
					if (board[i + 2][j - 2] == null && board[i + 1][j - 1] == confirmColor
							&& board[i - 1][j + 1] == confirmColor && board[i - 2][j + 2] == null) {
						weight[i][j] += PRIORITY_4;
					}
				}
				
				// 活二_●_●_最右边那颗
				// 1.横向
				if (i - 4 >= 0) {
					if (board[i - 1][j] == confirmColor && board[i - 2][j] == null && board[i - 3][j] == confirmColor && board[i - 4][j] == null) {
						weight[i][j] += PRIORITY_4;
					}
				}
				//2.纵向
				if (j - 4 >= 0) {
					if (board[i][j - 1] == confirmColor && board[i][j - 2] == null && board[i][j - 3] == confirmColor && board[i][j - 4] == null) {
						weight[i][j] += PRIORITY_4;
					}
				}
				//3.左上至右下
				if (i - 4 >= 0 && j - 4 >= 0) {
					if (board[i - 1][j - 1] == confirmColor && board[i - 2][j - 2] == null
							&& board[i - 3][j - 3] == confirmColor && board[i - 4][j - 4] == null) {
						weight[i][j] += PRIORITY_4;
					}
				}
				//4.右上至左下
				if (i + 4 < COLS && j - 4 >= 0) {
					if (board[i + 1][j - 1] == confirmColor && board[i + 2][j - 2] == null
							&& board[i + 3][j - 3] == confirmColor && board[i + 4][j - 4] == null) {
						weight[i][j] += PRIORITY_4;
					}
				}
				
				// 活二_●__●_最左边那颗
				// 1.横向
				if (i + 5 < COLS) {
					if (board[i + 1][j] == confirmColor && board[i + 2][j] == null && board[i + 3][j] == null
							&& board[i + 4][j] == confirmColor && board[i + 5][j] == null) {
						weight[i][j] += PRIORITY_3;
					}
				}
				//2.纵向
				if (j + 5 < ROWS) {
					if (board[i][j + 1] == confirmColor && board[i][j + 2] == null && board[i][j + 3] == null
							&& board[i][j + 4] == confirmColor && board[i][j + 5] == null) {
						weight[i][j] += PRIORITY_3;
					}
				}
				//3.左上至右下
				if (i + 5 < COLS && j + 5 < ROWS) {
					if (board[i + 1][j + 1] == confirmColor && board[i + 2][j + 2] == null && board[i + 3][j + 3] == null
							&& board[i + 4][j + 4] == confirmColor && board[i + 5][j + 5] == null) {
						weight[i][j] += PRIORITY_3;
					}
				}
				//4.右上至左下
				if (i - 5 >= 0 && j + 5 < ROWS) {
					if (board[i - 1][j + 1] == confirmColor && board[i - 2][j + 2] == null && board[i - 3][j + 3] == null
							&& board[i - 4][j + 4] == confirmColor && board[i - 5][j + 5] == null) {
						weight[i][j] += PRIORITY_3;
					}
				}
				
				// 活二_●__●_左二那颗
				// 1.横向
				if (i - 2 >= 0 && i + 3 < COLS) {
					if (board[i - 2][j] == null && board[i - 1][j] == confirmColor && board[i + 1][j] == null
							&& board[i + 2][j] == confirmColor && board[i + 3][j] == null) {
						weight[i][j] += PRIORITY_3;
					}
				}
				//2.纵向
				if (j - 2 >= 0 && j + 3 < ROWS) {
					if (board[i][j - 2] == null && board[i][j - 1] == confirmColor && board[i][j + 1] == null
							&& board[i][j + 2] == confirmColor && board[i][j + 3] == null) {
						weight[i][j] += PRIORITY_3;
					}
				}
				//3.左上至右下
				if ((i - 2 >= 0 && i + 3 < COLS) && (j - 2 >= 0 && j + 3 < ROWS)) {
					if (board[i - 2][j - 2] == null && board[i - 1][j - 1] == confirmColor && board[i + 1][j + 1] == null
							&& board[i + 2][j + 2] == confirmColor && board[i + 3][j + 3] == null) {
						weight[i][j] += PRIORITY_3;
					}
				}
				//4.右上至左下
				if (i + 2 < COLS && (i - 3 >= 0) && (j - 2 >= 0 && j + 3 < ROWS)) {
					if (board[i + 2][j - 2] == null && board[i + 1][j - 1] == confirmColor && board[i - 1][j + 1] == null
							&& board[i - 2][j + 2] == confirmColor && board[i - 3][j + 3] == null) {
						weight[i][j] += PRIORITY_3;
					}
				}
				
				// 活二_●__●_右二那颗
				// 1.横向
				if (i - 3 >= 0 && i + 2 < COLS) {
					if (board[i - 3][j] == null && board[i - 2][j] == confirmColor && board[i - 1][j] == null
							&& board[i + 1][j] == confirmColor && board[i + 2][j] == null) {
						weight[i][j] += PRIORITY_3;
					}
				}
				//2.纵向
				if (j - 3 >= 0 && j + 2 < ROWS) {
					if (board[i][j - 3] == null && board[i][j - 2] == confirmColor && board[i][j - 1] == null
							&& board[i][j + 1] == confirmColor && board[i][j + 2] == null) {
						weight[i][j] += PRIORITY_3;
					}
				}
				//3.左上至右下
				if ((i - 3 >= 0 && i + 2 < COLS) && (j - 3 >= 0 && j + 2 < ROWS)) {
					if (board[i - 3][j - 3] == null && board[i - 2][j - 2] == confirmColor && board[i - 1][j - 1] == null
							&& board[i + 1][j + 1] == confirmColor && board[i + 2][j + 2] == null) {
						weight[i][j] += PRIORITY_3;
					}
				}
				//4.右上至左下
				if ((i + 3 < COLS) && (i - 2 >= 0) && (j - 3 >= 0 && j + 2 < ROWS)) {
					if (board[i + 3][j - 3] == null && board[i + 2][j - 2] == confirmColor && board[i + 1][j - 1] == null
							&& board[i - 1][j + 1] == confirmColor && board[i - 2][j + 2] == null) {
						weight[i][j] += PRIORITY_3;
					}
				}
				
				// 活二_●__●_最右边那颗
				// 1.横向
				if (i - 5 >= 0) {
					if (board[i - 1][j] == confirmColor && board[i - 2][j] == null && board[i - 3][j] == null
							&& board[i - 4][j] == confirmColor && board[i - 5][j] == null) {
						weight[i][j] += PRIORITY_3;
					}
				}
				//2.纵向
				if (j - 5 >= 0) {
					if (board[i][j - 1] == confirmColor && board[i][j - 2] == null && board[i][j - 3] == null
							&& board[i][j - 4] == confirmColor && board[i][j - 5] == null) {
						weight[i][j] += PRIORITY_3;
					}
				}
				//3.左上至右下
				if (i - 5 >= 0 && j - 5 >= 0) {
					if (board[i - 1][j - 1] == confirmColor && board[i - 2][j - 2] == null && board[i - 3][j - 3] == null
							&& board[i - 4][j - 4] == confirmColor && board[i - 5][j - 5] == null) {
						weight[i][j] += PRIORITY_3;
					}
				}
				//4.右上至左下
				if (i + 5 < COLS && j - 5 >= 0) {
					if (board[i + 1][j - 1] == confirmColor && board[i + 2][j - 2] == null && board[i + 3][j - 3] == null
							&& board[i + 4][j - 4] == confirmColor && board[i + 5][j - 5] == null) {
						weight[i][j] += PRIORITY_3;
					}
				}
				
				//眠三__●●●○ 左
				// 1.横向
				if (i + 5 < COLS) {
					if (board[i + 1][j] == null && board[i + 2][j] == confirmColor && board[i + 3][j] == confirmColor
							&& board[i + 4][j] == confirmColor && board[i + 5][j] == anotherColor) {
						weight[i][j] += PRIORITY_2;
					}
				}
				//2.纵向
				if (j + 5 < ROWS) {
					if (board[i][j + 1] == null && board[i][j + 2] == confirmColor && board[i][j + 3] == confirmColor
							&& board[i][j + 4] == confirmColor && board[i][j + 5] == anotherColor) {
						weight[i][j] += PRIORITY_2;
					}
				}
				//3.左上至右下
				if (i + 5 < COLS && j + 5 < ROWS) {
					if (board[i + 1][j + 1] == null && board[i + 2][j + 2] == confirmColor && board[i + 3][j + 3] == confirmColor
							&& board[i + 4][j + 4] == confirmColor && board[i + 5][j + 5] == anotherColor) {
						weight[i][j] += PRIORITY_2;
					}
				}
				//4.右上至左下
				if ((i - 5 >= 0) && j + 5 < ROWS) {
					if (board[i - 1][j + 1] == null && board[i - 2][j + 2] == confirmColor && board[i - 3][j + 3] == confirmColor
							&& board[i - 4][j + 4] == confirmColor && board[i - 5][j + 5] == anotherColor) {
						weight[i][j] += PRIORITY_2;
					}
				}
				
				//眠三__●●●○ 右
				// 1.横向
				if (i - 1 >= 0 && i + 4 < COLS) {
					if (board[i - 1][j] == null && board[i + 1][j] == confirmColor && board[i + 2][j] == confirmColor
							&& board[i + 3][j] == confirmColor && board[i + 4][j] == anotherColor) {
						weight[i][j] += PRIORITY_2;
					}
				}
				//2.纵向
				if (j - 1 >= 0 && j + 4 < ROWS) {
					if (board[i][j - 1] == null && board[i][j + 1] == confirmColor && board[i][j + 2] == confirmColor
							&& board[i][j + 3] == confirmColor && board[i][j + 4] == anotherColor) {
						weight[i][j] += PRIORITY_2;
					}
				}
				//3.左上至右下
				if ((i - 1 >= 0 && i + 4 < COLS) && (j - 1 >= 0 && j + 4 < ROWS)) {
					if (board[i - 1][j - 1] == null && board[i + 1][j + 1] == confirmColor && board[i + 2][j + 2] == confirmColor
							&& board[i + 3][j + 3] == confirmColor && board[i + 4][j + 4] == anotherColor) {
						weight[i][j] += PRIORITY_2;
					}
				}
				//4.右上至左下
				if ((i + 1 < COLS && i - 4 >= 0) && (j - 1 >= 0 && j + 4 < ROWS)) {
					if (board[i + 1][j - 1] == null && board[i - 1][j + 1] == confirmColor && board[i - 2][j + 2] == confirmColor
							&& board[i - 3][j + 3] == confirmColor && board[i - 4][j + 4] == anotherColor) {
						weight[i][j] += PRIORITY_2;
					}
				}
				
				//眠三○●●●__ 左
				// 1.横向
				if (i - 4 >= 0 && i + 1 < COLS) {
					if (board[i - 4][j] == anotherColor && board[i - 3][j] == confirmColor && board[i - 2][j] == confirmColor
							&& board[i - 1][j] == confirmColor && board[i + 1][j] == null) {
						weight[i][j] += PRIORITY_2;
					}
				}
				//2.纵向
				if (j - 4 >= 0 && j + 1 < COLS) {
					if (board[i][j - 4] == anotherColor && board[i][j - 3] == confirmColor && board[i][j - 2] == confirmColor
							&& board[i][j - 1] == confirmColor && board[i][j + 1] == null) {
						weight[i][j] += PRIORITY_2;
					}
				}
				//3.左上至右下
				if (((i - 4 >= 0) && i + 1 < COLS) && (j - 4 >= 0 && j + 1 < COLS)) {
					if (board[i - 4][j - 4] == anotherColor && board[i - 3][j - 3] == confirmColor && board[i - 2][j - 2] == confirmColor
							&& board[i - 1][j - 1] == confirmColor && board[i + 1][j + 1] == null) {
						weight[i][j] += PRIORITY_2;
					}
				}
				//4.右上至左下
				if ((i + 4 < COLS && i - 1 >= 0) && (j - 4 >= 0 && j + 1 < COLS)) {
					if (board[i - 1][j + 1] == null && board[i + 1][j - 1] == confirmColor && board[i + 2][j - 2] == confirmColor
							&& board[i + 3][j - 3] == confirmColor && board[i + 4][j - 4] == anotherColor) {
						weight[i][j] += PRIORITY_2;
					}
				}
				
				//眠三○●●●__ 右
				// 1.横向
				if (i - 5 >= 0) {
					if (board[i - 1][j] == null && board[i - 2][j] == confirmColor && board[i - 3][j] == confirmColor
							&& board[i - 4][j] == confirmColor && board[i - 5][j] == anotherColor) {
						weight[i][j] += PRIORITY_2;
					}
				}
				//2.纵向
				if (j - 5 >= 0) {
					if (board[i][j - 1] == null && board[i][j - 2] == confirmColor && board[i][j - 3] == confirmColor
							&& board[i][j - 4] == confirmColor && board[i][j - 5] == anotherColor) {
						weight[i][j] += PRIORITY_2;
					}
				}
				//3.右下至左上
				if (i - 5 >= 0 && j - 5 >= 0) {
					if (board[i - 1][j - 1] == null && board[i - 2][j - 2] == confirmColor && board[i - 3][j - 3] == confirmColor
							&& board[i - 4][j - 4] == confirmColor && board[i - 5][j - 5] == anotherColor) {
						weight[i][j] += PRIORITY_2;
					}
				}
				//4.左下至右上
				if (i + 5 < COLS && j - 5 >= 0) {
					if (board[i + 1][j - 1] == null && board[i + 2][j - 2] == confirmColor && board[i + 3][j - 3] == confirmColor
							&& board[i + 4][j - 4] == confirmColor && board[i + 5][j - 5] == anotherColor) {
						weight[i][j] += PRIORITY_2;
					}
				}
				
				//眠三_●_●●○左边那颗
				// 1.横向
				if (i + 5 < COLS) {
					if (board[i + 1][j] == confirmColor && board[i + 2][j] == null && board[i + 3][j] == confirmColor
							&& board[i + 4][j] == confirmColor && board[i + 5][j] == anotherColor) {
						weight[i][j] += PRIORITY_2;
					}
				}
				//2.纵向
				if (j + 5 < ROWS) {
					if (board[i][j + 1] == confirmColor && board[i][j + 2] == null && board[i][j + 3] == confirmColor
							&& board[i][j + 4] == confirmColor && board[i][j + 5] == anotherColor) {
						weight[i][j] += PRIORITY_2;
					}
				}
				//3.左上至右下
				if (i + 5 < COLS && j + 5 < ROWS) {
					if (board[i + 1][j + 1] == confirmColor && board[i + 2][j + 2] == null
							&& board[i + 3][j + 3] == confirmColor && board[i + 4][j + 4] == confirmColor && board[i + 5][j + 5] == anotherColor) {
						weight[i][j] += PRIORITY_2;
					}
				}
				//4.右上至左下
				if (i - 5 >= 0 && j + 5 < ROWS) {
					if (board[i - 1][j + 1] == confirmColor && board[i - 2][j + 2] == confirmColor
							&& board[i - 3][j + 3] == confirmColor && board[i - 4][j + 4] == confirmColor && board[i - 5][j + 5] == anotherColor) {
						weight[i][j] += PRIORITY_2;
					}
				}
				
				//眠三_●_●●○右边那颗
				// 1.横向
				if (i - 2 >= 0 && i + 3 < COLS) {
					if (board[i - 2][j] == null && board[i - 1][j] == confirmColor && board[i + 1][j] == confirmColor
							&& board[i + 2][j] == confirmColor && board[i + 3][j] == anotherColor) {
						weight[i][j] += PRIORITY_2;
					}
				}
				//2.纵向
				if (j - 2 >= 0 && j + 3 < ROWS) {
					if (board[i][j - 2] == null && board[i][j - 1] == confirmColor && board[i][j + 1] == confirmColor
							&& board[i][j + 2] == confirmColor && board[i][j + 3] == anotherColor) {
						weight[i][j] += PRIORITY_2;
					}
				}
				//3.左上至右下
				if ((i - 2 >= 0 && i + 3 < COLS) && (j - 2 >= 0 && j + 3 < ROWS)) {
					if (board[i - 2][j - 2] == null && board[i - 1][j - 1] == confirmColor && board[i + 1][j + 1] == confirmColor
							&& board[i + 2][j + 2] == confirmColor && board[i + 3][j + 3] == anotherColor) {
						weight[i][j] += PRIORITY_2;
					}
				}
				//4.右上至左下
				if ((i + 2 < COLS && i - 3 >= 0) && (j - 2 >= 0 && j + 3 < ROWS)) {
					if (board[i + 1][j - 1] == confirmColor && board[i + 2][j - 2] == null
							&& board[i - 1][j + 1] == confirmColor && board[i - 2][j + 2] == confirmColor && board[i - 3][j + 3] == anotherColor) {
						weight[i][j] += PRIORITY_2;
					}
				}
				
				//眠三○●●_●_左边
				// 1.横向
				if (i - 3 >= 0 && i + 2 < COLS) {
					if (board[i - 3][j] == anotherColor && board[i - 2][j] == confirmColor && board[i - 1][j] == confirmColor
							&& board[i + 1][j] == confirmColor && board[i + 2][j] == null) {
						weight[i][j] += PRIORITY_2;
					}
				}
				//2.纵向
				if (j - 3 >= 0 && j + 2 < ROWS) {
					if (board[i][j - 3] == anotherColor && board[i][j - 2] == confirmColor && board[i][j - 1] == confirmColor
							&& board[i][j + 1] == confirmColor && board[i][j + 2] == null) {
						weight[i][j] += PRIORITY_2;
					}
				}
				//3.左上至右下
				if ((i - 3 >= 0 && i + 2 < COLS) && (j - 3 >= 0 && j + 2 < ROWS)) {
					if (board[i - 3][j - 3] == anotherColor && board[i - 2][j - 2] == confirmColor
							&& board[i - 1][j - 1] == confirmColor && board[i + 1][j + 1] == confirmColor && board[i + 2][j + 2] == null) {
						weight[i][j] += PRIORITY_2;
					}
				}
				//4.右上至左下
				if ((i + 3 < COLS && i - 2 >= 0) && (j - 3 >= 0 && j + 2 < ROWS)) {
					if (board[i + 3][j - 3] == anotherColor && board[i + 2][j - 2] == confirmColor
							&& board[i + 1][j - 1] == confirmColor && board[i - 1][j + 1] == confirmColor && board[i - 2][j + 2] == null) {
						weight[i][j] += PRIORITY_2;
					}
				}
				
				//眠三○●●_●_右边
				// 1.横向
				if (i - 5 >= 0) {
					if (board[i - 5][j] == anotherColor && board[i - 4][j] == confirmColor && board[i - 3][j] == confirmColor
							&& board[i - 2][j] == null && board[i - 1][j] == confirmColor) {
						weight[i][j] += PRIORITY_2;
					}
				}
				//2.纵向
				if (j - 5 >= 0) {
					if (board[i][j - 5] == anotherColor && board[i][j - 4] == confirmColor && board[i][j - 3] == confirmColor
							&& board[i][j - 2] == null && board[i][j - 1] == confirmColor) {
						weight[i][j] += PRIORITY_2;
					}
				}
				//3.左上至右下
				if (i - 5 >= 0 && j - 5 >= 0) {
					if (board[i - 5][j - 5] == anotherColor && board[i - 4][j - 4] == confirmColor
							&& board[i - 3][j - 3] == confirmColor && board[i - 2][j - 2] == null && board[i - 1][j - 1] == confirmColor) {
						weight[i][j] += PRIORITY_2;
					}
				}
				//4.右上至左下
				if (i + 5 < COLS && j - 5 >= 0) {
					if (board[i + 5][j - 5] == anotherColor && board[i + 4][j - 4] == confirmColor
							&& board[i + 3][j - 3] == confirmColor && board[i + 2][j - 2] == null && board[i + 1][j - 1] == confirmColor) {
						weight[i][j] += PRIORITY_2;
					}
				}
				
				//眠三○●_●●_ 左边
				// 1.横向
				if (i - 2 >= 0 && i + 3 < COLS) {
					if (board[i - 2][j] == anotherColor && board[i - 1][j] == confirmColor && board[i + 1][j] == confirmColor
							&& board[i + 2][j] == confirmColor && board[i + 3][j] == null) {
						weight[i][j] += PRIORITY_2;
					}
				}
				//2.纵向
				if (j - 2 >= 0 && j + 3 < ROWS) {
					if (board[i][j - 2] == anotherColor && board[i][j - 1] == confirmColor && board[i][j + 1] == confirmColor
							&& board[i][j + 2] == confirmColor && board[i][j + 3] == null) {
						weight[i][j] += PRIORITY_2;
					}
				}
				//3.左上至右下
				if ((i - 2 >= 0 && i + 3 < COLS) && (j - 2 >= 0 && j + 3 < ROWS)) {
					if (board[i - 2][j - 2] == anotherColor && board[i - 1][j - 1] == confirmColor
							&& board[i + 1][j + 1] == confirmColor && board[i + 2][j + 2] == confirmColor && board[i + 3][j + 3] == null) {
						weight[i][j] += PRIORITY_2;
					}
				}
				//4.右上至左下
				if ((i + 2 < COLS && i - 3 >= 0) && (j - 2 >= 0 && j + 3 < ROWS)) {
					if (board[i + 2][j - 2] == anotherColor && board[i + 1][j - 1] == confirmColor
							&& board[i - 1][j + 1] == confirmColor && board[i - 2][j + 2] == confirmColor && board[i - 3][j + 3] == null) {
						weight[i][j] += PRIORITY_2;
					}
				}
				
				//眠三○●_●●_ 右边
				// 1.横向
				if (i - 5 >= 0) {
					if (board[i - 5][j] == anotherColor && board[i - 4][j] == confirmColor && board[i - 3][j] == null
							&& board[i - 2][j] == confirmColor && board[i - 1][j] == confirmColor) {
						weight[i][j] += PRIORITY_2;
					}
				}
				//2.纵向
				if (j - 5 >= 0) {
					if (board[i][j - 5] == anotherColor && board[i][j - 4] == confirmColor && board[i][j - 3] == null
							&& board[i][j - 2] == confirmColor && board[i][j - 1] == confirmColor) {
						weight[i][j] += PRIORITY_2;
					}
				}
				//3.左上至右下
				if (i - 5 >= 0 && j - 5 >= 0) {
					if (board[i - 5][j - 5] == anotherColor && board[i - 4][j - 4] == confirmColor
							&& board[i - 3][j - 3] == null && board[i - 2][j - 2] == confirmColor && board[i - 1][j - 1] == confirmColor) {
						weight[i][j] += PRIORITY_2;
					}
				}
				//4.右上至左下
				if (i + 5 < COLS && j - 5 >= 0) {
					if (board[i + 5][j - 5] == anotherColor && board[i + 4][j - 4] == confirmColor
							&& board[i + 3][j - 3] == null && board[i + 2][j - 2] == confirmColor && board[i + 1][j - 1] == confirmColor) {
						weight[i][j] += PRIORITY_2;
					}
				}
				
				//眠三_●●_●○ 左
				// 1.横向
				if (i + 5 < COLS) {
					if (board[i + 1][j] == confirmColor && board[i + 2][j] == confirmColor && board[i + 3][j] == null
							&& board[i + 4][j] == confirmColor && board[i + 5][j] == anotherColor) {
						weight[i][j] += PRIORITY_2;
					}
				}
				//2.纵向
				if (j + 5 < ROWS) {
					if (board[i][j + 1] == confirmColor && board[i][j + 2] == confirmColor && board[i][j + 3] == null
							&& board[i][j + 4] == confirmColor && board[i][j + 5] == anotherColor) {
						weight[i][j] += PRIORITY_2;
					}
				}
				//3.左上至右下
				if (i + 5 < COLS && j + 5 < ROWS) {
					if (board[i + 1][j + 1] == confirmColor && board[i + 2][j + 2] == confirmColor
							&& board[i + 3][j + 3] == null && board[i + 4][j + 4] == confirmColor && board[i + 5][j + 5] == anotherColor) {
						weight[i][j] += PRIORITY_2;
					}
				}
				//4.右上至左下
				if (i - 5 >= 0 && j + 5 < ROWS) {
					if (board[i - 1][j + 1] == confirmColor && board[i - 2][j + 2] == confirmColor
							&& board[i - 3][j + 3] == null && board[i - 4][j + 4] == confirmColor && board[i - 5][j + 5] == anotherColor) {
						weight[i][j] += PRIORITY_2;
					}
				}
				
				//眠三_●●_●○ 右
				// 1.横向
				if (i - 3 >= 0 && i + 2 < COLS) {
					if (board[i - 3][j] == null && board[i - 2][j] == confirmColor && board[i - 1][j] == confirmColor
							&& board[i + 1][j] == confirmColor && board[i + 2][j] == anotherColor) {
						weight[i][j] += PRIORITY_2;
					}
				}
				//2.纵向
				if (j - 3 >= 0 && j + 2 < ROWS) {
					if (board[i][j - 3] == null && board[i][j - 2] == confirmColor && board[i][j - 1] == confirmColor
							&& board[i][j + 1] == confirmColor && board[i][j + 2] == anotherColor) {
						weight[i][j] += PRIORITY_2;
					}
				}
				//3.左上至右下
				if ((i - 3 >= 0 && i + 2 < COLS) && (j - 3 >= 0 && j + 2 < ROWS)) {
					if (board[i - 3][j - 3] == null && board[i - 2][j - 2] == confirmColor && board[i - 1][j - 1] == confirmColor
							&& board[i + 1][j + 1] == confirmColor && board[i + 2][j + 2] == anotherColor) {
						weight[i][j] += PRIORITY_2;
					}
				}
				//4.右上至左下
				if ((i + 3 < COLS && i - 2 >= 0) && ((j - 3 >= 0) && j + 2 < ROWS)) {
					if (board[i + 3][j - 3] == null && board[i + 2][j - 2] == confirmColor && board[i + 1][j - 1] == confirmColor
							&& board[i - 1][j + 1] == confirmColor && board[i - 2][j + 2] == anotherColor) {
						weight[i][j] += PRIORITY_2;
					}
				}
				
				//眠三●_ _●● 左
				// 1.横向
				if (i - 1 >= 0 && i + 3 < COLS) {
					if (board[i - 1][j] == confirmColor && board[i + 1][j] == null && board[i + 2][j] == confirmColor && board[i + 3][j] == confirmColor) {
						weight[i][j] += PRIORITY_2;
					}
				}
				//2.纵向
				if (j - 1 >= 0 && j + 3 < ROWS) {
					if (board[i][j - 1] == confirmColor && board[i][j + 1] == null && board[i][j + 2] == confirmColor && board[i][j + 3] == confirmColor) {
						weight[i][j] += PRIORITY_2;
					}
				}
				//3.左上至右下
				if ((i - 1 >= 0 && i + 3 < COLS) && (j - 1 >= 0 && j + 3 < ROWS)) {
					if (board[i - 1][j - 1] == confirmColor && board[i + 1][j + 1] == null
							&& board[i + 2][j + 2] == confirmColor && board[i + 3][j + 3] == confirmColor) {
						weight[i][j] += PRIORITY_2;
					}
				}
				//4.右上至左下
				if ((i + 1 < COLS && i - 3 >= 0) && (j - 1 >= 0 && j + 3 < ROWS)) {
					if (board[i + 1][j - 1] == confirmColor && board[i - 1][j + 1] == null
							&& board[i - 2][j + 2] == confirmColor && board[i - 3][j + 3] == confirmColor) {
						weight[i][j] += PRIORITY_2;
					}
				}
				
				//眠三●_ _●● 右
				// 1.横向
				if (i - 2 >= 0 && i + 2 < COLS) {
					if (board[i - 2][j] == confirmColor && board[i - 1][j] == null && board[i + 1][j] == confirmColor && board[i + 2][j] == confirmColor) {
						weight[i][j] += PRIORITY_2;
					}
				}
				//2.纵向
				if (j - 2 >= 0 && j + 2 < ROWS) {
					if (board[i][j - 2] == confirmColor && board[i][j - 1] == null && board[i][j + 1] == confirmColor && board[i][j + 2] == confirmColor) {
						weight[i][j] += PRIORITY_2;
					}
				}
				//3.左上至右下
				if ((i - 2 >= 0 && i + 2 < COLS) && (j - 2 >= 0 && j + 2 < ROWS)) {
					if (board[i - 2][j - 2] == confirmColor && board[i - 1][j - 1] == null
							&& board[i + 1][j + 1] == confirmColor && board[i + 2][j + 2] == confirmColor) {
						weight[i][j] += PRIORITY_2;
					}
				}
				//4.右上至左下
				if ((i + 2 < COLS && i - 2 >= 0) && (j - 2 >= 0 && j + 2 < ROWS)) {
					if (board[i + 2][j - 2] == confirmColor && board[i + 1][j - 1] == null
							&& board[i - 1][j + 1] == confirmColor && board[i - 2][j + 2] == confirmColor) {
						weight[i][j] += PRIORITY_2;
					}
				}
				
				//眠三●●_ _● 左
				// 1.横向
				if ((i - 2 >= 0) && i + 2 < COLS) {
					if (board[i - 2][j] == confirmColor && board[i - 1][j] == confirmColor && board[i + 1][j] == null && board[i + 2][j] == confirmColor) {
						weight[i][j] += PRIORITY_2;
					}
				}
				//2.纵向
				if ((j - 2 >= 0) && j + 2 < ROWS) {
					if (board[i][j - 2] == confirmColor && board[i][j - 1] == confirmColor && board[i][j + 1] == null && board[i][j + 2] == confirmColor) {
						weight[i][j] += PRIORITY_2;
					}
				}
				//3.左上至右下
				if ((i - 2 >= 0 && i + 2 < COLS) && (j - 2 >= 0 && j + 2 < ROWS)) {
					if (board[i - 2][j - 2] == confirmColor && board[i - 1][j - 1] == confirmColor
							&& board[i + 1][j + 1] == null && board[i + 2][j + 2] == confirmColor) {
						weight[i][j] += PRIORITY_2;
					}
				}
				//4.右上至左下
				if ((i + 2 < COLS && i - 2 >= 0) && (j - 2 >= 0 && j + 2 < ROWS)) {
					if (board[i + 2][j - 2] == confirmColor && board[i + 1][j - 1] == confirmColor
							&& board[i - 1][j + 1] == null && board[i - 2][j + 2] == confirmColor) {
						weight[i][j] += PRIORITY_2;
					}
				}
				
				//眠三●●_ _● 右
				// 1.横向
				if (i - 3 >= 0 && i + 1 < COLS) {
					if (board[i - 3][j] == confirmColor && board[i - 2][j] == confirmColor && board[i - 1][j] == null && board[i + 1][j] == confirmColor) {
						weight[i][j] += PRIORITY_2;
					}
				}
				//2.纵向
				if (j - 3 >= 0 && j + 1 < ROWS) {
					if (board[i][j - 3] == confirmColor && board[i][j - 2] == confirmColor && board[i][j - 1] == null && board[i][j + 1] == confirmColor) {
						weight[i][j] += PRIORITY_2;
					}
				}
				//3.左上至右下
				if ((i - 3 >= 0 && i + 1 < COLS) && (j - 3 >= 0 && j + 1 < ROWS)) {
					if (board[i - 3][j - 3] == confirmColor && board[i - 2][j - 2] == confirmColor
							&& board[i - 1][j - 1] == null && board[i + 1][j + 1] == confirmColor) {
						weight[i][j] += PRIORITY_2;
					}
				}
				//4.右上至左下
				if ((i + 3 < COLS && i - 1 >= 0) && (j - 3 >= 0 && j + 1 < ROWS)) {
					if (board[i + 3][j - 3] == confirmColor && board[i + 2][j - 2] == confirmColor
							&& board[i + 1][j - 1] == null && board[i - 1][j + 1] == confirmColor) {
						weight[i][j] += PRIORITY_2;
					}
				}
				
				//眠三●_●_● 左
				// 1.横向
				if (i - 1 >= 0 && i + 3 < COLS) {
					if (board[i - 1][j] == confirmColor && board[i + 1][j] == confirmColor && board[i + 2][j] == null && board[i + 3][j] == confirmColor) {
						weight[i][j] += PRIORITY_2;
					}
				}
				//2.纵向
				if (j - 1 >= 0 && j + 3 < ROWS) {
					if (board[i][j - 1] == confirmColor && board[i][j + 1] == confirmColor && board[i][j + 2] == null && board[i][j + 3] == confirmColor) {
						weight[i][j] += PRIORITY_2;
					}
				}
				//3.左上至右下
				if ((i - 1 >= 0 && i + 3 < COLS) && (j - 1 >= 0 && j + 3 < ROWS)) {
					if (board[i - 1][j - 1] == confirmColor && board[i + 1][j + 1] == confirmColor
							&& board[i + 2][j + 2] == null && board[i + 3][j + 3] == confirmColor) {
						weight[i][j] += PRIORITY_2;
					}
				}
				//4.右上至左下
				if ((i + 1 < COLS && i - 3 >= 0) && (j - 1 >= 0 && j + 3 < ROWS)) {
					if (board[i + 1][j - 1] == confirmColor && board[i - 1][j + 1] == confirmColor
							&& board[i - 2][j + 2] == null && board[i - 3][j + 3] == confirmColor) {
						weight[i][j] += PRIORITY_2;
					}
				}
				
				//眠三●_●_● 右
				// 1.横向
				if (i - 3 >= 0 && i + 1 < COLS) {
					if (board[i - 3][j] == confirmColor && board[i - 2][j] == null && board[i - 1][j] == confirmColor && board[i + 1][j] == confirmColor) {
						weight[i][j] += PRIORITY_2;
					}
				}
				//2.纵向
				if (j - 3 >= 0 && j + 1 < ROWS) {
					if (board[i][j - 3] == confirmColor && board[i][j - 2] == null && board[i][j - 1] == confirmColor && board[i][j + 1] == confirmColor) {
						weight[i][j] += PRIORITY_2;
					}
				}
				//3.左上至右下
				if ((i - 3 >= 0 && i + 1 < COLS) && (j - 3 >= 0 && j + 1 < ROWS)) {
					if (board[i - 3][j - 3] == confirmColor && board[i - 2][j - 2] == null
							&& board[i - 1][j - 1] == confirmColor && board[i + 1][j + 1] == confirmColor) {
						weight[i][j] += PRIORITY_2;
					}
				}
				//4.右上至左下
				if ((i + 3 < COLS && i - 1 >= 0) && (j - 3 >= 0 && j + 1 < ROWS)) {
					if (board[i + 3][j - 3] == confirmColor && board[i + 2][j - 2] == null
							&& board[i + 1][j - 1] == confirmColor && board[i - 1][j + 1] == confirmColor) {
						weight[i][j] += PRIORITY_2;
					}
				}
				
				//眠三○_●●●_○ 左
				// 1.横向
				if (i - 1 >= 0 && i + 5 < COLS) {
					if (board[i - 1][j] == anotherColor && board[i + 1][j] == confirmColor && board[i + 2][j] == confirmColor
							&& board[i + 3][j] == confirmColor && board[i + 4][j] == null && board[i + 5][j] == anotherColor) {
						weight[i][j] += PRIORITY_2;
					}
				}
				//2.纵向
				if (j - 1 >= 0 && j + 5 < ROWS) {
					if (board[i][j - 1] == anotherColor && board[i][j + 1] == confirmColor && board[i][j + 2] == confirmColor
							&& board[i][j + 3] == confirmColor && board[i][j + 4] == null && board[i][j + 5] == anotherColor) {
						weight[i][j] += PRIORITY_2;
					}
				}
				//3.左上至右下
				if ((i - 1 >= 0 && i + 5 < COLS) && (j - 1 >= 0 && j + 5 < ROWS)) {
					if (board[i - 1][j - 1] == anotherColor && board[i + 1][j + 1] == confirmColor && board[i + 2][j + 2] == confirmColor
							&& board[i + 3][j + 3] == confirmColor && board[i + 4][j + 4] == null && board[i + 5][j + 5] == anotherColor) {
						weight[i][j] += PRIORITY_2;
					}
				}
				//4.右上至左下
				if ((i + 1 < COLS && i - 5 >= 0) && (j - 1 >= 0 && j + 5 < ROWS)) {
					if (board[i + 1][j - 1] == anotherColor && board[i - 1][j + 1] == confirmColor && board[i - 2][j + 2] == confirmColor
							&& board[i - 3][j + 3] == confirmColor && board[i - 4][j + 4] == null && board[i - 5][j + 5] == anotherColor) {
						weight[i][j] += PRIORITY_2;
					}
				}
				
				//眠三○_●●●_○ 右
				// 1.横向
				if ((i - 5 >= 0) && i + 1 < COLS) {
					if (board[i - 5][j] == anotherColor && board[i - 4][j] == null && board[i - 3][j] == confirmColor
							&& board[i - 2][j] == confirmColor && board[i - 1][j] == confirmColor && board[i + 1][j] == anotherColor) {
						weight[i][j] += PRIORITY_2;
					}
				}
				//2.纵向
				if (j - 5 >= 0 && j + 1 < ROWS) {
					if (board[i][j - 5] == anotherColor && board[i][j - 4] == null && board[i][j - 3] == confirmColor
							&& board[i][j - 2] == confirmColor && board[i][j - 1] == confirmColor && board[i][j + 1] == anotherColor) {
						weight[i][j] += PRIORITY_2;
					}
				}
				//3.左上至右下
				if ((i - 5 >= 0 && i + 1 < COLS) && (j - 5 >= 0 && j + 1 < ROWS)) {
					if (board[i - 5][j - 5] == anotherColor && board[i - 4][j - 4] == null && board[i - 3][j - 3] == confirmColor
							&& board[i - 2][j - 2] == confirmColor && board[i - 1][j - 1] == confirmColor && board[i + 1][j + 1] == anotherColor) {
						weight[i][j] += PRIORITY_2;
					}
				}
				//4.右上至左下
				if ((i + 5 < COLS && i - 1 >= 0) && (j - 5 >= 0 && j + 1 < ROWS)) {
					if (board[i + 5][j - 5] == anotherColor && board[i + 4][j - 4] == null && board[i + 3][j - 3] == confirmColor
							&& board[i + 2][j - 2] == confirmColor && board[i + 1][j - 1] == confirmColor && board[i - 1][j + 1] == anotherColor) {
						weight[i][j] += PRIORITY_2;
					}
				}
				
				//眠二○●●_ _ _ 左
				// 1.横向
				if (i - 3 >= 0 && i + 2 < COLS) {
					if (board[i - 3][j] == anotherColor && board[i - 2][j] == confirmColor && board[i - 1][j] == confirmColor
							&& board[i + 1][j] == null && board[i + 2][j] == null) {
						weight[i][j] += PRIORITY_1;
					}
				}
				//2.纵向
				if (j - 3 >= 0 && j + 2 < ROWS) {
					if (board[i][j - 3] == anotherColor && board[i][j - 2] == confirmColor && board[i][j - 1] == confirmColor
							&& board[i][j + 1] == null && board[i][j + 2] == null) {
						weight[i][j] += PRIORITY_1;
					}
				}
				//3.右下至左上
				if ((i - 3 >= 0 && i + 2 < COLS) && (j - 3 >= 0 && j + 2 < ROWS)) {
					if (board[i - 3][j - 3] == anotherColor && board[i - 2][j - 2] == confirmColor && board[i - 1][j - 1] == confirmColor
							&& board[i + 1][j + 1] == null && board[i + 2][j + 2] == null) {
						weight[i][j] += PRIORITY_1;
					}
				}
				//4.左下至右上
				if ((i + 3 < COLS && i - 2 >= 0) && (j - 3 >= 0 && j + 2 < ROWS)) {
					if (board[i + 3][j - 3] == anotherColor && board[i + 2][j - 2] == confirmColor && board[i + 1][j - 1] == confirmColor
							&& board[i - 1][j + 1] == null && board[i - 2][j + 2] == null) {
						weight[i][j] += PRIORITY_1;
					}
				}
				
				//眠二○●●_ _ _ 中
				// 1.横向
				if (i - 4 >= 0 && i + 1 < COLS) {
					if (board[i - 4][j] == anotherColor && board[i - 3][j] == confirmColor && board[i - 2][j] == confirmColor
							&& board[i - 1][j] == null && board[i + 1][j] == null) {
						weight[i][j] += PRIORITY_1;
					}
				}
				//2.纵向
				if (j - 4 >= 0 && j + 1 < ROWS) {
					if (board[i][j - 4] == anotherColor && board[i][j - 3] == confirmColor && board[i][j - 2] == confirmColor
							&& board[i][j - 1] == null && board[i][j + 1] == null) {
						weight[i][j] += PRIORITY_1;
					}
				}
				//3.右下至左上
				if ((i - 4 >= 0 && i + 1 < COLS) && (j - 4 >= 0 && j + 1 < ROWS)) {
					if (board[i - 4][j - 4] == anotherColor && board[i - 3][j - 3] == confirmColor && board[i - 2][j - 2] == confirmColor
							&& board[i - 1][j - 1] == null && board[i + 1][j + 1] == null) {
						weight[i][j] += PRIORITY_1;
					}
				}
				//4.左下至右上
				if ((i + 4 < COLS && i - 1 >= 0) && (j - 4 >= 0 && j + 1 < ROWS)) {
					if (board[i + 4][j - 4] == anotherColor && board[i + 3][j - 3] == confirmColor && board[i + 2][j - 2] == confirmColor
							&& board[i + 1][j - 1] == null && board[i - 1][j + 1] == null) {
						weight[i][j] += PRIORITY_1;
					}
				}
				
				//眠二○●●_ _ _ 右
				// 1.横向
				if (i - 5 >= 0) {
					if (board[i - 5][j] == anotherColor && board[i - 4][j] == confirmColor && board[i - 3][j] == confirmColor
							&& board[i - 2][j] == null && board[i - 1][j] == null) {
						weight[i][j] += PRIORITY_1;
					}
				}
				//2.纵向
				if (j - 5 >= 0) {
					if (board[i][j - 5] == anotherColor && board[i][j - 4] == confirmColor && board[i][j - 3] == confirmColor
							&& board[i][j - 2] == null && board[i][j - 1] == null) {
						weight[i][j] += PRIORITY_1;
					}
				}
				//3.右下至左上
				if (i - 5 >= 0 && j - 5 >= 0) {
					if (board[i - 5][j - 5] == anotherColor && board[i - 4][j - 4] == confirmColor && board[i - 3][j - 3] == confirmColor
							&& board[i - 2][j - 2] == null && board[i - 1][j - 1] == null) {
						weight[i][j] += PRIORITY_1;
					}
				}
				//4.左下至右上
				if (i + 5 < COLS && j - 5 >= 0) {
					if (board[i + 5][j - 5] == anotherColor && board[i + 4][j - 4] == confirmColor && board[i + 3][j - 3] == confirmColor
							&& board[i + 2][j - 2] == null && board[i + 1][j - 1] == null) {
						weight[i][j] += PRIORITY_1;
					}
				}
				
				//眠二_ _ _●●○ 左
				// 1.横向
				if (i + 5 < COLS) {
					if (board[i + 1][j] == null && board[i + 2][j] == null && board[i + 3][j] == confirmColor
							&& board[i + 4][j] == confirmColor && board[i + 5][j] == anotherColor) {
						weight[i][j] += PRIORITY_1;
					}
				}
				//2.纵向
				if (j + 5 < ROWS) {
					if (board[i][j + 1] == null && board[i][j + 2] == null && board[i][j + 3] == confirmColor
							&& board[i][j + 4] == confirmColor && board[i][j + 5] == anotherColor) {
						weight[i][j] += PRIORITY_1;
					}
				}
				//3.左上至右下
				if (i + 5 < COLS && j + 5 < ROWS) {
					if (board[i + 1][j + 1] == null && board[i + 2][j + 2] == null && board[i + 3][j + 3] == confirmColor
							&& board[i + 4][j + 4] == confirmColor && board[i + 5][j + 5] == anotherColor) {
						weight[i][j] += PRIORITY_1;
					}
				}
				//4.右上至左下
				if (i - 5 >= 0 && j + 5 < ROWS) {
					if (board[i - 1][j + 1] == null && board[i - 2][j + 2] == null && board[i - 3][j + 3] == confirmColor
							&& board[i - 4][j + 4] == confirmColor && board[i - 5][j + 5] == anotherColor) {
						weight[i][j] += PRIORITY_1;
					}
				}
				
				//眠二_ _ _●●○ 中
				// 1.横向
				if (i - 1 >= 0 && i + 4 < COLS) {
					if (board[i - 1][j] == null && board[i + 1][j] == null && board[i + 2][j] == confirmColor
							&& board[i + 3][j] == confirmColor && board[i + 4][j] == anotherColor) {
						weight[i][j] += PRIORITY_1;
					}
				}
				//2.纵向
				if (j - 1 >= 0 && j + 4 < ROWS) {
					if (board[i][j - 1] == null && board[i][j + 1] == null && board[i][j + 2] == confirmColor
							&& board[i][j + 3] == confirmColor && board[i][j + 4] == anotherColor) {
						weight[i][j] += PRIORITY_1;
					}
				}
				//3.左上至右下
				if ((i - 1 >= 0 && i + 4 < COLS) && (j - 1 >= 0 && j + 4 < ROWS)) {
					if (board[i - 1][j - 1] == null && board[i + 1][j + 1] == null && board[i + 2][j + 2] == confirmColor
							&& board[i + 3][j + 3] == confirmColor && board[i + 4][j + 4] == anotherColor) {
						weight[i][j] += PRIORITY_1;
					}
				}
				//4.右上至左下
				if ((i + 1 < COLS && i - 4 >= 0) && (j - 1 >= 0 && j + 4 < ROWS)) {
					if (board[i + 1][j - 1] == null && board[i - 1][j + 1] == null && board[i - 2][j + 2] == confirmColor
							&& board[i - 3][j + 3] == confirmColor && board[i - 4][j + 4] == anotherColor) {
						weight[i][j] += PRIORITY_1;
					}
				}
				
				//眠二_ _ _●●○ 右
				// 1.横向
				if ((i - 2 >= 0) && i + 3 < COLS) {
					if (board[i - 2][j] == null && board[i - 1][j] == null && board[i + 1][j] == confirmColor
							&& board[i + 2][j] == confirmColor && board[i + 3][j] == anotherColor) {
						weight[i][j] += PRIORITY_1;
					}
				}
				//2.纵向
				if ((j - 2 >= 0) && j + 3 < ROWS) {
					if (board[i][j - 2] == null && board[i][j - 1] == null && board[i][j + 1] == confirmColor
							&& board[i][j + 2] == confirmColor && board[i][j + 3] == anotherColor) {
						weight[i][j] += PRIORITY_1;
					}
				}
				//3.左上至右下
				if ((i - 2 >= 0 && i + 3 < COLS) && (j - 2 >= 0 && j + 3 < ROWS)) {
					if (board[i - 2][j - 2] == null && board[i - 1][j - 1] == null && board[i + 1][j + 1] == confirmColor
							&& board[i + 2][j + 2] == confirmColor && board[i + 3][j + 3] == anotherColor) {
						weight[i][j] += PRIORITY_1;
					}
				}
				//4.右上至左下
				if ((i + 2 < COLS && i - 3 >= 0) && (j - 2 >= 0 && j + 3 < ROWS)) {
					if (board[i + 2][j - 2] == null && board[i + 1][j - 1] == null && board[i - 1][j + 1] == confirmColor
							&& board[i - 2][j + 2] == confirmColor && board[i - 3][j + 3] == anotherColor) {
						weight[i][j] += PRIORITY_1;
					}
				}
				
				//眠二○●_ _●_ 左
				// 1.横向
				if ((i - 2 >= 0) && i + 3 < COLS) {
					if (board[i - 2][j] == anotherColor && board[i - 1][j] == confirmColor && board[i + 1][j] == null
							&& board[i + 2][j] == confirmColor && board[i + 3][j] == null) {
						weight[i][j] += PRIORITY_1;
					}
				}
				//2.纵向
				if ((j - 2 >= 0) && j + 3 < ROWS) {
					if (board[i][j - 2] == anotherColor && board[i][j - 1] == confirmColor && board[i][j + 1] == null
							&& board[i][j + 2] == confirmColor && board[i][j + 3] == null) {
						weight[i][j] += PRIORITY_1;
					}
				}
				//3.左上至右下
				if ((i - 2 >= 0 && i + 3 < COLS) && (j - 2 >= 0 && j + 3 < ROWS)) {
					if (board[i - 2][j - 2] == anotherColor && board[i - 1][j - 1] == confirmColor && board[i + 1][j + 1] == null
							&& board[i + 2][j + 2] == confirmColor && board[i + 3][j + 3] == null) {
						weight[i][j] += PRIORITY_1;
					}
				}
				//4.右上至左下
				if ((i + 2 < COLS && i - 3 >= 0) && (j - 2 >= 0 && j + 3 < ROWS)) {
					if (board[i + 2][j - 2] == anotherColor && board[i + 1][j - 1] == confirmColor && board[i - 1][j + 1] == null
							&& board[i - 2][j + 2] == confirmColor && board[i - 3][j + 3] == null) {
						weight[i][j] += PRIORITY_1;
					}
				}
				
				//眠二○●_ _●_ 中
				// 1.横向
				if ((i - 3 >= 0) && i + 2 < COLS) {
					if (board[i - 3][j] == anotherColor && board[i - 2][j] == confirmColor && board[i - 1][j] == null
							&& board[i + 1][j] == confirmColor && board[i + 2][j] == null) {
						weight[i][j] += PRIORITY_1;
					}
				}
				//2.纵向
				if (j - 3 >= 0 && j + 2 < ROWS) {
					if (board[i][j - 3] == anotherColor && board[i][j - 2] == confirmColor && board[i][j - 1] == null
							&& board[i][j + 1] == confirmColor && board[i][j + 2] == null) {
						weight[i][j] += PRIORITY_1;
					}
				}
				//3.左上至右下
				if ((i - 3 >= 0 && i + 2 < COLS) && (j - 3 >= 0 && j + 2 < ROWS)) {
					if (board[i - 3][j - 3] == anotherColor && board[i - 2][j - 2] == confirmColor && board[i - 1][j - 1] == null
							&& board[i + 1][j + 1] == confirmColor && board[i + 2][j + 2] == null) {
						weight[i][j] += PRIORITY_1;
					}
				}
				//4.右上至左下
				if ((i + 3 < COLS && i - 2 >= 0) && (j - 3 >= 0 && j + 2 < ROWS)) {
					if (board[i + 3][j - 3] == anotherColor && board[i + 2][j - 2] == confirmColor && board[i + 1][j - 1] == null
							&& board[i - 1][j + 1] == confirmColor && board[i - 2][j + 2] == null) {
						weight[i][j] += PRIORITY_1;
					}
				}
				
				//眠二○●_ _●_ 右
				// 1.横向
				if (i - 5 >= 0) {
					if (board[i - 5][j] == anotherColor && board[i - 4][j] == confirmColor && board[i - 3][j] == null
							&& board[i - 2][j] == null && board[i - 1][j] == confirmColor) {
						weight[i][j] += PRIORITY_1;
					}
				}
				//2.纵向
				if (j - 5 >= 0) {
					if (board[i][j - 5] == anotherColor && board[i][j - 4] == confirmColor && board[i][j - 3] == null
							&& board[i][j - 2] == null && board[i][j - 1] == confirmColor) {
						weight[i][j] += PRIORITY_1;
					}
				}
				//3.左上至右下
				if ((i - 5 >= 0) && j - 5 >= 0) {
					if (board[i - 5][j - 5] == anotherColor && board[i - 4][j - 4] == confirmColor && board[i - 3][j - 3] == null
							&& board[i - 2][j - 2] == null && board[i - 1][j - 1] == confirmColor) {
						weight[i][j] += PRIORITY_1;
					}
				}
				//4.右上至左下
				if (i + 5 < COLS && j - 5 >= 0) {
					if (board[i + 5][j - 5] == anotherColor && board[i + 4][j - 4] == confirmColor && board[i + 3][j - 3] == null
							&& board[i + 2][j - 2] == null && board[i + 1][j - 1] == confirmColor) {
						weight[i][j] += PRIORITY_1;
					}
				}
				
				//眠二_●_ _●○ 左
				// 1.横向
				if (i + 5 < COLS) {
					if (board[i + 1][j] == confirmColor && board[i + 2][j] == null && board[i + 3][j] == null
							&& board[i + 4][j] == confirmColor && board[i + 5][j] == anotherColor) {
						weight[i][j] += PRIORITY_1;
					}
				}
				//2.纵向
				if (j + 5 < ROWS) {
					if (board[i][j + 1] == confirmColor && board[i][j + 2] == null && board[i][j + 3] == null
							&& board[i][j + 4] == confirmColor && board[i][j + 5] == anotherColor) {
						weight[i][j] += PRIORITY_1;
					}
				}
				//3.左上至右下
				if (i + 5 < COLS && j + 5 < ROWS) {
					if (board[i + 1][j + 1] == confirmColor && board[i + 2][j + 2] == null && board[i + 3][j + 3] == null
							&& board[i + 4][j + 4] == confirmColor && board[i + 5][j + 5] == anotherColor) {
						weight[i][j] += PRIORITY_1;
					}
				}
				//4.右上至左下
				if (i - 5 >= 0 && j + 5 < ROWS) {
					if (board[i - 1][j + 1] == confirmColor && board[i - 2][j + 2] == null && board[i - 3][j + 3] == null
							&& board[i - 4][j + 4] == confirmColor && board[i - 5][j + 5] == anotherColor) {
						weight[i][j] += PRIORITY_1;
					}
				}
				
				//眠二_●_ _●○ 中
				// 1.横向
				if (i - 2 >= 0 && i + 3 < COLS) {
					if (board[i - 2][j] == null && board[i - 1][j] == confirmColor && board[i + 1][j] == null
							&& board[i + 2][j] == confirmColor && board[i + 3][j] == anotherColor) {
						weight[i][j] += PRIORITY_1;
					}
				}
				//2.纵向
				if ((j - 2 >= 0) && j + 3 < ROWS) {
					if (board[i][j - 2] == null && board[i][j - 1] == confirmColor && board[i][j + 1] == null
							&& board[i][j + 2] == confirmColor && board[i][j + 3] == anotherColor) {
						weight[i][j] += PRIORITY_1;
					}
				}
				//3.左上至右下
				if ((i - 2 >= 0 && i + 3 < COLS) && (j - 2 >= 0 && j + 3 < ROWS)) {
					if (board[i - 2][j - 2] == null && board[i - 1][j - 1] == confirmColor && board[i + 1][j + 1] == null
							&& board[i + 2][j + 2] == confirmColor && board[i + 3][j + 3] == anotherColor) {
						weight[i][j] += PRIORITY_1;
					}
				}
				//4.右上至左下
				if ((i + 2 < COLS && i - 3 >= 0) && (j - 2 >= 0 && j + 3 < ROWS)) {
					if (board[i + 2][j - 2] == null && board[i + 1][j - 1] == confirmColor && board[i - 1][j + 1] == null
							&& board[i - 2][j + 2] == confirmColor && board[i - 3][j + 3] == anotherColor) {
						weight[i][j] += PRIORITY_1;
					}
				}
				
				//眠二_●_ _●○ 右
				// 1.横向
				if (i - 3 >= 0 && i + 2 < COLS) {
					if (board[i - 3][j] == null && board[i - 2][j] == confirmColor && board[i - 1][j] == null
							&& board[i + 1][j] == confirmColor && board[i + 2][j] == anotherColor) {
						weight[i][j] += PRIORITY_1;
					}
				}
				//2.纵向
				if (j - 3 >= 0 && j + 2 < ROWS) {
					if (board[i][j - 3] == null && board[i][j - 2] == confirmColor && board[i][j - 1] == null
							&& board[i][j + 1] == confirmColor && board[i][j + 2] == anotherColor) {
						weight[i][j] += PRIORITY_1;
					}
				}
				//3.左上至右下
				if ((i - 3 >= 0 && i + 2 < COLS) && (j - 3 >= 0 && j + 2 < ROWS)) {
					if (board[i - 3][j - 3] == null && board[i - 2][j - 2] == confirmColor && board[i - 1][j - 1] == null
							&& board[i + 1][j + 1] == confirmColor && board[i + 2][j + 2] == anotherColor) {
						weight[i][j] += PRIORITY_1;
					}
				}
				//4.右上至左下
				if ((i + 3 < COLS && i - 2 >= 0) && (j - 3 >= 0 && j + 2 < ROWS)) {
					if (board[i + 3][j - 3] == null && board[i + 2][j - 2] == confirmColor && board[i + 1][j - 1] == null
							&& board[i - 1][j + 1] == confirmColor && board[i - 2][j + 2] == anotherColor) {
						weight[i][j] += PRIORITY_1;
					}
				}
				
				//眠二●_ _ _● 左
				// 1.横向
				if (i - 1 >= 0 && i + 3 < COLS) {
					if (board[i - 1][j] == confirmColor && board[i + 1][j] == null && board[i + 2][j] == null && board[i + 3][j] == confirmColor) {
						weight[i][j] += PRIORITY_2;
					}
				}
				//2.纵向
				if (j - 1 >= 0 && j + 3 < ROWS) {
					if (board[i][j - 1] == confirmColor && board[i][j + 1] == null && board[i][j + 2] == null && board[i][j + 3] == confirmColor) {
						weight[i][j] += PRIORITY_2;
					}
				}
				//3.左上至右下
				if ((i - 1 >= 0 && i + 3 < COLS) & (j - 1 >= 0 && j + 3 < ROWS)) {
					if (board[i - 1][j - 1] == confirmColor && board[i + 1][j + 1] == null
							&& board[i + 2][j + 2] == null && board[i + 3][j + 3] == confirmColor) {
						weight[i][j] += PRIORITY_2;
					}
				}
				//4.右上至左下
				if ((i + 1 < COLS && i - 3 >= 0) && (j - 1 >= 0 && j + 3 < ROWS)) {
					if (board[i + 1][j - 1] == confirmColor && board[i - 1][j + 1] == null
							&& board[i - 2][j + 2] == null && board[i - 3][j + 3] == confirmColor) {
						weight[i][j] += PRIORITY_2;
					}
				}
				
				//眠二●_ _ _● 中
				// 1.横向
				if (i - 2 >= 0 && i + 2 < COLS) {
					if (board[i - 2][j] == confirmColor && board[i - 1][j] == null && board[i + 1][j] == null && board[i + 2][j] == confirmColor) {
						weight[i][j] += PRIORITY_1;
					}
				}
				//2.纵向
				if (j - 2 >= 0 && j + 2 < ROWS) {
					if (board[i][j - 2] == confirmColor && board[i][j - 1] == null && board[i][j + 1] == null && board[i][j + 2] == confirmColor) {
						weight[i][j] += PRIORITY_1;
					}
				}
				//3.左上至右下
				if ((i - 2 >= 0 && i + 2 < COLS) && (j - 2 >= 0 && j + 2 < ROWS)) {
					if (board[i - 2][j - 2] == confirmColor && board[i - 1][j - 1] == null
							&& board[i + 1][j + 1] == null && board[i + 2][j + 2] == confirmColor) {
						weight[i][j] += PRIORITY_1;
					}
				}
				//4.右上至左下
				if ((i + 2 < COLS && i - 2 >= 0) && (j - 2 >= 0 && j + 2 < ROWS)) {
					if (board[i + 2][j - 2] == confirmColor && board[i + 1][j - 1] == null
							&& board[i - 1][j + 1] == null && board[i - 2][j + 2] == confirmColor) {
						weight[i][j] += PRIORITY_1;
					}
				}
				
				//眠二●_ _ _● 右
				// 1.横向
				if (i - 3 >= 0 && i + 1 < COLS) {
					if (board[i - 3][j] == confirmColor && board[i - 2][j] == null && board[i - 1][j] == null && board[i + 1][j] == confirmColor) {
						weight[i][j] += PRIORITY_1;
					}
				}
				//2.纵向
				if (j - 3 >= 0 && j + 1 < ROWS) {
					if (board[i][j - 3] == confirmColor && board[i][j - 2] == null && board[i][j - 1] == null && board[i][j + 1] == confirmColor) {
						weight[i][j] += PRIORITY_1;
					}
				}
				//3.左上至右下
				if ((i - 3 >= 0 && i + 1 < COLS) && (j - 3 >= 0 && j + 1 < ROWS)) {
					if (board[i - 3][j - 3] == confirmColor && board[i - 2][j - 2] == null
							&& board[i - 1][j - 1] == null && board[i + 1][j + 1] == confirmColor) {
						weight[i][j] += PRIORITY_1;
					}
				}
				//4.右上至左下
				if ((i + 3 < COLS && i - 1 >= 0) && (j - 3 >= 0 && j + 1 < ROWS)) {
					if (board[i + 3][j - 3] == confirmColor && board[i + 2][j - 2] == null
							&& board[i + 1][j - 1] == null && board[i - 1][j + 1] == confirmColor) {
						weight[i][j] += PRIORITY_1;
					}
				}
				
				if (firstDrop && confirmColor == Piece.B) {
					if (board[i][j] != null) {
						weight[i][j] = -1;
						if (i - 1 >= 0 && j - 1 >= 0) weight[i - 1][j - 1] = 1;
						if (j - 1 >= 0) weight[i][j - 1] = 1;
						if (i + 1 < COLS && j - 1 >= 0) weight[i + 1][j - 1] = 1;
						if (i - 1 >= 0) weight[i - 1][j] = 1;
						if (i + 1 < COLS) weight[i + 1][j] = 1;
						if (i - 1 >=0 && j + 1 < ROWS) weight[i - 1][j + 1] = 1;
						if (j + 1 < ROWS) weight[i][j + 1] = 1;
						if (i + 1 < COLS && j + 1 < ROWS) weight[i + 1][j + 1] = 1;
						firstDrop = false;
					}
				}
			}
		}
		
		return weight;
	}

}
