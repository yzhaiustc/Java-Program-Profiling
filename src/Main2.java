import java.util.*;

import soot.*;
import soot.jimple.AssignStmt;
import soot.jimple.Constant;
import soot.jimple.DefinitionStmt;
import soot.jimple.GotoStmt;
import soot.jimple.IfStmt;
import soot.jimple.IntConstant;
import soot.jimple.InvokeExpr;
import soot.jimple.InvokeStmt;
import soot.jimple.Jimple;
import soot.jimple.JimpleBody;
import soot.jimple.LongConstant;
import soot.jimple.ReturnStmt;
import soot.jimple.ReturnVoidStmt;
import soot.jimple.StaticInvokeExpr;
import soot.jimple.Stmt;
import soot.jimple.StringConstant;
import soot.options.Options;
import soot.toolkits.graph.*;
import soot.toolkits.scalar.*;
import soot.util.Chain;

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
				FlowSet uf = (FlowSet) dest;
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
 
 class AnalysisDefs extends ForwardFlowAnalysis{
	 private List<Integer> Loops;
	 private FlowSet emptySet;
	 private FlowSet fullSet;
	
	 public AnalysisDefs(DirectedGraph graph, List<Integer> loops) {
		 	super(graph);
		 	Loops = loops;
			emptySet = new ArraySparseSet();
			fullSet = new ArraySparseSet();
			
			BlockGraph bg = (BlockGraph) graph;
			Iterator<Block> bloc = bg.iterator();
			while(bloc.hasNext()){
				Block tmp = bloc.next();
				int num = tmp.getIndexInMethod();
				boolean flag = false;
				for(int i = 0; i < loops.size();++ i){
					if(loops.get(i) == num){
						flag = true;
					}
				}
				if(flag == false){ continue; }
				Iterator<Unit> iunit = tmp.iterator();
				while(iunit.hasNext()){
					Unit defloop = iunit.next();
					if(defloop.getUseBoxes().size()>0){
						for(ValueBox x : defloop.getUseBoxes()){
							fullSet.add(x.getValue());
						}
					}
				}
			}

			doAnalysis();
			// TODO Auto-generated constructor stub
		}

		@Override
		protected void flowThrough(Object srcValue,Object u, Object destValue) {
			FlowSet src = (FlowSet)srcValue,
					dest = (FlowSet)destValue;
					Block ut = (Block)u;
			src.copy(dest);
			int num = ut.getIndexInMethod();
			boolean flag = false;
			for(int i = 0; i < Loops.size();++ i){
				if(Loops.get(i) == num){
					flag = true;
				}
			}
			if(flag){
				Iterator<Unit> iu = ut.iterator();
				while(iu.hasNext()){
					Unit statement = iu.next();
					for (ValueBox box : statement.getDefBoxes())
					{
						dest.add(box.getValue());
					}
					
				}
			}
			
			
		}

		@Override
		protected void copy(Object src, Object dest) {
			FlowSet sourceSet = (FlowSet)src,
			destSet = (FlowSet) dest;
			sourceSet.copy(destSet);
		}

		@Override
		protected Object entryInitialFlow() {
			// TODO Auto-generated method stub
			return emptySet.clone();
		}

		@Override
		protected void merge(Object a1, Object a2, Object a3) {
			FlowSet src1Set = (FlowSet) a1, src2Set = (FlowSet) a2, destSet  = (FlowSet) a3;
			src1Set.intersection(src2Set, destSet);
		}

		@Override
		protected Object newInitialFlow() {
			// TODO Auto-generated method stub
			return fullSet.clone();
		}
 }
 

 class statement{
		private SootClass cname = null;
		private SootMethod methodname = null;
		private String statementname;
		public statement(){
			
		}
		public String nameofstmt(){
			return "Class: "+ cname.getName() + " Method: " + methodname.getName() + " Statement: " + statementname; 
		}
		public void set(SootClass c, SootMethod m, Stmt st){
			cname = c;
			methodname = m;
			statementname = st.toString();
		}
		public int hash(){
			int number = 0;
			for(int i = 0; i < cname.getName().length(); ++ i){
				number *= 31;
				number %= 9999991;
				number += cname.getName().charAt(i);
			}
			for(int i = 0; i < methodname.getName().length(); ++ i){
				number *= 31;
				number %= 9999991;
				number += methodname.getName().charAt(i);
			}
			for(int i = 0; i < statementname.length(); ++ i){
				number *= 31;
				number %= 9999991;
				number += statementname.charAt(i);
			}
			return number;
		}
 }
 
