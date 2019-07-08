package com.twinkle.framework.core.asm.transformer;

import com.twinkle.framework.core.asm.data.AnnotationDefine;
import com.twinkle.framework.core.asm.data.MethodDefine;
import com.twinkle.framework.core.asm.data.ParameterDefine;
import com.twinkle.framework.core.asm.utils.TypeUtil;
import lombok.extern.slf4j.Slf4j;
import org.objectweb.asm.Label;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.*;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Modifier;
import java.util.*;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2019-06-27 18:31<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
@Slf4j
public abstract class AddMethodTransformer extends ClassTransformer {
    /**
     * Class Transformer.
     */
    protected ClassTransformer classTransformer;
    /**
     * Method definition, will be used to add into the dest class.
     */
    protected MethodDefine methodDefine;
    /**
     * The packed Method node.
     */
    protected MethodNode methodNode;
    /**
     * The Label List for this method.
     */
    protected List<LabelNode> labelNodeList;
    /**
     * The class node.
     */
    protected ClassNode classNode;

    public AddMethodTransformer(ClassTransformer _transformer, MethodDefine _methodDefine){
        super(_transformer);
        this.classTransformer = _transformer;
        this.methodDefine = _methodDefine;
        this.labelNodeList = new ArrayList<>();
        this.initialize();
    }

    /**
     * Initialize the method node.
     */
    private void initialize(){
        this.methodNode = new MethodNode(
                this.methodDefine.getAccess(),
                this.methodDefine.getName(),
                this.methodDefine.getDescriptor(),
                this.methodDefine.getSignature(),
                this.methodDefine.getExceptions()
        );
    }

    @Override
    public void transform(ClassNode _classNode) {
        this.classNode = _classNode;
        boolean isPresent = false;
        for(MethodNode tempNode : _classNode.methods) {
            if(!this.methodDefine.getName().equals(tempNode.name)) {
                continue;
            }
            // Descriptor is different, so dismiss.
            if(!this.methodDefine.getDescriptor().equals(tempNode.desc)) {
                continue;
            }
            //Signature is different, so dismiss.
            if(!this.methodDefine.getSignature().equals(tempNode.signature)) {
                continue;
            }
            log.info("Find the same method [{}] with same parameters in this class.", this.methodDefine.getName());
            isPresent = true;
        }

        if (!isPresent) {
            this.packMethodNode();
            //Set the method's annotations.
            this.methodNode.visibleAnnotations = this.packAnnotationNodeByDefine(this.methodDefine.getAnnotationDefineList());
            //Set the method's parameters
            Map<ParameterNode, List<AnnotationNode>> tempParameterMap = this.visitMethodParameters();

            Iterator<Map.Entry<ParameterNode, List<AnnotationNode>>> tempEntries = tempParameterMap.entrySet().iterator();
            List<ParameterNode> tempParameterNodeList = new ArrayList<>(tempParameterMap.size());
            List<AnnotationNode>[] tempAnnotationListArray = new ArrayList[tempParameterMap.size()];
            int tempIndex = 0;
            while (tempEntries.hasNext()) {
                Map.Entry<ParameterNode, List<AnnotationNode>> entry = tempEntries.next();
                tempParameterNodeList.add(entry.getKey());
                tempAnnotationListArray[tempIndex] = entry.getValue();
                tempIndex ++;
            }

            this.methodNode.parameters = tempParameterNodeList;
            //Set the parameter's annotations.
            this.methodNode.visibleParameterAnnotations = tempAnnotationListArray;
            //set the local parameters. static method no need "this" parameter.
            if(Modifier.isStatic(this.methodDefine.getAccess())){
                this.methodNode.localVariables = this.visitGeneralLocalParameters(0);
            } else {
                this.methodNode.localVariables = this.visitLocalParameters();
            }
            this.methodNode.maxLocals = this.methodNode.localVariables.size();
            this.methodNode.maxStack = this.methodNode.maxLocals;

            this.methodNode.accept(_classNode);
            super.transform(_classNode);
        }
    }

    /**
     * Pack the instructions for this method.
     *
     * @return
     */
    public abstract void packMethodNode();

    /**
     * Visit the method's parameters.
     */
    private Map<ParameterNode, List<AnnotationNode>> visitMethodParameters() {
        Map<ParameterNode, List<AnnotationNode>> tempMap = new LinkedHashMap<>(8);
        if (CollectionUtils.isEmpty(this.methodDefine.getParameterDefineList())) {
            return tempMap;
        }

        for (ParameterDefine tempDefine : this.methodDefine.getParameterDefineList()) {
            ParameterNode tempNode = new ParameterNode(tempDefine.getName(),tempDefine.getAccess());
            tempMap.put(tempNode, this.packAnnotationNodeByDefine(tempDefine.getAnnotationDefineList()));
        }
        return tempMap;
    }

