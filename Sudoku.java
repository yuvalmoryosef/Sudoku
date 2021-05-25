//Author: Yuval Mor Yosef 312475528

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Sudoku {

	public static void main(String[] args) {
		int[][] board = readBoardFromFile("S1.txt");
	}

	// **************   Sudoku - Read Board From Input File   **************
	public static int[][] readBoardFromFile(String fileToRead){
		int[][] board = new int[9][9];
		try {
			BufferedReader bufferedReader = new BufferedReader(new FileReader(fileToRead)); // change S1.txt to any file you like (S2.txt, ...)
			int row = 0;
			String line;
			while ((line = bufferedReader.readLine()) != null) {
				for(int column = 0; column < line.length(); column++){
					char c = line.charAt(column);
					if(c == 'X')
						board[row][column] = 0;
					else board[row][column] = c - '0';
				}
				row++;
			}
			bufferedReader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return board;
	}



	// **************   Sudoku - Part1 (iterative)   **************
	public static int[][][] eliminateDomains(int[][] board){
		int[][][] domains= new int[9][9][9];
		boolean isItFull=false;
		do
		{
			isItFull=true;
			domains=mergeDomains(board);
			for(int i=0;i<9;i++)
			{
				for(int j=0;j<9;j++)
				{
					if(board[i][j]==0)
					{
						if(result(i, j, domains, board)!=-1)
						{
							board[i][j]=result(i, j, domains,board);
							domains[i][j][result(i, j, domains,board)-1]=1;
							isItFull=false;
							break;
						}
					}
				}
				if(isItFull==false)
					break;
			}
		}while(!isItFull);
		return domains;
	}
	public static int result(int row,int col, int[][][] domains, int[][]board){
		//the method return the number that can be in the row/col index
		//return the number if there is, if not, bring -1;
		int indexForChange=-1;
		int indexForCheck=0;
		for(int i=0;i<9;i++)
		{
			if(domains[row][col][i]==1)
			{
				indexForChange=i+1;
				//indexForCheck=calculateConditions(row,col,domains,indexForChange,board);
				if(indexForChange==indexForCheck)
					return indexForChange;
				for(int j=0;j<9;j++)
				{
					if(i!=j)
					{
						if(domains[row][col][j]==1) {
							indexForChange=10;
							break;
						}
					}
				}
				if(indexForChange==10)
					break;
			}
		}
		if(indexForChange==10)
		{
			return -1;
		}
		else
			return indexForChange;
	}
	public static int calculateConditions(int row,int col, int[][][] domains,int indexForChange, int[][]board){
		//check if there is a number in potential that shows in columns or rows in the same squere; 
		//return the number if there is, if not, bring -1;
		int countRow=0;//count same values.
		int countCol=0;//count same values.
		//row between 0-2,col between 0-2.
		if((row<=2))
		{
			for(int i=0;i!=row &&i<=2;i++)
			{
				for(int j=0;j<=8;j++)
				{
					if(board[i][j]==indexForChange)
						countRow++;
				}
			}
		}
		//col between 0-2.
		if((col<=2))
		{
			for(int i=0;i<=8;i++)
			{
				for(int j=0;j!=col&&j<=2;j++)
				{
					if(board[i][j]==indexForChange)
						countCol++;
				}
			}
		}
		//col between 3-5.
		if((col>=3)&&(col<=5))
		{
			for(int i=0;i<=8;i++)
			{
				for(int j=3;j==col&&j<=5;j++)
				{
					if(board[i][j]==indexForChange)
						countCol++;
				}
			}
		}
		//col between 6-8.
		if((col>=6))
		{
			for(int i=0;i<=8;i++)
			{
				for(int j=6;j==col&&j<=8;j++)
				{
					if(board[i][j]==indexForChange)
						countCol++;
				}
			}
		}
		//row between 3-5
		if((row>=3)&&(row<=5))
		{
			for(int i=3;i!=row &&i<=5;i++)
			{
				for(int j=0;j<=8;j++)
				{
					if(board[i][j]==indexForChange)
						countRow++;
				}
			}
		}

		//row between 6-8
		if((row>=6))
		{
			for(int i=6;i!=row &&i<=8;i++)
			{
				for(int j=0;j<=8;j++)
				{
					if(board[i][j]==indexForChange)
						countRow++;
				}
			}
		}
		if((countCol==2)||(countRow==2))
			return indexForChange;
		else
			return -1;
	}


	public static int[] createBasicArr(){
		int[] basicArr= new int[9];//create array.
		for(int i=0;i<9;i++)
			basicArr[i]=-1;
		return basicArr;
	}
	public static int[] createBasicArrForNotBeing(){
		int[] basicArr= new int[9];//create array.
		for(int i=0;i<9;i++)
			basicArr[i]=1;
		return basicArr;
	}
	public static int[][][] mergeDomains(int[][]board){
		int[][][] domains= new int[9][9][9];//create domains.
		for(int i=0;i<9;i++)
		{
			for(int j=0;j<9;j++) {
				domains[i][j]=eliminateCol(board,j,i);
			}
		}

		return domains;

	}

	public static int[] eliminateCol(int[][] board,int col,int row){
		//Eliminate the col and return to row for continue
		int[] indexArr= new int[9];
		if(board[row][col]!=0)
		{
			indexArr=createBasicArr();
			indexArr[board[row][col]-1]=1;
		}
		else
		{
			indexArr=createBasicArrForNotBeing();
			for(int i=0;i<9;i++)
			{
				if(board[i][col]!=0)
				{
					indexArr[board[i][col]-1]=-1;
				}
			}


		}
		return eliminateRow(board,col,row,indexArr);
	}
	public static int[] eliminateRow(int[][] board,int col,int row,int[]  indexArr){
		if(board[row][col]!=0)
		{
			return indexArr;
		}
		else
		{
			for(int i=0;i<9;i++)
				if(board[row][i]!=0)
					indexArr[board[row][i]-1]=-1;
		}
		return eliminateSquere(board,col,row,indexArr);
	}
	public static int[] eliminateSquere(int[][] board,int col,int row, int[]indexArr){
		if(board[row][col]!=0)
		{
			return indexArr;
		}
		else
		{
			//checking the number in the match squere. 
			//row between 0-2,col between 0-2.
			if((row<=2)&&(col<=2))
			{
				for(int i=0;i<=2;i++)
				{
					for(int j=0;j<=2;j++)
					{
						if(board[i][j]!=0)
							indexArr[board[i][j]-1]=-1;

					}
				}
			}
			//row between 0-2,col between 3-5.
			if((row<=2)&&(col<=5)&&(col>=3))
			{
				for(int i=0;i<=2;i++)
				{
					for(int j=3;j<6;j++)
					{
						if(board[i][j]!=0)
						{
							indexArr[board[i][j]-1]=-1;
						}
					}
				}
			}
			//row between 0-2,col between 6-8.
			if((row<=2)&&(col<=8)&&(col>=6))
			{
				for(int i=0;i<=2;i++)
				{
					for(int j=6;j<=8;j++)
					{
						if(board[i][j]!=0)
							indexArr[board[i][j]-1]=-1;
					}
				}
			}
			//row between 3-5,col between 0-2.
			if((row>=3)&&(row<=5)&&(col<=2))
			{
				for(int i=3;i<=5;i++)
				{
					for(int j=0;j<=2;j++)
					{
						if(board[i][j]!=0)
							indexArr[board[i][j]-1]=-1;
					}
				}
			}
			//row between 3-5,col between 3-5.
			if((row>=3)&&(row<=5)&&(col<=5)&&(col>=3))
			{
				for(int i=3;i<=5;i++)
				{
					for(int j=3;j<=5;j++)
					{
						if(board[i][j]!=0)
							indexArr[board[i][j]-1]=-1;
					}
				}
			}
			//row between 3-5,col between 6-8.
			if((row>=3)&&(row<=5)&&(col>=6)&&(col<=8))
			{
				for(int i=3;i<=5;i++)
				{
					for(int j=6;j<=8;j++)
					{
						if(board[i][j]!=0)
							indexArr[board[i][j]-1]=-1;
					}
				}
			}
			//row between 6-8,col between 0-2.
			if((row>=6)&&(col<=8)&&(col<=2))
			{
				for(int i=6;i<=8;i++)
				{
					for(int j=0;j<=2;j++)
					{
						if(board[i][j]!=0)
							indexArr[board[i][j]-1]=-1;
					}
				}
			}
			//row between 6-8,col between 3-5.
			if((row>=6)&&(col<=5)&&(col>=3))
			{
				for(int i=6;i<=8;i++)
				{
					for(int j=3;j<=5;j++)
					{
						if(board[i][j]!=0)
							indexArr[board[i][j]-1]=-1;
					}
				}
			}
			//row between 6-8,col between 6-8.
			if((row>=6)&&(col>=6))
			{
				for(int i=6;i<=8;i++)
				{
					for(int j=6;j<=8;j++)
					{
						if(board[i][j]!=0)
							indexArr[board[i][j]-1]=-1;
					}
				}
			}
		}
		return indexArr;
	}
	public static void printBoard(int[][][] domains, int[][] board){
		//Printing the board:
		printEliminateBoard(board);
		printEliminateDomains(domains);
	}
	public static void printEliminateBoard(int[][] board){
		//the method get the board array and print it.
		for(int i=0;i<3;i++)
		{
			System.out.print(board[i][0]+""+board[i][1]+""+board[i][2]+"|");
			System.out.print(board[i][3]+""+board[i][4]+""+board[i][5]+"|");
			System.out.print(board[i][6]+""+board[i][7]+""+board[i][8]);
			System.out.println();
		}
		System.out.println("---+---+---");
		for(int i=3;i<6;i++)
		{
			System.out.print(board[i][0]+""+board[i][1]+""+board[i][2]+"|");
			System.out.print(board[i][3]+""+board[i][4]+""+board[i][5]+"|");
			System.out.print(board[i][6]+""+board[i][7]+""+board[i][8]);
			System.out.println();
		}
		System.out.println("---+---+---");
		for(int i=6;i<9;i++)
		{
			System.out.print(board[i][0]+""+board[i][1]+""+board[i][2]+"|");
			System.out.print(board[i][3]+""+board[i][4]+""+board[i][5]+"|");
			System.out.print(board[i][6]+""+board[i][7]+""+board[i][8]);
			System.out.println();
		}	
	}
	public static void printEliminateDomains(int[][][] domains){
		//the method get the domain array and print all the options for every index.
		for(int i=0;i<9;i++)
		{
			for(int j=0;j<9;j++)
			{
				System.out.print(i+","+j+" = ");
				for(int k=0;k<9;k++) 
				{
					if(domains[i][j][k]==1)
					{
						System.out.print(k+1+",");
					}
				}
				System.out.println();
			}
		}
	}

	// **************   Sudoku - Part2 (recursive)   **************
	public static boolean solveSudoku(int[][] board){
		int[][] copyBoard=copyBoard(board);//create copy for board.
		int[][][]domains=new int [9][9][9];
		domains=eliminateDomains(copyBoard);//create domains for copyBoard.
		if(!isOk(domains,0,0))
			return false;
		int[] position=findEmptyIndex(copyBoard, 0, 0);//find place with zero in board.
		if((position[0]==-1)&&(position[1]==-1))
			return true;
		else {
			int getOption=getOption(domains, position, 0);
			return solveSudoku(board,copyBoard,domains,position,getOption);
		}
	}

	public static boolean solveSudoku(int[][]board,int[][] copyBoard,int[][][]domains,int[] position,int getOption){ 
		if(eliminatePro(board,copyBoard,domains,position,getOption))
			return true;
		else if(getOption==-1) {
			position=findEmptyIndex(copyBoard, 0, 0);
			if((position[0]==-1)&&(position[1]==-1))
			{
				if(isDone(domains, 0, 0))
					return true;
				else
					return false;
			}else
			{
				getOption=getOption(domains, position, 0);
				return eliminatePro(board,copyBoard,domains,position,getOption);
			}
		}
		else
			return eliminatePro(board,copyBoard,domains,position,getOption);
	}

	public static int getOption(int[][][]domains,int[]position,int i){
		//return the next option in this position.
		//i- counter in array.
		if((position[0]==-1)||(position[1]==-1)||(i>8))
			return -1;
		if(domains[position[0]][position[1]][i]==1)
			return (i+1);//there is no option for index to check.
		else {
			//return getOption(domains, position, i+1);
			return getOption(domains, position, i+1);
		}
	}
	public static boolean eliminatePro(int[][] board,int[][] copyBoard,int[][][]domains,int[]position,int getOption){
		copyBoard[position[0]][position[1]]=getOption;
		if(!isNull(eliminateDomains(copyBoard),position,0,0))
			return true;
		else {
			getOption=getOption(domains, position, getOption);
			return solveSudoku(board, copyBoard, domains, position, getOption);
		}
	}
	public static boolean isNull(int[][][]domains,int[]position,int sum,int counter){
		//if every index is -1=null
		if(counter>8)//if every index is -1=null
			if(sum==-9)
				return true;//there is no option for index to check.
			else
				return false;
		else {
			sum+=domains[position[0]][position[1]][counter];
			return isNull(domains,position,sum,counter+1);
		}
	}
	public static boolean isOk(int[][][]domains,int row,int col){
		//if every index is -1=null
		if((row>8)&&(col>8))
			return true;
		else {
			int[]pos= {row,col};
			if(isNull(domains, pos, 0, 0))
				return false;
			else
				if((row==8)&&(col==8))
					return true;
			if(col<8)
				return isOk(domains,row,col+1);
			else
				return isOk(domains,row+1,0);
		}
	}
	public static boolean isDone(int[][][]domains,int row,int col){
		//check if there is only one option in the domains for each index
		if((row>8)&&(col>8))
			return true;
		if(domains[row][col].length>0)
			return false;
		else
			if(col<9)
				isDone(domains,row,col+1);
			else
				isDone(domains,row+1,0);
		return true;
	}

	public static int [] findEmptyIndex(int[][] copyBoard,int row,int col){
		//the method get board and return index with zero;
		int[]position= {-1,-1};//index 0 contains row and 1 contains col.
		if((row>8)&&(col>8))
			return position;
		else if(row<=8)
		{
			if(col<9)
			{
				if(copyBoard[row][col]==0)
				{
					position[0]=row;
					position[1]=col;
					return position;
				}
				return findEmptyIndex(copyBoard ,row,col+1);
			}else
			{
				return findEmptyIndex(copyBoard ,row+1,0);
			}
		}
		return position;
	}
	public static int[][][] copyDomains(int[][][] domains){
		//the method get board and return copy;
		int[][][]copyDomains=new int [9][9][9];
		copyDomains=copyDomains(domains,copyDomains,0,0,0);
		return copyDomains;
	}
	public static int[][][] copyDomains(int[][][]domains,int[][][] copyDomains ,int row,int col,int counter){
		//the method get domains and return copy;
		if((row>8)&&(col>8)&&(counter>8))
			return copyDomains;
		else if(row<=8)
		{
			if(col<9)
			{
				if(counter<9) {
					copyDomains[row][col][counter]=domains[row][col][counter];
					return copyDomains(domains,copyDomains ,row,col,counter+1);
				}else
					return copyDomains(domains,copyDomains ,row,col+1,0);
			}else
			{
				return copyDomains(domains,copyDomains ,row+1,0,0);
			}
		}
		return copyDomains;
	}
	public static int[][] copyBoard(int[][] board){
		//the method get board and return copy;
		int[][]copyBoard=new int [9][9];
		copyBoard=copyBoard(board,copyBoard,0,0);
		return copyBoard;
	}
	public static int[][] copyBoard(int[][] board,int[][]copyBoard ,int row,int col){
		//the method get board and return copy;
		if((row>8)&&(col>8))
			return copyBoard;
		else if(row<=8)
		{
			if(col<9)
			{
				copyBoard[row][col]=board[row][col];
				return copyBoard(board,copyBoard ,row,col+1);
			}else
			{
				return copyBoard(board,copyBoard ,row+1,0);
			}
		}
		return copyBoard;
	}
}