package com.twinkle.framework.context.manager;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.twinkle.framework.api.component.AbstractComponent;
import com.twinkle.framework.api.config.Configurable;
import com.twinkle.framework.api.constant.ExceptionCode;
import com.twinkle.framework.api.exception.ConfigurationException;
import com.twinkle.framework.context.PrimitiveAttributeSchema;
import com.twinkle.framework.core.lang.ObjectAttribute;
import com.twinkle.framework.struct.context.StructAttributeSchema;
import com.twinkle.framework.struct.context.StructAttributeSchemaManager;
import com.twinkle.framework.struct.factory.StructAttributeFactoryCenter;
import com.twinkle.framework.struct.factory.StructAttributeFactoryCenterImpl;
import com.twinkle.framework.struct.serialize.FastJSONStructAttributeSerializer;
import com.twinkle.framework.struct.utils.StructAttributeUtil;
import com.twinkle.framework.struct.error.*;
import com.twinkle.framework.struct.type.AttributeType;
import com.twinkle.framework.struct.type.StructAttribute;
import com.twinkle.framework.struct.type.StructType;
import com.twinkle.framework.struct.type.AttributeTypeManager;
import com.twinkle.framework.struct.utils.StructAttributeNameValidator;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     9/6/19 10:03 PM<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
@Slf4j
public class StructAttributeManager extends AbstractComponent implements Configurable {
    public static String NS_SEPARATOR = ":";
    private JSONArray structAttributeArray;
    /**
     * Struct Attribute's name list.
     */
    @Getter
    private List<String> structAttributeList;

    @Override
    public void configure(JSONObject _conf) throws ConfigurationException {
        log.info("Struct Attribute Loader.configure()");
        StructAttributeSchema tempSchema = StructAttributeSchemaManager.getStructAttributeSchema();

        JSONArray tempStructAttributeNameArray = _conf.getJSONArray("NameSpaceNames");
        this.structAttributeArray = _conf.getJSONArray("NameSpaces");
        if (CollectionUtils.isEmpty(this.structAttributeArray) || CollectionUtils.isEmpty(tempStructAttributeNameArray)) {
            this.structAttributeList = new ArrayList<>(0);
            return;
        }
        this.structAttributeList = new ArrayList<>(32);
        for (int i = 0; i < tempStructAttributeNameArray.size(); i++) {
            String tempNameSpaceName = tempStructAttributeNameArray.getString(i);
            for (int j = 0; j < this.structAttributeArray.size(); j++) {
                JSONObject tempObject = this.structAttributeArray.getJSONObject(j);
                String tempItemName = tempObject.getString("NameSpace");
                if (tempNameSpaceName.equals(tempItemName)) {
                    try {
                        if (!tempSchema.hasNamespace(tempItemName)) {
                            StructAttributeNameValidator.checkName(tempItemName);
                            tempSchema.addNamespace(tempItemName);
                        }
                        this.loadNameSpace(tempSchema, tempObject, tempNameSpaceName);
                    } catch (BadAttributeNameException e) {
                        log.warn("The namespace name[{}]'s name is invalid, dismiss it.", tempItemName);
                    } catch (NamespaceAlreadyExistsException e) {
                        log.info("The namespace [{}] exists already, so do nothing.", tempItemName);
                    }
                }
            }
        }
        this.registerIntoSchemaManager();
    }

    /**
     * Register the StructAttributeSchema into the schema manager.
     */
    private void registerIntoSchemaManager() {
        StructAttributeSchema tempStructSchema = StructAttributeSchemaManager.getStructAttributeSchema();
        StructAttributeFactoryCenter tempCenter = new StructAttributeFactoryCenterImpl(
                tempStructSchema, this.getClass().getClassLoader()
        );
        StructAttributeSchemaManager.registerStructAttributeImpl(tempCenter);
    }

    /**
     * Load some namespace's types and attributes.
     *
     * @param _schema
     * @param _conf
     * @param _namespace
     */
    private void loadNameSpace(StructAttributeSchema _schema, JSONObject _conf, String _namespace) {
        JSONArray tempTypes = _conf.getJSONArray("Types");
        for (int i = 0; i < tempTypes.size(); i++) {
            JSONObject tempType = tempTypes.getJSONObject(i);
            String tempTypeName = tempType.getString("TypeName");
            try {
                if (!_schema.hasStructAttributeType(_namespace, tempTypeName)) {
                    StructAttributeNameValidator.checkName(tempTypeName);
                    addStructAttributeType(_schema, tempType, _namespace, tempTypeName, new Vector());
                }
            } catch (BadAttributeNameException e) {
                log.warn("The namespace name[{}]-[{}]'s name is invalid, dismiss it.", tempTypeName);
            } catch (ConfigurationException e) {
                log.error("Encountered configuration exception:", e);
                throw e;
            }
        }
    }

