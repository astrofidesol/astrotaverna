package org.purl.wf4ever.astrotaverna.pdl;

import static org.purl.wf4ever.astrotaverna.pdl.PDLServiceParameterHealthCheck.CONNECTED_TO_NON_PDL;
import static org.purl.wf4ever.astrotaverna.pdl.PDLServiceParameterHealthCheck.NON_METADATA_ERROR;
//important status constants
import static org.purl.wf4ever.astrotaverna.pdl.PDLServiceParameterHealthCheck.NO_ERROR;
import static org.purl.wf4ever.astrotaverna.pdl.PDLServiceParameterHealthCheck.PRECISION_ERROR;
import static org.purl.wf4ever.astrotaverna.pdl.PDLServiceParameterHealthCheck.SKOS_ERROR;
import static org.purl.wf4ever.astrotaverna.pdl.PDLServiceParameterHealthCheck.TYPE_ERROR;
import static org.purl.wf4ever.astrotaverna.pdl.PDLServiceParameterHealthCheck.UCD_ERROR;
import static org.purl.wf4ever.astrotaverna.pdl.PDLServiceParameterHealthCheck.UNIT_ERROR;
import static org.purl.wf4ever.astrotaverna.pdl.PDLServiceParameterHealthCheck.UNKNOWN;
import static org.purl.wf4ever.astrotaverna.pdl.PDLServiceParameterHealthCheck.UTYPE_ERROR;

import javax.swing.JComponent;

import net.sf.taverna.t2.lang.ui.ReadOnlyTextArea;
import net.sf.taverna.t2.visit.VisitKind;
import net.sf.taverna.t2.visit.VisitReport;
import net.sf.taverna.t2.workbench.report.explainer.VisitExplainer;

/**
 * 
 * @author Julian Garrido
 */
public class PDLServiceParameterHealthCheckVisitExplainer implements VisitExplainer{

	@Override
	public boolean canExplain(VisitKind vk, int resultId) {
		return (vk instanceof PDLServiceParameterHealthCheck);
	}

	/**
	   * This class only handles {@link VisitReport} instances that are of
	   * {@link PDLServiceParameterHealthCheck} kind. Therefore, decisions on
	   * the explanations / solutions are made solely by visit result IDs.
	   */
	@Override
	public JComponent getExplanation(VisitReport vr) {
		int resultId = vr.getResultId();
		String explanation = "";
		//explanation = "puedo explicarlo";
		
		if(resultId == NO_ERROR){
			explanation = "No problem found.\n";
		}
		if((resultId & PRECISION_ERROR) == PRECISION_ERROR){
			explanation = explanation + "Precision metadata don't match in the sink and source ports.\n";
		}
		if((resultId & TYPE_ERROR) == TYPE_ERROR){
			explanation = explanation + "Types don't match in the sink and source ports.\n";
		}
		if((resultId & UTYPE_ERROR) == UTYPE_ERROR){
			explanation = explanation + "UTypes don't match in the sink and source ports.\n";
		}
		if((resultId & UCD_ERROR) == UCD_ERROR){
			explanation = explanation + "UCDs don't match in the sink and source ports.\n";
		}
		if((resultId & UNIT_ERROR) == UNIT_ERROR){
			explanation = explanation + "Units don't match in the sink and source ports.\n";
		}
		if((resultId & SKOS_ERROR) == SKOS_ERROR){
			explanation = explanation + "SKOS concepts don't match in the sink and source ports.\n";
		}
		if((resultId & CONNECTED_TO_NON_PDL) == CONNECTED_TO_NON_PDL){
			explanation = explanation + "If output-input pairs don't have PDL descriptions for both services, the interoperability cannot be guaranteed.\n";
		}
		if(resultId == UNKNOWN){
			explanation = explanation + "Unknown issue - no expalanation available.\n";
		}
		if(resultId == NON_METADATA_ERROR){
			explanation = explanation + "There is no metadata to verify the interoperability.\n";
		}
		
		return new ReadOnlyTextArea(explanation);
	}

	/**
	   * This class only handles {@link VisitReport} instances that are of
	   * {@link PDLServiceParameterHealthCheck} kind. Therefore, decisions on
	   * the explanations / solutions are made solely by visit result IDs.
	   */
	@Override
	public JComponent getSolution(VisitReport vr) {
		// TODO Auto-generated method stub
		int resultId = vr.getResultId();
	    String explanation = "";
	    boolean includeConfigButton = false;
	    //explanation = "puedo solucinarlo";
	    
	    if(resultId == NO_ERROR){
			explanation = "No change necessary.\n";
	    }else{
	    	if((resultId & PRECISION_ERROR) == PRECISION_ERROR)
	    		explanation = explanation + "Check the sink activity requires more precession that the source provides.\n";
			
	    	if((resultId & TYPE_ERROR) == TYPE_ERROR)
				explanation = explanation + "Check the types and consider using a beanshell for small transformations.\n";
			
			if((resultId & UTYPE_ERROR) == UTYPE_ERROR)
				explanation = explanation + "Check if source and sink ports corresponds to the same concept in a VO datamodel or if they are equivalent.\n";
			
			if((resultId & UCD_ERROR) == UCD_ERROR)
				explanation = explanation + "Check if source and sink ports correspond to equivalent concepts (UCDs).\n";
			
			if((resultId & UNIT_ERROR) == UNIT_ERROR)
				explanation = explanation + "Check if you need a conversion factor between source and sink or if they are not compatible.\n";
			
			if((resultId & SKOS_ERROR) == SKOS_ERROR)
				explanation = explanation + "Check if source and sink ports are represented by equivalent concepts.\n";
	    }
		if(resultId == UNKNOWN)
			explanation = explanation + "Unknown issue - no solution available\n";
		
		if(resultId == NON_METADATA_ERROR)
			explanation = explanation + "Ask the service provider for a more detailed PDL description.\n";
		
	    
		return new ReadOnlyTextArea(explanation);
		
		//I can use the next code for being able to open the config activity dialog from a buttom. 
		//this code has dependencies
		/*
	    JPanel jpSolution = new JPanel();
	    jpSolution.setLayout(new BoxLayout(jpSolution, BoxLayout.Y_AXIS));
	    
	    ReadOnlyTextArea taExplanation = new ReadOnlyTextArea(explanation);
	    taExplanation.setAlignmentX(Component.LEFT_ALIGNMENT);
	    jpSolution.add(taExplanation);
	    
	    if (includeConfigButton)
	    {
	      JButton button = new JButton();
	      Processor p = (Processor) (vr.getSubject());
	      button.setAction(new ReportViewConfigureAction(p));
	      button.setText("Open REST Activity configuration dialog");
	      button.setAlignmentX(Component.LEFT_ALIGNMENT);
	      
	      jpSolution.add(button);
	    }
	    
	    
	    return (jpSolution);
	    */
	}
	
	

}
