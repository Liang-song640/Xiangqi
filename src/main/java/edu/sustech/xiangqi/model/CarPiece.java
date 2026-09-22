package edu.sustech.xiangqi.model;

public class CarPiece extends AbstractPiece {

    public CarPiece(String name, int row, int col, boolean isRed) {
        super(name, row, col, isRed);
    }

    @Override
    public boolean canMoveTo(int targetRow, int targetCol, ChessBoardModel model) {
        int currentRow = getRow();
        int currentCol = getCol();

        //边界检查
        if (targetRow < 0 || targetRow > 9 || targetCol < 0 || targetCol > 8) {
            return false;
        }

        //排除原地不动
        if (currentRow == targetRow && currentCol == targetCol) {
            return false;
        }

        //车必须走直线
        boolean isStraightMove = (currentRow == targetRow) || (currentCol == targetCol);
        if (!isStraightMove) {
            return false;
        }

        //不能越子
        if (currentRow == targetRow) {
            //情况1：横走（行相同，遍历列方向的所有格子）
            int startCol = Math.min(currentCol, targetCol) + 1;
            int endCol = Math.max(currentCol, targetCol);
            for (int col = startCol; col < endCol; col++) {
                if (model.getPieceAt(currentRow, col) != null) {
                    return false;
                }
            }
        } else {
            //情况2：竖走（列相同，遍历行方向的所有格子）
            int startRow = Math.min(currentRow, targetRow) + 1;
            int endRow = Math.max(currentRow, targetRow);
            for (int row = startRow; row < endRow; row++) {
                if (model.getPieceAt(row, currentCol) != null) {
                    return false;
                }
            }
        }

        //目标位置不能有己方棋子
        AbstractPiece targetPiece = model.getPieceAt(targetRow, targetCol);
        return targetPiece == null || targetPiece.isRed() != this.isRed();
    }
}