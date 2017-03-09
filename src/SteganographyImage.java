import java.awt.image.BufferedImage;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Created by HP User on 3/5/2017.
 */
public abstract class SteganographyImage implements Closeable, Iterable<Integer>
{
    public class BufferedImageIterator implements Iterator<Integer>
    {
        private int x, y;

        public Integer next() throws NoSuchElementException
        {
            if (!hasNext())
            {
                throw new NoSuchElementException();
            }

            int pixel = SteganographyImage.this.bufferedImage.getRGB(x, y);

            if (x >= SteganographyImage.this.bufferedImage.getWidth())
            {
                x = 0;
                y++;
            }
            else
            {
                x++;
            }

            return pixel;
        }

        public boolean hasNext()
        {
            return y > SteganographyImage.this.bufferedImage.getHeight()
                   && x > SteganographyImage.this.bufferedImage.getWidth();
        }
    }

    private static final int END = 0;
    protected final BufferedImage bufferedImage;
    protected final BitStream     bitStream;

    protected SteganographyImage(BufferedImage bufferedImage, BitStream bitStream)
            throws IOException
    {
        this.bufferedImage = bufferedImage;
        this.bitStream = bitStream;
    }

    public int[] decodePixel(int pixel)
    {
        return new int[]{(pixel >> 16) & 1, (pixel >> 8) & 1, pixel & 1};
    }

    public int encodePixel(int pixel, int firstBit, int secondBit, int thirdBit)
    {
        return pixel | firstBit | secondBit << 8 | thirdBit << 16;
    }

    public void close() throws IOException
    {
        this.bitStream.close();
    }
}
