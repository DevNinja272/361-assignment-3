import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

public class SteganographyImageEncoder extends SteganographyImage
{
    private final InputBitStream inputBitStream;
    private BufferedImage stegBufferedImage;
    private int originalImageWidth;
    private int originalImageHeight;
    private int currentWidth;
    private int currentHeight;

    // //////////////
    /* Constructor */
    /////////////////

    public SteganographyImageEncoder(BufferedImage bufferedImage, InputStream inputStream)
            throws IOException
    {
        // TODO: check whether it should be input or output bit stream
        super(bufferedImage, new InputBitStream(new Base64EncodingStream(inputStream)));
        this.inputBitStream = (InputBitStream) super.bitStream;
        this.originalImageHeight = bufferedImage.getHeight();
        this.originalImageWidth = bufferedImage.getWidth();
        this.currentWidth = -1;
        this.currentHeight = 0;
    }

    ////////////////////////////
    /* Accessors and Mutators */
    ////////////////////////////

    private int getOriginalImageWidth()
    { return this.originalImageWidth; }

    private void setOriginalImageWidth(int width)
    { this.originalImageWidth = width; }

    private int getOriginalImageHeight()
    { return this.originalImageHeight; }

    private void setOriginalImageHeight(int height)
    { this.originalImageHeight = height; }

    private int getCurrentWidth()
    { return this.currentWidth; }

    private void setCurrentWidth(int width)
    { this.currentWidth = width; }

    private int getCurrentHeight()
    { return this.currentHeight; }

    private void setCurrentHeight(int height)
    { this.currentHeight = height; }

    ////////////
    /* Helper */
    ////////////

    private void updateStegImage (int rgb)
    {
        if (getCurrentWidth() > getOriginalImageWidth())
        {
            setCurrentWidth(-1);
            setCurrentHeight(getCurrentHeight() + 1);
        }
        
        if (getCurrentHeight() > getOriginalImageHeight() || this.stegBufferedImage == null)
        {
            return;
        }
        else
        {
            setCurrentWidth(getCurrentWidth() + 1);
        }

        this.stegBufferedImage.setRGB(getCurrentWidth(), getCurrentHeight(), rgb);
    }

    ////////////
    /* Public */
    ////////////
    public void encode() throws IOException
    {

        System.out.println("before for loop of encode");
        for (Integer pixel : this)
        {
            int firstBit = this.inputBitStream.next();
            int secondBit = this.inputBitStream.next();
            int thirdBit = this.inputBitStream.next();

            if (firstBit == -1 || secondBit == -1 || thirdBit == -1)
            {
                
            }

            System.out.println("pixel: " + pixel + ", firstbit: " + firstBit + ", secondBit: " + secondBit + ", thirdBit: " + thirdBit);
            int encodedPixel = encodePixel(pixel, firstBit, secondBit, thirdBit);

            updateStegImage(encodedPixel);
        }
    }

    public BufferedImage getBufferedImage()
    { return this.stegBufferedImage; }
}