    /**
     * Load the namespace and namespace's attributes.
     *
     * @param _schema
     * @param _namespace
     * @param _typeName
     * @param _typeList
     */
    private void loadNameSpaceAttributes(StructAttributeSchema _schema, String _namespace, String _typeName, List _typeList) {
        for (int i = 0; i < this.structAttributeArray.size(); i++) {
            JSONObject tempNameSpaceObj = this.structAttributeArray.getJSONObject(i);
            String tempNameSpaceName = tempNameSpaceObj.getString("NameSpace");
            if (!tempNameSpaceName.equals(_namespace)) {
                continue;
            }
            JSONArray tempTypes = tempNameSpaceObj.getJSONArray("Types");

            for (int j = 0; j < tempTypes.size(); j++) {
                JSONObject tempType = tempTypes.getJSONObject(j);
                String tempTypeName = tempType.getString("TypeName");
                if (!tempTypeName.equals(_typeName)) {
                    continue;
                }
                try {
                    if (!_schema.hasStructAttributeType(tempNameSpaceName, tempTypeName)) {
                        StructAttributeNameValidator.checkName(tempTypeName);
                        addStructAttributeType(_schema, tempType, tempNameSpaceName, tempTypeName, _typeList);
                    }
                } catch (BadAttributeNameException e) {
                    log.warn("The namespace name[{}]-[{}]'s name is invalid, dismiss it.", tempTypeName);
                } catch (ConfigurationException e) {
                    log.error("Encountered configuration exception:", e);
                    throw e;
                }
            }
        }
    }

