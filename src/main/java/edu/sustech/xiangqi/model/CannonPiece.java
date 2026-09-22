package edu.sustech.xiangqi.model;

public class CannonPiece extends AbstractPiece {

    public CannonPiece(String name, int row, int col, boolean isRed) {
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
            int gapCol=Math.abs(currentCol-targetCol);
            int a=0;
            for (int vCol = 1; vCol < gapCol ; vCol++) {
                a=a;
                int col=currentCol+(targetCol-currentCol)*vCol/gapCol;
                if (model.getPieceAt(currentRow, col) != null) {
                    a++;
                }
                if(a==1&&model.getPieceAt(targetRow, targetCol) == null){
                    return false;
                }
                if(a==2){
                    return false;
                }
            }
            if(a==0&&model.getPieceAt(targetRow, targetCol) != null){
                return false;
            }

        }
        else {
            // 情况1：竖走（列相同，遍历行方向的所有格子）
            int gapRow = Math.abs(currentRow - targetRow);
            int a = 0;
            for (int vRow = 1; vRow < gapRow; vRow++) {
                a = a;
                int row = currentRow + (targetRow - currentRow) * vRow / gapRow;
                if (model.getPieceAt(row, currentCol) != null) {
                    a++;
                }
                if (a == 1 && model.getPieceAt(targetRow, targetCol) == null) {
                    return false;
                }
                if (a == 2) {
                    return false;
                }
            }
            if (a == 0 && model.getPieceAt(targetRow, targetCol) != null) {
                return false;
            }
        }

        //目标位置不能有己方棋子
        AbstractPiece targetPiece = model.getPieceAt(targetRow, targetCol);
        return targetPiece == null || targetPiece.isRed() != this.isRed();
    }
}