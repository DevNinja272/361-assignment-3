/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.util.ArrayList;

/**
 *
 * @author jinwook
 */
public class ReferenceMonitor 
{
    protected ObjectManager objMan;
    protected ArrayList<SecurityInfo> subjSecurityInfoList;
    protected ArrayList<SecurityInfo> objSecurityInfoList;
    
    public ReferenceMonitor()
    {
        objMan = new ObjectManager();
        subjSecurityInfoList = new ArrayList<SecurityInfo>();
        objSecurityInfoList = new ArrayList<SecurityInfo>();
    }

    protected class SecurityInfo
    {
        protected SecurityLevel level;
        protected Object item;
        
        public SecurityInfo(Object item, SecurityLevel level)
        {
            this.level = level;
            this.item = item;
        }
    }
    
    private SecurityLevel getSubjSecurityLevel(Object item)
    {
        for(SecurityInfo si: subjSecurityInfoList)
        {
            if (si != null && si.item != null && si.item instanceof BLPsubject)
            {
                if (((BLPsubject)(si.item)).equals(item)) {
                    return si.level;
                }
            }
        }
        return null;
    }

    private SecurityLevel getObjSecurityLevel(Object item)
    {
        for(SecurityInfo si: objSecurityInfoList)
        {
            if(si.item.equals(item))
            {
                return si.level;
            }
        }
        return null;
    }

    public void createSubjectEntry(BLPsubject subj, SecurityLevel level)
    {
        subjSecurityInfoList.add(new SecurityInfo(subj, level));
    }

    public void createObject(String name, SecurityLevel level)
    {
        BLPobject obj = objMan.createObject(name);
        if (obj != null)
        {
            objSecurityInfoList.add(new SecurityInfo(obj.getName(), level));
        }
    }

    public void printState()
    {
        System.out.println("The current state is:");
        for(SecurityInfo si: objSecurityInfoList)
        {
            if (si != null && si.item != null) 
            {
                BLPobject obj = objMan.get(si.item.toString());
                String s = "\t" + obj.getName() + " has value: " + obj.getValue();
                System.out.println(s);
            }
        }

        for(SecurityInfo si: subjSecurityInfoList)
        {
            if (si != null && si.item != null && si.item instanceof BLPsubject) 
            {
                BLPsubject subj = (BLPsubject)si.item;
                String s = "\t" + subj.getName() + " has recently read: " + subj.getTemp();
                System.out.println(s);
            }
        }

        System.out.println();
    }

    public void execute(InstructionObject instruction)
    {
        SecurityLevel objSL = getObjSecurityLevel(instruction.getObj());
        SecurityLevel subjSL = getSubjSecurityLevel(instruction.getSubj());

        BadInstruction bi = null;
        String result = null;
        switch(instruction.getCommand())
        {
            case "read":
                result = instruction.getSubj()
                        + " reads "
                        + instruction.getObj();

                if(objSL == null || subjSL == null)
                {
                    // BAD INSTRUCTION - SYNTAX ERROR
                    bi = new BadInstruction("READ: Invalid syntax. Object/Subject does not exist.");
                }
                else
                {
                    int value = objMan.read(instruction.getObj());
                    if (!subjSL.dominates(objSL))
                    {
                        value = 0;
                    }

                    for(SecurityInfo si: subjSecurityInfoList)
                    {
                        if (si != null && si.item != null && si.item instanceof BLPsubject)
                        {
                            BLPsubject subj = (BLPsubject) si.item;
                            if (subj.getName().equals(instruction.getSubj()))
                            {
                                subj.setTemp(value);
                                break;
                            }
                        }
                    }
                }
                break;
            case "write":
                Integer value;
                try
                {
                    value = Integer.valueOf(instruction.getVal());
                }
                catch(NumberFormatException e)
                {
                    value = null;
                }

                result = instruction.getSubj().toLowerCase()
                        + " writes value "
                        + value
                        + " to "
                        + instruction.getObj().toLowerCase();

                if(objSL == null || subjSL == null || value == null)
                {
                    // BAD INSTRUCTION - SYNTAX ERROR
                    bi = new BadInstruction("WRITE: Invalid Syntax. Object/Subject/Value does not exist.");
                }
                else if (subjSL == SecurityLevel.HIGH && objSL == SecurityLevel.LOW)
                {
                    // BAD INSTRUCTION - PERMISSION DENIED
                }
                else
                {
                    if(!objMan.write(instruction.getObj(), value))
                    {
                        bi = new BadInstruction("Write failed.");
                    }
                }
                break;
            default:
                // BAD INSTRUCTION - SYNTAX ERROR
                bi = new BadInstruction("Command does not exist");
        }

        if (bi != null)
        {
            result = bi.toString();
        }

        System.out.println(result != null ? result : new BadInstruction("Unknown error."));
        printState();
    }
}
