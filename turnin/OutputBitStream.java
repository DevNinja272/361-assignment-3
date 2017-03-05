import java.io.Closeable;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by HP User on 2/22/2017.
 */
public class OutputBitStream extends BitStream implements Closeable
{
    ////////////////
    /* Properties */
    ////////////////

    private final OutputStream outputStream;

    //////////////////
    /* Constructors */
    //////////////////

    public OutputBitStream(OutputStream outputStream)
    {
        super(0);
        this.outputStream = outputStream;

        if (getOutputStream() == null)
        {
            super.setAsExhausted();
        }
    }

    //////////////////////////
    /* Accessors & Mutators */
    //////////////////////////

    private OutputStream getOutputStream()
    { return this.outputStream; }

    ////////////////////
    /* Public Methods */
    ////////////////////

    public void write(int bit) throws IOException
    {
        if (!super.exhausted())
        {
            super.setCurrentBit(bit);
            super.advanceBitIndex();
        }
    }

    public void flush() throws IOException
    {
        if (!super.exhausted() && getCurrentBitIndex() > 0)
        {
            OutputStream outputStream = getOutputStream();
            outputStream.write(super.getCurrentByte());
            outputStream.flush();
        }
    }

    //////////////////////////////
    /* BitStream Implementation */
    //////////////////////////////

    protected final void handleByteConsumed(int byteConsumed) throws IOException
    {
        getOutputStream().write(byteConsumed);
        super.setNewCurrentByte(0);
    }

    //////////////////////////////////
    /* AutoCloseable Implementation */
    //////////////////////////////////

    public void close() throws IOException
    {
        OutputStream outputStream = getOutputStream();
        if (outputStream != null)
        {
            try
            {
                flush();
            }
            finally
            {
                outputStream.close();
            }
        }

        super.setAsExhausted();
    }
}
