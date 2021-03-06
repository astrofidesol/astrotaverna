package org.purl.wf4ever.astrotaverna.tcat;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.taverna.t2.activities.testutils.ActivityInvoker;
import net.sf.taverna.t2.workflowmodel.processor.activity.ActivityConfigurationException;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

public class TcatListActivityTest {

	private TcatListActivityConfigurationBean configBean;

	//these variables must be the same than the ones defined in the activity class
	private static final String IN_FIRST_INPUT = "votableList";
	private static final String IN_OUTPUT_TABLE_NAME = "outputFileNameIn";
	private static final String OUT_SIMPLE_OUTPUT = "outputFileOut";
	private static final String OUT_REPORT = "report";
	
	private TcatListActivity activity = new TcatListActivity();
	
	private List<String> votableListIn;

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
	
	//this method is invoked before each test method
	@Before
	public void makeConfigBean() throws Exception {
		configBean = new TcatListActivityConfigurationBean();
		
		configBean.setTypeOfInput("File");
		votableListIn = new ArrayList<String>();
		votableListIn.add(table1);
		votableListIn.add(table2);
	}

	@Test(expected = ActivityConfigurationException.class)
	public void invalidConfiguration() throws ActivityConfigurationException {
		TcatListActivityConfigurationBean invalidBean = new TcatListActivityConfigurationBean();
		invalidBean.setTypeOfInput("Fileon");
		// Should throw ActivityConfigurationException
		activity.configure(invalidBean);
	}
	
	

	//this test is valid only with the right folders

	
	@Test(expected = Exception.class)
	public void executeAsynchWithUnexistingFile() throws Exception {
		configBean.setTypeOfInput("File");
		activity.configure(configBean);

		Map<String, Object> inputs = new HashMap<String, Object>();
		votableListIn.add("/home/julian/Documentos/wf4ever/tables/filenoexist.xml");
		votableListIn.add("/home/julian/Documentos/wf4ever/tables/filenoexist2.xml");
		inputs.put(IN_FIRST_INPUT, votableListIn);
		inputs.put(IN_OUTPUT_TABLE_NAME, "/home/julian/Documentos/wf4ever/tables/file.xml");
		

		Map<String, Class<?>> expectedOutputTypes = new HashMap<String, Class<?>>();
		expectedOutputTypes.put(OUT_SIMPLE_OUTPUT, String.class);
		expectedOutputTypes.put(OUT_REPORT, String.class);

		Map<String, Object> outputs = ActivityInvoker.invokeAsyncActivity(
				activity, inputs, expectedOutputTypes);

		assertEquals("Unexpected outputs", 2, outputs.size());
		assertEquals("/home/julian/Documentos/wf4ever/tables/join_test.xml", outputs.get(OUT_SIMPLE_OUTPUT));
		assertEquals("simple-report", outputs.get(OUT_REPORT));
		
		//assertEquals(Arrays.asList("Value 1", "Value 2"), outputs
		//		.get("moreOutputs"));

	}

	@Test
	public void executeAsynchWitStrings() throws Exception {
		configBean.setTypeOfInput("String");
		activity.configure(configBean);

		Map<String, Object> inputs = new HashMap<String, Object>();
		
		inputs.put(IN_FIRST_INPUT, votableListIn);
		
		

		Map<String, Class<?>> expectedOutputTypes = new HashMap<String, Class<?>>();
		expectedOutputTypes.put(OUT_SIMPLE_OUTPUT, String.class);
		expectedOutputTypes.put(OUT_REPORT, String.class);

		Map<String, Object> outputs = ActivityInvoker.invokeAsyncActivity(
				activity, inputs, expectedOutputTypes);

		String result = (String) outputs.get(OUT_SIMPLE_OUTPUT);
		
		//result = result.replace("\n", "").replace("\t", "").replace(" ", "").replace(System.getProperty("line.separator"), "");
		//tableresult = tableresult.replace("\n", "").replace("\t", "").replace(" ", "").replace(System.getProperty("line.separator"), "");		
		//assertTrue("Wrong output : ", (result.length()> tableresult.length()-6) && (result.length()< tableresult.length()+6));
		assertTrue("Wrong output : ", result.indexOf("nrows=\"3\"")!=-1);
		assertEquals("Unexpected outputs", 2, outputs.size());
		//assertEquals("/home/julian/Documentos/wf4ever/tables/join_test.xml", outputs.get(OUT_SIMPLE_OUTPUT));
		assertEquals("simple-report", outputs.get(OUT_REPORT));
		
		//assertEquals(Arrays.asList("Value 1", "Value 2"), outputs
		//		.get("moreOutputs"));

	}
	
