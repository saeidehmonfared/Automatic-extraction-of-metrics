package MetricExtraction;
import ANTLRParser.*;
import ASTGenerator.StaticList;
import Scopes.ClassScope;
import Scopes.GlobalScope;
import Scopes.Scope;
import Symbols.MethodSymbol;
import Symbols.Symbol;
import Symbols.VariableSymbol;
import org.antlr.v4.runtime.tree.ParseTreeProperty;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by saeideh on 1/9/17.
 */
public class ClassLevelMetrics extends javaBaseListener{
    ParseTreeProperty<Scope> scopes;
    GlobalScope globals;
    Scope currentScope;
   public String classname;
    public String classname1;
    String packagename="";
    Symbol classsymbol=null;
    Map<Symbol,Map<String,ArrayList<String>>>listofclass=new LinkedHashMap<Symbol,Map<String, ArrayList<String>>>();
    Map<String,ArrayList<String>>metriclist=new LinkedHashMap<String,ArrayList<String>>();
    ArrayList<Symbol>allofmethods=new ArrayList<Symbol>();
    ArrayList<Symbol>overridedmethods=new ArrayList<Symbol>();
    public static int allofabstractmethods=0;
    public static ArrayList<String> allofabstractmethodslist=new ArrayList<String>();
    int numberofoverridemethods;
    public ClassLevelMetrics(GlobalScope globals, ParseTreeProperty<Scope> Scopes) {
        this.globals = globals;
        this.scopes = Scopes;

    }


    public void enterCompilationUnit(javaParser.CompilationUnitContext ctx) {
        currentScope = this.globals;

        for (Symbol value : currentScope.symboltableshow().values()) {
            Symbol s = value;
            classname = s.name;
            if (s.type.equals(Symbol.Type.tCLASS)) {
              //  classsymbol=s;
                System.out.println("Class name is:" + s.name + "\npackage of this class is:" + s.packagename);
            } else if (s.type.equals(Symbol.Type.tINTERFACE)) {
               //@@ System.out.println("Interface name is:" + s.name + "\npackage of this interface is:" + s.packagename);
            }


        }
        metriclist.put("publicmethods",new ArrayList<String>());
        metriclist.put("privatemethods",new ArrayList<String>());
        metriclist.put("protectedmethods",new ArrayList<String>());
        metriclist.put("publicvariables",new ArrayList<String>());
        metriclist.put("protectedvariables",new ArrayList<String>());
        metriclist.put("privatevariables",new ArrayList<String>());
        listofclass.put(classsymbol,metriclist);


    }

    public void exitCompilationUnit(javaParser.CompilationUnitContext ctx) {


        }
        //-------------------------------------------------------------------------
        @Override public void enterPackageDeclaration(javaParser.PackageDeclarationContext ctx) {

            for(int i=1;i<ctx.getChildCount()-1;i++){
                packagename=packagename+ctx.getChild(i).getText();
            }
            packagename=packagename+".";

            // System.out.println("mypackname:"+packagename);
        }

