//   /* Código para crear una instancia a partir de tu vector de features (O AQUÍ EN EL EJEMPLO PARA MÁS DE UNA INSTANCIA) */
//    private void CreateInstanceS() {
//    //NÚMERO DE ATRIBUTOS QUE TENGA TU REPRESENTACIÓN, POR EJEMPLO 1000 SI ES UNA BOLSA DE PALABRAS CON LAS 1000 MÁS FRECUENTES;
//    int nAttrib = NATTRIB;
//    //NÚMERO DE CLASES DEL PROBLEMA. POR EJEMPLO, EN IDENTIFICACIÓN DE SEXO SON 2;
//    int nClasses = 2;
//    int iClass = nAttrib - 1;
//    try
//    {
//        FastVector features = new FastVector(nAttrib);
//
//
//            /* Para cada atributo haces lo siguiente */
//            attInfo.addElement(new Attribute("NOMBRE DEL ATRIBUTO"));
//            ...
//
//
//            FastVector oFv = new FastVector(nClasses);
//            /* Para cada clase haces lo siguiente */
//            oFv.addElement("NOMBRE DE LA CLASE");
//            ...
//
//            attInfo.addElement(new Attribute("class", oFv));
//
//
//
//        Instances instances = new Instances("NOMBRE DE TU MODELO", attInfo, nAttrib);
//        instances.setClassIndex(iClass);
//
//        Instance inst = new DenseInstance(nAttrib);
//
//        int iAttrib = 0;
//            /* PARA CADA DOCUMENTO, TENDRÁS UN VECTOR DE FEATURES (POR EJEMPLO LA BOLSA DE PALABRAS), QUE ES LO QUE DEBES PONER AQUÍ ABAJO */
//            inst.setValue(iAttrib++, "EL VALOR CORRESPONDIENTE DE LA FEATURE ACTUAL");
//            ...
//
//            /* POR EJEMPLO, SI TIENES 50 DOCUMENTOS, A 1000 FEATURES POR DOCUMENTO, EN UNA MATRIZ, SERÍA ALGO COMO */
//
//            for (int i=0;i<50;i++) {
//                    iAttrib = 0;
//                    for (int j=0;j<1000;j++) {
//                            inst.setValue(iAttrib++, Features[i][j]);
//                    }
//                    inst.setValue(iAttrib++, Class[i])); 	// AQUÍ AÑADES LA CLASE
//                    instances.add(inst);
//            }
//
//
//            /* PARA ACCEDER A LA PRIMERA INSTANCIA */
//        mInstance = instances.instance(0);
//    }
//    catch (Exception ex)
//    {
//    }
//    }
//
//
////    /* NECESITAS UNA FUNCIÓN QUE TE CONVIERTA UN TEXTO EN UN VECTOR DE FEATURES */
////
////    public ArrayList<String> Text2Vector(String text) {}
////
////
////    /* Y LA FUNCIÓN DE PREDICCIÓN */
////    public String Predict(String text) {
////        String sPrediction = "";
////        
////        try {
////			Classifier oClassifier = LoadClassifier();
////            ArrayList<String> Features = Text2Vector(text);
////            Instance inst = GetInstance(Features);
////            double class = oClassifier.classifyInstance(inst); 
////sPrediction = GetClassFromPredicted(class);
////        } catch (Exception ex) {
////            System.out.println(ex.toString());
////        }
////        
////        return sclass;
////    }
////
//
//    
//    public static void configureProperties(String propfile)
//    {
//        try {
//            props = new Properties();
//            props.load(new FileInputStream(propfile));
//            MODEL = props.getProperty("model");
//            
//        }catch (Exception e) {
//            //logger.error("Exception reading properties file "+e.getMessage());
//        }
//    }    
//}
