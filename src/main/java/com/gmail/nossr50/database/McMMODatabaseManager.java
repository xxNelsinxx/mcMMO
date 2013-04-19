package com.gmail.nossr50.database;

import com.gmail.nossr50.mcMMO;
import com.gmail.nossr50.util.Misc;

public class McMMODatabaseManager {
    private final mcMMO plugin;
    private boolean isUsingSQL;

    public McMMODatabaseManager(final mcMMO plugin, final boolean isUsingSQL) {
        this.plugin = plugin;
        this.isUsingSQL = isUsingSQL;
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
}
