import java.awt.Image;
import java.io.IOException;

public abstract class SteganographyImage () implements Closeable
{
	protected final Image image;
	protected final BitStream bitStream;

	public SteganographyImage (Image image)
	{
		this.image = image;
	}

	public decodePixel(pixel)
	{
		int thirdBit = (pixel >> 16) & 1;
		int secondBit = (pixel >> 8) & 1;
		int firstBit = pixel & 1;
		return new int[]{firstBit, secondBit, thirdBit}; 
	}

	public encodePixel(int pixel, int firstBit, int secondBit, int thirdBit)
	{
		pixel |= firstBit & 1;
		pixel |= (secondBit & 1) << 8;
		pixel |= (thirdBit & 1) << 16;
		return pixel;
	}

	public void close() throws IOException
	{
		this.bitStream.close();
	}
}