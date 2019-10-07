package com.twinkle.framework.ruleengine.rule.support;

//import com.alibaba.fastjson.JSONObject;
//import com.twinkle.framework.api.config.Configurable;
//import com.twinkle.framework.api.context.NormalizedContext;
//import com.twinkle.framework.api.exception.ConfigurationException;
//import com.twinkle.framework.struct.context.StructAttributeSchema;
//import com.twinkle.framework.struct.context.StructAttributeSchemaManager;
//import com.twinkle.framework.struct.factory.StructAttributeFactory;
//import com.twinkle.framework.struct.ref.AttributeRef;
//import com.twinkle.framework.struct.type.StructAttribute;
//import com.twinkle.framework.struct.type.StructType;
//
//import java.util.HashMap;
//import java.util.Map;
//
///**
// * Function: TODO ADD FUNCTION. <br/>
// * Reason:	 TODO ADD REASON. <br/>
// * Date:     9/25/19 9:32 PM<br/>
// *
// * @author chenxj
// * @see
// * @since JDK 1.8
// */
//public class StructAttributeManipulator implements Configurable {
//    private StructType rootNMEType_;
//    private String encodeAttrName_;
//    private AttributeRef encodeAttr_;
//    private Map<String, String> decodeAttrNameMap_;
//    private Map<StructType, AttributeRef> decodeAttributeMap_;
//    private boolean dynamicEncode_;
//    private String decodeSpecifier_;
//    private AttributeRef decodeSpecifierAttr_;
//
//    @Override
//    public void configure(JSONObject _conf) throws ConfigurationException {
//        if (var1 != null) {
//            String var2 = var1.getAttribute("RootNMEType");
//            this.encodeAttrName_ = var1.getAttribute("EncodeAttribute");
//            String[][] var3 = (String[][])null;
//            if (var1.getAttribute("DecodeAttributes") != null) {
//                var3 = var1.getAttributesAsColumns("DecodeAttributes", 2, ",");
//            } else if (var2 != null && this.encodeAttrName_ == null) {
//                throw new ConfigurationException(var1, "RootNMEType", "EncodeAttribute or DecodeAttributes should be configured");
//            }
//
//            StructAttributeSchema var4 = StructAttributeSchemaManager.getStructAttributeSchema();
//            StructAttributeFactory var5 = StructAttributeSchemaManager.getStructAttributeFactory();
//            if (var2 != null) {
//                this.rootNMEType_ = var4.getStructAttributeType(var2);
//            }
//
//            if (this.encodeAttrName_ != null) {
//                if (this.encodeAttrName_.startsWith("%") && this.encodeAttrName_.endsWith("%")) {
//                    this.dynamicEncode_ = true;
//                    this.encodeAttrName_ = this.encodeAttrName_.substring(1, this.encodeAttrName_.length() - 1);
//                }
//
//                if (this.rootNMEType_ != null) {
//                    this.encodeAttr_ = var5.getAttributeRef(this.rootNMEType_, this.encodeAttrName_);
//                    if (this.dynamicEncode_) {
//                        if (!this.encodeAttr_.getType().isStringType()) {
//                            throw new ConfigurationException(var1, "EncodeAttribute", this.encodeAttrName_ + " should be of string type but is " + this.encodeAttr_.getType().getName());
//                        }
//                    } else if (!this.encodeAttr_.getType().isStructType()) {
//                        throw new ConfigurationException(var1, "EncodeAttribute", this.encodeAttrName_ + " should be a NME type but is " + this.encodeAttr_.getType().getName());
//                    }
//                }
//            }
//
//            if (var3 != null) {
//                this.decodeAttrNameMap_ = new HashMap();
//                if (this.rootNMEType_ != null) {
//                    this.decodeAttributeMap_ = new HashMap();
//                }
//
//                for(int i = 0; i < var3.length; ++i) {
//                    this.decodeAttrNameMap_.put(var3[i][0], var3[i][1]);
//                    if (this.rootNMEType_ != null) {
//                        AttributeRef var7 = var5.getAttributeRef(this.rootNMEType_, var3[i][1]);
//                        if (!var7.getType().isStructType()) {
//                            throw new ConfigurationException(var1, "DecodeAttributes", var3[i][1] + " should be a NME but is " + var7.getType().getName());
//                        }
//
//                        this.decodeAttributeMap_.put(var4.getStructAttributeType(var3[i][0]), var7);
//                    }
//                }
//
//                this.decodeSpecifier_ = var1.getAttribute("DecodedAttributeSpecifier");
//                if (this.decodeSpecifier_ != null && this.rootNMEType_ != null) {
//                    this.decodeSpecifierAttr_ = var5.getAttributeRef(this.rootNMEType_, this.decodeSpecifier_);
//                    if (!this.decodeSpecifierAttr_.getType().isStringType()) {
//                        throw new ConfigurationException(var1, "DecodedAttributeSpecifier", this.decodeSpecifier_ + " should be of string type but is " + this.decodeSpecifierAttr_.getType().getName());
//                    }
//                }
//            }
//
//        }
//    }
//    public StructAttribute drillDown(NormalizedContext var1) {
//        StructAttribute var2 = null;
//        if (this.encodeAttrName_ != null && var1 instanceof NMEAdapter) {
//            NMEAdapter var3 = (NMEAdapter)var1;
//            var2 = var3.getNME();
//            if (var2 != null) {
//                var3.setNME(this.getSubSNME(var2));
//            }
//        }
//
//        return var2;
//    }
//
//    public StructAttribute getSubSNME(StructAttribute var1) {
//        if (this.encodeAttrName_ != null) {
//            StructAttributeFactory var2 = StructAttributeSchemaManager.getStructAttributeFactory();
//            StructType var3 = var1.getType();
//            AttributeRef var4;
//            if (this.encodeAttr_ != null) {
//                if (!var3.equals(this.rootNMEType_)) {
//                    throw new IllegalArgumentException("For getting the sub SNME, expected SNME of type " + this.rootNMEType_.getQualifiedName() + " but got " + var3.getQualifiedName());
//                }
//
//                var4 = this.encodeAttr_;
//            } else {
//                var4 = var2.getAttributeRef(var3, this.encodeAttrName_);
//            }
//
//            if (this.dynamicEncode_) {
//                var4 = var2.getAttributeRef(var3, var1.getString(var4));
//            }
//
//            StructAttribute var5 = var1.getStruct(var4);
//            return var5;
//        } else {
//            return var1;
//        }
//    }
//
//    public String getAttributeToBeEncoded(StructAttribute var1) {
//        if (this.encodeAttrName_ != null) {
//            if (this.dynamicEncode_) {
//                StructAttributeFactory var2 = StructAttributeSchemaManager.getStructAttributeFactory();
//                StructType var3 = var1.getType();
//                AttributeRef var4;
//                if (this.encodeAttr_ != null) {
//                    if (!var3.equals(this.rootNMEType_)) {
//                        throw new IllegalArgumentException("For getting the sub SNME, expected SNME of type " + this.rootNMEType_.getQualifiedName() + " but got " + var3.getQualifiedName());
//                    }
//
//                    var4 = this.encodeAttr_;
//                } else {
//                    var4 = var2.getAttributeRef(var3, this.encodeAttrName_);
//                }
//
//                return var1.getString(var4);
//            } else {
//                return this.encodeAttrName_;
//            }
//        } else {
//            return null;
//        }
//    }
//
//    public void floatUp(NormalizedContext var1, StructAttribute var2) {
//        if (this.decodeAttrNameMap_ != null && var1 instanceof NMEAdapter) {
//            NMEAdapter var3 = (NMEAdapter)var1;
//            StructAttribute var4 = var3.getNME();
//            if (var4 != null) {
//                var3.setNME(this.putAsSubSNME(var4, var2));
//            }
//        }
//
//    }
//
//    public StructAttribute putAsSubSNME(StructAttribute var1, StructAttribute var2) {
//        StructAttributeFactory var3 = StructAttributeSchemaManager.getStructAttributeFactory();
//        AttributeRef var4 = null;
//        if (this.decodeAttributeMap_ != null) {
//            var4 = (AttributeRef)this.decodeAttributeMap_.get(var1.getType());
//            if (var4 == null) {
//                throw new IllegalArgumentException("Unexpected record type " + var1.getType().getQualifiedName());
//            }
//
//            if (var2 == null) {
//                var2 = var3.newStructAttribute(this.rootNMEType_);
//            } else if (!var2.getType().equals(this.rootNMEType_)) {
//                throw new IllegalArgumentException("Expected SNME of type " + this.rootNMEType_.getQualifiedName() + " but got " + var2.getType().getQualifiedName());
//            }
//        } else if (this.decodeAttrNameMap_ != null) {
//            String var5 = (String)this.decodeAttrNameMap_.get(var1.getType().getQualifiedName());
//            if (var5 == null) {
//                throw new IllegalArgumentException("Unexpected record type " + var1.getType().getQualifiedName());
//            }
//
//            if (var2 == null) {
//                throw new IllegalArgumentException("Input NME not available");
//            }
//
//            var4 = var3.getAttributeRef(var2.getType(), var5);
//        }
//
//        if (var4 != null) {
//            var2.setStruct(var4, var1);
//            if (this.decodeSpecifier_ != null) {
//                AttributeRef var6 = this.decodeSpecifierAttr_;
//                if (var6 == null) {
//                    var6 = var3.getAttributeRef(var2.getType(), this.decodeSpecifier_);
//                }
//
//                var2.setString(var6, var4.getName());
//            }
//
//            return var2;
//        } else {
//            return var1;
//        }
//    }
//}
