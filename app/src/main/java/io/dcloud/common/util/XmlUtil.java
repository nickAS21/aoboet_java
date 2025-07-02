package io.dcloud.common.util;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

import java.io.InputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/* loaded from: classes.dex */
public class XmlUtil {
    public static final int CDATA = 4;
    public static final int ELEMENT = 1;
    public static final int TEXT = 3;

    public static String getAttributeValue(DHNode dHNode, String str, String str2) {
        String attributeValue = getAttributeValue(dHNode, str);
        return attributeValue == null ? str2 : attributeValue;
    }

    public static int getAttributeValue(DHNode dHNode, String str, int i) {
        try {
            return Integer.parseInt(getAttributeValue(dHNode, str, String.valueOf(i)));
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return i;
        }
    }

    public static String getAttributeValue(DHNode dHNode, String str) {
        if (dHNode != null && (dHNode.mNode instanceof Element)) {
            return ((Element) dHNode.mNode).getAttribute(str);
        }
        return null;
    }

    public static String getText(DHNode dHNode) {
        StringBuffer stringBuffer = new StringBuffer();
        if (dHNode == null) {
            return stringBuffer.toString();
        }
        if (dHNode.mNode instanceof Element) {
            NodeList childNodes = ((Element) dHNode.mNode).getChildNodes();
            int length = childNodes.getLength();
            for (int i = 0; i < length; i++) {
                stringBuffer.append(childNodes.item(i).getNodeValue());
            }
        }
        return stringBuffer.toString();
    }

    public static void setText(DHNode dHNode, String str) {
        dHNode.mNode.setNodeValue(str);
    }

    public static String getElementName(DHNode dHNode) {
        return dHNode.mNode.getNodeName();
    }

    public static ArrayList<DHNode> getElements(DHNode dHNode, String str) {
        NodeList elementsByTagName;
        if (dHNode == null || !(dHNode.mNode instanceof Element) || (elementsByTagName = ((Element) dHNode.mNode).getElementsByTagName(str)) == null) {
            return null;
        }
        ArrayList<DHNode> arrayList = new ArrayList<>(2);
        int length = elementsByTagName.getLength();
        for (int i = 0; i < length; i++) {
            arrayList.add(new DHNode(elementsByTagName.item(i)));
        }
        return arrayList;
    }

    public static DHNode getElement(DHNode dHNode, String str) {
        Node item;
        if (dHNode == null || !(dHNode.mNode instanceof Element) || (item = ((Element) dHNode.mNode).getElementsByTagName(str).item(0)) == null) {
            return null;
        }
        return new DHNode(item);
    }

    public static DHNode getChild(DHNode dHNode, int i) {
        NodeList childNodes;
        if (dHNode == null || (childNodes = dHNode.mNode.getChildNodes()) == null) {
            return null;
        }
        return new DHNode(childNodes.item(i));
    }

    public static int getNodeType(DHNode dHNode, int i) {
        if (dHNode == null) {
            return -1;
        }
        return dHNode.mNode.getNodeType();
    }

    public static boolean isElement(Object obj) {
        return obj instanceof Element;
    }

    public static DHNode newElement(DHNode dHNode, String str) {
        return new DHNode(dHNode.mNode.getOwnerDocument().createElement(str));
    }

    public static void addChild(DHNode dHNode, DHNode dHNode2, int i) {
        dHNode.mNode.appendChild(dHNode2.mNode);
    }

    public static void removeChild(DHNode dHNode, int i) {
        dHNode.mNode.removeChild(dHNode.mNode.getChildNodes().item(i));
    }

    public static void removeChild(DHNode dHNode, DHNode dHNode2) {
        if (dHNode2 == null) {
            return;
        }
        dHNode.mNode.removeChild(dHNode2.mNode);
    }

    public static void setAttributeValue(DHNode dHNode, String str, String str2) {
        if (dHNode.mNode instanceof Element) {
            ((Element) dHNode.mNode).setAttribute(str, str2);
        }
    }

    public static DHNode XML_Parser(InputStream inputStream) {
        try {
            return new DHNode(((Document) XML_ParserDocument(inputStream).mNode).getDocumentElement());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static DHNode getRootElement(DHNode dHNode) {
        try {
            if (dHNode.mNode instanceof Document) {
                return new DHNode(((Document) dHNode.mNode).getDocumentElement());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static ArrayList<String[]> getAttributes(DHNode dHNode) {
        NamedNodeMap attributes;
        ArrayList<String[]> arrayList = null;
        if (dHNode == null) {
            return null;
        }
        if ((dHNode.mNode instanceof Element) && (attributes = ((Element) dHNode.mNode).getAttributes()) != null && attributes.getLength() > 0) {
            arrayList = new ArrayList<>();
            for (int i = 0; i < attributes.getLength(); i++) {
                Attr attr = (Attr) attributes.item(i);
                arrayList.add(new String[]{attr.getName(), attr.getValue(), attr.getNamespaceURI()});
            }
        }
        return arrayList;
    }

    public static void attrFill2HashMap(HashMap<String, String> hashMap, DHNode dHNode) {
        NamedNodeMap attributes;
        if (dHNode == null || hashMap == null || !(dHNode.mNode instanceof Element) || (attributes = ((Element) dHNode.mNode).getAttributes()) == null || attributes.getLength() <= 0) {
            return;
        }
        for (int i = 0; i < attributes.getLength(); i++) {
            Attr attr = (Attr) attributes.item(i);
            hashMap.put(attr.getName(), attr.getValue());
        }
    }

    public static DHNode newDocument() {
        try {
            return new DHNode(DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument());
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static boolean isText(DHNode dHNode) {
        return dHNode.mNode instanceof Text;
    }

    public static String getTextValue(DHNode dHNode) {
        return ((Text) dHNode.mNode).getNodeValue();
    }

    public static DHNode XML_ParserDocument(InputStream inputStream) {
        try {
            return new DHNode(DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(inputStream));
        } catch (Throwable th) {
            th.printStackTrace();
            return null;
        }
    }

    public static DHNode getElementDocument(DHNode dHNode) {
        return new DHNode(dHNode.mNode.getOwnerDocument());
    }

    public static void removeAttribute(DHNode dHNode, String str) {
        if (dHNode.mNode == null) {
            return;
        }
        dHNode.mNode.getAttributes().removeNamedItem(str);
    }

    public static String elementToString(DHNode dHNode) {
        return new StringWriter().toString();
    }

    /* loaded from: classes.dex */
    public static class DHNode {
        Node mNode;

        private DHNode(Node node) {
            this.mNode = node;
        }

        public boolean equals(Object obj) {
            if (obj instanceof DHNode) {
                return this.mNode.equals(((DHNode) obj).mNode);
            }
            return super.equals(obj);
        }
    }
}
