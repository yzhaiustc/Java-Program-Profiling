# Java Program Profiling

## Group Members

<strong>Student1</strong>: Yuzhe (Zack) Ni

NetID: yni012

SID: 862140656

---

<strong>Student2</strong>: Yujia Zhai

NetID: yzhai015

SID: 862120464


## How To Run

* This project needs both JDK7 (for Dava decompilation) and JDK8 for both static/dynamic analysis. When you run with JDK7, you should select the external soot library ```soot-2.5.0.jar```. Otherwise, that's to say, you are running with JDK8, the external soot library should be selected as ```sootclasses-trunk-jar-with-dependencies.jar```.
* For the four sample test programs, ```Test{1-4}.java```, you should run static AND dynamic analysis under JDK8, and then SWITCH to JDK7 with the soot library for decompilation. Then you can find your decompiled JAVA program under ```sootOutput/dava/src/``` . Repeat this procedure for ALL test cases you have.
* Go to ```sootOutput/dava/src/``` to do manual compilation for all ```*.java``` you got.
* Just run the code using ```java Test1``` if you are to run ```Test1.class``` you just compiled.

## Extra Notes
* You MUST change the configuration in ```Line 467``` of ```Main.java``` by yourself with your local path.
* Though we tried to avoid ```SocketFlow.*``` generated by Dava decompilation, there still exists this issue in the decompiled Java programs. You can modify ```SocketFlow.*``` to an integer manually to make the decompiled Java program ready to compile.
* Test2 is faulty due to the existing bug in Dava.

## Sample Output
* I saved ALL static profiling output results under ```myOutput/staticOutput```.
* Dynamic profiling output results are generated by running the ```runDynamic.sh```. The output results are saved under ```myOutput/dynamicOutput```.