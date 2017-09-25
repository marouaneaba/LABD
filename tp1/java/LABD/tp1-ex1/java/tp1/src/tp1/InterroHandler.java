package tp1;

import org.xml.sax.* ;
import org.xml.sax.helpers.* ;

public class InterroHandler extends DefaultHandler {	
	private int sum ;
	private boolean fermante ;
	private boolean surfaceCacul ;
	private boolean parentsHadSurface ;
	
	public void startDocument() {this.sum = 0 ; this.fermante = false ; this.surfaceCacul = false;}
	
	public void endDocument() {System.out.println() ;}
	
	public void startElement(String nameSpaceURI, 
							 String localName, 
							 String rawName, 
							 Attributes attributs)  {
		if(localName.equals("maison")){
			if (attributs.getLength() != 0) 
				System.out.println(" Maison : "+attributs.getValue("id")+"\n") ;
			
		}
		
		if(localName.equals("RDC") || localName.equals("étage"))
			surfaceCacul = true;
		
		
		for (int index = 0; index < attributs.getLength(); index++) { 
				if(attributs.getLocalName(index).equals("surface-m2"))
					parentsHadSurface = true;
		} 
		
	
		
		if(this.surfaceCacul && !this.parentsHadSurface){
			this.sum += Integer.parseInt(attributs.getValue("surface-m2"));
		}	
		
		if (this.fermante) 
			System.out.print("superfice total : " + this.sum + "m2 \n");
		//this.n++ ;
		//this.fermante = false ;
	
	}
	
	public void endElement(	String nameSpaceURI, 
						   String localName, 
						   String rawName)  {
		
		if(localName.equals("maison")){
			this.fermante = true ;     
			this.sum = 0;
		}
			
		this.surfaceCacul = false;
	}
	
	public static void main(String[] args) {
		try {
			XMLReader saxReader = XMLReaderFactory.createXMLReader();
			saxReader.setContentHandler(new InterroHandler());
			saxReader.parse(args[0]);
		} catch (Exception t) {
			t.printStackTrace();
		}
	}
	
}