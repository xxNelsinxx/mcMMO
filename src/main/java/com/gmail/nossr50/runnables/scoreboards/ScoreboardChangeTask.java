package com.gmail.nossr50.runnables.scoreboards;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Scoreboard;

public class ScoreboardChangeTask extends BukkitRunnable {
    private Player player;
    private Scoreboard oldScoreboard;

    public ScoreboardChangeTask(Player player, Scoreboard oldScoreboard) {
        this.player = player;
        this.oldScoreboard = oldScoreboard;
    }

    @Override
    public void run() {
        player.setScoreboard(oldScoreboard);
    }
}
