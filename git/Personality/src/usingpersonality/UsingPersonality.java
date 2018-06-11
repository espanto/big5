/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package usingpersonality;

import com.arap.demo.tools.LangDetector;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import personality.Personality;
import twittermngr.TwitterMngr;
import weka.classifiers.Classifier;

/**
 *
 * @author sun
 */
public class UsingPersonality {

    static int N = 100;
    private static final int NATTRIB = 1004;
    private static final String BOW = "src/personality/bow/bow.txt";    
    private static final String MODEL = "src/personality/models/best_{task}.model"; 
    
    public static void main(String[] args) throws Exception {
        Personality per = new Personality();
        //String sentence = "Muchos, pero todos asumibles. Pasas a ser lo contrario de una persona normal. Yo falté a la boda de mi hermano. No lo olvidará nunca y yo tampoco. Coincidieron varias cosas en las mismas fechas. Pero sobre todo es que empiezan a invadir Chechenia y el presidente de la República, que era un señor de la guerra, nos invitó a un reducidísimo grupo de periodistas a ir al palacio presidencial. A ver, yo quiero muchísimo a mi hermano, pero ¡me había invitado el presidente! Sacrificas muchas cosas, sacrificas la humanidad, te conviertes en un agente externo, un poco en un marciano. Llegas a Madrid y tus prioridades son diferentes. No entiendo la inquietud que puedan generar algunas cuestiones.";
        String sentence = "Aún era un chaval, vivía con mis padres y tenía que ir a colegio. Dust se formaron en el instituto, cuando teníamos diecisiete o dieciocho años, y nadie te contrata a esa edad. Cuando Dust se disolvieron mis padres querían que terminara el instituto, porque así es como eran las cosas en esa época. Me gradué y me dieron mi diploma. Mis padres lo colgaron en la pared, muy orgullosos, aunque ya ves, no me ha servido para una mierda. Pero la escuela es importante, yo siempre les recomiendo a los estudiantes que sigan estudiando. Si quieren ser músicos que lo sigan siendo, pero que no dejen el colegio. Uno tiene que conservar algo, porque la tecnología ha tomado el poder, y hay que estar al tanto de lo que se cuece ahí fuera. Pero como te decía: Dust se disolvieron, y mi guitarra solista [Richie Wise] acabó produciendo los dos primeros álbumes de Kiss. A los diecinueve. Él ayudó a crear aquel típico sonido Kiss. Y yo empecé a salir por Nueva York con Richard Hell y The Voidoids.";
        
        LangDetector langDetect = new LangDetector("src/profiles");
        String sUser = "kicorangel";
        
        sentence = TwitterMngr.GetTextFromStatuses(TwitterMngr.LoadUserTimeline(sUser, N, "es", langDetect));
        
        // Load classifier for big5
        LoadClassifiers(per);
        
        // Read the bag of words
        ArrayList<String> bow = LoadBOW(BOW, NATTRIB); 
        per.setBow(bow);
        
        // Get predictions
        double ePred = per.PredictExtroverted(sentence);
        double oPred = per.PredictOpen(sentence);
        double cPred = per.PredictConscientious(sentence);
        double aPred = per.PredictAgreeable(sentence);
        double sPred = per.PredictStable(sentence);
        
        // Prints
        System.out.println("Predict Extroverted: " + ePred);
        System.out.println("Predict Open: " + oPred);
        System.out.println("Predict Conscientiuos: " + cPred);
        System.out.println("Predict Agreeable: " + aPred);
        System.out.println("Predict Stable: " + sPred);
    }
    
   /**
   *  LOad the cassifiers with models
   * 
   * @param per Personality object
   * @return double
   */
  public static Personality LoadClassifiers(Personality per) { 

    String eModel = MODEL;    
    eModel = eModel.replace("{task}", "extroverted");
    Classifier eClassifier = LoadClassifier(eModel);
    per.seteClassifier(eClassifier);
    
    String oModel = MODEL;       
    oModel = oModel.replace("{task}", "open");
    Classifier oClassifier = LoadClassifier(oModel);
    per.setoClassifier(oClassifier);
    
    String cModel = MODEL;       
    cModel = cModel.replace("{task}", "conscientious");
    Classifier cClassifier = LoadClassifier(cModel);
    per.setcClassifier(cClassifier);

    String aModel = MODEL;     
    aModel = aModel.replace("{task}", "agreeable");
    Classifier aClassifier = LoadClassifier(aModel);
    per.setaClassifier(aClassifier);
    
    String sModel = MODEL;     
    sModel =sModel.replace("{task}", "stable");
    Classifier sClassifier = LoadClassifier(sModel);
    per.setsClassifier(sClassifier);
    
    
    return per;
  }
  
/**
    * Function to load classifier
    * @param pathName
    * @return 
    */
   public static Classifier LoadClassifier(String pathName)
   {
         Classifier oClassifier = null;
         try
         {
             java.io.ObjectInput oi = new java.io.ObjectInputStream(new java.io.FileInputStream(pathName));
             //BufferedReader oi = new BufbferedReader(new InputStreamReader(getClass().getResourceAsStream(pathName)));

             Object o = oi.readObject();

             oClassifier = (weka.classifiers.Classifier)o;
             oi.close();
         }
         catch (IOException ex)
         {
             System.out.println(" pathName: "+pathName);
             System.out.println(" Load Classifier 1.... ");
             String sDescription = ex.toString();
             String s = sDescription;
         }
         catch (ClassNotFoundException ex)
         {
             System.out.println(" Load Classifier 1.... ");            
             String sDesc = ex.toString();
             String s = sDesc;
         }
         catch (Error e)
         {
             System.out.println(" Load Classifier 1.... ");            
             String sDescription = e.getMessage();
             String s = sDescription;
         }
         catch (Exception ex)
         {
             System.out.println(" Load Classifier 1.... ");            
             String sDescription = ex.toString();
             String s = sDescription;
         }

         return oClassifier;
   }
  
/**
   * Function to load the Features (bag of words)
   * @param bowPath
   * @param nAttrib
   * @return 
   */
  private static ArrayList<String> LoadBOW(String bowPath, int nAttrib)   {
        ArrayList<String> aBOW = new ArrayList();
      
        if (new File(bowPath).exists()) {
            FileReader fr = null;
            BufferedReader bf = null;

            try {
                fr = new FileReader(bowPath);
                bf = new BufferedReader(fr);
                String sCadena;
                int cont = 0;
                while (( sCadena = bf.readLine())!=null && cont < nAttrib )
                {
                    String []data = sCadena.split(";;;");
                    if (data.length==2) {
                        String sTerm = data[0];
                        aBOW.add(sTerm);
                        cont++;
                    }
                }
            } catch (IOException ex) {
                System.out.println(" From ReadBOW.... ");
                System.out.println(ex.toString());
            } finally {
                if (bf!=null) { try { bf.close(); } catch (IOException k) {System.out.println(k.toString());} }
                if (fr!=null) { try { fr.close(); } catch (IOException k) {System.out.println(k.toString());} }
            }
        }            
        return aBOW;
  }  
}
