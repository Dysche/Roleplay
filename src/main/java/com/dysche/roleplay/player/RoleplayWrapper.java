package com.dysche.roleplay.player;

import org.cantaloupe.permission.Allowable;
import org.cantaloupe.permission.group.Group;
import org.cantaloupe.player.Player;
import org.cantaloupe.player.PlayerWrapper;

import com.dysche.roleplay.economy.account.Account;
import com.dysche.roleplay.permission.groups.Adult;
import com.dysche.roleplay.permission.groups.Child;
import com.dysche.roleplay.permission.groups.Elder;
import com.dysche.roleplay.permission.groups.Teen;
import com.dysche.roleplay.permission.groups.YoungAdult;
import com.dysche.roleplay.screen.ScoreboardManager;

public class RoleplayWrapper extends PlayerWrapper {
    private Account account = null;

    public RoleplayWrapper(Player player) {
        super(player);
    }

    @Override
    public void onLoad() {
        for (Allowable allowable : Allowable.values()) {
            this.getPlayer().allow(allowable);
        }
    }

    public void updateBalance() {
        ScoreboardManager.updateBalance(this);

        PlayerHelper.savePlayer(this);
    }

    public void updateAgeGroup() {
        ScoreboardManager.updateAgeGroup(this);

        PlayerHelper.savePlayer(this);
    }

    public void setAgeGroup(Group group) {
        if (this.getAgeGroup() != null) {
            this.getPlayer().leaveGroup(this.getAgeGroup());
        }

        this.getPlayer().joinGroup(group);
    }

    public Group getAgeGroup() {
        for (Group group : this.getPlayer().getGroups()) {
            if (group instanceof Child || group instanceof Teen || group instanceof YoungAdult || group instanceof Adult || group instanceof Elder) {
                return group;
            }
        }

        return null;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public Account getAccount() {
        return this.account;
    }
}