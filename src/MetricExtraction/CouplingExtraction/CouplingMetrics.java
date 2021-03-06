package MetricExtraction.CouplingExtraction;
import ANTLRParser.*;
import ASTGenerator.Inheritancelist;
import ASTGenerator.StaticList;
import Scopes.ClassScope;
import Scopes.GlobalScope;
import Scopes.Scope;
import Symbols.MethodSymbol;
import Symbols.Symbol;
import java.lang.String;

import Symbols.VariableSymbol;
import com.sun.org.apache.xpath.internal.SourceTree;

import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeProperty;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by saeideh on 1/14/17.
 */
public class CouplingMetrics extends javaBaseListener {
    public static Map<String,Map<String,Map<String,ArrayList<Invoc>>>> returnvalue=new LinkedHashMap<>();
    public static Map<String,ArrayList<Object>> objectinstancevalue=new LinkedHashMap<>();
    public static Map<String,Map<String,Map<String,ArrayList<cohesionobject>>>>Cohesionlist=new LinkedHashMap<>();
    ArrayList<Symbol> Couplinglistofclass = new ArrayList<Symbol>();

    ParseTreeProperty<Scope> scopes;
    GlobalScope globals;
    Scope Currentscope;
    Map<Symbol, String> refrencesofclass = new LinkedHashMap<Symbol, String>();
    Map<String, Symbol> importlistofclass = new LinkedHashMap<String, Symbol>();
    // classname;
    String packagename="";
    public  Map<String, ArrayList<Invoc>> Couplinglist = new LinkedHashMap<String, ArrayList<Invoc>>();
    public Map<String, ArrayList<Invoc>> Couplinglisthelper=new LinkedHashMap<String,ArrayList<Invoc>>();
    public Map<String,String> assignmentinmethod=new LinkedHashMap<String,String>();

    public ArrayList<Object> objectinstances = new ArrayList<Object>();


    public ArrayList<Symbol> Inheritancelistofclass = new ArrayList<Symbol>();
    Scope currentScope;
    String assignmentclassname=null;
    String assignmentname=null;
    String leftofassignment;
    String classname;
    ParseTreeProperty<String>assigmnetrightside=new ParseTreeProperty<String>();
    public void setValue(ParseTree ctx, String value) {
        assigmnetrightside.put(ctx, value);
    }

    public String getValue(ParseTree ctx) {
        return assigmnetrightside.get(ctx);
    }

    public CouplingMetrics(GlobalScope globals, ParseTreeProperty<Scope> Scopes, Map<Symbol, String> refrences, Map<String, Symbol> importlist, ArrayList<Symbol> inheritancelistofclass, ArrayList<Object> objectinstances) {
        this.globals = globals;
        this.scopes = Scopes;
        this.refrencesofclass = refrences;
        this.importlistofclass = importlist;
        this.Inheritancelistofclass = inheritancelistofclass;
        this.objectinstances = objectinstances;

    }


    //-----------------------------------------------------------------------

    public void enterCompilationUnit(javaParser.CompilationUnitContext ctx) {
        Currentscope = globals;
        // System.out.println(this.objectinstances+"66666666666666666666");

    }

    //--------------------------------------------------------------------------


    public void exitCompilationUnit(javaParser.CompilationUnitContext ctx) {


        //Couplinglist.put(packagename+classname,Couplinglistofclass);


        Iterator<Symbol> it = Couplinglistofclass.iterator();
        while (it.hasNext()) {
            Symbol name1 = it.next();
            for (String value : Couplinglist.keySet()) {
                String name = value;

            }
            //Couplinglist.put(name1.packagename+name1.name,)

        }
        System.out.println("coupling list is:" );


        for (String value : Couplinglist.keySet()) {
            String name = value;


            ArrayList<Invoc> mylist = new ArrayList<Invoc>();
            mylist = Couplinglist.get(name);
            System.out.println(name);
            Iterator<Invoc> it1 = Couplinglist.get(name).iterator();
            while (it1.hasNext()) {
                Invoc name1 = it1.next();
                System.out.println(name1.name+"  "+ name1.invoctype+" "+name1.relationType.toString());


            }
        }


        if(classname!=null){
            for(String value1:Couplinglist.keySet()){
                String name1=value1;
                ArrayList<Invoc> mylist1=new ArrayList<>();
                mylist1=Couplinglist.get(name1);
                returnvalue.get(packagename).get(classname).put(name1,mylist1);
            }
        }



    }

    //-----------------------------------------------------------------------------
    //public void enterNormalClassDeclaration(javaParser.NormalClassDeclarationContext ctx) {
    //classname = ctx.getText();

    // System.out.println("Name of class is:" + ctx.Identifier());
    // Couplinglist.put(packagename + classname, null);

    //  }
    //----------------------------------------------------------------------------

