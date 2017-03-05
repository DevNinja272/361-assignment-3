import java.awt.Image;


public class SteganographyImageDecoder implements SteganographyImage
{

	public SteganographyImageDecoder (Image image)
	{
		super(image);
		super.bitStream = new BitInputStream(..);
	}

	public byte[] read()
	{

	}
}