	@Test(expected = Exception.class)
	public void executeAsynchWitNullInport() throws Exception {
		configBean.setTypeOfInput("String");
		activity.configure(configBean);

		Map<String, Object> inputs = new HashMap<String, Object>();
		
		//inputs.put(IN_FIRST_INPUT, votableListIn);
		
		

		Map<String, Class<?>> expectedOutputTypes = new HashMap<String, Class<?>>();
		expectedOutputTypes.put(OUT_SIMPLE_OUTPUT, String.class);
		expectedOutputTypes.put(OUT_REPORT, String.class);

		Map<String, Object> outputs = ActivityInvoker.invokeAsyncActivity(
				activity, inputs, expectedOutputTypes);

		String result = (String) outputs.get(OUT_SIMPLE_OUTPUT);
		
		assertTrue("Wrong output : ", result.indexOf("nrows=\"3\"")!=-1);
		assertEquals("Unexpected outputs", 2, outputs.size());
		//assertEquals("/home/julian/Documentos/wf4ever/tables/join_test.xml", outputs.get(OUT_SIMPLE_OUTPUT));
		assertEquals("simple-report", outputs.get(OUT_REPORT));
		
		//assertEquals(Arrays.asList("Value 1", "Value 2"), outputs
		//		.get("moreOutputs"));

	}
	
