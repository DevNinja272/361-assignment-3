/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.Closeable;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.function.Consumer;
import java.util.function.IntSupplier;

/**
 * @author jinwook
 */
class ReferenceMonitor implements Closeable
{
    ////////////////
    /* Properties */
    ////////////////

    private final SubjectManager          subjectManager;
    private final ObjectManager           objectManager;
    private final ArrayList<SecurityInfo> subjectSIList;
    private final ArrayList<SecurityInfo> objectSIList;
    private       PrintWriter             printWriter;

    /////////////////
    /* Initializer */
    /////////////////

    {
        subjectManager = new SubjectManager();
        objectManager = new ObjectManager();
        subjectSIList = new ArrayList<>();
        objectSIList = new ArrayList<>();
    }

    //////////////////////////
    /* Accessors & Mutators */
    //////////////////////////

    private SubjectManager getSubjectManager()
    { return this.subjectManager; }

    private ObjectManager getObjectManager()
    { return this.objectManager; }

    private ArrayList<SecurityInfo> getSubjectSIList()
    { return this.subjectSIList; }

    private ArrayList<SecurityInfo> getObjectSIList()
    { return this.objectSIList; }

    private PrintWriter getPrintWriter()
    { return this.printWriter; }

    private void setPrintWriter(PrintWriter printWriter)
    { this.printWriter = printWriter; }

    ////////////////////////
    /* SecurityInfo Class */
    ////////////////////////

    private class SecurityInfo
    {
        /* Properties */
        final SecurityLevel level;
        final String        name;

        /* Constructor */
        SecurityInfo(String name, SecurityLevel level)
        {
            this.level = level;
            this.name = name;
        }

        /* Accessors & Mutators */
        String getName()
        { return this.name; }

        SecurityLevel getLevel()
        { return this.level; }

        /* Helper Methods */
        boolean isValid()
        {
            return getLevel() != null && getName() != null;
        }
    }

    ////////////////////
    /* Helper Methods */
    ////////////////////

    private SecurityInfo getSIFromSIListByName(String name, ArrayList<SecurityInfo> siList)
    {
        if (siList != null)
        {
            for (SecurityInfo si : siList)
            {
                if (areValidSI(si) && si.getName().equalsIgnoreCase(name))
                {
                    return si;
                }
            }
        }

        return null;
    }

    private SecurityInfo getSubjectSecurityInfo(String name)
    { return getSIFromSIListByName(name, getSubjectSIList()); }

    private SecurityInfo getObjectSecurityInfo(String name)
    { return getSIFromSIListByName(name, getObjectSIList()); }

    private boolean existsByName(String itemName, ObjectManager manager)
    { return manager.exists(itemName); }

    private boolean subjectExists(String subjectName)
    { return existsByName(subjectName, getSubjectManager()); }

    private boolean objectExists(String objectName)
    { return existsByName(objectName, getObjectManager()); }

    private boolean areValidSI(SecurityInfo... siList)
    {
        for (SecurityInfo si : siList)
        {
            if (si == null || !si.isValid())
            {
                return false;
            }
        }

        return true;
    }

    private void createObject(SecurityInfo si,
                              ArrayList<SecurityInfo> siList,
                              ObjectManager manager)
    {
        if (areValidSI(si) && !manager.exists(si.getName()))
        {
            siList.add(si);
            manager.add(si.getName());
        }
    }

    private void createObject(String name, SecurityLevel level)
    {
        createObject(new SecurityInfo(name, level), getObjectSIList(), getObjectManager());
    }

    private void createSubject(String name, SecurityLevel level)
    {
        createObject(new SecurityInfo(name, level), getSubjectSIList(), getSubjectManager());
    }

    private void destroyObject(String objectName)
    {
        getObjectManager().remove(objectName);
        getObjectSIList().remove(getObjectSecurityInfo(objectName));
    }

    private boolean canCreate(SecurityInfo subjectSI, SecurityInfo objectSI)
    {
        return areValidSI(subjectSI, objectSI)
               && getSubjectManager().exists(subjectSI.getName())
               && !getObjectManager().exists(objectSI.getName());
    }

