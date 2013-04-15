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
import com.gmail.nossr50.datatypes.player.McMMOPlayer;
import com.gmail.nossr50.datatypes.player.PlayerProfile;
import com.gmail.nossr50.datatypes.skills.SkillType;
import com.gmail.nossr50.util.player.UserManager;
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

        globalStatsScoreboard.registerNewObjective(SkillUtils.getSkillName(SkillType.ACROBATICS), PLAYER_STATS_CRITERIA);
        globalStatsScoreboard.registerNewObjective(SkillUtils.getSkillName(SkillType.ARCHERY), PLAYER_STATS_CRITERIA);
        globalStatsScoreboard.registerNewObjective(SkillUtils.getSkillName(SkillType.AXES), PLAYER_STATS_CRITERIA);
        globalStatsScoreboard.registerNewObjective(SkillUtils.getSkillName(SkillType.EXCAVATION), PLAYER_STATS_CRITERIA);
        globalStatsScoreboard.registerNewObjective(SkillUtils.getSkillName(SkillType.FISHING), PLAYER_STATS_CRITERIA);
        globalStatsScoreboard.registerNewObjective(SkillUtils.getSkillName(SkillType.HERBALISM), PLAYER_STATS_CRITERIA);
        globalStatsScoreboard.registerNewObjective(SkillUtils.getSkillName(SkillType.MINING), PLAYER_STATS_CRITERIA);
        globalStatsScoreboard.registerNewObjective(SkillUtils.getSkillName(SkillType.REPAIR), PLAYER_STATS_CRITERIA);
        globalStatsScoreboard.registerNewObjective(SkillUtils.getSkillName(SkillType.SMELTING), PLAYER_STATS_CRITERIA);
        globalStatsScoreboard.registerNewObjective(SkillUtils.getSkillName(SkillType.SWORDS), PLAYER_STATS_CRITERIA);
        globalStatsScoreboard.registerNewObjective(SkillUtils.getSkillName(SkillType.TAMING), PLAYER_STATS_CRITERIA);
        globalStatsScoreboard.registerNewObjective(SkillUtils.getSkillName(SkillType.UNARMED), PLAYER_STATS_CRITERIA);
        globalStatsScoreboard.registerNewObjective(SkillUtils.getSkillName(SkillType.WOODCUTTING), PLAYER_STATS_CRITERIA);

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
        if (player.getScoreboard().getObjective(DisplaySlot.SIDEBAR) == globalStatsScoreboard.getObjective(DisplaySlot.SIDEBAR)) {
            return;
        }

        if (skillName.equalsIgnoreCase("ALL")) {
            updateGlobalStatsScores();
        }
        else {
            updateGlobalStatsScores(skillName);
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

    private static void updateGlobalStatsScores(String skillName) {
        SkillType skill = SkillType.getSkill(skillName);
        Objective objective = globalStatsScoreboard.getObjective(skillName);

        for (McMMOPlayer mcMMOPlayer : UserManager.getPlayers().values()) {
            objective.getScore(mcMMOPlayer.getPlayer()).setScore(mcMMOPlayer.getProfile().getSkillLevel(skill));
        }

        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
    }

    private static void updateGlobalStatsScores() {
        for (McMMOPlayer mcMMOPlayer : UserManager.getPlayers().values()) {
            globalPowerlevel.getScore(mcMMOPlayer.getPlayer()).setScore(mcMMOPlayer.getPowerLevel());
        }

        globalPowerlevel.setDisplaySlot(DisplaySlot.SIDEBAR);
    }
}
