package com.twinkle.framework.connector.http.server.handler;

import com.twinkle.framework.api.constant.ResultCode;
import com.twinkle.framework.api.data.GeneralResult;
import com.twinkle.framework.api.exception.RuleException;
import com.twinkle.framework.core.context.model.NormalizedContext;
import com.twinkle.framework.core.datastruct.handler.AbstractMethodInstructionHandler;
import com.twinkle.framework.core.datastruct.schema.AttributeDef;
import com.twinkle.framework.core.datastruct.schema.MethodDef;
import com.twinkle.framework.core.lang.Attribute;
import com.twinkle.framework.core.lang.MethodAttribute;
import com.twinkle.framework.core.utils.TypeUtil;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.slf4j.Logger;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2019-08-21 17:04<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public class DefaultHttpMethodInstructionHandler extends AbstractMethodInstructionHandler {
    @Override
    public MethodVisitor addInstructions(MethodVisitor _visitor, String _className, MethodDef _methodDef) {
        //log.info("The request is: {} -> {}", _userName, _testParam);
        Label tempLabelA = new Label();
        _visitor.visitLabel(tempLabelA);
        _visitor.visitFieldInsn(Opcodes.GETSTATIC, _className, "log", Type.getDescriptor(Logger.class));
        List<AttributeDef> tempParameterList = _methodDef.getParameterAttrs();
        int tempLocalIndex = 1;
        if (CollectionUtils.isEmpty(tempParameterList)) {
            _visitor.visitLdcInsn("The Request does not contain parameter.");
            _visitor.visitMethodInsn(Opcodes.INVOKEINTERFACE, Type.getInternalName(Logger.class), "info", Type.getMethodDescriptor(Type.getType(Void.TYPE), Type.getType(String.class)));
        } else {
            this.visitInComingLog(_visitor, tempParameterList);
            tempLocalIndex = tempParameterList.size() + 1;
        }

        //NormalizedContext tempNc = this.getNormalizedContext();
        Label tempLabelB = new Label();
        _visitor.visitLabel(tempLabelB);
        _visitor.visitVarInsn(Opcodes.ALOAD, 0);
        _visitor.visitMethodInsn(Opcodes.INVOKEVIRTUAL, _className, "getNormalizedContext", Type.getMethodDescriptor(Type.getType(NormalizedContext.class)));
        int tempNCIndex = tempLocalIndex++;
        _visitor.visitVarInsn(Opcodes.ASTORE, tempNCIndex);
        //Decode the method parameter one by one.
        //this.decodeParameter(tempNc, 15, _userName);
        //this.decodeParameter(tempNc, 100, _testParam);
        Label tempLabelC = new Label();
        _visitor.visitLabel(tempLabelC);
        for (int i = 0; i < tempParameterList.size(); i++) {
            if (i > 0) {
                Label tempLabel = new Label();
                _visitor.visitLabel(tempLabel);
            }
            _visitor.visitVarInsn(Opcodes.ALOAD, 0);// this
            _visitor.visitVarInsn(Opcodes.ALOAD, tempNCIndex);
            Object tempIndexObj = tempParameterList.get(i).getExtraInfoByKey(Attribute.EXT_INFO_NC_INDEX);
            int tempIndex = Integer.parseInt(tempIndexObj.toString());
            this.visitConstantValue(_visitor, tempIndex);
            _visitor.visitVarInsn(Opcodes.ALOAD, i + 1);
            _visitor.visitMethodInsn(Opcodes.INVOKEVIRTUAL, _className, "decodeParameter", Type.getMethodDescriptor(Type.getType(Void.TYPE), Type.getType(NormalizedContext.class), Type.getType(Integer.TYPE), Type.getType(Object.class)));
        }
        //GeneralResult<String> tempResult = new GeneralResult<>();
        Label tempLabelD = new Label();
        _visitor.visitLabel(tempLabelD);
        _visitor.visitTypeInsn(Opcodes.NEW, Type.getInternalName(GeneralResult.class));
        _visitor.visitInsn(Opcodes.DUP);
        _visitor.visitMethodInsn(Opcodes.INVOKESPECIAL,
                Type.getInternalName(GeneralResult.class),
                "<init>",
                Type.getMethodDescriptor(Type.getType(Void.TYPE)));
        int tempResultIndex = tempLocalIndex++;
        _visitor.visitVarInsn(Opcodes.ASTORE, tempResultIndex);
        //try
        // this.invokeRuleChain(tempNc, "TestRuleChain");
        Label tempLabelE = new Label();
        _visitor.visitLabel(tempLabelE);
        _visitor.visitVarInsn(Opcodes.ALOAD, 0);
        _visitor.visitVarInsn(Opcodes.ALOAD, tempNCIndex);
        Object tempRuleChain = _methodDef.getExtraInfoByKey(MethodAttribute.EXT_INFO_RULE_CHAIN);
        _visitor.visitLdcInsn(tempRuleChain);
        _visitor.visitMethodInsn(Opcodes.INVOKEVIRTUAL, _className, "invokeRuleChain", Type.getMethodDescriptor(Type.getType(NormalizedContext.class), Type.getType(NormalizedContext.class), Type.getType(String.class)));
        _visitor.visitInsn(Opcodes.POP);

        //tempResult.setCode(ResultCode.OPERATION_SUCCESS);
        Label tempLabelF = new Label();
        _visitor.visitLabel(tempLabelF);
        _visitor.visitVarInsn(Opcodes.ALOAD, tempResultIndex);
        _visitor.visitLdcInsn(ResultCode.OPERATION_SUCCESS);
        _visitor.visitMethodInsn(Opcodes.INVOKEVIRTUAL,
                Type.getInternalName(GeneralResult.class),
                "setCode",
                Type.getMethodDescriptor(Type.getType(Void.TYPE), Type.getType(String.class)));

        // tempResult.setData(this.encodeReturnData(tempNc, 2));
        Label tempLabelG = new Label();
        _visitor.visitLabel(tempLabelG);
        _visitor.visitVarInsn(Opcodes.ALOAD, tempResultIndex);
        _visitor.visitVarInsn(Opcodes.ALOAD, 0);
        _visitor.visitVarInsn(Opcodes.ALOAD, tempNCIndex);
        Object tempIndexObj = _methodDef.getExtraInfoByKey(MethodAttribute.EXT_INFO_RETURN_NC_INDEX);
        int tempIndex = Integer.parseInt(tempIndexObj.toString());
        this.visitConstantValue(_visitor, tempIndex);
        _visitor.visitMethodInsn(Opcodes.INVOKEVIRTUAL,
                _className,
                "encodeReturnData",
                Type.getMethodDescriptor(Type.getType(Object.class), Type.getType(NormalizedContext.class), Type.getType(Integer.TYPE)));
        _visitor.visitMethodInsn(Opcodes.INVOKEVIRTUAL,
                Type.getInternalName(GeneralResult.class),
                "setData",
                Type.getMethodDescriptor(Type.getType(Void.TYPE), Type.getType(Object.class)));

        //catch
        Label tempLabelH = new Label();
        _visitor.visitLabel(tempLabelH);
        Label tempLabelN = new Label();
        _visitor.visitJumpInsn(Opcodes.GOTO, tempLabelN);
        //(RuleException e)
        Label tempLabelI = new Label();
        _visitor.visitLabel(tempLabelI);
        int tempRuleExceptionIndex = tempLocalIndex++;
        _visitor.visitVarInsn(Opcodes.ASTORE, tempRuleExceptionIndex);
        //log.error("Encountered error while applying the rule chain[{}]. Exception: {}", "TestRuleChain", e);
        Label tempLabelJ = new Label();
        _visitor.visitLabel(tempLabelJ);
        _visitor.visitFieldInsn(Opcodes.GETSTATIC, _className, "log", Type.getDescriptor(Logger.class));
        _visitor.visitLdcInsn("Encountered error while applying the rule chain[{}]. Exception: {}");
        _visitor.visitLdcInsn(tempRuleChain);
        _visitor.visitVarInsn(Opcodes.ALOAD, tempRuleExceptionIndex);
        _visitor.visitMethodInsn(Opcodes.INVOKEINTERFACE,
                Type.getInternalName(Logger.class), "error",
                Type.getMethodDescriptor(Type.getType(Void.TYPE), Type.getType(String.class), Type.getType(Object.class), Type.getType(Object.class)));

        //tempResult.setCode(e.getCode());
        Label tempLabelK = new Label();
        _visitor.visitLabel(tempLabelK);
        _visitor.visitVarInsn(Opcodes.ALOAD, tempResultIndex);
        _visitor.visitVarInsn(Opcodes.ALOAD, tempRuleExceptionIndex);
        _visitor.visitMethodInsn(Opcodes.INVOKEVIRTUAL,
                Type.getInternalName(RuleException.class),
                "getCode",
                Type.getMethodDescriptor(Type.getType(Integer.TYPE)));
        _visitor.visitMethodInsn(Opcodes.INVOKEVIRTUAL,
                Type.getInternalName(GeneralResult.class),
                "setCode",
                Type.getMethodDescriptor(Type.getType(Void.TYPE), Type.getType(Integer.TYPE)));
        //tempResult.setData(e.getMessage());
        Label tempLabelL = new Label();
        _visitor.visitLabel(tempLabelL);
        _visitor.visitVarInsn(Opcodes.ALOAD, tempResultIndex);
        _visitor.visitVarInsn(Opcodes.ALOAD, tempRuleExceptionIndex);
        _visitor.visitMethodInsn(Opcodes.INVOKEVIRTUAL,
                Type.getInternalName(RuleException.class),
                "getMessage",
                Type.getMethodDescriptor(Type.getType(String.class)));
        _visitor.visitMethodInsn(Opcodes.INVOKEVIRTUAL,
                Type.getInternalName(GeneralResult.class),
                "setData",
                Type.getMethodDescriptor(Type.getType(Void.TYPE), Type.getType(Object.class)));
        //return tempResult.
        _visitor.visitLabel(tempLabelN);
        _visitor.visitVarInsn(Opcodes.ALOAD, tempResultIndex);
        _visitor.visitInsn(Opcodes.ARETURN);
        Label tempLabelO = new Label();
        _visitor.visitLabel(tempLabelO);
        _visitor.visitTryCatchBlock(tempLabelE, tempLabelH,tempLabelI,
                Type.getInternalName(RuleException.class));

        // Add the local parameters into the method body.
        _visitor.visitLocalVariable("this",
                TypeUtil.getDescriptorByClassName(_className), "",
                tempLabelA, tempLabelO, 0);
        for (int i = 0; i < tempParameterList.size(); i++) {
            AttributeDef tempAttr = tempParameterList.get(i);
            _visitor.visitLocalVariable(tempAttr.getFieldName(), TypeUtil.getFieldDescriptor(tempAttr.getType().getType()),
                    TypeUtil.getTypeSignature(tempAttr.getType()),
                    tempLabelA, tempLabelO, i + 1);
        }
        _visitor.visitLocalVariable("tempNc",
                Type.getDescriptor(NormalizedContext.class), "",
                tempLabelC, tempLabelO, tempNCIndex);

        _visitor.visitLocalVariable("tempResult",
                Type.getDescriptor(GeneralResult.class), TypeUtil.getTypeSignature(_methodDef.getReturnType()),
                tempLabelE, tempLabelO, tempResultIndex);

        _visitor.visitLocalVariable("e",
                Type.getDescriptor(RuleException.class), "",
                tempLabelJ, tempLabelN, tempRuleExceptionIndex);

        return _visitor;
    }

    /**
     * add the log.info() firstly.
     *
     * @param _visitor
     * @param _paramList
     */
    private void visitInComingLog(MethodVisitor _visitor, List<AttributeDef> _paramList) {
        int tempSize = _paramList.size();
        switch (tempSize) {
            case 0:
                _visitor.visitLdcInsn("The Request does not contain parameter.");
                _visitor.visitMethodInsn(Opcodes.INVOKEINTERFACE, Type.getInternalName(Logger.class), "info", Type.getMethodDescriptor(Type.getType(Void.TYPE), Type.getType(String.class)));
                break;
            case 1:
                StringBuilder tempBuilder = new StringBuilder("The Request contains: ");
                tempBuilder.append(_paramList.get(0).getName());
                tempBuilder.append("=[{}].");
                _visitor.visitLdcInsn(tempBuilder.toString());
                _visitor.visitVarInsn(Opcodes.ALOAD, 1);
                _visitor.visitMethodInsn(Opcodes.INVOKEINTERFACE, Type.getInternalName(Logger.class), "info", Type.getMethodDescriptor(Type.getType(Void.TYPE), Type.getType(String.class), Type.getType(Object.class)));
                break;
            case 2:
                tempBuilder = new StringBuilder("The Request contains: ");
                tempBuilder.append(_paramList.get(0).getName());
                tempBuilder.append("=[{}] ");
                tempBuilder.append(_paramList.get(1).getName());
                tempBuilder.append("=[{}] ");
                _visitor.visitLdcInsn(tempBuilder.toString());
                _visitor.visitVarInsn(Opcodes.ALOAD, 1);
                _visitor.visitVarInsn(Opcodes.ALOAD, 2);
                _visitor.visitMethodInsn(Opcodes.INVOKEINTERFACE, Type.getInternalName(Logger.class), "info", Type.getMethodDescriptor(Type.getType(Void.TYPE), Type.getType(String.class), Type.getType(Object.class), Type.getType(Object.class)));
                break;
            default:
                tempBuilder = new StringBuilder("The Request contains: ");
                for (AttributeDef tempAttr : _paramList) {
                    tempBuilder.append(tempAttr.getName());
                    tempBuilder.append("=[{}] ");
                }
                _visitor.visitLdcInsn(tempBuilder.toString());
                this.visitConstantValue(_visitor, tempSize);
                _visitor.visitTypeInsn(Opcodes.ANEWARRAY, Type.getInternalName(Object.class));

                for (int i = 0; i < tempSize; i++) {
                    _visitor.visitInsn(Opcodes.DUP);
                    this.visitConstantValue(_visitor, i);
                    _visitor.visitVarInsn(Opcodes.ALOAD, i + 1);
                    _visitor.visitInsn(Opcodes.AASTORE);
                }
                _visitor.visitMethodInsn(Opcodes.INVOKEINTERFACE, Type.getInternalName(Logger.class), "info", Type.getMethodDescriptor(Type.getType(Void.TYPE), Type.getType(String.class), Type.getType(Object[].class)));
        }
    }
}
