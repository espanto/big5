/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;

import weka.classifiers.Classifier;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.PropertyConfigurator;
import weka.classifiers.meta.FilteredClassifier;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.filters.unsupervised.attribute.Remove;
/**
 *
 * @author sun
 */
public class Model {
static Properties props;
    
    private static String MODEL = "";   //model=/Users/sun/Documents/big5/models/MultilayerPerceptron.model;
    private static String TEST = "";    //test=/Users/sun/Documents/big5/data/pan-ap2015-test/arff/pan-ap2015-test-es-test-gender-extended.arff
    private static String BOW = "";     ///Users/sun/Documents/big5/data/pan-ap2015-test/bow.txt
    private static String NATTRIB = "1000";
    private static String TEST_UNLABELED = "";    //test_unlabeled=/Users/sun/Documents/big5/data/pan-ap2015-test/arff/pan-ap2015-test-es-test-gender-extended_unlabeled.arff    
    private static String TEST_LABELED = "";    //test_labeled=/Users/sun/Documents/big5/data/pan-ap2015-test/arff/pan-ap2015-test-es-test-gender-extended_labeled.arff        
    public static void main(String[] args) {
        FileWriter fw = null;
        
        try {
            
            PropertyConfigurator.configure("data.properties");
            
            configureProperties("data.properties");                        
            
            ChooseModel("extroverted");
            //Build the classifier loading Model from weka file 
            Classifier oClassifier = LoadClassifier(MODEL);
            String text = "Ejemplo de frase a vectorizar username";
            //int nAttrib = Integer.getInteger(NATTRIB);
            ArrayList<Double> Features = Text2Vector(text, BOW, 1004);
            //If the data is unlabeled we need the next step
//            ClassifierUnlabeledDataset(oClassifier, TEST_LABELED, TEST_UNLABELED);
            //If the data is labeled we can split the step before
            Instances data = CreateInstances(Features);
            //Prediction with model and data
            Predict(data, oClassifier);
            
        }catch (Exception ex) {
            System.out.println(ex.toString());
            
        }
    } 
       
    /**
     * Function to choose a model exported from Weka
     * 
     * @param modelName
     */
    public static void ChooseModel(String modelName){
       MODEL = MODEL.replace("{task}", modelName); 
    }
   
    /**
     * Function to load a model exported from Weka
     * 
     * @param pathName
     * @return 
     */
    private static Classifier LoadClassifier(String pathName)
    {
        Classifier oClassifier = null;
        try
        {
            java.io.ObjectInput oi = new java.io.ObjectInputStream(new java.io.FileInputStream(pathName));
            Object o = oi.readObject();
            oClassifier = (weka.classifiers.Classifier)o;
            oi.close();
        }
        catch (IOException ex)
        {
            String sDescription = ex.toString();
            String s = sDescription;
        }
        catch (ClassNotFoundException ex)
        {
            String sDesc = ex.toString();
            String s = sDesc;
        }
        catch (Error e)
        {
            String sDescription = e.getMessage();
            String s = sDescription;
        }
        catch (Exception ex)
        {
            String sDescription = ex.toString();
            String s = sDescription;
        }

        return oClassifier;
    }
   
    /**
     * Function to convert a sentence in a features vector
     * 
     * @param text
     * @param bow
     * @param nAttrib
     * @return features
     */
    private static ArrayList<Double> Text2Vector(String text, String bowPath, int nAttrib)
    {        
        // Read the bag of words
        ArrayList<String> bow = new ArrayList();
        bow = ReadBOW(bowPath, nAttrib);
        
        // Find every word of the sentence in the bow and create the features
        ArrayList<Double> features = new ArrayList(Collections.nCopies(nAttrib, 0.0));
        //        FastVector features = new FastVector(nAttrib);
        
        // Build the features vector normalized
        int sizei = features.size();
        String []data = text.split(" ");
        for (String word : data){
            int i = 0;
            for (String bagword : bow){
                i++;
                if (bagword.toLowerCase().equals(word.toLowerCase())){
                    features.set(i, features.get(i) + 1/bow.size());
                    break;
                }
            }
        }        
        
        return features;
    }
/**
     * Function to load a model exported from Weka
     * 
     * @param pathName
     * @return 
     */
    private static Classifier LoadFilteredClassifier(String pathName)
    {
        // filter
        Remove rm = new Remove();
        rm.setAttributeIndices("1");  // remove 1st attribute
        
        FilteredClassifier oClassifier = new FilteredClassifier();
        oClassifier.setFilter(rm);
        
        try
        {
            java.io.ObjectInput oi = new java.io.ObjectInputStream(new java.io.FileInputStream(pathName));
            Object o = oi.readObject();
            oClassifier = (FilteredClassifier)o;
            oi.close();
        }
        catch (IOException ex)
        {
            String sDescription = ex.toString();
            String s = sDescription;
        }
        catch (ClassNotFoundException ex)
        {
            String sDesc = ex.toString();
            String s = sDesc;
        }
        catch (Error e)
        {
            String sDescription = e.getMessage();
            String s = sDescription;
        }
        catch (Exception ex)
        {
            String sDescription = ex.toString();
            String s = sDescription;
        }
        
        return oClassifier;
    }

    
    /* Código para crear una instancia a partir de tu vector de features (O AQUÍ EN EL EJEMPLO PARA MÁS DE UNA INSTANCIA) */
    private static Instances CreateInstances(ArrayList features) {
    Instances instances = null;

    try
    {     
        // Get the number of attributes
        int nAttrib = features.size();
        
        // Create attributes
        ArrayList<Attribute> atts = new ArrayList<Attribute>();
        for (int i = 0; i < nAttrib; i++) {
            atts.add(new Attribute("Attribute" + i, i));
        }
        int iClass = nAttrib;
        atts.add(new Attribute("class",iClass));
        
        // Create the instance, set the feature value for every att
        Instance inst = new DenseInstance(nAttrib + 1);
        for (int j=0;j<nAttrib;j++) {
            double value = Double.parseDouble(features.get(j).toString());
            inst.setValue(atts.get(j), value);
        }
        //Add the class, don't mind the value because we will predict it
        inst.setValue(atts.get(iClass), 0.0);
        
        //Define Instances
        instances = new Instances("modeloDePrueba", atts, nAttrib);
        instances.setClassIndex(iClass);
        instances.add(inst);
    }

/*
            // PARA ACCEDER A LA PRIMERA INSTANCIA 
        mInstance = instances.instance(0);
        
        
        //Sin FastVector
        ArrayList<Attribute> atts = new ArrayList<Attribute>();
        for (int att = 0; att < numAtts; att++) {
            atts.add(new Attribute("Attribute" + att, att));
        }
    
        // Create an empty training set
        Instances isTrainingSet = new Instances("ModelodePrueba", fvWekaAttributes, 10);
    

    */
    catch (Exception ex)
    {
            System.out.println(ex.toString());
    }
    return instances;
}


