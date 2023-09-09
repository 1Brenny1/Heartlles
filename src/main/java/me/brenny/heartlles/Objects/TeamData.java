package me.brenny.heartlles.Objects;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TeamData {
    public Integer Id = -1;
    public String Name;
    public UUID Owner;
    public Boolean PVP = true;
    public List<UUID> Members = new ArrayList<>();
    public List<UUID> Mods = new ArrayList<>();
    public List<UUID> Invites = new ArrayList<>();
}