    @Override public void exitPackageDeclaration(javaParser.PackageDeclarationContext ctx) {



    }
//------------------------------------------------------------------
    @Override public void enterNormalClassDeclaration1(javaParser.NormalClassDeclaration1Context ctx) {

        ArrayList<String> listofPublicmethods = new ArrayList<String>();
        ArrayList<String> listofPrivatemethods = new ArrayList<String>();
        ArrayList<String> listofStaticmethods = new ArrayList<String>();
        ArrayList<String> listofAbstractcMethods = new ArrayList<String>();
        ArrayList<String> listofProtectedmethods = new ArrayList<String>();
        ArrayList<String> listofPublicVariables = new ArrayList<String>();
        ArrayList<String> listofPrivateVariables = new ArrayList<String>();
        ArrayList<String> listofProtectedVariables = new ArrayList<String>();
        ArrayList<String> listofStaticvariables = new ArrayList<String>();

        currentScope = scopes.get(ctx);
        classname1=ctx.Identifier().getText();



        int Methodclasscount = 0;
        int Variableclasscount = 0;

        for (Symbol value : currentScope.symboltableshow().values()) {
            Symbol s = value;

            if (s.getClass().getName().equals("Symbols.MethodSymbol")) {
                //Symbol m= (MethodSymbol)s;
                Methodclasscount = Methodclasscount + 1;
                allofmethods.add(s);

                for (int i = 0; i < ((MethodSymbol) s).methodmodifier.size(); i++) {


                    if (((MethodSymbol) s).methodmodifier.get(i).equals(Symbol.AccessModifier.tpublic)){
                        listofPublicmethods.add(s.name);}
                       else if (((MethodSymbol) s).methodmodifier.get(i).equals(Symbol.AccessModifier.tprivate)){
                        listofPrivatemethods.add(s.name);}
                    else if (((MethodSymbol) s).methodmodifier.get(i).equals(Symbol.AccessModifier.tprotected)){
                        listofProtectedmethods.add(s.name);}
                    else if (((MethodSymbol) s).methodmodifier.get(i).equals(Symbol.AccessModifier.TSTATIC)){
                        listofStaticmethods.add(s.name);}
                    else if (((MethodSymbol) s).methodmodifier.get(i).equals(Symbol.AccessModifier.TABSTRACT)){
                        listofAbstractcMethods.add(s.name);}

                }

            } else if (s.getClass().getName().equals("Symbols.VariableSymbol")) { //TODO
                Variableclasscount = Variableclasscount + 1;

                for (int i = 0; i < ((VariableSymbol) s).variablemodifier.size(); i++) {
                    if (((VariableSymbol) s).variablemodifier.get(i).equals(Symbol.AccessModifier.tpublic))
                        listofPublicVariables.add(s.name);
                    else if (((VariableSymbol) s).variablemodifier.get(i).equals(Symbol.AccessModifier.tprivate))
                        listofPrivateVariables.add(s.name);

                    else if (((VariableSymbol) s).variablemodifier.get(i).equals(Symbol.AccessModifier.tprotected))
                        listofProtectedVariables.add(s.name);
                    else if (((VariableSymbol) s).variablemodifier.get(i).equals(Symbol.AccessModifier.TSTATIC))
                        listofStaticvariables.add(s.name);
                }


            }

        }

            allofabstractmethods=allofabstractmethods+listofAbstractcMethods.size();
        allofabstractmethodslist.addAll(listofAbstractcMethods);

        metriclist.get("publicmethods").addAll(listofPublicmethods);
        metriclist.get("protectedmethods").addAll(listofProtectedmethods);
        metriclist.get("privatemethods").addAll(listofPrivatemethods);
        metriclist.get("publicvariables").addAll(listofPublicVariables);
        metriclist.get("privatevariables").addAll(listofPrivateVariables);
        metriclist.get("protectedvariables").addAll(listofProtectedVariables);

        Symbol s1=new Symbol(classname1,packagename);
        classsymbol=s1;
        Staticlistclasslevelmetrics.put(s1,metriclist);


      //@@  System.out.println("number of methods of this class is:"+Methodclasscount);
       //@@ System.out.println("PublicMethods:"+listofPublicmethods);
       //@@ System.out.println("privateMethods:"+listofPrivatemethods);
       //@@ System.out.println("Protectedmethods:"+listofProtectedmethods);
       //@@ System.out.println("Abstractmethods:"+listofAbstractcMethods);
       //@@ System.out.println("Staticmethods:"+listofStaticmethods);

        System.out.println("number of variables of this class is:"+Variableclasscount);
        System.out.println("PublicVariables:"+listofPublicVariables);
        System.out.println("privateVariables:"+listofPrivateVariables);
        System.out.println("ProtectedVariables:"+listofProtectedVariables);
        System.out.println("StaticVariables:"+listofStaticvariables);





    }

    @Override public void exitNormalClassDeclaration1(javaParser.NormalClassDeclaration1Context ctx) {

        ///fekr mikonam hadaf inja mohasebey overload hast

        //TODO:number of overload of on method should be calculat
        int count=0;
        boolean b=true;
        for(int i=0;i<allofmethods.size();i++){
            for(int j=i+1;j<allofmethods.size();j++)
            {
                if(allofmethods.get(i).name.equals(allofmethods.get(j).name)){
                    if(overridedmethods.isEmpty()){
                        overridedmethods.add(allofmethods.get(i));
                        numberofoverridemethods++;
                    }
                    for(int k=0;k<overridedmethods.size();k++){
                        if(allofmethods.get(i).name.equals(overridedmethods.get(k).name)){
                            count=1;
                            break;

                        }
                    }
                    if(count==0){
                        b=false;
                    }
                    if(!b){
                        overridedmethods.add(allofmethods.get(i));
                        numberofoverridemethods++;

                    }
                    count=0;
                    b=true;

                }

            }
        }
        System.out.println("number of overloaded methods:"+numberofoverridemethods);
        System.out.println("list of overloaded methods:"+overridedmethods);

    }


