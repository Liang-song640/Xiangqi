package edu.sustech.xiangqi.model;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
public class GameManager {
    //储存移动记录
    private List<MoveRecord> moveHistory=new ArrayList<>();
    private JLabel lastMoveLabel;
    public GameManager(JLabel lastMoveLabel){
        this.lastMoveLabel=lastMoveLabel;
    }

    public void recordMove(String pieceType,int fromRow,int fromCol,int toRow,int toCol){
        String fromPos="("+fromRow+","+fromCol+")";
        String toPos="("+toRow+","+toCol+")";
        //创建移动记录并保存
        MoveRecord lastMove=new MoveRecord(pieceType,fromPos,toPos);
        moveHistory.add(lastMove);
        updataLastMoveDisplay(lastMove);
    }
    private void updataLastMoveDisplay(MoveRecord lastMove){
        if(lastMoveLabel==null)
            return;
        //显示从哪移步到哪一步
        String displayText=String.format("对方上一步：%s 从%s到%s",lastMove.getPieceType(),lastMove.getFromPos(),lastMove.getToPos());
        lastMoveLabel.setText(displayText);
    }
    public void reset(){
        moveHistory.clear();
        if(lastMoveLabel!=null){
            lastMoveLabel.setText("对方上一步暂无操作");
        }
    }
    public List<MoveRecord> getMoveHistory(){
        return moveHistory;
    }
}
