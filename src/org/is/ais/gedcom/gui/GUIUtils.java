/*
 	Copyright (C) 2009 Vasili Gavrilov

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
package org.is.ais.gedcom.gui;

import java.awt.Component;

import java.awt.Point;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Window;
import java.awt.Dialog;
import java.awt.Toolkit;
import java.awt.Color;
import java.awt.Font;

/**
 * Utilities class - simple set of useful static functions which are used by
 * different classes from different packages. Mostly GUI utilities.
 */
public class GUIUtils{

	/**
	 * Puts component to be centralized relative to the parent
	 */
	public static void setCentalizedLocationRelativeMe(Component parent, Component me){

		if(parent==null)return;

		Point parentLoc=parent.getLocation();
		int x=0,y=0; //my future coordinates

		int parentX=parentLoc.x;
		int parentY=parentLoc.y;
		int parentW=parent.getSize().width;
		int parentH=parent.getSize().height;

		int w=me.getSize().width;
		int h=me.getSize().height;

		if(w<parentW)x=parentX+(parentW-w)/2;
		else x=parentX-(w-parentW)/2;

		if(h<parentH)y=parentY+(parentH-h)/2;
		else y=parentY-(h-parentH)/2;

		me.setLocation(x,y);
	}

	/**
	 * Puts the component into the center of the screen
	 */
	public static void setCentalizedLocation(Component me){

		int x=0,y=0; //my future coordinates
		Dimension dim=Toolkit.getDefaultToolkit().getScreenSize();
		int screenW=dim.width;
		int screenH=dim.height;

		int w=me.getSize().width;
		int h=me.getSize().height;

		if(w<screenW) x=(screenW-w)/2;
		else x=0;

		if(h<screenH) y=(screenH-h)/2;
		else y=0;

		me.setLocation(x,y);
//		setLocation((screen.width - getSize().width) / 2,	(screen.height - getSize().height) / 2);
	}


	/**
	 * Returns the main parent frame for the component
	 */
	public static Frame getParentFrame(Component c){

		while(c.getParent()!=null){
			c=c.getParent();
		}
		return (Frame)c;
	}

	/**
	 * Returns the nearest Window for the component
	 */
	public static Window getNearestWindow(Component c){

		while(c!=null && !(c instanceof Window)){
			c=c.getParent();
		}
		return (Window)c;
	}

	/**
	 * Returns the nearest frame for the component
	 */
	public static Frame getNearestFrame(Component c){

		while(c!=null && !(c instanceof Frame)){
			c=c.getParent();
		}
		return (Frame)c;
	}

	/**
	 * Returns the nearest Dialog for the component
	 */
	public static Dialog getNearestDialog(Component c){

		while(c!=null && !(c instanceof Dialog)){
			c=c.getParent();
		}
		return (Dialog)c;
	}

	/**
	 * Prints all parents of the component (for debugging)
	 */
	public static String printParents(Component c){

		StringBuffer sb=new StringBuffer("");

		while(c.getParent()!=null){
			sb.append(c.getName());
			sb.append("/");
			c=c.getParent();
		}
		sb.append(c.getName());
		return sb.toString();
	}

	/**
	 * Changes the cursor. This function is a warkaround for JSplitPane bug
	 */
	/*
  public static void makeWaitCursor(Container c, boolean wait_cur){

      java.awt.Component components[] = c.getComponents();
      java.awt.Component current;

      for (int i = 0; i < components.length; i++){

        current=components[i];
        current.addMouseListener(new MouseAdapter() {}); //to walkaround cursor bug

        if(wait_cur){
          current.setCursor(new Cursor(Cursor.WAIT_CURSOR));
        }
        else{
          current.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        }

        current.repaint();

	      if(current instanceof Container){
	        makeWaitCursor((Container)current, wait_cur);
	      }
      }//for
  }
	 */

