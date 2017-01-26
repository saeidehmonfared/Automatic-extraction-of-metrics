package MetricExtraction;
import ANTLRParser.*;
import ASTGenerator.CheckVariableSymbol;
import MetricExtraction.CouplingExtraction.Invoc;
import MetricExtraction.CouplingExtraction.Object;
import Scopes.GlobalScope;
import Scopes.Scope;
import Symbols.MethodSymbol;
import Symbols.Symbol;
import Symbols.VariableSymbol;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeProperty;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by saeideh on 1/15/17.
 */
public class CohesionMetrics extends javaBaseListener {
    ParseTreeProperty<Scope> scopes;
    GlobalScope globals;
    Scope currentScope;
    Map<String, VariableSymbol> argumentlist = new LinkedHashMap<String, VariableSymbol>();
    public ArrayList<Symbol.AccessModifier> variablemodifier = new ArrayList<Symbol.AccessModifier>();
    Map<String, ArrayList<Symbol>> methodlist = new LinkedHashMap<String, ArrayList<Symbol>>();
    ArrayList<String> distinictargs = new ArrayList<String>();
    ParseTreeProperty<VariableSymbol.TYPE> Types = new ParseTreeProperty<VariableSymbol.TYPE>();
    Map<String, ArrayList<String>> Allofmethods=new LinkedHashMap<String, ArrayList<String>>();
    Map<String, ArrayList<String >>classvariables=new LinkedHashMap<String,ArrayList<String>>();
    Map<String,ArrayList<String>>methodcalls=new LinkedHashMap<String,ArrayList<String>>();
    public ArrayList<Object> objectinstances = new ArrayList<Object>();
    Map<String, Symbol> importlistofclass = new LinkedHashMap<String, Symbol>();


    public void setValue(ParseTree ctx, VariableSymbol.TYPE value) {
        Types.put(ctx, value);
    }

    public VariableSymbol.TYPE getValue(ParseTree ctx) {
        return Types.get(ctx);
    }


    public CohesionMetrics(GlobalScope globals, ParseTreeProperty<Scope> scopes,ArrayList<Object>objectinsatnce,Map<String,Symbol>importlistofclass) {
        this.globals = globals;
        this.scopes = scopes;
        this.objectinstances=objectinsatnce;
        this.importlistofclass=importlistofclass;

    }


    public void enterCompilationUnit(javaParser.CompilationUnitContext ctx) {

        currentScope = this.globals;
        System.out.println("cohesion metrics is:");


    }

    public void exitCompilationUnit(javaParser.CompilationUnitContext ctx) {
        System.out.println("All of methods with distinict parameters:");
        System.out.println(Allofmethods);


    }
    //----------------------------------------------------------------

    @Override
    public void enterNormalClassDeclaration1(javaParser.NormalClassDeclaration1Context ctx) {
        currentScope = scopes.get(ctx);

        for (Symbol value : currentScope.symboltableshow().values()) {
            Symbol s = value;
            if (s.getClass().getName().equals("Symbols.VariableSymbol")) {
                ArrayList<String> var=new ArrayList<String>();
                classvariables.put(((VariableSymbol)s).name,var);
            }
        }
        for (Symbol value : currentScope.symboltableshow().values()) {
            Symbol s = value;
            if (s.getClass().getName().equals("Symbols.MethodSymbol")) {
                ArrayList<String> var=new ArrayList<String>();
                methodcalls.put(((MethodSymbol)s).name,var);
            }
        }

    }

    @Override
    public void exitNormalClassDeclaration1(javaParser.NormalClassDeclaration1Context ctx) {
        currentScope = currentScope.getEnclosingScope();
        System.out.println("classs variables is:"+classvariables);
        System.out.println("method calls is:"+methodcalls);
    }

    //-----------------------------------------------------------------------------------------------------
    @Override
    public void enterNormalClassdeclaration2(javaParser.NormalClassdeclaration2Context ctx) {
        currentScope = scopes.get(ctx);

        for (Symbol value : currentScope.symboltableshow().values()) {
            Symbol s = value;
            if (s.getClass().getName().equals("Symbols.VariableSymbol")) {
                ArrayList<String>var=new ArrayList<String>();
                classvariables.put(((VariableSymbol)s).name,var);
            }
        }
        for (Symbol value : currentScope.symboltableshow().values()) {
            Symbol s = value;
            if (s.getClass().getName().equals("Symbols.MethodSymbol")) {
                ArrayList<String> var=new ArrayList<String>();
                methodcalls.put(((MethodSymbol)s).name,var);
            }
        }
    }

    @Override
    public void exitNormalClassdeclaration2(javaParser.NormalClassdeclaration2Context ctx) {
        currentScope = currentScope.getEnclosingScope();
        System.out.println("classs variables:"+classvariables);
        System.out.println("method calls is:"+methodcalls);
    }

