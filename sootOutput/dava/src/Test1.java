import java.io.PrintStream;

public class Test1
{
    static long temporarycounter3308744;
    static long temporarycounter5123396;

    public static void main(String[]  r0)
    {

        long l0, l1;
        PrintStream r1, r2;
        Test1.func1(0);
        Test1.func1(95);
        r1 = System.out;
        l0 = temporarycounter3308744;
        r1.print("Class: Test1 Method: func1 Statement: temp$0 = x % 4 : ");
        r1.println(l0);
        r2 = System.out;
        l1 = temporarycounter5123396;
        r2.print("Class: Test1 Method: func1 Statement: temp$1 = x / 4 : ");
        r2.println(l1);
    }

    public static void func1(int  i0)
    {

        int i1, i2;
        long l3, l4, l5, l6;
        label_0:
        if (i0 != 0)
        {
            while (true)
            {
                l3 = temporarycounter3308744;
                l4 = l3 + 1L;
                temporarycounter3308744 = l4;
                i1 = i0 % 4;

                if (i1 == 0)
                {
                    break label_0;
                }

                l5 = temporarycounter5123396;
                l6 = l5 + 1L;
                temporarycounter5123396 = l6;
                i2 = i0 / 4;
                i0 = i2;
            }
        }
    }
}
