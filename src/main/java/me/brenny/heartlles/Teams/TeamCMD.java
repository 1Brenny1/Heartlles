package me.brenny.heartlles.Teams;

import jdk.jshell.execution.Util;
import me.brenny.heartlles.Heartlles;
import me.brenny.heartlles.Objects.TeamData;
import me.brenny.heartlles.SQL;
import me.brenny.heartlles.Utils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

public class TeamCMD implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (commandSender instanceof Player Plr) {
            if (strings.length == 0) {
                Plr.sendMessage(Heartlles.Prefix + Utils.Color("&cMissing Arguments"));
                return true;
            }
            switch (strings[0].toLowerCase()) {
                case "create":
                    if (Heartlles.PlrData.get(((Player) commandSender).getUniqueId()).TeamId == -1) {
                        if (strings.length == 2) {
                            String Name = strings[1];
                            AtomicReference<Boolean> Taken = new AtomicReference<>(false);
                            Heartlles.TData.forEach((Id, Team) -> {
                                if (Team.Name.equalsIgnoreCase(Name)) Taken.set(true);
                            });
                            if (Taken.get()) {
                                Plr.sendMessage(Heartlles.Prefix + Utils.Color("&cTeam all ready exists with the name " + Name));
                            } else {
                                TeamData Team = new TeamData();
                                Team.Name = Name;
                                Team.Owner = Plr.getUniqueId();
                                Team.Members.add(Plr.getUniqueId());
                                Team.Id = Heartlles.TData.size();
                                Heartlles.TData.put(Team.Id, Team);
                                Heartlles.PlrData.get(Plr.getUniqueId()).TeamId = Team.Id;
                                Plr.sendMessage(Heartlles.Prefix + Utils.Color("Created team #"+Team.Id+" named &a") + Name);
                            }
                        } else {
                            Plr.sendMessage(Heartlles.Prefix + Utils.Color("&cMissing Team Name"));
                        }
                    } else {
                        Plr.sendMessage(Heartlles.Prefix + Utils.Color("&cYou are all ready in a team"));
                    }
                    break;
                case "disband":
                    if (Heartlles.PlrData.get(((Player) commandSender).getUniqueId()).TeamId != -1) {
                        TeamData Team = Heartlles.TData.get(Heartlles.PlrData.get(((Player) commandSender).getUniqueId()).TeamId);
                        if (!Team.Owner.equals(Plr.getUniqueId())) {
                            Plr.sendMessage(Heartlles.Prefix + Utils.Color("&cTeam owner only"));
                            return true;
                        }
                        Team.Members.forEach((Member) -> {
                            OfflinePlayer Target = Bukkit.getOfflinePlayer(Member);
                            if (Target.isOnline()) {
                                Heartlles.PlrData.get(Target.getUniqueId()).TeamId = -1;
                                ((Player) Target).sendMessage(Heartlles.Prefix + Utils.Color("&cYour team has been disbanded"));
                            } else {
                                SQL.LoadData((Player) Target);
                                Heartlles.PlrData.get(Target.getUniqueId()).TeamId = -1;
                                SQL.SaveData((Player) Target);
                                Heartlles.PlrData.remove(Target.getUniqueId());
                            }
                        });
                        SQL.ExSQL("DELETE FROM Teams WHERE Id=" + Team.Id);
                        Heartlles.TData.remove(Team.Id);
                        Plr.sendMessage(Heartlles.Prefix + Utils.Color("Team has been disbanded"));
                    }
                    break;
                case "leave":
                    if (Heartlles.PlrData.get(((Player) commandSender).getUniqueId()).TeamId != -1) {
                        TeamData Team = Heartlles.TData.get(Heartlles.PlrData.get(((Player) commandSender).getUniqueId()).TeamId);
                        if (Team.Owner.equals(Plr.getUniqueId())) {
                            Plr.sendMessage(Heartlles.Prefix + Utils.Color("&cThe team owner cannot leave, but needs to disband instead"));
                            return true;
                        }
                        Team.Members.remove(Plr.getUniqueId());
                        Team.Members.forEach((Mem) -> {
                            OfflinePlayer Member = Bukkit.getOfflinePlayer(Mem);
                            if (Member.isOnline()) ((Player)Member).sendMessage(Heartlles.Prefix + Utils.Color("&c" + Plr.getName() + " has left your team"));
                        });
                        Heartlles.PlrData.get(Plr.getUniqueId()).TeamId = -1;
                        Plr.sendMessage(Heartlles.Prefix + Utils.Color("You have left your team"));
                    }
                    break;
                case "invite":
                    if (Heartlles.PlrData.get(((Player) commandSender).getUniqueId()).TeamId != -1) {
                        TeamData Team = Heartlles.TData.get(Heartlles.PlrData.get(((Player) commandSender).getUniqueId()).TeamId);
                        if (!Team.Owner.equals(Plr.getUniqueId()) && !Team.Mods.contains(Plr.getUniqueId())) {
                            Plr.sendMessage(Heartlles.Prefix + Utils.Color("&cNo Permission"));
                            return true;
                        }
                        if (strings.length >= 2) {
                            Player Target = Bukkit.getPlayer(strings[1]);
                            if (Target == null) {
                                Plr.sendMessage(Heartlles.Prefix + Utils.Color("&cNo player online named " + strings[1]));
                                return true;
                            }
                            if (Team.Invites.size() >= 10) {
                                Plr.sendMessage(Heartlles.Prefix + Utils.Color("&cMax of 10 Invites have been sent"));
                                return true;
                            }
                            if (Heartlles.PlrData.get(Target.getUniqueId()).Invites.contains(Team.Id)) {
                                Plr.sendMessage(Heartlles.Prefix + Utils.Color("&c"+Target.getName()+" has all ready been invited"));
                                return true;
                            }
                            Heartlles.PlrData.get(Target.getUniqueId()).Invites.add(Team.Id);
                            Team.Invites.add(Target.getUniqueId());
                            Plr.sendMessage(Heartlles.Prefix + Utils.Color("Invited &a" + Target.getName()));
                        } else {
                            Plr.sendMessage(Heartlles.Prefix + Utils.Color("&cMissing player argument"));
                        }
                    }
                    break;
                case "join":
                    if (Heartlles.PlrData.get(((Player) commandSender).getUniqueId()).TeamId == -1) {
                        if (strings.length <= 1) {
                            Plr.sendMessage(Heartlles.Prefix + Utils.Color("&cMissing team name argument"));
                            return true;
                        }
                        TeamData team = null;
                        for (Integer TeamID : Heartlles.PlrData.get(((Player) commandSender).getUniqueId()).Invites) {
                            if (Heartlles.TData.get(TeamID).Name.equalsIgnoreCase(strings[1])) {
                                team = Heartlles.TData.get(TeamID);
                            }
                        }
                        if (team != null){
                            if (team.Members.size() >= 10) {
                                Plr.sendMessage(Heartlles.Prefix + Utils.Color("&cTeam has reached its limit of 10 players"));
                                return true;
                            }
                            team.Members.forEach((Member) -> {
                                OfflinePlayer Mem = Bukkit.getOfflinePlayer(Member);
                                if (Mem.isOnline()) {
                                    ((Player) Mem).sendMessage(Heartlles.Prefix + "&a" + Plr.getName() + " joined your team");
                                }
                            });
                            team.Invites.remove(Plr.getUniqueId());
                            Heartlles.PlrData.get(Plr.getUniqueId()).Invites.remove(team.Id);
                            team.Members.add(Plr.getUniqueId());
                            Heartlles.PlrData.get(Plr.getUniqueId()).TeamId = team.Id;
                            Plr.sendMessage(Heartlles.Prefix + Utils.Color("&aJoined team " + team.Name));
                        }else {
                            Plr.sendMessage(Heartlles.Prefix + Utils.Color("&cNo invites from team " + strings[1]));
                        }
                    } else {
                        Plr.sendMessage(Heartlles.Prefix + Utils.Color("&cYou are all ready in a team"));
                    }
                    break;
                case "uninvite":
                    if (Heartlles.PlrData.get(((Player) commandSender).getUniqueId()).TeamId != -1) {
                        TeamData Team = Heartlles.TData.get(Heartlles.PlrData.get(((Player) commandSender).getUniqueId()).TeamId);
                        if (!Team.Owner.equals(Plr.getUniqueId()) && !Team.Mods.contains(Plr.getUniqueId())) {
                            Plr.sendMessage(Heartlles.Prefix + Utils.Color("&cNo Permission"));
                            return true;
                        }
                        if (strings.length >= 2) {
                            OfflinePlayer Target = Bukkit.getOfflinePlayer(strings[1]);
                            if (Target == null) {
                                Plr.sendMessage(Heartlles.Prefix + Utils.Color("&cNo player named " + strings[1]));
                                return true;
                            }
                            if (Team.Invites.contains(Target.getUniqueId())) {
                                if (Target.isOnline()) {
                                    Heartlles.PlrData.get(Target.getUniqueId()).Invites.remove(Team.Id);
                                } else {
                                    SQL.LoadData((Player) Target);
                                    Heartlles.PlrData.get(Target.getUniqueId()).Invites.remove(Team.Id);
                                    SQL.SaveData((Player) Target);
                                    Heartlles.PlrData.remove(Target.getUniqueId());
                                }
                                Team.Invites.remove(Target.getUniqueId());
                                Plr.sendMessage(Heartlles.Prefix + Utils.Color("Uninvited " + Target.getName()));
                            } else {
                                Plr.sendMessage(Heartlles.Prefix + Utils.Color("&c" + strings[1] + " has not been invited to your team"));
                            }
                        } else {
                            Plr.sendMessage(Heartlles.Prefix + Utils.Color("&cMissing player argument"));
                        }
                    } else {
                        Plr.sendMessage(Heartlles.Prefix + Utils.Color("&cYou are not in a team"));
                    }
                    break;
                case "kick":
                    if (Heartlles.PlrData.get(((Player) commandSender).getUniqueId()).TeamId != -1) {
                        TeamData Team = Heartlles.TData.get(Heartlles.PlrData.get(((Player) commandSender).getUniqueId()).TeamId);
                        if (!Team.Owner.equals(Plr.getUniqueId()) && !Team.Mods.contains(Plr.getUniqueId())) {
                            Plr.sendMessage(Heartlles.Prefix + Utils.Color("&cNo Permission"));
                            return true;
                        }
                        if (strings.length >= 2) {
                            OfflinePlayer Target = Bukkit.getOfflinePlayer(strings[1]);
                            if (Target != null) {
                                if (Heartlles.PlrData.get(Target.getUniqueId()).TeamId != -1) {
                                    if (Team.Members.contains(Target.getUniqueId())) {
                                        if (Team.Owner == Target.getUniqueId()) {
                                            Plr.sendMessage(Heartlles.Prefix + Utils.Color("&cCannot kick the owner"));
                                            return true;
                                        }

                                        if (Team.Mods.contains(Target.getUniqueId())) {
                                            if (Team.Owner.equals(Plr.getUniqueId())) {
                                                Team.Members.remove(Target.getUniqueId());
                                                Team.Mods.remove(Target.getUniqueId());
                                            } else {
                                                Plr.sendMessage(Heartlles.Prefix + Utils.Color("&cNo Permission"));
                                                return true;
                                            }
                                        } else {
                                            Team.Members.remove(Target.getUniqueId());
                                        }
                                        Plr.sendMessage(Heartlles.Prefix + Utils.Color("Kicked " + Target.getName() + " from your team"));
                                        Team.Members.forEach((Mem) -> {
                                            if (Bukkit.getPlayer(Mem) != null) Bukkit.getPlayer(Mem).sendMessage(Heartlles.Prefix + Utils.Color("&c" + Target.getName() + " has been kicked from your team by " + Plr.getName()));
                                        });
                                        if (Target.isOnline()) {
                                            ((Player)Target).sendMessage(Heartlles.Prefix + Utils.Color("&cYou have been kicked from you team"));
                                        }
                                    } else {
                                        Plr.sendMessage(Heartlles.Prefix + Utils.Color("&c" + Target.getName() + " is not in your team"));
                                    }
                                } else {
                                    Plr.sendMessage(Heartlles.Prefix + Utils.Color("&c" + Target.getName() + " is not in a team"));
                                }
                            } else {
                                Plr.sendMessage(Heartlles.Prefix + Utils.Color("&cNo player with the name " + strings[1]));
                            }
                        }else{
                            Plr.sendMessage(Heartlles.Prefix + Utils.Color("&cMissing player argument"));
                        }
                    }else {
                        Plr.sendMessage(Heartlles.Prefix + Utils.Color("&cYou are not in a team"));
                    }
                    break;
                case "promote":
                    if (Heartlles.PlrData.get(((Player) commandSender).getUniqueId()).TeamId != -1) {
                        TeamData Team = Heartlles.TData.get(Heartlles.PlrData.get(((Player) commandSender).getUniqueId()).TeamId);
                        if (!Team.Owner.equals(Plr.getUniqueId())) {
                            Plr.sendMessage(Heartlles.Prefix + Utils.Color("&cNo Permission"));
                            return true;
                        }
                        if (strings.length >= 2) {
                            OfflinePlayer Target = Bukkit.getOfflinePlayer(strings[1]);
                            if (Team.Members.contains(Target.getUniqueId())) {
                                if (Team.Mods.contains(Target.getUniqueId())) {
                                    Plr.sendMessage(Heartlles.Prefix + Utils.Color("&c" + Target.getName() + " is all ready a mod"));
                                } else {
                                    Team.Mods.add(Target.getUniqueId());
                                    Plr.sendMessage(Heartlles.Prefix + Utils.Color(Target.getName() + " has been promoted to Mod"));
                                    if (Target.isOnline()) ((Player) Target).sendMessage(Heartlles.Prefix + "You have been promoted to Mod in your team");
                                }
                            } else {
                                Plr.sendMessage(Heartlles.Prefix + Utils.Color("&c" + Target.getName() + " is not in your team"));
                            }
                        } else{
                            Plr.sendMessage(Heartlles.Prefix + Utils.Color("&cMissing player argument"));
                        }
                    }else {
                        Plr.sendMessage(Heartlles.Prefix + Utils.Color("&cYou are not in a team"));
                    }
                    break;
                case "pvp":
                    if (Heartlles.PlrData.get(((Player) commandSender).getUniqueId()).TeamId != -1) {
                        TeamData Team = Heartlles.TData.get(Heartlles.PlrData.get(((Player) commandSender).getUniqueId()).TeamId);
                        if (!Team.Owner.equals(Plr.getUniqueId()) && !Team.Mods.contains(Plr.getUniqueId())) {
                            Plr.sendMessage(Heartlles.Prefix + Utils.Color("&cNo Permission"));
                            return true;
                        }
                        Team.PVP = !Team.PVP;
                        Plr.sendMessage(Heartlles.Prefix + Utils.Color("Pvp has been set to &b" + Team.PVP));
                    }
                    break;
                case "list":
                    Plr.sendMessage(Heartlles.Prefix + Utils.Color("All Teams: "));
                    Heartlles.TData.forEach((Id, Team) -> {
                        Plr.sendMessage(Utils.Color("&b" + Id + ") &r" + Team.Name));
                    });
                    break;
                case "info":
                    if (strings.length == 1) {
                        if (Heartlles.PlrData.get(((Player) commandSender).getUniqueId()).TeamId == -1) {
                            Plr.sendMessage(Heartlles.Prefix + Utils.Color("&cYou are not in a team"));
                            return true;
                        }
                        TeamData Team = Heartlles.TData.get(Heartlles.PlrData.get(((Player) commandSender).getUniqueId()).TeamId);
                        Plr.sendMessage(Heartlles.Prefix + Utils.Color("Info for team &a" + Team.Name));
                        Plr.sendMessage(Utils.Color("&bName: " + Team.Name));
                        Plr.sendMessage(Utils.Color("&bOwner: " + Bukkit.getOfflinePlayer(Team.Owner).getName()));
                        String Mods = "";
                        for (UUID Mod : Team.Mods) {
                            Mods += Bukkit.getOfflinePlayer(Mod).getName() + ", ";
                        }
                        Mods += " ";
                        Mods = Mods.replace(",  ", "");
                        Plr.sendMessage(Utils.Color("&bMods: " + Mods));
                        String Members = "";
                        for (UUID Member : Team.Members) {
                            Members += Bukkit.getOfflinePlayer(Member).getName() + ", ";
                        }
                        Members += "";
                        Members = Members.replace(",  ", "");
                        Plr.sendMessage(Utils.Color("&bMembers: " + Members));
                    } else {
                        TeamData Team = null;
                        for (TeamData _Team : Heartlles.TData.values()) {
                            if (_Team.Name.equalsIgnoreCase(strings[1])) {
                                Team = _Team;
                                break;
                            }
                        }
                        if (Team != null) {
                            Plr.sendMessage(Heartlles.Prefix + Utils.Color("Info for team &a" + Team.Name));
                            Plr.sendMessage(Utils.Color("&bName: " + Team.Name));
                            Plr.sendMessage(Utils.Color("&bOwner: " + Bukkit.getOfflinePlayer(Team.Owner).getName()));
                            String Mods = "";
                            for (UUID Mod : Team.Mods) {
                                Mods += Bukkit.getOfflinePlayer(Mod).getName() + ", ";
                            }
                            Mods += " ";
                            Mods = Mods.replace(",  ", "");
                            Plr.sendMessage(Utils.Color("&bMods: " + Mods));
                            String Members = "";
                            for (UUID Member : Team.Members) {
                                Members += Bukkit.getOfflinePlayer(Member).getName() + ", ";
                            }
                            Members += "";
                            Members = Members.replace(",  ", "");
                            Plr.sendMessage(Utils.Color("&bMembers: " + Members));
                        } else {
                            Plr.sendMessage(Heartlles.Prefix + Utils.Color("&cNo team with name " + strings[1]));
                        }
                    }
                    break;
            }
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {

        List<String> TabComplete = new ArrayList<>();
        switch (strings.length) {
            case 1:
                TabComplete.add("List");
                TabComplete.add("Info");
                if (Heartlles.PlrData.get(((Player) commandSender).getUniqueId()).TeamId == -1) {
                    TabComplete.add("Create");
                    TabComplete.add("Join");
                } else {
                    TabComplete.add("Invite");
                    TabComplete.add("PVP");
                    TabComplete.add("UnInvite");
                    TabComplete.add("Promote");
                    TabComplete.add("Leave");
                    TabComplete.add("Disband");
                    TabComplete.add("Kick");
                }
                break;
            case 2:
                if (strings[0].equalsIgnoreCase("Info")) {
                    Heartlles.TData.forEach((Id, Team) -> TabComplete.add(Team.Name));
                }

                if (Heartlles.PlrData.get(((Player) commandSender).getUniqueId()).TeamId == -1) {
                    if (strings[0].equalsIgnoreCase("Join")) {
                        List<Integer> Invites = Heartlles.PlrData.get(((Player) commandSender).getUniqueId()).Invites;
                        Invites.forEach((Team) -> TabComplete.add(Heartlles.TData.get(Team).Name));
                    }
                } else {
                    if (strings[0].equalsIgnoreCase("Invite")) {
                        Bukkit.getOnlinePlayers().forEach((Plr) -> TabComplete.add(Plr.getName()));
                    }
                    if (strings[0].equalsIgnoreCase("Kick") || strings[1].equalsIgnoreCase("Promote")) {
                        TeamData Team = Heartlles.TData.get(Heartlles.PlrData.get(((Player) commandSender).getUniqueId()).TeamId);
                        Team.Members.forEach((Id) -> TabComplete.add(Bukkit.getOfflinePlayer(Id).getName()));
                    }
                    if (strings[0].equalsIgnoreCase("UnInvite")) {
                        TeamData Team = Heartlles.TData.get(Heartlles.PlrData.get(((Player) commandSender).getUniqueId()).TeamId);
                        Team.Invites.forEach((Invite) -> {
                            OfflinePlayer Target = Bukkit.getOfflinePlayer(Invite);
                            TabComplete.add(Target.getName());
                        });
                    }
                }
        }
        return TabComplete;
    }
}
