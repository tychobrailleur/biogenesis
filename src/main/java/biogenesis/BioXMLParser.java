/* Copyright (C) 2006-2010  Joan Queralt Molina
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 *
 */
package biogenesis;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

public class BioXMLParser implements ErrorHandler {
	protected DocumentBuilder builder = null;
	protected Document doc = null;
	
	public BioXMLParser() {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setValidating(true);
		try {
			builder = factory.newDocumentBuilder();
			builder.setErrorHandler(this);
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
	}
	
	public static void writeGeneticCode(PrintStream ps, GeneticCode geneticCode) {
		ps.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>"); //$NON-NLS-1$
		ps.println("<!DOCTYPE genetic_code ["); //$NON-NLS-1$
		ps.println("<!ELEMENT genetic_code (gene+)>"); //$NON-NLS-1$
		ps.println("<!ATTLIST genetic_code symmetry (1|2|3|4|5|6|7|8) #REQUIRED>"); //$NON-NLS-1$
		ps.println("<!ATTLIST genetic_code mirror (yes|no) #REQUIRED>"); //$NON-NLS-1$
		ps.println("<!ATTLIST genetic_code disperse (yes|no) #REQUIRED>"); //$NON-NLS-1$
		ps.println("<!ELEMENT gene EMPTY>"); //$NON-NLS-1$
		ps.println("<!ATTLIST gene length CDATA #REQUIRED>"); //$NON-NLS-1$
		ps.println("<!ATTLIST gene theta CDATA #REQUIRED>"); //$NON-NLS-1$
		ps.println("<!ATTLIST gene color (red|green|blue|cyan|yellow|gray|white) #REQUIRED>"); //$NON-NLS-1$
		ps.println("]>"); //$NON-NLS-1$
		ps.println("<genetic_code symmetry=\""+Integer.toString(geneticCode.getSymmetry())+ //$NON-NLS-1$
				"\" mirror=\""+(geneticCode.getMirror()==0?"no":"yes")+ //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				"\" disperse=\""+(geneticCode.getDisperseChildren()?"yes":"no")+  //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$
				"\">"); //$NON-NLS-1$
		for (int i=0; i<geneticCode.getNGenes(); i++)
			writeGene(ps,geneticCode.getGene(i));
		ps.println("</genetic_code>"); //$NON-NLS-1$
	}
	
	public static void writeGene(PrintStream ps, Gene gene) {
		ps.println("\t<gene length=\""+Double.toString(gene.getLength())+"\" theta=\""+ //$NON-NLS-1$ //$NON-NLS-2$
				Double.toString(gene.getTheta())+"\" color=\""+ //$NON-NLS-1$
				colorToString(gene.getColor())+"\" />"); //$NON-NLS-1$
	}
	
	public GeneticCode parseGeneticCode(File f) throws SAXException, IOException {
		int symmetry, mirror;
		boolean disperse;
		List<Gene> genes = new ArrayList<Gene>();
		String s;
		doc = builder.parse(f);
		Element geneticCode = doc.getDocumentElement();
		if (geneticCode.getNodeName().equals("genetic_code")) { //$NON-NLS-1$
			s = geneticCode.getAttribute("symmetry"); //$NON-NLS-1$
			try {
				symmetry = Integer.parseInt(s); //$NON-NLS-1$
			} catch (NumberFormatException e) {
				throw new SAXException("Symmetry has not an allowed value."); //$NON-NLS-1$
			}
			if (symmetry<1 || symmetry>8)
				throw new SAXException("Symmetry has not an allowed value."); //$NON-NLS-1$
			s = geneticCode.getAttribute("mirror"); //$NON-NLS-1$
			if (s.equals("yes")) //$NON-NLS-1$
				mirror = 1;
			else
				if (s.equals("no")) //$NON-NLS-1$
					mirror = 0;
				else
					throw new SAXException("Mirror has not an allowed value."); //$NON-NLS-1$
			s = geneticCode.getAttribute("disperse"); //$NON-NLS-1$
			if (s.equals("yes")) //$NON-NLS-1$
				disperse = true;
			else
				if (s.equals("no")) //$NON-NLS-1$
					disperse = false;
				else
					throw new SAXException("Disperse has not an allowed value."); //$NON-NLS-1$
			
			Node gene = geneticCode.getFirstChild();
			gene = getNextElement(gene);
			while (gene != null) {
				genes.add(parseGene((Element)gene));
				gene = getNextElement(gene.getNextSibling());
			}
			return new GeneticCode(genes, symmetry, mirror, disperse);
		}
		throw new SAXException("This file does not contain a genetic_code."); //$NON-NLS-1$
	}
	
	private static Node getNextElement(Node n) {
		while (n != null && n.getNodeType() != Node.ELEMENT_NODE) 
			n = n.getNextSibling();
		return n;
	}
	
	public Gene parseGene(Element gene) throws SAXException {
		double length, theta;
		Color color;
		if (gene.getNodeName().equals("gene")) { //$NON-NLS-1$
			try {
				length = Double.parseDouble(gene.getAttribute("length")); //$NON-NLS-1$
				theta = Double.parseDouble(gene.getAttribute("theta")); //$NON-NLS-1$
			} catch (NumberFormatException e) {
				throw new SAXException("Attributes length and theta do not exist or have not an allowed value."); //$NON-NLS-1$
			}
			try {
				color = stringToColor(gene.getAttribute("color")); //$NON-NLS-1$
			} catch (IllegalArgumentException e) {
				throw new SAXException("Attribute color does not exist or has not an allowed value."); //$NON-NLS-1$
			}
			return new Gene(length,theta,color);
		}
		throw new SAXException("Parse error. "+gene.getNodeName()+" found but gene expected.");  //$NON-NLS-1$//$NON-NLS-2$
	}
	
	private static Color stringToColor(String s) throws IllegalArgumentException {
		if (s.equals("green")) return Color.GREEN; //$NON-NLS-1$
		if (s.equals("red")) return Color.RED; //$NON-NLS-1$
		if (s.equals("blue")) return Color.BLUE; //$NON-NLS-1$
		if (s.equals("cyan")) return Color.CYAN; //$NON-NLS-1$
		if (s.equals("gray")) return Color.GRAY; //$NON-NLS-1$
		if (s.equals("white")) return Color.WHITE; //$NON-NLS-1$
		if (s.equals("yellow")) return Color.YELLOW; //$NON-NLS-1$
		throw new IllegalArgumentException();
	}
	
	private static String colorToString(Color c) {
		if (c.equals(Color.RED)) return "red"; //$NON-NLS-1$
		if (c.equals(Color.GREEN)) return "green"; //$NON-NLS-1$
		if (c.equals(Color.BLUE)) return "blue"; //$NON-NLS-1$
		if (c.equals(Color.CYAN)) return "cyan"; //$NON-NLS-1$
		if (c.equals(Color.WHITE)) return "white"; //$NON-NLS-1$
		if (c.equals(Color.GRAY)) return "gray"; //$NON-NLS-1$
		if (c.equals(Color.YELLOW)) return "yellow"; //$NON-NLS-1$
		return ""; //$NON-NLS-1$
	}

	public void warning(SAXParseException arg0) throws SAXException {
		throw new SAXException(arg0.getMessage());
	}

	public void error(SAXParseException arg0) throws SAXException {
		throw new SAXException(arg0.getMessage());
	}

	public void fatalError(SAXParseException arg0) throws SAXException {
		throw new SAXException(arg0.getMessage());
	}
}
