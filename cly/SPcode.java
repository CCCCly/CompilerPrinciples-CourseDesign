package cly;
/**
 * ����Ŀ�����
 */
public class SPcode 
{
/**  
    *    F�δ���α������
    *    L�δ�����ò���˵����Ĳ��ֵ
    *    A�δ���λ��������Ե�ַ��*/

    private int F;
    private int L;
    private int A;

    public SPcode(int f,int l,int a)
    {
        F=f;
        L=l;
        A=a;
    }
    public void setF(int f)
    {
        F=f;
    }
    public void setL(int l)
    {
        L=l;
    }
    public void setA(int a)
    {
        A=a;
    }
    public int getF()
    {
        return F;
    }
    public int getL()
    {
        return L;
    }
    public int getA()
    {
        return A;
    }
}