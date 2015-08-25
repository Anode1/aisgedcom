package org.is.ais.gedcom.tworelatives;

public class Relations_en extends RelationsBase{
	
	public Relations_en() throws Exception{
		super();
	}
	
	
	public String[] get(String path1, String path2){
		
		boolean switched=false;
		if(path1.length()>path2.length()){
			String temp=path1;
			path1=path2;
			path2=temp;
			switched=true;
		}
		
		int length1=path1.length();
		int length2=path2.length();
		int diff=length2-length1;
		String first,second;
		
		if(diff==0){
			// (self,self) is treated above and will never come here  
			if(length1==2){ //brothers
				/*
				MX,MX,brother,brother
				MX,FX,brother,sister
				FX,FX,sister,sister
				 */
				if(path1.startsWith("M"))
					first=strings.get("brother");
				else
					first=strings.get("sister");

				if(path2.startsWith("M"))
					second=strings.get("brother");
				else
					second=strings.get("sister");
			}
			else{
				/*
				#cousines on the same level (the same number of X)
				XXX,XXX,cousin,cousin
				XXXX,XXXX,2-cousin,2-cousin
				XXXXX,XXXXX,3-cousin,3-cousin
				X(X*N)X,X(X*N)X,N-cousin,N-cousin
				 */
				StringBuffer sb1=new StringBuffer();
				StringBuffer sb2=new StringBuffer();

				int prefix=length1-2;
				if(prefix>1){
					if(path1.startsWith("M")){
						sb1.append(prefix);
						sb1.append("-");
					}
					else{
						sb1.append(prefix);
						sb1.append("-");						
					}
					if(path2.startsWith("M")){
						sb2.append(prefix);
						sb2.append("-");
					}
					else{
						sb2.append(prefix);
						sb2.append("-");
					}
				}				
				
				sb1.append(strings.get("cousin"));
				sb2.append(strings.get("cousin"));

				first=sb1.toString();
				second=sb2.toString();						
			}//length>2 (diff==0)
		}
		else{ //diff > 0
			if(length1==1){ //direct ancestor
				if(diff==1){
					/*
					M,MM,father,son
					M,FM,father,daughter
					F,MF,mother,son
					F,FF,mother,daughter
					*/
					if(path1.startsWith("M")){
						first=strings.get("father");
					}
					else{
						first=strings.get("mother");
					}
					if(path2.startsWith("M")){
						second=strings.get("son");
					}
					else{
						second=strings.get("daughter");
					}
				}
				else{ //diff>1
					/*
					F,MXF,grandmother,grandson
					F,FXF,grandmother,granddaughter
					M,MXM,grandfather,grandson
					M,FXM,grandfather,granddaughter

					F,MXXF,g.-grandmother,g.-grandson
					F,FXXF,g.-grandmother,g.-granddaughter
					M,MXXM,g.-grandfather,g.-grandson
					M,FXXM,g.-grandfather,g.-granddaughter
					...
					F,MX(X*N)F,<(g.-)*N>grandmother,<(g.-)*N>grandson
					F,FX(X*N)F,<(g.-)*N>grandmother,<(g.-)*N>granddaughter
					M,MX(X*N)M,<(g.-)*N>grandfather,<(g.-)*N>grandson
					M,FX(X*N)M,<(g.-)*N>grandfather,<(g.-)*N>granddaughter
					*/
					StringBuffer sb1=new StringBuffer();
					StringBuffer sb2=new StringBuffer();
					//collect prefix:
					String prefix=strings.get("prefix");
					for(int i=2; i<diff; i++){ //for diff>2
						sb1.append(prefix);
						sb2.append(prefix);
					}
					
					if(path1.startsWith("M")){
						sb1.append(strings.get("grandfather"));
					}
					else{
						sb1.append(strings.get("grandmother"));
					}
					if(path2.startsWith("M")){
						sb2.append(strings.get("grandson"));
					}
					else{
						sb2.append(strings.get("granddaughter"));
					}
					
					first=sb1.toString();
					second=sb2.toString();
				}
			}//end direct ancestors
			else if(length1==2){ //not direct
				/*
				MX,MXX,uncle,nephew
				MX,FXX,uncle,niece
				FX,MXX,aunt,nephew
				FX,FXX,aunt,niece

				MX,MXXX,great-uncle,grand-nephew
				MX,FXXX,great-uncle,grand-niece
				FX,MXXX,great-aunt,grand-nephew
				FX,FXXX,great-aunt,grand-niece

				MX,MXXXX,g.-great-uncle,g.-grand-nephew
				MX,FXXXX,g.-great-uncle,g.-grand-niece
				FX,MXXXX,g.-great-aunt,g.-grand-nephew
				FX,FXXXX,g.-great-aunt,g.-grand-niece
				...
				FX,FXXXX,g.-great-aunt,g.-grand-niece
				...
				MX,MXX(X*N)X,<(g.-)*N>great-uncle,<(g.-)*N>grand-nephew
				MX,FXX(X*N)X,<(g.-)*N>great-uncle,<(g.-)*N>grand-niece
				FX,MXX(X*N)X,<(g.-)*N>great-aunt,<(g.-)*N>grand-nephew
				FX,FXX(X*N)X,<(g.-)*N>great-aunt,<(g.-)*N>grand-niece
				*/
				if(diff==1){	
					if(path1.startsWith("M")){
						first=strings.get("uncle");
					}
					else{
						first=strings.get("aunt");
					}
					if(path2.startsWith("M")){
						second=strings.get("nephew");
					}
					else{
						second=strings.get("niece");
					}
				}
				else{ //diff>=2
					StringBuffer sb1=new StringBuffer();
					StringBuffer sb2=new StringBuffer();
					//collect prefix:
					String prefix=strings.get("prefix");
					for(int i=2; i<diff; i++){ //for diff>2
						sb1.append(prefix);
						sb2.append(prefix);
					}					
					
					if(path1.startsWith("M")){
						sb1.append(strings.get("great-uncle"));
					}
					else{
						sb1.append(strings.get("great-aunt"));
					}
					if(path2.startsWith("M")){
						sb2.append(strings.get("grand-nephew"));
					}
					else{
						sb2.append(strings.get("grand-niece"));
					}
					
					first=sb1.toString();
					second=sb2.toString();					
				}
			}
			else{ // length>2 (and diff > 0)
				/*
				XXX,XXXX,1-cousin once-removed,1-cousin once-removed
				XXX,XXXXX,1-cousin twice-removed,1-cousin twice-removed
				...
				XXXX,XXXXX,2-cousin once-removed,2-cousin once-removed
				XXXX,XXXXXX,2-cousin twice-removed,2-cousin twice-removed
				...
				X(X*N)X,X(X*M)X,N-cousin (M-N) removed
				*/
				StringBuffer sb1=new StringBuffer();
				StringBuffer sb2=new StringBuffer();
				
				int prefix=length1-2;
				if(prefix>1){
					if(path1.startsWith("M")){
						sb1.append(prefix);
						sb1.append("-");
					}
					else{
						sb1.append(prefix);
						sb1.append("-");						
					}
					if(path2.startsWith("M")){
						sb2.append(prefix);
						sb2.append("-");
					}
					else{
						sb2.append(prefix);
						sb2.append("-");
					}
				}					
				
				sb1.append(strings.get("cousin"));
				sb2.append(strings.get("cousin"));
				
				sb1.append(" ");
				sb2.append(" ");
				
				sb1.append(diff+"-removed");
				sb2.append(diff+"-removed");
				
				first=sb1.toString();
				second=sb2.toString();					
			}
		}
		
		if(switched)
			return new String[]{second, first};		
		else
			return new String[]{first, second};
	}		
	

}
