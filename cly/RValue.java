package cly;
//RValue ÿһ�е�id���кš���Ӧ��ֵ�����ڴʷ�����ʶ��ÿ������
public class RValue 
{
    private int id;
    private int line;
    private String value;

    public void setId(int _id)
    {
        id=_id;
    }
    public void setValue(String _value)
    {
        value=_value;
    }
    public void setLine(int _line)
    {
        line=_line;
    }
    public int getId()
    {
        return id;
    }
    public int getLine()
    {
        return line;
    }
    public String getValue()
    {
        return value;
    }
}

