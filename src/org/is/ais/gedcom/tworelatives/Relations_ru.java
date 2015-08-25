package org.is.ais.gedcom.tworelatives;

public class Relations_ru extends RelationsBase{
	
	
	public Relations_ru() throws Exception{
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

				int prefix=length1-1;
				if(path1.startsWith("M")){
					sb1.append(prefix);
					sb1.append("-");
					sb1.append(strings.get("yurodniy")+" ");
				}
				else{
					sb1.append(prefix);
					sb1.append("-");						
					sb1.append(strings.get("yurodnaya")+" ");
				}
				if(path2.startsWith("M")){
					sb2.append(prefix);
					sb2.append("-");
					sb2.append(strings.get("yurodniy")+" ");
				}
				else{
					sb2.append(prefix);
					sb2.append("-");
					sb2.append(strings.get("yurodnaya")+" ");
				}
				
				if(path1.startsWith("M"))
					sb1.append(strings.get("brother"));
				else
					sb1.append(strings.get("sister"));
				
				if(path2.startsWith("M"))
					sb2.append(strings.get("brother"));
				else
					sb2.append(strings.get("sister"));				

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

					sb1.append(getPraPrefix(diff-2));
					sb2.append(getPraPrefix(diff-2));
					
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
			else{  //not direct
				if(diff==1){ //uncles
					/*
					MX,MXX,uncle,nephew
					MX,FXX,uncle,niece
					FX,MXX,aunt,nephew
					FX,FXX,aunt,niece
					*/
					if(length1==2){
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
					else{ //length1>2 (diff==1)
						/*
						M(X*N)X , M(X*N)XX = N+1-yurodniy dyadya, N+1-yurodniy plemyannik
						M(X*N)X , F(X*N)XX = N+1-yurodniy dyadya, N+1-yurodnaya plemyannica
						F(X*N)X , M(X*N)XX = N+1-yurodnaya tyotya, N+1-yurodniy plemyannik
						F(X*N)X , F(X*N)XX = N+1-yurodnaya tyotya, N+1-yurodnaya plemyannica
						*/						
						StringBuffer sb1=new StringBuffer();
						StringBuffer sb2=new StringBuffer();
						int prefix=length1-1;
						
						sb1.append(prefix);
						sb1.append("-");
						
						sb2.append(prefix);
						sb2.append("-");	

						if(path1.startsWith("M")){
							sb1.append(strings.get("yurodniy")+" ");
							sb1.append(first=strings.get("uncle"));
						}
						else{
							sb1.append(strings.get("yurodnaya")+" ");
							sb1.append(first=strings.get("aunt"));
						}
						if(path2.startsWith("M")){
							sb2.append(strings.get("yurodniy")+" ");
							sb2.append(first=strings.get("nephew"));
						}
						else{
							sb2.append(strings.get("yurodnaya")+" ");
							sb2.append(first=strings.get("niece"));
						}
						
						first=sb1.toString();
						second=sb2.toString();	
					}
				}
				else{//diff>1
					/*
					M-N=2 
					M(X*N)X , M(X*M)X = (N+2)-yurodniy ded, (N+1)-yurodniy vnuchatiy plemyannik
					M(X*N)X , F(X*M)X = (N+2)-yurodniy ded, (N+1)-yurodnaya vnuchataya plemyannica
					F(X*N)X , M(X*M)X = (N+2)-yurodnaya babka, (N+1)-yurodniy vnuchatiy plemyannik
					F(X*N)X , F(X*M)X = (N+2)-yurodnaya babka, (N+1)-yurodnaya vnuchataya plemyannica
					
					M(X*N)X , M(X*M)X = (N+2)-yurodniy (M-N-2)-raza pra- ded, (N+1)-yurodniy (M-N-2)-raza vnuchatiy plemyannik
					M(X*N)X , F(X*M)X = (N+2)-yurodniy (M-N-2)-raza pra- ded, (N+1)-yurodnaya (M-N-2)-raza vnuchataya plemyannica
					F(X*N)X , M(X*M)X = (N+2)-yurodniy (M-N-2)-raza pra- babka, (N+1)-yurodniy (M-N-2)-raza vnuchatiy plemyannik
					F(X*N)X , F(X*M)X = (N+2)-yurodniy (M-N-2)-raza pra- babka, (N+1)-yurodnaya (M-N-2)-raza vnuchataya plemyannica

					*/
					StringBuffer sb1=new StringBuffer();
					StringBuffer sb2=new StringBuffer();
					
					int prefix=length1;
					sb1.append(prefix);
					sb1.append("-");
					
					if(path1.startsWith("M")){
						sb1.append(strings.get("yurodniy")+" ");
					}
					else{
						sb1.append(strings.get("yurodnaya")+" ");
					}

					if(prefix>2){
						sb2.append(prefix-1);
						sb2.append("-");					
						if(path2.startsWith("M")){
							sb2.append(strings.get("yurodniy")+" ");
						}
						else{
							sb2.append(strings.get("yurodnaya")+" ");
						}
					}
					
					int n=diff-2;
					sb1.append(getPraPrefix(n)); //will be "" - if n=0
					sb2.append(getPraPrefix(n)); //will be "" - if n=0		
					
					if(path1.startsWith("M")){
						sb1.append(strings.get("grandfather"));
					}
					else{
						sb1.append(strings.get("grandmother"));
					}
					if(path2.startsWith("M")){
						sb2.append(first=strings.get("vnuchatiy"));
						sb2.append(" ");
						sb2.append(first=strings.get("nephew"));
					}
					else{
						sb2.append(first=strings.get("vnuchataya"));
						sb2.append(" ");
						sb2.append(first=strings.get("niece"));
					}
					
					first=sb1.toString();
					second=sb2.toString();					
				}

			}
		}
		
		if(switched)
			return new String[]{second, first};		
		else
			return new String[]{first, second};
	}
	
	
	private String getPraPrefix(int n){
		
		if(n==0){
			return "";
		}
		else{
			//collect prefix:
			String pra=strings.get("prefix");
			StringBuffer praBuffer=new StringBuffer();
			
			if(n==1){ //praded
				praBuffer.append(pra);
			}
			else if(n==2){ //prapraded
				praBuffer.append(pra);
				praBuffer.append(pra);
			}
			else if(n==3){ //praprapraded
				praBuffer.append(pra);
				praBuffer.append(pra);
				praBuffer.append(pra);
			}
			else{ //n>3
				//(4 pra-) ded
				praBuffer.append("(");
				String nAsString=Integer.toString(n);
				praBuffer.append(nAsString);
				praBuffer.append(" ");
				
				if(nAsString.endsWith("2") || nAsString.endsWith("3") || nAsString.endsWith("4")){
					praBuffer.append(strings.get("raza"));	
				}
				else{
					praBuffer.append(strings.get("raz"));
				}
				
				praBuffer.append(" ");
				praBuffer.append(pra);
				praBuffer.append("-) ");
			}
			return praBuffer.toString();
		}
	}
	

}