    /**
     * Pack the AnnotationNode with the Annotation Define list.
     *
     * @param _defineList
     * @return
     */
    private List<AnnotationNode> packAnnotationNodeByDefine(List<AnnotationDefine> _defineList){
        List<AnnotationNode> tempAnnotationNodeList = new ArrayList<>();
        if (CollectionUtils.isEmpty(_defineList)) {
            log.debug("Did not find Method[{}]-Annotations.", this.methodDefine.getName());
            return tempAnnotationNodeList;
        }
        for (AnnotationDefine tempAnnotationDefine : _defineList) {
            AnnotationNode tempAnnotationNode = new AnnotationNode(Type.getDescriptor(tempAnnotationDefine.getAnnotationClass()));
            if (CollectionUtils.isEmpty(tempAnnotationDefine.getValuesMap())) {
                log.debug("Do not find Method[{}]-Annotation[{}]'s values.", this.methodDefine.getName(), tempAnnotationDefine.getAnnotationClass());
                tempAnnotationNodeList.add(tempAnnotationNode);
                continue;
            }
            List<Object> tempValuesList = new ArrayList<>();
            //Add Annotation's Parameters and the mapping Values.
            tempAnnotationDefine.getValuesMap().forEach((k, v) -> {tempValuesList.add(k); tempValuesList.add(v);});
            tempAnnotationNode.values = tempValuesList;
            tempAnnotationNodeList.add(tempAnnotationNode);
        }
        return tempAnnotationNodeList;
    }

    /**
     * Add "this" into the local Parameters.
     */
    private List<LocalVariableNode> visitLocalParameters() {
        List<LocalVariableNode> tempList = new ArrayList<>();
        tempList.add(new LocalVariableNode(
                "this",
                this.classNode.name,
                this.classNode.signature,
                (LabelNode)this.methodNode.instructions.getFirst(),
                (LabelNode)this.methodNode.instructions.getLast(),
                0
        ));
        log.debug("Added [this] parameter.");
        tempList.addAll(this.visitGeneralLocalParameters(1));
        return tempList;
    }

    /**
     * Visit the Local Parameters.
     *
     * @param _startIndex
     * @return
     */
    private List<LocalVariableNode> visitGeneralLocalParameters(int _startIndex) {
        List<LocalVariableNode> tempList = new ArrayList<>();
        //Visit the Method parameter firstly.
        //The parameters of the method are the local ones as well.
        tempList.addAll(this.visitLocalParameterList(this.methodDefine.getParameterDefineList(), _startIndex));
        //Visit the Method's Local Parameters.
        int tempIndex = this.methodNode.localVariables.size();
        tempList.addAll(this.visitLocalParameterList(this.methodDefine.getLocalParameterDefineList(), tempIndex));

        return tempList;
    }

    /**
     * Visit the local parameters.
     *
     * @param _defineList
     * @param _startIndex
     */
    private List<LocalVariableNode> visitLocalParameterList(List<ParameterDefine> _defineList, int _startIndex) {
        List<LocalVariableNode> tempList = new ArrayList<>();
        if (CollectionUtils.isEmpty(_defineList)) {
            return tempList;
        }
        int tempIndex = _startIndex;
        for (ParameterDefine tempItem : _defineList) {
            tempList.add(new LocalVariableNode(
                    tempItem.getName(),
                    Type.getDescriptor(tempItem.getTypeDefine().getTypeClass()),
                    TypeUtil.getTypeSignature(tempItem.getTypeDefine()),
                    this.getLabelNodeFromList(tempItem.getStartLabelIndex()),
                    this.getLabelNodeFromList(tempItem.getEndLabelIndex()),
                    tempIndex++)
            );
        }
        return tempList;
    }

    /**
     * Get the Label from LabelNode.
     *
     * @param _insnNode
     * @return
     */
    private Label getLabelFromLabelNode(AbstractInsnNode _insnNode) {
        if (_insnNode instanceof LabelNode) {
            return ((LabelNode) _insnNode).getLabel();
        }
        log.warn("The node [{}] is not LabelNode, so return a new Label.", _insnNode);
        return new Label();
    }

    /**
     * Get label by the label's index.
     *
     * @param _index
     * @return
     */
    private LabelNode getLabelNodeFromList(int _index) {
        if (CollectionUtils.isEmpty(this.labelNodeList)) {
            log.warn("The Label list is empty, so return a new Label.");
            return new LabelNode();
        }
        if (_index >= this.labelNodeList.size()) {
            log.warn("The index [{}] exceed the size of the label list, so return the last Label.", _index);
            return this.labelNodeList.get(this.labelNodeList.size() - 1);
        }
        return this.labelNodeList.get(_index);
    }
}
