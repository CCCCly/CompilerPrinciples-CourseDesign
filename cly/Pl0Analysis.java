package cly;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * �������
 * �ؼ���const��Ӧ�ı�ʶ����con
 * ��������Ӧ�ı�ʶ����const
 * ����������ű�����ַ��1
 * ������¼���ű�����ַ��1
 * ���̵�¼���ű����Ƚ�level��1�����������address��0��������ɺ��ٽ�level��һ���ٽ�address�ָ�
 */
import java.io.*;
public class Pl0Analysis 
{
    private static  int PROG=1;//program
    private static  int BEGIN=2;//begin
    private static  int END=3;//end
    private static  int IF=4;//if
    private static  int THEN=5;//then
    private static  int ELS=6;//else
    private static  int CON=7;//const
    private static  int PROC=8;//procdure
    private static  int VAR=9;//var
    private static  int DO=10;//do
    private static  int WHI=11;//while
    private static  int CAL=12;//call
    private static  int REA=13;//read
    private static  int WRI=14;//write
    private static  int ODD=15;//  oddl      ��keyWord��ÿ���ֵ��������ȵ�

    private static  int EQU=16;//"="
    private static  int LES=17;//"<"
    private static  int LESE=18;//"<="
    private static  int LARE=19;//">="
    private static  int LAR=20;//">"
    private static  int NEQU=21;//"<>"


    private static  int ADD=22;//"+"
    private static  int SUB=23;//"-"
    private static  int MUL=24;//"*"
    private static  int DIV=25;//"/"

    private static  int SYM=26;//��ʶ��
    private static  int CONST=27;//����

    private static  int CEQU=28;//":="

    private static  int COMMA=29;//","
    private static  int SEMIC=30;//";"
    private static  int POI=31;//"."
    private static  int LBR=32;//"("
    private static  int RBR=33;//")"

    LexAnalysis lex;
    private boolean errorHappen=false;
    private int rvLength=1000;    //����������
    private RValue[] rv=new RValue[rvLength];
    private int terPtr=0;       //RValue�ĵ����� iterator pointer

    private SymTable STable=new SymTable();       //���ű�
    private SymTable ST=new SymTable();       //���ű�
    private APcode  Pcode=new APcode();                 //���Ŀ�����


    private int level=0;                //������Ϊ��0��
    private int address=0;             //������������������Ϊ0
    private int addrIncrement=1;//TabelUnit�е�address����������������ʱ��ʹ������

    public Pl0Analysis(String filename)
    {
    	//��ʼ������
        for(int i=0;i<rvLength;i++)
        {
            rv[i]=new RValue();
            rv[i].setId(-2);
            rv[i].setValue("-2");
        }
        lex=new LexAnalysis(filename);
    }
    
    public void readLex()
    {
        String filename="LexTable.txt";
        File file=new File(filename);
        BufferedReader ints=null;
        String tempLex,temp[];
        try{
            ints=new BufferedReader(new FileReader(file));
        }catch(FileNotFoundException e){
            e.printStackTrace();
        }
        try{
            int i=0;
            while((tempLex=ints.readLine())!=null) {
                temp = tempLex.split(" ");
                rv[i].setId(Integer.parseInt(temp[0], 10));//���ַ���temp[0]ת����ʮ������
                rv[i].setValue(temp[1]);//���ַ���
                rv[i].setLine(Integer.parseInt(temp[2]));//���ַ���temp[2]ת����ʮ������
                i++;
            }
        }catch(IOException e){
            e.printStackTrace();
        }
    }
    
    public void prog()//����ݹ����<prog> �� program <id>��<block>
    {
        if(rv[terPtr].getId()==PROG)
        {
            terPtr++;
            if(rv[terPtr].getId()!=SYM)
            {
                errorHappen=true;
                showError(1,"");
            }
            else
            {
                terPtr++;
                if(rv[terPtr].getId()!=SEMIC)
                {
                    errorHappen=true;
                    showError(0,"");
                    return;
                }
                else
                {
                    terPtr++;
                    block();
                }
            }
        }
        else 
        {
            errorHappen = true;
            showError(2,"");
            return;//����
        }
    }

