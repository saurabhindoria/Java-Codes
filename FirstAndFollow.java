/*
* @author: Saurabh Indoria
* email: saurabhindoria2012@gmail.com
* College: TCET, Mumbai University
* Date: 23rd April, 2017
*
* Problem Definition:
*	Finding FIRST and FOLLOW set in the given grammar
*	The input pattern is as explained:
*
*	Accept all productions on a new line, use # for Epsilon
*	Do not use symbols like E',
*	Instead denote it with some new symbol like P, Q, etc.
*	Do not use two letter terminal like 'id', instead use single letter
*
*		Input will be:
*		S=bB
*		S=#
*		B=a
*		B=#
*		
*		Logically Input will be saved like this:
*			Index	LHS(char)	RHS(String)
*			0		S			bB
*			1		S			#
*			2		B			a
*			3		B			#
*		
*/

//imports for BufferedReader
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

class FirstAndFollow
{
	public static char[] LHS;
	public static String[] RHS;
	public static int N;
	public static String result="";
	public static String resultSet = "";
	
	public static void main(String args[]) throws IOException
	{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("Enter number of productions: ");
		N = Integer.parseInt(br.readLine());
		LHS = new char[N];
		RHS = new String[N];
		
		for(int i = 0; i < N; i++)
		{
			String input = br.readLine();
			LHS[i] = (char)input.charAt(0);
			RHS[i] = input.substring(2);
		}
		//Find FIRST
		char choice = ' ';
		do{
		System.out.println("Enter symbol to find FIRST: ");
		char symbol = (char)br.read();
		result = "";
		FIRST(symbol);
		System.out.println("FIRST("+symbol+")= {"+resultSet+"}");
		System.out.println("Do you want to continue?(Y?N):");
		br.readLine();
		choice = (char)br.read();
		br.readLine();
		}while(choice=='Y');
		
		//Find FOLLOW
		do{
		System.out.println("Enter symbol to find FOLLOW: ");
		char symbol = (char)br.read();
		result = "";
		FOLLOW(symbol);
		//PAY ATTENTION: we are calling createResultSet() here unlike in FIRST.
		createResultSet();
		System.out.println("FOLLOW("+symbol+")= {"+resultSet+"}");
		System.out.println("Do you want to continue?(Y?N):");
		br.readLine();
		choice = (char)br.read();
		br.readLine();
		}while(choice=='Y');
		
		//End
	}

/*
* 	LOGIC for FIRST set
*	
*	RULES:
*		1.If X is a terminal then First(X) is just X!
*		2.If there is a Production X → ε then add ε to first(X)
*		3.If there is a Production X → Y1Y2..Yk then add first(Y1Y2..Yk) to first(X)
*		4.First(Y1Y2..Yk) is either
*			1.First(Y1) (if First(Y1) doesn't contain ε)
*			2.OR (if First(Y1) does contain ε) then First (Y1Y2..Yk) is everything in First(Y1) <except for ε > as well as everything in First(Y2..Yk)
*			3.If First(Y1) First(Y2)..First(Yk) all contain ε then add ε to First(Y1Y2..Yk) as well.
*/	
	
	
	public static void FIRST(char symbol)
	{
		if(!Character.isUpperCase(symbol))
		{
			result += symbol;
			createResultSet();
			return;
		}
		//traverse entire grammar for finding Symbol in LHS
		for(int i = 0; i < LHS.length; i++)
		{
			if(LHS[i] == symbol)
			{
				//found a production with symbol in LHS
				//traverse RHS
				int j=0;
				while( j < RHS[i].length() )
				{
					char new_symbol = RHS[i].charAt(j);
					FIRST(new_symbol);
					
					//if the symbol was terminal, no need to see RHS more.
					if(!Character.isUpperCase(new_symbol))
						break;
					
					//if it was a Non Terminal, then see if # was there in the FIRST(symbol)
					//if Yes, the traverse further grammar
					//else Stop traversing RHS more.
					
					if( j != RHS.length-1 ) //we do not remove the last epsilon bcoz if ALL symbols in RHS had epsilon, then we add epsilon to result
						if(!removableEpsilon()) //this function returns true if it was able to remove epsilon
							break;
					j++;
				}
			}
		}
		//now clean the results
		removeDuplicates();
		createResultSet();
		return;
	}
	
	public static boolean removableEpsilon()
	{
		String temp = "";
		boolean found = false;
		
		for( int i = 0; i < result.length(); i++ )
		{
			if(result.charAt(i) != '#')
				temp += result.charAt(i);
			else
				found = true;
		}
		result = temp;
		return found;
	}
	
	public static void removeDuplicates()
	{
		String newStr="";
		for(int i = 0; i < result.length(); i++)
		{
			if(!newStr.contains(String.valueOf(result.charAt(i))))
				newStr += String.valueOf(result.charAt(i));
		}
		result = newStr;
		return;
	}
	
	public static void createResultSet()
	{
		resultSet="";
		for( int i = 0; i < result.length(); i++ )
		{
			resultSet+=result.charAt(i);
			if(i!= result.length()-1)
				resultSet+=",";
		}
		return;
	}

/*
* 	LOGIC for FOLLOW set
*	
*	RULES:
*		1.First put $ (the end of input marker) in Follow(S) (S is the start symbol)
*		2.If there is a production A → aBb, (where a can be a whole string) then everything in FIRST(b) except for ε is placed in FOLLOW(B).
*		3.If there is a production A → aB, then everything in FOLLOW(A) is in FOLLOW(B)
*		4.If there is a production A → aBb, where FIRST(b) contains ε, then everything in FOLLOW(A) is in FOLLOW(B)
*/	
	
	public static String FOLLOW(char symbol)
	{
		String answer = "";
		
		//add $ to start symbol
		if(LHS[0] == symbol)
			answer += "$";
		
		//traverse entire grammar for finding Symbol in RHS
		for(int i = 0; i < RHS.length; i++)
		{
			int j=0;
			while( j < RHS[i].length() )
			{
				if(RHS[i].charAt(j) == symbol)
				{
					//if there is nothing ahead then just take follow of LHS
					if(j == RHS[i].length()-1 )
					{
						//call only if LHS is not same as symbol
						//otherwise it will go in an infinite loop
						if(symbol != LHS[i])
							answer +=FOLLOW(LHS[i]);
						
						//else simply ignore
					}
					else
					{
						//We nee to find the First of whatever is to the right
						//if suppose A->aBCDE and we were finding FOLLOW(B)
						//then we need to find FIRST(CDE)
						//but our function can find FIRST of only 1 character
						//so we wrote a new function compositeFIRST() which returns a string.
						answer += compositeFIRST(RHS[i].substring(j+1));
						
						//now, if the FIRST contained epsilon, we add FOLLOW of LHS
						result = answer; 
						if(removableEpsilon())
						{
							//COPY result back in answer
							answer = result;
							answer += FOLLOW(LHS[i]);
						}		
					}
				}
				j++;
			}
		}
		//now adopt removeDuplicates() for FOLLOW
		result = answer;
		removeDuplicates();
		return result;
	}
	
	public static String compositeFIRST(String s)
	{
		//THIS FUNCTION USES SAME LOGIC as FIRST().
		result = "";
		
		//For all symbols in String s, find the FIRST
		int j = 0;
		while( j < s.length() )
		{
			char new_symbol = s.charAt(j);
			FIRST(new_symbol);
			
			if(!Character.isUpperCase(new_symbol))
				break;
			
			if( j != s.length()-1 )
				if(!removableEpsilon())
					break;
			j++;
		}

		return result;
	}
	
}