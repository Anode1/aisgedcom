package org.is.ais.gedcom.tests;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import junit.framework.TestCase;

import org.is.ais.gedcom.Constants;
import org.is.ais.gedcom.Counter;
import org.is.ais.gedcom.DateUtil;
import org.is.ais.gedcom.FamilyLinesComparator;
import org.is.ais.gedcom.Main;
import org.is.ais.gedcom.Parameters;
import org.is.ais.gedcom.Statistics;
import org.is.ais.gedcom.Strings;
import org.is.ais.gedcom.Transcode;
import org.is.ais.gedcom.Util;
import org.is.ais.gedcom.atree.AtreeParser;
import org.is.ais.gedcom.atree.AtreeProcessor;
import org.is.ais.gedcom.atree.Data;
import org.is.ais.gedcom.atree.Dates;
import org.is.ais.gedcom.atree.Sequence;
import org.is.ais.gedcom.tworelatives.CodeMap;
import org.is.ais.gedcom.tworelatives.Encodings;
import org.is.ais.gedcom.tworelatives.RelationsMap;


/**
 * All regression tests are here 
 */
public class Tests extends TestCase{


	public Tests(String name) throws Exception{
	    super(name);
	}
	
	
	public void testPath2FamilyLine(){
	
		assertEquals("M", Util.path2familyLine("M")); 
		assertEquals("M", Util.path2familyLine("MM"));
		assertEquals("MF", Util.path2familyLine("MF"));
		assertEquals("MFMFMFM", Util.path2familyLine("MFMFMFM"));
		assertEquals("MFMFMFM", Util.path2familyLine("MFMFMFMM"));
		assertEquals("F", Util.path2familyLine("FFFFFFFFFFFFFFF"));
		assertEquals("MFFFFMF", Util.path2familyLine("MFFFFMF"));
	}
	

	public void testDates(){
		assertEquals(1889, DateUtil.parseYear("14 MAY 1889", false));
		assertEquals(1883, DateUtil.parseYear("9 APR 1883", false));
		assertEquals(1889, DateUtil.parseYear("MAY 1889", false));
		assertEquals(1889, DateUtil.parseYear("1889", false));
		assertEquals(1810, DateUtil.parseYear("ABT 1810", false));
		assertEquals(1789, DateUtil.parseYear("BEF 1789", false));
		assertEquals(1834, DateUtil.parseYear("AFT 1834", false));
		assertEquals(0, DateUtil.parseYear("ABT 1810", true));
		assertEquals(0, DateUtil.parseYear("BEF 1789", true));
		assertEquals(0, DateUtil.parseYear("AFT 1834", true));		

		assertEquals(200, DateUtil.parseYear("14 MAY 1811", false), DateUtil.thisYear);
	
		assertEquals(19, DateUtil.year2Century(1800));
		assertEquals(19, DateUtil.year2Century(1859));
		assertEquals(20, DateUtil.year2Century(1999));
		assertEquals(13, DateUtil.year2Century(1200));
	}

	
	/*
	public void testMonths(){
		Calendar from=Calendar.getInstance();
		from.set(2000, 12, 22);
		Calendar to=Calendar.getInstance();
		to.set(2001, 12, 21);
		
		assertEquals(11, DateUtil.monthsBetween(from, to));
		
		from.set(2000, 2, 12); to.set(2001, 3, 12);
		assertEquals(13, DateUtil.monthsBetween(from, to));
		
		from.set(2000, 1, 30); to.set(2005, 3, 31);
		assertEquals(62, DateUtil.monthsBetween(from, to));		
	}
	*/
	
