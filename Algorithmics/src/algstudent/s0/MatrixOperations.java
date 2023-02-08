package algstudent.s0;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;

public class MatrixOperations {
	
	public static int MIN_TRAVEL_PATH_VALUE = 1;
	public static int MAX_TRAVEL_PATH_VALUE = 4;
	
	private int[][] matrix;
	private int minValue;
	private int maxValue;
	
	/**
	 * Creates a new matrix given its size and the minimum and maximum possible values contained inside
	 * @param n; size of the matrix to be created
	 * @param min ; minimum value contained
	 * @param max ; maximum value contained
	 */
	public MatrixOperations(int n, int min, int max) {
		if(max<min) {
			throw new IllegalArgumentException("The maximum values must be greater than the minimum values");
		}
		
		createMatrixOperations(n);
		Random rand = new Random();
		
		for(int i=0; i<n; i++) {
			for(int j=0; j<n; j++) {
				matrix[i][j] = rand.nextInt(min, max+1);
			}
		}
		
		this.minValue = min;
		this.maxValue = max;
	}
	
	/**
	 * Renders the matrix contained at the file provided updating the minimum and maximum value contained
	 * <p>
	 * 	The size of the matrix must be contained on the first line and
	 *  the matrix must be defined afterwards line by line separating each element by a tabulator
	 * </p>
	 * @param fileName with size and matrix
	 */
	public MatrixOperations(String fileName)  {
		try {
			BufferedReader reader = new BufferedReader(new FileReader(fileName));
			String[] line;
			
			createMatrixOperations(Integer.parseInt(reader.readLine().strip()));
			
			int i = 0;
			int j;
			
			int value;
			int max = Integer.MIN_VALUE;
			int min = Integer.MAX_VALUE;
			
			int columns = 0;
			
			while(reader.ready()) {
				j = 0;
				line = reader.readLine().split("	");
				
				if(line.length!=getSize()) {
					throw new IllegalStateException("The matrix provided has a different size regarding the intitial specified size on the input file");
				}
				
				for(String number: line) {
					value = Integer.parseInt(number);
					
					if(value<min) {
						min = value;
					}
					if(value>max) {
						max = value;
					}
					
					matrix[i][j++] = value;
				}
				i++;
				columns++;
			}
			
			if(columns!=getSize()) {
				throw new IllegalStateException("The matrix provided on the file is not square");
			}
			
			this.maxValue = max;
			this.minValue = min;
			
		} 
		catch(NumberFormatException e) {
			throw new IllegalStateException("There is a wrong provided number insdie the file");
		}
		catch (FileNotFoundException e) {
			throw new IllegalArgumentException("The name of the file provided does not correspond to any existing file");
		}
		catch (IOException e) {
			System.out.println("Error while handling the file");
		}
	}
	
	private void createMatrixOperations(int n) {
		if(n<1) {
			throw new IllegalArgumentException("The square matrix to be created cannot have a size lower than 1");
		}
		
		matrix = new int[n][n];
	}
	
	/**
	 * Returns the size of the matrix
	 * @return size
	 */
	public int getSize() {
		return matrix.length;
	}
	
	/**
	 * Prints on the console a graphical representation of the matrix
	 */
	public void write() {
		for(int i=0; i<getSize(); i++) {
			for(int j=0; j<getSize(); j++) {
				System.out.print(matrix[i][j] + "	");
			}
			System.out.println();
		}
	}
	
	/**
	 * Sums the elements on the diagonal with a quadratic complexity
	 * @return summation of the elements on the matrix diagonal
	 */
	public int sumDiagonal1() {
		int sumation = 0;
		for(int i=0; i<getSize(); i++) {
			for(int j=0; j<getSize(); j++) {
				if(i==j) {
					sumation += matrix[i][j];
				}
			}
		}
		return sumation;
	}
	
	/**
	 * Sums the elements on the diagonal with a linear complexity
	 * @return summation of the elements on the matrix diagonal
	 */
	public int sumDiagonal2() {
		int sumation = 0;
		for(int i=0; i<getSize(); i++) {
			sumation += matrix[i][i];
		}
		return sumation;
	}
	
	/**
	 * Travels along the matrix whenever the minimum value contained is 1 and the maximum value contained is 4
	 * <p>
	 * 	From the provided element from the matrix given its row and column, if the value is equal to...:
	 * 	<li>
	 * 		<ul> 1; then the path goes up
	 * 		<ul> 2; then the path goes right
	 * 		<ul> 3; then the path goes down
	 * 		<ul> 4; then the path goes left
	 * 	</li>
	 * </p>
	 * 
	 * <p>
	 * 	Traversed element are set to -1 and the process ends whenever the path goes beyond the limits of the matrix or a -1 value is found
	 * </p>
	 * 
	 * @param i ; staring row
	 * @param j ; starting column
	 */
	public void travelPath(int i, int j) {
		if(minValue<MIN_TRAVEL_PATH_VALUE) {
			throw new IllegalStateException("The minimum possible value for the elements on the matrix fot the travel path to be executed is " + MIN_TRAVEL_PATH_VALUE + " and there is at least one element with value " + minValue);
		}
		if(maxValue>MAX_TRAVEL_PATH_VALUE) {
			throw new IllegalStateException("The maximmum possible value for the elements on the matrix fot the travel path to be executed is " + MAX_TRAVEL_PATH_VALUE + " and there is at least one element with value " + maxValue);
		}
		
		if(getSize()<1) {
			throw new IllegalStateException("The travel path cannot be computed since the marix size is 0");
		}
		if(i<0 || i>=getSize()) {
			throw new IllegalArgumentException("The row provided for starting the travel path is not valid");
		}
		if(j<0 || j>=getSize()) {
			throw new IllegalArgumentException("The column provided for starting the travel path is not valid");
		}
		
		printTravelPathMatrixAndMovemenetsNeeded(i, j);
	}

	private void printTravelPathMatrixAndMovemenetsNeeded(int i, int j) {
		int[][] travelPathMatrix = cloneMatrix();
		int next;
		int movements = 0;
		while((next = travelPathMatrix[i][j])!=-1) {
			movements++;
			travelPathMatrix[i][j] = -1;
			
			switch(next) {
				case 1:
					if(i==0) {
						break;
					}
					i--;
					break;
				case 2:
					if(j==getSize()-1) {
						break;
					}
					j++;
					break;
				case 3:
					if(i==getSize()-1) {
						break;
					}
					i++;
					break;
				case 4:
					if(j==0) {
						break;
					}
					j--;
					break;
			}
		}
		
		for(i=0; i<getSize(); i++) {
			for(j=0; j<getSize(); j++) {
				System.out.print(travelPathMatrix[i][j] + "		");
			}
			System.out.println();
		}
		
		System.out.println("Number of movements = " + movements);
	}
	
	private int[][] cloneMatrix(){
		int[][] clone = new int[getSize()][getSize()];
		
		for(int i=0; i<getSize(); i++) {
			for(int j=0; j<getSize(); j++) {
				clone[i][j] = matrix[i][j];
			}
		}
		return clone;
	}
}
