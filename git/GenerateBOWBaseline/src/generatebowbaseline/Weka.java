package generatebowbaseline;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.TreeMap;

/**
 *
 * @author kico
 */
public class Weka {
    
    public static String HeaderToWeka(ArrayList<String> BOW, int iNTerms, String classValue, String attributeName)
    {
        String sHeader =  "@relation 'BOW'\n" ;

        for (int i=0;i<BOW.size();i++) {
            String sTerm = BOW.get(i);
            sHeader += "@attribute 'term-" + sTerm.replaceAll("'", "quote") + "' real\n";
            
            if (i>=iNTerms) {
                break;
            }
        }
        sHeader += "@attribute 'ncomas' real\n" +
                "@attribute 'npuntos' real\n" +
                "@attribute 'n2puntos' real\n";
        if (attributeName != null) {
            sHeader += "@attribute class numeric\n";
        }
        if (classValue != null) {
            sHeader += "@attribute 'class' {" + classValue + "}\n";            
        }        
        sHeader += "@data\n";
        return sHeader;
    }
    
    public static String FeaturesToWeka(ArrayList<String> BOW, Hashtable<String, Integer>oDoc, Features oFeatures, int iN, String classValue)    {
        String weka = "";
        int iTotal = oDoc.size();
        for (int i=0;i<BOW.size();i++) {
            String sTerm = BOW.get(i);
            double freq = 0;
            if (oDoc.containsKey(sTerm)) {
                freq = (double)((double)oDoc.get(sTerm) / (double)iTotal);
            }
            
            weka += freq + ",";
            
            if (i>=iN) {
                break;
            }
        }
        
        if (Double.isNaN((double)((double)oFeatures.NComas / (double)iTotal)) || Double.isInfinite((double)((double)oFeatures.NComas / (double)iTotal)) ||
                Double.isNaN((double)((double)oFeatures.NPuntos / (double)iTotal)) || Double.isInfinite((double)((double)oFeatures.NPuntos / (double)iTotal)) ||
                Double.isNaN((double)((double)oFeatures.NPuntos / (double)iTotal)) || Double.isInfinite((double)((double)oFeatures.NPuntos / (double)iTotal))
                ) {
            System.out.println("ERROR");
            
        }
            
        
        
        weka += (double)((double)oFeatures.NComas / (double)iTotal) + "," + 
                (double)((double)oFeatures.NPuntos / (double)iTotal) + "," + 
                (double)((double)oFeatures.NPuntos / (double)iTotal) + ",";
        
        weka +=  classValue + "\n";
        
        return weka;
    }
}
