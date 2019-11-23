import java.io.PrintStream;

public class Test3
{
    static long temporarycounter8979231;
    static long temporarycounter3541439;
    static long temporarycounter352157;
    static long temporarycounter8188358;

    public static void main(String[]  r0)
    {

        long l0, l1, l2, l3;
        PrintStream r1, r2, r3, r4;
        Test3.func1(100);
        Test3.func2(10);
        r1 = System.out;
        l0 = temporarycounter8979231;
        r1.print("Class: Test3 Method: func1 Statement: if x > 0 goto nop : ");
        r1.println(l0);
        r2 = System.out;
        l1 = temporarycounter3541439;
        r2.print("Class: Test3 Method: func1 Statement: temp$0 = x : ");
        r2.println(l1);
        r3 = System.out;
        l2 = temporarycounter352157;
        r3.print("Class: Test3 Method: func2 Statement: if x < 100 goto nop : ");
        r3.println(l2);
        r4 = System.out;
        l3 = temporarycounter8188358;
        r4.print("Class: Test3 Method: func2 Statement: temp$0 = x : ");
        r4.println(l3);
    }

    public static void func1(int  i0)
    {

        int i1;
        long l2, l3, l4, l5;
        while (true)
        {
            l2 = temporarycounter8979231;
            l3 = l2 + 1L;
            temporarycounter8979231 = l3;

            if (i0 <= 0)
            {
                return;
            }

            l4 = temporarycounter3541439;
            l5 = l4 + 1L;
            temporarycounter3541439 = l5;
            i1 = i0 - 1;
            i0 = i1;
        }
    }

    public static void func2(int  i0)
    {

        int i1;
        long l2, l3, l4, l5;
        while (true)
        {
            l2 = temporarycounter352157;
            l3 = l2 + 1L;
            temporarycounter352157 = l3;

            if (i0 >= 100)
            {
                return;
            }

            l4 = temporarycounter8188358;
            l5 = l4 + 1L;
            temporarycounter8188358 = l5;
            i1 = i0 + 1;
            i0 = i1;
        }
    }
}
