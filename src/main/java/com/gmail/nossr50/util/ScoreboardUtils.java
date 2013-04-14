package com.gmail.nossr50.util;

import org.bukkit.Server;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;

import com.gmail.nossr50.mcMMO;
import com.gmail.nossr50.datatypes.player.McMMOPlayer;
import com.gmail.nossr50.datatypes.player.PlayerProfile;
import com.gmail.nossr50.datatypes.skills.SkillType;
import com.gmail.nossr50.util.skills.SkillUtils;

public class ScoreboardUtils {
    public final static String STATS_HEADER = "mcMMO Stats";
    private static Objective stats;

    public static void setupStatsScoreboard(McMMOPlayer mcMMOPlayer) {
        PlayerProfile profile = mcMMOPlayer.getProfile();
        profile.setStatsScoreboard(mcMMO.p.getServer().getScoreboardManager().getNewScoreboard());

        stats = profile.getStatsScoreboard().registerNewObjective(STATS_HEADER, "SKILL_LEVELS");
        stats.setDisplaySlot(DisplaySlot.SIDEBAR);
    }

    public static void enableStatsScoreboard(McMMOPlayer mcMMOPlayer) {
        updateScores(mcMMOPlayer);
        mcMMOPlayer.getPlayer().setScoreboard(mcMMOPlayer.getProfile().getStatsScoreboard());
    }

    public static void updateScore(McMMOPlayer mcMMOPlayer, SkillType skill) {
        stats.getScore(mcMMO.p.getServer().getOfflinePlayer(SkillUtils.getSkillName(skill))).setScore(mcMMOPlayer.getProfile().getSkillLevel(skill));
    }

    private static void updateScores(McMMOPlayer mcMMOPlayer) {
        PlayerProfile profile = mcMMOPlayer.getProfile();
        Server server = mcMMO.p.getServer();

        for (SkillType skill : SkillType.values()) {
            stats.getScore(server.getOfflinePlayer(SkillUtils.getSkillName(skill))).setScore(profile.getSkillLevel(skill));
        }
    }
}