    @Override public void enterNormalClassdeclaration2(javaParser.NormalClassdeclaration2Context ctx) {

        ArrayList<String> listofPublicmethods = new ArrayList<String>();
        ArrayList<String> listofPrivatemethods = new ArrayList<String>();
        ArrayList<String> listofStaticmethods = new ArrayList<String>();
        ArrayList<String> listofAbstractcMethods = new ArrayList<String>();
        ArrayList<String> listofProtectedmethods = new ArrayList<String>();
        ArrayList<String> listofPublicVariables = new ArrayList<String>();
        ArrayList<String> listofPrivateVariables = new ArrayList<String>();
        ArrayList<String> listofProtectedVariables = new ArrayList<String>();
        ArrayList<String> listofStaticvariables = new ArrayList<String>();

        currentScope = scopes.get(ctx);



        int Methodclasscount = 0;
        int Variableclasscount = 0;

        for (Symbol value : currentScope.symboltableshow().values()) {
            Symbol s = value;

            if (s.getClass().getName().equals("Symbols.MethodSymbol")) {
                allofmethods.add(s);
                //Symbol m= (MethodSymbol)s;
                Methodclasscount = Methodclasscount + 1;

                for (int i = 0; i < ((MethodSymbol) s).methodmodifier.size(); i++) {


                    if (((MethodSymbol) s).methodmodifier.get(i).equals(Symbol.AccessModifier.tpublic)){
                        listofPublicmethods.add(s.name);}
                    else if (((MethodSymbol) s).methodmodifier.get(i).equals(Symbol.AccessModifier.tprivate)){
                        listofPrivatemethods.add(s.name);}
                    else if (((MethodSymbol) s).methodmodifier.get(i).equals(Symbol.AccessModifier.tprotected)){
                        listofProtectedmethods.add(s.name);}
                    else if (((MethodSymbol) s).methodmodifier.get(i).equals(Symbol.AccessModifier.TSTATIC)){
                        listofStaticmethods.add(s.name);}
                    else if (((MethodSymbol) s).methodmodifier.get(i).equals(Symbol.AccessModifier.TABSTRACT)){
                        listofAbstractcMethods.add(s.name);}

                }

            } else if (s.getClass().getName().equals("Symbols.VariableSymbol")) {
                Variableclasscount = Variableclasscount + 1;

                for (int i = 0; i < ((VariableSymbol) s).variablemodifier.size(); i++) {
                    if (((VariableSymbol) s).variablemodifier.get(i).equals(Symbol.AccessModifier.tpublic))
                        listofPublicVariables.add(s.name);
                    else if (((VariableSymbol) s).variablemodifier.get(i).equals(Symbol.AccessModifier.tprivate))
                        listofPrivateVariables.add(s.name);

                    else if (((VariableSymbol) s).variablemodifier.get(i).equals(Symbol.AccessModifier.tprotected))
                        listofProtectedVariables.add(s.name);
                    else if (((VariableSymbol) s).variablemodifier.get(i).equals(Symbol.AccessModifier.TSTATIC))
                        listofStaticvariables.add(s.name);
                }


            }

        }


                classname1=ctx.Identifier().getText();

        allofabstractmethods=allofabstractmethods+listofAbstractcMethods.size();
        allofabstractmethodslist.addAll(listofAbstractcMethods);

        metriclist.get("publicmethods").addAll(listofPublicmethods);
        metriclist.get("protectedmethods").addAll(listofProtectedmethods);
        metriclist.get("privatemethods").addAll(listofPrivatemethods);
        metriclist.get("publicvariables").addAll(listofPublicVariables);
        metriclist.get("privatevariables").addAll(listofPrivateVariables);
        metriclist.get("protectedvariables").addAll(listofProtectedVariables);

        Symbol s1=new Symbol(classname1,packagename);
        classsymbol=s1;
        Staticlistclasslevelmetrics.put(s1,metriclist);


        System.out.println("number of methods of this class is:"+Methodclasscount);
        System.out.println("PublicMethods:"+listofPublicmethods);
        System.out.println("privateMethods:"+listofPrivatemethods);
        System.out.println("Protectedmethods:"+listofProtectedmethods);
        System.out.println("Abstractmethods:"+listofAbstractcMethods);
        System.out.println("Staticmethods:"+listofStaticmethods);

        System.out.println("number of variables of this class is:"+Variableclasscount);
        System.out.println("PublicVariables:"+listofPublicVariables);
        System.out.println("privateVariables:"+listofPrivateVariables);
        System.out.println("ProtectedVariables:"+listofProtectedVariables);
        System.out.println("StaticVariables:"+listofStaticvariables);




    }

