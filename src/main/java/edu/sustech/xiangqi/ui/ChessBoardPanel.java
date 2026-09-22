package edu.sustech.xiangqi.ui;

import edu.sustech.xiangqi.model.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.util.ArrayList;
import java.util.List;

public class ChessBoardPanel extends JPanel {
    private final ChessBoardModel model;
    private static final int CELL_SIZE = 64;
    private static final int MARGIN = 40;
    private static final int PIECE_RADIUS = 25;
    private static final int MARK_RADIUS = 10;
    //上一步走子标记大小
    private static final int LAST_MOVE_MARK_RADIUS = 8;

    private AbstractPiece selectedPiece = null;
    private List<Point> movePositions = new ArrayList<>(); //普通移动（橙色）
    private List<Point> attackPositions = new ArrayList<>(); //攻击吃子（红色）

    //主题
    private String currentTheme = "default";
    private BufferedImage boardBackground;
    private Color boardColor = new Color(220, 192, 92);
    private Color moveColor = new Color(255, 165, 0, 180);
    private Color attackColor = new Color(255, 0, 0, 200);
    private Color lastMoveColor = new Color(0, 100, 255, 180);
    private int lastFromRow = -1, lastFromCol = -1;
    private int lastToRow = -1, lastToCol = -1;
    //实时记录鼠标位置
    private int mouseRow = -1;
    private int mouseCol = -1;

    private JLabel turnLabel;
    private JLabel lastMoveLabel;
    private String lastPieceName;
    private boolean lastPieceIsRed;
    private GameManager gameManager;