    public void block()//�顢������ݹ����
    //<block> �� [<condecl>][<vardecl>][<proc>]<body>
    {
        int addr0=address;        //��¼����֮ǰ����������������¼����ʼ��ַ�����Ա�ָ�ʱ����
        int tx0=STable.getTablePtr();       //��¼��������ڷ��ű�ĳ�ʼλ��
        int cx0;							//������Ҫ�����jmpָ����pcode�е�����
        int  propos=0;					   //��������ڷ��ű��е�λ��
        if(tx0>0)
        {
            propos=STable.getLevelPorc();        
            tx0=tx0- STable.getRow(propos).getSize();   //��¼ <�������> �ڷ��ű�Ŀ�ʼλ��
        }
        if(tx0==0)//����㻹δ���ٻ�洢�ռ�
        {
            address=3;      //ÿһ���ʼλ�õ������ռ�������ž�̬��SL����̬��DL���ͷ��ص�ַRA
        }
        else
        {
            //ÿһ���ʼλ�õ������ռ�������ž�̬��SL�ù���ֱ�����Ļ�׵�ַ��
        	//��̬��DL�����ߵĻ��¼�׵�ַ���ͷ��ص�ַRA
            //�����ŷ��βεĸ���,�βθ����洢�ڹ��̵�size��
            address=3+STable.getAllTable()[propos].getSize();
        }

        //�ݴ浱ǰPcode.codePtr��ֵ����jmp,0,0��codePtr�е�λ�ã�����һ�����
        cx0= Pcode.getCodePtr();
        Pcode.gen(Pcode.getJMP(),0,0);
        
        
        if(rv[terPtr].getId()!=CON&&rv[terPtr].getId()!=VAR&&
           rv[terPtr].getId()!=PROC&&rv[terPtr].getId()!=BEGIN)
        {
        	errorHappen = true;
        	showError(17,rv[terPtr].getValue());
        	return;
        }
        if(rv[terPtr].getId()==CON)
        {//�˴�û��terPtr++
            condecl();
        } 
        if(rv[terPtr].getId()==VAR)
        {
            vardecl();
        } 
        if(rv[terPtr].getId()==PROC)
        {
            proc();
            level--;
        }
        if(errorHappen==true)
        {
        	return;
        }
        /*
        * ����������ɣ�������䴦���֣�֮ǰ���ɵ�jmp��0��0Ӧ����ת�����λ��
        *
        * */
        //����jmp��0��0����ת��ַ
        if(tx0>0)
        {//ʵ������ֵ���� �βα���
            for(int i=0;i<STable.getAllTable()[propos].getSize();i++)
            {
                Pcode.gen(Pcode.getSTO(),0,STable.getAllTable()[propos].getSize()+3-1-i);
            }
        }
        
        //������������ָ��
        Pcode.getPcodeArray()[cx0].setA(Pcode.getCodePtr());
        Pcode.gen(Pcode.getINT(),0,address);        //���ɱ��������¼�ڴ�Ĵ���
        if(tx0==0)
        {
           // STable.getRow(tx0).setValue(Pcode.getCodePtr());     
        	//���������ڷ��ű��е�ֵ��Ϊ������ִ����俪ʼ��λ��
        }
        else 
        {
            STable.getRow(propos).setValue(Pcode.getCodePtr()-1-STable.getAllTable()[propos].getSize());     
          //������ڵ�ַ����value
        }

        body();
        Pcode.gen(Pcode.getOPR(),0,0);      //�����˳����̵Ĵ��룬������������ֱ���˳�����

        address=addr0;      //�ֳ���������ָ�address �ͷ��ű���һ�����д��һ��ָ��
        STable.setTablePtr(tx0);  
        STable.clearTheFowllowing(tx0);
    }
 
    public void condecl()//����˵�� �ݹ��ӳ���
    //<condecl> �� const <const>{,<const>};
    {         
        if(rv[terPtr].getId()==CON)
        {// const�����ĸ
            terPtr++;
            myconst();
            if(errorHappen==true)
            {
            	return;
            }
            while(rv[terPtr].getId()==COMMA)
            {//  ���� 
                terPtr++;
                myconst();
                if(errorHappen==true)
                {
                	return;
                }
            }
            if(rv[terPtr].getId()!=SEMIC)
            {//�ֺ�
                errorHappen=true;
                showError(0,"");
                return;
            }
            else
            {
                terPtr++;
            }
        }
        else
        {
            errorHappen=true;
            showError(-1,"");
            return;
        }
    }

    public void myconst()//�����ݹ��ӳ���
    //<const> �� <id>:=<integer>
    {
        String name;
        int value;
        if(rv[terPtr].getId()==SYM)
        {//��ʶ��
            name=rv[terPtr].getValue();
            terPtr++;
            if(rv[terPtr].getId()==CEQU)
            {// ��ֵ�ţ�=
                terPtr++;
                if(rv[terPtr].getId()==CONST)
                {
                    value=Integer.parseInt(rv[terPtr].getValue());//�ַ���ת������
                    if(STable.isNowExistSTable(name, level))
                    {
                        errorHappen=true;
                        showError(15,name);
                    }
                    STable.enterConst(name,level,value,address);//��¼���ű�
                    terPtr++;     
                }
            }
            else
            {
                errorHappen=true;
                showError(3,"");
                return;
            }
        }
        else 
        {
            errorHappen=true;
            showError(1,"");
            return;
        }
    }

    public void vardecl()//����˵��
    //<vardecl> �� var <id>{,<id>};
    {
        String name;
        int value;
        if(rv[terPtr].getId()==VAR)
        {
            terPtr++;
            if(rv[terPtr].getId()==SYM)
            {//��ʶ��
                name=rv[terPtr].getValue();
                if(STable.isNowExistSTable(name, level))//ͬ����
                {
                    errorHappen=true;
                    showError(15,name);
                }
                STable.enterVar(name,level,address);//��¼���ű�
                address+=addrIncrement;//����������һ
                terPtr++;
                while(rv[terPtr].getId()==COMMA)
                {
                    terPtr++;
                    if(rv[terPtr].getId()==SYM)
                    {
                        name=rv[terPtr].getValue();
                        if(STable.isNowExistSTable(name, level))
                        {
                            errorHappen=true;
                            showError(15,name);
                        }
                        STable.enterVar(name,level,address);
                        address+=addrIncrement;     //����������1��¼���ű�
                        terPtr++;
                    }
                    else
                    {
                        errorHappen=true;
                        showError(1,"");
                        return;
                    }
                }
                if(rv[terPtr].getId()!=SEMIC)
                {//�ֺ�
                    errorHappen=true;
                    showError(0,"");
                    return;
                }
                else
                {
                    terPtr++;
                }
            }
            else 
            {
                errorHappen=true;
                showError(1,"");
                return;
            }

        }
        else
        {
            errorHappen=true;
            showError(-1,"");
            return;
        }
    }

