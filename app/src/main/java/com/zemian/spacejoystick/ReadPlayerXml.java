package com.zemian.spacejoystick;

import android.content.res.AssetManager;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * Created by Oleksandr on 15/02/2017.
 */

public class ReadPlayerXml {

    private Document document;

    public ReadPlayerXml(AssetManager assets){
        try {
            InputStream is = assets.open("player.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            document = dBuilder.parse(is);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void readPlayer(PlayerData player){
        Element element=document.getDocumentElement();
        element.normalize();
        /*player.setName(element.getElementsByTagName("name").item(0).getTextContent());
        player.setHealth(element.getElementsByTagName("health").item(0).getTextContent());*/
    }

    public void saveTag(String tag, String value){
        try {
            Element element=document.getDocumentElement();
            element.normalize();
            element.getElementsByTagName(tag).item(0).setTextContent(value);
            System.out.println("Salvate: tag->" + tag + ", value->" + value);
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
