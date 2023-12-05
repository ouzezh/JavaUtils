package com.ozz.demo.xml;

import cn.hutool.log.StaticLog;
import lombok.SneakyThrows;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

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
    StaticLog.info(text);
    
    StaticLog.info("----");
    NodeList nodeList = (NodeList)path.evaluate("/html/body/label", new InputSource(XmlDemo.class.getResourceAsStream("test.xml")), XPathConstants.NODESET);
    StaticLog.info(nodeList.item(1).getTextContent());
    
    StaticLog.info("----");
    Node node = (Node)path.evaluate("/html/body", new InputSource(XmlDemo.class.getResourceAsStream("test.xml")), XPathConstants.NODE);
    StaticLog.info(node.getTextContent());
  }
  
  
}