    @Override
    public void enterPackageDeclaration(javaParser.PackageDeclarationContext ctx) {

        for (int i = 1; i < ctx.getChildCount() - 1; i++) {
            packagename = packagename + ctx.getChild(i).getText();
        }
        packagename = packagename + ".";

        // System.out.println("mypackname:"+packagename);
        boolean a=true;
        for(String value: returnvalue.keySet())
        {
            if(value.equals(packagename)){
                a=false;
            }
        }
        if(a){
            returnvalue.put(packagename,new LinkedHashMap<>());
        }
    }

    @Override
    public void exitPackageDeclaration(javaParser.PackageDeclarationContext ctx) {


    }
    public String classnameasli="";

    @Override public void enterNormalClassDeclaration1(javaParser.NormalClassDeclaration1Context ctx) {
        currentScope=scopes.get(ctx);
        classname=ctx.Identifier().getText();
        classnameasli=ctx.Identifier().getText();
        objectinstancevalue.put(classname,new ArrayList<Object>());
        objectinstancevalue.get(classname).addAll(objectinstances);
        returnvalue.get(packagename).put(classname,new LinkedHashMap<>());
        Cohesionlist.put(classname,new LinkedHashMap<>());
    }

    @Override public void exitNormalClassDeclaration1(javaParser.NormalClassDeclaration1Context ctx) {
        classnameasli="";
    }


    @Override public void enterNormalClassdeclaration2(javaParser.NormalClassdeclaration2Context ctx) {currentScope=scopes.get(ctx);
        classname=ctx.Identifier().getText();
        classnameasli=ctx.Identifier().getText();
        returnvalue.get(packagename).put(classname,new LinkedHashMap<>());
        Cohesionlist.put(classname,new LinkedHashMap<>());

    }

    @Override public void exitNormalClassdeclaration2(javaParser.NormalClassdeclaration2Context ctx) {
        classnameasli="";
    }

    //----------------------------------------------------------------------------------------
    String methodname;
    @Override
    public void enterMethodDeclaration(javaParser.MethodDeclarationContext ctx) {
        currentScope = scopes.get(ctx);
        assignmentinmethod.clear();

    }

    @Override
    public void exitMethodDeclaration(javaParser.MethodDeclarationContext ctx) {

        // System.out.println("coupling list helper is:"+Couplinglisthelper);
        currentScope = currentScope.getEnclosingScope();


        for(String value:Couplinglisthelper.keySet()){
            for(int i=0;i<Couplinglisthelper.get(value).size();i++){

                Invoc Invocname=Couplinglisthelper.get(value).get(i);
                for(String value1: assignmentinmethod.keySet())
                {
                    if(Invocname.objectname.equals(assignmentinmethod.get(value1))){file:///home/saeideh/Downloads/0article_v3.pdf
                        if(Couplinglisthelper.get(value).get(i).relationType.equals(Invoc.RelationType.DEPENDENCY)){
                            Couplinglist.get(value).get(i).relationType=Invoc.RelationType.ASSOSIATION;
                            Couplinglisthelper.get(value).get(i).relationType=Invoc.RelationType.ASSOSIATION;


                        }

                    }
                }
            }


        }

        // Couplinglist.putAll(Couplinglisthelper);

        // Couplinglisthelper.clear();
        assignmentinmethod.clear();

        methodnameasli="";



    }
    //----------------------------------------------------------------------------------
    String methodnameasli;
    @Override public void enterMethodDeclarator(javaParser.MethodDeclaratorContext ctx) {
        methodname=ctx.Identifier().getText();
        methodnameasli=ctx.Identifier().getText();
        if((!classnameasli.equals("")) && (!methodnameasli.equals(""))) {
            Cohesionlist.get(classnameasli).put(methodnameasli, new LinkedHashMap<>());
            Cohesionlist.get(classnameasli).get(methodnameasli).put("innerattributes", new ArrayList<cohesionobject>());
            Cohesionlist.get(classnameasli).get(methodnameasli).put("outerattributes", new ArrayList<cohesionobject>());
            Cohesionlist.get(classnameasli).get(methodnameasli).put("innercalls", new ArrayList<cohesionobject>());
            Cohesionlist.get(classnameasli).get(methodnameasli).put("outercalls", new ArrayList<cohesionobject>());
        }
    }

    @Override public void exitMethodDeclarator(javaParser.MethodDeclaratorContext ctx) {





        methodname="";




    }
    @Override public void enterConstructorDeclaration(javaParser.ConstructorDeclarationContext ctx) {
        methodnameasli=ctx.constructorDeclarator().simpleTypeName().Identifier().toString();

        Cohesionlist.get(classnameasli).put(methodnameasli,new LinkedHashMap<>());
        Cohesionlist.get(classnameasli).get(methodnameasli).put("innerattributes",new ArrayList<cohesionobject>());
        Cohesionlist.get(classnameasli).get(methodnameasli).put("outerattributes",new ArrayList<cohesionobject>());
        Cohesionlist.get(classnameasli).get(methodnameasli).put("innercalls",new ArrayList<cohesionobject>());
        Cohesionlist.get(classnameasli).get(methodnameasli).put("outercalls",new ArrayList<cohesionobject>());
    }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation does nothing.</p>
     */
    @Override public void exitConstructorDeclaration(javaParser.ConstructorDeclarationContext ctx) {
        methodnameasli="";
    }