    @Override public void exitNormalClassdeclaration2(javaParser.NormalClassdeclaration2Context ctx) {

        int count = 0;
        boolean b = true;
        for (int i = 0; i < allofmethods.size(); i++) {
            for (int j = i + 1; j < allofmethods.size(); j++) {
                if (allofmethods.get(i).name.equals(allofmethods.get(j).name)) {
                    if (overridedmethods.isEmpty()) {
                        overridedmethods.add(allofmethods.get(i));
                        numberofoverridemethods++;
                    }
                    for (int k = 0; k < overridedmethods.size(); k++) {
                        if (allofmethods.get(i).name.equals(overridedmethods.get(k).name)) {
                            count = 1;
                            break;

                        }
                    }
                    if (count == 0) {
                        b = false;
                    }
                    if (!b) {
                        overridedmethods.add(allofmethods.get(i));
                        numberofoverridemethods++;

                    }
                    count = 0;
                    b = true;

                }

            }



        }

        System.out.println("number of overloaded methods:" + numberofoverridemethods);
        System.out.println("list of overloaded methods:" + overridedmethods);
    }
    //------------------------------------------------------------------------
    @Override public void enterNormalInterfaceDeclaration1(javaParser.NormalInterfaceDeclaration1Context ctx) {

        ArrayList<String> listofPublicmethods = new ArrayList<String>();
        ArrayList<String> listofPrivatemethods = new ArrayList<String>();
        ArrayList<String> listofStaticmethods = new ArrayList<String>();
        ArrayList<String> listofAbstractcMethods = new ArrayList<String>();
        ArrayList<String> listofProtectedmethods = new ArrayList<String>();
        ArrayList<String> listofPublicVariables = new ArrayList<String>();
        ArrayList<String> listofPrivateVariables = new ArrayList<String>();
        ArrayList<String> listofProtectedVariables = new ArrayList<String>();
        ArrayList<String> listofStaticvariables = new ArrayList<String>();

        classname1=ctx.Identifier().getText();

        currentScope = scopes.get(ctx);
       //@@ System.out.println("rrrrrrrrrrrrrrrrrrrrrrrrrrrsssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssss"+currentScope);



        int Methodclasscount = 0;
        int Variableclasscount = 0;

        for (Symbol value : currentScope.symboltableshow().values()) {
            Symbol s = value;

            if (s.getClass().getName().equals("Symbols.MethodSymbol")) {
                allofmethods.add(s);
                //Symbol m= (MethodSymbol)s;
                Methodclasscount = Methodclasscount + 1;

                for (int i = 0; i < ((MethodSymbol) s).methodmodifier.size(); i++) {


                    if (((MethodSymbol) s).methodmodifier.get(i).equals(Symbol.AccessModifier.tpublic)){
                        listofPublicmethods.add(s.name);}
                    else if (((MethodSymbol) s).methodmodifier.get(i).equals(Symbol.AccessModifier.tprivate)){
                        listofPrivatemethods.add(s.name);}
                    else if (((MethodSymbol) s).methodmodifier.get(i).equals(Symbol.AccessModifier.tprotected)){
                        listofProtectedmethods.add(s.name);}
                    else if (((MethodSymbol) s).methodmodifier.get(i).equals(Symbol.AccessModifier.TSTATIC)){
                        listofStaticmethods.add(s.name);}
                    else if (((MethodSymbol) s).methodmodifier.get(i).equals(Symbol.AccessModifier.TABSTRACT)){
                        listofAbstractcMethods.add(s.name);}

                }

            } else if (s.getClass().getName().equals("Symbols.VariableSymbol")) {
                Variableclasscount = Variableclasscount + 1;

                for (int i = 0; i < ((VariableSymbol) s).variablemodifier.size(); i++) {
                    if (((VariableSymbol) s).variablemodifier.get(i).equals(Symbol.AccessModifier.tpublic))
                        listofPublicVariables.add(s.name);
                    else if (((VariableSymbol) s).variablemodifier.get(i).equals(Symbol.AccessModifier.tprivate))
                        listofPrivateVariables.add(s.name);

                    else if (((VariableSymbol) s).variablemodifier.get(i).equals(Symbol.AccessModifier.tprotected))
                        listofProtectedVariables.add(s.name);
                    else if (((VariableSymbol) s).variablemodifier.get(i).equals(Symbol.AccessModifier.TSTATIC))
                        listofStaticvariables.add(s.name);
                }


            }

        }




        allofabstractmethods=allofabstractmethods+listofAbstractcMethods.size();
        allofabstractmethodslist.addAll(listofAbstractcMethods);

        metriclist.get("publicmethods").addAll(listofPublicmethods);
        metriclist.get("protectedmethods").addAll(listofProtectedmethods);
        metriclist.get("privatemethods").addAll(listofPrivatemethods);
        metriclist.get("publicvariables").addAll(listofPublicVariables);
        metriclist.get("privatevariables").addAll(listofPrivateVariables);
        metriclist.get("protectedvariables").addAll(listofProtectedVariables);

        Symbol s1=new Symbol(classname1,packagename);
        classsymbol=s1;
        Staticlistclasslevelmetrics.put(s1,metriclist);


       //@@ System.out.println("number of methods of this interface is:"+Methodclasscount);
       //@@ System.out.println("PublicMethods:"+listofPublicmethods);
      //@@  System.out.println("privateMethods:"+listofPrivatemethods);
       //@@ System.out.println("Protectedmethods:"+listofProtectedmethods);
        //@@System.out.println("Abstractmethods:"+listofAbstractcMethods);
        //@@System.out.println("Staticmethods:"+listofStaticmethods);

       //@@ System.out.println("number of variables of this interface is:"+Variableclasscount);
       //@@ System.out.println("PublicVariables:"+listofPublicVariables);
        //@@System.out.println("privateVariables:"+listofPrivateVariables);
        //@@System.out.println("ProtectedVariables:"+listofProtectedVariables);
        //@@System.out.println("StaticVariables:"+listofStaticvariables);




    }

