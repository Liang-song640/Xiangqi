package edu.sustech.xiangqi.model;

//帅&将
public class GeneralPiece extends AbstractPiece {

    public GeneralPiece(String name, int row, int col, boolean isRed) {
        super(name, row, col, isRed);
    }

    @Override
    public boolean canMoveTo(int targetRow, int targetCol, ChessBoardModel model) {
        //边界+九宫格
        if (targetRow < 0 || targetRow > 9 || targetCol < 0 || targetCol > 8) return false;
        boolean inPalace = isRed() ?
                (targetRow >=7 && targetRow <=9 && targetCol >=3 && targetCol <=5) :
                (targetRow >=0 && targetRow <=2 && targetCol >=3 && targetCol <=5);
        if (!inPalace) return false;

        //只能横竖走一步
        int rowDiff = Math.abs(targetRow - getRow());
        int colDiff = Math.abs(targetCol - getCol());
        if (!((rowDiff == 1 && colDiff == 0) || (rowDiff == 0 && colDiff == 1))) {
            return false;
        }

        //不能走到自己棋子上
        AbstractPiece target = model.getPieceAt(targetRow, targetCol);
        return target == null || target.isRed() != this.isRed();
    }
}