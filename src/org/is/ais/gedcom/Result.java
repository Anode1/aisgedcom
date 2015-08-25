package org.is.ais.gedcom;


/**
 * Context (used by both gui and command line versions). So, we always have one message rather than a list
 *
 */
public class Result {

	public String criticalMessage; //shown in GUI status line and in command line version at the end
	public String infoMessage;

}
