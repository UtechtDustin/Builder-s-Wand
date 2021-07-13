package de.False.BuildersWand.enums;

import com.sk89q.worldguard.protection.flags.Flags;
import com.sk89q.worldguard.protection.flags.StateFlag;
import de.False.BuildersWand.helper.WorldGuardAPI;

public enum WGFlag {
    BUILD;

    private static byte version = WorldGuardAPI.getWorldGuardAPI().version;
    private StateFlag flag;
    private boolean supportsLegacy;
    WGFlag() { supportsLegacy = true; }
    WGFlag(boolean supportsLegacy) { this.supportsLegacy = supportsLegacy; }

    public  StateFlag getFlag() {
        if(flag != null) return flag;
        final String n = name();
        try {
            flag = (StateFlag) ((version == 6 ? com.sk89q.worldguard.protection.flags.DefaultFlag.class : Flags.class).getDeclaredField(n).get(null));
        } catch(Exception e) {
            System.out.println("[WGFlag] Unsupported Flag! WorldGuard version=" + version + ";Flag=" + n);
            e.printStackTrace();
        }
        return flag;
    }
}