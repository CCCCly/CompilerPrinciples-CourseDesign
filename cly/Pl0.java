package cly;
/*
 * 
 * ������
 * SymTable��TableUnit�����ɷ��ű�ĵ�¼����������TableUnit��SymTable�еı���
 * ��½���ű�����������������
 */
import java.io.*;
import java.util.Scanner;

public class Pl0
{
    public static void main(String [] args) 
    {
        String filename;
        System.out.println("����Դ�����ļ���:");
        System.out.print(">>");
        Scanner s=new Scanner(System.in);
        filename=s.next();
        Pl0Analysis mp=new Pl0Analysis(filename);
        if(!mp.mgpAnalysis())
        {
            System.out.println(">����ɹ�");
            System.out.println(">�� yִ�г���:");       
            mp.printTable();//������ű���ű�ÿ���˳������        
            mp.showPcodeInStack();//���PCode
            String choice;
            Scanner s1=new Scanner(System.in);
            choice = s1.next();
            System.out.print(">>");
            if(choice.equals("y")) 
            {
            	System.out.println("��ʼִ��:");
            	mp.interpreter();
            } 
        }
        System.out.println("���������");
    }
}
