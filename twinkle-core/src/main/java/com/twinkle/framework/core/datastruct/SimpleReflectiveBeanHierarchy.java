package com.twinkle.framework.core.datastruct;

import com.twinkle.framework.core.datastruct.define.HierarchicalName;

import java.text.ParseException;
import java.util.*;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2019-08-03 16:04<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public class SimpleReflectiveBeanHierarchy implements SimpleReflectiveBean {
    private final SimpleReflectiveBean bean;
    private final String[] arrayBrackets;
    private final Map<String, HierarchicalName> names;

    public SimpleReflectiveBeanHierarchy(SimpleReflectiveBean _bean, String[] _arrayBracketArray) {
        this.bean = _bean;
        this.arrayBrackets = _arrayBracketArray;
        this.names = new HashMap();
    }

    public SimpleReflectiveBeanHierarchy(SimpleReflectiveBean _bean) {
        this.bean = _bean;
        this.arrayBrackets = HierarchicalName.ARRAY_LIST_BRACKETS;
        this.names = new HashMap();
    }

    protected SimpleReflectiveBean traverseHierarchy(HierarchicalName _name) {
        if (_name == null) {
            return null;
        } else {
            SimpleReflectiveBean tempCurrentBean = this.bean;

            for (HierarchicalName item = _name.head(); item != _name; item = item.next()) {
                SimpleReflectiveBean tempBean = tempCurrentBean;
                if (item.index() >= 0) {
                    tempCurrentBean = this.getBeanArrayElement(tempCurrentBean, item);
                } else {
                    tempCurrentBean = this.getBeanElement(tempCurrentBean, item);
                }

                if (tempCurrentBean == null) {
                    if (!tempBean.isAttributeSet(item.name())) {
                        throw new IllegalStateException("A value for attribute " + item.toString() + " is not set.");
                    }

                    throw new IllegalStateException("A value for attribute " + item.toString() + " is null.");
                }
            }

            return tempCurrentBean;
        }
    }

    protected SimpleReflectiveBean getBeanElement(SimpleReflectiveBean _bean, HierarchicalName _hierarchicalName) {
        try {
            return (SimpleReflectiveBean) _bean.getObjectAttribute(_hierarchicalName.name());
        } catch (ClassCastException e) {
            throw new NoSuchElementException(_hierarchicalName.toString() + "[" + e.getMessage() + "]");
        } catch (NoSuchElementException e) {
            throw new NoSuchElementException(_hierarchicalName.toString() + "[" + e.getMessage() + "]");
        }
    }

    protected SimpleReflectiveBean getBeanArrayElement(SimpleReflectiveBean _bean, HierarchicalName _hierarchicalName) {
        switch (_hierarchicalName.kind()) {
            case 0:
                this.getListElement(_bean, _hierarchicalName);
            default:
                throw new UnsupportedOperationException("Unsupported array/list brackets: " + _hierarchicalName.toString());
        }
    }

    protected SimpleReflectiveBean getListElement(SimpleReflectiveBean _bean, HierarchicalName _hierarchicalName) {
        List<SimpleReflectiveBean> tempBeanList = (List<SimpleReflectiveBean>) _bean.getObjectAttribute(_hierarchicalName.name());

        try {
            return tempBeanList.get(_hierarchicalName.index());
        } catch (IndexOutOfBoundsException e) {
            ArrayIndexOutOfBoundsException tempException = new ArrayIndexOutOfBoundsException(_hierarchicalName.toString());
            tempException.initCause(e);
            throw tempException;
        } catch (ClassCastException e) {
            NoSuchElementException tempException = new NoSuchElementException(_hierarchicalName.next().toString());
            tempException.initCause(e);
            throw tempException;
        }
    }

    protected String[] getArrayBrackets() {
        return this.arrayBrackets;
    }

    protected HierarchicalName getHierarchicalName(String _name) {
        HierarchicalName tempHeirarchicalName = this.names.get(_name);
        if (tempHeirarchicalName == null) {
            try {
                tempHeirarchicalName = new HierarchicalName(_name, this.getArrayBrackets());
                this.names.put(_name, tempHeirarchicalName);
            } catch (ParseException var4) {
                throw new IllegalArgumentException(var4);
            }
        }

        return tempHeirarchicalName;
    }

    public Collection<HierarchicalName> getHierarchicalNames(String... _hierarchicalNames) {
        if (_hierarchicalNames == null) {
            return null;
        }
        Set<HierarchicalName> tempResultSet = new HashSet<>(_hierarchicalNames.length);
        for (int i = 0; i < _hierarchicalNames.length; ++i) {
            tempResultSet.add(this.getHierarchicalName(_hierarchicalNames[i]));
        }
        return tempResultSet;
    }

    public SimpleReflectiveBean getBean() {
        return this.bean;
    }

    @Override
    public byte getByteAttribute(String _name) {
        HierarchicalName tempHierarchicalName = this.getHierarchicalName(_name);
        SimpleReflectiveBean tempBean = this.traverseHierarchy(tempHierarchicalName);
        return tempBean.getByteAttribute(tempHierarchicalName.name());
    }

    @Override
    public void setByteAttribute(String _name, byte _value) {
        HierarchicalName tempHierarchicalName = this.getHierarchicalName(_name);
        SimpleReflectiveBean tempBean = this.traverseHierarchy(tempHierarchicalName);
        tempBean.setByteAttribute(tempHierarchicalName.name(), _value);
    }

    @Override
    public short getShortAttribute(String _name) {
        HierarchicalName tempHierarchicalName = this.getHierarchicalName(_name);
        SimpleReflectiveBean tempBean = this.traverseHierarchy(tempHierarchicalName);
        return tempBean.getShortAttribute(tempHierarchicalName.name());
    }

    @Override
    public void setShortAttribute(String _name, short _value) {
        HierarchicalName tempHierarchicalName = this.getHierarchicalName(_name);
        SimpleReflectiveBean tempBean = this.traverseHierarchy(tempHierarchicalName);
        tempBean.setShortAttribute(tempHierarchicalName.name(), _value);
    }

    @Override
    public int getIntAttribute(String _name) {
        HierarchicalName tempHierarchicalName = this.getHierarchicalName(_name);
        SimpleReflectiveBean tempBean = this.traverseHierarchy(tempHierarchicalName);
        return tempBean.getIntAttribute(tempHierarchicalName.name());
    }

    @Override
    public void setIntAttribute(String _name, int _value) {
        HierarchicalName tempHierarchicalName = this.getHierarchicalName(_name);
        SimpleReflectiveBean tempBean = this.traverseHierarchy(tempHierarchicalName);
        tempBean.setIntAttribute(tempHierarchicalName.name(), _value);
    }

    @Override
    public long getLongAttribute(String _name) {
        HierarchicalName tempHierarchicalName = this.getHierarchicalName(_name);
        SimpleReflectiveBean tempBean = this.traverseHierarchy(tempHierarchicalName);
        return tempBean.getLongAttribute(tempHierarchicalName.name());
    }

    @Override
    public void setLongAttribute(String _name, long _value) {
        HierarchicalName tempHierarchicalName = this.getHierarchicalName(_name);
        SimpleReflectiveBean tempBean = this.traverseHierarchy(tempHierarchicalName);
        tempBean.setLongAttribute(tempHierarchicalName.name(), _value);
    }

    @Override
    public char getCharAttribute(String _name) {
        HierarchicalName tempHierarchicalName = this.getHierarchicalName(_name);
        SimpleReflectiveBean tempBean = this.traverseHierarchy(tempHierarchicalName);
        return tempBean.getCharAttribute(tempHierarchicalName.name());
    }

    @Override
    public void setCharAttribute(String _name, char _value) {
        HierarchicalName tempHierarchicalName = this.getHierarchicalName(_name);
        SimpleReflectiveBean tempBean = this.traverseHierarchy(tempHierarchicalName);
        tempBean.setCharAttribute(tempHierarchicalName.name(), _value);
    }

    @Override
    public boolean getBooleanAttribute(String _name) {
        HierarchicalName tempHierarchicalName = this.getHierarchicalName(_name);
        SimpleReflectiveBean tempBean = this.traverseHierarchy(tempHierarchicalName);
        return tempBean.getBooleanAttribute(tempHierarchicalName.name());
    }

    @Override
    public void setBooleanAttribute(String _name, boolean _value) {
        HierarchicalName tempHierarchicalName = this.getHierarchicalName(_name);
        SimpleReflectiveBean tempBean = this.traverseHierarchy(tempHierarchicalName);
        tempBean.setBooleanAttribute(tempHierarchicalName.name(), _value);
    }

    @Override
    public float getFloatAttribute(String _name) {
        HierarchicalName tempHierarchicalName = this.getHierarchicalName(_name);
        SimpleReflectiveBean tempBean = this.traverseHierarchy(tempHierarchicalName);
        return tempBean.getFloatAttribute(tempHierarchicalName.name());
    }

    @Override
    public void setFloatAttribute(String _name, float _value) {
        HierarchicalName tempHierarchicalName = this.getHierarchicalName(_name);
        SimpleReflectiveBean tempBean = this.traverseHierarchy(tempHierarchicalName);
        tempBean.setFloatAttribute(tempHierarchicalName.name(), _value);
    }

    @Override
    public double getDoubleAttribute(String _name) {
        HierarchicalName tempHierarchicalName = this.getHierarchicalName(_name);
        SimpleReflectiveBean tempBean = this.traverseHierarchy(tempHierarchicalName);
        return tempBean.getDoubleAttribute(tempHierarchicalName.name());
    }

    @Override
    public void setDoubleAttribute(String _name, double _value) {
        HierarchicalName tempHierarchicalName = this.getHierarchicalName(_name);
        SimpleReflectiveBean tempBean = this.traverseHierarchy(tempHierarchicalName);
        tempBean.setDoubleAttribute(tempHierarchicalName.name(), _value);
    }

    @Override
    public String getStringAttribute(String _name) {
        HierarchicalName tempHierarchicalName = this.getHierarchicalName(_name);
        SimpleReflectiveBean tempBean = this.traverseHierarchy(tempHierarchicalName);
        return tempBean.getStringAttribute(tempHierarchicalName.name());
    }

    @Override
    public void setStringAttribute(String _name, String _value) {
        HierarchicalName tempHierarchicalName = this.getHierarchicalName(_name);
        SimpleReflectiveBean tempBean = this.traverseHierarchy(tempHierarchicalName);
        tempBean.setStringAttribute(tempHierarchicalName.name(), _value);
    }

    @Override
    public Object getObjectAttribute(String _name) {
        HierarchicalName tempHierarchicalName = this.getHierarchicalName(_name);
        SimpleReflectiveBean tempBean = this.traverseHierarchy(tempHierarchicalName);
        return tempBean.getObjectAttribute(tempHierarchicalName.name());
    }

    @Override
    public void setObjectAttribute(String _name, Object _value) {
        HierarchicalName tempHierarchicalName = this.getHierarchicalName(_name);
        SimpleReflectiveBean tempBean = this.traverseHierarchy(tempHierarchicalName);
        tempBean.setObjectAttribute(tempHierarchicalName.name(), _value);
    }

    @Override
    public boolean isAttributeSet(String _name) {
        HierarchicalName tempHierarchicalName = this.getHierarchicalName(_name);
        SimpleReflectiveBean tempBean = this.traverseHierarchy(tempHierarchicalName);
        return tempBean.isAttributeSet(tempHierarchicalName.name());
    }

    @Override
    public void clear(String _name) {
        HierarchicalName tempHierarchicalName = this.getHierarchicalName(_name);
        SimpleReflectiveBean tempBean = this.traverseHierarchy(tempHierarchicalName);
        tempBean.clear(tempHierarchicalName.name());
    }

    @Override
    public void clear() {
        this.bean.clear();
    }
}
