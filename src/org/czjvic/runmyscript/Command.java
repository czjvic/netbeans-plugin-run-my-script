/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.czjvic.runmyscript;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.Scanner;
import org.openide.util.NbPreferences;
import org.openide.windows.IOProvider;
import org.openide.windows.InputOutput;
import org.openide.windows.OutputWriter;

/**
 *
 * @author josefvrba
 */
public class Command implements Runnable {

    String cmd;

    public Command(String cmd) {
        this.cmd = cmd;
    }

    @Override
    public void run() {

        //close previous window
        InputOutput outputWindow = IOProvider.getDefault().getIO("Run My Script", false);
        outputWindow.closeInputOutput();

        //create new output window
        outputWindow = IOProvider.getDefault().getIO("Run My Script", true);
        outputWindow.getOut().println("Starting command: " + this.cmd);

        //show window if enabled in preferences
        if (NbPreferences.forModule(RunMyScriptPanel.class).getBoolean("showWindow", true)) {
            outputWindow.select();
        }

        //prepare enviroment variables
        String enviromentVariable = NbPreferences.forModule(RunMyScriptPanel.class).get("enviroment", "");
        String[] env = {enviromentVariable};

        try {
            Process process = Runtime.getRuntime().exec(this.cmd, env);
            inheritIO(process.getInputStream(), process.getErrorStream(), outputWindow.getOut());
        } catch (Exception ex) {
            ex.printStackTrace();
            outputWindow.getOut().println("Failed running command. Exception message: " + ex.getMessage());
        }
    }

    /**
     * Process output streams.
     *
     * @param outputStream
     * @param errorStream
     * @param dest
     *
     * @return void
     */
    private static void inheritIO(final InputStream outputStream, final InputStream errorStream, final OutputWriter dest) {
        new Thread(new Runnable() {
            public void run() {
                Scanner sc = new Scanner(outputStream);
                while (sc.hasNextLine()) {
                    dest.println(sc.nextLine());
                }

                sc = new Scanner(errorStream);
                while (sc.hasNextLine()) {
                    dest.println(sc.nextLine());
                }
                dest.println("Command successful finished.");
            }
        }).start();
    }

}
