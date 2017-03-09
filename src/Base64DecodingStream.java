import com.sun.istack.internal.NotNull;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Base64;

/**
 * Created by HP User on 3/7/2017.
 */
public class Base64DecodingStream extends OutputStream
{
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

    public Base64DecodingStream(InputStream inputStream)
    {
        this.inputStream = inputStream;
        this.setRawDataChunk(new byte[Base64EncodingStream.ENCODED_CHUNK_SIZE]);
        this.setProcessedDataChunk(new byte[Base64EncodingStream.UNENCODED_CHUNK_SIZE]);
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

    private byte[] writeNextRawDataChunk() throws IOException
    {
        throw new NotImplementedException();
    }

    protected byte[] processRawDataChunk(byte[] rawDataChunk) throws IOException
    {
        return (rawDataChunk == null || !(rawDataChunk.length > 0))
               ? null
               : Base64.getDecoder().decode(rawDataChunk);
    }

    ////////////////////
    /* Public Methods */
    ////////////////////

    @Override
    public void write(byte[] src) throws IOException
    {
        throw new NotImplementedException();
    }

    @Override
    public void write(int b) throws IOException
    {
        throw new NotImplementedException();
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