    //----------------------------------------------------------------------------------
    @Override public void enterExpertionName1(javaParser.ExpertionName1Context ctx) { }

    @Override public void exitExpertionName1(javaParser.ExpertionName1Context ctx) {
        String name=ctx.Identifier().getText();
        setValue(ctx,name);
        cohesionobject ob=new cohesionobject(name);
        if((classnameasli.equals("")) && (!methodnameasli.equals(""))){
        Cohesionlist.get(classnameasli).get(methodnameasli).get("innerattributes").add(ob);}
        //TODO:deghat kon inja samte raste yek ebarat ke mitone parametere khode method bashe ro ham  b onvane attribute dakheli dar nazar migire,hatman ye fekri barash bokon.
    }

    //-----------------------------------------------------------------------------------


    public void enterExpertionName2(javaParser.ExpertionName2Context ctx) {

        String name = ctx.ambiguousName().Identifier().toString();
        setValue(ctx,name);

        String classname=null;

        boolean coupling = true;

        Invoc.RelationType relation = Invoc.RelationType.INVALID;

        ArrayList<Object>kandidlist=new ArrayList<Object>();

        Iterator<Object> it0 = objectinstances.iterator();
        while (it0.hasNext()) {
            Object name1 = it0.next();
            if (name.equals(name1.symbol.name)) {
                kandidlist.add(name1);
            }
        }

        Object primaryname=null;

        Iterator<Object> it01 = kandidlist.iterator();
        while (it01.hasNext()) {
            Object name2 = it01.next();
            classname = name2.classname;

            if ((name2.symbol.name.equals(name))&&(name2.currentscope.getScopeName().equals(currentScope.getScopeName()))) {

                primaryname = name2;
                break;
            }
        }

        if(primaryname==null){

            Iterator<Object> it02 = kandidlist.iterator();
            while (it02.hasNext()) {
                Object name2 = it02.next();
                classname = name2.classname;

                if ((name2.symbol.name.equals(name))) {

                    primaryname = name2;
                    break;}



            }
        }

        //in tike baray por kardan list cohesion hast va rabti b coupling nadard
        cohesionobject ob=new cohesionobject(name,classname);
        if(!(classnameasli.equals("")) && (!methodnameasli.equals(""))){
        Cohesionlist.get(classnameasli).get(methodnameasli).get("outerattributes").add(ob);}

        if(primaryname!=null) {
            if (primaryname.currentscope.getScopeName().equals("Class") ) {
                relation = Invoc.RelationType.ASSOSIATION;
            } else if ((primaryname.currentscope.getClass().getName().equals("Symbols.MethodSymbol"))) {
                if (((MethodSymbol) primaryname.currentscope).returntype.equals(VariableSymbol.TYPE.TCONSTRUCTOR))
                    relation = Invoc.RelationType.ASSOSIATION;
                else
                    relation = Invoc.RelationType.DEPENDENCY;
            }
        }





        Invoc inv = new Invoc(name, ctx.Identifier().getText(), Invoc.InvocType.ATTRIBUTEINVOC, relation,currentScope,"");
        //be in nokte deghat kon k momken ast dar packagehay mokhtalef classhay hamname dashte bashim, felan in ro dar nazar nagerefti
        Symbol s1=null;
        boolean r=false;
        for (Symbol value1 : importlistofclass.values()) {
            s1 = value1;

            if (s1.name.equals(classname)) {
                Iterator<Symbol> it = Inheritancelistofclass.iterator();
                while (it.hasNext()) {
                    Symbol name2 = it.next();
                    if ((classname.equals(name2.name))) {
                        coupling = false;
                        break;

                    }
                }
                r=true;
                break;





            }
        }
        if (coupling && r) {

            String keyname = s1.packagename + s1.name;
            boolean f = false;
            String s = null;
            for (String value : Couplinglist.keySet()) {
                s = value;
                if (s.equals(keyname)) {
                    f = true;
                    break;
                }
                // Couplinglist.get(s).add()

            }

            if (f) {
                boolean h = true;

                //@@ Iterator<Invoc> it3 = Couplinglist.get(keyname).iterator();
                //@@ while (it3.hasNext()) {
                //@@  Invoc name2 = it3.next();
                //@@  if ((name2.name.equals(inv.name))&&(name2.currentScope.getScopeName().equals(inv.currentScope.getScopeName()))) {
                //@@ h = false;
                //@@   break;

                //@@  }
                //@@ }

                if (h) {
                    Couplinglist.get(keyname).add(inv);
                    if(Couplinglisthelper.size()==0) {
                        ArrayList<Invoc> invlist0 = new ArrayList<Invoc>();
                        Couplinglisthelper.put(keyname, invlist0);
                        Couplinglisthelper.get(keyname).add(inv);
                    }
                    else if(!(Couplinglisthelper.size()==0)) {
                        boolean h1 = false;
                        Iterator<String> it4 = Couplinglisthelper.keySet().iterator();
                        while (it4.hasNext()) {
                            String name2 = it4.next();
                            if (name2.equals(keyname)) {
                                h1 = true;
                            }
                            if (h1 == false) {
                                ArrayList<Invoc> invlist0 = new ArrayList<Invoc>();
                                Couplinglisthelper.put(keyname, invlist0);
                                Couplinglisthelper.get(keyname).add(inv);

                            } else if (h1 == true) {
                                Couplinglisthelper.get(keyname).add(inv);

                            }

                        }
                    }

                }

            }
            else if(!(f)){
                ArrayList<Invoc>invoclist=new ArrayList<Invoc>();
                ArrayList<Invoc>invoclist1=new ArrayList<>();

                Couplinglist.put(keyname,invoclist);
                Couplinglist.get(keyname).add(inv);

                Couplinglisthelper.put(keyname,invoclist1);
                Couplinglisthelper.get(keyname).add(inv);

            }

        }

    }
    //---------------------------------------------------------------------------
    @Override public void enterClassInstanceCreationExpression_lfno_primary(javaParser.ClassInstanceCreationExpression_lfno_primaryContext ctx) {
        assignmentname=ctx.Identifier(0).getText();
        assignmentclassname=ctx.Identifier().get(0).getText();
        //System.out.println(assignmentclassname+"maaaaaaaaaaaaaaan");

    }
    //--------------------------------------------------------

