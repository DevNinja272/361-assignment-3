import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;
import java.lang.IllegalArgumentException;

public class SteganographyImageDecoder extends SteganographyImage
{
    private final OutputBitStream outputBitStream;

    public SteganographyImageDecoder(BufferedImage bufferedImage, OutputStream outputStream)
            throws IOException
    {
        super(bufferedImage, new OutputBitStream(new Base64DecodingStream(outputStream)));
        this.outputBitStream = (OutputBitStream) super.bitStream;
    }

    ////////////
    /* Public */
    ////////////

    public void decode() throws IOException
    {
        try
        {
            for (Integer pixel : this)
            {
                int decodedPixel[] = decodePixel(pixel);

                outputBitStream.write(decodedPixel[0]);
                outputBitStream.write(decodedPixel[1]);
                outputBitStream.write(decodedPixel[2]);
                System.out.println(decodedPixel[0] + "" + decodedPixel[1] + "" + decodedPixel[2]);
            }
        }
        catch (IllegalArgumentException iae)
        {
            // Done parsing 
        }
    }
}