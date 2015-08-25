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

import javax.swing.Icon;
import javax.swing.ImageIcon;

import java.util.Hashtable;


/**
 * Small Icons caching repository for local images used by the program such as icons,
 * splash screen image etc. Assumed that the total size is small enough, i.e. they
 * are kept in memory. LRUCache should be used instead - if this size will be big
 * (when the application will contain a big number of images, logos etc) or better
 * in that case - not to use cache at all
 * Works under application and applet contexts (uses the current class loader)
 */
public class Images{

	/**
	 * Cache for images
	 */
	private static Hashtable cache=new Hashtable();

	/**
	 * Tries to get the icon from the cache. If not found - creates it, putting
	 * into the cache for subsequent use;
	 */
	public static Icon getIcon(String name){

		try{
			Icon icon = (Icon)cache.get(name);
			if(icon != null){
				return icon;
			}

			icon=createIcon(name);
			cache.put(name, icon);
			return icon;
		}
		catch(Exception e){
			//log.error("", e);
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Creates an icon not saving it in the cache;
	 */
	private static Icon createIcon(String name) throws Exception{

		return new ImageIcon("images/"+name);
	}


}
