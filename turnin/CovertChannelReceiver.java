import java.io.Closeable;
import java.io.IOException;
import java.io.OutputStream;
import java.util.function.IntSupplier;

public class CovertChannelReceiver implements Closeable
{
    ////////////////
    /* Properties */
    ////////////////

    private OutputBitStream outputBitStream;
    private String          subjectName, objectName;
    private ReferenceMonitor referenceMonitor;

    /////////////////
    /* Constructor */
    /////////////////

    CovertChannelReceiver(ReferenceMonitor referenceMonitor,
                          String subjectName,
                          String objectName,
                          OutputStream outputStream)
    {
        this.setSubjectName(subjectName);
        this.setObjectName(objectName);
        this.setReferenceMonitor(referenceMonitor);
        this.setOutputBitStream(new OutputBitStream(outputStream));
    }

    //////////////////////////
    /* Accessors & Mutators */
    //////////////////////////

    private OutputBitStream getOutputBitStream()
    { return this.outputBitStream; }

    private void setOutputBitStream(OutputBitStream outputBitStream)
    { this.outputBitStream = outputBitStream; }

    private String getSubjectName()
    { return this.subjectName; }

    private void setSubjectName(String subjectName)
    { this.subjectName = subjectName; }

    private String getObjectName()
    { return this.objectName; }

    private void setObjectName(String objectName)
    { this.objectName = objectName; }

    private ReferenceMonitor getReferenceMonitor()
    { return this.referenceMonitor; }

    private void setReferenceMonitor(ReferenceMonitor referenceMonitor)
    { this.referenceMonitor = referenceMonitor; }

    ////////////////////
    /* Public Methods */
    ////////////////////

    /**
     * Receives a bit from the other end of the covert channel by polling the system's
     * ReferenceMonitor. Uses the given IntSupplier to check the result of the ReferenceMonitor
     * instructions used for the poll. (The IntSupplier is how the CovertChannelReceiver checks
     * the value read into the subject - it is effectively an alias for SecureSubject.getValue().)
     * @param intSupplier
     * @throws IOException
     */
    public void receiveBit(IntSupplier intSupplier) throws IOException
    {
        ReferenceMonitor referenceMonitor = getReferenceMonitor();
        String           subjectName      = getSubjectName();
        String           objectName       = getObjectName();

        referenceMonitor.execute(new Instruction(Instruction.Command.CREATE,
                                                 subjectName,
                                                 objectName,
                                                 null));
        referenceMonitor.execute(new Instruction(Instruction.Command.WRITE,
                                                 subjectName,
                                                 objectName,
                                                 1));
        referenceMonitor.execute(new Instruction(Instruction.Command.READ,
                                                 subjectName,
                                                 objectName,
                                                 null));
        referenceMonitor.execute(new Instruction(Instruction.Command.DESTROY,
                                                 subjectName,
                                                 objectName,
                                                 null));

        getOutputBitStream().write(intSupplier.getAsInt() == 0 ? 1 : 0);
    }

    ///////////////
    /* Closeable */
    ///////////////

    public void close() throws IOException
    {
        OutputBitStream outputBitStream = getOutputBitStream();
        if (outputBitStream != null)
        {
            outputBitStream.close();
        }
    }
}