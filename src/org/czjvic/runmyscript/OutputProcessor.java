/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.czjvic.runmyscript;

import java.io.StringReader;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

/**
 *
 * @author josefvrba
 */
public class OutputProcessor {

    public OutputProcessor(String result) {
        try {

            DocumentBuilder dBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            System.out.println(result);
            Document doc = dBuilder.parse(new InputSource(new StringReader(result)));
            if (doc.hasChildNodes()) {
                printNote(doc.getChildNodes());
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }

    private static void printNote(NodeList nodeList) {

        for (int count = 0; count < nodeList.getLength(); count++) {

            Node tempNode = nodeList.item(count);

            // make sure it's element node.
            if (tempNode.getNodeType() == Node.ELEMENT_NODE) {

                if (tempNode.hasAttributes() && tempNode.getNodeName().equals("error")) {

                    // get attributes names and values
                    NamedNodeMap nodeMap = tempNode.getAttributes();

                    String message = "";
                    int column = 0;
                    int line = 0;
                    boolean isError = false;
                    for (int i = 0; i < nodeMap.getLength(); i++) {

                        Node node = nodeMap.item(i);

                        if (node.getNodeName() == "message") {
                            message = node.getNodeValue();
                        } else if (node.getNodeName() == "column") {
                            column = Integer.parseInt(node.getNodeValue());
                        } else if (node.getNodeName() == "line") {
                            line = Integer.parseInt(node.getNodeValue());
                        } else if (node.getNodeName() == "severity") {
                            isError = node.getNodeValue().equals("error");
                        }
                    }
                    message = "Line: " + line +", column: " + column + " - " + message;
                    Utils.addAnotation(line, message, isError);
                }

                if (tempNode.hasChildNodes()) {

                    // loop again if has child nodes
                    printNote(tempNode.getChildNodes());
                }
            }

        }

    }

}