class Counter extends BodyTransformer{
	private static Counter instance = new Counter();
	private static List<statements> res;
	private Counter(){}
	private SootClass javaIoPrintStream;
	private boolean addedFieldToMainClassAndLoadedPrintStream = false;
	private static List<statement> criticalstatements = new ArrayList<statement>(); 
	
	public static Counter v() { return instance; }
	public static void setFlowSet(List<statements> arg0){
		res = arg0;
	}
	private Local addTmpRef(Body body)
	{
		 Local tmpRef = Jimple.v().newLocal("tmpRef", RefType.v("java.io.PrintStream"));
		 body.getLocals().add(tmpRef);
		 return tmpRef;
	}
	private Local addTmpLong(Body body){
		Local tmpLong = Jimple.v().newLocal("tmpLong", LongType.v());
		body.getLocals().add(tmpLong);
		return tmpLong;
	}
	private void addStmtsToBefore(Chain units, Stmt s, SootField gotoCounter, Local tmpRef,
			Local tmpLong, String statement)
	{
		units.insertBefore(Jimple.v().newAssignStmt(
				tmpRef, Jimple.v().newStaticFieldRef(
			    Scene.v().getField("<java.lang.System: java.io.PrintStream out>").makeRef())), s);
		units.insertBefore(Jimple.v().newAssignStmt(tmpLong,Jimple.v().newStaticFieldRef(gotoCounter.makeRef())), s);
		SootMethod allPrint = javaIoPrintStream.getMethod("void print(java.lang.String)");
		SootMethod toCall = javaIoPrintStream.getMethod("void println(long)");
		units.insertBefore(Jimple.v().newInvokeStmt(
				Jimple.v().newVirtualInvokeExpr(tmpRef, allPrint.makeRef(),StringConstant.v(statement + " : "))), s);
		units.insertBefore(Jimple.v().newInvokeStmt(
				Jimple.v().newVirtualInvokeExpr(tmpRef, toCall.makeRef(),tmpLong)), s);
	}
	
	
	private void ProcessMethod(SootClass classname, SootMethod methodname, Body body, String phaseName,
			Map options){
		Local tmpRef = null, tmpLong = null;
		boolean addedLocals = false;
		Iterator<statements> it = res.iterator();
		boolean isMainMethod = body.getMethod().getSubSignature().equals("void main(java.lang.String[])");
		Local tmpLocal = Jimple.v().newLocal("tmp", LongType.v());
		body.getLocals().add(tmpLocal);
		while(it.hasNext()){
			statements current = it.next();
			if(current.equals(classname,methodname)){
				ArrayList<Stmt> astmt = current.getstmts();
				ArrayList<Integer> aInt = current.getmarked();
				Chain units = body.getUnits();
				Iterator stmtIt = units.snapshotIterator();
				Iterator curstmt = astmt.iterator();
				Iterator numering = aInt.iterator();

				while(stmtIt.hasNext())
				{
					Stmt s = (Stmt) stmtIt.next();
					Stmt stm = (Stmt) curstmt.next();
					int num = (Integer) numering.next();
					SootField Instruction = null;
					if(num == -1){
						boolean flag = false;
						statement stam = new statement();
						stam.set(classname, methodname, s);
					    try{
					    	SootField ttmp = classname.getFieldByName("temporarycounter"+stam.hash());
					    	Instruction = ttmp;
					    	flag = true;
					    }catch(Exception e){
					    	
					    }
					    if(!flag){
					    	SootField tmp = new SootField("temporarycounter"+stam.hash(), LongType.v(),Modifier.STATIC);
							classname.addField(tmp);
							Instruction = tmp;
							criticalstatements.add(stam);
							javaIoPrintStream = Scene.v().getSootClass("java.io.PrintStream");
					    }
					    /*if(s instanceof IfStmt){
					    	AssignStmt toAdd1 = Jimple.v().newAssignStmt(tmpLocal,
									Jimple.v().newStaticFieldRef(Instruction.makeRef()));
							AssignStmt toAdd2 = Jimple.v().newAssignStmt(tmpLocal,
									Jimple.v().newAddExpr(tmpLocal, LongConstant.v(1L)));
							AssignStmt toAdd3 = Jimple.v().newAssignStmt(Jimple.v().
									newStaticFieldRef(Instruction.makeRef()), tmpLocal);
							units.insertAfter(toAdd3, s);
							units.insertAfter(toAdd2, s);
							units.insertAfter(toAdd1, s);
							toAdd1 = Jimple.v().newAssignStmt(tmpLocal,Jimple.v().newStaticFieldRef(Instruction.makeRef()));
							toAdd2 = Jimple.v().newAssignStmt(tmpLocal,Jimple.v().newAddExpr(tmpLocal, LongConstant.v(1L)));
							toAdd3 = Jimple.v().newAssignStmt(Jimple.v().newStaticFieldRef(Instruction.makeRef()), tmpLocal);
							Unit target = ((IfStmt) s).getTarget();
							units.insertBefore(toAdd1, target);
							units.insertBefore(toAdd2, target);
							units.insertBefore(toAdd3, target);
							
					    }
					    else{*/
					    	AssignStmt toAdd1 = Jimple.v().newAssignStmt(tmpLocal,
									Jimple.v().newStaticFieldRef(Instruction.makeRef()));
							AssignStmt toAdd2 = Jimple.v().newAssignStmt(tmpLocal,
									Jimple.v().newAddExpr(tmpLocal, LongConstant.v(1L)));
							AssignStmt toAdd3 = Jimple.v().newAssignStmt(Jimple.v().
									newStaticFieldRef(Instruction.makeRef()), tmpLocal);
							units.insertBefore(toAdd1, s);
							units.insertBefore(toAdd2, s);
							units.insertBefore(toAdd3, s);
					    //}
					}else if(isMainMethod && (s instanceof  ReturnStmt || s instanceof
							 ReturnVoidStmt))
					{
						if(!addedLocals){
							 tmpRef = addTmpRef(body); tmpLong = addTmpLong(body);
							 addedLocals = true;
						}
						for(int i = 0; i < criticalstatements.size();++i){
							Instruction = classname.getFieldByName("temporarycounter"+criticalstatements.get(i).hash());
							addStmtsToBefore(units, s, Instruction, tmpRef, tmpLong, criticalstatements.get(i).nameofstmt());
						}
					}
				}
				
			}
		}
	}
	