    @Override public void exitClassInstanceCreationExpression_lfno_primary(javaParser.ClassInstanceCreationExpression_lfno_primaryContext ctx) { }

    @Override
    public void exitExpertionName2(javaParser.ExpertionName2Context ctx) {
    }
    //------------------------------------------------------------------------------
    @Override public void enterAssignment1(javaParser.Assignment1Context ctx) {



        //assignmentinmethod.put(leftsidename,rightsidename);


    }

    @Override public void exitAssignment1(javaParser.Assignment1Context ctx) {


        String leftsidename=ctx.expressionName().getText();
        ArrayList<Object>candid=new ArrayList<Object>();
        boolean classscope=false;
        boolean methodscope=false;

        Iterator<Object> it=objectinstances.iterator();
        while (it.hasNext()){
            Object s=it.next();
            if(s.symbol.name.equals(leftsidename)){
                candid.add(s);
            }

        }

        String objectname;
        Iterator<Object> it0=candid.iterator();
        while (it0.hasNext()){
            Object s1=it0.next();
            if(s1.currentscope.getScopeName().equals(methodname)){
                methodscope=true;
                break;

            }
            else if(s1.currentscope.getScopeName().equals("Class")){
                classscope=true;
                break;


            }

        }

        if((methodscope==false) && (classscope=true)){

            String rightsidename=getValue(ctx.getChild(2));
            assignmentinmethod.put(leftsidename,rightsidename);
            methodscope=false;
            classscope=false;
            candid.clear();

        }




        assignmentname=null;
        leftofassignment=ctx.expressionName().getText();



    }
    //---------------------------------------------------------------------------
    @Override public void enterExpression(javaParser.ExpressionContext ctx) { }

    @Override public void exitExpression(javaParser.ExpressionContext ctx) {
        String name=getValue(ctx.getChild(0));
        setValue(ctx,name);
    }
    //--------------------------------------------------------------------------------
    @Override public void enterAssignmentExpression(javaParser.AssignmentExpressionContext ctx) { }

    @Override public void exitAssignmentExpression(javaParser.AssignmentExpressionContext ctx) {
        String name=getValue(ctx.getChild(0));
        setValue(ctx,name);
    }
    //--------------------------------------------------------------------------------
    @Override public void enterConditionalExpression(javaParser.ConditionalExpressionContext ctx) { }

    @Override public void exitConditionalExpression(javaParser.ConditionalExpressionContext ctx) {
        String name=getValue(ctx.getChild(0));
        setValue(ctx,name);
    }
    //---------------------------------------------------------------------------------
    @Override public void enterConditionalOrExpression(javaParser.ConditionalOrExpressionContext ctx) { }

