package edu.team06.boundary;

import edu.team06.control.PrimaryController;

public class Main {

    // calls the primary controller and runs it
    public static void main(String[] args)
    {
        PrimaryController game = new PrimaryController();
        game.run();

        return;
    }

}
