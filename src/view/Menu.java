package view;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedHashMap;
import java.util.Scanner;
import java.util.Set;

import global.*;



public class Menu {

	private LinkedHashMap<String, String> menu = new LinkedHashMap();
	private int lengthLongestOption;
	
	
	public Menu(String filename) throws Exception {

		if (!loadMenu(filename)) {
			throw new Exception("Menu load failure for filename " + filename);
		}
		
		lengthLongestOption = longestOption(menu.keySet());
	}
	
	
	public boolean ignoreLine(String line) {
		return line.length() == 0 || line.charAt(0) == '#';
	}
	
	
	public boolean keyValueLine(String line) {
		return line.indexOf('=') != 0;
	}
	
	
	// open a file and load in a set of key value pairs
	// each line is of the form 
	// menuchoice = message
	//
	// spacing is irrelavent
	// comment lines begin with a #
	// blank lines are ignored.
	// returns true on success
	// false on any failure
	
	
	
	public boolean loadMenu(String filename) {
		File file = new File(filename);
		Scanner sc;
		
		try {
			sc = new Scanner(file);
		} catch (FileNotFoundException fnfe) {
			System.out.println("File not found");
			return false;
		}
		
		boolean optionsFound = false;
		
		while (sc.hasNextLine()) {
			String line = sc.nextLine();
			
			if (!ignoreLine(line)) {
				
				// Extract the key/value parts
				String[] components = line.split("=");
				menu.put(components[0].trim(), components[1].trim());
				optionsFound = true;
			}
		}
		sc.close();
		return optionsFound;
	}
	
	// is the chosen string one of the options?
	private boolean caseIgnoreContains(Set<String> options, String chosen) {
		for (String option: options) {
			if (option.equalsIgnoreCase(chosen)) {
				return true;
			}
		}
		return false;
	}
	
	
	// display the menu, keep prompting until one of the options is chosen.
	// return the option.
	// case insensitive.
	
	public String show() {
		String format = "%-" + Integer.toString(lengthLongestOption) + "s: ";
		Set<String> keys = menu.keySet();
		String choice;
		
		do {
			for (String option : keys) {
				System.out.printf(format, option);
				System.out.println(menu.get(option));
			}
			System.out.print("Choice :");
			choice = Global.scanner.nextLine();
			System.out.println();
		} while (!caseIgnoreContains(keys, choice));
		
		return choice;
	}
	
	
	// what is the length of the longest option?
	// use this for formatting
	
	public int longestOption(Set<String> options) {
		int max = 0;
		
		for (String option: options) {
			if (option.length() > max) {
				max = option.length();
			}
		}
		
		return max;
	}
}