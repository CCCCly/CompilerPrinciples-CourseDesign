package cly;
/**
 * Ŀ����������
 */
public class APcode 
{
    private static int LIT = 0; //LIT 0 ��a ȡ����a��������ջջ��
    private static int OPR = 1; //OPR 0 ��a ִ�����㣬a��ʾִ��ĳ�����㣬�����Ǻ�������������ע��
    private static int LOD = 2; //LOD L ��a ȡ��������Ե�ַΪa�����ΪL���ŵ�����ջ��ջ��
    private static int STO = 3; //STO L ��a ������ջջ�������ݴ����������Ե�ַΪa����β�ΪL��
    private static int CAL = 4; //CAL L ��a ���ù��̣�ת��ָ�����ڵ�ַΪa����β�ΪL��
    private static int INT = 5; //INT 0 ��a ����ջջ��ָ������a
    private static int JMP = 6; //JMP 0 ��a������ת�Ƶ���ַΪa��ָ��
    private static int JPC = 7; //JPC 0 ��a ����ת��ָ�ת�Ƶ���ַΪa��ָ��
    private static int RED = 8; //RED L ��a �����ݲ������������Ե�ַΪa����β�ΪL��
    private static int WRT = 9; //WRT 0 ��0 ��ջ���������

    private int MAX_PCODE=10000;
    private int codePtr=0;          //ָ����һ����Ҫ�����Ĵ������APcode�еĵ�ַ


    private SPcode[] pcodeArray=new SPcode[MAX_PCODE];//����һ��Ŀ���������

    public APcode()
    {
    	//��ʼ��
        for(int i=0;i<MAX_PCODE;i++)
        {
            pcodeArray[i]=new SPcode(-1,-1,-1);
        }
    }
    //����Ŀ�����
    public void gen(int f,int l,int a)
    {
        pcodeArray[codePtr].setF(f);
        pcodeArray[codePtr].setL(l);
        pcodeArray[codePtr].setA(a);
        codePtr++;
    }

    public int getCodePtr()
    {
        return codePtr;
    }

    public int getOPR()
    {
        return OPR;
    }

    public int getLIT() 
    {
        return LIT;
    }

    public int getLOD() 
    {
        return LOD;
    }

    public int getSTO()
    {
        return STO;
    }

    public int getCAL() 
    {
        return CAL;
    }

    public int getINT() 
    {
        return INT;
    }

    public int getJMP() 
    {
        return JMP;
    }

    public int getJPC() 
    {
        return JPC;
    }

    public int getRED() 
    {
        return RED;
    }

    public int getWRT() 
    {
        return WRT;
    }

    public SPcode[] getPcodeArray() 
    {
        return pcodeArray;
    }

}

