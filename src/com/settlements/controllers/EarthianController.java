package com.settlements.controllers;

import com.settlements.models.Earthian;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Dan on 23-Aug-15.
 */
public class EarthianController
{
    private Map<Player, Earthian> earthians = new HashMap<Player, Earthian>();

    /**
     * Gets the player's associated earthian object
     *
     * @param player the player whose earthian object to get
     * @return the earthian object associated with the player
     */
    public Earthian getEarthian(Player player)
    {
        return earthians.get(player);
    }

    /**
     * Creates a new earthian object and
     * associates it with the given player
     *
     * @param player the player to associate
     *               the new earthian object with
     * @return the new earthian object
     */
    public Earthian newEarthian(Player player)
    {
        Earthian earthian = new Earthian(player);
        earthians.put(player, earthian);
        return earthian;
    }
}
