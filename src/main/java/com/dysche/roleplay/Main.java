package com.dysche.roleplay;

import org.cantaloupe.Cantaloupe;
import org.cantaloupe.audio.AudioServer;
import org.cantaloupe.audio.sound.Sound;
import org.cantaloupe.audio.source.SourceSettings;
import org.cantaloupe.audio.source.SourceSettings.AttenuationType;
import org.cantaloupe.audio.sources.PositionedSource;
import org.cantaloupe.permission.group.GroupManager;
import org.cantaloupe.player.PlayerManager;
import org.cantaloupe.plugin.CantaloupePlugin;
import org.cantaloupe.service.services.MongoService;
import org.cantaloupe.text.Text;
import org.cantaloupe.world.World;
import org.cantaloupe.world.location.ImmutableLocation;
import org.joml.Vector3d;

import com.dysche.roleplay.commands.CommandAgeGroup;
import com.dysche.roleplay.economy.Economy;
import com.dysche.roleplay.event.EventListener;
import com.dysche.roleplay.permission.groups.Adult;
import com.dysche.roleplay.permission.groups.Child;
import com.dysche.roleplay.permission.groups.Elder;
import com.dysche.roleplay.permission.groups.Teen;
import com.dysche.roleplay.permission.groups.YoungAdult;
import com.dysche.roleplay.player.PlayerHelper;
import com.dysche.roleplay.player.RoleplayWrapper;
import com.dysche.roleplay.screen.ScoreboardManager;
import com.dysche.roleplay.world.time.Time;

public class Main extends CantaloupePlugin {
    private static Main instance = null;
    private Economy     economy  = null;

    @Override
    public void onPreInit() {
        instance = this;
        this.economy = new Economy();

        this.setup();
        this.setupAudioServer();

        Cantaloupe.getServiceManager().provide(MongoService.class).connect("main");
        Cantaloupe.getPlayerManager().registerWrapper(RoleplayWrapper.class);
        Cantaloupe.getPlayerManager().inject(PlayerManager.Scopes.LOAD, player -> {
            RoleplayWrapper wrapper = player.<RoleplayWrapper>getWrapper(RoleplayWrapper.class).get();
            wrapper.setAccount(this.economy.getAccountManager().loadAccount(player));

            if (PlayerHelper.isRegistered(wrapper)) {
                PlayerHelper.loadPlayer(wrapper);
            } else {
                PlayerHelper.registerPlayer(wrapper);
            }

            player.setScoreboard(ScoreboardManager.createMainScoreboard(wrapper));
        });

        Cantaloupe.getPlayerManager().inject(PlayerManager.Scopes.UNLOAD, player -> {
            PlayerHelper.savePlayer(player.<RoleplayWrapper>getWrapper(RoleplayWrapper.class).get());

            this.economy.getAccountManager().unloadAccount(player);
        });

        World world = Cantaloupe.getWorldManager().getWorld("city1");
        PositionedSource source = Cantaloupe.getAudioServer().get().getSourceManager().createPositionedSource("test", ImmutableLocation.of(world, new Vector3d(-142, 68, 105)), SourceSettings.of(true, true, 0, 25, AttenuationType.LINEAR));
        // source.setSound(Sound.of("ariana_grande:dangerous_woman", 235500));
        // source.setSound(Sound.of("faizar_reaper_gunships", 240500));
        source.setSound(Sound.of("syrin_rektor_light_on", "syrin_rektor_light_on.mp3", 210500));
    }

    @Override
    public void onInit() {
        Cantaloupe.registerListener(new EventListener());
    }

    @Override
    public void onDeinit() {
        Cantaloupe.stopAudioServer();
    }

    private void setup() {
        Time.startTask(Cantaloupe.getWorldManager().getWorld("city1"));

        this.registerGroups();
        this.registerCommands();
    }

    private void setupAudioServer() {
        AudioServer server = Cantaloupe.setupAudioServer(9001);

        server.inject(AudioServer.Scopes.SESSION_BEGIN, wrapper -> {
            wrapper.getPlayer().sendMessage(Text.of("Connection made."));
        });

        server.inject(AudioServer.Scopes.SESSION_END, wrapper -> {
            wrapper.getPlayer().sendMessage(Text.of("Connection closed."));
        });

        Cantaloupe.startAudioServer();
    }

    private void registerGroups() {
        GroupManager.registerGroup(new Child());
        GroupManager.registerGroup(new Teen());
        GroupManager.registerGroup(new YoungAdult());
        GroupManager.registerGroup(new Adult());
        GroupManager.registerGroup(new Elder());
    }

    private void registerCommands() {
        CommandAgeGroup.register(this);
    }

    public static Main getInstance() {
        return instance;
    }

    public Economy getEconomy() {
        return this.economy;
    }
}