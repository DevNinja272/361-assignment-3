import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

public class SteganographyImageDecoder extends SteganographyImage
{
    private final InputBitStream inputBitStream;

    public SteganographyImageDecoder(BufferedImage bufferedImage, InputStream inputStream)
            throws IOException
    {
        super(bufferedImage, new InputBitStream(inputStream));
        this.inputBitStream = (InputBitStream) super.bitStream;
    }

    public Iterator iterator()
    {
        throw new NotImplementedException();
        // return new SteganographyImage.BufferedImageIterator();
    }

    public byte[] read() throws IOException
    {
        for (Integer pixel : this)
        {
            throw new NotImplementedException();
            // this.inputBitStream.next()
            // int bits[] = decodePixel(pixel);
        }

        return null;
    }

    // private InputBitStream get
}