    public static double Predict(Instances data, Classifier oClassifier) {
        double pred = 0.0;
        
        try {
            //ArrayList<String> Features = Text2Vector(text);
            //Instance inst = GetInstance(Features);
            for (int i = 0; i < data.numInstances(); i++) {
              Instance instance = data.instance(i);
              pred = oClassifier.classifyInstance(instance);
              System.out.print("ID: " + data.instance(i).value(0));
              //System.out.print(", actual: " + data.classAttribute().value((int) data.instance(i).classValue()));
              //System.out.println(", predicted: " + data.classAttribute().value((int) pred));
              System.out.println(", predicted: " + pred);
            }
        } catch (Exception ex) {
            System.out.println(ex.toString());
        }
        
        return pred;
    }

    
    /**
     *  Classifing unlabeled dataset
     * @param oClassifier 
     */
    public static Classifier ClassifierUnlabeledDataset(Classifier oClassifier, String labeledData, String unlabeledData) throws Exception{
    
        try {
            // load unlabeled data
            Instances unlabeled = new Instances(
                                    new BufferedReader(
                                      new FileReader(unlabeledData)));

            // set class attribute
            unlabeled.setClassIndex(unlabeled.numAttributes() - 1);

            // create copy
            Instances labeled = new Instances(unlabeled);

            // label instances
            for (int i = 0; i < unlabeled.numInstances(); i++) {
              double clsLabel = oClassifier.classifyInstance(unlabeled.instance(i));
              labeled.instance(i).setClassValue(clsLabel);
            }
            // save labeled data
            BufferedWriter writer = new BufferedWriter(
                                      new FileWriter(labeledData));
            writer.write(labeled.toString());
            writer.newLine();
            writer.flush();
            writer.close();
        }catch(IOException e)
        {
            String sDescription = e.toString();
            String s = sDescription;
            
        }catch (Exception ex)
        {
            String sDescription = ex.toString();
            String s = sDescription;
        }
        
        return oClassifier;                
    }

        private static ArrayList<String> ReadBOW(String bowPath, int nAttrib) {
        ArrayList<String> aBOW = new ArrayList<String>();
        
        if (new File(bowPath).exists()) {
            FileReader fr = null;
            BufferedReader bf = null;

            try {
                fr = new FileReader(bowPath);
                bf = new BufferedReader(fr);
                String sCadena = "";
                int cont = 0;
                while ((sCadena = bf.readLine())!=null && cont < nAttrib )
                {
                    String []data = sCadena.split(":::");
                    if (data.length==2) {
                        String sTerm = data[0];
                        aBOW.add(sTerm);
                        cont++;
                    }
                }
            } catch (Exception ex) {
                System.out.println(ex.toString());
            } finally {
                if (bf!=null) { try { bf.close(); } catch (Exception k) {} }
                if (fr!=null) { try { fr.close(); } catch (Exception k) {} }
            }
        }            
        return aBOW;
    }
        
    public static void configureProperties(String propfile)
    {
        try {
            props = new Properties();
            props.load(new FileInputStream(propfile));
            MODEL = props.getProperty("model");
            BOW = props.getProperty("bow");
            NATTRIB = props.getProperty("nattrib");
            TEST = props.getProperty("test");
            TEST_UNLABELED = props.getProperty("test_unlabeled");
            TEST_LABELED = props.getProperty("test_labeled");
        }catch (Exception e) {
            //logger.error("Exception reading properties file "+e.getMessage());
            System.out.print(e.getMessage());
        }
    }    
}
