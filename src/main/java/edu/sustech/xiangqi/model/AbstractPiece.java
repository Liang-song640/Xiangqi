package edu.sustech.xiangqi.model;

public abstract class AbstractPiece {
    private final String name;
    private int row;
    private int col;
    private final boolean isRed;

    public AbstractPiece(String name, int row, int col, boolean isRed) {
        this.name = name;
        this.row = row;
        this.col = col;
        this.isRed = isRed;
    }

    // 移动棋子到新位置
    public void moveTo(int newRow, int newCol) {
        this.row = newRow;
        this.col = newCol;
    }

    // 抽象方法：判断是否可移动到目标位置
    public abstract boolean canMoveTo(int targetRow, int targetCol, ChessBoardModel model);

    // Getter方法
    public String getName() { return name; }
    public int getRow() { return row; }
    public int getCol() { return col; }
    public boolean isRed() { return isRed; }


}