    public ChessBoardPanel(ChessBoardModel model) {
        this.model = model;
        setPreferredSize(new Dimension(
                CELL_SIZE * (ChessBoardModel.getCols() - 1) + MARGIN * 2,
                CELL_SIZE * (ChessBoardModel.getRows() - 1) + MARGIN * 2
        ));
        setBackground(boardColor);
        loadTheme("default");

        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                mouseCol = (int) Math.floor((e.getX() - MARGIN) / (double) CELL_SIZE);
                mouseRow = (int) Math.floor((e.getY() - MARGIN) / (double) CELL_SIZE);
                if (model.isValidPosition(mouseRow, mouseCol)) {
                    repaint();
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                mouseRow = -1;
                mouseCol = -1;
                repaint();
            }
        });

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                handleMouseClick(e.getX(), e.getY());
            }
        });
    }

    //主题
    public void loadTheme(String themeName) {
        currentTheme = themeName;
        boardBackground = null;
        try {
            String bgPath = "";
            switch (themeName) {
                case "黑色":
                    bgPath = "./src/Background/Black.png";
                    boardColor = new Color(50, 50, 50);
                    break;
                case "绯红":
                    bgPath = "./src/Background/Scarlet.png";
                    boardColor = new Color(180, 50, 50);
                    break;
                case "绿色":
                    bgPath = "./src/Background/Green.png";
                    boardColor = new Color(50, 120, 50);
                    break;
                case "浅蓝":
                    bgPath = "./src/Background/LightBlue.png";
                    boardColor = new Color(173, 216, 230);
                    break;
                case "黄色":
                    bgPath = "./src/Background/Yellow.png";
                    boardColor = new Color(255, 255, 224);
                    break;
                case "春意":
                    bgPath = "./src/Background/SpringVibe.png";
                    boardColor = new Color(152, 251, 152);
                    break;
                case "橙色":
                    bgPath = "./src/Background/Orange.png";
                    boardColor = new Color(255, 228, 196);
                    break;
                case "紫色":
                    bgPath = "./src/Background/Purple.png";
                    boardColor = new Color(216, 191, 216);
                    break;
                case "玫红":
                    bgPath = "./src/Background/RoseRed.png";
                    boardColor = new Color(255, 192, 203);
                    break;
                case "池水":
                    bgPath = "./src/Background/PoorWater.png";
                    boardColor = new Color(240, 248, 255);
                    break;
                default:
                    boardColor = new Color(220, 192, 92);
                    break;
            }
            if (!bgPath.isEmpty()) {
                boardBackground = ImageIO.read(new File(bgPath));
            }
        } catch (IOException e) {
            System.err.println("加载主题背景失败: " + e.getMessage());
        }
        setBackground(boardColor);
        repaint();
    }

    //处理棋子点击
    private void handleMouseClick(int x, int y) {
        //计算行列
        int col = (int) Math.floor((x - MARGIN) / (double) CELL_SIZE);
        int row = (int) Math.floor((y - MARGIN) / (double) CELL_SIZE);

        if (!model.isValidPosition(row, col) || model.isGameOver()) {
            return;
        }

        if (selectedPiece == null) {
            AbstractPiece clickedPiece = model.getPieceAt(row, col);
            if (clickedPiece != null && clickedPiece.isRed() == model.isRedTurn()) {
                selectedPiece = clickedPiece;
                calculateAllValidPositions(clickedPiece);
                lastPieceName = clickedPiece.getName();
                lastPieceIsRed = clickedPiece.isRed();
            }
        } else {
            boolean moveSuccess = false;
            boolean isAttack = isInAttackPositions(row, col);
            boolean isMove = isInMovePositions(row, col);

            //在移动前记录当前位置
            int oldRow = selectedPiece.getRow();
            int oldCol = selectedPiece.getCol();

            if (isAttack || isMove) {
                moveSuccess = model.movePiece(selectedPiece, row, col);
            }

            if (moveSuccess) {
                //记录上一步的起点和终点
                lastFromRow = oldRow;
                lastFromCol = oldCol;
                lastToRow = row;
                lastToCol = col;

                String moveText = String.format("对方上一步：%s%s 从（%d,%d）→（%d,%d）",
                        lastPieceIsRed ? "红" : "黑", lastPieceName,
                        lastFromRow, lastFromCol, lastToRow, lastToCol);
                if (lastMoveLabel != null) {
                    lastMoveLabel.setText(moveText);
                }
                model.switchTurn();
                if (turnLabel != null) {
                    turnLabel.setText(model.isRedTurn() ? "当前回合：红方" : "当前回合：黑方");
                }
                if (model.isGameOver()) {
                    JOptionPane.showMessageDialog(this,
                            "游戏结束！" + model.getWinner() + "获胜！",
                            "胜负已分", JOptionPane.INFORMATION_MESSAGE);
                }
            }
            selectedPiece = null;
            movePositions.clear();
            attackPositions.clear();
        }
        repaint();
    }

    //判断攻击位置
    private boolean isInAttackPositions(int row, int col) {
        for (Point p : attackPositions) {
            if (p.x == row && p.y == col) {
                return true;
            }
        }
        return false;
    }

    //判断移动位置
    private boolean isInMovePositions(int row, int col) {
        for (Point p : movePositions) {
            if (p.x == row && p.y == col) {
                return true;
            }
        }
        return false;
    }

    //计算可移动/攻击位置
    private void calculateAllValidPositions(AbstractPiece piece) {
        movePositions.clear();
        attackPositions.clear();

        int originalRow = piece.getRow();
        int originalCol = piece.getCol();

        for (int r = 0; r < ChessBoardModel.getRows(); r++) {
            for (int c = 0; c < ChessBoardModel.getCols(); c++) {
                if (r == originalRow && c == originalCol) continue;

                if (piece.canMoveTo(r, c, model)) {
                    AbstractPiece targetPiece = model.getPieceAt(r, c);
                    //常规移动标记
                    if (targetPiece == null) {
                        movePositions.add(new Point(r, c));
                    }
                    //常规攻击标记（敌方棋子）
                    else if (targetPiece.isRed() != piece.isRed()) {
                        attackPositions.add(new Point(r, c));
                    }

                    //将帅相遇时，对方将帅位置强制加入攻击标记
                    if (piece instanceof GeneralPiece && targetPiece instanceof GeneralPiece) {
                        GeneralPiece general = (GeneralPiece) piece;
                        AbstractPiece enemyGeneral = null;
                        for (AbstractPiece p : model.getPieces()) {
                            if (p instanceof GeneralPiece && p.isRed() != general.isRed()) {
                                enemyGeneral = p;
                                break;
                            }
                        }
                        if (enemyGeneral != null && general.getCol() == enemyGeneral.getCol()) {
                            int minRow = Math.min(general.getRow(), enemyGeneral.getRow());
                            int maxRow = Math.max(general.getRow(), enemyGeneral.getRow());
                            boolean noObstacle = true;
                            for (int row = minRow + 1; row < maxRow; row++) {
                                if (model.getPieceAt(row, general.getCol()) != null) {
                                    noObstacle = false;
                                    break;
                                }
                            }
                            if (noObstacle) {
                                attackPositions.add(new Point(enemyGeneral.getRow(), enemyGeneral.getCol()));
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if (boardBackground != null) {
            g2d.drawImage(boardBackground, 0, 0, getWidth(), getHeight(), this);
        }

        //绘制顺序：先画棋子，再画标记
        drawBoard(g2d);
        drawPieces(g2d);
        drawLastMoveMarker(g2d);
        drawMoveAndAttackMarkers(g2d);
        drawMousePosition(g2d); //鼠标位置提示
    }

    //上一步标记改为蓝色圆形光点
    private void drawLastMoveMarker(Graphics2D g) {
        if (lastFromRow == -1 || lastToRow == -1) {
            return;
        }

        g.setColor(lastMoveColor);
        g.setStroke(new BasicStroke(2));
        int fromX = MARGIN + lastFromCol * CELL_SIZE;
        int fromY = MARGIN + lastFromRow * CELL_SIZE;
        g.drawOval(fromX - LAST_MOVE_MARK_RADIUS, fromY - LAST_MOVE_MARK_RADIUS,
                LAST_MOVE_MARK_RADIUS * 2, LAST_MOVE_MARK_RADIUS * 2);
        int toX = MARGIN + lastToCol * CELL_SIZE;
        int toY = MARGIN + lastToRow * CELL_SIZE;
        g.fillOval(toX - LAST_MOVE_MARK_RADIUS, toY - LAST_MOVE_MARK_RADIUS,
                LAST_MOVE_MARK_RADIUS * 2, LAST_MOVE_MARK_RADIUS * 2);
        g.setColor(Color.BLACK);
        g.drawOval(toX - LAST_MOVE_MARK_RADIUS, toY - LAST_MOVE_MARK_RADIUS,
                LAST_MOVE_MARK_RADIUS * 2, LAST_MOVE_MARK_RADIUS * 2);
        g.setColor(lastMoveColor);
    }

    private void drawMoveAndAttackMarkers(Graphics2D g) {
        //普通移动标记（橙色）
        g.setColor(moveColor);
        for (Point p : movePositions) {
            int x = MARGIN + p.y * CELL_SIZE;
            int y = MARGIN + p.x * CELL_SIZE;
            //绘制橙色实心圆
            g.fillOval(x - MARK_RADIUS, y - MARK_RADIUS, MARK_RADIUS * 2, MARK_RADIUS * 2);
            //橙色边框
            g.setColor(new Color(255, 140, 0));
            g.setStroke(new BasicStroke(1));
            g.drawOval(x - MARK_RADIUS, y - MARK_RADIUS, MARK_RADIUS * 2, MARK_RADIUS * 2);
            g.setColor(moveColor);
        }

        //攻击吃子标记
        g.setColor(attackColor);
        for (Point p : attackPositions) {
            int x = MARGIN + p.y * CELL_SIZE;
            int y = MARGIN + p.x * CELL_SIZE;

            //绘制红色实心圆
            g.fillOval(x - MARK_RADIUS, y - MARK_RADIUS, MARK_RADIUS * 2, MARK_RADIUS * 2);

            //绘制黑色边框，让红色光点更突出
            g.setColor(Color.BLACK);
            g.setStroke(new BasicStroke(2));
            g.drawOval(x - MARK_RADIUS, y - MARK_RADIUS, MARK_RADIUS * 2, MARK_RADIUS * 2);

            //恢复红色
            g.setColor(attackColor);
        }
    }

    //绘制鼠标位置
    private void drawMousePosition(Graphics2D g) {
        if (mouseRow == -1 || mouseCol == -1 || !model.isValidPosition(mouseRow, mouseCol)) {
            return;
        }

        int x = MARGIN + mouseCol * CELL_SIZE;
        int y = MARGIN + mouseRow * CELL_SIZE;

        //检查当前位置是否有棋子
        AbstractPiece piece = model.getPieceAt(mouseRow, mouseCol);

        //绘制半透明高亮框
        g.setColor(new Color(100, 100, 255, 80));
        g.setStroke(new BasicStroke(2));
        g.drawRect(x - PIECE_RADIUS - 2, y - PIECE_RADIUS - 2,
                PIECE_RADIUS * 2 + 4, PIECE_RADIUS * 2 + 4);

        //显示坐标信息
        g.setColor(Color.BLACK);
        g.setFont(new Font("楷体", Font.PLAIN, 12));
        String posText;
        if (piece != null) {
            posText = String.format("%s(%d,%d)", piece.getName(), mouseRow, mouseCol);
        } else {
            posText = String.format("(%d,%d)", mouseRow, mouseCol);
        }
        g.drawString(posText, x + PIECE_RADIUS + 5, y + 5);
    }

    //绘制棋盘
    private void drawBoard(Graphics2D g) {
        g.setColor(Color.BLACK);
        g.setStroke(new BasicStroke(2));
        //横线
        for (int i = 0; i < ChessBoardModel.getRows(); i++) {
            int y = MARGIN + i * CELL_SIZE;
            g.drawLine(MARGIN, y, MARGIN + (ChessBoardModel.getCols() - 1) * CELL_SIZE, y);
        }
        //竖线
        for (int i = 0; i < ChessBoardModel.getCols(); i++) {
            int x = MARGIN + i * CELL_SIZE;
            if (i == 0 || i == ChessBoardModel.getCols() - 1) {
                g.drawLine(x, MARGIN, x, MARGIN + (ChessBoardModel.getRows() - 1) * CELL_SIZE);
            } else {
                g.drawLine(x, MARGIN, x, MARGIN + 4 * CELL_SIZE);
                g.drawLine(x, MARGIN + 5 * CELL_SIZE, x, MARGIN + (ChessBoardModel.getRows() - 1) * CELL_SIZE);
            }
        }
        //楚河汉界
        g.setFont(new Font("楷体", Font.BOLD, 24));
        int riverY = MARGIN + 4 * CELL_SIZE + CELL_SIZE / 2;
        String chuHe = "楚河";
        String hanJie = "汉界";
        g.drawString(chuHe, MARGIN + CELL_SIZE * 2 - g.getFontMetrics().stringWidth(chuHe)/2, riverY + 8);
        g.drawString(hanJie, MARGIN + CELL_SIZE * 6 - g.getFontMetrics().stringWidth(hanJie)/2, riverY + 8);
    }

    //绘制棋子
    private void drawPieces(Graphics2D g) {
        for (AbstractPiece piece : model.getPieces()) {
            int x = MARGIN + piece.getCol() * CELL_SIZE;
            int y = MARGIN + piece.getRow() * CELL_SIZE;
            boolean isSelected = (piece == selectedPiece);

            g.setColor(new Color(245, 222, 179));
            g.fillOval(x - PIECE_RADIUS, y - PIECE_RADIUS, PIECE_RADIUS * 2, PIECE_RADIUS * 2);
            g.setColor(Color.BLACK);
            g.setStroke(new BasicStroke(2));
            g.drawOval(x - PIECE_RADIUS, y - PIECE_RADIUS, PIECE_RADIUS * 2, PIECE_RADIUS * 2);

            if (isSelected) {
                drawCornerBorders(g, x, y);
            }

            g.setColor(piece.isRed() ? new Color(200, 0, 0) : Color.BLACK);
            g.setFont(new Font("楷体", Font.BOLD, 22));
            FontMetrics fm = g.getFontMetrics();
            int textW = fm.stringWidth(piece.getName());
            int textH = fm.getAscent();
            g.drawString(piece.getName(), x - textW/2, y + textH/2 - 2);
        }
    }

    //选中棋子边框
    private void drawCornerBorders(Graphics2D g, int centerX, int centerY) {
        g.setColor(new Color(0, 100, 255));
        g.setStroke(new BasicStroke(3));
        int cornerSize = 32;
        int lineLen = 12;
        //左上
        g.drawLine(centerX - cornerSize, centerY - cornerSize, centerX - cornerSize + lineLen, centerY - cornerSize);
        g.drawLine(centerX - cornerSize, centerY - cornerSize, centerX - cornerSize, centerY - cornerSize + lineLen);
        //右上
        g.drawLine(centerX + cornerSize, centerY - cornerSize, centerX + cornerSize - lineLen, centerY - cornerSize);
        g.drawLine(centerX + cornerSize, centerY - cornerSize, centerX + cornerSize, centerY - cornerSize + lineLen);
        //左下
        g.drawLine(centerX - cornerSize, centerY + cornerSize, centerX - cornerSize + lineLen, centerY + cornerSize);
        g.drawLine(centerX - cornerSize, centerY + cornerSize, centerX - cornerSize, centerY + cornerSize - lineLen);
        //右下
        g.drawLine(centerX + cornerSize, centerY + cornerSize, centerX + cornerSize - lineLen, centerY + cornerSize);
        g.drawLine(centerX + cornerSize, centerY + cornerSize, centerX + cornerSize, centerY + cornerSize - lineLen);
    }

    //清空标记
    public void clearMoveTrail() {
        lastFromRow = -1;
        lastFromCol = -1;
        lastToRow = -1;
        lastToCol = -1;
        mouseRow = -1;
        mouseCol = -1;
        repaint();
    }

    //Setter方法
    public void setGameManager(GameManager gameManager) { this.gameManager = gameManager; }
    public void setLastMoveLabel(JLabel lastMoveLabel) { this.lastMoveLabel = lastMoveLabel; }
    public void setTurnLabel(JLabel turnLabel) { this.turnLabel = turnLabel; }
    public String getCurrentTheme() { return currentTheme; }
}