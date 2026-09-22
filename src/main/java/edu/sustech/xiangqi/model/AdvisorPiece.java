package edu.sustech.xiangqi.model;

//士/仕
public class AdvisorPiece extends AbstractPiece {

    public AdvisorPiece(String name, int row, int col, boolean isRed) {
        super(name, row, col, isRed);
    }

    @Override
    public boolean canMoveTo(int targetRow, int targetCol, ChessBoardModel model) {
        //边界检查
        if (targetRow < 0 || targetRow > 9 || targetCol < 0 || targetCol > 8) {
            return false;
        }
        int currentRow=getRow();
        int currentCol=getCol();
        //排除原地不动
        if (currentRow == targetRow && currentCol == targetCol) {
            return false;
        }
        //士只能在田字格里面移动
        if(targetRow>2 && targetRow<7){
            return false;
        }
        if(targetCol >5 || targetCol <3){
            return false;
        }
        if (!isRed() && Math.abs(targetRow-1)*Math.abs(targetRow-1)+Math.abs(targetCol-4)*Math.abs(targetCol-4)>2) {
            return false;
        }
        if (isRed() && Math.abs(targetRow-8)*Math.abs(targetRow-8)+Math.abs(targetCol-4)*Math.abs(targetCol-4)>2) {
            return false;
        }
        //士的移动
        int rowDiff = targetRow - currentRow;
        int colDiff = targetCol - currentCol;

        if (Math.abs(rowDiff) != 1 || Math.abs(colDiff) != 1) {
            return false;
        }
        //检查目标位置
        AbstractPiece targetPiece = model.getPieceAt(targetRow, targetCol);
        return targetPiece == null || targetPiece.isRed() != isRed();
    }
}