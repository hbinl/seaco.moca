package com.seaco.moca;

import android.graphics.Bitmap;
import android.os.Environment;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

/**
 * Created by HaoBin on 15/01/16.
 */
public class XMLHandlerSession {

    private static String UUID;
    private static String filename;
    private static String dirpath;
    private static String filepath;
    private static HashMap<String, String> sessionData;
    private Calendar c;

    private Document doc;
    private File file;
    private Node root;

    private long startTime;

    public boolean additionalPointForEducation;

    public int localeInt;


    public XMLHandlerSession(HashMap<String, String> userData, int lang) {

        localeInt = lang;
        c = Calendar.getInstance();
        startTime = c.getTimeInMillis();

        UUID = generateUUID();

        filename = generateFileName();
        dirpath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/SEACO/MOCA/";
        File dir = new File(dirpath);
        if(!dir.exists() && !dir.isDirectory()) {
            dir.mkdirs();
        }
        filepath = dirpath + filename;
        //System.out.println(filepath);
        createFile();

        root = doc.getFirstChild();
        initializeWithInitialUserSessionData(userData);
        }


    public void saveFinalMarks(int mark) {
        Element endTime = doc.createElement("TestEndedDateTime");
        Node val = doc.createTextNode(String.valueOf(getCurrentDateTime()));
        endTime.appendChild(val);

        Element totalTime = doc.createElement("TotalTimeElapsedInMilliSeconds");
        String elapsed = String.valueOf(getCurrentDateTimeUTC() - startTime);
        Node val_time = doc.createTextNode(elapsed);
        totalTime.appendChild(val_time);

        Element finalM = doc.createElement("total-marks");
        Node value = doc.createTextNode(String.valueOf(mark));
        finalM.appendChild(value);

        root.appendChild(finalM);
        root.appendChild(endTime);
        root.appendChild(totalTime);

        writeFile();
    }

    public boolean saveTestData(String nodeRoot, HashMap<String, String> map, boolean replace) {
        Element test = doc.createElement(nodeRoot);

        Map<String, String> mapx = new TreeMap<String, String>(map);
        for (Map.Entry<String, String> e : mapx.entrySet()) {
            Node key = doc.createElement(e.getKey());
            Node value = doc.createTextNode(e.getValue());
            key.appendChild(value);
            test.appendChild(key);
            //System.out.println(e.getKey() + " : " + e.getValue());
        }
        Node tests = doc.getElementsByTagName("moca-tests").item(0);

        if (doc.getElementsByTagName(nodeRoot).getLength() != 0) {
            if (replace) {
                for (int i=0; i<doc.getElementsByTagName(nodeRoot).getLength(); i++) {
                    tests.removeChild(doc.getElementsByTagName(nodeRoot).item(i));
                }
            } else {
                return false;
            }
        }
        tests.appendChild(test);
        writeFile();
        return true;


    }

    public long getCurrentDateTimeUTC() {
        c = Calendar.getInstance();
        return c.getTimeInMillis();
    }

    private void initializeWithInitialUserSessionData(HashMap<String, String> data) {
        Element subject = doc.createElement("subject");

        for (HashMap.Entry<String, String> e : data.entrySet()) {
            Node key = doc.createElement(e.getKey());
            Node value = doc.createTextNode(e.getValue());
            key.appendChild(value);
            subject.appendChild(key);
            //System.out.println(e.getKey() + " : " + e.getValue());
        }
        Node key = doc.createElement("TestStartedDateTime");
        Node dt = doc.createTextNode(getCurrentDateTime());
        key.appendChild(dt);
        root.appendChild(key);
        root.appendChild(subject);
        root.appendChild(doc.createElement("moca-tests"));
        writeFile();
    }

    public String getFilePath() {
        return filepath;
    }

    public String getDirpath() { return dirpath; }

    private void createFile() {
        try {
            //System.out.println("Start");
            file = new File(filepath);
            boolean fileCreated = file.createNewFile();
            //System.out.println(fileCreated);

            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            doc = dBuilder.newDocument();

            // root element
            Element rootElement = doc.createElement("seaco-moca");
            doc.appendChild(rootElement);

            writeFile();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void writeFile() {
        try {
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT,"yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
            DOMSource source = new DOMSource(doc);
            transformer.transform(source, new StreamResult(file));
            //transformer.transform(source, new StreamResult(System.out));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private String generateFileName() {
        return UUID + "_" + getCurrentDateTime() + ".xml";
    }

    public String getFilename() {
        return filename;
    }

    public String getCurrentDateTime() {
        c = Calendar.getInstance();

        String currentDateTime = c.get(Calendar.YEAR) + "-"
                + (c.get(Calendar.MONTH)+1)
                + "-" + c.get(Calendar.DAY_OF_MONTH)
                + "-" + String.format("%02d", c.get(Calendar.HOUR_OF_DAY))
                 + String.format("%02d", c.get(Calendar.MINUTE));
        return currentDateTime;
    }

    private String generateUUID() {
        String UUIDx = "";

        char[] ints = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};
        Random randomizer = new Random();
        for (int i = 0; i < 16; i++) {
            char random = ints[randomizer.nextInt(ints.length)];
            UUIDx += random;
        }
        return UUIDx;
    }

    public String getUUID() {
        return UUID;
    }

    public void saveImage(Bitmap bmap, String name) {
        String path = dirpath + filename + "_image_" + name + ".jpg";
        try {
            FileOutputStream fos = new FileOutputStream(path);
            bmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public int genRandomInt() {
        Random randomizer = new Random();
        return randomizer.nextInt(1000);
    }
}
