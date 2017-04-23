/*
* @author: Saurabh Indoria
* email: saurabhindoria2012@gmail.com
* College: TCET, Mumbai University
* Date: 23rd April, 2017
*
*	Sample input:
*	id1=id2*id3
*	id1=id2*36 (Instead of 36, You can take any number)
*	id1=25*67
*
*	It also works on
*	id1=id2+id3*id4
*	id1=id2+id3*56
*	id1=35+id3*59
*	id1=35+21*59
*/

//imports for BufferedReader
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

/*
* NOTE: This is a simplified version, sufficient for the practical mentioned in MU curriculum.
*		This may NOT be the complete/correct code.
*/

class IntermediateCodeGenerator
{
	public static boolean isInteger(String s)
	{
		try
		{
			Integer.parseInt(s);
		}
		catch(Exception e)
		{
			//string cannot be converted into integer and threw exception
			return false;
		}
		//string can be converted into an integer
		return true;
	}
	public static void main(String args[]) throws IOException
	{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("Enter the expression: ");
		String input = br.readLine();
		char[] ops = {'*','+','-','/','='};
		int tempCount = 0;
		char latestOperator = ' ';
		String latestTemp = "";
		
		String temp="";
		for(int i = input.length()-1; i >= 0; i--)
		{
			boolean operatorFound = false;
			//find operators
			for(int j = 0; j < 5; j++)
				if(input.charAt(i) == ops[j])
				{
					if(isInteger(temp))
					{
						temp = "intoint("+temp+")";
						if(latestOperator != ' ')
						{
							//latest operator has some value,
							//so first print convert intoint()
							System.out.println("temp"+tempCount+"="+temp);
							temp = "temp"+tempCount++; //bcoz instead of 25, we want temp1
							//and then print the next step
						}
					}
					System.out.println("temp"+tempCount+"="+temp+latestOperator+latestTemp);
					latestOperator = ops[j];
					latestTemp = "temp"+tempCount;
					tempCount++;
					temp="";
					operatorFound = true;
				}
			if(!operatorFound)
				temp=Character.toString(input.charAt(i))+temp;	
		}
		
		//print last line
		System.out.println(temp+"="+latestTemp);
			
	}
}