package cn.easygd.dynaguard.core.engine.groovy.ast;

import cn.easygd.dynaguard.core.trace.BizTracker;
import com.google.common.collect.Lists;
import org.codehaus.groovy.ast.ASTNode;
import org.codehaus.groovy.ast.ClassNode;
import org.codehaus.groovy.ast.MethodNode;
import org.codehaus.groovy.ast.Parameter;
import org.codehaus.groovy.ast.expr.*;
import org.codehaus.groovy.ast.stmt.*;
import org.codehaus.groovy.control.SourceUnit;
import org.codehaus.groovy.syntax.Token;
import org.codehaus.groovy.syntax.Types;
import org.codehaus.groovy.transform.ASTTransformation;
import org.codehaus.groovy.transform.GroovyASTTransformation;

import java.util.ArrayList;
import java.util.List;

/**
 * groovy追踪ast配置
 *
 * @author VD
 * @date 2025/8/21 19:47
 */
@GroovyASTTransformation
public class GroovyTrackingTransformation implements ASTTransformation {

    /**
     * 条件变量前缀
     */
    private static final String CONDITION_VAR_PREFIX = "cond_";

    /**
     * 追踪方法名
     */
    private static final String TRIGGER_CONDITION_METHOD = "triggerCondition";

    /**
     * 记录行号方法名
     */
    private static final String RECORD_LINE_NUMBER_METHOD = "recordLineNumber";

    /**
     * 记录触发条件方法名
     */
    private static final String RECORD_TRIGGER_CONDITION_METHOD = "recordTriggerCondition";

    /**
     * 记录变量方法名
     */
    private static final String RECORD_VARIABLE_METHOD = "recordVariable";

    /**
     * The method is invoked when an AST Transformation is active. For local transformations, it is invoked once
     * each time the local annotation is encountered. For global transformations, it is invoked once for every source
     * unit, which is typically a source file.
     *
     * @param nodes  The ASTnodes when the call was triggered. Element 0 is the AnnotationNode that triggered this
     *               annotation to be activated. Element 1 is the AnnotatedNode decorated, such as a MethodNode or ClassNode. For
     *               global transformations it is usually safe to ignore this parameter.
     * @param source The source unit being compiled. The source unit may contain several classes. For global transformations,
     */
    @Override
    public void visit(ASTNode[] nodes, SourceUnit source) {
        // 源文件为空则返回
        if (source == null) {
            return;
        }

        // 遍历所有类节点（脚本会被编译为类）
        List<ClassNode> classNodes = source.getAST().getClasses();
        for (ClassNode classNode : classNodes) {
            processClassNode(classNode);
        }
    }


    /**
     * 处理类节点，遍历方法
     *
     * @param classNode 类节点
     */
    private void processClassNode(ClassNode classNode) {
        for (MethodNode method : classNode.getMethods()) {
            Statement code = method.getCode();
            // 这个地方脚本会被放入run方法中
            if (code instanceof BlockStatement) {
                processBlockStatement((BlockStatement) code, method);
            }
        }
    }


    /**
     * 处理聚合块
     *
     * @param block  聚合块
     * @param method 方法
     */
    private void processBlockStatement(BlockStatement block, MethodNode method) {
        List<Statement> statements = new ArrayList<>(block.getStatements());
        block.getStatements().clear();

        for (Statement stmt : statements) {
            // 递归处理嵌套代码块（如if/for/while中的块）
            if (stmt instanceof BlockStatement) {
                processBlockStatement((BlockStatement) stmt, method);
                block.addStatement(stmt);
            } else if (stmt instanceof IfStatement) {
                // 处理if语句（重点捕获条件表达式）
                processIfStatement((IfStatement) stmt, method);
                block.addStatement(stmt);
            } else if (stmt instanceof ReturnStatement) {
                // 处理return语句，插入追踪逻辑
                ReturnStatement returnStmt = (ReturnStatement) stmt;
                // 生成追踪代码
                List<Statement> trackingStmts = generateTrackingStatements(returnStmt, method);
                // 先添加追踪代码，再添加原return语句
                block.addStatements(trackingStmts);
                block.addStatement(returnStmt);
            } else {
                // 其他语句直接添加
                block.addStatement(stmt);
            }
        }
    }