    @Override public void exitConditionalOrExpression(javaParser.ConditionalOrExpressionContext ctx) {
        String name=getValue(ctx.getChild(0));
        setValue(ctx,name);
    }
    //----------------------------------------------------------------------------------
    @Override public void enterConditionalAndExpression(javaParser.ConditionalAndExpressionContext ctx) { }

    @Override public void exitConditionalAndExpression(javaParser.ConditionalAndExpressionContext ctx) {
        String name=getValue(ctx.getChild(0));
        setValue(ctx,name);
    }
    //-----------------------------------------------------------------------------------------------
    @Override public void enterInclusiveOrExpression(javaParser.InclusiveOrExpressionContext ctx) { }

    @Override public void exitInclusiveOrExpression(javaParser.InclusiveOrExpressionContext ctx) {
        String name=getValue(ctx.getChild(0));
        setValue(ctx,name);
    }
    //-----------------------------------------------------------------------------------------------

    @Override public void enterExclusiveOrExpression(javaParser.ExclusiveOrExpressionContext ctx) { }

    @Override public void exitExclusiveOrExpression(javaParser.ExclusiveOrExpressionContext ctx) {
        String name=getValue(ctx.getChild(0));
        setValue(ctx,name);
    }
    //---------------------------------------------------------------------------------------------
    @Override public void enterAndExpression(javaParser.AndExpressionContext ctx) { }

    @Override public void exitAndExpression(javaParser.AndExpressionContext ctx) {

        String name=getValue(ctx.getChild(0));
        setValue(ctx,name);
    }
    //---------------------------------------------------------------------------------------------
    @Override public void enterEqualityExpression(javaParser.EqualityExpressionContext ctx) { }

    @Override public void exitEqualityExpression(javaParser.EqualityExpressionContext ctx) {
        String name=getValue(ctx.getChild(0));
        setValue(ctx,name);
    }
    //---------------------------------------------------------------------------------------------
    @Override public void enterRelationalExpression(javaParser.RelationalExpressionContext ctx) { }

    @Override public void exitRelationalExpression(javaParser.RelationalExpressionContext ctx) {
        String name=getValue(ctx.getChild(0));
        setValue(ctx,name);
    }
    //---------------------------------------------------------------------------------------------
    @Override public void enterShiftExpression(javaParser.ShiftExpressionContext ctx) { }

    @Override public void exitShiftExpression(javaParser.ShiftExpressionContext ctx) {
        String name=getValue(ctx.getChild(0));
        setValue(ctx,name);
    }

    //----------------------------------------------------------------------------------------------
    @Override public void enterAdditiveExpression(javaParser.AdditiveExpressionContext ctx) { }

    @Override public void exitAdditiveExpression(javaParser.AdditiveExpressionContext ctx) {
        String name=getValue(ctx.getChild(0));
        setValue(ctx,name);
    }
    //-----------------------------------------------------------------------------------------------
    @Override public void enterMultiplicativeExpression(javaParser.MultiplicativeExpressionContext ctx) { }

    @Override public void exitMultiplicativeExpression(javaParser.MultiplicativeExpressionContext ctx) {
        String name=getValue(ctx.getChild(0));
        setValue(ctx,name);
    }
    //------------------------------------------------------------------------------------------------

    @Override public void enterUnaryExpression(javaParser.UnaryExpressionContext ctx) { }

    @Override public void exitUnaryExpression(javaParser.UnaryExpressionContext ctx) {
        String name=getValue(ctx.getChild(0));
        setValue(ctx,name);
    }
    //---------------------------------------------------------------------------------------------
    @Override public void enterPreIncrementExpression(javaParser.PreIncrementExpressionContext ctx) { }

    @Override public void exitPreIncrementExpression(javaParser.PreIncrementExpressionContext ctx) {

        String name=getValue(ctx.getChild(0));
        setValue(ctx,name);

    }


    //----------------------------------------------------------------------------------------------

    @Override public void enterPreDecrementExpression(javaParser.PreDecrementExpressionContext ctx) { }

    @Override public void exitPreDecrementExpression(javaParser.PreDecrementExpressionContext ctx) {
        String name=getValue(ctx.getChild(0));
        setValue(ctx,name);

    }
    //---------------------------------------------------------------------------------------------
    @Override public void enterUnaryExpressionNotPlusMinus(javaParser.UnaryExpressionNotPlusMinusContext ctx) { }

    @Override public void exitUnaryExpressionNotPlusMinus(javaParser.UnaryExpressionNotPlusMinusContext ctx) {
        String name=getValue(ctx.getChild(0));
        setValue(ctx,name);
    }
    //----------------------------------------------------------------------------------------------
    @Override public void enterPostfixExpression(javaParser.PostfixExpressionContext ctx) { }

    @Override public void exitPostfixExpression(javaParser.PostfixExpressionContext ctx) {
        String name=getValue(ctx.getChild(0));
        setValue(ctx,name);
    }
    //----------------------------------------------------------------------------------------------
    @Override public void enterCastExpression(javaParser.CastExpressionContext ctx) { }

