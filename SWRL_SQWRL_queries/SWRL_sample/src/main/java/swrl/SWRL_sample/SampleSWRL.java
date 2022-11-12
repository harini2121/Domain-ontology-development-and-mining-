package swrl.SWRL_sample;

import com.clarkparsia.pellet.owlapiv3.PelletReasonerFactory;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.dlsyntax.renderer.DLSyntaxObjectRenderer;

import org.semanticweb.owlapi.formats.OWLXMLDocumentFormat;
import org.semanticweb.owlapi.io.OWLObjectRenderer;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;
import org.semanticweb.owlapi.reasoner.SimpleConfiguration;
import org.semanticweb.owlapi.util.DefaultPrefixManager;
import org.semanticweb.owlapi.vocab.OWLRDFVocabulary;
import org.swrlapi.exceptions.SWRLBuiltInException;
import org.swrlapi.parser.SWRLParseException;
import org.swrlapi.sqwrl.SQWRLQueryEngine;
import org.swrlapi.sqwrl.SQWRLResult;

import java.io.File;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

import org.swrlapi.factory.SWRLAPIFactory;




public class Sampleswrl {
	
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
        
      //set up prefixes
        DefaultPrefixManager pm = new DefaultPrefixManager();
        pm.setDefaultPrefix(DOCUMENT_IRI + "#");
        pm.setPrefix("var:", "urn:swrl#");

        
    
        //class declarations
        OWLClass natureClass = factory.getOWLClass(":Nature", pm);
        manager.addAxiom(ontology, factory.getOWLDeclarationAxiom(natureClass));
        
        OWLClass FreeEntranceClass = factory.getOWLClass(":Free", pm);
        manager.addAxiom(ontology, factory.getOWLDeclarationAxiom(FreeEntranceClass));
        
        OWLClass HighClass = factory.getOWLClass(":High", pm);
        manager.addAxiom(ontology, factory.getOWLDeclarationAxiom(HighClass));
        
        OWLClass HotelClass = factory.getOWLClass(":Hotel", pm);
        manager.addAxiom(ontology, factory.getOWLDeclarationAxiom(HotelClass));
        
        
       // OWLClass englishProgrammerClass = factory.getOWLClass(":EnglishProgrammer", pm);
        //manager.addAxiom(ontology, factory.getOWLDeclarationAxiom(englishProgrammerClass));
        
      //object property declaration
        OWLObjectProperty attractiveProperty = createObjectProperty(ontology, pm, manager, ":attractive_with");
       // OWLObjectProperty attractiveProperty = createObjectProperty(ontology, pm, manager, ":attractive_with");
        
        //OWLObjectProperty hasKnowledgeOfProperty = createObjectProperty(ontology, pm, manager, ":hasKnowledgeOf");
 
       
      //named individuals declarations
        OWLNamedIndividual OpenSpace = createIndividual(ontology, pm, manager, ":Open Space");
        OWLNamedIndividual DutchArc = createIndividual(ontology, pm, manager, ":Dutch Architecture");
        //OWLNamedIndividual comp = createIndividual(ontology, pm, manager, ":Computer-Programming");
        //OWLNamedIndividual john = createIndividual(ontology, pm, manager, ":John");

        
        
        //axiom - EnglishProgrammers is equivalent to intersection of classes
        OWLObjectHasValue c1 = factory.getOWLObjectHasValue(attractiveProperty, OpenSpace);
        //OWLObjectHasValue c2 = factory.getOWLObjectHasValue(hasKnowledgeOfProperty, comp);
        OWLObjectIntersectionOf andExpr = factory.getOWLObjectIntersectionOf(natureClass, c1);
        manager.addAxiom(ontology, factory.getOWLEquivalentClassesAxiom(FreeEntranceClass, andExpr));  
        
        OWLObjectHasValue c2 = factory.getOWLObjectHasValue(attractiveProperty, DutchArc);
        //OWLObjectHasValue c2 = factory.getOWLObjectHasValue(hasKnowledgeOfProperty, comp);
        OWLObjectIntersectionOf andExpr1 = factory.getOWLObjectIntersectionOf(HotelClass, c2);
        manager.addAxiom(ontology, factory.getOWLEquivalentClassesAxiom(HighClass, andExpr1));   
        
        
        //SWRL rule - NaturePLace,attractive with(?x,OpenSpace)->FreeEntrance(?x)
        SWRLVariable varX = factory.getSWRLVariable(pm.getIRI("var:x"));
        Set<SWRLAtom> body = new LinkedHashSet<>();
        body.add(factory.getSWRLClassAtom(natureClass, varX));
        body.add(factory.getSWRLObjectPropertyAtom(attractiveProperty, varX, factory.getSWRLIndividualArgument(OpenSpace)));
        //body.add(factory.getSWRLObjectPropertyAtom(hasKnowledgeOfProperty, varX, factory.getSWRLIndividualArgument(comp)));
        Set<? extends SWRLAtom> head = Collections.singleton(factory.getSWRLClassAtom(FreeEntranceClass, varX));
        SWRLRule swrlRule = factory.getSWRLRule(body, head);
        manager.addAxiom(ontology, swrlRule);
        
      //SWRL rule - HotelPLace,attractive with(?x,Dutch architecture)->HighClass(?x)
        SWRLVariable varY = factory.getSWRLVariable(pm.getIRI("var:y"));
        Set<SWRLAtom> body1 = new LinkedHashSet<>();
        body1.add(factory.getSWRLClassAtom(HotelClass, varY));
        body1.add(factory.getSWRLObjectPropertyAtom(attractiveProperty, varY, factory.getSWRLIndividualArgument(DutchArc)));
        //body.add(factory.getSWRLObjectPropertyAtom(hasKnowledgeOfProperty, varX, factory.getSWRLIndividualArgument(comp)));
        Set<? extends SWRLAtom> head1 = Collections.singleton(factory.getSWRLClassAtom(HighClass, varY));
        SWRLRule swrlRule1 = factory.getSWRLRule(body1, head1);
        manager.addAxiom(ontology, swrlRule1);
        
        
        
        listSWRLRules(ontology, pm);

        
        
    }
    
    
    private static OWLNamedIndividual createIndividual(OWLOntology ontology, DefaultPrefixManager pm, OWLOntologyManager manager, String name) {
        OWLDataFactory factory = manager.getOWLDataFactory();
        OWLNamedIndividual individual = factory.getOWLNamedIndividual(name, pm);
        manager.addAxiom(ontology, factory.getOWLDeclarationAxiom(individual));
        return individual;
    }
    
    private static OWLObjectProperty createObjectProperty(OWLOntology ontology, DefaultPrefixManager pm, OWLOntologyManager manager, String name) {
        OWLDataFactory factory = manager.getOWLDataFactory();
        OWLObjectProperty objectProperty = factory.getOWLObjectProperty(name, pm);
        manager.addAxiom(ontology, factory.getOWLDeclarationAxiom(objectProperty));
        return objectProperty;


    } 
     
        
    public static void listSWRLRules(OWLOntology ontology, DefaultPrefixManager pm) {
        OWLObjectRenderer renderer = new DLSyntaxObjectRenderer();
        for (SWRLRule rule : ontology.getAxioms(AxiomType.SWRL_RULE)) {
            System.out.println("Created SWRL Rule is = "+renderer.render(rule));
        }


}

}