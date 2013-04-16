package com.gmail.nossr50.util.scoreboards;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.OfflinePlayer;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import com.gmail.nossr50.mcMMO;
import com.gmail.nossr50.database.LeaderboardManager;
import com.gmail.nossr50.datatypes.database.PlayerStat;
import com.gmail.nossr50.datatypes.player.McMMOPlayer;
import com.gmail.nossr50.datatypes.player.PlayerProfile;
import com.gmail.nossr50.datatypes.skills.SkillType;
import com.gmail.nossr50.util.skills.SkillUtils;

public class ScoreboardManager {
//    public static final OfflinePlayer ACROBATICS_PLAYER = mcMMO.p.getServer().getOfflinePlayer(SkillUtils.getSkillName(SkillType.ACROBATICS));
//    public static final OfflinePlayer ARCHERY_PLAYER = mcMMO.p.getServer().getOfflinePlayer(SkillUtils.getSkillName(SkillType.ARCHERY));
//    public static final OfflinePlayer AXES_PLAYER = mcMMO.p.getServer().getOfflinePlayer(SkillUtils.getSkillName(SkillType.AXES));
//    public static final OfflinePlayer EXCAVATION_PLAYER = mcMMO.p.getServer().getOfflinePlayer(SkillUtils.getSkillName(SkillType.EXCAVATION));
//    public static final OfflinePlayer FISHING_PLAYER = mcMMO.p.getServer().getOfflinePlayer(SkillUtils.getSkillName(SkillType.FISHING));
//    public static final OfflinePlayer HERBALISM_PLAYER = mcMMO.p.getServer().getOfflinePlayer(SkillUtils.getSkillName(SkillType.HERBALISM));
//    public static final OfflinePlayer MINING_PLAYER = mcMMO.p.getServer().getOfflinePlayer(SkillUtils.getSkillName(SkillType.MINING));
//    public static final OfflinePlayer REPAIR_PLAYER = mcMMO.p.getServer().getOfflinePlayer(SkillUtils.getSkillName(SkillType.REPAIR));
//    public static final OfflinePlayer SMELTING_PLAYER = mcMMO.p.getServer().getOfflinePlayer(SkillUtils.getSkillName(SkillType.SMELTING));
//    public static final OfflinePlayer SWORDS_PLAYER = mcMMO.p.getServer().getOfflinePlayer(SkillUtils.getSkillName(SkillType.SWORDS));
//    public static final OfflinePlayer TAMING_PLAYER = mcMMO.p.getServer().getOfflinePlayer(SkillUtils.getSkillName(SkillType.TAMING));
//    public static final OfflinePlayer UNARMED_PLAYER = mcMMO.p.getServer().getOfflinePlayer(SkillUtils.getSkillName(SkillType.UNARMED));
//    public static final OfflinePlayer WOODCUTTING_PLAYER = mcMMO.p.getServer().getOfflinePlayer(SkillUtils.getSkillName(SkillType.WOODCUTTING));

    public static final Map<String, Scoreboard> PLAYER_STATS_SCOREBOARDS = new HashMap<String, Scoreboard>();

    public static Scoreboard globalStatsScoreboard;

    private static Objective playerStats;
    private static Objective globalPowerlevel;

    public final static String PLAYER_STATS_HEADER   = "mcMMO Stats";
    public final static String PLAYER_STATS_CRITERIA = "Player Skill Levels";

    public final static String GLOBAL_STATS_POWER_LEVEL = "Power Level";

    public static void setupPlayerStatsScoreboard(String playerName) {
        if (PLAYER_STATS_SCOREBOARDS.containsKey(playerName)) {
            return;
        }

        Scoreboard scoreboard = mcMMO.p.getServer().getScoreboardManager().getNewScoreboard();

        playerStats = scoreboard.registerNewObjective(PLAYER_STATS_HEADER, PLAYER_STATS_CRITERIA);
        playerStats.setDisplaySlot(DisplaySlot.SIDEBAR);

        PLAYER_STATS_SCOREBOARDS.put(playerName, scoreboard);
    }

    public static void setupGlobalStatsScoreboard() {
        if (globalStatsScoreboard != null) {
            return;
        }

        globalStatsScoreboard = mcMMO.p.getServer().getScoreboardManager().getNewScoreboard();

        for (SkillType skill : SkillType.values()) {
            globalStatsScoreboard.registerNewObjective(SkillUtils.getSkillName(skill), PLAYER_STATS_CRITERIA);
        }

        globalPowerlevel = globalStatsScoreboard.registerNewObjective(GLOBAL_STATS_POWER_LEVEL, PLAYER_STATS_CRITERIA);
    }

    public static void enablePlayerStatsScoreboard(McMMOPlayer mcMMOPlayer) {
        Player player = mcMMOPlayer.getPlayer();
        Scoreboard scoreboard = PLAYER_STATS_SCOREBOARDS.get(player.getName());

        if (player.getScoreboard() == scoreboard) {
            return;
        }

        updatePlayerStatsScores(mcMMOPlayer);
        player.setScoreboard(scoreboard);
    }

    public static void enableGlobalStatsScoreboard(Player player, String skillName) {
        Scoreboard scoreboard = player.getScoreboard();
        Objective objective = getGlobalObjective(skillName);

        if (scoreboard.getObjective(DisplaySlot.SIDEBAR) == objective) {
            return;
        }

        updateGlobalStatsScores(objective, skillName);

        if (scoreboard == globalStatsScoreboard) {
            return;
        }

        player.setScoreboard(globalStatsScoreboard);
    }

    public static void updatePlayerStatsScore(McMMOPlayer mcMMOPlayer, SkillType skill) {
        playerStats.getScore(mcMMO.p.getServer().getOfflinePlayer(SkillUtils.getSkillName(skill))).setScore(mcMMOPlayer.getProfile().getSkillLevel(skill));
    }

    private static void updatePlayerStatsScores(McMMOPlayer mcMMOPlayer) {
        PlayerProfile profile = mcMMOPlayer.getProfile();
        Server server = mcMMO.p.getServer();

        for (SkillType skill : SkillType.values()) {
            playerStats.getScore(server.getOfflinePlayer(SkillUtils.getSkillName(skill))).setScore(profile.getSkillLevel(skill));
        }
    }

    private static void updateGlobalStatsScores(Objective objective, String skillName) {
        Server server = mcMMO.p.getServer();

        int i = 0;

        for (PlayerStat stat : LeaderboardManager.getPlayerStats(skillName)) {
            if (i > 14) {
                break;
            }

            objective.getScore(server.getOfflinePlayer(stat.name)).setScore(stat.statVal);
            i++;
        }

        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
    }

    private static Objective getGlobalObjective(String skillName) {
        return skillName.equalsIgnoreCase("all") ? globalPowerlevel : globalStatsScoreboard.getObjective(SkillUtils.getSkillName(SkillType.getSkill(skillName)));
    }
}
