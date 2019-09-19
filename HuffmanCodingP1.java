package ut1;

import java.util.Scanner;

public class Huffman {
	public static void main(String[] args) {
		int[] frequencies = new int[256];
		Scanner scan = new Scanner(System.in);
		String toSort = scan.nextLine();
		int temp = 0;
		int highest = -1;
		scan.close();
	
		for(int i = 0; i < toSort.length(); i++) {
			frequencies[(int)toSort.charAt(i)]++;			
		}
		do {
			highest = 0;
			for(int i = 0; i < frequencies.length; i++) {
				if(frequencies[i] > highest) {
					highest = frequencies[i];
					temp = i;
				}
			}
			if(temp != 0)
				System.out.print((char)temp);
			frequencies[temp] = 0;
			temp = 0;
		}
		while(highest != 0);
	}
}