    public void proc()//�ֳ���
    //<proc> �� procedure <id>��[<id>{,<id>}]��;<block>{;<proc>}
    {
        if(rv[terPtr].getId()==PROC)
        {
            terPtr++;
            int count=0;//������¼proc���βεĸ���
            int propos;// ��¼���ֳ����ڷ��ű��е�λ��
            if(rv[terPtr].getId()==SYM)
            {
                String name=rv[terPtr].getValue();
                if(STable.isNowExistSTable(name, level))
                {
                    errorHappen=true;
                    showError(15,name);
                }
                propos=STable.getTablePtr();
                STable.enterProc(rv[terPtr].getValue(),level,address);//��¼���ű�
                level++;                //levelֵ��һ����Ϊ�������ж�����ڸ��µ�proc�����
                terPtr++;
                if(rv[terPtr].getId()==LBR)
                {
                    terPtr++;
                    if(rv[terPtr].getId()==SYM)
                    {
                    	//�βε�������ѹ����ű����ý����Ƿ��Ѿ����ڵļ�飩
                        STable.enterVar(rv[terPtr].getValue(),level,3+count) ;//��3+count����ʽ��Ԫλ����������֮��P244��
                        count++;
                        STable.getAllTable()[propos].setSize(count);
                        //�ñ������ڷ��ű��е�size���¼�βεĸ���
                        terPtr++;
                        while(rv[terPtr].getId()==COMMA)
                        {
                            terPtr++;
                            if(rv[terPtr].getId()==SYM)
                            {
                                STable.enterVar(rv[terPtr].getValue(),level,3+count) ;      //3+count+1Ϊ�β��ڴ洢�ռ��е�λ��
                                count++;
                                STable.getAllTable()[propos].setSize(count); //�ñ������ڷ��ű��е�size���¼�βεĸ���
                                terPtr++;
                            }
                            else
                            {
                                errorHappen=true;
                                showError(1,"");
                                return;
                            }
                        }
                    }
                    if(rv[terPtr].getId()==RBR)
                    {
                        terPtr++;
                        if(rv[terPtr].getId()!=SEMIC)
                        {
                            errorHappen=true;
                            showError(0,"");
                            return;
                        }
                        else
                        {
                            terPtr++;
                            block();
                            while(rv[terPtr].getId()==SEMIC)
                            {
                                terPtr++;
                                proc();
                            }
                        }
                    }
                    else
                    {
                        errorHappen=true;
                        showError(5,"");
                        return;
                    }

                }
                else
                {
                    errorHappen=true;
                    showError(4,"");
                    return;
                }
            }
            else
            {
                errorHappen=true;
                showError(1,"");
                return;
            }

        }
        else
        {
            errorHappen=true;
            showError(-1,"");
            return;
        }
    }

    public void body()//�������
    //begin <statement>{;<statement>}end
    {
        if(rv[terPtr].getId()==BEGIN)
        {
            terPtr++;
            statement();
            while(rv[terPtr].getId()==SEMIC||rv[terPtr].getId()!=END)
            {
              if(rv[terPtr].getId()==SEMIC)
              {      
            	terPtr++;
                statement();
              }
              else
              {
            	errorHappen=true;
                showError(0,"");
                return; 
              }
            }
            if(rv[terPtr].getId()==END)
            {
                terPtr++;
            }
            else
            {
                errorHappen=true;
                showError(7,"");
                return;
            }
        }
        else
        {
            errorHappen=true;
            showError(6,"");
            return;
        }
    }

    public void statement()//���
   /* <statement> �� <id> := <exp>               
    			   |if <lexp> then <statement>[else <statement>]
                   |while <lexp> do <statement>
                   |call <id>��[<exp>{,<exp>}]��
                   |<body>
                   |read (<id>{��<id>})
                   |write (<exp>{,<exp>})*/