    /**
     * Add Struct Attribute into the schema.
     *
     * @param _schema
     * @param _config
     * @param _namespace
     * @param _typeName
     * @param _typeList
     * @throws ConfigurationException
     */
    private void addStructAttributeType(StructAttributeSchema _schema, JSONObject _config, String _namespace, String _typeName, List _typeList) throws ConfigurationException {
        if (_schema.hasStructAttributeType(_namespace, _typeName)) {
            log.info("The namespace[{}] -> [{}] exists in the schema already.", _namespace, _typeName);
            return;
        }

        JSONArray tempAttributeArray = _config.getJSONArray("Attributes");
        if (tempAttributeArray.isEmpty()) {
            throw new ConfigurationException(ExceptionCode.LOGIC_CONF_SA_TYPE_ATTR_MISSED, "Type [" + _typeName + "] does not have attributes.");
        }
        int tempTypeIndex = _typeList.indexOf(_namespace + NS_SEPARATOR + _typeName);
        if (tempTypeIndex != -1) {
            StringBuffer tempBuff = new StringBuffer((String) _typeList.get(tempTypeIndex));
            for (int i = tempTypeIndex + 1; i < _typeList.size(); i++) {
                tempBuff.append("=>");
                tempBuff.append((String) _typeList.get(i));
            }

            tempBuff.append("=>");
            tempBuff.append(_namespace + NS_SEPARATOR + _typeName);
            throw new ConfigurationException(ExceptionCode.LOGIC_CONF_SA_TYPE_ATTR_CIRCULAR_PATH_FOUND, "Circular path is detected: " + tempBuff.toString());
        }
        _typeList.add(_namespace + NS_SEPARATOR + _typeName);
        AttributeTypeManager tempTypeManager = null;
        StructType tempStructType = null;

        try {
            tempStructType = _schema.newStructAttributeType(_namespace, _typeName);
            tempTypeManager = _schema.getTypeManager(_namespace);
        } catch (NamespaceNotFoundException e) {
            throw new ConfigurationException(ExceptionCode.LOGIC_CONF_SA_TYPE_ADD_FAILED, "Can not add struct attribute type " + _namespace + ":" + _typeName, e);
        }
        for (int i = 0; i < tempAttributeArray.size(); i++) {
            JSONObject tempAttrObj = tempAttributeArray.getJSONObject(i);
            if (tempAttrObj.isEmpty()) {
                throw new ConfigurationException(ExceptionCode.LOGIC_CONF_SA_TYPE_ATTR_FIELD_MISSED, "Bad Configuration Format for " + _namespace + NS_SEPARATOR + _typeName);
            }
            String tempAttrName = tempAttrObj.getString("Name");
            String tempAttrType = tempAttrObj.getString("Type");
            String tempAttrOptional = tempAttrObj.getString("Optional");
            if (StringUtils.isBlank(tempAttrName) || StringUtils.isBlank(tempAttrType)) {
                throw new ConfigurationException(ExceptionCode.LOGIC_CONF_SA_TYPE_ATTR_FIELD_MISSED, "Bad Configuration Format for " + _namespace + NS_SEPARATOR + _typeName);
            }
            boolean isOptional = false;
            if (!StringUtils.isBlank(tempAttrOptional)) {
                isOptional = Boolean.parseBoolean(tempAttrOptional);
            }
            AttributeType tempAttrStructType = null;
            try {
                try {
                    tempAttrStructType = tempTypeManager.getType(tempAttrType);
                } catch (TypeNotFoundException e) {
                }

                boolean isStruct = tempAttrStructType == null || tempAttrStructType.isStructType();
                if (isStruct) {
                    int tempNSIndex = tempAttrType.indexOf(NS_SEPARATOR);
                    if (tempNSIndex == -1) {
                        tempAttrType = _namespace + NS_SEPARATOR + tempAttrType;
                    }
                }

                if (tempAttrStructType == null) {
                    String tempAttrNameSpace = tempAttrType.split(NS_SEPARATOR)[0];
                    String tempAttrNameOfNS = tempAttrType.split(NS_SEPARATOR)[1];
                    int tempArrayFlag = tempAttrNameOfNS.indexOf("[]");
                    if (tempArrayFlag != -1) {
                        tempAttrNameOfNS = tempAttrNameOfNS.substring(0, tempArrayFlag);
                    }

                    AttributeTypeManager tempNameSpaceTypeManager = _schema.getTypeManager(tempAttrNameSpace);
                    if (!tempNameSpaceTypeManager.hasTypeName(tempAttrNameOfNS)) {
                        this.loadNameSpaceAttributes(_schema, tempAttrNameSpace, tempAttrNameOfNS, _typeList);
                    }
                }
                tempStructType.addAttribute(tempAttrName, tempAttrType, isOptional);
            } catch (AttributeAlreadyExistsException e) {
                log.warn("Attribute [{}] already exists in type[{}].", tempAttrName, _typeName);
            } catch (TypeNotFoundException e) {
                log.error("Type [{}] not found for attribute [{}]->[{}].", tempAttrType, _typeName, tempAttrName);
                throw new ConfigurationException(ExceptionCode.LOGIC_CONF_ATTR_SA_TYPE_MISSED, "Can not add attribute " + tempAttrName + " into " + _typeName + " type " + tempAttrType + " not found.", e);
            } catch (BadAttributeNameException e) {
                log.error("Attribute name [{}] is invalid.", tempAttrName, e);
                throw new ConfigurationException(ExceptionCode.LOGIC_CONF_SA_NAME_INVALID, "The name of attribute " + tempAttrName + " in Struct Attribute type " + _typeName + " is invalid.", e);
            } catch (StructAttributeTypeNotFoundException e) {
                log.error("Struct Attribute Type for [{}]->[{}] not found.", tempAttrName, _typeName);
                throw new ConfigurationException(ExceptionCode.LOGIC_CONF_SA_TYPE_NOT_FOUND, "Struct Attribute Type is not found", e);
            } catch (NamespaceNotFoundException e) {
                log.error("NameSpace [{}] is not found in the schema.", _namespace);
                throw new ConfigurationException(ExceptionCode.LOGIC_CONF_SA_NAMESPACE_NOT_FOUND, "Namespace is not found.", e);
            }
        }

        try {
            _schema.addStructAttributeType(tempStructType);
            PrimitiveAttributeSchema.getInstance().addAttribute(tempStructType.getQualifiedName(), ObjectAttribute.class.getName());
//            PrimitiveAttributeSchema.getInstance().addAttribute(tempStructAttributeType.getQualifiedName() + "[]", ObjectAttribute.class.getName());
            this.structAttributeList.add(tempStructType.getQualifiedName());
        } catch (StructAttributeTypeAlreadyExistsException e) {
        } catch (NamespaceNotFoundException e) {
        }
        _typeList.remove(_typeList.size() - 1);
    }

    /**
     * Add FastJSON Serializer support for Struct Attributes.
     */
    public final void addFastJsonSerializerSupport() {
        for (String tempItem : this.structAttributeList) {
            StructAttribute tempAttr = StructAttributeUtil.newStructAttribute(tempItem);
            SerializeConfig.getGlobalInstance().put(tempAttr.getClass(), new FastJSONStructAttributeSerializer());
        }
    }
}