    //-----------------------------------------------------------------------
    @Override
    public void enterMethodDeclaration(javaParser.MethodDeclarationContext ctx) {
        currentScope = scopes.get(ctx);
        distinictargs.clear();


    }

    @Override
    public void exitMethodDeclaration(javaParser.MethodDeclarationContext ctx) {


        String parametertypename=null;
        System.out.println("name of method is:" + currentScope.getScopeName());
        //System.out.println(argumentlist);
        boolean b = true;
        VariableSymbol s=null;
        if(!(argumentlist.equals(null))) {
            for (String value : argumentlist.keySet()) {
                b=true;
                s = argumentlist.get(value);
                if ((s.vartype.equals(VariableSymbol.TYPE.TREFRENCE))) {
                    parametertypename = value.toString();
                } else if (!(s.vartype.equals(VariableSymbol.TYPE.TREFRENCE))) {
                    parametertypename=s.vartype.toString();
                }


                for (int i = 0; i < distinictargs.size(); i++) {
                    if (parametertypename.equals(distinictargs.get(i))) {
                        b = false;
                        break;

                    }
                }

                if (b == true) {
                    distinictargs.add(parametertypename);
                }

            }
        }
            if(distinictargs!=null){
                System.out.println("distinict parameter type of this method is:"+distinictargs+"\n");
        String methodname=currentScope.getScopeName().toString();

                ArrayList<String>args=new ArrayList<String>();
             Allofmethods.put(methodname,args);
                Allofmethods.get(methodname).addAll(distinictargs);



            }

      // System.out.println(currentScope.symboltableshow());
        currentScope = currentScope.getEnclosingScope();
        argumentlist.clear();

    }

    //-------------------------------------------------------------------------------

    public void enterUnannType(javaParser.UnannTypeContext ctx) {


    }

    @Override
    public void exitUnannType(javaParser.UnannTypeContext ctx) {
        VariableSymbol.TYPE type = getValue(ctx.getChild(0));
        setValue(ctx, type);

    }

    //-------------------------------------------------------------------------
    @Override
    public void enterUnannPrimitiveType(javaParser.UnannPrimitiveTypeContext ctx) {
    }

    @Override
    public void exitUnannPrimitiveType(javaParser.UnannPrimitiveTypeContext ctx) {
        int t = ctx.start.getType();

        VariableSymbol.TYPE type = CheckVariableSymbol.getType(t);
        setValue(ctx, type);
        // System.out.println(type);

    }

    //-----------------------------------------------------------------------------
    @Override
    public void enterUnannReferenceType(javaParser.UnannReferenceTypeContext ctx) {

    }

    @Override
    public void exitUnannReferenceType(javaParser.UnannReferenceTypeContext ctx) {
        VariableSymbol.TYPE type = getValue(ctx.getChild(0));
        setValue(ctx, type);
        //System.out.println(type);
    }

    //------------------------------------------------------------------------------
    @Override
    public void enterUnannClassOrInterfaceType(javaParser.UnannClassOrInterfaceTypeContext ctx) {
    }

    @Override
    public void exitUnannClassOrInterfaceType(javaParser.UnannClassOrInterfaceTypeContext ctx) {
        VariableSymbol.TYPE type = getValue(ctx.getChild(0));
        setValue(ctx, type);
        //System.out.println(type);

    }

    //------------------------------------------------------------------------------------

    @Override
    public void enterUnannClassType_lfno_unannClassOrInterfaceType(javaParser.UnannClassType_lfno_unannClassOrInterfaceTypeContext ctx) {
    }

    @Override
    public void exitUnannClassType_lfno_unannClassOrInterfaceType(javaParser.UnannClassType_lfno_unannClassOrInterfaceTypeContext ctx) {
        int t = ctx.start.getType();

        VariableSymbol.TYPE type = CheckVariableSymbol.getType(t);
        setValue(ctx, type);

    }
    //--------------------------------------------------------------------------------------


    public void enterFormalParameters(javaParser.FormalParametersContext ctx) {
        //String[] par = new String[4];

    }


    @Override
    public void exitFormalParameters(javaParser.FormalParametersContext ctx) {

        VariableSymbol.TYPE type = getValue(ctx.getChild(0));
        setValue(ctx, type);

    }
    //----------------------------------------------------------------------

    @Override
    public void enterFormalParameter(javaParser.FormalParameterContext ctx) {
    }