	/*
	public void test() throws Exception{
		byte[] iso = "Рязанская губерния, Рязанский уезд".getBytes("UTF-8");
		//byte[] utf8 = new String(iso, "ISO-8859-1").getBytes("ISO-8859-1");
		String res=new String(iso, "UTF-8");  
		PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("test.txt", false)));			
		//out.println(s);
		out.println(res);
		//System.out.println(res);
		out.close();
	}*/
	
			
	public void testLines2Binary(){
		assertEquals(Util.lines2binary("M"), 1);
		assertEquals(Util.lines2binary("MM"), 2);
		assertEquals(Util.lines2binary("MF"), 3);
		assertEquals(Util.lines2binary("MMM"), 4);
		assertEquals(Util.lines2binary("MMF"), 5);
		assertEquals(Util.lines2binary("MFM"), 6);
		assertEquals(Util.lines2binary("MFF"), 7);
		assertEquals(Util.lines2binary("MMMMMMMM"), 128);
		assertEquals(Util.lines2binary("F"), 1);
		assertEquals(Util.lines2binary("FM"), 2);
		assertEquals(Util.lines2binary("FF"), 3);
		assertEquals(Util.lines2binary("FMM"), 4);
		assertEquals(Util.lines2binary("FMF"), 5);
		assertEquals(Util.lines2binary("FFM"), 6);
		assertEquals(Util.lines2binary("FFF"), 7);
		assertEquals(Util.lines2binary("FMMMMMMM"), 128);

		//System.out.println(Util.lines2binary("FFFMFMFMMMFMMMFFFMFMFMMMFMMFFFFMFFMMFM"));
		
		ArrayList view = new ArrayList();
		view.add("FFFMFMFMMMFMMMFFFMFMFMMMFMMFFFFMFFMMFM");view.add("MM"); view.add("F"); view.add("FF");view.add("M");view.add("MF");view.add("MMF");
		Collections.sort(view, new FamilyLinesComparator());
		//System.out.println(view);
		assertEquals("M", view.get(0));
		assertEquals("F", view.get(1));
		assertEquals("MM", view.get(2));
		assertEquals("MF", view.get(3));
		assertEquals("FF", view.get(4));
		assertEquals("MMF", view.get(5));
		assertEquals("FFFMFMFMMMFMMMFFFMFMFMMMFMMFFFFMFFMMFM", view.get(6));
	}
	
	
	public void testModesCollector(){
		
		HashMap counter=new HashMap();
		counter.put(1, new Counter(3));
		counter.put(2, new Counter(10));
		counter.put(5, new Counter(9));
		counter.put(6, new Counter(10));
		counter.put(10, new Counter(1));
		
		ArrayList result = Statistics.findMostFrequent(counter);
		assertEquals("[2, 6]", result.toString());
		
		counter=new HashMap();
		counter.put(1, new Counter(3));
		counter.put(2, new Counter(11));
		result = Statistics.findMostFrequent(counter);
		assertEquals("[2]", result.toString());
		
		counter=new HashMap();
		counter.put(1, new Counter(3));
		result = Statistics.findMostFrequent(counter);
		assertEquals("[1]", result.toString());		
	}

	
	public void testLangs() throws Exception{
		Strings strings=new Strings();
		assertEquals("mother", strings.get("mother"));
		Parameters.getInstance().put(Constants.LANG_KEY, "ru_Windows-1251");
		strings=new Strings(); //load again, now not default language
		
		//PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream("test.txt"), CodeMap.getCode())));
		//out.println(strings.get("mother"));
		//out.close();
		
		//assertEquals("РјР°С‚СЊ", strings.get("mother"));
		
		Parameters.getInstance().put(Constants.LANG_KEY, "en"); //back to English
	}
	
	
	public void testRelationsMapEn() throws Exception{
		RelationsMap map=new RelationsMap();
		//default language (English)
		assertEquals("father, son", map.getString("M", "MM"));
		assertEquals("son, father", map.getString("MM", "M"));
		assertEquals("mother, son", map.getString("F", "MF"));
		assertEquals("daughter, mother", map.getString("FF", "F"));
		assertEquals("brother, brother", map.getString("MM", "MM"));
		assertEquals("sister, brother", map.getString("FF", "MF"));		
		assertEquals("grandfather, grandson", map.getString("M", "MMM"));
		assertEquals("grandson, grandfather", map.getString("MMM", "M"));
		assertEquals("grandfather, granddaughter", map.getString("M", "FXM"));
		assertEquals("g.-grandfather, g.-granddaughter", map.getString("M", "FXXM"));
		assertEquals("g.-g.-g.-grandfather, g.-g.-g.-granddaughter", map.getString("M", "FXXMXM"));
		assertEquals("nephew, uncle", map.getString("MXX", "MX"));
		assertEquals("aunt, niece", map.getString("FX", "FXX"));
		assertEquals("great-uncle, grand-niece", map.getString("MX", "FXXX"));
		assertEquals("g.-great-uncle, g.-grand-niece", map.getString("MX", "FXXXX"));
		assertEquals("g.-g.-great-uncle, g.-g.-grand-niece", map.getString("MX", "FXXXXX"));
		assertEquals("cousin, cousin", map.getString("XXX", "XXX"));
		assertEquals("3-cousin, 3-cousin", map.getString("XXXXX", "XXXXX"));
		assertEquals("cousin 1-removed, cousin 1-removed", map.getString("XXX", "XXXX"));
		assertEquals("cousin 2-removed, cousin 2-removed", map.getString("XXX", "XXXXX"));
		assertEquals("3-cousin 2-removed, 3-cousin 2-removed", map.getString("XXXXX", "XXXXXXX"));
		//System.out.println(map.map);
	}

	
	public void testRelationsMapRu() throws Exception{
		Parameters.getInstance().put(Constants.LANG_KEY, "ru_translit");
		RelationsMap map=new RelationsMap();
		
		assertEquals("otec, sin", map.getString("M", "MM"));
		assertEquals("sin, otec", map.getString("MM", "M"));
		assertEquals("mat, sin", map.getString("F", "MF"));
		assertEquals("doch, mat", map.getString("FF", "F"));
		assertEquals("brat, brat", map.getString("MM", "MM"));
		assertEquals("sestra, brat", map.getString("FF", "MF"));		
		assertEquals("ded, vnuk", map.getString("M", "MMM"));
		assertEquals("vnuk, ded", map.getString("MMM", "M"));
		assertEquals("ded, vnuchka", map.getString("M", "FXM"));
		assertEquals("praded, pravnuchka", map.getString("M", "FXXM"));
		assertEquals("prapraded, prapravnuchka", map.getString("M", "FXXXM"));
		assertEquals("praprapraded, praprapravnuchka", map.getString("M", "FXXXXM"));
		assertEquals("(4 raza pra-) ded, (4 raza pra-) vnuchka", map.getString("M", "FXXMXXM"));
		assertEquals("(7 raz pra-) ded, (7 raz pra-) vnuchka", map.getString("M", "FXXMXXXXXM"));
		assertEquals("(27 raz pra-) ded, (27 raz pra-) vnuk", map.getString("M", "MXXMXXXXXXXXXXXXXXXXXXXXXXXXXM"));
		assertEquals("plemyannik, dyadya", map.getString("MXX", "MX"));
		assertEquals("tyotya, plemyannica", map.getString("FX", "FXX"));
		assertEquals("2-yurodniy brat, 2-yurodniy brat", map.getString("MXX", "MXX"));
		assertEquals("2-yurodnaya sestra, 2-yurodniy brat", map.getString("FXX", "MXX"));
		assertEquals("4-yurodnaya sestra, 4-yurodnaya sestra", map.getString("FXXXX", "FXXXX"));
		assertEquals("2-yurodniy dyadya, 2-yurodniy plemyannik", map.getString("MXX", "MXXX"));
		assertEquals("4-yurodniy dyadya, 4-yurodnaya plemyannica", map.getString("MXXXX", "FXXXXX"));
		assertEquals("2-yurodniy ded, vnuchataya plemyannica", map.getString("MX", "FXXX"));
		assertEquals("2-yurodniy praded, pravnuchataya plemyannica", map.getString("MX", "FXXXX"));
		assertEquals("2-yurodniy prapraded, prapravnuchatiy plemyannik", map.getString("MX", "MXXXXX"));
		assertEquals("3-yurodnaya babushka, 2-yurodnaya vnuchataya plemyannica", map.getString("FXX", "FXXXX"));
		assertEquals("3-yurodniy praded, 2-yurodniy pravnuchatiy plemyannik", map.getString("MXX", "MXXXXX"));
		assertEquals("3-yurodnaya prababushka, 2-yurodniy pravnuchatiy plemyannik", map.getString("FXX", "MXXXXX"));
		assertEquals("3-yurodnaya praprababushka, 2-yurodniy prapravnuchatiy plemyannik", map.getString("FXX", "MXXXXXX"));
		assertEquals("2-yurodnaya (4 raza pra-) babushka, (4 raza pra-) vnuchatiy plemyannik", map.getString("FX", "MXXXXXXX"));
		
		Parameters.getInstance().put(Constants.LANG_KEY, "en"); //switch back
	}
	
	
	public void testLoadEncodings() throws Exception{
	
		String[] list = Encodings.loadSupportedEncoding();
		assertEquals(4, list.length);
	}

/*
	public void test() throws Exception{
		
		Parameters.getInstance().put(Constants.LANG_KEY, "ru");
		RelationsMap map=new RelationsMap();

		PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream("test.txt"), CodeMap.getCode())));
		//out.println(s);
		//out.println(map.get("MM", "M"));
		out.println(map.get("FX", "MXXXXXXX"));
		
		out.println(map.get("M", "MM"));
		out.println(map.get("MM", "M"));
		out.println(map.get("F", "MF"));
		out.println(map.get("FF", "F"));
		out.println(map.get("MM", "MM"));
		out.println(map.get("FF", "MF"));		
		out.println(map.get("M", "MMM"));
		out.println(map.get("MMM", "M"));
		out.println(map.get("M", "FXM"));
		out.println(map.get("M", "FXXM"));
		out.println(map.get("M", "FXXXM"));
		out.println(map.get("M", "FXXXXM"));
		out.println(map.get("M", "FXXMXXM"));
		out.println(map.get("M", "FXXMXXXXXM"));
		out.println(map.get("M", "MXXMXXXXXXXXXXXXXXXXXXXXXXXXXM"));
		out.println(map.get("MXX", "MX"));
		out.println(map.get("FX", "FXX"));
		out.println(map.get("MXX", "MXX"));
		out.println(map.get("FXX", "MXX"));
		out.println(map.get("FXXXX", "FXXXX"));
		out.println(map.get("MXX", "MXXX"));
		out.println(map.get("MXXXX", "FXXXXX"));
		out.println(map.get("MX", "FXXX"));
		out.println(map.get("MX", "FXXXX"));
		out.println(map.get("MX", "MXXXXX"));
		out.println(map.get("FXX", "FXXXX"));
		out.println(map.get("MXX", "MXXXXX"));
		out.println(map.get("FXX", "MXXXXX"));
		out.println(map.get("FXX", "MXXXXXX"));
		out.println(map.get("FX", "MXXXXXXX"));
		
		//System.out.println(res);
		out.close();
	}	*/
	
	
	public void testIsGoodDates() throws Exception{
		assertTrue(Dates.isGoodDate("1600"));
		assertTrue(Dates.isGoodDate("   1600    "));
		assertTrue(Dates.isGoodDate("ABT 1600"));
		assertTrue(Dates.isGoodDate("AFT 1600"));
		assertTrue(Dates.isGoodDate("BEF 1600"));
		assertTrue(Dates.isGoodDate("EST 1600"));
		assertTrue(Dates.isGoodDate("    EST     1600    "));
		assertTrue(Dates.isGoodDate("JAN 1600"));
		assertTrue(Dates.isGoodDate("DEC 2000"));
		assertTrue(Dates.isGoodDate("1 DEC 2000"));
		assertTrue(Dates.isGoodDate("  2   DEC    2000   "));
	}
	
	
	public void testParseDates() throws Exception{
		Data data=new Data();
		AtreeParser.parseDates("1600", data);
		assertEquals("1600", data.bDate);
		
		AtreeParser.parseDates("ABT 1600", data);
		assertEquals("ABT 1600", data.bDate);
		
		AtreeParser.parseDates("1600", data);
		assertEquals("1600", data.bDate);
		
		AtreeParser.parseDates("1600-1700", data);
		assertEquals("1600", data.bDate);
		assertEquals("1700", data.dDate);
		
	}
	
	
	public void testAtreeParser() throws Exception{
		
		Data data=AtreeParser.parseData("FirstName");
		assertEquals("FirstName", data.name);
		
		data=AtreeParser.parseData("FirstName (1600)");
		assertEquals("FirstName", data.name);
		assertEquals("1600", data.bDate);
		
		data=AtreeParser.parseData("FirstName (ABT 1600)");
		assertEquals("FirstName", data.name);
		assertEquals("ABT 1600", data.bDate);
		
		data=AtreeParser.parseData("FirstName (14 AUG 1855-5 APR 1877)");
		assertEquals("FirstName", data.name);
		assertEquals("14 AUG 1855", data.bDate);
		assertEquals("5 APR 1877", data.dDate);
		
		data=AtreeParser.parseData("FirstName /Surname/");
		assertEquals("FirstName /Surname/", data.name);
		
		data=AtreeParser.parseData("FirstName /Surname / (?-ABT 1800)");
		assertEquals("FirstName /Surname /", data.name);
		assertEquals("?", data.bDate);
		assertEquals("ABT 1800", data.dDate);
		
		data=AtreeParser.parseData("  FirstName   /   Surname   / ( ? - ? )   ");
		assertEquals("FirstName   /   Surname   /", data.name);
		assertEquals("?", data.bDate);
		assertEquals("?", data.dDate);		
		
		data=AtreeParser.parseData("FirstName /Surname/ (1600-1680)");
		assertEquals("FirstName /Surname/", data.name);
		assertEquals("1600", data.bDate);
		assertEquals("1680", data.dDate);
		
		data=AtreeParser.parseData("/Surname (?)/ (28 OCT 1852-BEF 1880)");
		assertEquals("/Surname (?)/", data.name);
		assertEquals("28 OCT 1852", data.bDate);
		assertEquals("BEF 1880", data.dDate);
		
		data=AtreeParser.parseData("FirstName /Surname/ (1742-20 DEC 1799), Place of origin, Something else");
		assertEquals("FirstName /Surname/", data.name);
		assertEquals("1742", data.bDate);
		assertEquals("20 DEC 1799", data.dDate);
		assertEquals("Place of origin, Something else", data.bPlace);		
		
		data=AtreeParser.parseData("FirstName /Surname (MadenSurname)/ (1764-6 OCT 1813)");
		assertEquals("FirstName /Surname (MadenSurname)/", data.name);
		assertEquals("1764", data.bDate);
		assertEquals("6 OCT 1813", data.dDate);
		
		data=AtreeParser.parseData("FirstName Another Name /Surname (MadenSurname)/ (14 AUG 1855-5 APR 1877), Place of origin");
		assertEquals("FirstName Another Name /Surname (MadenSurname)/", data.name);
		assertEquals("14 AUG 1855", data.bDate);
		assertEquals("5 APR 1877", data.dDate);
		assertEquals("Place of origin", data.bPlace);
		
		data=AtreeParser.parseData("FirstName Another Name /Surname (MadenSurname)/ (14 AUG 1855-5 APR 1877) Place of origin");
		assertEquals("FirstName Another Name /Surname (MadenSurname)/", data.name);
		assertEquals("14 AUG 1855", data.bDate);
		assertEquals("5 APR 1877", data.dDate);
		assertEquals("Place of origin", data.bPlace);
		
		data=AtreeParser.parseData("//");
		assertEquals("//", data.name);
		
		data=AtreeParser.parseData("FirstName /Surname/ (?)");
		assertEquals("FirstName /Surname/", data.name);
		assertEquals("?", data.bDate);
		
		data=AtreeParser.parseData("FirstName /Surname/ (?-?)");
		assertEquals("FirstName /Surname/", data.name);
		assertEquals("?", data.bDate);
		assertEquals("?", data.dDate);
		
		data=AtreeParser.parseData("   FirstName //       ");
		assertEquals("FirstName //", data.name);
		assertEquals(null, data.bDate);
		assertEquals(null, data.bPlace);
		
		data=AtreeParser.parseData("FirstName /?/");
		assertEquals("FirstName /?/", data.name);
		assertEquals(null, data.bDate);
		assertEquals(null, data.bPlace);		
		
		data=AtreeParser.parseData("FirstName /Surname(?)/ ?"); //maiden surname and other names are unknown
		assertEquals("FirstName /Surname(?)/ ?", data.name);
		assertEquals(null, data.bDate);
		assertEquals(null, data.bPlace);
		
		data=AtreeParser.parseData("FirstName /Surname (?)/ (?)"); //maiden surname and dates are unknown
		assertEquals("FirstName /Surname (?)/", data.name);
		assertEquals("?", data.bDate);
		assertEquals(null, data.bPlace);
		
		data=AtreeParser.parseData("FirstName /Surname (?)/ (?) place"); //maiden surname and dates are unknown
		assertEquals("FirstName /Surname (?)/", data.name);
		assertEquals("?", data.bDate);
		assertEquals("place", data.bPlace);
		
		data=AtreeParser.parseData("FirstName /?/, ?");
		assertEquals("FirstName /?/", data.name);
		assertEquals(null, data.bDate);
		assertEquals("?", data.bPlace);
		
		data=AtreeParser.parseData("LastName, FirstName // Other Name");
		assertEquals("LastName, FirstName // Other Name", data.name);
		assertEquals(null, data.bPlace);
		
		data=AtreeParser.parseData("Names (?), Place of origin");
		assertEquals("Names", data.name);
		assertEquals("?", data.bDate);
		assertEquals("Place of origin", data.bPlace);
		
		data=AtreeParser.parseData("Names (?) Place of origin");
		assertEquals("Names", data.name);
		assertEquals("?", data.bDate);
		assertEquals("Place of origin", data.bPlace);		
		
		data=AtreeParser.parseData("FirstName AnotherName /Surname/ (?-2 AUG 1885), Place");
		assertEquals("FirstName AnotherName /Surname/", data.name);
		assertEquals("?", data.bDate);
		assertEquals("2 AUG 1885", data.dDate);
		assertEquals("Place", data.bPlace);
		
		data=AtreeParser.parseData("FirstName 2");
		assertEquals("FirstName 2", data.name);

		data=AtreeParser.parseData("FirstName 2 (1600)");
		assertEquals("FirstName 2", data.name);
		assertEquals("1600", data.bDate);
		
		data=AtreeParser.parseData("FirstName 2 // (1600)");
		assertEquals("FirstName 2 //", data.name);
		assertEquals("1600", data.bDate);

		data=AtreeParser.parseData("Лицо Связка 2 // (ABT 1620)");
		assertEquals("Лицо Связка 2 //", data.name);
		assertEquals("ABT 1620", data.bDate);
		
		//Result result=new Result();
		//AtreeProcessor.process("atree.txt", result);
	}
	
	
	public void testFillMissing(){
		HashMap test=new HashMap();
		test.put("MMMFM", "");
		AtreeProcessor.fillMissing(test);
		assertEquals(5, test.size());
		
		test=new HashMap();
		test.put("M", "");
		AtreeProcessor.fillMissing(test);
		assertEquals(1, test.size());

		test=new HashMap();
		test.put("M", "");
		test.put("MF", "");
		test.put("MM", "");
		test.put("MMMM", "");
		AtreeProcessor.fillMissing(test);
		assertEquals(5, test.size());
	}
	
	
	public void testSequence(){
		Sequence seq=new Sequence();
		assertEquals("@I1@", seq.getNextIndiId());
		assertEquals("@I2@", seq.getNextIndiId());
		assertEquals("@F1@", seq.getNextFamId());
		assertEquals("@I3@", seq.getNextIndiId());
		assertEquals("@F2@", seq.getNextFamId());
	}
	
	
	public void testReverse() throws Exception{
		
		Main.main(new String[]{"test2.ged", "@I1@", "--output", "atree.txt" }); //will generate atree.txt
		
		assertTrue(new File(Constants.DEFAULT_DESC_FILE).delete());
		
		Main.main(new String[]{"atree.txt", "--atree2gedcom", "--output", "gedcom.ged"}); //will produce gedcom
	
		Main.main(new String[]{"gedcom.ged", "@I1@","--output", "atree2.txt" }); //will generate atree.txt from generated gedcom

		assertTrue(new File(Constants.DEFAULT_DESC_FILE).delete());
		assertTrue(new File("gedcom.ged").delete());
		
		String firstResult = TestUtils.file2String("atree.txt");
		assertTrue(new File("atree.txt").delete());

		String secondResult = TestUtils.file2String("atree2.txt");
		assertTrue(new File("atree2.txt").delete());
		
		assertEquals(firstResult, secondResult);
	}
	

	//add here more tests
	
}
