import java.io.PrintStream;

public class Test4
{
    static long temporarycounter1522469;

    public static void main(String[]  r0)
    {

        int i0, i1;
        long l2, l3, l4;
        PrintStream r1;
        i0 = 10;

        do
        {
            i1 = i0 + 1;
            l2 = temporarycounter1522469;
            l4 = l2 + 1L;
            temporarycounter1522469 = l4;
            i0 = i1;
        }
        while (i1 < 40);

        r1 = System.out;
        l3 = temporarycounter1522469;
        r1.print("Class: Test4 Method: main Statement: x = temp$1 : ");
        r1.println(l3);
    }
}