	@Test
	public void reConfiguredActivity() throws Exception {
		assertEquals("Unexpected inputs", 0, activity.getInputPorts().size());
		assertEquals("Unexpected outputs", 0, activity.getOutputPorts().size());

		activity.configure(configBean);
		assertEquals("Unexpected inputs", 2, activity.getInputPorts().size());
		assertEquals("Unexpected outputs", 2, activity.getOutputPorts().size());

		activity.configure(configBean);
		// Should not change on reconfigure
		assertEquals("Unexpected inputs", 2, activity.getInputPorts().size());
		assertEquals("Unexpected outputs", 2, activity.getOutputPorts().size());
	}

	
	@Test
	public void reConfiguredPorts() throws Exception {
		activity.configure(configBean);

		TcatListActivityConfigurationBean specialBean = new TcatListActivityConfigurationBean();
		specialBean.setTypeOfInput("String");

		activity.configure(specialBean);		
		// Should now have added the optional ports
		assertEquals("Unexpected inputs", 1, activity.getInputPorts().size());
		assertEquals("Unexpected outputs", 2, activity.getOutputPorts().size());
	}
	
	
	private String table1 = "<?xml version='1.0'?>"
			+ "<VOTABLE version=\"1.1\""
			+ " xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\""
			+ " xsi:schemaLocation=\"http://www.ivoa.net/xml/VOTable/v1.1 http://www.ivoa.net/xml/VOTable/v1.1\""
			+ " xmlns=\"http://www.ivoa.net/xml/VOTable/v1.1\">"
			+ "<!--"
			+ " !  VOTable written by STIL version 3.0-3 (uk.ac.starlink.votable.VOTableWriter)"
			+ " !  at 2012-05-20T11:42:06"
			+ " !-->"
			+ "<RESOURCE>"
			+ "<TABLE nrows=\"1\">"
			+ "<DESCRIPTION>"
			+ "Faint Images of the Radio Sky at Twenty cm (FIRST)"
			+ "</DESCRIPTION>"
			+ "<PARAM arraysize=\"18\" datatype=\"char\" name=\"default_search_radius\" ucd=\"OBS_ANG-SIZE\" value=\"0.0166666666666667\"/>"
			+ "<PARAM arraysize=\"18\" datatype=\"char\" name=\"default_search_radius\" ucd=\"OBS_ANG-SIZE\" value=\"0.0166666666666667\"/>"
			+ "<FIELD datatype=\"int\" name=\"unique_id_1\">"
			+ "<DESCRIPTION>Integer key</DESCRIPTION>"
			+ "<VALUES null='-2147483648'/>"
			+ "</FIELD>"
			+ "<FIELD arraysize=\"*\" datatype=\"char\" name=\"name\" ucd=\"ID_MAIN\">"
			+ "<DESCRIPTION>FIRST Source Designation</DESCRIPTION>"
			+ "</FIELD>"
			+ "<FIELD datatype=\"double\" name=\"ra\" ucd=\"POS_EQ_RA_MAIN\" unit=\"degree\">"
			+ "<DESCRIPTION>Right Ascension</DESCRIPTION>"
			+ "</FIELD>"
			+ "<FIELD datatype=\"double\" name=\"dec\" ucd=\"POS_EQ_DEC_MAIN\" unit=\"degree\">"
			+ "<DESCRIPTION>Declination</DESCRIPTION>"
			+ "</FIELD>"
			+ "<FIELD datatype=\"int\" name=\"unique_id_2\">"
			+ "<DESCRIPTION>Integer key</DESCRIPTION>"
			+ "<VALUES null='-2147483648'/>"
			+ "</FIELD>"
			+ "<FIELD datatype=\"double\" name=\"flux_20_cm\" ucd=\"phot.flux.density;em.radio.750-1500MHz\" unit=\"mJy\">"
			+ "<DESCRIPTION>Peak Flux Density at 1.4GHz (mJy)</DESCRIPTION>"
			+ "</FIELD>"
			+ "<FIELD datatype=\"double\" name=\"flux_20_cm_error\" ucd=\"stat.error;phot.flux.density;em.radio.750-1500MHz\" unit=\"mJy\">"
			+ "<DESCRIPTION>Local Noise Estimate of Source (mJy)</DESCRIPTION>"
			+ "</FIELD>"
			+ "<FIELD datatype=\"double\" name=\"int_flux_20_cm\" ucd=\"phot.flux.density;em.radio.750-1500MHz\" unit=\"mJy\">"
			+ "<DESCRIPTION>Integrated Flux Density at 1.4GHz (mJy)</DESCRIPTION>"
			+ "</FIELD>"
			+ "<FIELD datatype=\"double\" name=\"sidelobe_prob\" ucd=\"stat.probability\">"
			+ "<DESCRIPTION>Probability That Source Is a Sidelobe</DESCRIPTION>"
			+ "</FIELD>"
			+ "<FIELD datatype=\"double\" name=\"twomass_first_offset\" ucd=\"pos.angDistance;em.IR;em.radio.750-1500MHz\" unit=\"arcsec\">"
			+ "<DESCRIPTION>Offset of Nearest 2MASS Source</DESCRIPTION>"
			+ "</FIELD>"
			+ "<FIELD datatype=\"double\" name=\"twomass_kmag\" ucd=\"phot.mag;em.IR.K\" unit=\"mag\">"
			+ "<DESCRIPTION>K Magnitude of Nearest 2MASS Source</DESCRIPTION>"
			+ "</FIELD>"
			+ "<FIELD datatype=\"double\" name=\"Search_Offset\" unit=\"'\">"
			+ "<DESCRIPTION>Offset of target/observation from query center</DESCRIPTION>"
			+ "</FIELD>"
			+ "<DATA>"
			+ "<TABLEDATA>"
			+ "  <TR>"
			+ "    <TD>946464</TD>"
			+ "    <TD>FIRST J233859.7-112355</TD>"
			+ "    <TD>354.749046</TD>"
			+ "    <TD>-11.398828</TD>"
			+ "    <TD>946464</TD>"
			+ "    <TD>1.68</TD>"
			+ "    <TD>0.14</TD>"
			+ "    <TD>1.75</TD>"
			+ "    <TD>0.016</TD>"
			+ "    <TD>NaN</TD>"
			+ "    <TD>NaN</TD>"
			+ "    <TD>0.0</TD>"
			+ "  </TR>"
			+ "</TABLEDATA>"
			+ "</DATA>"
			+ "</TABLE>"
			+ "</RESOURCE>"
			+ "</VOTABLE>";
	
