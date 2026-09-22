package edu.sustech.xiangqi.model;

import java.io.*;
import java.nio.file.*;
import java.util.*;

/**
 * 存档管理器，负责游戏存档的保存、读取和管理
 */
public class SaveManager {
    private static final String SAVE_DIR = "saves";
    private static SaveManager instance;

    /**
     * 私有构造函数，实现单例模式
     */
    private SaveManager() {
        // 确保存档目录存在
        ensureSaveDirectoryExists();
    }

    /**
     * 获取单例实例
     */
    public static synchronized SaveManager getInstance() {
        if (instance == null) {
            instance = new SaveManager();
        }
        return instance;
    }

    /**
     * 确保存档目录存在
     */
    private void ensureSaveDirectoryExists() {
        File dir = new File(SAVE_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    /**
     * 保存游戏状态
     * @param model 棋盘模型
     * @param username 用户名
     * @param saveName 存档名称
     * @return 是否保存成功
     */
    public boolean saveGame(ChessBoardModel model, String username, String saveName) {
        try {
            // 创建游戏状态对象
            GameState gameState = new GameState(model, username, saveName);

            // 确保用户存档目录存在
            String userSaveDir = SAVE_DIR + File.separator + username;
            File dir = new File(userSaveDir);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            // 保存到文件
            String savePath = userSaveDir + File.separator + saveName + ".ser";
            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(savePath))) {
                oos.writeObject(gameState);
            }

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 读取游戏存档
     * @param username 用户名
     * @param saveName 存档名称
     * @return 游戏状态对象，如果读取失败返回null
     */
    public GameState loadGame(String username, String saveName) {
        try {
            String savePath = SAVE_DIR + File.separator + username + File.separator + saveName + ".ser";

            // 检查存档文件是否存在
            File saveFile = new File(savePath);
            if (!saveFile.exists()) {
                return null;
            }

            // 读取存档文件
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(savePath))) {
                GameState gameState = (GameState) ois.readObject();

                // 验证存档完整性
                if (isValidGameState(gameState)) {
                    return gameState;
                } else {
                    System.err.println("存档文件损坏或格式错误");
                    return null;
                }
            }
        } catch (Exception e) {
            // 捕获所有异常，防止程序崩溃
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 检查存档是否有效
     * @param gameState 游戏状态对象
     * @return 是否有效
     */
    private boolean isValidGameState(GameState gameState) {
        if (gameState == null) {
            return false;
        }

        // 检查关键字段是否为空
        if (gameState.getUsername() == null || gameState.getSaveName() == null) {
            return false;
        }

        // 检查棋子位置信息是否为空
        if (gameState.getPiecePositions() == null) {
            return false;
        }

        return true;
    }

    /**
     * 获取用户的所有存档
     * @param username 用户名
     * @return 存档列表
     */
    public List<GameState> getUserSaves(String username) {
        List<GameState> saves = new ArrayList<>();

        try {
            String userSaveDir = SAVE_DIR + File.separator + username;
            File dir = new File(userSaveDir);

            if (dir.exists() && dir.isDirectory()) {
                File[] saveFiles = dir.listFiles((d, name) -> name.endsWith(".ser"));

                if (saveFiles != null) {
                    for (File saveFile : saveFiles) {
                        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(saveFile))) {
                            GameState gameState = (GameState) ois.readObject();
                            if (isValidGameState(gameState)) {
                                saves.add(gameState);
                            }
                        }
                    }
                }
            }

            // 时间戳降序排序（最新的存档在前）
            saves.sort((s1, s2) -> Long.compare(s2.getTimestamp(), s1.getTimestamp()));

        } catch (Exception e) {
            //捕获所有异常，防止程序崩溃
            e.printStackTrace();
        }

        return saves;
    }

    /**
     * 检查存档名称是否已存在
     * @param username 用户名
     * @param saveName 存档名称
     * @return 是否已存在
     */
    public boolean isSaveNameExists(String username, String saveName) {
        String savePath = SAVE_DIR + File.separator + username + File.separator + saveName + ".ser";
        File saveFile = new File(savePath);
        return saveFile.exists();
    }

    /**
     * 删除存档
     * @param username 用户名
     * @param saveName 存档名称
     * @return 是否删除成功
     */
    public boolean deleteSave(String username, String saveName) {
        try {
            String savePath = SAVE_DIR + File.separator + username + File.separator + saveName + ".ser";
            File saveFile = new File(savePath);
            return saveFile.delete();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}