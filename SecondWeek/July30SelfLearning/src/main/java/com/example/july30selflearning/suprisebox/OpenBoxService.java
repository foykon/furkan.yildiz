package com.example.july30selflearning.suprisebox;

import java.util.HashMap;
import java.util.Map;

public class OpenBoxService {
    private static OpenBoxService instance;
    private Map<String, String> userRewardMap;

    private OpenBoxService() {
        userRewardMap = new HashMap<>();
    }
    public static OpenBoxService getInstance() {
        if (instance == null) {
            instance = new OpenBoxService();
        }
        return instance;
    }

    public void put(String user, String reward) {
        userRewardMap.put(user, reward);
    }

    public String get(String user) {
        return userRewardMap.get(user);
    }

    public Map<String, String> getUserRewardMap(String user) {
        return userRewardMap;
    }

    public boolean isOpened(String user) {

        return userRewardMap.containsKey(user);
    }

}
