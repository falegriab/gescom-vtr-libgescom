package ivrbase;


import ivrbase.KVPair;

import java.io.*;
import java.util.*;

import javax.xml.parsers.*;

import org.w3c.dom.*;


public class KVPairList
{
    private ArrayList KeyList = new ArrayList(1);
    private ArrayList ValueList = new ArrayList(1);
    private int auxI = -1;

    public void clear ()
    {
        KeyList.clear();
        ValueList.clear();
        return;
    }

    public boolean add (String Key, String Value)
    {
        if ( KeyList.add(Key) )
        {
            // if ( ValueList.add(Value) )
            if ( ValueList.add(new KVPair(Key, Value)) )
            {
                return true;
            }
        }

        return false;
    }

    public boolean add (String Key, int Value)
    {
        if ( KeyList.add(Key) )
        {
            // if ( ValueList.add(Value) )
            if ( ValueList.add(new KVPair(Key, Value)) )
            {
                return true;
            }
        }

        return false;
    }

    public boolean add (String Key, float Value)
    {
        if ( KeyList.add(Key) )
        {
            // if ( ValueList.add(Value) )
            if ( ValueList.add(new KVPair(Key, Value)) )
            {
                return true;
            }
        }

        return false;
    }

    public boolean add (String Key, boolean Value)
    {
        if ( KeyList.add(Key) )
        {
            // if ( ValueList.add(Value) )
            if ( ValueList.add(new KVPair(Key, Value)) )
            {
                return true;
            }
        }

        return false;
    }

    public boolean add (String Key, KVPairList Value)
    {
        if ( KeyList.add(Key) )
        {
            // if ( ValueList.add(Value) )
            if ( ValueList.add(new KVPair(Key, Value)) )
            {
                return true;
            }
        }

        return false;
    }

    public boolean remove (String Key)
    {
        String sKey = "";
        String sValue = "";

        if ( (auxI = KeyList.indexOf(Key)) == -1 ) return false;

        sKey = (String) KeyList.get(auxI);
        sValue = (String) ValueList.get(auxI);

        if ( KeyList.remove(sKey) )
        {
            if ( ValueList.remove(sValue) )
            {
                return true;
            }
        }

        return false;
    }

    public int count ()
    {
        return KeyList.size();
    }

    public boolean find (String Key)
    {
        if ( KeyList.indexOf(Key) != -1 ) return true;

        return false;
    }

    public String getKey (int Index)
    {
        return (String) KeyList.get(Index);
    }

    public String getValue (int Index)
    {
        // return (String) ValueList.get(Index);
        return (String) ((KVPair) ValueList.get(Index)).getValue();
    }

    public String getValue (String Key)
    {
        if ( (auxI = KeyList.indexOf(Key)) == -1 ) return "";

        // return (String) ValueList.get(auxI);
        return (String) ((KVPair) ValueList.get(auxI)).getValue();
    }

    public KVPair getItem (int Index)
    {
        // return (String) ValueList.get(Index);
        return (KVPair) ValueList.get(Index);
    }

    public KVPair getItem (String Key)
    {
        if ( (auxI = KeyList.indexOf(Key)) == -1 ) return null;

        // return (String) ValueList.get(Index);
        return (KVPair) ValueList.get(auxI);
    }

    public String toXMLString(boolean IncludeHeader)
    {
        String XML = "";

        if (IncludeHeader) XML += "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";

        for( auxI = 0; auxI < KeyList.size(); auxI++ )
        {
            KVPair auxKVP = (KVPair)ValueList.get(auxI);

            XML += "<" + auxKVP.Key + " ";

            switch( auxKVP.Type )
            {
                case 1:
                    XML += "Type=\"KVTypeString\">";
                    XML += auxKVP.getValue();
                case 2:
                    XML += "Type=\"KVTypeInt\">";
                    XML += auxKVP.getValue();
                case 3:
                    XML += "Type=\"KVTypeFloat\">";
                    XML += auxKVP.getValue();
                case 4:
                    XML += "Type=\"KVTypeBoolean\">";
                    XML += auxKVP.getValue();
                case 5:
                    XML += "Type=\"KVTypeList\">";
                    XML += auxKVP.ListValue.toXMLString(false);
            }

            XML += "</" + auxKVP.Key + ">";
        }

        return XML;
    }

    public String toXMLString()
    {
        return toXMLString(true);
    }

    private void DisplayNode (Node CurrNode, KVPairList KVPList)
    {
        if( CurrNode == null )
            return;

        if( (CurrNode.getNodeName().compareTo("#text") == 0) && (CurrNode.getChildNodes().getLength() == 0) )
            return;

        if( CurrNode.getChildNodes().getLength() == 0 )
        {
            KVPList.add(CurrNode.getNodeName(), "");
        }
        else if( (CurrNode.getChildNodes().getLength() == 1) && (CurrNode.getChildNodes().item(0).getNodeType() == Node.TEXT_NODE) )
        {
            //if( CurrNode.getChildNodes().item(0).getNodeType() == Node.TEXT_NODE )
            //{
                KVPList.add(CurrNode.getNodeName(), CurrNode.getChildNodes().item(0).getNodeValue());
            //}
        }
        else
        {
            NodeList childNodes = CurrNode.getChildNodes();

            if( childNodes != null )
            {
                int length = childNodes.getLength();
                KVPairList myKVPList = new KVPairList();

                for(int loopIndex = 0; loopIndex < length; loopIndex++)
                {
                    DisplayNode(childNodes.item(loopIndex), myKVPList);
                }

                KVPList.add(CurrNode.getNodeName(), myKVPList);
            }
        }
    }

    public boolean parseXML (InputStream XMLInputStream)
    {
        this.clear();

        try
        {
            // Parsea el XML
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse( XMLInputStream );

            // Normaliza el �rbol
            document.getDocumentElement().normalize();

            // Se recorre el �rbol
            Element root = document.getDocumentElement();
            DisplayNode(root, this);

            XMLInputStream.close();
        }

        catch( Exception e )
        {
            try
            {
            XMLInputStream.close();
            }
            catch(Exception e2)
            {
            }

            return false;
        }

        return true;
    }

    public boolean parseXMLText (String XMLText)
    {
        byte[] bXMLText = XMLText.getBytes();
        ByteArrayInputStream isXMLText = new ByteArrayInputStream(bXMLText);

        return parseXML(isXMLText);
    }

    public boolean parseXMLFile (String XMLFile)
    {
        try
        {
            File fXMLFile = new File(XMLFile);
            FileInputStream isXMLFile = new FileInputStream(fXMLFile);

            return parseXML(isXMLFile);
        }

        catch( Exception e )
        {
            return false;
        }
    }
}