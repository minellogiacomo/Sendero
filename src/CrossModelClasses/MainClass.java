/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package CrossModelClasses;

import NKCModel.NKCModelBatch;
import NKCModel.NKCModelGUI;
import NKModel.NKModelBatch;
import NKModel.NKModelGUI;

import uchicago.src.sim.engine.SimInit;

/**
 *
 * @author jamesmitchell
 */
public class MainClass {
        public static void main (String args[]) {
        
        boolean paramsOK = false;    
            
            if (args[0].equals("nkcgui")) {
                paramsOK = true;
                SimInit init = new SimInit();
		NKCModelGUI model = new NKCModelGUI();
		init.loadModel(model, args[1], false);
            }
            
            if (args[0].equals("nkcbatch")) {
                paramsOK = true;
               System.out.println("Processing NKC Batch");
		SimInit init = new SimInit();
		NKCModelBatch model = new NKCModelBatch();
		init.loadModel(model, args[1], true);
            }
            
             if (args[0].equals("nkgui")) {
                 paramsOK = true;
               SimInit init = new SimInit();
		NKModelGUI model = new NKModelGUI();
		init.loadModel(model, args[1], false);
            }
            
              if (args[0].equals("nkbatch")) {
                  paramsOK = true;
                  System.out.println("Processing NK Batch");
               SimInit init = new SimInit();
		NKModelBatch model = new NKModelBatch();
		init.loadModel(model, args[1], true);
            }
            
            if (!paramsOK) {
                System.err.println("Invalid arguments - exiting");
                System.err.println("Usage: NKCModelProject.jar ([nk|nkc][batch|gui]) param_file");
            }
                    
            
        }
}
