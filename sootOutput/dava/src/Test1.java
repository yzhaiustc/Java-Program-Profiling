import java.io.PrintStream;

public class Test1
{
    static long UsedInst0ExeNum;
    static long UsedInst1ExeNum;

    public static void main(String[]  r0)
    {

        long l0, l1;
        PrintStream r1;
        int i2, i3;
        Test1.func1(0);
        Test1.func1(95);
        r1 = System.out;
        r1.print("temp$0 = x % 4: ");
        l0 = UsedInst0ExeNum;
        i2 = (int) l0;
        r1.println(i2);
        r1.print("temp$1 = x / 4: ");
        l1 = UsedInst1ExeNum;
        i3 = (int) l1;
        r1.println(i3);
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
                l3 = UsedInst0ExeNum;
                i1 = i0 % 4;
                l4 = l3 + 1L;
                UsedInst0ExeNum = l4;

                if (i1 == 0)
                {
                    break label_0;
                }

                l5 = UsedInst1ExeNum;
                i2 = i0 / 4;
                l6 = l5 + 1L;
                UsedInst1ExeNum = l6;
                i0 = i2;
            }
        }
    }
}
