package com.spraxs.oorlogsimulatie.utils.logger;

/**
 * Author: Spraxs.
 * Date: 1-3-2018.
 */

public class Debugger extends Logger {

    public Debugger() {
        super("[DEBUG]");
    }

    @Override
    public void log(String message) {

        boolean debugging = true;

        if (debugging) {
            System.out.println("[OorlogSimulatiePlugin] " + prefix + " " + message);
        }
    }
}
