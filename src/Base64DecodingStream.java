import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Base64;
import java.util.Arrays;

public class Base64DecodingStream extends OutputStream
{
    ////////////////
    /* Properties */
    ////////////////

    private final OutputStream outputStream;
    private final byte[]       rawDataChunk;
    private       int          currentByteIndex;

    //////////////////
    /* Constructors */
    //////////////////

    public Base64DecodingStream(OutputStream outputStream)
    {
        this.outputStream = outputStream;
        this.rawDataChunk = new byte[Base64EncodingStream.ENCODED_CHUNK_SIZE];

        if (this.getOutputStream() == null)
        {
            this.setCurrentByteIndex(-1);
        }
    }

    /////////////////////////
    /* Accesors & Mutators */
    /////////////////////////

    private OutputStream getOutputStream()
    { return this.outputStream; }

    private byte[] getRawDataChunk()
    { return this.rawDataChunk; }

    private int getCurrentByteIndex()
    { return this.currentByteIndex; }

    private void setCurrentByteIndex(int currentByteIndex)
    { this.currentByteIndex = currentByteIndex; }

    ////////////////////
    /* Helper Methods */
    ////////////////////

    private void decodeAndWrite(byte[] buffer) throws IOException
    {
        OutputStream outputStream = this.getOutputStream();
        outputStream.write(Base64.getDecoder().decode(buffer));
    }

    private void refreshRawDataChunk()
    {
        byte buffer[] = this.getRawDataChunk();   
        Arrays.fill(buffer, (byte) 0);
        this.setCurrentByteIndex(0);
    }

    ////////////////////
    /* Public Methods */
    ////////////////////

    @Override
    public void write(byte[] src) throws IOException
    {
        if (this.getCurrentByteIndex() < 0)
        {
            return;
        }
         
        int index = 0;
        while (index < src.length)
        {
            this.write(src[index++]);
        }
    }

    @Override
    public void write(int b) throws IOException
    {
        byte rawDataChunk[]   = this.getRawDataChunk();
        int  currentByteIndex = this.getCurrentByteIndex();

        if (currentByteIndex < 0)
        {
            return;
        }
        else if (currentByteIndex >= rawDataChunk.length)
        {
            this.decodeAndWrite(rawDataChunk);
            this.refreshRawDataChunk();
        }

        rawDataChunk[currentByteIndex] = (byte) b;
        this.setCurrentByteIndex(++currentByteIndex);
    }

    @Override
    public void flush() throws IOException
    {
        byte rawDataChunk[]  = this.getRawDataChunk();
        int currentByteIndex = this.getCurrentByteIndex();

        if (currentByteIndex > 0 && currentByteIndex <= rawDataChunk.length)
        {
            if (currentByteIndex < rawDataChunk.length)
            {
                byte writtenBytes[] = new byte[currentByteIndex];
                System.arraycopy(rawDataChunk, 0, writtenBytes, 0, writtenBytes.length);
                rawDataChunk = writtenBytes;
            }

            this.decodeAndWrite(rawDataChunk);
            this.refreshRawDataChunk();
            this.getOutputStream().flush();
        }
    }

    @Override
    public void close() throws IOException
    {
        this.setCurrentByteIndex(-1);
        this.flush();
        this.getOutputStream().close();
    }
}