    {
        if(rv[terPtr].getId()==IF)//if <lexp> then <statement>[else <statement>]
        {
            int cx1;
            terPtr++;
            lexp();
            if(rv[terPtr].getId()==THEN)
            {
                cx1=Pcode.getCodePtr(); //��cx1��¼jpc ��0��0��Pcode�еĵ�ַ����������
                Pcode.gen(Pcode.getJPC(),0,0);//��������ת��ָ�������boolֵΪ0ʱ��ת��
                							  //��ת��Ŀ�ĵ�ַ��ʱ��Ϊ0
                terPtr++;
                statement();
                int cx2=Pcode.getCodePtr();  //cx2��¼jmp��Pcode�еĵ�ַ��һ����������
                Pcode.gen(Pcode.getJMP(),0,0);
                
                Pcode.getPcodeArray()[cx1].setA(Pcode.getCodePtr()); 
                //��ַ�����jpc��0��0�е�A����
                Pcode.getPcodeArray()[cx2].setA(Pcode.getCodePtr());//���ǵ�û��else�����
                if(rv[terPtr].getId()==ELS)
                {
                    terPtr++;
                    statement();
                    Pcode.getPcodeArray()[cx2].setA(Pcode.getCodePtr());
                }
            }
            else
            {
                errorHappen=true;
                showError(8,"");
                return;
            }                                 
        }
        else if(rv[terPtr].getId()==WHI)//while <lexp> do <statement>
        {
            int cx1=Pcode.getCodePtr(); //�����������ʽ��Pcode�еĵ�ַ������do���ѭ��
            terPtr++;
            lexp();
            if(rv[terPtr].getId()==DO)
            {
                int cx2=Pcode.getCodePtr();//����������תָ��ĵ�ַ���ڻ���ʱʹ�ã�������������������ת
                Pcode.gen(Pcode.getJPC(),0,0);
                terPtr++;
                statement();
                Pcode.gen(Pcode.getJMP(),0,cx1); //���DO������������Ҫ��ת���������ʽ����
                								//����Ƿ�������������Ƿ����ѭ��
                Pcode.getPcodeArray()[cx2].setA(Pcode.getCodePtr()); //����JPC����ת��ָ��
            }
            else
            {
                errorHappen=true;
                showError(9,"");
                return;
            }
        }
        
        else if(rv[terPtr].getId()==CAL)//call <id>��[<exp>{,<exp>}])
        {
            terPtr++;
            int count=0;//�������鴫��Ĳ������趨�Ĳ����Ƿ����
            TableUnit tempRow;
            if(rv[terPtr].getId()==SYM)
            {
                if(STable.isPreExistSTable(rv[terPtr].getValue(),level))
                {        //���ű��д��ڸñ�ʶ��
                     tempRow=STable.getRow(STable.getNameRow(rv[terPtr].getValue()));  
                     //��ȡ�ñ�ʶ�������е�������Ϣ��������tempRow��
                    if(tempRow.getType()==STable.getProc()) 
                    { //�жϸñ�ʶ�������Ƿ�Ϊprocedure��SymTable��procdure������proc��������ʾ��
                        ;
                    }           //if����Ϊproc(��������)
                    else
                    {       //cal���Ͳ�һ�µĴ���
                        errorHappen=true;
                        showError(11,"");
                        return;
                    }
                }       //if���ű��д��ڱ�ʶ��
                else
                {           //cal δ��������Ĵ���
                    errorHappen=true;
                    showError(10,"");
                    return;
                }
                terPtr++;
                if(rv[terPtr].getId()==LBR)
                {
                    terPtr++;
                    if(rv[terPtr].getId()==RBR)
                    {//�Ȱ�û�в�������е���ǰ
                        terPtr++;
                        Pcode.gen(Pcode.getCAL(),level-tempRow.getLevel(),tempRow.getValue());        //���ù����еı����ֳ��ɽ��ͳ�����ɣ�����ֻ����Ŀ�����,+3����ϸ˵��
                    }
                    else
                    {
                        exp();
                        count++;
                        while(rv[terPtr].getId()==COMMA)
                        {
                            terPtr++;
                            exp();
                            count++;
                        }
                        if(count!=tempRow.getSize())//���������Ƿ�ƥ��
                        {
                            errorHappen=true;
                            showError(16,tempRow.getName());
                            return;
                        }
                        //��ַ����tempRow.getValue()
                        Pcode.gen(Pcode.getCAL(),level-tempRow.getLevel(),tempRow.getValue());
                        //���ù����еı����ֳ��ɽ��ͳ�����ɣ�����ֻ����Ŀ�����
                        if(rv[terPtr].getId()==RBR)
                        {
                            terPtr++;
                        }
                        else
                        {
                            errorHappen=true;
                            showError(5,"");
                            return;
                        }
                    }
                }
                else
                {
                    errorHappen=true;
                    showError(4,"");
                    return;
                }
            }
            else
            {
                errorHappen=true;
                showError(1,"");
                return;
            }
            
        }
        else if(rv[terPtr].getId()==REA)//read (<id>{��<id>})
        {
            terPtr++;
            if(rv[terPtr].getId()==LBR)
            {//(
                terPtr++;
                if(rv[terPtr].getId()==SYM)
                {//��ʶ��
                    if(!STable.isPreExistSTable((rv[terPtr].getValue()),level))
                    {      //�����ж��ڷ��ű����ڱ���򱾲�֮ǰ�Ƿ��д˱���
                        errorHappen=true;
                        showError(10,"");
                        return;
                    }//if�ж��ڷ��ű����Ƿ��д˱���
                    else
                    {           //stoδ��������Ĵ���
                        TableUnit tempTable=STable.getRow(STable.getNameRow(rv[terPtr].getValue()));
                        if(tempTable.getType()==STable.getVar())
                        {       //�ñ�ʶ���Ƿ�Ϊ��������
                            Pcode.gen(Pcode.getOPR(),0,16); //OPR 0 16	�������ж���һ����������ջ��   
                            Pcode.gen(Pcode.getSTO(),level-tempTable.getLevel(),tempTable.getAddress());  
                            //STO L ��a ������ջջ�������ݴ����������Ե�ַΪa����β�ΪL��
                        }//if��ʶ���Ƿ�Ϊ��������
                        else
                        {       //sto���Ͳ�һ�µĴ���
                            errorHappen=true;
                            showError(12,"");
                            return;
                        }
                    }
                    terPtr++;
                    while(rv[terPtr].getId()==COMMA)
                    {
                        terPtr++;
                        if(rv[terPtr].getId()==SYM)
                        {
                            if(!STable.isPreExistSTable((rv[terPtr].getValue()),level))
                            {      //�����ж��ڷ��ű����Ƿ��д˱���
                                errorHappen=true;
                                showError(10,"");
                                return;

                            }//if�ж��ڷ��ű����Ƿ��д˱���
                            else
                            {           //stoδ��������Ĵ���
                                TableUnit tempTable=STable.getRow(STable.getNameRow(rv[terPtr].getValue()));
                                if(tempTable.getType()==STable.getVar())
                                {       //�ñ�ʶ���Ƿ�Ϊ��������
                                    Pcode.gen(Pcode.getOPR(),0,16);         
                                    //OPR 0 16	�������ж���һ����������ջ�� 
                                    Pcode.gen(Pcode.getSTO(),level-tempTable.getLevel(),tempTable.getAddress());  //STO L ��a ������ջջ�������ݴ����������Ե�ַΪa����β�ΪL��
                                }//if��ʶ���Ƿ�Ϊ��������
                                else
                                {       //sto���Ͳ�һ�µĴ���
                                    errorHappen=true;
                                    showError(12,"");
                                    return;
                                }
                            }
                            terPtr++;
                        }else
                        {
                            errorHappen=true;
                            showError(1,"");
                            return;
                        }
                    }
                    if(rv[terPtr].getId()==RBR)
                    {//)
                        terPtr++;
                    }
                    else
                    {
                        errorHappen=true;
                        showError(5,"");
                    }
                }
                else
                {
                    errorHappen=true;
                    showError(26,"");
                }
            }
            else
            {
                errorHappen=true;
                showError(4,"");
                return;
            }
        }
        else if(rv[terPtr].getId()==WRI)//write (<exp>{,<exp>})
        {
            terPtr++;
            if(rv[terPtr].getId()==LBR)
            {
                terPtr++;
                exp();
                Pcode.gen(Pcode.getOPR(),0,14);         //���ջ����ֵ����Ļ
                while(rv[terPtr].getId()==COMMA)
                {
                    terPtr++;
                    exp();
                    Pcode.gen(Pcode.getOPR(),0,14);         //���ջ����ֵ����Ļ
                }

                Pcode.gen(Pcode.getOPR(),0,15);         //�������
                if(rv[terPtr].getId()==RBR)
                {
                    terPtr++;
                }
                else
                {
                    errorHappen=true;
                    showError(5,"");
                    return;
                }
            }
            else
            {
                errorHappen=true;
                showError(4,"");
                return;
            }
           
        }
        else if(rv[terPtr].getId()==BEGIN)
        {         //body������Ŀ�����
            body();
        }
        else if(rv[terPtr].getId()==SYM)//<id> := <exp>
        {      //��ֵ���
            String name=rv[terPtr].getValue();
            terPtr++;
            if(rv[terPtr].getId()==CEQU)
            {//:=
                terPtr++;
                exp();
                if(!STable.isPreExistSTable(name,level))
                {        //����ʶ���Ƿ��ڷ��ű��д���
                    errorHappen=true;
                    showError(14,name);
                    return;
                }//if�ж��ڷ��ű����Ƿ��д˱���
                else
                {           //stoδ��������Ĵ���
                    TableUnit tempTable=STable.getRow(STable.getNameRow(name));
                    if(tempTable.getType()==STable.getVar())//����ʶ���Ƿ�Ϊ��������
                    {           //����ʶ���Ƿ�Ϊ��������
                        Pcode.gen(Pcode.getSTO(),level-tempTable.getLevel(),tempTable.getAddress());  
                        //STO L ��a ������ջջ�������ݴ������
                    }
                    else
                    {       //���Ͳ�һ�µĴ���
                        errorHappen=true;
                        showError(13,name);
                        return;
                    }
                }
            }
            else
            {
                errorHappen=true;
                showError(3,"");
                return;
            }
        }
        else
        {
            errorHappen=true;
            showError(1,"");
            return;
        }
    }

