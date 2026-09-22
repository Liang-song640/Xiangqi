package edu.sustech.xiangqi.model;
public class ElephantPiece extends AbstractPiece {

    public ElephantPiece(String name, int row, int col, boolean isRed) {
        super(name, row, col, isRed);
    }

    @Override
    public boolean canMoveTo(int targetRow, int targetCol, ChessBoardModel model) {
        //边界检查
        if (targetRow < 0 || targetRow > 9 || targetCol < 0 || targetCol > 8) {
            return false;
        }
        int currentRow = getRow();
        int currentCol = getCol();
        //排除原地不动
        if (currentRow == targetRow && currentCol == targetCol) {
            return false;
        }
        //象不能过河
        if (!isRed() && targetRow > 4) {
            return false;
        }
        if (isRed() && targetRow < 5) {
            return false;
        }
        //走"田"字判断
        int rowDiff = targetRow - currentRow;
        int colDiff = targetCol - currentCol;

        if (Math.abs(rowDiff) != 2 || Math.abs(colDiff) != 2) {
            return false;
        }

        //计算并检查绊象眼
        int blockRow = currentRow + rowDiff / 2;
        int blockCol = currentCol + colDiff / 2;

        if (model.getPieceAt(blockRow, blockCol) != null) {
            return false;
        }

        // TODO: 实现相/象的移动规则

        //检查目标位置
        AbstractPiece targetPiece = model.getPieceAt(targetRow, targetCol);
        return targetPiece == null || targetPiece.isRed() != isRed();
    }
}