/**
 * Created by HP User on 2/20/2017.
 */

import java.util.function.Consumer;
import java.util.function.IntSupplier;

class SubjectManager extends ObjectManager
{
    ////////////////////
    /* Helper Methods */
    ////////////////////

    @Override
    protected SecureObject getNewInstance(String name)
    {
        return new SecureSubject(name);
    }

    /////////////////////
    /* Package Methods */
    /////////////////////

    void setSubjectCode(String subjectName, Consumer<IntSupplier> code)
    {
        SecureSubject subject = (SecureSubject) super.getByName(subjectName);
        if (subject != null)
        {
            subject.setCode(code);
        }
    }

    void run(String name)
    {
        SecureSubject subject = (SecureSubject) super.getByName(name);
        if (subject != null)
        {
            try
            {
                subject.run();
            }
            catch (Exception e)
            {
                System.out.println("Error while running "
                                   + subject.getName()
                                   + ": "
                                   + e);
            }
        }
    }
}