    public void lexp()//����
    //<lexp> �� <exp> <lop> <exp>|odd <exp>
    {
        if(rv[terPtr].getId()==ODD)
        {
            terPtr++;
            exp();
            Pcode.gen(Pcode.getOPR(),0,6);  //OPR 0 6	ջ��Ԫ�ص���ż�жϣ����ֵ��ջ��
        }
        else
        {
            exp();
            int operator=lop();        //����ֵ��������Ŀ����룬����
            exp();
            if(operator==EQU)
            {
                Pcode.gen(Pcode.getOPR(),0,8);      //OPR 0 8	��ջ����ջ���Ƿ���ȣ�������ջԪ�أ����ֵ��ջ
            }
            else if(operator==NEQU)
            {
                Pcode.gen(Pcode.getOPR(),0,9);      //OPR 0 9	��ջ����ջ���Ƿ񲻵ȣ�������ջԪ�أ����ֵ��ջ
            }
            else if(operator==LES)
            {
                Pcode.gen(Pcode.getOPR(),0,10);     //OPR 0 10	��ջ���Ƿ�С��ջ����������ջԪ�أ����ֵ��ջ
            }
            else if(operator==LESE)
            {
                Pcode.gen(Pcode.getOPR(),0,13);     // OPR 0 13	��ջ���Ƿ�С�ڵ���ջ����������ջԪ�أ����ֵ��ջ
            }
            else if(operator==LAR)
            {
                Pcode.gen(Pcode.getOPR(),0,12);     //OPR 0 12	��ջ���Ƿ����ջ����������ջԪ�أ����ֵ��ջ
            }
            else if(operator==LARE)
            {
                Pcode.gen(Pcode.getOPR(),0,11);     //OPR 0 11	��ջ���Ƿ���ڵ���ջ����������ջԪ�أ����ֵ��ջ
            }
        }
    }