	@Override
	protected void internalTransform(Body body, String phaseName,
			Map options) {
		
		
		if (!Scene.v().getMainClass().
				declaresMethod("void main(java.lang.String[])"))
				throw new RuntimeException("couldn’t find main() in mainClass");
		boolean isMainMethod = body.getMethod().getSubSignature().equals("void main(java.lang.String[])");
        if (isMainMethod) {
            // for each method in this class
            List<SootMethod> methods = body.getMethod().getDeclaringClass().getMethods();
            for (SootMethod m : methods) {
                boolean _isMainMethod = m.getSubSignature().equals("void main(java.lang.String[])");
                if (_isMainMethod)
                    continue;
                if (m.getName().equalsIgnoreCase("<init>"))
                    continue;
                this.ProcessMethod(body.getMethod().getDeclaringClass(), m,m.retrieveActiveBody(), phaseName, options);
            }
            this.ProcessMethod(body.getMethod().getDeclaringClass(), body.getMethod(),body, phaseName, options);
        } 
	
		


		// TODO Auto-generated method stub
		 
	}
}

class statements{
	private SootClass cname = null;
	private SootMethod methodname = null;
	private ArrayList<Stmt> listofstatement;
	private ArrayList<Integer> markedornot;
	public statements(){
		listofstatement = new ArrayList<Stmt>();
		markedornot = new ArrayList<Integer>();
	}
	
	public void set(SootClass cname, SootMethod methodname){
		this.cname = cname; this.methodname = methodname;
	}
	public void add(Stmt name, int box){
		listofstatement.add(name);
		markedornot.add(box);
	}
	public void change(Stmt name, int box){
		for(int i = 0; i < listofstatement.size(); ++ i){
			if(name.equals(listofstatement.get(i)) && markedornot.get(i).equals(box)){
				markedornot.set(i, -1);
			}
		}
	}
	public boolean equals(SootClass cl, SootMethod me){
		return cl.getName().equals(this.cname.getName()) && this.methodname.toString().equals(me.toString());
	}
	public ArrayList<Stmt> getstmts(){
		return listofstatement;	
	}
	public ArrayList<Integer> getmarked(){
		return markedornot;	
	}
}
 