    /**
     * 处理if语句
     *
     * @param ifStmt if语句
     * @param method 方法
     */
    private void processIfStatement(IfStatement ifStmt, MethodNode method) {
        Expression condition = ifStmt.getBooleanExpression();
        // 为条件表达式生成变量名（用于记录结果）
        String conditionVar = CONDITION_VAR_PREFIX + System.identityHashCode(condition);

        // 新的if块
        BlockStatement newIfBlock = new BlockStatement();

        // 1. 记录条件表达式文本
        newIfBlock.addStatement(genConditionTextStatement(condition));

        // 2. 记录条件结果值
        newIfBlock.addStatement(genConditionValueStatement(condition, conditionVar));

        // 3. 添加原if块内容
        newIfBlock.addStatements(((BlockStatement) ifStmt.getIfBlock()).getStatements());

        ifStmt.setIfBlock(newIfBlock);

        // 递归处理if块内的语句
        processBlockStatement(newIfBlock, method);

        // 处理else块（如果有）
        Statement elseBlock = ifStmt.getElseBlock();
        if (elseBlock instanceof BlockStatement) {
            // 这个地方兼容else中有复杂处理逻辑的情况
            processBlockStatement((BlockStatement) elseBlock, method);
        }
    }

    /**
     * 生成追踪方法
     *
     * @param returnStmt return语句
     * @param method     方法
     * @return 追踪方法
     */
    private List<Statement> generateTrackingStatements(ReturnStatement returnStmt, MethodNode method) {
        List<Statement> stmts = Lists.newArrayList();
        int lineNumber = returnStmt.getLineNumber();

        // 使用BizTracker中的方法记录信息
        ClassExpression tracker = new ClassExpression(new ClassNode(BizTracker.class));

        // 记录行号的参数
        ArgumentListExpression lineParams = new ArgumentListExpression(new ConstantExpression(lineNumber));
        // 记录行号的方法
        MethodCallExpression lineMethodCallExpression = new MethodCallExpression(tracker, RECORD_LINE_NUMBER_METHOD, lineParams);

        // 记录触发条件的参数
        ArgumentListExpression triggerConditionParams = new ArgumentListExpression(new VariableExpression(TRIGGER_CONDITION_METHOD));
        // 记录触发条件的方法
        MethodCallExpression triggerConditionExpression = new MethodCallExpression(tracker, RECORD_TRIGGER_CONDITION_METHOD, triggerConditionParams);

        // 构建出记录行号的语句
        ExpressionStatement lineSt = new ExpressionStatement(lineMethodCallExpression);
        // 构建出记录触发条件的语句
        ExpressionStatement triggerConditionSt = new ExpressionStatement(triggerConditionExpression);

        // 添加记录行号的语句
        stmts.add(lineSt);
        // 添加记录触发return的条件的语句
        stmts.add(triggerConditionSt);

        // 记录关键变量值（获取当前作用域的变量）, 这个地方不收集局部变量
        List<VariableExpression> variables = getScopeVariables(method);
        for (VariableExpression var : variables) {
            ConstantExpression varNameExpression = new ConstantExpression(var.getName());
            ArgumentListExpression varParams = new ArgumentListExpression(varNameExpression, var);
            MethodCallExpression recordVariableExpression = new MethodCallExpression(tracker, RECORD_VARIABLE_METHOD, varParams);
            ExpressionStatement varSt = new ExpressionStatement(recordVariableExpression);
            stmts.add(varSt);
        }
        return stmts;
    }

    /**
     * 生成记录条件表达式文本的代码
     *
     * @param condition 条件表达式
     * @return 语句
     */
    private Statement genConditionTextStatement(Expression condition) {
        BinaryExpression expression = new BinaryExpression(
                new VariableExpression(TRIGGER_CONDITION_METHOD),
                Token.newSymbol(Types.EQUALS, 0, 0),
                new ConstantExpression(condition.getText()));
        return new ExpressionStatement(expression);
    }

    /**
     * 生成记录条件结果的代码
     *
     * @param condition 条件表达式
     * @param varName   变量名
     * @return 语句
     */
    private Statement genConditionValueStatement(Expression condition, String varName) {
        BinaryExpression expression = new BinaryExpression(
                new VariableExpression(varName),
                Token.newSymbol(Types.EQUALS, 0, 0),
                condition);
        return new ExpressionStatement(expression);
    }

    /**
     * 获取当前方法作用域中的变量
     *
     * @param method 方法
     * @return 变量列表
     */
    private List<VariableExpression> getScopeVariables(MethodNode method) {
        // 简化实现：获取方法参数和局部变量（实际场景可扩展）
        List<VariableExpression> vars = new ArrayList<>();
        // 添加方法参数
        for (Parameter param : method.getParameters()) {
            vars.add(new VariableExpression(param.getName()));
        }
        return vars;
    }
}
