package org.purl.wf4ever.astrotaverna.aladin;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

/** 
 * 
 * @author julian Garrido 
 * Some tests may fail because the resulting votable name comes from a random number 
 */
public class AladinScriptParserTest {
	private AladinScriptParser parser;

	//this method is invoked before each test method
	@Before
	public void intTest() throws Exception {
		parser = new AladinScriptParser();
	}
	
	@Test
	public void parseSaveScript(){
		//String script = "get aladin(J,FITS) m1 ;\n save /Users/julian/Documents/wf4ever/aladin/exampleTests/m1.jpg; quit";
		String script = "get aladin(J,FITS) m1 ;\n save -png -jpg /Users/julian/Documents/wf4ever/aladin/exampleTests/m1.jpg -lk; backup /users/juan\\juan.aj; save m1.eps ; quit";
		//String script = "get aladin(J,FITS) m1 ;\n save /Users/julian/Documents/wf4ever/aladin/exampletests/m1.jpg; quit";
		//String script = "get aladin(J,FITS) m1 ;\n save /Users/julian/Documents/wf4ever/aladin/example&tests/m1.jpg; quit";
		ArrayList<String> list = parser.parseScript(script);

		System.out.println(list.toString());
		
		assertEquals("Unexpected number of elemens", 3, list.size());
		if(list.size()>1){
			assertEquals("Unexpected filename", "/Users/julian/Documents/wf4ever/aladin/exampleTests/m1.jpg", list.get(0));
			assertEquals("Unexpected filename", "/users/juan\\juan.aj", list.get(1));
			assertEquals("Unexpected filename", "m1.eps", list.get(2));
		}
	}
	
	@Test
	public void parseExportScript(){
		//String script = "get aladin(J,FITS) m1 ;\n save /Users/julian/Documents/wf4ever/aladin/exampleTests/m1.jpg; quit";
		String script = "get aladin(J,FITS) m1 ;\n export DSS1.V.SERC C:\\DSS2image.fits; export -votable GSC1.2 /home/gsc1.2.dat; export RGBimg m1RGB.fits; quit";
		//String script = "get aladin(J,FITS) m1 ;\n save /Users/julian/Documents/wf4ever/aladin/exampletests/m1.jpg; quit";
		//String script = "get aladin(J,FITS) m1 ;\n save /Users/julian/Documents/wf4ever/aladin/example&tests/m1.jpg; quit";
		ArrayList<String> list = parser.parseScript(script);

		assertEquals("Unexpected number of elemens", list.size(), 3);
		if(list.size()>=3){
			assertEquals("Unexpected filename", "C:\\DSS2image.fits", list.get(0));
			assertEquals("Unexpected filename", "/home/gsc1.2.dat", list.get(1));
			assertEquals("Unexpected filename", "m1RGB.fits", list.get(2));
		}
	}
	
	@Test
	public void parseFileScript(){
		
	}
	
	
	@Test
	public void parseURLScript() throws Exception {
		ArrayList<String> list = parser.parseURL("http://cdsweb.u-strasbg.fr/~allen/CDS_Tutorial/attachments/Arp_script.ajs");
		
		assertEquals("Unexpected number of elemens", 2,  list.size());
		if(list.size()>0){
			assertEquals("Unexpected filename", "/Users/allen/Desktop/Arp/Arp-$2_chart.png", list.get(0));
		}		
	}
	
	@Test
	public void replaceDolars(){
		String file = "/Users/allen/Desktop/Arp/Arp-$2_chart.png";
		String dolar = Pattern.quote("$")+"2";
		String value = "abcde";
		
		String result = file.replaceAll(dolar, value);
		//template.replaceAll(Pattern.quote("$")+mappingPos.get(k)+Pattern.quote("$"), obj.toString());
		
		assertEquals("replacement not done. ", "/Users/allen/Desktop/Arp/Arp-abcde_chart.png",result);
	}
	
	@Test
	public void parseURLMacro() throws Exception {
		String lsp = System.getProperty("line.separator");
		//System.out.println("----"+lsp+"---");
		String params ="ab1	1	1.11	gh1	ih1" + lsp +
				"ab2	2	1.22	gh2	ih2" + lsp +
				"ab3	3	1.33	gh3	ih3";
		File file = writeStringAsTmpFile(params);
		
		ArrayList<ArrayList<String>> list = parser.parseURLMacro("http://cdsweb.u-strasbg.fr/~allen/CDS_Tutorial/attachments/Arp_script.ajs", file.toURI().toString());
		
		assertEquals("Unexpected number of elemens", 3,  list.size());
		if(list.size()>0){
			assertEquals("Unexpected filename", "/Users/allen/Desktop/Arp/Arp-1_chart.png", list.get(0).get(0));
			assertEquals("Unexpected filename", "/Users/allen/Desktop/Arp/Arp-1_stack.aj", list.get(0).get(1));
			assertEquals("Unexpected filename", "/Users/allen/Desktop/Arp/Arp-2_chart.png", list.get(1).get(0));
			assertEquals("Unexpected filename", "/Users/allen/Desktop/Arp/Arp-2_stack.aj", list.get(1).get(1));
			assertEquals("Unexpected filename", "/Users/allen/Desktop/Arp/Arp-3_chart.png", list.get(2).get(0));
			assertEquals("Unexpected filename", "/Users/allen/Desktop/Arp/Arp-3_stack.aj", list.get(2).get(1));
		}		
	}
	
