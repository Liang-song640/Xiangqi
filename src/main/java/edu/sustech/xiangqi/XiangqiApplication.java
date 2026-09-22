package edu.sustech.xiangqi;

import edu.sustech.xiangqi.model.*;
import edu.sustech.xiangqi.ui.ChessBoardPanel;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class XiangqiApplication {
    private static JLabel userInfoLabel;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            //登录窗口
            JFrame loginFrame = new JFrame("登录");
            loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            loginFrame.setLayout(null);
            loginFrame.setLocation(450, 200);
            loginFrame.setSize(500, 500);

            JLabel label1 = new JLabel("用户:");
            label1.setFont(new Font("楷体", Font.PLAIN, 16));
            label1.setLocation(130, 40);
            label1.setSize(80, 80);
            loginFrame.add(label1);

            JTextField field1 = new JTextField();
            field1.setFont(new Font("楷体", Font.PLAIN, 16));
            field1.setLocation(210, 60);
            field1.setSize(100, 40);
            loginFrame.add(field1);

            JLabel label2 = new JLabel("密码:");
            label2.setFont(new Font("楷体", Font.PLAIN, 16));
            label2.setLocation(130, 100);
            label2.setSize(80, 80);
            loginFrame.add(label2);

            JPasswordField field2 = new JPasswordField();
            field2.setFont(new Font("楷体", Font.PLAIN, 16));
            field2.setLocation(210, 120);
            field2.setSize(100, 40);
            loginFrame.add(field2);

            JButton button1 = new JButton("登录");
            button1.setFont(new Font("楷体", Font.PLAIN, 16));
            button1.setLocation(210, 180);
            button1.setSize(100, 40);
            loginFrame.add(button1);

            JButton button2 = new JButton("注册");
            button2.setFont(new Font("楷体", Font.PLAIN, 16));
            button2.setLocation(210, 240);
            button2.setSize(100, 40);
            loginFrame.add(button2);

            JButton button3 = new JButton("游客访问");
            button3.setFont(new Font("楷体", Font.PLAIN, 16));
            button3.setLocation(210, 300);
            button3.setSize(100, 40);
            loginFrame.add(button3);

            //进入游戏逻辑
            Runnable enterGame = () -> {
                loginFrame.dispose();
                JFrame gameFrame = new JFrame("中国象棋");
                gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                gameFrame.setLayout(new BorderLayout());

                //初始化音乐播放器
                MusicPlayer musicPlayer = new MusicPlayer();
                String bgm2Path = "./src/Music/BGM2.wav";
                String bgm3Path = "./src/Music/BGM3.wav";
                String capturePath = "./src/Music/BGM4.wav";
                musicPlayer.loadMusic("bgm2", bgm2Path);
                musicPlayer.loadMusic("bgm3", bgm3Path);
                musicPlayer.loadSoundEffect("bgm4", capturePath);

                //初始化棋盘
                ChessBoardModel model = new ChessBoardModel();
                model.setMusicPlayer(musicPlayer);
                ChessBoardPanel boardPanel = new ChessBoardPanel(model);

                //顶部标签
                JLabel turnLabel = new JLabel("当前回合：红方", SwingConstants.CENTER);
                turnLabel.setFont(new Font("楷体", Font.BOLD, 20));
                turnLabel.setPreferredSize(new Dimension(0, 50));

                JLabel lastMoveLabel = new JLabel("对方上一步操作是：暂无操作", SwingConstants.CENTER);
                lastMoveLabel.setFont(new Font("楷体", Font.PLAIN, 18));
                lastMoveLabel.setForeground(Color.RED);
                lastMoveLabel.setPreferredSize(new Dimension(0, 40));

                //音乐控制面板
                JPanel musicPanel = new JPanel();
                musicPanel.setBorder(BorderFactory.createTitledBorder("音乐控制"));
                musicPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
                JComboBox<String> musicSelector = new JComboBox<>(new String[]{"背景音乐1", "背景音乐2"});
                musicPanel.add(new JLabel("选择音乐："));
                musicPanel.add(musicSelector);
                JButton stopBtn = new JButton("停止音乐");
                musicPanel.add(stopBtn);
                JSlider volumeSlider = new JSlider(0, 100, 100);
                volumeSlider.setPreferredSize(new Dimension(150, 20));
                musicPanel.add(new JLabel("音量："));
                musicPanel.add(volumeSlider);

                //主题控制面板（补充10种背景）
                JPanel themePanel = new JPanel();
                themePanel.setBorder(BorderFactory.createTitledBorder("背景设置"));
                themePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
                JComboBox<String> themeSelector = new JComboBox<>(new String[]{
                        "默认主题", "黑色主题", "绯红主题", "绿色主题",
                        "浅蓝主题", "黄色主题", "春意主题", "橙色主题",
                        "紫色主题", "玫红主题", "池水主题"
                });
                themePanel.add(new JLabel("选择背景："));
                themePanel.add(themeSelector);

                //音乐控制逻辑
                musicSelector.addActionListener(e -> {
                    int idx = musicSelector.getSelectedIndex();
                    if (idx == 0) musicPlayer.playMusic("bgm2");
                    else musicPlayer.playMusic("bgm3");
                });
                stopBtn.addActionListener(e -> musicPlayer.stopCurrent());
                volumeSlider.addChangeListener(e -> musicPlayer.setVolume(volumeSlider.getValue()));

                //主题控制逻辑（匹配10种背景）
                themeSelector.addActionListener(e -> {
                    int idx = themeSelector.getSelectedIndex();
                    switch (idx) {
                        case 1:
                            boardPanel.loadTheme("黑色");
                            break;
                        case 2:
                            boardPanel.loadTheme("绯红");
                            break;
                        case 3:
                            boardPanel.loadTheme("绿色");
                            break;
                        case 4:
                            boardPanel.loadTheme("浅蓝");
                            break;
                        case 5:
                            boardPanel.loadTheme("黄色");
                            break;
                        case 6:
                            boardPanel.loadTheme("春意");
                            break;
                        case 7:
                            boardPanel.loadTheme("橙色");
                            break;
                        case 8:
                            boardPanel.loadTheme("紫色");
                            break;
                        case 9:
                            boardPanel.loadTheme("玫红");
                            break;
                        case 10:
                            boardPanel.loadTheme("池水");
                            break;
                        default:
                            boardPanel.loadTheme("default");
                            break;
                    }
                });

                //北部面板
                JPanel northPanel = new JPanel(new GridLayout(4, 1));
                northPanel.add(turnLabel);
                northPanel.add(lastMoveLabel);
                northPanel.add(musicPanel);
                northPanel.add(themePanel);
                gameFrame.add(northPanel, BorderLayout.NORTH);

                //底部控制面板
                JPanel controlPanel = new JPanel(new GridLayout(1, 5));
                controlPanel.setPreferredSize(new Dimension(0, 50));

                JButton restartBtn = new JButton("重新开始");
                restartBtn.setFont(new Font("楷体", Font.PLAIN, 16));
                restartBtn.addActionListener(e -> {
                    model.resetBoard();
                    turnLabel.setText("当前回合：红方");
                    lastMoveLabel.setText("对方上一步暂无操作");
                    boardPanel.clearMoveTrail();
                    boardPanel.repaint();
                });

                JButton saveBtn = new JButton("保存游戏");
                saveBtn.setFont(new Font("楷体", Font.PLAIN, 16));
                saveBtn.setEnabled(!UserManager.getInstance().isGuest());
                saveBtn.addActionListener(e -> saveGame(model, gameFrame));

                JButton loadBtn = new JButton("读取存档");
                loadBtn.setFont(new Font("楷体", Font.PLAIN, 16));
                loadBtn.setEnabled(!UserManager.getInstance().isGuest());
                loadBtn.addActionListener(e -> loadGame(model, boardPanel, turnLabel, lastMoveLabel, gameFrame));

                JButton logoutBtn = new JButton("退出登录");
                logoutBtn.setFont(new Font("楷体", Font.PLAIN, 16));
                logoutBtn.addActionListener(e -> {
                    musicPlayer.release();
                    UserManager.getInstance().logout();
                    gameFrame.dispose();
                    SwingUtilities.invokeLater(() -> XiangqiApplication.main(new String[0]));
                });

                JButton exitBtn = new JButton("退出游戏");
                exitBtn.setFont(new Font("楷体", Font.PLAIN, 16));
                exitBtn.addActionListener(e -> {
                    musicPlayer.release();
                    System.exit(0);
                });

                controlPanel.add(restartBtn);
                controlPanel.add(saveBtn);
                controlPanel.add(loadBtn);
                controlPanel.add(logoutBtn);
                controlPanel.add(exitBtn);

                //用户信息标签
                userInfoLabel = new JLabel("当前用户：" + (UserManager.getInstance().getCurrentUsername() != null ?
                        UserManager.getInstance().getCurrentUsername() : "游客"), SwingConstants.CENTER);
                userInfoLabel.setFont(new Font("楷体", Font.PLAIN, 16));
                userInfoLabel.setPreferredSize(new Dimension(0, 30));

                JPanel bottomPanel = new JPanel(new BorderLayout());
                bottomPanel.add(controlPanel, BorderLayout.NORTH);
                bottomPanel.add(userInfoLabel, BorderLayout.SOUTH);
                gameFrame.add(bottomPanel, BorderLayout.SOUTH);

                //棋盘面板
                gameFrame.add(boardPanel, BorderLayout.CENTER);
                boardPanel.setTurnLabel(turnLabel);
                boardPanel.setLastMoveLabel(lastMoveLabel);
                boardPanel.setGameManager(new GameManager(lastMoveLabel));

                //窗口设置
                gameFrame.pack();
                gameFrame.setLocationRelativeTo(null);
                gameFrame.setResizable(false);
                gameFrame.setVisible(true);

                //关闭时释放资源
                gameFrame.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        musicPlayer.release();
                    }
                });
            };

            //登录/注册/游客逻辑
            button1.addActionListener(e -> {
                String username = field1.getText().trim();
                String password = new String(field2.getPassword());
                if (username.isEmpty() || password.isEmpty()) {
                    JOptionPane.showMessageDialog(loginFrame, "用户名和密码不能为空！", "错误", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (UserManager.getInstance().login(username, password)) {
                    enterGame.run();
                } else {
                    JOptionPane.showMessageDialog(loginFrame, "用户名或密码错误！", "登录失败", JOptionPane.ERROR_MESSAGE);
                    field2.setText("");
                }
            });

            button3.addActionListener(e -> {
                UserManager.getInstance().loginAsGuest();
                enterGame.run();
            });

            button2.addActionListener(e -> {
                String username = field1.getText().trim();
                String password = new String(field2.getPassword());
                if (username.isEmpty() || password.isEmpty()) {
                    JOptionPane.showMessageDialog(loginFrame, "用户名和密码不能为空！", "错误", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (UserManager.getInstance().register(username, password)) {
                    JOptionPane.showMessageDialog(loginFrame, "注册成功！", "提示", JOptionPane.INFORMATION_MESSAGE);
                    UserManager.getInstance().login(username, password);
                    enterGame.run();
                } else {
                    JOptionPane.showMessageDialog(loginFrame, "用户名已存在！", "注册失败", JOptionPane.ERROR_MESSAGE);
                    field1.setText("");
                    field2.setText("");
                }
            });

            loginFrame.setVisible(true);
        });
    }

    // 保存游戏
    private static void saveGame(ChessBoardModel model, JFrame parentFrame) {
        if (UserManager.getInstance().isGuest()) {
            JOptionPane.showMessageDialog(parentFrame, "游客模式无法存档，请先登录！", "提示", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        String username = UserManager.getInstance().getCurrentUsername();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
        String defaultName = "存档_" + sdf.format(new Date());
        Object input = JOptionPane.showInputDialog(parentFrame, "请输入存档名称：", "保存游戏", JOptionPane.PLAIN_MESSAGE, null, null, defaultName);
        if (input == null) return;
        String saveName = input.toString().trim();
        if (saveName.isEmpty()) {
            JOptionPane.showMessageDialog(parentFrame, "存档名称不能为空！", "错误", JOptionPane.ERROR_MESSAGE);
            return;
        }
        saveName = saveName.replaceAll("[\\/:*?\"\u003c\u003e|]", "_");
        if (SaveManager.getInstance().isSaveNameExists(username, saveName)) {
            int option = JOptionPane.showConfirmDialog(parentFrame, "存档已存在，是否覆盖？", "确认覆盖", JOptionPane.YES_NO_OPTION);
            if (option != JOptionPane.YES_OPTION) return;
        }
        if (SaveManager.getInstance().saveGame(model, username, saveName)) {
            JOptionPane.showMessageDialog(parentFrame, "游戏保存成功！", "提示", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(parentFrame, "游戏保存失败！", "错误", JOptionPane.ERROR_MESSAGE);
        }
    }

    //存档读取逻辑
    private static void loadGame(ChessBoardModel model, ChessBoardPanel boardPanel, JLabel turnLabel, JLabel lastMoveLabel, JFrame parentFrame) {
        if (UserManager.getInstance().isGuest()) {
            JOptionPane.showMessageDialog(parentFrame, "游客模式无法读档，请先登录！", "提示", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        String username = UserManager.getInstance().getCurrentUsername();
        List<GameState> saves = SaveManager.getInstance().getUserSaves(username);
        if (saves == null || saves.isEmpty()) {
            JOptionPane.showMessageDialog(parentFrame, "没有找到存档！", "提示", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        //存档选择对话框
        JDialog dialog = new JDialog(parentFrame, "选择存档", true);
        dialog.setLayout(new BorderLayout());
        dialog.setSize(500, 350);
        dialog.setLocationRelativeTo(parentFrame);

        //存档列表（显示名称+时间）
        JList<GameState> saveList = new JList<>(saves.toArray(new GameState[0]));
        saveList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        saveList.setFont(new Font("楷体", Font.PLAIN, 16));
        saveList.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (c instanceof JLabel && value instanceof GameState) {
                    GameState state = (GameState) value;
                    String displayText = String.format("%s - %s",
                            state.getSaveName(),
                            new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(state.getTimestamp())));
                    ((JLabel) c).setText(displayText);
                }
                return c;
            }
        });
        dialog.add(new JScrollPane(saveList), BorderLayout.CENTER);

        //按钮面板
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton loadBtn = new JButton("加载存档");
        loadBtn.setFont(new Font("楷体", Font.PLAIN, 16));
        loadBtn.addActionListener(e -> {
            GameState state = saveList.getSelectedValue();
            if (state != null) {
                dialog.dispose();
                try {
                    //先清空模型再应用存档
                    model.getPieces().clear();
                    state.applyToModel(model);
                    turnLabel.setText("当前回合：" + (model.isRedTurn() ? "红方" : "黑方"));
                    lastMoveLabel.setText("对方上一步操作是：暂无操作");
                    boardPanel.clearMoveTrail();
                    boardPanel.repaint();
                    JOptionPane.showMessageDialog(parentFrame, "存档加载成功！", "提示", JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(parentFrame, "存档加载失败：" + ex.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                }
            } else {
                JOptionPane.showMessageDialog(dialog, "请选择一个存档！", "提示", JOptionPane.WARNING_MESSAGE);
            }
        });

        JButton deleteBtn = new JButton("删除存档");
        deleteBtn.setFont(new Font("楷体", Font.PLAIN, 16));
        deleteBtn.addActionListener(e -> {
            GameState state = saveList.getSelectedValue();
            if (state != null) {
                int confirm = JOptionPane.showConfirmDialog(dialog,
                        "确定删除存档：" + state.getSaveName() + "？",
                        "删除确认", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    boolean deleted = SaveManager.getInstance().deleteSave(username, state.getSaveName());
                    if (deleted) {
                        JOptionPane.showMessageDialog(dialog, "存档删除成功！", "提示", JOptionPane.INFORMATION_MESSAGE);
                        List<GameState> newSaves = SaveManager.getInstance().getUserSaves(username);
                        saveList.setListData(newSaves.toArray(new GameState[0]));
                    } else {
                        JOptionPane.showMessageDialog(dialog, "存档删除失败！", "错误", JOptionPane.ERROR_MESSAGE);
                    }
                }
            } else {
                JOptionPane.showMessageDialog(dialog, "请选择一个存档！", "提示", JOptionPane.WARNING_MESSAGE);
            }
        });

        JButton cancelBtn = new JButton("取消");
        cancelBtn.setFont(new Font("楷体", Font.PLAIN, 16));
        cancelBtn.addActionListener(e -> dialog.dispose());

        btnPanel.add(deleteBtn);
        btnPanel.add(loadBtn);
        btnPanel.add(cancelBtn);
        dialog.add(btnPanel, BorderLayout.SOUTH);

        dialog.setVisible(true);
    }

    public static void updateUserInfoLabel() {
        if (userInfoLabel != null) {
            userInfoLabel.setText("当前用户：" + (UserManager.getInstance().getCurrentUsername() != null ?
                    UserManager.getInstance().getCurrentUsername() : "游客"));
        }
    }
}