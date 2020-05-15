package com.ozz.demo.xml;

import java.io.FileNotFoundException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.w3c.dom.Node;

public class XmlDemo {
  public static void main(String[] args) throws FileNotFoundException, XPathExpressionException {
    new XmlDemo().getNode();
  }
  
  /**
   * 获取节点
   */
  public void getNode() throws FileNotFoundException, XPathExpressionException {
    XPathFactory xpfactory = XPathFactory.newInstance();
    XPath path = xpfactory.newXPath();
    
    String text = path.evaluate("/html/body/label", new InputSource(XmlDemo.class.getResourceAsStream("test.xml")));
    System.out.println(text);
    
    System.out.println("----");
    NodeList nodeList = (NodeList)path.evaluate("/html/body/label", new InputSource(XmlDemo.class.getResourceAsStream("test.xml")), XPathConstants.NODESET);
    System.out.println(nodeList.item(1).getTextContent());
    
    System.out.println("----");
    Node node = (Node)path.evaluate("/html/body", new InputSource(XmlDemo.class.getResourceAsStream("test.xml")), XPathConstants.NODE);
    System.out.println(node.getTextContent());
  }
  
  
}
