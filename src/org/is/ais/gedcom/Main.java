/*
 	Copyright (C) 2011 Vasili Gavrilov

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/
package org.is.ais.gedcom;

/**
 * Main entry point
 */
public class Main {

	
	public static void main(String[] args) {
		
		Result result=new Result();
		try{
			long t0=System.currentTimeMillis();
			
			Parameters.getInstance().clear();

			if(new ArgsParser().setArgs(args, Parameters.getInstance()) != 0)
				return; //either we have an error in arguments or help asked to be printed
			
			Processor.process(result);
			if(result.criticalMessage!=null){
				System.out.println(result.criticalMessage);
				System.out.println();
			}
			else if (result.infoMessage!=null){
				System.out.println(result.infoMessage);
				System.out.println();
			}
			else{
				System.out.println("Processed in " + (System.currentTimeMillis()-t0) + " ms.");
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}

}



