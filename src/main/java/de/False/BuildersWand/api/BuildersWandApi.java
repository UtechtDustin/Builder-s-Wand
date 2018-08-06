package de.False.BuildersWand.api;

import de.False.BuildersWand.events.WandEvents;

public class BuildersWandApi {
    public static void addcanBuildHandler(canBuildHandler canBuildHandler){
        WandEvents.canBuildHandlers.add(canBuildHandler);
    }
}