	/**
	 * Dumps (prints out) current memory state (VM total/free) to stdout
	 */
	public static void sample(){

		System.out.println("-----------------Sampling------------------");
		long total=Runtime.getRuntime().totalMemory();
		long free=Runtime.getRuntime().freeMemory();
		System.out.println("Total memory:"+total);
		System.out.println("Free memory:"+free);
		System.out.println("Percentage available:"+(int)(((double)free / (double)total) * (double)100)+"%");
		//System.out.println("Max Memory:"+Runtime.getRuntime().mamaxMemory());
	}

	/*
  public static String extractVersion(String idString){

      return
  }
	 */


	/**
	 * Makes Color objects from the string specified according to [CSS2-color]
	 * Fixed currently supported: black|blue|cyan|darkGray|gray|green|lightGray|magenta|orange|pink|red|white|yellow
	 * or you can define as rgb format: #rrggbb
	 *//*
  public static Color str2Color(String s){

    Color color=null;

    if(s.startsWith("#")){
       return fromRGB(s);
    }
    else if(s.equals("black")){
       color=color.black;
    }
    else if(s.equals("blue")){
       color=color.blue;
    }
    else if(s.equals("cyan")){
       color=color.cyan;
    }
    else if(s.equals("darkGray")){
       color=color.darkGray;
    }
    else if(s.equals("gray")){
       color=color.gray;
    }
    else if(s.equals("green")){
       color=color.green;
    }
    else if(s.equals("lightGray")){
       color=color.lightGray;
    }
    else if(s.equals("magenta")){
       color=color.magenta;
    }
    else if(s.equals("orange")){
       color=color.orange;
    }
    else if(s.equals("pink")){
       color=color.pink;
    }
    else if(s.equals("red")){
       color=color.red;
    }
    else if(s.equals("white")){
       color=color.white;
    }
    else if(s.equals("yellow")){
       color=color.yellow;
    }
    return color;
  }
	  */
	private static Color fromRGB(String tag){

		Color color=null;

		try{
			String rgb=tag.substring(1);
			int r=Integer.parseInt(rgb.substring(0,2),16);
			int g=Integer.parseInt(rgb.substring(2,4),16);
			int b=Integer.parseInt(rgb.substring(4,6),16);
			return new Color(r,g,b);
		}
		catch(Exception e){
			//LogManager.err(e);
		}
		return color;
	}

	public final static Font deriveFont(Font sample, String family, String style, String sizeStr) throws Exception{

		int size;

		if(sizeStr==null){
			size=sample.getSize();
		}
		else{
			size=Integer.parseInt(sizeStr);
		}

		int fs;
		if(style==null){
			fs=sample.getStyle();
		}
		else if(style.equals("bold")){
			fs=Font.BOLD;
		}
		else if(style.equals("italic")){
			fs=Font.ITALIC;
		}
		else if(style.equals("bold_italic")){
			fs=(Font.ITALIC|Font.BOLD);
		}
		else if(style.equals("plane")){
			fs=Font.PLAIN;
		}
		else{
			throw new Exception("Font style:"+style+" does not exist");
		}

		String ff=null;
		if(family==null){
			ff=sample.getName();
		}
		else{
			ff=family;
		}

		return new Font(ff,fs,size);
	}

	public static void main(String[] args){

		java.util.Properties props=new java.util.Properties();
		props.put("aaa","bbb");    props.put("bbb","bbb");    props.put("c","bbb");
		props.put("Properties.h1","h1value");    props.put("Properties.h2","h2value");

		java.util.Properties servletSpecificProps=new java.util.Properties();
		String fullClassName=servletSpecificProps.getClass().getName();
		String justName=fullClassName.substring(fullClassName.lastIndexOf(".")+1);
		String thisServletPrefix=justName+".";
		// System.out.println("thisServletPrefix="+thisServletPrefix);
		int indFrom=thisServletPrefix.length();
		for(java.util.Enumeration en = props.keys(); en.hasMoreElements() ;) {
			String aKey=(String)en.nextElement();
			if(!aKey.startsWith(thisServletPrefix))continue;
			Object aValue=props.get(aKey);
			String newKey=aKey.substring(indFrom);
			servletSpecificProps.put(newKey,aValue);
		}

		System.out.println(servletSpecificProps);

	}

}//Utils
