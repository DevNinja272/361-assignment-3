import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;

public class CovertChannelSender implements Closeable
{
    ////////////////
    /* Properties */
    ////////////////

    private ReferenceMonitor referenceMonitor;
    private InputBitStream   inputBitStream;
    private String           subjectName, objectName;

    /////////////////
    /* Constructor */
    /////////////////

    CovertChannelSender(ReferenceMonitor referenceMonitor,
                        String subjectName,
                        String objectName,
                        InputStream inputStream) throws IOException
    {
        this.setSubjectName(subjectName);
        this.setObjectName(objectName);
        this.setReferenceMonitor(referenceMonitor);
        this.setInputBitStream(new InputBitStream(inputStream));
    }

    //////////////////////////
    /* Accessors & Mutators */
    //////////////////////////

    private InputBitStream getInputBitStream()
    { return this.inputBitStream; }

    private void setInputBitStream(InputBitStream inputBitStream)
    { this.inputBitStream = inputBitStream; }

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
     * Signals a bit on the covert channel by either either ensuring an object does or does not
     * exist. A receiver will have to detect if that file exists. This allows high-permission
     * subjects to send information to low-permission subjects, a violation of BLP-style security
     * models.
     * @throws IOException
     */
    public void signalNextBit() throws IOException
    {
        Instruction instruction = new Instruction((getInputBitStream().next() == 0
                                                   ? Instruction.Command.DESTROY
                                                   : Instruction.Command.CREATE),
                                                  getSubjectName(),
                                                  getObjectName(),
                                                  null);
        getReferenceMonitor().execute(instruction);
    }

    public boolean exhausted()
    {
        return getInputBitStream().exhausted();
    }

    ///////////////
    /* Closeable */
    ///////////////

    public void close() throws IOException
    {
        InputBitStream inputBitStream = getInputBitStream();
        if (inputBitStream != null)
        {
            inputBitStream.close();
        }
    }
}

