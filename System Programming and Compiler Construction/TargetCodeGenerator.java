/*
* @author: Saurabh Indoria
* email: saurabhindoria2012@gmail.com
* College: TCET, Mumbai University
* Date: 23rd April, 2017
*
*	Sample input
*	a=b+c*60
*
*/

//imports for BufferedReader
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

/*
* NOTE: This is a simplified version, sufficient for the practical mentioned in MU curriculum.
*		This may NOT be the complete/correct code.
*/

class TargetCodeGenerator
{
	static String kw(char a)
	{
		if(a=='*') return "MULF";
		if(a=='/') return "DIVF";
		if(a=='+') return "ADDF";
		if(a=='-') return "SUBF";
		if(a=='=') return "MOVF";
		else
			return "ERR";
	}
	public static void main(String args[]) throws IOException
	{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("Enter the expression: ");
		String input = br.readLine();
		char[] ops = {'*','+','-','/','='};
		char[] operators = new char[10];
		int operatorsCount = 0;
		String[] variables = new String[10];
		int variablesCount = 0;
		
		String temp="";
		for(int i = input.length()-1; i >= 0; i--)
		{
			boolean operatorFound = false;
			//find operators
			for(int j = 0; j < 5; j++)
				if(input.charAt(i) == ops[j])
				{
					operators[operatorsCount++] = ops[j];
					System.out.println("MOVF "+temp+", R"+variablesCount);
					variables[variablesCount] = "R"+variablesCount++;
					temp="";
					operatorFound = true;
				}
			if(!operatorFound)
				temp=Character.toString(input.charAt(i))+temp;	
		}
		//save the first variable, ie, 'a' in a=b+c*60
		variables[variablesCount++] = temp;
		
		//for every operator, write operations
		for(int i = 0; i < operatorsCount; i++)
			System.out.println(kw(operators[i])+" "+variables[i]+", "+variables[i+1]);
		
	}
}