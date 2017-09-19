package com.dysche.roleplay.screen;

import org.cantaloupe.scoreboard.Objective;
import org.cantaloupe.scoreboard.Objective.DisplaySlot;
import org.cantaloupe.scoreboard.Scoreboard;
import org.cantaloupe.scoreboard.entry.SpaceEntry;
import org.cantaloupe.scoreboard.entry.TextEntry;
import org.cantaloupe.text.Text;

import com.dysche.roleplay.player.RoleplayWrapper;

public class ScoreboardManager {
    public static Scoreboard createMainScoreboard(RoleplayWrapper wrapper) {
        Scoreboard scoreboard = Scoreboard.of();
        Objective objective = scoreboard.createObjective("side", "dummy");
        objective.setSlot(DisplaySlot.SIDEBAR);
        objective.setTitle(Text.fromLegacy("      &lNAME     "));
        objective.addEntry(0, TextEntry.of(Text.fromLegacy("&m+------" + getDashes(wrapper) + "------+")));
        objective.addEntry(1, SpaceEntry.of());
        objective.addEntry(2, TextEntry.of(Text.fromLegacy("&d&lWelcome")));
        objective.addEntry(3, TextEntry.of(Text.of(wrapper.getPlayer().getName())));
        objective.addEntry(4, SpaceEntry.of());
        objective.addEntry(5, TextEntry.of(Text.fromLegacy("&d&lAge Group")));
        objective.addEntry(6, TextEntry.of(Text.of(wrapper.getAgeGroup().getPrefix().toPlain().replace("-", " "))));
        objective.addEntry(7, SpaceEntry.of());
        objective.addEntry(8, TextEntry.of(Text.fromLegacy("&d&lBalance")));
        objective.addEntry(9, TextEntry.of(Text.of("$" + wrapper.getAccount().getBalance().getAmount())));
        objective.addEntry(10, SpaceEntry.of());
        objective.addEntry(11, TextEntry.of(Text.fromLegacy("&m+------" + getDashes(wrapper) + "------+&r ")));

        return scoreboard;
    }

    public static void updateBalance(RoleplayWrapper wrapper) {
        TextEntry entry = (TextEntry) wrapper.getPlayer().getCurrentScoreboard().getObjective("side").get().getEntry(9);
        entry.setText(Text.of("$" + wrapper.getAccount().getBalance().getAmount()));
    }

    public static void updateAgeGroup(RoleplayWrapper wrapper) {
        TextEntry entry = (TextEntry) wrapper.getPlayer().getCurrentScoreboard().getObjective("side").get().getEntry(6);
        entry.setText(Text.of(wrapper.getAgeGroup().getPrefix().toPlain().replace("-", " ")));
    }

    private static String getDashes(RoleplayWrapper wrapper) {
        String dashes = "";

        for (int i = 0; i < wrapper.getPlayer().getName().length() - 8; i++) {
            dashes += "-";
        }

        return dashes;
    }
}