public class Main {
	private static ArrayList<Unit> res = new ArrayList();
	private static List<SootMethod> methods = null;
	private static int methodsNum;
    private static List<SootField> myFields = new ArrayList<>();
    private static List<String> myNameList = new ArrayList<>();
    private static List<statements> allstatements = new ArrayList<>();
	public static void main(String[] args) {
		
		//Static Analysis (Retrieve Flow Graph)
		staticAnalysis();

		//Dynamic Analysis (Instrumentation) 
		dynamicAnalysis();
 
		soot.Main.main(args);
		
		System.out.println("-------------------------------------");
		System.out.println("End of execution");
	}

	private static void staticAnalysis(){
		configure("C:\\Users\\niyou\\Downloads\\CS201Fall19.tar\\CS201Fall19\\Analysis"); //Change this path to your Analysis folder path in your project directory
		SootClass sootClass = Scene.v().loadClassAndSupport("Test1");
	    sootClass.setApplicationClass();
	    List<SootMethod> tmp  = sootClass.getMethods();
        ListIterator<SootMethod> iterator = tmp.listIterator(); 
        while (iterator.hasNext()) {
        	SootMethod ite = iterator.next();
        	statements tmporarystatement = new statements();
        	tmporarystatement.set(sootClass, ite);
        	System.out.println("Method:"+ite);
        	BlockGraph bg = new BriefBlockGraph(ite.retrieveActiveBody());
        	DominAnalysis lv =	new DominAnalysis(bg);
        	ReachingDef ev = new ReachingDef(bg);
        	Iterator<Block> blockIt = bg.iterator();
        	String Loop = "";
        	String overrepeatedStatements = "";
        	ArraySparseSet allvariables = new ArraySparseSet();
        	while (blockIt.hasNext()) {
    			Block s = blockIt.next();
    			int blockid = s.getIndexInMethod();
    			System.out.println("BB"+blockid+":");
    			Iterator<Unit> itu = s.iterator();
    			while(itu.hasNext()){
    				Unit xtmp = itu.next();
    				System.out.println(xtmp);
    				tmporarystatement.add((Stmt)xtmp, blockid);
    				for(ValueBox x : xtmp.getDefBoxes()){
    					allvariables.add(x.getValue());
    				}
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
    					
    					ArrayList<Unit> used = new ArrayList<Unit>();
    					ArrayList<Integer> Location = new ArrayList<Integer>();
    					ArrayList<Integer> blocknum = new ArrayList<Integer>();
    					for(int i = 0; i < len; ++ i){
    						blocknum.add(pbloc.get(i).getIndexInMethod());
    						
    					}
    					
    					AnalysisDefs asd = new AnalysisDefs(bg,blocknum);
    					
    					for(int i = 0; i < len; ++ i){
    						Block wb  = pbloc.get(i);
    						Iterator<Unit> iunit = wb.iterator();
    						ArraySparseSet cur = (ArraySparseSet) asd.getFlowBefore(wb);
    						List<Value> allva = allvariables.toList();
    						while(iunit.hasNext()){
    							List<Value> lb= cur.toList();
    							Unit stm = iunit.next();
    							if(stm.getUseBoxes().size()>0){
    								boolean flag = true;
    								for(ValueBox x : stm.getUseBoxes()){
    									boolean isexists = true;
    									for(int j = 0; j < allva.size();++j){
    										if(allva.get(j).toString().equals(x.getValue().toString())){
    											isexists = false;
    										}
    									}
    									for(int j = 0; j < lb.size();++j){
    										if(x.getValue().toString().equals(lb.get(j).toString())){
    											isexists = true; break;
    										}
    									}
    									if(isexists == false){flag = false; break;}
    								}
    								if(flag == false){
    									tmporarystatement.change((Stmt)stm, wb.getIndexInMethod());
    									overrepeatedStatements += stm.toString() + "\n"; 
    								}
    							}
    							if(stm.getDefBoxes().size()>0){
    								for(ValueBox x : stm.getDefBoxes()){
    									cur.add(x.getValue());
    								}
    							}
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
        	allstatements.add(tmporarystatement);
        }
	    //Static Analysis code
	}
	




	private static void dynamicAnalysis(){
		Counter.setFlowSet(allstatements);
		PackManager.v().getPack("jtp").add(new Transform("jtp.myInstrumenter", Counter.v()));
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
