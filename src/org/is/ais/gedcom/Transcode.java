package org.is.ais.gedcom;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Reader;
import java.util.HashMap;


public class Transcode {

	
	public static void transcode(String filePath, Result result) throws Exception{
		
		//required parameters:
		
		String e1=Parameters.getString(Constants.E1);
		if(e1==null){
			result.criticalMessage="Parameter "+Constants.E1+"  required";
			return;
		}
		String e2=Parameters.getString(Constants.E2);
		if(e2==null){
			result.criticalMessage="Parameter "+Constants.E2+"  required";
			return;
		}
		
		String outputFilePath = Util.outputFileFromInput(filePath);		
		
		FileInputStream fis = null;
		FileOutputStream fos = null;
		try{
			fis = new FileInputStream(filePath);
			fos = new FileOutputStream(outputFilePath);
			
			Transcode.transcode(fis, fos, e1, e2);
			
			result.infoMessage="File created: " + outputFilePath;
		}
		finally{
			if(fis!=null)fis.close();
			if(fos!=null)fos.close();
		}
		
		
	}
	

	public static void transcode(InputStream is, OutputStream os, String encodingIn, String encodingOut) throws Exception{
	
		CodePage currentCodePage=null; //used also as a flag (whether to use custom encoding)
		
		if(Parameters.getAsBoolean(Constants.TO_TRANSLIT)){
			currentCodePage=codePage; //implement here multiple code pages
		}
		
		Reader in = new BufferedReader(new InputStreamReader(is, encodingIn));
		PrintWriter pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(os, encodingOut)));
        int ch;
        while ((ch = in.read()) > -1){
        	if(currentCodePage!=null){
        		String substitute = currentCodePage.getEnglishSubstitute(Integer.toString(ch));
        		if(substitute==null){
        			pw.append((char)ch);
        		}
        		else{
        			pw.append(substitute);
        		}
        	}
        	else{ //use default
        		pw.append((char)ch);
        	}
        }
        
        in.close();
        pw.close();
	}
	
	
	//private static HashMap codePages = new HashMap();
	static CodePage codePage = new CodePage();
	
	static{
		//CodePage codePage = new CodePage();
		//codePages.put("windows-1251", codePage);
		
		codePage.map.put("1025", "Yo");		
		
		codePage.map.put("1105", "yo");
		
		codePage.map.put("1040", "A");
		codePage.map.put("1041", "B");
		codePage.map.put("1042", "V");
		codePage.map.put("1043", "G");
		codePage.map.put("1044", "D");
		codePage.map.put("1045", "E");
		codePage.map.put("1046", "Zh");
		codePage.map.put("1047", "Z");
		codePage.map.put("1048", "I");
		codePage.map.put("1049", "J");
		codePage.map.put("1050", "K");
		codePage.map.put("1051", "L");
		codePage.map.put("1052", "M");
		codePage.map.put("1053", "N");
		codePage.map.put("1054", "O");
		codePage.map.put("1055", "P");
		codePage.map.put("1056", "R");
		codePage.map.put("1057", "S");
		codePage.map.put("1058", "T");
		codePage.map.put("1059", "U");
		codePage.map.put("1060", "F");
		codePage.map.put("1061", "H");
		codePage.map.put("1062", "C");
		codePage.map.put("1063", "Ch");
		codePage.map.put("1064", "Sh");
		codePage.map.put("1065", "Shh");
		codePage.map.put("1066", "");
		codePage.map.put("1067", "Y");
		codePage.map.put("1068", "'");
		codePage.map.put("1069", "Je");
		codePage.map.put("1070", "Yu");
		codePage.map.put("1071", "Ya");
		
		codePage.map.put("1072", "a");
		codePage.map.put("1073", "b");
		codePage.map.put("1074", "v");
		codePage.map.put("1075", "g");
		codePage.map.put("1076", "d");
		codePage.map.put("1077", "e");
		codePage.map.put("1078", "zh");
		codePage.map.put("1079", "z");
		codePage.map.put("1080", "i");
		codePage.map.put("1081", "j");
		codePage.map.put("1082", "k");
		codePage.map.put("1083", "l");
		codePage.map.put("1084", "m");
		codePage.map.put("1085", "n");
		codePage.map.put("1086", "o");
		codePage.map.put("1087", "p");
		codePage.map.put("1088", "r");
		codePage.map.put("1089", "s");
		codePage.map.put("1090", "t");
		codePage.map.put("1091", "u");
		codePage.map.put("1092", "f");
		codePage.map.put("1093", "h");
		codePage.map.put("1094", "c");
		codePage.map.put("1095", "ch");
		codePage.map.put("1096", "sh");
		codePage.map.put("1097", "shh");
		codePage.map.put("1098", "");
		codePage.map.put("1099", "y");
		codePage.map.put("1100", "'");
		codePage.map.put("1101", "je");
		codePage.map.put("1102", "yu");
		codePage.map.put("1103", "ya");		
		
	}
}


class CodePage{
	HashMap map=new HashMap();
	
	String getEnglishSubstitute(String charAsInt){
		String english=(String)map.get(charAsInt);
		return english;
	}
}