    public void exp()//���ʽ
    //<exp> �� [+|-]<term>{<aop><term>}
    {
        int tempId=rv[terPtr].getId();
        if(rv[terPtr].getId()==ADD)
        {
            terPtr++;
        }
        else if(rv[terPtr].getId()==SUB)
        {
            terPtr++;
        }
        term();
        if(tempId==SUB)
        {
            Pcode.gen(Pcode.getOPR(),0,1); //  OPR 0 1	ջ��Ԫ��ȡ��,˵���Ǹ���
        }
        while(rv[terPtr].getId()==ADD||rv[terPtr].getId()==SUB)
        {
            tempId=rv[terPtr].getId();
            terPtr++;
            term();
            if(tempId==ADD)
            {
                Pcode.gen(Pcode.getOPR(),0,2); //OPR 0 2	��ջ����ջ����ӣ�������ջԪ�أ����ֵ��ջ
            }
            else if(tempId==SUB)
            {
                Pcode.gen(Pcode.getOPR(),0,3);  //OPR 0 3	��ջ����ȥջ����������ջԪ�أ����ֵ��ջ
            }
        }
    }

    public void term()
    //<term> �� <factor>{<mop><factor>}
    {
        factor();
        while(rv[terPtr].getId()==MUL||rv[terPtr].getId()==DIV)
        {
            int tempId=rv[terPtr].getId();
            terPtr++;
            factor();
            if(tempId==MUL)
            {
                Pcode.gen(Pcode.getOPR(),0,4);       //OPR 0 4	��ջ������ջ����������ջԪ�أ����ֵ��ջ
            }
            else if(tempId==DIV)
            {
                Pcode.gen(Pcode.getOPR(),0,5);      // OPR 0 5	��ջ������ջ����������ջԪ�أ����ֵ��ջ
            }
        }
    } 

    public void factor()//����
    //<factor>��<id>|<integer>|(<exp>)
    {
        if(rv[terPtr].getId()==CONST)
        {//integer
            Pcode.gen(Pcode.getLIT(),0,Integer.parseInt(rv[terPtr].getValue()));    
            //�Ǹ�����,  LIT 0 a ȡ����a��������ջջ��
            terPtr++;
        }
        else if(rv[terPtr].getId()==LBR)
        {//(
            terPtr++;
            exp();
            if(rv[terPtr].getId()==RBR)
            {
                terPtr++;
            }
            else
            {
                errorHappen=true;
                showError(5,"");
            }
        }
        else if(rv[terPtr].getId()==SYM)
        {
            String name=rv[terPtr].getValue();
            if(!STable.isPreExistSTable(name,level))
            {     //�ж������б�ʶ���ڷ��ű����Ƿ����
                errorHappen=true;
                showError(10,"");
                return;
            }
            else
            {           //δ��������Ĵ���
                TableUnit tempRow= STable.getRow(STable.getNameRow(name));
                //ͨ�� name ->�к� ->��һ�����е�ֵ
                if(tempRow.getType()==STable.getVar())
                { //��ʶ���Ǳ�������
                    Pcode.gen(Pcode.getLOD(),level-tempRow.getLevel(),tempRow.getAddress());    
                    //������LOD L  ȡ��������Ե�ַΪa�����ΪL���ŵ�����ջ��ջ��
                }
                else if (tempRow.getType()==STable.getMyconst())
                {
                    Pcode.gen(Pcode.getLIT(),0,tempRow.getValue());         //������LIT 0 a ȡ����a��������ջջ��
                }
                else
                {       //���Ͳ�һ�µĴ���
                    errorHappen=true;
                    showError(12,"");
                    return;
                }
            }
            terPtr++;
        }
        else 
        {
            errorHappen=true;
            showError(1,"");
        }
    }
 
