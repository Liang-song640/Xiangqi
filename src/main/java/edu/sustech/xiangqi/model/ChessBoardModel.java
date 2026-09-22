package edu.sustech.xiangqi.model;
import java.util.ArrayList;
import java.util.List;

public class ChessBoardModel {
    private final List<AbstractPiece> pieces;
    private static final int ROWS = 10;
    private static final int COLS = 9;
    private boolean gameOver = false;
    private String winner = null;
    private MusicPlayer musicPlayer;
    private boolean isRedTurn = true;

    public ChessBoardModel() {
        pieces = new ArrayList<>();
        initializePieces();
    }

    public void setMusicPlayer(MusicPlayer musicPlayer) {
        this.musicPlayer = musicPlayer;
    }

    private void initializePieces() {
        // 黑方
        pieces.add(new GeneralPiece("將", 0, 4, false));
        pieces.add(new SoldierPiece("卒", 3, 0, false));
        pieces.add(new SoldierPiece("卒", 3, 2, false));
        pieces.add(new SoldierPiece("卒", 3, 4, false));
        pieces.add(new SoldierPiece("卒", 3, 6, false));
        pieces.add(new SoldierPiece("卒", 3, 8, false));
        pieces.add(new HorsePiece("马", 0, 1, false));
        pieces.add(new HorsePiece("马", 0, 7, false));
        pieces.add(new CarPiece("车", 0, 0, false));
        pieces.add(new CarPiece("车", 0, 8, false));
        pieces.add(new ElephantPiece("象", 0, 2, false));
        pieces.add(new ElephantPiece("象", 0, 6, false));
        pieces.add(new AdvisorPiece("士", 0, 3, false));
        pieces.add(new AdvisorPiece("士", 0, 5, false));
        pieces.add(new CannonPiece("炮", 2, 1, false));
        pieces.add(new CannonPiece("炮", 2, 7, false));

        // 红方
        pieces.add(new GeneralPiece("帅", 9, 4, true));
        pieces.add(new SoldierPiece("兵", 6, 0, true));
        pieces.add(new SoldierPiece("兵", 6, 2, true));
        pieces.add(new SoldierPiece("兵", 6, 4, true));
        pieces.add(new SoldierPiece("兵", 6, 6, true));
        pieces.add(new SoldierPiece("兵", 6, 8, true));
        pieces.add(new HorsePiece("马", 9, 1, true));
        pieces.add(new HorsePiece("马", 9, 7, true));
        pieces.add(new CarPiece("车", 9, 0, true));
        pieces.add(new CarPiece("车", 9, 8, true));
        pieces.add(new ElephantPiece("相", 9, 2, true));
        pieces.add(new ElephantPiece("相", 9, 6, true));
        pieces.add(new AdvisorPiece("仕", 9, 3, true));
        pieces.add(new AdvisorPiece("仕", 9, 5, true));
        pieces.add(new CannonPiece("炮", 7, 1, true));
        pieces.add(new CannonPiece("炮", 7, 7, true));
    }

    public boolean movePiece(AbstractPiece piece, int newRow, int newCol) {
        if (gameOver || !isValidPosition(newRow, newCol)) return false;

        AbstractPiece target = getPieceAt(newRow, newCol);
        //不能吃自己的棋子
        if (target != null && target.isRed() == piece.isRed()) return false;
        //检查移动规则
        if (!piece.canMoveTo(newRow, newCol, this)) return false;

        //处理吃子
        if (target != null) {
            pieces.remove(target);
            //播放吃子音效（Key对应BGM4）
            if (musicPlayer != null) {
                musicPlayer.playSoundEffect("bgm4");
            }
        }

        piece.moveTo(newRow, newCol);
        GeneralPiece redGen = null;
        GeneralPiece blackGen = null;
        for (AbstractPiece p : pieces) {
            if (p instanceof GeneralPiece) {
                if (p.isRed()) redGen = (GeneralPiece) p;
                else blackGen = (GeneralPiece) p;
            }
        }
        if (redGen != null && blackGen != null && redGen.getCol() == blackGen.getCol()) {
            boolean noBlock = true;
            int start = Math.min(redGen.getRow(), blackGen.getRow()) + 1;
            int end = Math.max(redGen.getRow(), blackGen.getRow());
            for (int r = start; r < end; r++) {
                if (getPieceAt(r, redGen.getCol()) != null) {
                    noBlock = false;
                    break;
                }
            }
            if (noBlock) {
                gameOver = true;
                winner = isRedTurn ? "黑方" : "红方";
            }
        }
        if (target instanceof GeneralPiece) {
            gameOver = true;
            winner = piece.isRed() ? "红方" : "黑方";
        }
        return true;
    }

    //重置棋盘时清空胜负状态
    public void resetBoard() {
        pieces.clear();
        initializePieces();
        isRedTurn = true;
        gameOver = false;
        winner = null;
    }

    //Getter/Setter
    public List<AbstractPiece> getPieces() { return pieces; }
    public AbstractPiece getPieceAt(int row, int col) {
        for (AbstractPiece p : pieces) {
            if (p.getRow() == row && p.getCol() == col) return p;
        }
        return null;
    }
    public boolean isValidPosition(int row, int col) {
        return row >= 0 && row < ROWS && col >= 0 && col < COLS;
    }
    public boolean isGameOver() { return gameOver; }
    public String getWinner() { return winner; }
    public boolean isRedTurn() { return isRedTurn; }
    public void setRedTurn(boolean redTurn) { isRedTurn = redTurn; }
    public void switchTurn() { isRedTurn = !isRedTurn; }
    public static int getRows() { return ROWS; }
    public static int getCols() { return COLS; }
    //添加setter方法，用于设置游戏状态
    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }

    public void setWinner(String winner) {
        this.winner = winner;
    }
}