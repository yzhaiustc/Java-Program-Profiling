import java.util.*;

import jdk.nashorn.internal.ir.BlockStatement;
import soot.*;
import soot.jimple.AssignStmt;
import soot.jimple.Jimple;
import soot.jimple.LongConstant;
import soot.jimple.ReturnStmt;
import soot.jimple.ReturnVoidStmt;
import soot.jimple.Stmt;
import soot.jimple.StringConstant;
import soot.options.Options;
import soot.toolkits.graph.*;
import soot.toolkits.scalar.*;
import soot.util.Chain;
import soot.util.HashChain;

 class DominAnalysis
	extends ForwardFlowAnalysis
	{
	protected void copy(Object src,
			Object dest) {
			FlowSet sourceSet = (FlowSet)src,
			destSet = (FlowSet) dest;
			sourceSet.copy(destSet);
			}
	protected void merge(Object a1, Object a2, Object a3){
		FlowSet src1Set = (FlowSet) a1, src2Set = (FlowSet) a2, destSet  = (FlowSet) a3;
		src1Set.intersection(src2Set, destSet);
	}
	protected void flowThrough(Object srcValue,Object u, Object destValue)
	{
		FlowSet src = (FlowSet)srcValue,
				dest = (FlowSet)destValue;
				Block ut = (Block)u;
		src.copy (dest);
		dest.add(ut);
	}
	protected Object newInitialFlow() {ArraySparseSet tmp = new ArraySparseSet();
	BlockGraph bg = (BlockGraph)this.graph;
	List<Block>	lb = bg.getBlocks();
	for(Block bl : lb){
		tmp.add(bl);
	}
	return tmp;}
	protected Object entryInitialFlow() {
		return new ArraySparseSet();
	}
	public DominAnalysis(DirectedGraph graph) {
		super(graph);
		doAnalysis();
		// TODO Auto-generated constructor stub
	}

	
	
}

 class ReachingDef
	extends ForwardFlowAnalysis
	{
	protected void copy(Object src,
			Object dest) {
			FlowSet sourceSet = (FlowSet)src,
			destSet = (FlowSet) dest;
			sourceSet.copy(destSet);
			}
	protected void merge(Object a1, Object a2, Object a3){
		FlowSet src1Set = (FlowSet) a1, src2Set = (FlowSet) a2, destSet  = (FlowSet) a3;
		src1Set.union(src2Set, destSet);
	}
	protected void flowThrough(Object srcValue,Object u, Object destValue)
	{
		FlowSet src = (FlowSet)srcValue,
				dest = (FlowSet)destValue;
				Block ut = (Block)u;
		src.copy(dest);
		Iterator<Unit> iu = ut.iterator();
		while(iu.hasNext()){
			Unit statement = iu.next();
			for (ValueBox box : statement.getDefBoxes())
			{
				FlowSet<Unit> uf = (FlowSet<Unit>) dest;
				Iterator<Unit> iff = uf.iterator();
				while(iff.hasNext()){
					Unit stat = iff.next();
					for (ValueBox box2 : stat.getDefBoxes())
					{
						if(box2.getValue().toString().equals(box.getValue().toString())){
							dest.remove(stat);
						}
					}
				}
			}
			if(statement.getDefBoxes().size() > 0) dest.add(statement);
			
		}
	}	
		
	
	protected Object newInitialFlow() {return new ArraySparseSet();}
	protected Object entryInitialFlow() {
		return new ArraySparseSet();
	}
	public ReachingDef(DirectedGraph graph) {
		super(graph);
		doAnalysis();
		// TODO Auto-generated constructor stub
	}

	
	
}


public class Main {
	private static FlowSet<Unit> res = new ArraySparseSet();
	private static List<SootMethod> methods = null;
	private static int methodsNum;
    private static List<SootField> myFields = new ArrayList<>();
    private static List<String> myNameList = new ArrayList<>();
	public static void main(String[] args) {
		
		//Static Analysis (Retrieve Flow Graph)
		staticAnalysis();

		//Dynamic Analysis (Instrumentation) 
		dynamicAnalysis();
 
        Scene.v().addBasicClass("java.io.PrintStream",SootClass.SIGNATURES);
        Scene.v().addBasicClass("java.lang.System",SootClass.SIGNATURES);
		
        soot.Main.main(args);

		System.out.println("-------------------------------------");
		System.out.println("End of execution");
	}