    public int lop()//��ϵ�����
    //<lop> �� =|<>|<|<=|>|>=
    {
        String loperator;
        if(rv[terPtr].getId()==EQU)
        {
            terPtr++;
            return EQU;//=
        }
        else if(rv[terPtr].getId()==NEQU)
        {
            terPtr++;
            return NEQU;//<>
        }
        else if(rv[terPtr].getId()==LES)
        {
            terPtr++;
            return LES;//<
        }
        else if(rv[terPtr].getId()==LESE)
        {
            terPtr++;
            return LESE;//<=
        }
        else if(rv[terPtr].getId()==LAR)
        {
            terPtr++;
            return LAR;//>
        }
        else if(rv[terPtr].getId()==LARE)
        {
            terPtr++;//>=
            return LARE;
        }
        return -1;
    }

    public boolean mgpAnalysis()
    {
       if( lex.bAnalysis())
       {  //���дʷ�����
	    	readLex();
	        prog();
	        return errorHappen;
       }
       else
       {
    	    return true;//�дʷ�����
       }
    }

    public void printTable()
    {
        if(errorHappen)
        {
        	return;
        }
    	String str="SymbolTable.txt";
        OutputStream myout=null;
        TableUnit temp;//����ÿһ�ε�һ����
    	
        File file=new File(str);
        try
        {
            myout=new FileOutputStream(file);
        }catch(FileNotFoundException e){
            e.printStackTrace();
        }
        String ttype;
        String tname ;
        String tlevel ;
        String taddress;
        String tvalue;
        String tsize;
        
        for(int i=0;i<STable.getLength();i++)
        {
        	temp = STable.getRow(i);
        	ttype = String.valueOf(temp.getType());
        	tname =String.valueOf(temp.getName());
            tlevel = String.valueOf(temp.getLevel()) ;
            taddress = String.valueOf(temp.getAddress());
            tvalue = String.valueOf(temp.getValue());
            tsize= String.valueOf(temp.getSize());
        	byte [] btype = ttype.getBytes();
        	byte [] bname = tname.getBytes();
        	byte [] blevel = tlevel.getBytes();
        	byte [] baddress = taddress.getBytes();
        	byte [] bvalue = tvalue.getBytes();
        	byte [] bsize = tsize.getBytes();
        	
            try{
                myout.write(btype);
                myout.write(' ');
                myout.write(bname);
                myout.write(' ');
                myout.write(blevel);
                myout.write(' ');
                myout.write(baddress);
                myout.write(' ');
                myout.write(bvalue);
                myout.write(' ');
                myout.write(bsize);
                myout.write('\r'); 
                myout.write('\n'); 
            }catch(IOException e){
                e.printStackTrace();
            }

        }
        
        try{
            myout.close();
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public void showPcode()
    {
    	
        for(int i=0;i<Pcode.getCodePtr();i++)
        {
           switch(Pcode.getPcodeArray()[i].getF())
           {
               case 0:
                   System.out.print("LIT  ");
                   break;
               case 1:
                   System.out.print("OPR  ");
                   break;
               case 2:
                   System.out.print("LOD  ");
                   break;
               case 3:
                   System.out.print("STO  ");
                   break;
               case 4:
                   System.out.print("CAL  ");
                   break;
               case 5:
                   System.out.print("INT  ");
                   break;
               case 6:
                   System.out.print("JMP  ");
                   break;
               case 7:
                   System.out.print("JPC  ");
                   break;
               case 8:
                   System.out.print("RED  ");
                   break;
               case 9:
                   System.out.print("WRI  ");
                   break;
           }
            System.out.println(Pcode.getPcodeArray()[i].getL()+"  "+Pcode.getPcodeArray()[i].getA());
        }
    }

    public void showPcodeInStack()
    {
    	if(errorHappen)
    	{
    		return;
    	}
        Interpreter inter=new Interpreter();
        inter.setPcode(Pcode);
        
        OutputStream myout=null ;
        String str="PcodeTable.txt";
        File file =new File(str);
        try 
        {
        	myout = new FileOutputStream(file);
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();	
        }
        String t_o = null;
        String t_f;
        String t_l;
        String t_a;
        for(int i=0;i<inter.getCode().getCodePtr();i++)
        {
            switch (inter.getCode().getPcodeArray()[i].getF())
            {
                case 0:
                  //  System.out.print("LIT  ");
                    t_o ="LIT"; 
                    break;
                case 1:
                //    System.out.print("OPR  ");
                    t_o ="OPR";
                    break;
                case 2:
                //    System.out.print("LOD  ");
                    t_o ="LOD";
                    break;
                case 3:
                  //  System.out.print("STO  ");
                    t_o ="STO";
                    break;
                case 4:
                //    System.out.print("CAL  ");
                    t_o ="CAL";
                    break;
                case 5:
                //    System.out.print("INT  ");
                    t_o ="INT";
                    break;
                case 6:
                //    System.out.print("JMP  ");
                    t_o ="JMP";
                    break;
                case 7:
                //    System.out.print("JPC  ");
                    t_o ="JPC";
                    break;
                case 8:
                 //   System.out.print("RED  ");
                    t_o ="RED";
                    break;
                case 9:
                 //   System.out.print("WRI  ");
                    t_o ="WRI";
                    break;
            }
            
            t_f = t_o;
            t_l = String.valueOf(inter.getCode().getPcodeArray()[i].getL());
            t_a = String.valueOf(inter.getCode().getPcodeArray()[i].getA());
            
            byte [] b_f=t_f.getBytes();
            byte [] b_l= t_l.getBytes();
            byte [] b_a=t_a.getBytes();
            try{
            	myout.write(b_f);
            	myout.write(' ');
            	myout.write(b_l);
            	myout.write(' ');
            	myout.write(b_a);
            	myout.write('\r');
            	myout.write('\n');
            }catch(IOException e){
                e.printStackTrace();
            }
            
         //   System.out.println(inter.getCode().getPcodeArray()[i].getL()+"  "+inter.getCode().getPcodeArray()[i].getA());
        }
    }

    public void showError(int i,String name)
    {
        switch (i)
        {
            case -1:
                System.out.print("ERROR "+i+" "+"in line " + rv[terPtr].getLine()+":");
                System.out.println("wrong token");        //�������岻��const��ͷ,�������岻��var ��ͷ
                break;
            case 0:
                System.out.print("ERROR "+i+" "+"in line " + (rv[terPtr].getLine()-1)+":");
                System.out.println("Missing semicolon");        //ȱ�ٷֺ�
                break;
            case 1:
                System.out.print("ERROR "+i+" "+"in line " + rv[terPtr].getLine()+":");
                System.out.println("Identifier illegal");       //��ʶ�����Ϸ�
                break;
            case 2:
                System.out.print("ERROR "+i+" "+"in line " + rv[terPtr].getLine()+":");
                System.out.println("The beginning of program must be 'program'");       //����ʼ��һ���ַ�������program
                break;
            case 3:
                System.out.print("ERROR "+i+" "+"in line " + rv[terPtr].getLine()+":");
                System.out.println("Assign must be ':='");       //��ֵû�ã�=
                break;
            case 4:
                System.out.print("ERROR "+i+" "+"in line " + rv[terPtr].getLine()+":");
                System.out.println("Missing '('");       //ȱ��������
                break;
            case 5:
                System.out.print("ERROR "+i+" "+"in line " + rv[terPtr].getLine()+":");
                System.out.println("Missing ')'");       //ȱ��������
                break;
            case 6:
                System.out.print("ERROR "+i+" "+"in line " + rv[terPtr].getLine()+":");
                System.out.println("Missing 'begin'");       //ȱ��begin
                break;
            case 7:
                System.out.print("ERROR "+i+" "+"in line " + rv[terPtr].getLine()+":");
                System.out.println("Missing 'end'");       //ȱ��end
                break;
            case 8:
                System.out.print("ERROR "+i+" "+"in line " + rv[terPtr].getLine()+":");
                System.out.println("Missing 'then'");       //ȱ��then
                break;
            case 9:
                System.out.print("ERROR "+i+" "+"in line " + rv[terPtr].getLine()+":");
                System.out.println("Missing 'do'");       //ȱ��do
                break;
            case 10:
                System.out.print("ERROR "+i+" "+"in line " + rv[terPtr].getLine()+":");
                System.out.println("Not exist "+"'"+rv[terPtr].getValue()+"'");       //call��write��read����У������ڱ�ʶ��
                break;
            case 11:
                System.out.print("ERROR "+i+" "+"in line " + rv[terPtr].getLine()+":");
                System.out.println("'"+rv[terPtr].getValue()+"'"+"is not a procedure");       //�ñ�ʶ������proc����
                break;
            case 12:
                System.out.print("ERROR "+i+" "+"in line " + rv[terPtr].getLine()+":");
                System.out.println("'"+rv[terPtr].getValue()+"'"+"is not a variable");       //read��write����У��ñ�ʶ������var����
                break;
            case 13:
                System.out.print("ERROR "+i+" "+"in line " + rv[terPtr].getLine()+":");
                System.out.println("'"+name+"'"+"is not a variable");       //��ֵ����У��ñ�ʶ������var����
                break;
            case 14:
                System.out.print("ERROR "+i+" "+"in line " + rv[terPtr].getLine()+":");
                System.out.println("Not exist"+"'"+name+"'");       //��ֵ����У��ñ�ʶ��������
                break;
            case 15:
                System.out.print("ERROR "+i+" "+"in line " + rv[terPtr].getLine()+":");
                System.out.println("Already exist"+"'"+name+"'");       //�ñ�ʶ���Ѿ�����
                break;
            case 16:
                System.out.print("ERROR "+i+" "+"in line " + rv[terPtr].getLine()+":");
                System.out.println("Number of parameters of procedure "+"'"+name+"'"+"is incorrect");       //�ñ�ʶ���Ѿ�����
                break;
            case 17:
                System.out.print("ERROR "+i+" "+"in line " + rv[terPtr].getLine()+":");
                System.out.println("ILLEGAL"+"'"+name+"'");       //�ñ�ʶ��������
                break;
            case 26:
                System.out.print("ERROR "+i+" "+"in line " + rv[terPtr].getLine()+":");
                System.out.println(name+"is not Var");       //�ñ�ʶ���Ѿ�����
                break;
        }

    }
    public void interpreter()
    {
        if(errorHappen)
        {
            return;
        }
        Interpreter inter=new Interpreter();
        inter.setPcode(Pcode);      //��Ŀ����봫�ݸ����ͳ�����н���ִ��
        inter.interpreter();
    }

}