    @Override public void exitCastExpression(javaParser.CastExpressionContext ctx) {

        String name=getValue(ctx.getChild(0));
        setValue(ctx,name);
    }

    //----------------------------------------------------------------------------------------------

    @Override public void enterPostIncrementExpression(javaParser.PostIncrementExpressionContext ctx) { }

    @Override public void exitPostIncrementExpression(javaParser.PostIncrementExpressionContext ctx) {
        String name=getValue(ctx.getChild(0));
        setValue(ctx,name);
    }
    //----------------------------------------------------------------------------------------------


    @Override public void enterPostIncrementExpression_lf_postfixExpression(javaParser.PostIncrementExpression_lf_postfixExpressionContext ctx) { }

    @Override public void exitPostIncrementExpression_lf_postfixExpression(javaParser.PostIncrementExpression_lf_postfixExpressionContext ctx) {

        String name=getValue(ctx.getChild(0));
        setValue(ctx,name);

    }

    //----------------------------------------------------------------------------------------------

    @Override public void enterPostDecrementExpression(javaParser.PostDecrementExpressionContext ctx) { }

    @Override public void exitPostDecrementExpression(javaParser.PostDecrementExpressionContext ctx) {

        String name=ctx.getText();
        setValue(ctx,name);
    }
    //----------------------------------------------------------------------------------------------

    @Override public void enterPostDecrementExpression_lf_postfixExpression(javaParser.PostDecrementExpression_lf_postfixExpressionContext ctx) { }

    @Override public void exitPostDecrementExpression_lf_postfixExpression(javaParser.PostDecrementExpression_lf_postfixExpressionContext ctx) {

        String name=ctx.getText();
        setValue(ctx,name);
    }
    //----------------------------------------------------------------------------------------------

