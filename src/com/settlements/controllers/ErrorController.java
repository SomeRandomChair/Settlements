package com.settlements.controllers;

import com.settlements.models.Earthian;
import com.settlements.models.Error;

public class ErrorController
{

    public void sendErrorMessage(Error error, Earthian recipient)
    {

        recipient.getPlayer().sendMessage("Error: " + error.getMessage());
    }
}
