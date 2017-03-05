/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.util.ArrayList;

/**
 *
 * @author jinwook
 */
public class SecureSystem {
	private ReferenceMonitor refMon;
	private ArrayList<BLPsubject> subjects;

	private SecureSystem()
	{
		refMon = new ReferenceMonitor();
		subjects = new ArrayList<BLPsubject>();
	}

	private ReferenceMonitor getReferenceMonitor()
	{
		return refMon;
	}

	private void createSubject(String name, SecurityLevel level)
	{
		BLPsubject subj = new BLPsubject(name);
		if(!subjects.contains(subj))
		{
	        subjects.add(subj);
	        getReferenceMonitor().createSubjectEntry(subj, level);
		}
	}

	private boolean subjExists(BLPsubject subj)
	{
		return subjects.contains(subj);
	}

	/**
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
        if(args.length > 0)	// Valid File
        {
        	File inputFile = new File(args[0]);

        	if(!SecureSystem.IsValidFile(inputFile))
        	{
        		SecureSystem.Fail();
        	}

        	SecureSystem sys = new SecureSystem();
        	sys.createSubject("lyle", SecurityLevel.LOW);
        	sys.createSubject("hal", SecurityLevel.HIGH);
        	sys.getReferenceMonitor().createObject("Lobj", SecurityLevel.LOW);
        	sys.getReferenceMonitor().createObject("Hobj", SecurityLevel.HIGH);

        	SecureSystem.execute(sys, inputFile);
        }
    }

    private static void execute(SecureSystem sys, File inputFile)
    {
		BufferedReader bufferedReader;;
		try
		{
		    bufferedReader = new BufferedReader(new FileReader(inputFile));

			String line;
			while ((line = bufferedReader.readLine()) != null)
			{
				line = line.trim().toLowerCase();
				if(!line.equals(""))
				{
					sys.getReferenceMonitor().execute(new InstructionObject(line));
				}
			}
		}
		catch (Exception e)
        {
            System.out.println("Error opening file.");
        }
    }

    private static boolean IsValidFile(File inputFile)
    {
    	if(!inputFile.exists())
        {
            SecureSystem.Log("ERROR: FILE DOES NOT EXIST");
            return false;
        }

        if(!inputFile.canRead())
        {
            SecureSystem.Log("ERROR: CANNOT READ FILE");
            return false;
        }

        return true;
    }

    private static void Log(String s)
    {
    	System.out.println(s);
    }

    private static void Fail()
    {
    	System.exit(-1);
    }
    
}
