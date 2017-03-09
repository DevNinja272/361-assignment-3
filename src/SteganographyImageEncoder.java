import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

public class SteganographyImageEncoder extends SteganographyImage
{
    private final InputBitStream inputBitStream;

    public SteganographyImageEncoder(BufferedImage bufferedImage, InputStream inputStream)
            throws IOException
    {
        // TODO: check whether it should be input or output bit stream
        super(bufferedImage, new InputBitStream(new Base64EncodingStream(inputStream)));
        this.inputBitStream = (InputBitStream) super.bitStream;
    }

    @Override
    public Iterator iterator()
    {
        throw new NotImplementedException();
        // return new SteganographyImage.BufferedImageIterator();
    }

    public void encode() throws IOException
    {
        for (Integer pixel : this)
        {
            throw new NotImplementedException();
            // int bits[] = decodePixel(pixel);
        }
    }
}