	/*
	//content at http://cdsweb.u-strasbg.fr/~allen/CDS_Tutorial/attachments/Arp_script.ajs
	#AJS
	reset
	grid on
	# DSS image:
	"ARP-$2_DSS" = get DSS.STScI(POSS2UKSTU_Red,15,15) $3
	# SDSS image:
	# "ARP-$2_SDSS" = get SDSS(keyword=Filter g) $3
	# SIMBAD plane
	# "ARP-$2_Simbad" = get Simbad $3 5'
	# Observation Logs
	viz_logHST=get vizier(logHST) $3 
	viz_logESO=get vizier(logESO) $3 
	viz_logChandra=get vizier(logChandra) $3
	sync
	pause 1
	# Write results to files
	#export  B/hst/hstlog /Users/allen/Desktop/Arp/Arp-$2_HST.xml
	save /Users/allen/Desktop/Arp/Arp-$2_chart.png
	backup /Users/allen/Desktop/Arp/Arp-$2_stack.aj
	*/

	
	public static File writeStringAsTmpFile(String content) throws java.io.IOException{
	    
	    File file = File.createTempFile("astro", null);
	    file.deleteOnExit();
	    FileWriter writer = new FileWriter(file);
	    writer.write(content);
	    writer.close();
	    
	    return file;
	}
	
	/*
	@Ignore("Not ready to run")
	@BeforeClass
	public static void createTableFiles(){
		//create files with votables
	}

	@Ignore("Not ready to run")
	@AfterClass
	public static void deleteTableFiles(){
		//delete files with votables
	}
	*/
	
	@Ignore
	@Test
	public void parseFileMacro() throws Exception {
		String lsp = System.getProperty("line.separator");
		//System.out.println("----"+lsp+"---");
		
		ArrayList<ArrayList<String>> list = parser.parseFileMacro("/Users/julian/src/astrotaverna/Image-activity/src/main/resources/macro.ajs", "/Users/julian/src/astrotaverna/Image-activity/src/main/resources/parameterFile.dat");
		
		assertEquals("Unexpected number of elemens", 20,  list.size());
		
		String path = "D:\\Escritorio\\SVOWf96413_2005WC48.xml";
		String readPath = list.get(0).get(0);
		
		System.out.println(path);
		System.out.println(readPath);
		
		if(list.size()>0){
			assertEquals("Unexpected filename", "D:\\Escritorio\\SVOWf96413_2005WC48.xml", list.get(0).get(0));
			assertEquals("Unexpected filename", "D:\\Escritorio\\SVOWf96413_2006XC43.xml", list.get(1).get(0));
			assertEquals("Unexpected filename", "D:\\Escritorio\\SVOWf96413_2001XQ72.xml", list.get(2).get(0));
			assertEquals("Unexpected filename", "D:\\Escritorio\\SVOWf96413_2008GX69.xml", list.get(3).get(0));
			assertEquals("Unexpected filename", "D:\\Escritorio\\SVOWf96426_2005TF111.xml", list.get(4).get(0));
			assertEquals("Unexpected filename", "D:\\Escritorio\\SVOWf96426_2005SD289.xml", list.get(5).get(0));
		}
	}
	
	@Ignore
	@Test
	public void parseFileMacro2() throws Exception {
		String lsp = System.getProperty("line.separator");
		//System.out.println("----"+lsp+"---");
		
		ArrayList<ArrayList<String>> list = parser.parseFileMacro("/Users/julian/src/astrotaverna/Image-activity/src/test/resources/macro_windows.txt", "/Users/julian/src/astrotaverna/Image-activity/src/test/resources/params_windows.txt");
		
		assertEquals("Unexpected number of elemens", 2,  list.size());
		
		String path = "D:\\Escritorio\\SVOWf96413_2005WC48.xml";
		String readPath = list.get(0).get(0);
		
		System.out.println(path);
		System.out.println(readPath);
		
		if(list.size()>0){
			assertEquals("Unexpected filename", "D:\\Escritorio\\SVO\\M51.jpg", list.get(0).get(0));
			assertEquals("Unexpected filename", "D:\\Escritorio\\SVO\\M101.jpg", list.get(1).get(0));
		}		
	}
	
	//inputs.put(FIRST_INPUT, "/home/julian/src/astrotaverna/Image-activity/src/main/resources/macro.ajs");
	//inputs.put(SECOND_INPUT, "/home/julian/src/astrotaverna/Image-activity/src/main/resources/parameterFile.dat");
	
	@Test
	public void replaceDolarsWithWindowsPaths(){
		String result = "__$2";
		String path = "D:\\Escritorio\\SVOWf96413_2005WC48.xml";
		String dolar = Pattern.quote("$") + 2;
		String aux1 = result.replaceAll(dolar, path);
		System.out.println(aux1);
		String aux2 = result.replaceAll("$2", path);
		System.out.println(aux2);
		String aux3 = result.replaceAll("$2", Pattern.quote(path));
		System.out.println(aux3);
		String aux4 = result.replaceAll(dolar, Pattern.quote(path));
		System.out.println(aux4);
		
	    String aux5 = Pattern.compile("$2").matcher(result).replaceAll(path);
	    System.out.println(aux5);

	    String aux6 = Pattern.compile(dolar).matcher(result).replaceAll(path);
	    System.out.println(aux6);
	    
	    Pattern.compile("$2").matcher(result);
		String aux7 = Matcher.quoteReplacement(path);
	    System.out.println(aux7);

	    String aux8 = Pattern.compile(dolar).matcher(result).quoteReplacement(path);
	    System.out.println(aux8);
	    
	    String aux9 = result.replaceAll(dolar, Matcher.quoteReplacement(path));
		System.out.println(aux9);
		
		assertEquals("We have lost slashes", "__"+path, aux9);
	}	

	
	
}