	private String table2 = "<?xml version='1.0'?>"
			+ "<VOTABLE version=\"1.1\""
			+ " xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\""
			+ " xsi:schemaLocation=\"http://www.ivoa.net/xml/VOTable/v1.1 http://www.ivoa.net/xml/VOTable/v1.1\""
			+ " xmlns=\"http://www.ivoa.net/xml/VOTable/v1.1\">"
			+ "<!--"
			+ " !  VOTable written by STIL version 3.0-3 (uk.ac.starlink.votable.VOTableWriter)"
			+ " !  at 2012-05-20T11:42:06"
			+ " !-->"
			+ "<RESOURCE>"
			+ "<TABLE nrows=\"2\">"
			+ "<DESCRIPTION>"
			+ "Faint Images of the Radio Sky at Twenty cm (FIRST)"
			+ "</DESCRIPTION>"
			+ "<PARAM arraysize=\"18\" datatype=\"char\" name=\"default_search_radius\" ucd=\"OBS_ANG-SIZE\" value=\"0.0166666666666667\"/>"
			+ "<PARAM arraysize=\"18\" datatype=\"char\" name=\"default_search_radius\" ucd=\"OBS_ANG-SIZE\" value=\"0.0166666666666667\"/>"
			+ "<FIELD datatype=\"int\" name=\"unique_id_1\">"
			+ "<DESCRIPTION>Integer key</DESCRIPTION>"
			+ "<VALUES null='-2147483648'/>"
			+ "</FIELD>"
			+ "<FIELD arraysize=\"*\" datatype=\"char\" name=\"name\" ucd=\"ID_MAIN\">"
			+ "<DESCRIPTION>FIRST Source Designation</DESCRIPTION>"
			+ "</FIELD>"
			+ "<FIELD datatype=\"double\" name=\"ra\" ucd=\"POS_EQ_RA_MAIN\" unit=\"degree\">"
			+ "<DESCRIPTION>Right Ascension</DESCRIPTION>"
			+ "</FIELD>"
			+ "<FIELD datatype=\"double\" name=\"dec\" ucd=\"POS_EQ_DEC_MAIN\" unit=\"degree\">"
			+ "<DESCRIPTION>Declination</DESCRIPTION>"
			+ "</FIELD>"
			+ "<FIELD datatype=\"int\" name=\"unique_id_2\">"
			+ "<DESCRIPTION>Integer key</DESCRIPTION>"
			+ "<VALUES null='-2147483648'/>"
			+ "</FIELD>"
			+ "<FIELD datatype=\"double\" name=\"flux_20_cm\" ucd=\"phot.flux.density;em.radio.750-1500MHz\" unit=\"mJy\">"
			+ "<DESCRIPTION>Peak Flux Density at 1.4GHz (mJy)</DESCRIPTION>"
			+ "</FIELD>"
			+ "<FIELD datatype=\"double\" name=\"flux_20_cm_error\" ucd=\"stat.error;phot.flux.density;em.radio.750-1500MHz\" unit=\"mJy\">"
			+ "<DESCRIPTION>Local Noise Estimate of Source (mJy)</DESCRIPTION>"
			+ "</FIELD>"
			+ "<FIELD datatype=\"double\" name=\"int_flux_20_cm\" ucd=\"phot.flux.density;em.radio.750-1500MHz\" unit=\"mJy\">"
			+ "<DESCRIPTION>Integrated Flux Density at 1.4GHz (mJy)</DESCRIPTION>"
			+ "</FIELD>"
			+ "<FIELD datatype=\"double\" name=\"sidelobe_prob\" ucd=\"stat.probability\">"
			+ "<DESCRIPTION>Probability That Source Is a Sidelobe</DESCRIPTION>"
			+ "</FIELD>"
			+ "<FIELD datatype=\"double\" name=\"twomass_first_offset\" ucd=\"pos.angDistance;em.IR;em.radio.750-1500MHz\" unit=\"arcsec\">"
			+ "<DESCRIPTION>Offset of Nearest 2MASS Source</DESCRIPTION>"
			+ "</FIELD>"
			+ "<FIELD datatype=\"double\" name=\"twomass_kmag\" ucd=\"phot.mag;em.IR.K\" unit=\"mag\">"
			+ "<DESCRIPTION>K Magnitude of Nearest 2MASS Source</DESCRIPTION>"
			+ "</FIELD>"
			+ "<FIELD datatype=\"double\" name=\"Search_Offset\" unit=\"'\">"
			+ "<DESCRIPTION>Offset of target/observation from query center</DESCRIPTION>"
			+ "</FIELD>"
			+ "<DATA>"
			+ "<TABLEDATA>"
			+ "  <TR>"
			+ "    <TD>946352</TD>"
			+ "    <TD>FIRST J233916.9-111928</TD>"
			+ "    <TD>354.820408</TD>"
			+ "    <TD>-11.324472</TD>"
			+ "    <TD>946352</TD>"
			+ "    <TD>1.62</TD>"
			+ "    <TD>0.138</TD>"
			+ "    <TD>1.48</TD>"
			+ "    <TD>0.047</TD>"
			+ "    <TD>0.12</TD>"
			+ "    <TD>14.99</TD>"
			+ "    <TD>6.126</TD>"
			+ "  </TR>"
			+ "  <TR>"
			+ "    <TD>946331</TD>"
			+ "    <TD>FIRST J233846.4-111841</TD>"
			+ "    <TD>354.693467</TD>"
			+ "    <TD>-11.311506</TD>"
			+ "    <TD>946331</TD>"
			+ "    <TD>11.41</TD>"
			+ "    <TD>0.137</TD>"
			+ "    <TD>15.56</TD>"
			+ "    <TD>0.014</TD>"
			+ "    <TD>NaN</TD>"
			+ "    <TD>NaN</TD>"
			+ "    <TD>6.176</TD>"
			+ "  </TR>"
			+ "</TABLEDATA>"
			+ "</DATA>"
			+ "</TABLE>"
			+ "</RESOURCE>"
			+ "</VOTABLE>";
	
