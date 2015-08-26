package com.settlements.controllers;

import com.settlements.models.*;
import com.settlements.models.Error;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class SettlementController
{

    private ColumnController columnController;
    private Map<String, Settlement> settlements = new HashMap<String, Settlement>();

    public SettlementController(ColumnController columnController)
    {
        this.columnController = columnController;
    }

    /**
     * Calculate the settlement type of the settlement.
     *
     * @param settlement the settlement to be calculated
     * @return the type of the settlement
     */
    public SettlementType calcType(Settlement settlement)
    {

        for (SettlementType type : Arrays.asList(SettlementType.values()))
            if (type.getSizeCo() >= settlement.getSize())
                return type;

        return SettlementType.EMPIRE;
    }

    /**
     * Check if the column can be added to the settlement and adds it if so.
     *
     * @param settlement the settlement to add the column to
     * @param column     the column to be added
     * @return returns true if successful
     */
    public Error claimColumn(Settlement settlement, Column column)
    {

        //
        if (!columnController.isAdjacent(column, settlement.getLand()))
            return Error.BLOCK_IS_NOT_ADJACENT;

        // If town has no column claiming blocks left return error.
        if (!(settlement.getLand().size() < settlement.getLandAllocation()))
            return Error.NO_REMAINING_BLOCKS;

        // If column is already claimed return error.
        for (Settlement oneOfAllSettlements : settlements.values())
            if (oneOfAllSettlements.getLand().contains(column))
                return Error.LAND_ALREADY_CLAIMED;

        return null;
    }

    /**
     * Checks that you aren't unclaiming the last column of the settlement or
     * claiming a block that would make another isolated before removing the
     * column from the settlement.
     *
     * @param settlement the settlement you are removing the column from
     * @param column     the column you are removing from the settlement
     * @return the error code
     */
    public Error unclaimColumn(Settlement settlement, Column column)
    {

        boolean isLast = true;

        for (Column adj : columnController.getAdjacent(column))
        {

            if (settlement.getLand().contains(adj))
            {

                boolean hasLandAdj = false;

                for (Column adjOfAdj : columnController.getAdjacent(adj))
                {

                    if (adjOfAdj.equals(adj))
                    {
                        continue;

                    }
                    else if (settlement.getLand().contains(adjOfAdj))
                    {

                        hasLandAdj = true;
                    }
                }

                isLast = false;

                if (!hasLandAdj)
                    return Error.WILL_ISOLATE_BLOCK;
            }
        }

        if (isLast)
            return Error.LAST_BLOCK;

        if (settlement.removeColumn(column))
            return null;

        else
            return Error.BLOCK_ISNT_CLAIMED;
    }

    /**
     * Adds an inhabitant to the settlement
     *
     * @param settlement the settlement the inhabitant is to be added to
     * @param earthian   the prospective inhabitant
     * @return true if successful
     */
    public Error addInhabitant(Settlement settlement, Earthian earthian)
    {

        if (!settlement.getInhabitants().contains(earthian))
        {

            settlement.getInhabitants().addEarthian(earthian);
            settlement.setType(calcType(settlement));
            return null;

        }
        else
        {

            return Error.PLAYER_ALREADY_IN_SETTLEMENT;
        }

    }

    public Error removeInhabitant(Settlement settlement, Earthian earthian)
    {

        if (settlement.getInhabitants().contains(earthian))
        {

            settlement.getInhabitants().removeEarthian(earthian);
            settlement.setType(calcType(settlement));
            return null;

        }
        else
        {

            return Error.PLAYER_NOT_IN_SETTLEMENT;
        }
    }

    /**
     * Creates a settlement.
     *
     * @param name   the name of the new settlement
     * @param leader the leader of the new settlement
     * @param land   the starting land of the new settlement
     * @return the created settlement, returns null if a settlement with the
     * same name already exists
     */
    public Error createSettlement(String name, Earthian leader, Set<Column> land)
    {

        if (settlements.containsKey(name))
            return Error.SETTLEMENT_ALREADY_EXISTS;

        Settlement settlement = new Settlement(name, leader, land);

        settlements.put(name, settlement);

        return null;
    }

    /**
     * Removes a settlement.
     *
     * @param name the name of the settlement to be removed
     * @return if successful returns null else returns an error
     */
    public Error removeSettlement(String name)
    {

        if (settlements.remove(name) == null)
            return Error.SETTLEMENT_DOESNT_EXIST;

        return null;
    }

    /**
     * Creates a plot with the given set of columns
     *
     * @param settlement the settlement to create the plot in
     * @param plot the plot to create
     * @return error if unsuccessful null otherwise
     */
    public Error createPlot(Settlement settlement, Set<Column> plot) {

        if(!settlement.getLand().containsAll(plot)) return Error.BLOCK_ISNT_CLAIMED;

        for(Set<Column> forSalePlots : settlement.getForSaleLand().keySet())
        {
            for(Column forSaleColumn : forSalePlots)
            {
                if (plot.contains(forSaleColumn))
                {
                    return Error.PLOT_OVERLAP;
                }
            }
        }

        for(Set<Column> plots : settlement.getPlots().keySet())
        {
            for(Column column : plots)
            {
                if (plot.contains(column))
                {
                    return Error.PLOT_OVERLAP;
                }
            }
        }

        settlement.getForSaleLand().put(plot, -1d);

        return null;
    }

    /**
     * Removes a plot with the given set of columns
     *
     * @param settlement the settlement to remove the plot from
     * @param plot the plot to remove
     * @return error if unsuccessful null otherwise
     */
    public Error removePlot(Settlement settlement, Set<Column> plot)
    {
        if(settlement.getPlots().remove(plot) != null) return null;

        if(settlement.getForSaleLand().remove(plot) != null) return null;

        return Error.NO_SUCH_PLOT;
    }
}