    /**
     * {@inheritDoc}
     * <p>
     * <p>The default implementation does nothing.</p>
     */
    @Override
    public void exitFormalParameter(javaParser.FormalParameterContext ctx) {

        String m = ctx.variableDeclaratorId().getText();
        VariableSymbol.TYPE type = getValue(ctx.getChild(0));

        VariableSymbol v = new VariableSymbol(m, variablemodifier, type);


        if (type.equals(VariableSymbol.TYPE.TREFRENCE)) {

            String s = ctx.unannType().unannReferenceType().unannClassOrInterfaceType().unannClassType_lfno_unannClassOrInterfaceType().Identifier().getText();

            argumentlist.put(s, v);
            // System.out.println(type);

            // System.out.println(currentscope);
            //currentscope = currentscope.getEnclosingScope();

        }
        else if(!(type.equals(VariableSymbol.TYPE.TREFRENCE))){
            argumentlist.put(v.name,v);
        }
    }

    //----------------------------------------------------------------
    @Override
    public void enterLastFormalParameter(javaParser.LastFormalParameterContext ctx) {
    }

    @Override
    public void exitLastFormalParameter(javaParser.LastFormalParameterContext ctx) {
        String m = ctx.formalParameter().variableDeclaratorId().getText();
        VariableSymbol.TYPE type = getValue(ctx.formalParameter().unannType());
        VariableSymbol v = new VariableSymbol(m, variablemodifier, type);

        if (type.equals(VariableSymbol.TYPE.TREFRENCE)) {

            String s = ctx.formalParameter().unannType().unannReferenceType().unannClassOrInterfaceType().unannClassType_lfno_unannClassOrInterfaceType().getText();

            argumentlist.put(s, v);
        }
        else if(!(type.equals(VariableSymbol.TYPE.TREFRENCE))){
            argumentlist.put(v.name,v);
        }





    }

    @Override public void enterAssignment(javaParser.AssignmentContext ctx) {
        ArrayList<String>methods=new ArrayList<String>();
        String name=ctx.leftHandSide().expressionName().getText();

       Symbol s= currentScope.resolve1(name);

        if(s==null){

            boolean b=true;
            for(String value:classvariables.keySet()){

                String name1=value;

                if(name.equals(name1)) {

                    for(int i=0;i<classvariables.get(name).size();i++){
                        if(currentScope.getScopeName().equals(classvariables.get(name1).get(i))){
                            b=false;break;
                        }
                    }
                    if(b==true)
                        methods=classvariables.get(name);
                    methods.add(currentScope.getScopeName());
                    classvariables.put(name,methods);

                         break;

                }

            }
        }
    }

    @Override public void exitAssignment(javaParser.AssignmentContext ctx) { }
    //--------------------------------------------------------------------------------------

    @Override public void enterMethodInvoc1(javaParser.MethodInvoc1Context ctx) {
        String name=ctx.methodName().getText();
        String scope=currentScope.getScopeName();
        boolean b=false;
        if(scope.equals("Block")){
            String methodname=currentScope.getEnclosingScope().getScopeName();
            for(String value:methodcalls.keySet()){
                String name2=value;
                if(methodname.equals(name2)){

                    for(int i=0;i<methodcalls.get(methodname).size();i++)
                    {
                        if(methodcalls.get(methodname).get(i).equals(name))
                            b=true;

                    }
                    if(!b){
                        methodcalls.get(name2).add(name);
                        b=false;
                        break;

                    }

                }
            }
        }
    }

    @Override public void exitMethodInvoc1(javaParser.MethodInvoc1Context ctx) { }

    //-----------------------------------------------------------------------------------------------
    public void enterMethodInvoc2(javaParser.MethodInvoc2Context ctx) {


        String objectname = ctx.typeName().getText();
        String classname=null;
        String name=ctx.Identifier().toString();
        String scope=currentScope.getScopeName();
        String methodname=null;
        if(scope.equals("Block")){
          methodname=currentScope.getEnclosingScope().getScopeName();

        }

    if(scope.equals("Block")) {
        Iterator<Object> it0 = objectinstances.iterator();
        while (it0.hasNext()) {
            Object name1 = it0.next();
            if (objectname.equals(name1.symbol.name)) {
                classname = name1.classname;

                // if (!(name1.currentscope.getScopeName().equals("Class"))) {
                //   relation = Invoc.RelationType.DEPENDENCY;
                //} else if(name1.currentscope.getScopeName().equals("Class")) {
                // relation = Invoc.RelationType.ASSOSIATION;
                //}
                break;

            }
        }


       // Invoc inv = new Invoc(ctx.Identifier().getText(), Invoc.InvocType.METHODINVOC, relation);
        //be in nokte deghat kon k momken ast dar packagehay mokhtalef classhay hamname dashte bashim, felan in ro dar nazar nagerefti
        Symbol s1 = null;
        boolean r = false;
        for (Symbol value1 : importlistofclass.values()) {
            s1 = value1;
            if (s1.name.equals(classname)) {
                /*Iterator<String> it = Inheritancelistofclass.iterator();
                while (it.hasNext()) {
                    String name2 = it.next();
                    if ((classname.equals(name2))) {
                        coupling = false;
                        break;

                    }
                }*/
                r = true;
                break;


            }
        }
        String keyname=null;
        if (r) {

            keyname  = s1.packagename + s1.name +"."+ name;}
        else if(!r){
            keyname=name;
        }
            String s = null;
        boolean b=false;
            for (String value : methodcalls.keySet()) {
                s = value;
                if (s.equals(methodname)) {
                    for(int i=0;i<methodcalls.get(methodname).size();i++)
                    {
                        if(methodcalls.get(methodname).get(i).equals(keyname))
                            b=true;

                    }
                    if(!b) {

                        methodcalls.get(methodname).add(keyname);
                        b = false;
                    }

                    break;
                }


            }


        }
    }





