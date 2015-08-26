package com.settlements.models;

import java.util.HashSet;
import java.util.Set;

public class EarthianGroup
{

    public static final int MAXLEADERS = 1;

    private Set<Earthian> leaders = new HashSet<>(1);
    private Set<Earthian> earthians = new HashSet<>();

    public EarthianGroup(Earthian leader, Set<Earthian> earthians)
    {
        this.leaders.add(leader);
        this.earthians = earthians;
    }

    public EarthianGroup(Set<Earthian> leaders, Set<Earthian> earthians)
    {
        this.leaders = leaders;
        this.earthians = earthians;
    }

    public EarthianGroup(Set<Earthian> leaders)
    {
        this.leaders = leaders;
    }

    public EarthianGroup(Earthian leader)
    {
        this.leaders.add(leader);
    }

    public boolean addEarthian(Earthian earthian)
    {
        return earthians.add(earthian);
    }

    public boolean removeEarthian(Earthian earthian)
    {
        return earthians.remove(earthian);
    }

    public boolean addLeader(Earthian leader)
    {
        if (leaders.size() >= MAXLEADERS)
            return false;

        earthians.add(leader);
        return true;
    }

    public int getSize()
    {
        return leaders.size() + earthians.size();
    }

    public boolean contains(Earthian earthian)
    {
        return leaders.contains(earthian) || earthians.contains(earthian);
    }

    @Override
    public boolean equals(Object obj)
    {
        if(obj == null) return false;

        if(!(obj instanceof EarthianGroup)) return false;

        EarthianGroup earthianGroup = (EarthianGroup) obj;

        return earthianGroup.leaders.equals(leaders)
                && earthianGroup.earthians.equals(earthians);
    }

    @Override
    public int hashCode()
    {
        return 78 * leaders.hashCode() * earthians.hashCode();
    }

    @Override
    public String toString()
    {
        return String.format("Leaders:%s, Earthians:%s",
                leaders.toString(), earthians.toString());
    }
}