	private String tableresult = "<?xml version='1.0'?>"
			+ "<VOTABLE version=\"1.1\""
			+ " xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\""
			+ " xsi:schemaLocation=\"http://www.ivoa.net/xml/VOTable/v1.1 http://www.ivoa.net/xml/VOTable/v1.1\""
			+ " xmlns=\"http://www.ivoa.net/xml/VOTable/v1.1\">"
			+ "<!--"
			+ " !  VOTable written by STIL version 3.0-3 (uk.ac.starlink.votable.VOTableWriter)"
			+ " !  at 2012-05-20T11:42:06"
			+ " !-->"
			+ "<RESOURCE>"
			+ "<TABLE nrows=\"3\">"
			+ "<DESCRIPTION>"
			+ "Faint Images of the Radio Sky at Twenty cm (FIRST)"
			+ "</DESCRIPTION>"
			+ "<PARAM arraysize=\"18\" datatype=\"char\" name=\"default_search_radius\" ucd=\"OBS_ANG-SIZE\" value=\"0.0166666666666667\"/>"
			+ "<PARAM arraysize=\"18\" datatype=\"char\" name=\"default_search_radius\" ucd=\"OBS_ANG-SIZE\" value=\"0.0166666666666667\"/>"
			+ "<FIELD datatype=\"int\" name=\"unique_id_1\">"
			+ "<DESCRIPTION>Integer key</DESCRIPTION>"
			+ "<VALUES null='-2147483648'/>"
			+ "</FIELD>"
			+ "<FIELD arraysize=\"*\" datatype=\"char\" name=\"name\" ucd=\"ID_MAIN\">"
			+ "<DESCRIPTION>FIRST Source Designation</DESCRIPTION>"
			+ "</FIELD>"
			+ "<FIELD datatype=\"double\" name=\"ra\" ucd=\"POS_EQ_RA_MAIN\" unit=\"degree\">"
			+ "<DESCRIPTION>Right Ascension</DESCRIPTION>"
			+ "</FIELD>"
			+ "<FIELD datatype=\"double\" name=\"dec\" ucd=\"POS_EQ_DEC_MAIN\" unit=\"degree\">"
			+ "<DESCRIPTION>Declination</DESCRIPTION>"
			+ "</FIELD>"
			+ "<FIELD datatype=\"int\" name=\"unique_id_2\">"
			+ "<DESCRIPTION>Integer key</DESCRIPTION>"
			+ "<VALUES null='-2147483648'/>"
			+ "</FIELD>"
			+ "<FIELD datatype=\"double\" name=\"flux_20_cm\" ucd=\"phot.flux.density;em.radio.750-1500MHz\" unit=\"mJy\">"
			+ "<DESCRIPTION>Peak Flux Density at 1.4GHz (mJy)</DESCRIPTION>"
			+ "</FIELD>"
			+ "<FIELD datatype=\"double\" name=\"flux_20_cm_error\" ucd=\"stat.error;phot.flux.density;em.radio.750-1500MHz\" unit=\"mJy\">"
			+ "<DESCRIPTION>Local Noise Estimate of Source (mJy)</DESCRIPTION>"
			+ "</FIELD>"
			+ "<FIELD datatype=\"double\" name=\"int_flux_20_cm\" ucd=\"phot.flux.density;em.radio.750-1500MHz\" unit=\"mJy\">"
			+ "<DESCRIPTION>Integrated Flux Density at 1.4GHz (mJy)</DESCRIPTION>"
			+ "</FIELD>"
			+ "<FIELD datatype=\"double\" name=\"sidelobe_prob\" ucd=\"stat.probability\">"
			+ "<DESCRIPTION>Probability That Source Is a Sidelobe</DESCRIPTION>"
			+ "</FIELD>"
			+ "<FIELD datatype=\"double\" name=\"twomass_first_offset\" ucd=\"pos.angDistance;em.IR;em.radio.750-1500MHz\" unit=\"arcsec\">"
			+ "<DESCRIPTION>Offset of Nearest 2MASS Source</DESCRIPTION>"
			+ "</FIELD>"
			+ "<FIELD datatype=\"double\" name=\"twomass_kmag\" ucd=\"phot.mag;em.IR.K\" unit=\"mag\">"
			+ "<DESCRIPTION>K Magnitude of Nearest 2MASS Source</DESCRIPTION>"
			+ "</FIELD>"
			+ "<FIELD datatype=\"double\" name=\"Search_Offset\" unit=\"'\">"
			+ "<DESCRIPTION>Offset of target/observation from query center</DESCRIPTION>"
			+ "</FIELD>"
			+ "<DATA>"
			+ "<TABLEDATA>"
			+ "  <TR>"
			+ "    <TD>946464</TD>"
			+ "    <TD>FIRST J233859.7-112355</TD>"
			+ "    <TD>354.749046</TD>"
			+ "    <TD>-11.398828</TD>"
			+ "    <TD>946464</TD>"
			+ "    <TD>1.68</TD>"
			+ "    <TD>0.14</TD>"
			+ "    <TD>1.75</TD>"
			+ "    <TD>0.016</TD>"
			+ "    <TD>NaN</TD>"
			+ "    <TD>NaN</TD>"
			+ "    <TD>0.0</TD>"
			+ "  </TR>"
			+ "  <TR>"
			+ "    <TD>946352</TD>"
			+ "    <TD>FIRST J233916.9-111928</TD>"
			+ "    <TD>354.820408</TD>"
			+ "    <TD>-11.324472</TD>"
			+ "    <TD>946352</TD>"
			+ "    <TD>1.62</TD>"
			+ "    <TD>0.138</TD>"
			+ "    <TD>1.48</TD>"
			+ "    <TD>0.047</TD>"
			+ "    <TD>0.12</TD>"
			+ "    <TD>14.99</TD>"
			+ "    <TD>6.126</TD>"
			+ "  </TR>"
			+ "  <TR>"
			+ "    <TD>946331</TD>"
			+ "    <TD>FIRST J233846.4-111841</TD>"
			+ "    <TD>354.693467</TD>"
			+ "    <TD>-11.311506</TD>"
			+ "    <TD>946331</TD>"
			+ "    <TD>11.41</TD>"
			+ "    <TD>0.137</TD>"
			+ "    <TD>15.56</TD>"
			+ "    <TD>0.014</TD>"
			+ "    <TD>NaN</TD>"
			+ "    <TD>NaN</TD>"
			+ "    <TD>6.176</TD>"
			+ "  </TR>"
			+ "</TABLEDATA>"
			+ "</DATA>"
			+ "</TABLE>"
			+ "</RESOURCE>"
			+ "</VOTABLE>";
	
	
}