    @Override
    public void enterMethodInvoc2(javaParser.MethodInvoc2Context ctx) {

        String objectname = ctx.typeName().getText();
        String classname=null;

        boolean coupling = true;

        Invoc.RelationType relation = Invoc.RelationType.INVALID;
        ArrayList<Object>kandidlist=new ArrayList<Object>();

        Iterator<Object> it0 = objectinstances.iterator();
        while (it0.hasNext()) {
            Object name1 = it0.next();
            if (objectname.equals(name1.symbol.name)) {
                kandidlist.add(name1);
            }
        }

        Object primaryname=null;

        Iterator<Object> it01 = kandidlist.iterator();
        while (it01.hasNext()) {
            Object name2 = it01.next();
            classname = name2.classname;

            if ((name2.symbol.name.equals(objectname))&&(name2.currentscope.getScopeName().equals(currentScope.getScopeName()))) {

                primaryname = name2;
                break;
            }
            //else if(objectname.equals(name2.symbol.name)){
             //   primaryname=name2;
             //   break;
           // }
        }


        if(primaryname==null){

            Iterator<Object> it02 = kandidlist.iterator();
            while (it02.hasNext()) {
                Object name2 = it02.next();
                classname = name2.classname;

                if ((name2.symbol.name.equals(objectname))) {

                    primaryname = name2;
                    break;}



            }
        }

        if(primaryname==null){
            boolean f=false;
            Symbol s0=null;
            for (Symbol value1 : importlistofclass.values()) {
                s0 = value1;
                if (s0.name.equals(objectname)) {
                    for(int i=0;i<s0.accessmodifier.size();i++){
                        if(s0.accessmodifier.get(i).equals(Symbol.AccessModifier.TSTATIC)) {
                            f = true;
                            break;
                        }

                    }
                    relation=Invoc.RelationType.STATICMETHOD;
                    f=false;
                    break;
                }
            }

        }
        //add to cohesion list
        cohesionobject ob=new cohesionobject(objectname,classname);
        if((!classnameasli.equals("")) && (!methodnameasli.equals(""))){
        Cohesionlist.get(classnameasli).get(methodnameasli).get("outercalls").add(ob);}


        if(primaryname!=null) {
            if (primaryname.currentscope.getScopeName().equals("Class")) {
                relation = Invoc.RelationType.ASSOSIATION;
            } else if ((primaryname.currentscope.getClass().getName().equals("Symbols.MethodSymbol"))) {
                if (((MethodSymbol) primaryname.currentscope).returntype.equals(VariableSymbol.TYPE.TCONSTRUCTOR))
                    relation = Invoc.RelationType.ASSOSIATION;
                else
                    relation = Invoc.RelationType.DEPENDENCY;
            }
        }







        Invoc inv = new Invoc(objectname,ctx.Identifier().getText(), Invoc.InvocType.METHODINVOC, relation,currentScope,"");
        //be in nokte deghat kon k momken ast dar packagehay mokhtalef classhay hamname dashte bashim, felan in ro dar nazar nagerefti
        Symbol s1=null;
        boolean r=false;
        for (Symbol value1 : importlistofclass.values()) {
            s1 = value1;
            if (s1.name.equals(classname)) {
                Iterator<Symbol> it = Inheritancelistofclass.iterator();
                while (it.hasNext()) {
                    Symbol name2 = it.next();
                    if ((classname.equals(name2.name))) {
                        coupling = false;
                        break;

                    }
                }
                r=true;
                break;





            }
        }
        if (coupling && r) {

            String keyname = s1.packagename + s1.name;
            boolean f = false;
            String s = null;
            for (String value : Couplinglist.keySet()) {
                s = value;
                if (s.equals(keyname)) {
                    f = true;
                    break;
                }
                // Couplinglist.get(s).add()

            }

            if (f) {
                boolean h = true;

                //@@ Iterator<Invoc> it3 = Couplinglist.get(keyname).iterator();
                // @@while (it3.hasNext()) {
                // @@ Invoc name2 = it3.next();
                // @@ if ((name2.name.equals(inv.name))&&(name2.currentScope.getScopeName().equals(inv.currentScope.getScopeName())))
                // @@ {
                //@@   h = false;
                // @@   break;

                // @@ }
                // @@}

                if (h) {

                    Couplinglist.get(keyname).add(inv);





                    if(Couplinglisthelper.size()==0) {


                        ArrayList<Invoc> invlist0 = new ArrayList<Invoc>();
                        Couplinglisthelper.put(keyname, invlist0);
                        Couplinglisthelper.get(keyname).add(inv);





                    }
                    else if(!(Couplinglisthelper.size()==0)){
                        boolean h1=false;
                        Iterator<String> it4 = Couplinglisthelper.keySet().iterator();
                        while (it4.hasNext()) {
                            String name2 = it4.next();
                            if(name2.equals(keyname)){
                                h1=true;
                            }
                            if(h1==false){
                                ArrayList<Invoc> invlist0 = new ArrayList<Invoc>();
                                Couplinglisthelper.put(keyname, invlist0);
                                Couplinglisthelper.get(keyname).add(inv);

                            }
                            else if(h1==true){
                                Couplinglisthelper.get(keyname).add(inv);

                            }

                        }


                    }

                }

            }
            else if(!(f)){
                ArrayList<Invoc>invoclist=new ArrayList<Invoc>();
                ArrayList<Invoc>invoclist1=new ArrayList<Invoc>();

                Couplinglist.put(keyname,invoclist);
                Couplinglist.get(keyname).add(inv);

                Couplinglisthelper.put(keyname,invoclist1);
                Couplinglisthelper.get(keyname).add(inv);





            }

        }
    }
    //--------------------------------------------------------------------
    @Override public void enterMethodinvocation_lfno_primary2(javaParser.Methodinvocation_lfno_primary2Context ctx) {

        String objectname = ctx.typeName().getText();
        String classname=null;

        boolean coupling = true;

        Invoc.RelationType relation = Invoc.RelationType.INVALID;

        ArrayList<Object>kandidlist=new ArrayList<Object>();

        Iterator<Object> it0 = objectinstances.iterator();
        while (it0.hasNext()) {
            Object name1 = it0.next();
            if (objectname.equals(name1.symbol.name)) {
                kandidlist.add(name1);
            }
        }

        Object primaryname=null;


        Iterator<Object> it01 = kandidlist.iterator();
        while (it01.hasNext()) {
            Object name2 = it01.next();
            classname = name2.classname;

            if ((name2.symbol.name.equals(objectname))&&(name2.currentscope.getScopeName().equals(currentScope.getScopeName()))) {

                primaryname = name2;
                break;
            }
            else if(objectname.equals(name2.symbol.name)){
                primaryname=name2;
                break;
            }
        }



        if(primaryname==null){

            Iterator<Object> it02 = kandidlist.iterator();
            while (it02.hasNext()) {
                Object name2 = it02.next();
                classname = name2.classname;

                if ((name2.symbol.name.equals(objectname))) {

                    primaryname = name2;
                    break;}



            }
        }
        if(primaryname==null){
            boolean f=false;
            Symbol s0=null;
            for (Symbol value1 : importlistofclass.values()) {
                s0 = value1;
                if (s0.name.equals(objectname)) {
                    for(int i=0;i<s0.accessmodifier.size();i++){
                        if(s0.accessmodifier.get(i).equals(Symbol.AccessModifier.TSTATIC)) {
                            f = true;
                            break;
                        }

                    }
                    relation=Invoc.RelationType.STATICMETHOD;
                    f=false;
                    break;
                }
            }

        }

        //add to cohesion list
        cohesionobject ob=new cohesionobject(objectname,classname);
        Cohesionlist.get(classnameasli).get(methodnameasli).get("outercalls").add(ob);
        if(primaryname!=null) {
            if (primaryname.currentscope.getScopeName().equals("Class")) {
                relation = Invoc.RelationType.ASSOSIATION;
            } else if ((primaryname.currentscope.getClass().getName().equals("Symbols.MethodSymbol"))) {
                if (((MethodSymbol) primaryname.currentscope).returntype.equals(VariableSymbol.TYPE.TCONSTRUCTOR))
                    relation = Invoc.RelationType.ASSOSIATION;
                else
                    relation = Invoc.RelationType.DEPENDENCY;
            }
        }




        Invoc inv = new Invoc(objectname,ctx.Identifier().getText(), Invoc.InvocType.METHODINVOC, relation,currentScope,"");
        //be in nokte deghat kon k momken ast dar packagehay mokhtalef classhay hamname dashte bashim, felan in ro dar nazar nagerefti
        Symbol s1=null;
        for (Symbol value1 : importlistofclass.values()) {
            s1 = value1;
            if (s1.name.equals(classname)) {
                Iterator<Symbol> it = Inheritancelistofclass.iterator();
                while (it.hasNext()) {
                    Symbol name2 = it.next();
                    if ((classname.equals(name2.name))) {
                        coupling = false;
                        break;

                    }


                }
                break;
            }
        }
        if (coupling) {

            String keyname = s1.packagename + s1.name;
            boolean f = false;
            String s = null;
            for (String value : Couplinglist.keySet()) {
                s = value;
                if (s.equals(keyname)) {
                    f = true;
                    break;
                }
                // Couplinglist.get(s).add()

            }

            if (f) {
                boolean h = true;

                // @@Iterator<Invoc> it3 = Couplinglist.get(keyname).iterator();
                //@@ while (it3.hasNext()) {
                // @@   Invoc name2 = it3.next();
                // @@  if ((name2.name.equals(inv.name))&&(name2.currentScope.getScopeName().equals(inv.currentScope.getScopeName())))
                //@@ {
                //@@ h = false;
                //@@  break;

                //@@ }
                //@@ }

                if (h) {
                    Couplinglist.get(keyname).add(inv);
                    if(Couplinglisthelper.size()==0) {
                        ArrayList<Invoc> invlist0 = new ArrayList<Invoc>();
                        Couplinglisthelper.put(keyname, invlist0);
                        Couplinglisthelper.get(keyname).add(inv);
                    }
                    else if(!(Couplinglisthelper.size()==0)) {
                        boolean h1 = false;
                        Iterator<String> it4 = Couplinglisthelper.keySet().iterator();
                        while (it4.hasNext()) {
                            String name2 = it4.next();
                            if (name2.equals(keyname)) {
                                h1 = true;
                            }
                            if (h1 == false) {
                                ArrayList<Invoc> invlist0 = new ArrayList<Invoc>();
                                Couplinglisthelper.put(keyname, invlist0);
                                Couplinglisthelper.get(keyname).add(inv);

                            } else if (h1 == true) {
                                Couplinglisthelper.get(keyname).add(inv);

                            }

                        }
                    }

                }

            }
            else if(!(f)){
                ArrayList<Invoc>invoclist=new ArrayList<Invoc>();
                ArrayList<Invoc>invoclist1=new ArrayList<Invoc>();

                Couplinglist.put(keyname,invoclist);
                Couplinglist.get(keyname).add(inv);

                Couplinglisthelper.put(keyname,invoclist1);
                Couplinglisthelper.get(keyname).add(inv);

            }

        }

    }
    @Override public void exitMethodinvocation_lfno_primary2(javaParser.Methodinvocation_lfno_primary2Context ctx) { }



    @Override public void exitMethodInvoc2(javaParser.MethodInvoc2Context ctx) { }
    //---------------------------------------------------------------------------------
    @Override public void enterMethodInvoc1(javaParser.MethodInvoc1Context ctx) {
        String name=ctx.methodName().getText();
        cohesionobject ob=new cohesionobject(name);
        Cohesionlist.get(classnameasli).get(methodnameasli).get("innercalls").add(ob);

    }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation does nothing.</p>
     */
    @Override public void exitMethodInvoc1(javaParser.MethodInvoc1Context ctx) { }
    //-----------------------------------------------------------------------------------

    @Override public void enterMethodinvocation_lfno_primary1(javaParser.Methodinvocation_lfno_primary1Context ctx) {

        String name=ctx.methodName().getText();
        cohesionobject ob=new cohesionobject(name);
        Cohesionlist.get(classnameasli).get(methodnameasli).get("innercalls").add(ob);

    }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation does nothing.</p>
     */
    @Override public void exitMethodinvocation_lfno_primary1(javaParser.Methodinvocation_lfno_primary1Context ctx) { }

    //---------------------------------------------------------------------




}


