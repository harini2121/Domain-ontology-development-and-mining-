package swrl.SWRL_sample;

import java.io.File;


import java.util.Set;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.dlsyntax.renderer.DLSyntaxObjectRenderer;
import org.semanticweb.owlapi.io.OWLObjectRenderer;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.reasoner.Node;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;
import org.semanticweb.owlapi.reasoner.SimpleConfiguration;
import org.semanticweb.owlapi.reasoner.structural.StructuralReasonerFactory;
import org.semanticweb.owlapi.util.DefaultPrefixManager;
import org.semanticweb.owlapi.vocab.OWL2Datatype;
import org.swrlapi.core.SWRLRuleEngine;
import org.swrlapi.exceptions.SWRLBuiltInException;
import org.swrlapi.factory.SWRLAPIFactory;
import org.swrlapi.parser.SWRLParseException;
import org.swrlapi.sqwrl.SQWRLQueryEngine;
import org.swrlapi.sqwrl.SQWRLResult;



public class SamapleSQWRL {

	
	
	//private static final String DOCUMENT_IRI = "http://acrab.ics.muni.cz/ontologies/example.owl";
    private static OWLObjectRenderer renderer = new DLSyntaxObjectRenderer();

    public static void main(String[] args) throws OWLOntologyCreationException, OWLOntologyStorageException, SWRLBuiltInException, SWRLParseException {

        OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
        OWLDataFactory factory = manager.getOWLDataFactory();
        File file = new File("C://Users//Dinesh//Documents//Dat MiningSem2//CW-DM/SLtourism.owl");
	    
      //Load the ontology from the file
	    OWLOntology ontology = manager.loadOntologyFromOntologyDocument(file);
	    
	  //Check if the ontology contains any axioms
	    System.out.println("Number of axioms: " + ontology.getAxiomCount());		   
        //OWLDataFactory factory = manager.getOWLDataFactory();
        
        String DOCUMENT_IRI = "http://SLtourism";
        

        //OWLReasonerFactory reasonerFactory = new StructuralReasonerFactory();
        //OWLReasoner reasoner = reasonerFactory.createReasoner(ontology, new SimpleConfiguration());
        //reasoner.precomputeInferences();
        //boolean consistent = reasoner.isConsistent();
        //System.out.println("Consistent: " + consistent);
        //Node<OWLClass> bottomNode = reasoner.getUnsatisfiableClasses();
        //Set<OWLClass> unsatisfiable = bottomNode.getEntitiesMinusBottom();
        //if (!unsatisfiable.isEmpty()) {
          //System.out.println("The following classes are unsatisfiable: ");
          //for (OWLClass cls : unsatisfiable) {
            //System.out.println(" " + cls);
          //}
        //} else {
          //System.out.println("There are no unsatisfiable classes");
        //}

        
        
        SQWRLQueryEngine qE= SWRLAPIFactory.createSQWRLQueryEngine(ontology);
        try {
          SQWRLResult rslt=qE.runSQWRLQuery("Q1","#Place(#nature_1)^ #attractive_with(#nature_1,?x)->sqwrl:select(?x)");
          while(rslt.next()){
            System.out.println("Name: "+rslt.getNamedIndividual("x"));
          }
        } catch (SWRLParseException e) {
          e.printStackTrace();
        }
        
	
    }
	
	
}
