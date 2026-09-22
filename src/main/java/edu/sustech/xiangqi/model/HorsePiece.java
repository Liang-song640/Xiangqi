package edu.sustech.xiangqi.model;

public class HorsePiece extends AbstractPiece {

    public HorsePiece(String name, int row, int col, boolean isRed) {
        super(name, row, col, isRed);
    }

    @Override
    public boolean canMoveTo(int targetRow, int targetCol, ChessBoardModel model) {
        int currentRow = getRow();
        int currentCol = getCol();
        int rowDiff = targetRow - currentRow;
        int colDiffRaw = targetCol - currentCol;
        int colDiff = Math.abs(colDiffRaw);
        boolean hasBlock = false;
        //排除原地不动
        if (currentRow == targetRow && currentCol == targetCol) {
            return false;
        }

        //计算绊马腿
        if (Math.abs(rowDiff) == 2) {
            int blockRow = currentRow + (rowDiff > 0 ? 1 : -1);
            hasBlock = model.getPieceAt(blockRow, currentCol) != null;
        } else if (colDiff == 2) {
            int blockCol = currentCol + (colDiffRaw > 0 ? 1 : -1);
            hasBlock = model.getPieceAt(currentRow, blockCol) != null;
        }

        //日字法判断
        boolean isLegalMove = (rowDiff == 2 && Math.abs(colDiffRaw) == 1)
                || (rowDiff == -2 && Math.abs(colDiffRaw) == 1)
                || (Math.abs(rowDiff) == 1 && colDiff == 2);

        //不能有己方棋子
        AbstractPiece targetPiece = model.getPieceAt(targetRow, targetCol);
        boolean isTargetLegal = false;
        if (targetPiece == null) {
            //目标是空位：合法
            isTargetLegal = true;
        } else {
            //目标有棋子：判断颜色是否不同（不同则合法，相同则非法）
            isTargetLegal = targetPiece.isRed() != this.isRed();
        }

        // TODO: 实现马的移动规则
        return isLegalMove && !hasBlock && isTargetLegal;
    }
}