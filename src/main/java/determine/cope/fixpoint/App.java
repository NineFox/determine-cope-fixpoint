package determine.cope.fixpoint;

import determine.cope.fixpoint.app.MainCommand;
import picocli.CommandLine;

public class App {

    public static void main(String[] args)
            throws IllegalArgumentException {

        var command = new MainCommand();
        var exitCode = new CommandLine(command).execute(args);
        System.exit(exitCode);
    }
}
