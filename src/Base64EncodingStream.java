import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;

/**
 * Created by HP User on 3/7/2017.
 */
public class Base64EncodingStream extends InputStream
{
    ///////////////////////
    /* Static Properties */
    ///////////////////////

    static final int ENCODED_CHUNK_SIZE;
    static final int UNENCODED_CHUNK_SIZE;

    /////////////////////////////////
    /* Static Property Initializer */
    /////////////////////////////////

    static
    {
        ENCODED_CHUNK_SIZE = 3;
        UNENCODED_CHUNK_SIZE = 4;
    }

    ////////////////
    /* Properties */
    ////////////////

    private final InputStream inputStream;
    private       byte[]      rawDataChunk;
    private       byte[]      processedDataChunk;
    private       int         currentByteIndex;

    //////////////////
    /* Constructors */
    //////////////////

    public Base64EncodingStream(InputStream inputStream)
    {
        this.inputStream = inputStream;
        this.setRawDataChunk(new byte[UNENCODED_CHUNK_SIZE]);
        this.setProcessedDataChunk(new byte[ENCODED_CHUNK_SIZE]);
    }

    /////////////////////////
    /* Accesors & Mutators */
    /////////////////////////

    private InputStream getInputStream()
    { return this.inputStream; }

    private byte[] getRawDataChunk()
    { return this.rawDataChunk; }

    private void setRawDataChunk(byte[] rawDataChunk)
    { this.rawDataChunk = rawDataChunk; }

    private byte[] getProcessedDataChunk()
    { return this.processedDataChunk; }

    private void setProcessedDataChunk(byte[] processedDataChunk)
    { this.processedDataChunk = processedDataChunk; }

    private int getCurrentByteIndex()
    { return this.currentByteIndex; }

    private void setCurrentByteIndex(int currentByteIndex)
    { this.currentByteIndex = currentByteIndex; }
    ////////////////////
    /* Helper Methods */
    ////////////////////

    private byte[] readNextRawDataChunk() throws IOException
    {
        byte buffer[] = this.getRawDataChunk();
        if (buffer == null) { return null; }

        int bytesRead = this.getInputStream().read(buffer);
        if (bytesRead <= 0)
        {
            buffer = null;
        }
        else if (bytesRead < buffer.length)
        {
            byte trimmedBuffer[] = new byte[bytesRead];
            System.arraycopy(buffer, 0, trimmedBuffer, 0, trimmedBuffer.length);
            buffer = trimmedBuffer;
        }

        return buffer;
    }

    protected byte[] processRawDataChunk(byte[] rawDataChunk) throws IOException
    {
        return (rawDataChunk == null || !(rawDataChunk.length > 0))
               ? null
               : Base64.getEncoder().encode(rawDataChunk);
    }

    ////////////////////
    /* Public Methods */
    ////////////////////

    @Override
    public int read(byte[] src) throws IOException
    {
        int count = 0;
        while (count < src.length && (src[count++] = (byte) this.read()) > 0) { ; }
        return (src.length > 0 && count == 0) ? -1 : count;
    }

    /* InputStream Implementation */
    public int read() throws IOException
    {
        byte processedDataChunk[] = this.getProcessedDataChunk();
        int  currentByteIndex     = this.getCurrentByteIndex();

        if (currentByteIndex < 0)
        {
            return currentByteIndex;
        }
        else if (currentByteIndex >= processedDataChunk.length)
        {
            byte rawDataChunk[] = readNextRawDataChunk();
            if (rawDataChunk == null)
            {
                setCurrentByteIndex(-1);
                setRawDataChunk(null);
                setProcessedDataChunk(null);
            }
            else
            {
                this.setProcessedDataChunk(processRawDataChunk(rawDataChunk));
                this.setRawDataChunk(rawDataChunk);
                this.setCurrentByteIndex(0);
            }

            processedDataChunk = this.getProcessedDataChunk();
            currentByteIndex = this.getCurrentByteIndex();
        }

        if (!(currentByteIndex >= 0 && currentByteIndex < processedDataChunk.length))
        {
            return -1;
        }

        byte returnByte = processedDataChunk[currentByteIndex];
        this.setCurrentByteIndex(currentByteIndex + 1);
        return returnByte;
    }

    //////////////////////////////
    /* Closeable Implementation */
    //////////////////////////////

    @Override
    public void close() throws IOException
    {
        this.getInputStream().close();
    }
}
