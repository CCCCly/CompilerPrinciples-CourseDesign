package cly;
/*
 * TableUnit�Ƿ��ű��е�ÿһ����
 */
public class TableUnit
{

    private int type;       //��ʾ���������������
    private int value;      //��ʾ�����������ֵ
    private int level;      //Ƕ�ײ�Σ����ӦΪ3���������������Ƿ�С�ڵ��ڣ���SymbolTable�м��
    private int address;    //���������Ƕ�׹��̻���ַ�ĵ�ַ
    private int size;       //��ʾ������������������ռ�Ĵ�С���˱����;���Ӳ���йأ�
    						//ʵ�����ڱ���������Ϊ�˷��㣬ͳһ��Ϊ4��,
    						//���ù�����SymTable�е�����enter������
    private String name;    //�����������������

    public int  getType()
    {
        return type;
    }

    public int getValue()
    {
        return value;
    }

    public int getLevel()
    {
        return level;
    }

    public int getAddress()
    {
        return address;
    }

    public int getSize()
    {
        return size;
    }

    public String getName()
    {
        return name;
    }

    public void setType(int t)
    {
        type=t;
    }

    public void setValue(int v)
    {
        value=v;
    }

    public void setLevel(int L)
    {
        level=L;
    }

    public void setAddress(int a)
    {
        address=a;
    }

    public void setSize(int s)
    {
        size=s;
    }

    public void setName(String s)
    {
        name=s;
    }
}