	private static void staticAnalysis(){
		configure("C:\\Users\\asus\\Desktop\\CS201Fall19\\CS201Fall19\\Analysis"); //Change this path to your Analysis folder path in your project directory
		
		SootClass sootClass = sootClass = Scene.v().loadClassAndSupport("Test1");
        sootClass.setApplicationClass();
        //Static Analysis code

        methods = sootClass.getMethods();
        methodsNum = sootClass.getMethodCount();

        System.out.println("+----------------------------------");
        System.out.println("| Control Flow Graphs");
        System.out.println("+----------------------------------");
        for (int i = 0; i < methodsNum-1; i++) {
            SootMethod currentMethod = methods.get(i);
            Body currentBody = currentMethod.retrieveActiveBody();
            Body _copy = (Body) currentBody.clone();
            
            BlockGraph blockGraph = new ClassicCompleteBlockGraph(currentBody);
//            System.out.println("Method: " + currentMethod.toString());
//            System.out.println(blockGraph.toString());
            UnitGraph ug = new ClassicCompleteUnitGraph(currentBody);
            for (Unit u : ug)
            {
            	System.out.println(u);
            }
        }


//		SootClass sootClass = Scene.v().loadClassAndSupport("Test1");
	    sootClass.setApplicationClass();
	    List<SootMethod> tmp  = sootClass.getMethods();
        ListIterator<SootMethod> iterator = tmp.listIterator(); 
        while (iterator.hasNext()) {
        	SootMethod ite = iterator.next();
        	System.out.println(ite);
        	BlockGraph bg = new BriefBlockGraph(ite.retrieveActiveBody());
        	DominAnalysis lv =	new DominAnalysis(bg);
        	ReachingDef ev = new ReachingDef(bg);
        	Iterator<Block> blockIt = bg.iterator();
    		while (blockIt.hasNext()) {
    			Block s = blockIt.next();
    			System.out.println(s.getIndexInMethod()+":");
    			
    			
    			List<Block> lbb = s.getSuccs();
    			
    		
    			ArraySparseSet settemp = (ArraySparseSet) lv.getFlowAfter(s);
				List<Block> w= settemp.toList();
    			for(Block e: lbb){
    				if(w.contains(e)){
    					List<Block> pbloc = new ArrayList<Block>(); 
    					pbloc.add(e);
    					Queue<Block> qbloc = new LinkedList<Block>();
    					qbloc.add(s);
    					while(!qbloc.isEmpty()){
    						Block pe = qbloc.peek();
    						qbloc.remove();
    						pbloc.add(pe);
    						List<Block> pre = pe.getPreds();
    						for(Block ppe: pre){
    							int u = ppe.getIndexInMethod();
    							int flag = 0;
    							for(Block wee: pbloc){
    								int v = wee.getIndexInMethod();
    								if(u == v){
    									flag = 1;
    								}
    							}
    							if(flag == 1){continue;}
    							qbloc.add(ppe);
    						}
    					}

    					int len = pbloc.size();
    					for(int i = 0; i < len; ++ i){
    						for(int j = i+1; j < len; ++ j){
        						if(pbloc.get(i).getIndexInMethod() > pbloc.get(j).getIndexInMethod()){
        							Block dtmp = pbloc.get(i);
        							pbloc.set(i, pbloc.get(j));
        							pbloc.set(j, dtmp);
        						}
        					}
    					}
    					for(int i = 0; i < len; ++ i){
    						System.out.print("B"+pbloc.get(i).getIndexInMethod()+" ");
    					}
    					System.out.println();
    					FlowSet<Unit> ass = new ArraySparseSet();
    					FlowSet<Unit> used = new ArraySparseSet();
    					for(int i = 0; i < len; ++ i){
    						Iterator<Unit> iunit = pbloc.get(i).iterator();
    						while(iunit.hasNext()){
    							Unit defloop = iunit.next();
    							if(defloop.getDefBoxes().size()>0){ass.add(defloop);}
    							if(defloop.getUseBoxes().size() > 0){used.add(defloop);}
    						}
    					}
    					FlowSet<Unit> ess = (FlowSet<Unit>)ev.getFlowAfter(pbloc.get(0));
    					FlowSet<Unit> ness =  new ArraySparseSet();
    					ess.copy(ness);
    					for(Unit essx : ess){
    						for(Unit assy : ass){
    							if(essx.toString().equals(assy.toString())){
    								ness.remove(essx);
    							}
    						}
    					}
    					FlowSet<Value> ness2 =  new ArraySparseSet();
    					for(Unit essx : ness){
    						for (ValueBox box : essx.getDefBoxes()){
    							ness2.add(box.getValue());
    						}
    					}

    					for(Unit essx : used){
    						int flag = 0;
    						for (ValueBox box : essx.getUseBoxes()){
SootClass sootClass = Scene.v().loadClassAndSupport("Test2");
	    sootClass.setApplicationClass();
	    List<SootMethod> tmp  = sootClass.getMethods();
        ListIterator<SootMethod> iterator = tmp.listIterator(); 
        while (iterator.hasNext()) {
        	SootMethod ite = iterator.next();
        	System.out.println("Method:"+ite);
        	BlockGraph bg = new BriefBlockGraph(ite.retrieveActiveBody());
        	DominAnalysis lv =	new DominAnalysis(bg);
        	ReachingDef ev = new ReachingDef(bg);
        	Iterator<Block> blockIt = bg.iterator();
        	String Loop = "";
        	String overrepeatedStatements = "";
        	while (blockIt.hasNext()) {
    			Block s = blockIt.next();
    			System.out.println("BB"+s.getIndexInMethod()+":");
    			Iterator<Unit> itu = s.iterator();
    			while(itu.hasNext()){
    				Unit xtmp = itu.next();
    				System.out.println(xtmp);
    			}
    			List<Block> lbb = s.getSuccs();
    			System.out.println();
    		    
    			ArraySparseSet settemp = (ArraySparseSet) lv.getFlowAfter(s);
				List<Block> w= settemp.toList();
    			for(Block e: lbb){
    				if(w.contains(e)){
    					List<Block> pbloc = new ArrayList<Block>(); 
    					pbloc.add(e);
    					Queue<Block> qbloc = new LinkedList<Block>();
    					qbloc.add(s);
    					boolean fflag = false;
    					if(s.getIndexInMethod() == e.getIndexInMethod()){fflag = true;}
    					while(!qbloc.isEmpty() && !fflag){
    						Block pe = qbloc.peek();
    						qbloc.remove();
    						pbloc.add(pe);
    						List<Block> pre = pe.getPreds();
    						for(Block ppe: pre){
    							int u = ppe.getIndexInMethod();
    							int flag = 0;
    							for(Block wee: pbloc){
    								int v = wee.getIndexInMethod();
    								if(u == v){
    									flag = 1;
    								}
    							}
    							if(flag == 1){continue;}
    							qbloc.add(ppe);
    						}
    					}

    					int len = pbloc.size();
    					for(int i = 0; i < len; ++ i){
    						for(int j = i+1; j < len; ++ j){
        						if(pbloc.get(i).getIndexInMethod() > pbloc.get(j).getIndexInMethod()){
        							Block dtmp = pbloc.get(i);
        							pbloc.set(i, pbloc.get(j));
        							pbloc.set(j, dtmp);
        						}
        					}
    					}
    					
    					FlowSet ass = new ArraySparseSet();
    					FlowSet used = new ArraySparseSet();
    					for(int i = 0; i < len; ++ i){
    						Iterator<Unit> iunit = pbloc.get(i).iterator();
    						while(iunit.hasNext()){
    							Unit defloop = iunit.next();
    							if(defloop.getDefBoxes().size()>0){ass.add(defloop);}
    							if(defloop.getUseBoxes().size()>0){used.add(defloop);}
    						}
    					}
    					FlowSet ess = (FlowSet)ev.getFlowBefore(pbloc.get(0));
    					FlowSet ness =  new ArraySparseSet();
    					ess.copy(ness);
    					Iterator<Unit> entranceunits = ess.iterator();
    					while(entranceunits.hasNext()){
    						Unit essx = entranceunits.next(); 
    						Iterator<Unit> wholeunits = ass.iterator();
    						while(wholeunits.hasNext()){
    							Unit assy = wholeunits.next();
    							if(essx.toString().equals(assy.toString())){
    								ness.remove(essx);
    							}
    						}
    					}
    					FlowSet ness2 =  new ArraySparseSet();
    					entranceunits = ness.iterator();
    					while(entranceunits.hasNext()){
    						Unit essx = entranceunits.next();
    						for (ValueBox box : essx.getDefBoxes()){
    							ness2.add(box.getValue());
    						}
    					}
    					entranceunits = used.iterator();
    					
    					while(entranceunits.hasNext()){
    						Unit essx = entranceunits.next();
    						int flag = 0;
    						for (ValueBox box : essx.getUseBoxes()){
    							Iterator<Value> yy = ness2.iterator();
    							while(yy.hasNext()){
    								Value y = yy.next();
    								if(y.toString().equals(box.getValue().toString())){
    									flag = 1;
    									break;
    								}
    							}
    							if(flag == 1) break;
    						}
    						if(flag == 1){
    							res.add(essx);
    							overrepeatedStatements += essx + "\n";
    						}
    					}
    					Loop += "[";
    					
    					for(int i = 0; i < len; ++ i){
    						String x = (i == len-1)? "" : " ,";
    						Loop += ("B"+pbloc.get(i).getIndexInMethod() + x);
    					}
    					Loop += "]\n";
    					
    				}
    			}
    			
    		}
        	System.out.println("Loops:\n"+ Loop);
        	System.out.println("Statements:\n" + overrepeatedStatements);
        }
	    //Static Analysis code
	}

    private static void dynamicAnalysis(){
        PackManager.v().getPack("jtp").add(new Transform("jtp.myInstrumenter", new BodyTransformer() {
        	private boolean isUsedStmt(Stmt s)
        	{
        		boolean flag = false;
        		for (Unit u : res)
        		{
        			if (s.toString().equals(u.toString()))
        			{
        				flag = true;
        				break;
        			}
        		}
				return flag;
        	}
            private void profile(Body arg0, String arg1, Map arg2) {
                SootClass currentSootClass = arg0.getMethod().getDeclaringClass();
                ClassicCompleteBlockGraph blockGraph = new ClassicCompleteBlockGraph(arg0);
                List<Block> blocks = blockGraph.getBlocks();
                int blockNum = blocks.size();
                String currentMethodName = arg0.getMethod().getName();
                Chain units = arg0.getUnits();
                Unit tailUnit = blockGraph.getTails().get(0).getTail();
                
                SootField blockExeNumField = null;
                SootField usedInstExeNumField = null;
                SootField edgeExeNumField = null;
                
                // Prepare
                // Add tmpLocal
                Local tmpLocal = Jimple.v().newLocal("tmpLocal", LongType.v());
                arg0.getLocals().add(tmpLocal);
                
                // If this is the main function
                boolean isMainMethod = arg0.getMethod().getSubSignature().equals("void main(java.lang.String[])");
                
                // Load Print Stream
                Local tmpRef = Jimple.v().newLocal("tmpRef", RefType.v("java.io.PrintStream"));
                arg0.getLocals().add(tmpRef);
                SootMethod printIntCall = Scene.v().getSootClass("java.io.PrintStream").getMethod("void println(int)");
                SootMethod printStringCall = Scene.v().getSootClass("java.io.PrintStream").getMethod("void print(java.lang.String)");
                
                int idx = 0;
                Iterator it = arg0.getUnits().snapshotIterator();


                while(it.hasNext())
                {
                	Stmt s = (Stmt)it.next();
                	if (isUsedStmt(s))
                	{
                		usedInstExeNumField = new SootField("UsedInst" + idx + "ExeNum", LongType.v(), Modifier.STATIC);
                		boolean flag = false;
                		for (SootField f : currentSootClass.getFields())
                    	{
                    		if (f.getName().equals(usedInstExeNumField.getName()))
                    		{
                    			flag = true;
                    			break;
                    		}
                    	}
                    	if (!flag) 
                    	{
                    		System.out.println("New Class!, curr inst is : " + s.toString());
                    		myFields.add(usedInstExeNumField);
                    		myNameList.add(s.toString());
                    		currentSootClass.addField(usedInstExeNumField);
                    		idx = idx + 1;
                    	}
                    	
                		System.out.println("Checked!! " + s.toString());
                		AssignStmt usedInstExeNumStmt1 = Jimple.v().newAssignStmt(tmpLocal, Jimple.v().newStaticFieldRef(usedInstExeNumField.makeRef()));
                		AssignStmt usedInstExeNumStmt2 = Jimple.v().newAssignStmt(tmpLocal, Jimple.v().newAddExpr(tmpLocal, LongConstant.v(1)));
                		AssignStmt usedInstExeNumStmt3 = Jimple.v().newAssignStmt(Jimple.v().newStaticFieldRef(usedInstExeNumField.makeRef()), tmpLocal);
                	
                		units.insertBefore(usedInstExeNumStmt1, s);
                		units.insertAfter(usedInstExeNumStmt2, s);
                		units.insertAfter(usedInstExeNumStmt3, usedInstExeNumStmt2);
                	}


                }
                
                it = arg0.getUnits().snapshotIterator();
                while(it.hasNext())
                {
                	Stmt s = (Stmt)it.next();
	            	if (isMainMethod && (s instanceof ReturnVoidStmt || s instanceof ReturnStmt))
	            	{
	            		units.insertBefore(Jimple.v().newAssignStmt(tmpRef, Jimple.v().newStaticFieldRef(Scene.v().getField("<java.lang.System: java.io.PrintStream out>").makeRef())), tailUnit);
	                    int idx1 = 0;
	                    System.out.println("myFields Size: " + myFields.size());
	                    System.out.println("myNameList Size: " + myNameList.size());
	            		for (SootField f : myFields) {
	            			System.out.println(f.getName());
	            			System.out.println(myNameList.get(idx1) + ": ");
	                        units.insertBefore(Jimple.v().newInvokeStmt(Jimple.v().newVirtualInvokeExpr(tmpRef, printStringCall.makeRef(), StringConstant.v(myNameList.get(idx1) + ": "))), tailUnit);
	                        units.insertBefore(Jimple.v().newAssignStmt(tmpLocal, Jimple.v().newStaticFieldRef(f.makeRef())), tailUnit);
	                        units.insertBefore(Jimple.v().newInvokeStmt(Jimple.v().newVirtualInvokeExpr(tmpRef, printIntCall.makeRef(), tmpLocal)), tailUnit);
	                        idx1 = idx1 + 1;
	            		}
	            	}
                }
                
            }   // end of profile()

        @Override
        protected void internalTransform(Body arg0, String arg1, Map arg2) {
            //Dynamic Analysis (Instrumentation) code
                boolean isMainMethod = arg0.getMethod().getSubSignature().equals("void main(java.lang.String[])");
                if (isMainMethod) {
                    // for each method in this class
                    List<SootMethod> methods = arg0.getMethod().getDeclaringClass().getMethods();
                    for (SootMethod m : methods) {
                        boolean _isMainMethod = m.getSubSignature().equals("void main(java.lang.String[])");
                        if (_isMainMethod)
                            continue;
                        if (m.getName().equalsIgnoreCase("<init>"))
                            continue;
                        this.profile(m.retrieveActiveBody(), arg1, arg2);
                    }
                    profile(arg0, arg1, arg2);
                }   // else do nothing              
        }           
       }));
    }
	
	public static void configure(String classpath) {		
        Options.v().set_whole_program(true);
        Options.v().set_allow_phantom_refs(true);
        Options.v().set_src_prec(Options.src_prec_java);
        Options.v().set_output_format(Options.output_format_jimple);
        Options.v().set_soot_classpath(classpath);
        Options.v().set_prepend_classpath(true);
        Options.v().setPhaseOption("cg.spark", "on");        
    }
}