    private boolean canWrite(SecurityInfo subjectSI, SecurityInfo objectSI)
    {
        return areValidSI(subjectSI, objectSI)
               && subjectExists(subjectSI.getName())
               && objectExists(objectSI.getName())
               && objectSI.getLevel().dominates(subjectSI.getLevel());
    }

    private boolean canRead(SecurityInfo subjectSI, SecurityInfo objectSI)
    {
        return areValidSI(subjectSI, objectSI)
               && subjectExists(subjectSI.getName())
               && objectExists(objectSI.getName())
               && subjectSI.getLevel().dominates(objectSI.getLevel());
    }

    private boolean canDestroy(SecurityInfo subjectSL, SecurityInfo objectSL)
    {
        return canWrite(subjectSL, objectSL);
    }

    private void logInstructionIfEnabled(Instruction instruction) throws IOException
    {
        PrintWriter printWriter = getPrintWriter();
        if (printWriter != null)
        {
            printWriter.println(instruction == null ? "NULL INSTRUCTION" : instruction.toString());
        }
    }

    /////////////////////
    /* Package Methods */
    /////////////////////

    void enableLogging(String verbosePath) throws IOException
    {
        setPrintWriter(new PrintWriter(verbosePath));
    }

    void disableLogging() throws IOException
    {
        PrintWriter printWriter = getPrintWriter();
        if (printWriter != null)
        {
            printWriter.close();
            setPrintWriter(null);
        }
    }

    void createSubject(String name, SecurityLevel level, Consumer<IntSupplier> code)
    {
        createSubject(name, level);
        getSubjectManager().setSubjectCode(name, code);
    }

    void printState()
    {
        String printString = "The current state is:\n";

        for (SecurityInfo si : getObjectSIList())
        {
            if (si != null && si.getName() != null)
            {
                printString += "\t"
                               + si.getName()
                               + " has value: "
                               + getObjectManager().read(si.getName())
                               + "\n";
            }
        }

        for (SecurityInfo si : getSubjectSIList())
        {
            if (si != null && si.getName() != null)
            {
                printString += "\t"
                               + si.getName()
                               + " has recently read: "
                               + getSubjectManager().read(si.getName())
                               + "\n";
            }
        }
    }

    /**
     * Executes the given instruction.
     * @param instruction
     * @throws IOException
     */
    void execute(Instruction instruction) throws IOException
    {
        logInstructionIfEnabled(instruction);

        if (instruction == null || !instruction.isValid())
        {
            return;
        }

        SecurityInfo objectSI  = getObjectSecurityInfo(instruction.getObjectName());
        SecurityInfo subjectSI = getSubjectSecurityInfo(instruction.getSubjectName());

        switch (instruction.getCommand())
        {
            case READ:
                if (canRead(subjectSI, objectSI))
                {
                    Integer value = getObjectManager().read(objectSI.getName());
                    getSubjectManager().write(subjectSI.getName(), value);
                }
                else if (areValidSI(subjectSI))
                {
                    getSubjectManager().write(subjectSI.getName(), 0);
                }
                break;
            case WRITE:
                if (canWrite(subjectSI, objectSI))
                {
                    getObjectManager().write(objectSI.getName(), instruction.getValue());
                }
                break;
            case CREATE:
                objectSI = new SecurityInfo(instruction.getObjectName(),
                                            subjectSI == null ? null : subjectSI.getLevel());
                if (canCreate(subjectSI, objectSI))
                {
                    createObject(objectSI.getName(), objectSI.getLevel());
                }
                break;
            case DESTROY:
                if (canDestroy(subjectSI, objectSI))
                {
                    destroyObject(objectSI.getName());
                }
                break;
            case RUN:
                if (subjectSI != null)
                {
                    getSubjectManager().run(subjectSI.getName());
                }
                break;
            default:
        }
    }

    ///////////////
    /* Closeable */
    ///////////////

    public void close() throws IOException
    {
        PrintWriter printWriter = getPrintWriter();
        if (printWriter != null)
        {
            try
            {
                printWriter.flush();
            }
            finally
            {
                printWriter.close();
            }
        }
    }
}
