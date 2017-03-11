UTEID: jk34669; jaw4978;
FIRSTNAME: Jinwook; Jesse;
LASTNAME: Kim; Wright;
CSACCOUNT: jk34669; jwright;
EMAIL: jinwook.kim@utexas.edu; jesse.wright@utexas.edu;

[Program 3]
[Description]
There are 9 files in total. Steganography is the entry point for the program. SteganographyImageEncoder and SteganographyImageDecoder are the two main steganography classes, and they use a BitStream and Base64Stream to either write encoded data to an image or read it from an image. A given message to be encoded into an image is Base64 encoded simply so that we can know where the message ends when reading in the message. To compile, just use "javac *.java". To run, use the given commands described in the original description by Young.

[Finish]
We finished the most of this assignment in terms of code. Currently the program doesn't encode properly because it doesn't set the pixels correctly. Decoding seems to work, but we haven't properly been able to test it since we can't encode.

[Questions&Answers]

[Question 1]
Comparing your original and modified images carefully, can you detect *any* difference visually (that is, in the appearance of the image)?

[Answer 1]
Given that we modigy the last bit of each channel, there's really not much of a discernible difference. This is a common basic approach to steganography with images.

   
[Question 2]
Can you think of other ways you might hide the message in image files (or in other types of files)?

[Answer 2]
One way might be to encode the last two bits of each color channel because it probably still wouldn't change much.


[Question 3]
Can you invent ways to increase the bandwidth of the channel?

[Answer 3]
Probably by modifying more bits in each color channel in each pixel, as in Answer 2.


[Question 4]
Suppose you were tasked to build an "image firewall" that would block images containing hidden messages. Could you do it? How might you approach this problem?

[Answer 4]
No, that would be extremely difficult. Data could be encrypted, making it seem random. It would be hard to tell if an image had secret data or just noise.


[Question 5]
Does this fit our definition of a covert channel? Explain your answer.

[Answer 5]
Yes, as information is being sent in a way not intended. It might not be a standard covert channel in that it doesn't use system metadata, etc., but it definitely repurposes something to be used to secretly send or receive information.s

