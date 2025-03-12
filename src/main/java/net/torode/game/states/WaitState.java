package net.torode.game.states;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.minestom.server.event.player.PlayerDisconnectEvent;
import net.minestom.server.event.player.PlayerSpawnEvent;
import net.minestom.server.network.packet.server.play.ScoreboardObjectivePacket;
import net.minestom.server.scoreboard.Sidebar;
import net.minestom.server.scoreboard.TabList;
import net.minestom.server.timer.Task;
import net.torode.GamePlayer;
import net.torode.game.AbstractGameState;
import net.torode.game.SquidGameManager;

import java.util.HashSet;
import java.util.Set;

import static net.kyori.adventure.text.format.NamedTextColor.GRAY;
import static net.kyori.adventure.text.format.NamedTextColor.WHITE;

public class WaitState implements AbstractGameState
{
    private static final String s_PLAYER_COUNT = "count";
    private static final String s_TIMER = "timer";

    private final Set<GamePlayer> players;
    private final Sidebar globalSidebar;
    private final TabList tab;
    private static final Component TAB_HEADER = MiniMessage.miniMessage().deserialize("<b><gradient:black:dark_purple>Squid Game</gradient></b>");
    private static final Component TAB_FOOTER = Component.text("localhost", GRAY);

    private static int blankId = 0;

    private static Sidebar.ScoreboardLine newBlankLine(int line)
    {
        return new Sidebar.ScoreboardLine(Integer.toString(blankId++), Component.empty(), line);
    }

    public WaitState()
    {
        globalSidebar = new Sidebar(TAB_HEADER);

        globalSidebar.createLine(newBlankLine(7));
        globalSidebar.createLine(new Sidebar.ScoreboardLine(s_PLAYER_COUNT, Component.empty(), 6));
        globalSidebar.createLine(newBlankLine(5));
        globalSidebar.createLine(new Sidebar.ScoreboardLine(s_TIMER, Component.text("Waiting...", WHITE), 4));
        globalSidebar.createLine(newBlankLine(3));
        globalSidebar.createLine(new Sidebar.ScoreboardLine("ip", TAB_FOOTER, 2));

        tab = new TabList(null, ScoreboardObjectivePacket.Type.INTEGER);

        players = new HashSet<>();
    }

    public Set<GamePlayer> getWaitingPlayers()
    {
        return players;
    }

    private int secondsUntilStart = 31;

    private Task tickTask;

    public void updateScoreboardTimer()
    {
        globalSidebar.updateLineContent(s_TIMER, Component.text("Starting in " + secondsUntilStart + " seconds", WHITE));
    }

    public void tickTimer()
    {
        --secondsUntilStart;

        if (secondsUntilStart == 0)
        {
            tickTask.cancel();
            SquidGameManager.start();
        }
    }

    private void updateScoreboardPlayerCount()
    {
        globalSidebar.updateLineContent(s_PLAYER_COUNT, Component.text("Players: " + players, WHITE));
    }

    public void addPlayer(GamePlayer player)
    {
        int count = players.size();

        if (count == SquidGameManager.MAX_PLAYERS)
        {

        }

        players.add(player);
        updateScoreboardPlayerCount();
    }

    public void removePlayer(GamePlayer player)
    {
        players.remove(player);
        updateScoreboardPlayerCount();
    }

    @Override
    public void spawnEvent(PlayerSpawnEvent event)
    {
        GamePlayer player = (GamePlayer) event.getPlayer();

        globalSidebar.addViewer(player);
        tab.addViewer(player);
        tab.sendPlayerListHeaderAndFooter(TAB_HEADER, TAB_FOOTER);

        addPlayer(player);
    }

    @Override
    public void disconnectEvent(PlayerDisconnectEvent event)
    {
        GamePlayer player = (GamePlayer) event.getPlayer();

        tab.removeViewer(player);
        globalSidebar.removeViewer(player);

        removePlayer(player);
    }
}
