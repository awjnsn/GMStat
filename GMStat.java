/*
Copyright (c) 2016 Alex Johnson

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 
To make an input file, use https://github.com/cdzombak/groupme-tools
Run the groupme-fetch.py tool to get a transcript, then process it into a text file with simple-transcript.py
*/

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

public class GMStat {
	public static void main(String[] args){
		if(args.length == 0){
			System.out.println("Usage: GMStat <filename>");
			System.exit(1);
		}
		else{
			File f = new File(args[0]);
			ArrayList<String> names = new ArrayList<String>();
			ArrayList<Name> single = new ArrayList<Name>();
			int total = 0;
			String output = "";
			try{
				Scanner sc = new Scanner(f);
				while(sc.hasNextLine()){
					String s = sc.nextLine();
					if(s.contains("(20") && !s.contains("(SYS)")){
						names.add(s.substring(0,s.indexOf("(")));
					}
				}
				sc.close();
				for(int i = 0; i < names.size(); i++){
					single.add( new Name(names.get(i), 1) );
				}
				for(int i = 0; i < names.size(); i++){
					for(int k = 0; k < single.size(); k++){
						if(names.get(i).equals(single.get(k).getName())){
							single.get(k).increment();
						}
					}
				}
				names.clear();
				for(int i = 0; i < single.size(); i++){
					if(!(names.contains(single.get(i).toString()))){
						names.add(single.get(i).toString());
						total += single.get(i).getCount();
					}
				}
				for(int i = 0; i < names.size(); i++){
					int n = Integer.parseInt(names.get(i).substring(names.get(i).lastIndexOf(" ") + 1, names.get(i).length()));
					names.set(i, names.get(i) +" (" + (double)n / total * 100.0 + "%)");
				}
				for(int i = 0; i < names.size(); i++){
					output += names.get(i) + "\n";
				}
				System.out.println("Total: " + total + "\n" + output);
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
	}
}

class Name{
	String myName = "";
	int myCount = 0;
	
	public Name(String name, int count){
		myName = name;
		myCount = count;
	}
	
	public void increment(){
		myCount++;
	}
	
	public String getName(){
		return myName;
	}
	
	public int getCount(){
		return myCount;
	}
	
	public String toString(){
		return myName + ": " + myCount;
	}
}