package com.spraxs.oorlogsimulatie.utils.logger;

/**
 * Author: Spraxs.
 * Date: 1-3-2018.
 */

public class MemLeakHunter extends Logger {

    public MemLeakHunter() {
        super("Memory Leak Hunter ");
    }

    @Override
    public void log(String message) {

        boolean debugging = false;

        if (debugging) {
            System.out.println("[OorlogSimulatiePlugin] " + prefix + message);
        }
    }
}
