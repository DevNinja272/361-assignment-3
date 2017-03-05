/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * @author jinwook
 */
public class Instruction
{
    //////////////////
    /* Command Enum */
    //////////////////

    public enum Command
    {
        /* Enum Values */
        CREATE("create"),
        WRITE("write"),
        READ("read"),
        RUN("run"),
        DESTROY("destroy"),
        BAD(null);

        /* Properties */
        private String description;

        /* Constructors */
        Command(String description)
        {
            this.setDescription(description);
        }

        /* Accessors & Mutators */
        private void setDescription(String description)
        {
            this.description = description;
        }

        public String getDescription()
        {
            return this.description;
        }

        /* Static Methods */
        public static Command fromString(String value)
        {
            for (Command command : Command.values())
            {
                if (command == BAD)
                {
                    break;
                }
                else if (command.getDescription().equalsIgnoreCase(value))
                {
                    return command;
                }
            }
            return BAD;
        }
    }

    ////////////////
    /* Properties */
    ////////////////

    private final Command command;
    private final String  subjectName;
    private final String  objectName;
    private final Integer value;

    //////////////////
    /* Constructors */
    //////////////////

    Instruction(Command command, String subjectName, String objectName, Integer value)
    {
        this.command = command;
        this.subjectName = subjectName;
        this.objectName = objectName;
        this.value = value;
    }

    private Instruction(String[] arguments)
    {
        Command command     = null;
        String  subjectName = null;
        String  objectName  = null;
        Integer value       = null;

        if (arguments != null)
        {
            switch (arguments.length)
            {
                // Note: Lack of break statements is purposeful; fall through
                //  ensures correct array access as well as command validation.
                case 4:
                    try
                    {
                        value = Integer.valueOf(arguments[1]);
                    }
                    catch (NumberFormatException ex)
                    {
                        value = null;
                    }
                case 3:
                    objectName = arguments[1];
                case 2:
                    subjectName = arguments[1];
                case 1:
                    command = Command.fromString(arguments[0]);
                default:
                    break;
            }
        }

        this.command = command;
        this.subjectName = subjectName;
        this.objectName = objectName;
        this.value = value;
    }

    Instruction(String line)
    {
        this(line == null
             ? null
             : line.trim().replaceAll("\\p{javaSpaceChar}{2,}", " ").split(" "));
    }

    /////////////////////////
    /* Accesors & Mutators */
    /////////////////////////

    Command getCommand()
    { return command; }

    String getSubjectName()
    { return this.subjectName; }

    String getObjectName()
    { return this.objectName; }

    Integer getValue()
    { return this.value; }

    ////////////////////
    /* Helper Methods */
    ////////////////////

    private boolean isValidCreate()
    {
        return getCommand() == Instruction.Command.CREATE
               && getSubjectName() != null
               && getObjectName() != null
               && getValue() == null;
    }

    private boolean isValidWrite()
    {
        return getCommand() == Command.WRITE
               && getSubjectName() != null
               && getObjectName() != null
               && getValue() != null;
    }

    private boolean isValidRead()
    {
        return getCommand() == Command.READ
               && getSubjectName() != null
               && getObjectName() != null
               && getValue() == null;
    }

    private boolean isValidRun()
    {
        return getCommand() == Command.RUN
               && getSubjectName() != null
               && getObjectName() == null
               && getValue() == null;
    }

    private boolean isValidDestroy()
    {
        return getCommand() == Command.DESTROY
               && getSubjectName() != null
               && getObjectName() != null
               && getValue() == null;
    }

    private String toStringOrDefault(Object object)
    {
        return object == null ? "" : object.toString();
    }

    /////////////////////
    /* Package Methods */
    /////////////////////

    boolean isValid()
    {
        switch (getCommand())
        {
            case CREATE:
                return isValidCreate();
            case DESTROY:
                return isValidDestroy();
            case READ:
                return isValidRead();
            case RUN:
                return isValidRun();
            case WRITE:
                return isValidWrite();
            default:
                return false;
        }
    }

    ////////////////////
    /* Public Methods */
    ////////////////////

    @Override
    public String toString()
    {
        //@formatter:off
        return (toStringOrDefault(getCommand())
                + " " + toStringOrDefault(getSubjectName())
                + " " + toStringOrDefault(getObjectName())
                + " " + toStringOrDefault(getValue()))
                .toUpperCase();
        //@formatter:on
    }
}