    @Override public void exitMethodInvoc2(javaParser.MethodInvoc2Context ctx) { }
    //--------------------------------------------------------------------

    @Override public void enterMethodinvocation_lfno_primary1(javaParser.Methodinvocation_lfno_primary1Context ctx) {
        String name=ctx.methodName().getText();
        String scope=currentScope.getScopeName();
        if(scope.equals("Block")){
            String methodname=currentScope.getEnclosingScope().getScopeName();
            boolean b=false;
            for(String value:methodcalls.keySet()){
                String name2=value;
                if(methodname.equals(name2)){



                    for(int i=0;i<methodcalls.get(methodname).size();i++)
                    {
                        if(methodcalls.get(methodname).get(i).equals(name))
                            b=true;

                    }
                    if(!b){
                        methodcalls.get(name2).add(name);
                        b=false;
                        break;

                    }
                }
            }
        }

    }

    @Override public void exitMethodinvocation_lfno_primary1(javaParser.Methodinvocation_lfno_primary1Context ctx) { }


    //----------------------------------------------------------------------

    @Override public void enterMethodinvocation_lfno_primary2(javaParser.Methodinvocation_lfno_primary2Context ctx) {
        String objectname = ctx.typeName().getText();
        String classname = null;
        String name = ctx.Identifier().toString();
        String scope = currentScope.getScopeName();
        String methodname = null;
        if (scope.equals("Block")) {
            methodname = currentScope.getEnclosingScope().getScopeName();

        }

        if (scope.equals("Block")) {
            Iterator<Object> it0 = objectinstances.iterator();
            while (it0.hasNext()) {
                Object name1 = it0.next();
                if (objectname.equals(name1.symbol.name)) {
                    classname = name1.classname;

                    // if (!(name1.currentscope.getScopeName().equals("Class"))) {
                    //   relation = Invoc.RelationType.DEPENDENCY;
                    //} else if(name1.currentscope.getScopeName().equals("Class")) {
                    // relation = Invoc.RelationType.ASSOSIATION;
                    //}
                    break;

                }
            }


            // Invoc inv = new Invoc(ctx.Identifier().getText(), Invoc.InvocType.METHODINVOC, relation);
            //be in nokte deghat kon k momken ast dar packagehay mokhtalef classhay hamname dashte bashim, felan in ro dar nazar nagerefti
            Symbol s1 = null;
            boolean r = false;
            for (Symbol value1 : importlistofclass.values()) {
                s1 = value1;
                if (s1.name.equals(classname)) {
                /*Iterator<String> it = Inheritancelistofclass.iterator();
                while (it.hasNext()) {
                    String name2 = it.next();
                    if ((classname.equals(name2))) {
                        coupling = false;
                        break;

                    }
                }*/
                    r = true;
                    break;


                }
            }
            String keyname=null;
            if (r) {

               keyname  = s1.packagename + s1.name +"."+ name;}
                else if(!r){
                    keyname=name;
                }

                String s = null;
            boolean b=false;
                for (String value : methodcalls.keySet()) {
                    s = value;
                    if (s.equals(methodname)) {

                        for(int i=0;i<methodcalls.get(methodname).size();i++)
                        {
                            if(methodcalls.get(methodname).get(i).equals(keyname))
                                b=true;

                        }
                        if(!b){
                        methodcalls.get(methodname).add(keyname);
                        b=false;
                        }

                        break;
                    }


                }


            }


        }


    @Override public void exitMethodinvocation_lfno_primary2(javaParser.Methodinvocation_lfno_primary2Context ctx) { }

    //--------------------------------------------------------------------------------------------
    @Override public void enterBlock(javaParser.BlockContext ctx) {
        currentScope=scopes.get(ctx);
    }

    @Override public void exitBlock(javaParser.BlockContext ctx) {
        currentScope=currentScope.getEnclosingScope();
    }
    //----------------------------------------------------------



}