    @Override public void exitNormalInterfaceDeclaration1(javaParser.NormalInterfaceDeclaration1Context ctx) {

    }
    //----------------------------------------------------------------------------
    @Override public void enterNormalInterfaceDeclaration2(javaParser.NormalInterfaceDeclaration2Context ctx) {

        ArrayList<String> listofPublicmethods = new ArrayList<String>();
        ArrayList<String> listofPrivatemethods = new ArrayList<String>();
        ArrayList<String> listofStaticmethods = new ArrayList<String>();
        ArrayList<String> listofAbstractcMethods = new ArrayList<String>();
        ArrayList<String> listofProtectedmethods = new ArrayList<String>();
        ArrayList<String> listofPublicVariables = new ArrayList<String>();
        ArrayList<String> listofPrivateVariables = new ArrayList<String>();
        ArrayList<String> listofProtectedVariables = new ArrayList<String>();
        ArrayList<String> listofStaticvariables = new ArrayList<String>();

        currentScope = scopes.get(ctx);

        //@@System.out.println("rrrrrrrrrrrrrrrrrrrrrrrrrrrsssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssss"+currentScope);


        int Methodclasscount = 0;
        int Variableclasscount = 0;

        for (Symbol value : currentScope.symboltableshow().values()) {
            Symbol s = value;

            if (s.getClass().getName().equals("Symbols.MethodSymbol")) {
                allofmethods.add(s);
                //Symbol m= (MethodSymbol)s;
                Methodclasscount = Methodclasscount + 1;

                for (int i = 0; i < ((MethodSymbol) s).methodmodifier.size(); i++) {


                    if (((MethodSymbol) s).methodmodifier.get(i).equals(Symbol.AccessModifier.tpublic)){
                        listofPublicmethods.add(s.name);}
                    else if (((MethodSymbol) s).methodmodifier.get(i).equals(Symbol.AccessModifier.tprivate)){
                        listofPrivatemethods.add(s.name);}
                    else if (((MethodSymbol) s).methodmodifier.get(i).equals(Symbol.AccessModifier.tprotected)){
                        listofProtectedmethods.add(s.name);}
                    else if (((MethodSymbol) s).methodmodifier.get(i).equals(Symbol.AccessModifier.TSTATIC)){
                        listofStaticmethods.add(s.name);}
                    else if (((MethodSymbol) s).methodmodifier.get(i).equals(Symbol.AccessModifier.TABSTRACT)){
                        listofAbstractcMethods.add(s.name);}

                }

            } else if (s.getClass().getName().equals("Symbols.VariableSymbol")) {
                Variableclasscount = Variableclasscount + 1;

                for (int i = 0; i < ((VariableSymbol) s).variablemodifier.size(); i++) {
                    if (((VariableSymbol) s).variablemodifier.get(i).equals(Symbol.AccessModifier.tpublic))
                        listofPublicVariables.add(s.name);
                    else if (((VariableSymbol) s).variablemodifier.get(i).equals(Symbol.AccessModifier.tprivate))
                        listofPrivateVariables.add(s.name);

                    else if (((VariableSymbol) s).variablemodifier.get(i).equals(Symbol.AccessModifier.tprotected))
                        listofProtectedVariables.add(s.name);
                    else if (((VariableSymbol) s).variablemodifier.get(i).equals(Symbol.AccessModifier.TSTATIC))
                        listofStaticvariables.add(s.name);
                }


            }

        }


        classname1=ctx.Identifier().getText();

        allofabstractmethods=allofabstractmethods+listofAbstractcMethods.size();
        allofabstractmethodslist.addAll(listofAbstractcMethods);

        metriclist.get("publicmethods").addAll(listofPublicmethods);
        metriclist.get("protectedmethods").addAll(listofProtectedmethods);
        metriclist.get("privatemethods").addAll(listofPrivatemethods);
        metriclist.get("publicvariables").addAll(listofPublicVariables);
        metriclist.get("privatevariables").addAll(listofPrivateVariables);
        metriclist.get("protectedvariables").addAll(listofProtectedVariables);

        Symbol s1=new Symbol(classname1,packagename);
        classsymbol=s1;
        Staticlistclasslevelmetrics.put(s1,metriclist);


        System.out.println("number of methods of this interface is:"+Methodclasscount);
        System.out.println("PublicMethods:"+listofPublicmethods);
        System.out.println("privateMethods:"+listofPrivatemethods);
        System.out.println("Protectedmethods:"+listofProtectedmethods);
        System.out.println("Abstractmethods:"+listofAbstractcMethods);
        System.out.println("Staticmethods:"+listofStaticmethods);

        System.out.println("number of variables of this interface is:"+Variableclasscount);
        System.out.println("PublicVariables:"+listofPublicVariables);
        System.out.println("privateVariables:"+listofPrivateVariables);
        System.out.println("ProtectedVariables:"+listofProtectedVariables);
        System.out.println("StaticVariables:"+listofStaticvariables);




    }

    @Override public void exitNormalInterfaceDeclaration2(javaParser.NormalInterfaceDeclaration2Context ctx) {

    }
    //-------------------------------------------------------------------------------------



    //--------------------------------------------------------------------
    @Override public void enterMethodDeclarator(javaParser.MethodDeclaratorContext ctx) {


        //currentScope=scopes.get(ctx);
        //VariableSymbol var=(VariableSymbol)currentScope.resolve("m1");
        // System.out.println(var.vartype);
       /// System.out.println( currentScope);



    }

    @Override public void enterBlock(javaParser.BlockContext ctx) {


    }
    @Override public void exitBlock(javaParser.BlockContext ctx) {

        //currentScope=scopes.get(ctx);
        //VariableSymbol var=(VariableSymbol)currentScope.resolve("m1");
        //2System.out.println(var.vartype);
        //System.out.println( currentScope.symboltableshow());
    }



}
