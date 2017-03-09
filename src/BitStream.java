/**
 * Created by HP User on 2/20/2017.
 */

import java.io.Closeable;
import java.io.IOException;

public abstract class BitStream implements Closeable
{
    static final int BITS_PER_BYTE = 8;
    static final int FULL_BYTE     = -1;

    ////////////////
    /* Properties */
    ////////////////

    private int currentByte;
    private int currentBitIndex;

    //////////////////
    /* Constructor */
    //////////////////

    protected BitStream(int startingBitIndex)
    {
        this.setCurrentBitIndex(startingBitIndex);
    }

    //////////////////////////
    /* Accessors & Mutators */
    //////////////////////////

    protected int getCurrentByte()
    { return this.currentByte; }

    protected void setCurrentByte(int currentByte)
    { this.currentByte = currentByte; }

    protected int getCurrentBitIndex()
    { return this.currentBitIndex; }

    protected void setCurrentBitIndex(int bitIndex)
    { this.currentBitIndex = bitIndex; }

    ////////////////////
    /* Helper Methods */
    ////////////////////

    protected final void setAsExhausted()
    {
        setCurrentBitIndex(FULL_BYTE);
    }

    protected final void resetCurrentBitIndex()
    {
        if (!exhausted())
        {
            setCurrentBitIndex(0);
        }
    }

    protected final void setNewCurrentByte(int currentByte)
    {
        setCurrentByte(currentByte);
        resetCurrentBitIndex();
    }

    protected final int getCurrentBit()
    {
        int currentBitIndex = getCurrentBitIndex();
        return exhausted() ? -1 : (getCurrentByte() >> currentBitIndex) & 1;
    }

    protected final void setCurrentBit(int bit)
    {
        setCurrentByte(getCurrentByte() | ((bit & 1) << getCurrentBitIndex()));
    }

    protected final void advanceBitIndex() throws IOException
    {
        if (exhausted())
        {
            return;
        }

        int newBitIndex = getCurrentBitIndex() + 1;
        if ((newBitIndex & 0x7) == newBitIndex)
        {
            setCurrentBitIndex(newBitIndex);
        }
        else
        {
            handleByteConsumed(getCurrentByte());
        }
    }

    ////////////////////
    /* Public Methods */
    ////////////////////

    public final boolean exhausted()
    {
        return this.getCurrentBitIndex() == FULL_BYTE;
    }

    //////////////////////
    /* Abstract Methods */
    //////////////////////

    protected abstract void handleByteConsumed(int byteConsumed) throws IOException;

    public abstract void close() throws IOException;
}
