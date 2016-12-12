package NKCModel;

import java.io.File;
import org.w3c.dom.*;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 * 
 * The XMLReader class gets passed an xml file contianing the detil of a 
 * number of species of orgnaiztion and it returns these detail in an array
 * of Species objects.
 * 
 * @author Administrator
 *
 */
public class XMLReader
{

	//the species array 
	private Species[] species_of_organziation;
	
	/**
	 * Read the given file and return the array of species
	 * 
	 * @param file_name xml file name
	 * @return
	 */
    public Species[] readFile(String file_name)
    {
    	try 
    	{
    	//set up the xml reader
            DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
            Document doc = docBuilder.parse (new File(file_name));

            // normalize text representation            
            doc.getDocumentElement ().normalize ();

            //find out how may orgnaiztion are stored in the file
            NodeList listOfOrganizations = doc.getElementsByTagName("org");
            int total_organizations = listOfOrganizations.getLength();
            //initialize the array of species
            species_of_organziation = new Species[total_organizations];

            for(int i = 0; i<listOfOrganizations.getLength() ; i++)
            {//for each species of organiztion
                Node organization_species = listOfOrganizations.item(i);
                if(organization_species.getNodeType() == Node.ELEMENT_NODE)
                {
                    Element organization_element = (Element)organization_species;

					//extract the value of N                    
                    NodeList N_list = organization_element.getElementsByTagName("N");
                    Element N_element = (Element)N_list.item(0);

                    NodeList text_N_list = N_element.getChildNodes();
                    int N = Integer.valueOf(((Node)text_N_list.item(0)).getNodeValue().trim()).intValue();
                    
                    //extract the value of K     
                    NodeList K_list = organization_element.getElementsByTagName("K");
                    Element K_element = (Element)K_list.item(0);

                    NodeList text_K_list = K_element.getChildNodes();
                    int K = Integer.valueOf(((Node)text_K_list.item(0)).getNodeValue().trim()).intValue();

                    //extract the value of A  
                    NodeList A_list = organization_element.getElementsByTagName("A");
                    Element A_element = (Element)A_list.item(0);

                    NodeList text_A_list = A_element.getChildNodes();
                    int A = Integer.valueOf(((Node)text_A_list.item(0)).getNodeValue().trim()).intValue();
                    
                    //create a new species using the provided values 
                    Species s = new Species(N, K, A);
                    
                    //add it to the species array
                    species_of_organziation[i] = s;

                }//end of if clause

            }//end of for loop with s var

        }
    	catch (SAXParseException err) 
    	{
    		System.out.println ("** Parsing error" + ", line " 
    				+ err.getLineNumber () + ", uri " + err.getSystemId ());
    		System.out.println(" " + err.getMessage ());

        }
    	catch (SAXException e) 
    	{
    		Exception x = e.getException ();
    		((x == null) ? e : x).printStackTrace ();

        }
    	catch (Throwable t) 
    	{
    		t.printStackTrace ();
        }
        
    	return species_of_organziation;
    }

}