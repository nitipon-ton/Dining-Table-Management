public class Main
{
	public static boolean freeBlock (String studentFree, String lunchTime) //will make studentFree = 3 char
	{
		for(int i=0;i<studentFree.length();i++)
		{
			if(studentFree.substring(i,i+1).equals(lunchTime))
			{
				return true;
			}
		}
		return false;
	}
	
	public static int groupClose (int[][] close,int[] table) //find max closeness of a whole group
	{
		int out = 0;
		for(int i=0; i<table.length-1; i++)
		{
			for(int j=1; j<table.length-i; j++)
			{
				out += close[table[i]][table[i+j]] + close[table[i+j]][table[i]];
			}
		}
		return out;
	}
	
	public static int personClose (int[][] close,int[] table) //find max sum of closenesses of any student with other students in group
	{
		int max = 0;
		for(int i=0; i<table.length; i++)
		{
			int count = 0;
			for(int j=0; j<table.length; j++)
			{
				if(i!=j)
				{
					count += close[table[i]][table[j]] + close[table[j]][table[i]];
				}
			}
			if(count>max)
			{
				max = count;
			}
		}
		return max;
	}
	
	public static int nameToNum (String[] a, String b)
	{
		for(int i=0; i<a.length; i++)
		{
			if(a[i].equals(b))
			{
				return i; //include 0 as first student
			}
		}
		return -1; //should not reach this value
	}
	public static void main(String[] args)
	{
		FileReader myFileReader = new FileReader("names.txt");
		FileReader table = new FileReader("history.txt");
		
		int attempt = 0;
		int tableNum = 52;
		int tableAtBlock = 0; //first few tables that need to have free block at the time
		int maxgClose = -1; //trying to find min maxClose
	    int maxpClose = -1;
	    int gLimit = 110; //limit of closeness inside each table
	    int pLimit = 60; //limit of closeness for each student
	    
	    String lunchBlock = " ";
	    
	    String[] stdNamesRaw = myFileReader.getStringData(340); //don't forget to increase the size if there are new students
	    String[] history = table.getStringData(392); //don't forget to increase the size if there are new data
	    int stdNum = stdNamesRaw.length;
	    String[] stdNames = new String[(tableNum*(((stdNum-1)/tableNum)+1))];
	    int ghost = stdNames.length-stdNamesRaw.length;
	    for(int i=0;i<stdNames.length; i++)
	    {
	    	if(i<stdNamesRaw.length)
	    	{
	    		stdNames[i] = stdNamesRaw[i];
	    	}
	    	else
	    	{
	    		stdNames[i] = "********************************";
	    	}
	    }
	    stdNum = stdNames.length; //include ghost students in the total num
	    
	    
	    int[][] closeness = new int[stdNames.length][stdNames.length];
	    for(int i=0;i<stdNames.length;i++)
	    {
	    	for(int j=i+1;j<stdNames.length;j++)
	    	{
	    		if(stdNames[i].substring(0,2).equals(stdNames[j].substring(0,2)))
	    		{
	    			closeness[i][j]+=10;
	    		}
	    		if(stdNames[i].substring(3,13).equals(stdNames[j].substring(3,13)))
	    		{
	    			closeness[i][j]+=50;
	    		}
	    	}
	    }
	    
	    for(int i=0;i<history.length; i++)
	    {
	    	if(history[i].length()>2)
	    	{
	    		int start = i;
	    		while(i<history.length && history[i].length()>2)
	    		{
	    			i++;
	    		}
	    		int end = i;
	    		for(int j=start; j<end; j++)
	    		{
	    			for(int k=1; k<end-j; k++)
	    			{
	    				if(nameToNum (stdNames, history[j])>=0 && nameToNum (stdNames, history[j+k])>=0)
	    				{
	    					closeness[nameToNum (stdNames, history[j])][nameToNum (stdNames, history[j+k])]+=30;
	    				}
	    			}
	    		}
	    	}
	    }
	    
	   
	    while(maxgClose==-1 || maxpClose == -1 || maxgClose>gLimit || maxpClose>pLimit)
	    {
	    	attempt++;
	    	int[][] sampleTables = new int [tableNum][((stdNum-1)/tableNum)+1];
	    	boolean[]assigned = new boolean [stdNum];
	    	for(int i=0; i<sampleTables.length; i++)
		    {
		    	for(int j=0; j<sampleTables[0].length; j++)
		    	{
		    		int temp = (int)(stdNamesRaw.length*Math.random());
		    		while(assigned[temp]||(i<tableAtBlock && !freeBlock(stdNames[temp].substring(14,17),lunchBlock)))
		    		{
		    			temp = (int)(stdNamesRaw.length*Math.random());
		    		}
		    		if(i<ghost && j==0)
		    		{
		    			temp = stdNamesRaw.length+i;
		    		}
		    		sampleTables[i][j] = temp;
		    		assigned[temp]= true;
		    		System.out.print(stdNames[sampleTables[i][j]] + " / ");
		    	}
		    	System.out.print("group: "+groupClose(closeness,sampleTables[i]));
		    	System.out.println(" individual: "+personClose(closeness,sampleTables[i]));
		    	if(groupClose(closeness,sampleTables[i])>maxgClose)
		    	{
		    		maxgClose = groupClose(closeness,sampleTables[i]);
		    	}
		    	if(personClose(closeness,sampleTables[i])>maxpClose)
		    	{
		    		maxpClose = personClose(closeness,sampleTables[i]);
		    	}
		    }
		    if(maxgClose>gLimit || maxpClose>pLimit)
		    {
		    	System.out.println("Attempt: "+attempt+" FAIL"+" g="+maxgClose+" p="+maxpClose+"\n"+"_____________________________________________________");
		    	maxgClose = -1; maxpClose = -1;
		    }
		    else
		    {
		    	System.out.println("Attempt: "+attempt+" PASS"+" g="+maxgClose+" p="+maxpClose+"\n"+"_____________________________________________________"); System.out.println("SUMMARY-1(to copy to history.txt)");
		    	for(int i=0; i<sampleTables.length; i++)
			    {
		    		System.out.println(i+1);
			    	for(int j=0; j<sampleTables[0].length; j++)
			    	{
			    		if(!stdNames[sampleTables[i][j]].substring(0,2).equals("**"))
			    		{
			    			System.out.println(stdNames[sampleTables[i][j]]);
			    		}	
			    	}
			    }
		    	System.out.println("_____________________________________________________");
		    	System.out.println("SUMMARY-2(to send out)");
		    	for(int i=0; i<sampleTables.length; i++)
			    {
		    		System.out.println(i+1);
			    	for(int j=0; j<sampleTables[0].length; j++)
			    	{
			    		if(!stdNames[sampleTables[i][j]].substring(0,2).equals("**"))
			    		{
			    			System.out.println(stdNames[sampleTables[i][j]].substring(18));
			    		}
			    	}
			    	System.out.println("_____________________________________________________");
			    }
		    }
	    }
	}
}