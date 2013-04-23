package com.gmail.nossr50.database;

import java.io.File;
import java.io.IOException;

import com.gmail.nossr50.mcMMO;
import com.gmail.nossr50.util.Misc;

public class McMMODatabaseManager {
    private final mcMMO plugin;
    private final boolean isUsingSQL;
    private File usersFile;

    public McMMODatabaseManager(final mcMMO plugin, final boolean isUsingSQL) {
        this.plugin = plugin;
        this.isUsingSQL = isUsingSQL;

        if (!isUsingSQL) {
            usersFile = new File(mcMMO.getUsersFilePath());
            createFlatfileDatabase();
        }
    }

    public void purgePowerlessUsers() {
        plugin.getLogger().info("Purging powerless users...");
        plugin.getLogger().info("Purged " + (isUsingSQL ? DatabaseManager.purgePowerlessSQL() : LeaderboardManager.purgePowerlessFlatfile()) + " users from the database.");
    }

    public void purgeOldUsers() {
        plugin.getLogger().info("Purging old users...");
        plugin.getLogger().info("Purged " + (isUsingSQL ? DatabaseManager.purgeOldSQL() : LeaderboardManager.removeOldFlatfileUsers()) + " users from the database.");
    }

    public boolean removeUser(String playerName) {
        if (isUsingSQL ? DatabaseManager.removeUserSQL(playerName) : LeaderboardManager.removeFlatFileUser(playerName)) {
            Misc.profileCleanup(playerName);
            return true;
        }

        return false;
    }

    private void createFlatfileDatabase() {
        if (usersFile.exists()) {
            return;
        }

        usersFile.getParentFile().mkdir();

        try {
            new File(mcMMO.getUsersFilePath()).createNewFile();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
