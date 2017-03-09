import java.io.BufferedInputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by HP User on 2/22/2017.
 */
public class InputBitStream extends BitStream implements Closeable
{
    ////////////////
    /* Properties */
    ////////////////

    private final InputStream inputStream;
    private final byte[] byteBuffer;

    //////////////////
    /* Constructors */
    //////////////////

    public InputBitStream(InputStream inputStream) throws IOException
    {
        super(BITS_PER_BYTE);
        this.inputStream = new BufferedInputStream(inputStream);
        this.byteBuffer  = new byte[]{0};

        if (this.getInputStream() == null)
        {
            super.setAsExhausted();
        }

        this.handleByteConsumed((byte) 0);
    }

    //////////////////////////
    /* Accessors & Mutators */
    //////////////////////////

    private InputStream getInputStream()
    { return this.inputStream; }

    private byte[] getByteBuffer()
    { return this.byteBuffer; }

    ////////////////////
    /* Public Methods */
    ////////////////////

    public int next() throws IOException
    {
        int currentBit = super.getCurrentBit();
        super.advanceBitIndex();
        return currentBit;
    }

    //////////////////////////////
    /* BitStream Implementation */
    //////////////////////////////

    protected final void handleByteConsumed(int byteConsumed) throws IOException
    {
        byte[] byteBuffer = getByteBuffer();
        super.setCurrentBitIndex(getInputStream().read(byteBuffer));
        super.setNewCurrentByte(byteBuffer[0]);
    }

    //////////////////////////////////
    /* AutoCloseable Implementation */
    //////////////////////////////////

    public void close() throws IOException
    {
        InputStream inputStream = getInputStream();
        if (inputStream != null)
        {
            inputStream.close();
        }

        super.setAsExhausted();
    }
}
