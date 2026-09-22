package edu.sustech.xiangqi.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 游戏状态类，修复棋子创建逻辑
 */
public class GameState implements Serializable {
    private static final long serialVersionUID = 1L;

    private String username;
    private String saveName;
    private long timestamp;
    private Map<String, String> piecePositions;
    private boolean redTurn;

    public GameState() {
        this.piecePositions = new HashMap<>();
        this.timestamp = System.currentTimeMillis();
    }

    public GameState(ChessBoardModel model, String username, String saveName) {
        this.username = username;
        this.saveName = saveName;
        this.timestamp = System.currentTimeMillis();
        this.redTurn = model.isRedTurn();

        this.piecePositions = new HashMap<>();
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 9; j++) {
                AbstractPiece piece = model.getPieceAt(i, j);
                if (piece != null) {
                    String key = i + "," + j;
                    //存储棋子名称+类型+颜色（原逻辑缺失名称）
                    String value = piece.getName() + "," + piece.getClass().getSimpleName() + "," + piece.isRed();
                    piecePositions.put(key, value);
                }
            }
        }
    }

    public void applyToModel(ChessBoardModel model) {
        model.getPieces().clear();
        model.setRedTurn(redTurn);
        model.setGameOver(false);
        model.setWinner(null);
        for (Map.Entry<String, String> entry : piecePositions.entrySet()) {
            String[] coordinates = entry.getKey().split(",");
            int row = Integer.parseInt(coordinates[0]);
            int col = Integer.parseInt(coordinates[1]);

            String[] pieceInfo = entry.getValue().split(",");
            String pieceName = pieceInfo[0];
            String pieceType = pieceInfo[1];
            boolean isRed = Boolean.parseBoolean(pieceInfo[2]);

            //根据名称+类型+颜色创建棋子
            AbstractPiece piece = createPiece(pieceType, pieceName, isRed, row, col);
            if (piece != null) {
                model.getPieces().add(piece);
            }
        }
    }

    //根据名称创建棋子
    private AbstractPiece createPiece(String pieceType, String pieceName, boolean isRed, int row, int col) {
        try {
            Class<?> pieceClass = Class.forName("edu.sustech.xiangqi.model." + pieceType);
            //调用构造函数
            return (AbstractPiece) pieceClass.getDeclaredConstructor(String.class, int.class, int.class, boolean.class)
                    .newInstance(pieceName, row, col, isRed);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    //Getter和Setter
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getSaveName() { return saveName; }
    public void setSaveName(String saveName) { this.saveName = saveName; }
    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
    public Map<String, String> getPiecePositions() { return piecePositions; }
    public void setPiecePositions(Map<String, String> piecePositions) { this.piecePositions = piecePositions; }
    public boolean isRedTurn() { return redTurn; }
    public void setRedTurn(boolean redTurn) { this.redTurn = redTurn; }

    @Override
    public String toString() {
        return saveName + " (" + new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                .format(new java.util.Date(timestamp)) + ")";
    }
}