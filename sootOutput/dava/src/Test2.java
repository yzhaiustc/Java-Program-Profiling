import java.io.PrintStream;

public class Test2
{
    static long temporarycounter7873603;
    static long temporarycounter7318849;
    static long temporarycounter4362128;
    static long temporarycounter8071949;
    static long temporarycounter9918990;

    public static void main(String[]  r0)
    {

        long l0, l1;
        PrintStream r1, r2, r3, r4, r5;
        Test2.func1(20);
        r1 = System.out;
        l0 = temporarycounter7873603;
        r1.print("Class: Test2 Method: func1 Statement: if y > 0 goto nop : ");
        r1.println(l0);
        r2 = System.out;
        l1 = temporarycounter7318849;
        r2.print("Class: Test2 Method: func1 Statement: z = x : ");
        r2.println(l1);
        r3 = System.out;
        r3.print("Class: Test2 Method: func1 Statement: if z > 0 goto nop : ");
        r3.println(0L);
        r4 = System.out;
        r4.print("Class: Test2 Method: func1 Statement: temp$0 = z : ");
        r4.println(0L);
        r5 = System.out;
        r5.print("Class: Test2 Method: func1 Statement: temp$2 = y : ");
        r5.println(0L);
    }

    public static void func1(int  i0)
    {

        long l5, l6, l7, l8;

        while (true)
        {
            l5 = temporarycounter7873603;
            l6 = l5 + 1L;
            temporarycounter7873603 = l6;

            if (i0 <= 0)
            {
                return;
            }

            l7 = temporarycounter7318849;
            l8 = l7 + 1L;
            temporarycounter7318849 = l8;
        }
    }
}
