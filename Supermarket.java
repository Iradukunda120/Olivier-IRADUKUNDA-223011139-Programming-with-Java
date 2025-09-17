import java.util.Scanner;


public class Supermarket {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		 Scanner input = new Scanner(System.in);
		 System.out.print("Enter number of different items: ");
	        int n = input.nextInt();
	        input.nextLine();
	        String[] itemNames = new String[n];
	        double[] prices = new double[n];
	        int[] quantities = new int[n];
	        double[] subtotals = new double[n];

	        double total = 0;
	        for (int i = 0; i < n; i++) {
	            System.out.println("\nEnter details for item " + (i + 1) + ":");

	            System.out.print("Item name: ");
	            itemNames[i] = input.nextLine();

	            System.out.print("Price per unit: ");
	            prices[i] = input.nextDouble();

	            System.out.print("Quantity purchased: ");
	            quantities[i] = input.nextInt();
	            input.nextLine(); 

	           
	            subtotals[i] = prices[i] * quantities[i];
	            total += subtotals[i];
	        }

	        double discount = 0;
	        if (total > 50000) {
	            discount = total * 0.05;
	        }
	        double finalAmount = total - discount;

	        
	        System.out.println("\n========= SUPERMARKET RECEIPT =========");
	        System.out.println("Item\t\tQty\tPrice\tSubtotal");

	        for (int i = 0; i < n; i++) {
	            System.out.println(itemNames[i] + "\t\t" 
	                + quantities[i] + "\t" 
	                + prices[i] + "\t" 
	                + subtotals[i]);
	        }

	        System.out.println("----------------------------------------");
	        System.out.println("Grand Total: " + total);
	        System.out.println("Discount: " + discount);
	        System.out.println("Final Amount Payable: " + finalAmount);
	        System.out.println("========================================");
	    }
	


	}


