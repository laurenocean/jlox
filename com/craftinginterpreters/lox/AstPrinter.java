package com.craftinginterpreters.lox;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;


class AstPrinter implements Expr.Visitor<String>, Stmt.Visitor<String> {

    public static void main(String[] args) throws IOException {
        String path = "test.lox";
        byte[] bytes = Files.readAllBytes(Paths.get(path));
        String source = new String(bytes, Charset.defaultCharset());

        List<Stmt> statements = Lox.getStatements(source);

        System.out.println("(ns user)");
        System.out.println("");
        System.out.println(new AstPrinter().print(statements));
    }

    String print (List<Stmt> statements) {
        StringBuilder str = new StringBuilder();

        try {
            int idx = 0;
            for (Stmt statement: statements) {
                idx++;
                String s = statement.accept(this);
                str.append(s);
                if (idx != statements.size()) {
                    str.append("\n");
                }
                
            }

        } catch (RuntimeError error) {
            Lox.runtimeError(error);
        }

        return str.toString();
    }

  @Override
  public String visitAssignExpr(Expr.Assign expr) {
    return null;
  }

  @Override
  public String visitCallExpr(Expr.Call expr) {
    StringBuilder str = new StringBuilder();
    
    str.append("(");

    String callee = expr.callee.accept(this);
    str.append(callee + " ");

    try {
        int idx = 0;
        for (Expr argument : expr.arguments) {
            ++idx; 
            String arg = argument.accept(this);
            str.append(arg);
            if (idx != expr.arguments.size()) {
                str.append(" ");
            }
        }
    } catch (RuntimeError error) {
        Lox.runtimeError(error);
    }

    str.append(")");

    return str.toString();

  }

  @Override
  public String visitGetExpr(Expr.Get expr) {
    return null;
  }

  @Override
  public String visitLogicalExpr(Expr.Logical expr) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'visitLogicalExpr'");
  }

  @Override
  public String visitSetExpr(Expr.Set expr) {
   return null;
}
  
  @Override
  public String visitSuperExpr(Expr.Super expr) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'visitSuperExpr'");
  }

  @Override
  public String visitThisExpr(Expr.This expr) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'visitThisExpr'");
  }

  @Override
  public String visitVariableExpr(Expr.Variable expr) {
    return expr.name.lexeme;
  }

  @Override
  public String visitBlockStmt(Stmt.Block stmt) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'visitBlockStmt'");
  }

  @Override
  public String visitClassStmt(Stmt.Class stmt) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'visitClassStmt'");
  }

  @Override
  public String visitExpressionStmt(Stmt.Expression stmt) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'visitExpressionStmt'");
  }

  @Override
  public String visitFunctionStmt(Stmt.Function stmt) {
    //"(defn name [] body)";
    StringBuilder str = new StringBuilder();

    str.append("(defn " + stmt.name.lexeme + " [");
    int idx = 0;
    for (Token t : stmt.params) {
            ++idx; 
            str.append(t.lexeme);
            if (idx != stmt.params.size()) {
                str.append(" ");
            }
        
    }
    str.append("] ");
    idx = 0;
    for (Stmt statement : stmt.body) {
        idx++;
        String s = statement.accept(this);
        str.append(s);
        if (idx != stmt.body.size()) {
            str.append("\n");
        }
    }

    str.append(")");

    return str.toString();
  }

  @Override
  public String visitIfStmt(Stmt.If stmt) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'visitIfStmt'");
  }

  @Override
  public String visitPrintStmt(Stmt.Print stmt) {
   StringBuilder str = new StringBuilder();

   str.append("(println " + stmt.expression.accept(this) + ")");

   return str.toString();
  }

  @Override
  public String visitReturnStmt(Stmt.Return stmt) {
    return stmt.value.accept(this);
  }

  @Override
  public String visitVarStmt(Stmt.Var stmt) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'visitVarStmt'");
  }

  @Override
  public String visitWhileStmt(Stmt.While stmt) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'visitWhileStmt'");
  }     

    @Override
    public String visitBinaryExpr(Expr.Binary expr){
        return parenthesize(expr.operator.lexeme, expr.left, expr.right);
    }

    @Override
    public String visitGroupingExpr(Expr.Grouping expr) {
        return parenthesize("group", expr.expression);
    }

    @Override
    public String visitLiteralExpr(Expr.Literal expr) {
        if (expr.value == null) return "nil";
        return expr.value.toString();
    }

    @Override
    public String visitUnaryExpr(Expr.Unary expr) {
        return parenthesize(expr.operator.lexeme, expr.right);
    }
    private String parenthesize(String name, Expr... exprs) {
        StringBuilder builder = new StringBuilder();

        builder.append("(").append(name);
        for (Expr expr :exprs) {
            builder.append(" ");
            builder.append(expr.accept(this));
        }
        builder.append(")");

        return builder.toString();
    }
  
}
