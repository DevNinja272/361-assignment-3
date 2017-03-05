/**
 * Created by HP User on 2/20/2017.
 */

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.function.Consumer;
import java.util.function.IntSupplier;

public class CovertChannel
{
    private static final String OptionsMessage = "Options are '[v] inputFilePath'";

    /**
     * The program entry point. Shows the use of a covert channel between two suggest sending
     * arbitrary files.
     * @param args
     */
    public static void main(String args[])
    {
        ValidateArguments(args);
        String path    = args.length == 1 ? args[0] : args[1];
        String logPath = "log.txt";

        /* Initialize system state */
        String outPath     = path + ".out";
        String subjectHigh = "HAL";
        String subjectLow  = "LYLE";
        String object      = "OBJ";

        /* Initialize covert channel */
        try  //@formatter:off
            (
                ReferenceMonitor referenceMonitor = new ReferenceMonitor();
                CovertChannelSender halSignal
                        = new CovertChannelSender(referenceMonitor,
                                                     subjectHigh,
                                                     object,
                                                     new FileInputStream(path));
                CovertChannelReceiver lyleSignal
                        = new CovertChannelReceiver(referenceMonitor,
                                                       subjectLow,
                                                       object,
                                                       new FileOutputStream(outPath))
            )//@formatter:on
        {
            /* Enable logging if verbose option set */
            if (args.length == 2 && args[0].equalsIgnoreCase("v"))
            {
                referenceMonitor.enableLogging(logPath);
            }

            /* Define sending subject's code */
            Consumer<IntSupplier> halsCode = valueReader ->
            {
                try { halSignal.signalNextBit(); } catch (IOException ex) {}
            };

            /* Define receiving subject's code */
            Consumer<IntSupplier> lylesCode = valueReader ->
            {
                try { lyleSignal.receiveBit(valueReader); } catch (IOException ex) {}
            };

            /* Define subjects */
            referenceMonitor.createSubject(subjectHigh, SecurityLevel.HIGH, halsCode);
            referenceMonitor.createSubject(subjectLow, SecurityLevel.LOW, lylesCode);

            /* For file-transfer timing
            long timeStart = System.nanoTime();
            */
            /* Simulate transfer */
            while (!halSignal.exhausted())
            {
                /* Send byte by byte */
                referenceMonitor.execute(new Instruction(Instruction.Command.RUN,
                                                         subjectHigh,
                                                         null,
                                                         null));
                referenceMonitor.execute(new Instruction(Instruction.Command.RUN,
                                                         subjectLow,
                                                         null,
                                                         null));
                referenceMonitor.execute(new Instruction(Instruction.Command.RUN,
                                                         subjectHigh,
                                                         null,
                                                         null));
                referenceMonitor.execute(new Instruction(Instruction.Command.RUN,
                                                         subjectLow,
                                                         null,
                                                         null));
                referenceMonitor.execute(new Instruction(Instruction.Command.RUN,
                                                         subjectHigh,
                                                         null,
                                                         null));
                referenceMonitor.execute(new Instruction(Instruction.Command.RUN,
                                                         subjectLow,
                                                         null,
                                                         null));
                referenceMonitor.execute(new Instruction(Instruction.Command.RUN,
                                                         subjectHigh,
                                                         null,
                                                         null));
                referenceMonitor.execute(new Instruction(Instruction.Command.RUN,
                                                         subjectLow,
                                                         null,
                                                         null));
            }

            /* For file-transfer timing
            double timeTaken = (System.nanoTime() - timeStart) / 1000000.0 + 1;
            long   fileSize  = new java.io.File(path).length();
            System.out.println(fileSize + "-byte transfer completed in " + timeTaken + " ms (" + (
                    fileSize * 8
                    / timeTaken) + " b/ms = " + (fileSize / timeTaken) + " kB/s)");
            */
        }
        catch (IOException ioe)
        {
            Fail("Error transferring file \"" + path + "\"\n" + ioe);
        }
    }

    /**
     * Validates the given argument list to be of the correct size and to have the correct content.
     *
     * @param args
     */
    private static void ValidateArguments(String[] args)
    {
        if (!(args.length == 1 || args.length == 2))
        {
            Fail("Invalid arguments: " + String.join(" ", args) + "\n" + OptionsMessage);
        }
        else if (args.length == 2 && !args[0].equalsIgnoreCase("v"))
        {
            Fail("Invalid arguments. " + OptionsMessage);
        }
    }

    /**
     * Causes the system to immediately exit the program with a failing error code after printing
     * the given message.
     * @param message
     */
    private static void Fail(String message)
    {
        System.out.println("Failing system execution: " + message);
        System.exit(-1);
    }
}   
