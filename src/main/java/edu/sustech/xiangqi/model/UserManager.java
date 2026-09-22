package edu.sustech.xiangqi.model;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 用户管理器，负责用户注册、登录和数据管理
 */
public class UserManager {
    private static final Logger logger = Logger.getLogger(UserManager.class.getName());
    private static final String USERS_FILE = "data/users.dat";
    private static UserManager instance;

    private Map<String, User> users; //用户名到用户对象的映射
    private User currentUser; //当前登录用户

    /**
     * 私有构造函数，实现单例模式
     */
    private UserManager() {
        users = new HashMap<>();
        loadUsers();
    }

    /**
     * 获取UserManager实例
     */
    public static synchronized UserManager getInstance() {
        if (instance == null) {
            instance = new UserManager();
        }
        return instance;
    }

    /**
     * 用户注册
     * @param username 用户名
     * @param password 密码
     * @return 注册是否成功
     */
    public boolean register(String username, String password) {
        //检查用户名是否已存在
        if (users.containsKey(username)) {
            logger.info("用户名 " + username + " 已存在");
            return false;
        }

        //哈希密码并创建用户
        String passwordHash = hashPassword(password);
        User newUser = new User(username, passwordHash);
        users.put(username, newUser);

        //保存用户数据
        saveUsers();
        logger.info("用户 " + username + " 注册成功");
        return true;
    }

    /**
     * 用户登录
     * @param username 用户名
     * @param password 密码
     * @return 登录是否成功
     */
    public boolean login(String username, String password) {
        User user = users.get(username);

        //检查用户是否存在
        if (user == null) {
            logger.info("用户名 " + username + " 不存在");
            return false;
        }

        //验证密码
        String passwordHash = hashPassword(password);
        if (user.getPasswordHash().equals(passwordHash)) {
            currentUser = user;
            logger.info("用户 " + username + " 登录成功");
            return true;
        } else {
            logger.info("用户 " + username + " 密码错误");
            return false;
        }
    }

    /**
     * 以游客身份登录
     */
    public void loginAsGuest() {
        currentUser = new User(); // 创建游客用户
        logger.info("游客登录");
    }

    /**
     * 退出登录
     */
    public void logout() {
        currentUser = null;
        logger.info("用户退出登录");
    }

    /**
     * 获取当前登录用户
     */
    public User getCurrentUser() {
        return currentUser;
    }

    /**
     * 获取当前登录用户名
     */
    public String getCurrentUsername() {
        if (currentUser != null) {
            return currentUser.getUsername();
        }
        return null;
    }

    /**
     * 检查当前是否为游客模式
     */
    public boolean isGuest() {
        return currentUser != null && currentUser.isGuest();
    }

    /**
     * 检查用户名是否存在
     */
    public boolean doesUserExist(String username) {
        return users.containsKey(username);
    }

    /**
     * 将密码进行SHA-256哈希
     */
    private String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedhash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            return bytesToHex(encodedhash);
        } catch (NoSuchAlgorithmException e) {
            logger.log(Level.SEVERE, "哈希算法不存在", e);
            return password; // 发生异常时直接返回原密码（不推荐，但作为应急措施）
        }
    }

    /**
     * 将字节数组转换为十六进制字符串
     */
    private static String bytesToHex(byte[] hash) {
        StringBuilder hexString = new StringBuilder(2 * hash.length);
        for (int i = 0; i < hash.length; i++) {
            String hex = Integer.toHexString(0xff & hash[i]);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }

    /**
     * 加载用户数据
     */
    @SuppressWarnings("unchecked")
    private void loadUsers() {
        File file = new File(USERS_FILE);
        if (file.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                users = (Map<String, User>) ois.readObject();
                logger.info("成功加载 " + users.size() + " 个用户");
            } catch (IOException | ClassNotFoundException e) {
                logger.log(Level.SEVERE, "加载用户数据失败", e);
                users = new HashMap<>(); //加载失败时使用空映射
            }
        } else {
            //创建data目录
            try {
                Path dataDir = Paths.get("data");
                Files.createDirectories(dataDir);
                logger.info("创建data目录");
            } catch (IOException e) {
                logger.log(Level.SEVERE, "创建data目录失败", e);
            }
        }
    }

    /**
     * 保存用户数据
     */
    private void saveUsers() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(USERS_FILE))) {
            oos.writeObject(users);
            logger.info("成功保存用户数据");
        } catch (IOException e) {
            logger.log(Level.SEVERE, "保存用户数据失败", e);
        }
    }
}