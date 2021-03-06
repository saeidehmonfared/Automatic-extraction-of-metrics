package ASTGenerator;
import ANTLRParser.*;
import Scopes.ClassScope;
import Symbols.Symbol;

import java.util.ArrayList;

/**
 * Created by saeideh on 1/3/17.
 */
public class StaticListphase extends javaBaseListener {
    public String packagename="";

    public ArrayList<Symbol.AccessModifier> accessmod=new ArrayList<Symbol.AccessModifier>();
    public ArrayList<Symbol.AccessModifier> interfacemodifier=new ArrayList<Symbol.AccessModifier>();



    @Override public void enterPackageDeclaration(javaParser.PackageDeclarationContext ctx) {

        for(int i=1;i<ctx.getChildCount()-1;i++){
            packagename=packagename+ctx.getChild(i).getText();
        }
        packagename=packagename+".";

        //System.out.println("mypackname:"+packagename);
    }

    @Override public void exitPackageDeclaration(javaParser.PackageDeclarationContext ctx) {

    }
    //----------------------------------------------------------------------------------------------


    @Override public void enterNormalInterfaceDeclaration1(javaParser.NormalInterfaceDeclaration1Context ctx) {
        interfacemodifier.clear();}

    @Override public void exitNormalInterfaceDeclaration1(javaParser.NormalInterfaceDeclaration1Context ctx) {
        String s1 = ctx.Identifier().getText().toString();


        boolean a=true;
        for(int i=0;i<interfacemodifier.size();i++)
        {
            Symbol.AccessModifier access=interfacemodifier.get(i);
            if ((access== Symbol.AccessModifier.tpublic)|| (access== Symbol.AccessModifier.tprivate)||(access== Symbol          .AccessModifier.tprotected)) a=false;

        }
        if(a){
            interfacemodifier.add(Symbol.AccessModifier.tpublic);}

        Symbol C = new Symbol(s1, interfacemodifier,Symbol.Type.tINTERFACE,packagename);
        // System.out.println("Accessmodofier of this interface is:"+C.accessmodifier);
        StaticList.insert(C);
        interfacemodifier.clear();
    }

    @Override public void enterNormalInterfaceDeclaration2(javaParser.NormalInterfaceDeclaration2Context ctx) {
        interfacemodifier.clear(); }

    @Override public void exitNormalInterfaceDeclaration2(javaParser.NormalInterfaceDeclaration2Context ctx) {
        String s1 = ctx.Identifier().getText().toString();


        boolean a=true;
        for(int i=0;i<interfacemodifier.size();i++)
        {
            Symbol.AccessModifier access=interfacemodifier.get(i);
            if ((access== Symbol.AccessModifier.tpublic)|| (access== Symbol.AccessModifier.tprivate)||(access== Symbol          .AccessModifier.tprotected)) a=false;

        }
        if(a){
            interfacemodifier.add(Symbol.AccessModifier.tpublic);}

        Symbol C = new Symbol(s1, interfacemodifier,Symbol.Type.tINTERFACE,packagename);
        // System.out.println("Accessmodofier of this interface is:"+C.accessmodifier);
        StaticList.insert(C);
        interfacemodifier.clear();
    }

    //------------------------------------------------------------------------------------------------------------

    @Override public void enterNormalClassDeclaration1(javaParser.NormalClassDeclaration1Context ctx) {  accessmod.clear();
    }

    @Override public void exitNormalClassDeclaration1(javaParser.NormalClassDeclaration1Context ctx) { String s = ctx.Identifier().getText().toString();
        boolean a=true;
        for(int i=0;i<accessmod.size();i++)
        {
            Symbol.AccessModifier access=accessmod.get(i);
            if ((access== Symbol.AccessModifier.tpublic)|| (access== Symbol.AccessModifier.tprivate)||(access== Symbol          .AccessModifier.tprotected)) a=false;

        }
        if(a){
            accessmod.add(Symbol.AccessModifier.tpublic);}

        Symbol C = new Symbol(s, accessmod,Symbol.Type.tCLASS,packagename);


        StaticList.insert(C);


        accessmod.clear();
    }
    //---------------------------------------------------------------------------

    @Override public void enterNormalClassdeclaration2(javaParser.NormalClassdeclaration2Context ctx) {  accessmod.clear();
    }

    @Override public void exitNormalClassdeclaration2(javaParser.NormalClassdeclaration2Context ctx) { String s = ctx.Identifier().getText().toString();
        boolean a=true;
        for(int i=0;i<accessmod.size();i++)
        {
            Symbol.AccessModifier access=accessmod.get(i);
            if ((access== Symbol.AccessModifier.tpublic)|| (access== Symbol.AccessModifier.tprivate)||(access== Symbol          .AccessModifier.tprotected)) a=false;

        }
        if(a){
            accessmod.add(Symbol.AccessModifier.tpublic);}

        Symbol C = new Symbol(s, accessmod,Symbol.Type.tCLASS,packagename);


        StaticList.insert(C);


        accessmod.clear();
    }






    ////--------------------------------------------------------------------------
    @Override public void enterClassModifier(javaParser.ClassModifierContext ctx) { }

    @Override public void exitClassModifier(javaParser.ClassModifierContext ctx) {
        int m=ctx.start.getType();
        Symbol.AccessModifier accesstype=CheckSymbols.getAccessmodifierType(m);
        this.accessmod.add(accesstype);
    }
    //-----------------------------------------------------------------------
    @Override public void enterInterfaceModifier(javaParser.InterfaceModifierContext ctx) { }

    @Override public void exitInterfaceModifier(javaParser.InterfaceModifierContext ctx) {
        int m =ctx.start.getType();
        Symbol.AccessModifier interfacemodifier=CheckSymbols.getAccessmodifierType(m);
        this.interfacemodifier.add(interfacemodifier);
    }
}
