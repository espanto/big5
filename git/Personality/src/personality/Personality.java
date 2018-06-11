package personality;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import weka.classifiers.Classifier;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;

/**
 * Class to predict personality traits
 * 
 * @author sun
 */
public class Personality {
  private static final int NATTRIB = 1004;
  
  private Classifier eClassifier;
  private Classifier oClassifier;
  private Classifier cClassifier;
  private Classifier aClassifier;
  private Classifier sClassifier;  
  
  private ArrayList<String> bow;
  
  
  /**
   *  Function to predict extroverted trait
   * 
   * @param text sentence to predict 
   * @return double
   */
  public double PredictExtroverted(String text) { 
    //Predict with model and data
    return Predict(text, eClassifier);
  }
  
   /**
   *  Function to predict open trait
   * 
   * @param text sentence to predict
   * @return double
   */
  public double PredictOpen(String text) { 
    //Predict with model and data
    return Predict(text, this.oClassifier);

  }
  
   /**
   *  Function to predict conscientious trait
   * 
   * @param text sentence to predict 
   * @return double
   */
  public double PredictConscientious(String text) { 
    //Predict with model and data
    return Predict(text, this.cClassifier);

  }
  
   /**
   *  Function to predict agreeable trait
   * 
   * @param text sentence to predict 
   * @return double
   */
  public double PredictAgreeable(String text) { 
    //Predict with model and data
    return Predict(text, this.aClassifier);
  }
  
  /**
   *  Function to predict stable trait
   * 
   * @param text sentence to predict 
   * @return double
   */
  public double PredictStable(String text) {
    //Predict with model and data
    return Predict(text, this.sClassifier);
  }  
  
  
  /**
     * Function to convert a sentence in a features vector
     * 
     * Finding every word of the sentence in the bow and get features
     * 
     * @param text
     * @param bow
     * @param nAttrib
     * @return features
     */
    private ArrayList<Double> Text2Vector(String text)
    {        
        // Initialize the features array
        ArrayList<Double> features = new ArrayList(Collections.nCopies(NATTRIB, 0.0));
        
        // Build hastable with words of the sentece
        String []data = text.split(" ");                
        Hashtable<String, Integer> oText = new Hashtable<String, Integer>();
        for (String word : data) {
            word = word.toLowerCase();
            int iFreq = 0;
            if (oText.containsKey(word)) {
                iFreq = oText.get(word);
            }
            oText.put(word, ++iFreq);
        }
        
        // Get the features
        int i=0;
        int iFreq = 0;
        for (String bagword : bow){            
            //bagword = bagword.toLowerCase(); //Not necessary                         
            if (oText.containsKey(bagword)) {
                iFreq = (int)oText.get(bagword);
                features.set(i,(double)((double)iFreq/(double)oText.size()));
            } 
            
            i++;
        }
        
        return features;
    }
    
  
private Instances CreateInstances(ArrayList features) {
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
            double value = (double) features.get(j);
            inst.setValue(atts.get(j), value);
        }
        //Add the class, don't mind the value because we will predict it
        inst.setValue(atts.get(iClass), 0.0);
        
        //Define Instances
        instances = new Instances("model", atts, nAttrib);
        instances.setClassIndex(iClass);
        instances.add(inst);
    }
    catch (NumberFormatException ex)
    {
        System.out.println(" From Create Instances.... ");
            System.out.println(ex.toString());
    }
    catch (Exception ex)
    {
            System.out.println(ex.toString());
    }
    return instances;
}
  
  
  /**
   * 
   * @param data
   * @param oClassifier
   * @return 
   */
  private double Predict(String text, Classifier oClassifier) {
        ArrayList<Double> Features = Text2Vector(text);
        // Create instances
        Instances data = CreateInstances(Features); 
    
        double pred = 0.0;
        
        try {
            for (int i = 0; i < data.numInstances(); i++) {
              Instance instance = data.instance(i);
              pred = oClassifier.classifyInstance(instance);
            }
        } catch (Exception ex) {
            System.out.println(" From Predict.... ");
            System.out.println(ex.toString());
        }
        
        return pred;
    }
  
    public Classifier geteClassifier() {
        return eClassifier;
    }

    public Classifier getoClassifier() {
        return oClassifier;
    }

    public Classifier getcClassifier() {
        return cClassifier;
    }

    public Classifier getaClassifier() {
        return aClassifier;
    }

    public Classifier getsClassifier() {
        return sClassifier;
    }

    public ArrayList<String> getBow() {
        return bow;
    }

    public void seteClassifier(Classifier eClassifier) {
        this.eClassifier = eClassifier;
    }

    public void setoClassifier(Classifier oClassifier) {
        this.oClassifier = oClassifier;
    }

    public void setcClassifier(Classifier cClassifier) {
        this.cClassifier = cClassifier;
    }

    public void setaClassifier(Classifier aClassifier) {
        this.aClassifier = aClassifier;
    }

    public void setsClassifier(Classifier sClassifier) {
        this.sClassifier = sClassifier;
    }

    public void setBow(ArrayList<String> bow) {
        this.bow = bow;
    }
  
  
    
}
