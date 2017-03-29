import java.awt.image.BufferedImage;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.File;
import javax.imageio.*;
import javax.imageio.stream.*;
import java.util.Iterator;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class Steganography
{
	private static final String OptionsMessage = "Options are '[-E or -D] image.ext message'";

    public static void main(String args[])
    {
        ValidateArguments(args);

        boolean boolEncode = (args[0].toLowerCase()).contains("e");
        boolean boolDecode = (args[0].toLowerCase()).contains("d");

        try
        {

			File readImageFile = new File(args[1]); 

			BufferedImage bufferedImage = ImageIO.read(readImageFile);
			String imageType = getImageType(readImageFile);

			String secret_message = args[2];
	        if (boolEncode)
	        {
	        	InputStream stream = new ByteArrayInputStream(args[2].getBytes());
	        	SteganographyImageEncoder steganographyImageEncoder = new SteganographyImageEncoder(bufferedImage, stream);
	        	steganographyImageEncoder.encode();

	        	ImageIO.write(bufferedImage, imageType, new File("image-steg.jpg"));
	        }

	        if (boolDecode)
	        {
	        	System.out.println("Decoding file...");
	        	SteganographyImageDecoder steganographyImageDecoder = new SteganographyImageDecoder(bufferedImage, System.out);
	        	steganographyImageDecoder.decode();
	        }

		}
		catch(IOException e)
		{
			// some code goes here...
			System.out.println("Something went wrong when processing the image: " + e);
		}
    }

  	/**
     * Validates the given argument list to be of the correct size and to have the correct content.
     *
     * @param args
     */
    private static void ValidateArguments(String[] args)
    {
        if (args.length == 0 || args.length == 1 || args.length == 2)
        {
            System.out.println("Invalid arguments: " + String.join(" ", args) + "\n" + OptionsMessage);
        }
    }

    private static String getImageType(File file) throws IOException
    {
        ImageInputStream iis = ImageIO.createImageInputStream(file);
		Iterator<ImageReader> imageReaders = ImageIO.getImageReaders(iis);
		if (imageReaders.hasNext())
		{
			ImageReader reader = (ImageReader) imageReaders.next();
			return reader.getFormatName();
		}

		return null;
    }

}