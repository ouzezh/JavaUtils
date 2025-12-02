package com.ozz.demo.xml;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

@Slf4j
public class XmlDemo {
  public static void main(String[] args) {
    new XmlDemo().getNode();
  }
  
  /**
   * 获取节点
   */
  @SneakyThrows
  public void getNode() {
    XPathFactory xpfactory = XPathFactory.newInstance();
    XPath path = xpfactory.newXPath();
    
    String text = path.evaluate("/html/body/label", new InputSource(XmlDemo.class.getResourceAsStream("test.xml")));
    log.info(text);
    
    log.info("----");
    NodeList nodeList = (NodeList)path.evaluate("/html/body/label", new InputSource(XmlDemo.class.getResourceAsStream("test.xml")), XPathConstants.NODESET);
    log.info(nodeList.item(1).getTextContent());

    log.info("----");
    Node node = (Node)path.evaluate("/html/body", new InputSource(XmlDemo.class.getResourceAsStream("test.xml")), XPathConstants.NODE);
    log.info(node.getTextContent());
  }
  
  
}
