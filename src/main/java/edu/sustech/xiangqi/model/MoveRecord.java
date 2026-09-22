package edu.sustech.xiangqi.model;

public class MoveRecord {
    private String pieceType;
    private String fromPos;
    private String toPos;
    public MoveRecord(String pieceType,String fromPos,String toPos){
        this.pieceType=pieceType;
        this.fromPos=fromPos;
        this.toPos=toPos;
    }

    public String getFromPos() {
        return fromPos;
    }

    public String getPieceType() {
        return pieceType;
    }

    public String getToPos() {
        return toPos;
    }
}
