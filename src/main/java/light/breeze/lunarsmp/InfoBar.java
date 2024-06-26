package light.breeze.lunarsmp;

import light.breeze.lunarsmp.mana.ManaSystem;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.*;

public class InfoBar {
    public static String[] getScoreList( Player player ) {
        String[] lines = new String[ 10 ];
        lines[ 0 ] = PlaceholderAPI.setPlaceholders( player, ChatColor.DARK_BLUE + "   [ %betterteams_color%%betterteams_name%" + ChatColor.DARK_BLUE + " ] " );
        lines[ 1 ] = ChatColor.YELLOW + PlaceholderAPI.setPlaceholders( player, "     Lag: %math_0:1_100-(" + PlaceholderAPI.setPlaceholders( player, "%server_tps_15%" ).replace( "*", "" ) + "/20*100)%%" );
        lines[ 2 ] = "  ";
        lines[ 3 ] = ChatColor.LIGHT_PURPLE + "     Mana: " + ManaSystem.getMana( player ) + "/" + ManaSystem.getMaxMana( player );
        lines[ 4 ] = ChatColor.RED + PlaceholderAPI.setPlaceholders( player, "     Kills: %statistic_player_kills%" );
        lines[ 5 ] = ChatColor.DARK_RED + PlaceholderAPI.setPlaceholders( player, "     Deaths: %statistic_deaths%" );
        lines[ 6 ] = " ";
        lines[ 7 ] = ChatColor.AQUA + PlaceholderAPI.setPlaceholders( player, "     Time Played: %statistic_hours_played%h" );
        lines[ 8 ] = ChatColor.DARK_AQUA + PlaceholderAPI.setPlaceholders( player, "     Alive For: %statistic_minutes_since_death%m" );
        lines[ 9 ] = "";

        return lines;
    }

    public static void updateScoreboardText( String[] lines, Player player ) {
        Scoreboard scoreboard = player.getScoreboard();
        String team_name = PlaceholderAPI.setPlaceholders( player, "%betterteams_color%%betterteams_name%" );
        Team team = scoreboard.getTeam( team_name );
        if ( team == null ) {
            team = scoreboard.registerNewTeam( team_name );
        }

        team.addEntry( player.getName() );

        if ( scoreboard.getObjective( "scoreboard" ) != null ) {
            scoreboard.getObjective( "scoreboard" ).unregister();
            Objective objective = scoreboard.registerNewObjective( "scoreboard", "dummy" );
            objective.setDisplayName( ChatColor.LIGHT_PURPLE + "-=[ LunarSMP ]=-" );
            objective.setDisplaySlot( DisplaySlot.SIDEBAR );
            int i = 0;
            for ( String line : lines ) {
                i += 1;
                Score score = objective.getScore( line );
                score.setScore( lines.length - i );
            }
        }
    }

    public static void updatePlayerboard( Player player ) {
        if ( player.getScoreboard().getEntries().size() != 10 ) {
            //Utils.log( "making new" );
            player.setScoreboard( InfoBar.createScoreboard( player ) );
        }
        updateScoreboardText( getScoreList( player ), player );

        player.setPlayerListName( PlaceholderAPI.setPlaceholders( player, "%betterteams_color%[%betterteams_name%] " ) + player.getName() );
    }

    public static Scoreboard createScoreboard( Player player ) {
        Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        Objective objective = scoreboard.registerNewObjective( "scoreboard", "dummy" );

        updateScoreboardText( getScoreList( player ), player );
        player.setPlayerListHeader( ChatColor.LIGHT_PURPLE + "   [ LunarSMP ]   " );
        player.setPlayerListFooter( ChatColor.LIGHT_PURPLE + "   [          ]   " );

        return scoreboard;
    }

    public static class Updater extends BukkitRunnable {
        @Override
        public void run() {
            for ( Player player : Bukkit.getServer().getOnlinePlayers() ) {
                updatePlayerboard( player );
            }
        }
    }
}

