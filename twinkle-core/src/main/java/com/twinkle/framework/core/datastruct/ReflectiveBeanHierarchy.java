package com.twinkle.framework.core.datastruct;

import com.twinkle.framework.core.datastruct.schema.HierarchicalName;

import java.util.NoSuchElementException;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2019-08-03 18:09<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public class ReflectiveBeanHierarchy extends SimpleReflectiveBeanHierarchy implements ReflectiveBean {
    public ReflectiveBeanHierarchy(ReflectiveBean _bean, String[] _arrayBracketArray) {
        super(_bean, _arrayBracketArray);
    }

    public ReflectiveBeanHierarchy(ReflectiveBean _bean) {
        super(_bean, HierarchicalName.DEFAULT_ARRAY_BRACKETS);
    }

    @Override
    protected SimpleReflectiveBean getBeanArrayElement(SimpleReflectiveBean _bean, HierarchicalName _hierarchicalName) {
        switch(_hierarchicalName.kind()) {
            case 0:
                return this.getListElement(_bean, _hierarchicalName);
            case 1:
            case 2:
                Object tempObjAttr = _bean.getObjectAttribute(_hierarchicalName.name());

                Object[] tempElementArray;
                try {
                    tempElementArray = (Object[])tempObjAttr;
                } catch (ClassCastException e) {
                    NoSuchElementException tempException = new NoSuchElementException(_hierarchicalName.next().toString());
                    tempException.initCause(e);
                    throw tempException;
                }

                try {
                    return (SimpleReflectiveBean)tempElementArray[_hierarchicalName.index()];
                } catch (ArrayIndexOutOfBoundsException e) {
                    ArrayIndexOutOfBoundsException tempException = new ArrayIndexOutOfBoundsException(_hierarchicalName.toString());
                    tempException.initCause(e);
                    throw tempException;
                }
            default:
                throw new UnsupportedOperationException("Unsupported array/list brackets: " + _hierarchicalName.toString());
        }
    }

    @Override
    public byte[] getByteArrayAttribute(String _name) {
        HierarchicalName tempHierarchicalName = this.getHierarchicalName(_name);
        ReflectiveBean tempBean = (ReflectiveBean)this.traverseHierarchy(tempHierarchicalName);
        return tempBean.getByteArrayAttribute(tempHierarchicalName.name());
    }

    @Override
    public void setByteArrayAttribute(String _name, byte[] _value) {
        HierarchicalName tempHierarchicalName = this.getHierarchicalName(_name);
        ReflectiveBean tempBean = (ReflectiveBean)this.traverseHierarchy(tempHierarchicalName);
        tempBean.setByteArrayAttribute(tempHierarchicalName.name(), _value);
    }

    @Override
    public short[] getShortArrayAttribute(String _name) {
        HierarchicalName tempHierarchicalName = this.getHierarchicalName(_name);
        ReflectiveBean tempBean = (ReflectiveBean)this.traverseHierarchy(tempHierarchicalName);
        return tempBean.getShortArrayAttribute(tempHierarchicalName.name());
    }

    @Override
    public void setShortArrayAttribute(String _name, short[] _value) {
        HierarchicalName tempHierarchicalName = this.getHierarchicalName(_name);
        ReflectiveBean tempBean = (ReflectiveBean)this.traverseHierarchy(tempHierarchicalName);
        tempBean.setShortArrayAttribute(tempHierarchicalName.name(), _value);
    }

    @Override
    public int[] getIntArrayAttribute(String _name) {
        HierarchicalName tempHierarchicalName = this.getHierarchicalName(_name);
        ReflectiveBean tempBean = (ReflectiveBean)this.traverseHierarchy(tempHierarchicalName);
        return tempBean.getIntArrayAttribute(tempHierarchicalName.name());
    }

    @Override
    public void setIntArrayAttribute(String _name, int[] _value) {
        HierarchicalName tempHierarchicalName = this.getHierarchicalName(_name);
        ReflectiveBean tempBean = (ReflectiveBean)this.traverseHierarchy(tempHierarchicalName);
        tempBean.setIntArrayAttribute(tempHierarchicalName.name(), _value);
    }

    @Override
    public long[] getLongArrayAttribute(String _name) {
        HierarchicalName tempHierarchicalName = this.getHierarchicalName(_name);
        ReflectiveBean tempBean = (ReflectiveBean)this.traverseHierarchy(tempHierarchicalName);
        return tempBean.getLongArrayAttribute(tempHierarchicalName.name());
    }

    @Override
    public void setLongArrayAttribute(String _name, long[] _value) {
        HierarchicalName tempHierarchicalName = this.getHierarchicalName(_name);
        ReflectiveBean tempBean = (ReflectiveBean)this.traverseHierarchy(tempHierarchicalName);
        tempBean.setLongArrayAttribute(tempHierarchicalName.name(), _value);
    }

    @Override
    public char[] getCharArrayAttribute(String _name) {
        HierarchicalName tempHierarchicalName = this.getHierarchicalName(_name);
        ReflectiveBean tempBean = (ReflectiveBean)this.traverseHierarchy(tempHierarchicalName);
        return tempBean.getCharArrayAttribute(tempHierarchicalName.name());
    }

    @Override
    public void setCharArrayAttribute(String _name, char[] _value) {
        HierarchicalName tempHierarchicalName = this.getHierarchicalName(_name);
        ReflectiveBean tempBean = (ReflectiveBean)this.traverseHierarchy(tempHierarchicalName);
        tempBean.setCharArrayAttribute(tempHierarchicalName.name(), _value);
    }

    @Override
    public boolean[] getBooleanArrayAttribute(String _name) {
        HierarchicalName tempHierarchicalName = this.getHierarchicalName(_name);
        ReflectiveBean tempBean = (ReflectiveBean)this.traverseHierarchy(tempHierarchicalName);
        return tempBean.getBooleanArrayAttribute(tempHierarchicalName.name());
    }

    @Override
    public void setBooleanArrayAttribute(String _name, boolean[] _value) {
        HierarchicalName tempHierarchicalName = this.getHierarchicalName(_name);
        ReflectiveBean tempBean = (ReflectiveBean)this.traverseHierarchy(tempHierarchicalName);
        tempBean.setBooleanArrayAttribute(tempHierarchicalName.name(), _value);
    }

    @Override
    public float[] getFloatArrayAttribute(String _name) {
        HierarchicalName tempHierarchicalName = this.getHierarchicalName(_name);
        ReflectiveBean tempBean = (ReflectiveBean)this.traverseHierarchy(tempHierarchicalName);
        return tempBean.getFloatArrayAttribute(tempHierarchicalName.name());
    }

    @Override
    public void setFloatArrayAttribute(String _name, float[] _value) {
        HierarchicalName tempHierarchicalName = this.getHierarchicalName(_name);
        ReflectiveBean tempBean = (ReflectiveBean)this.traverseHierarchy(tempHierarchicalName);
        tempBean.setFloatArrayAttribute(tempHierarchicalName.name(), _value);
    }

    @Override
    public double[] getDoubleArrayAttribute(String _name) {
        HierarchicalName tempHierarchicalName = this.getHierarchicalName(_name);
        ReflectiveBean tempBean = (ReflectiveBean)this.traverseHierarchy(tempHierarchicalName);
        return tempBean.getDoubleArrayAttribute(tempHierarchicalName.name());
    }

    @Override
    public void setDoubleArrayAttribute(String _name, double[] _value) {
        HierarchicalName tempHierarchicalName = this.getHierarchicalName(_name);
        ReflectiveBean tempBean = (ReflectiveBean)this.traverseHierarchy(tempHierarchicalName);
        tempBean.setDoubleArrayAttribute(tempHierarchicalName.name(), _value);
    }

    @Override
    public String[] getStringArrayAttribute(String _name) {
        HierarchicalName tempHierarchicalName = this.getHierarchicalName(_name);
        ReflectiveBean tempBean = (ReflectiveBean)this.traverseHierarchy(tempHierarchicalName);
        return tempBean.getStringArrayAttribute(tempHierarchicalName.name());
    }

    @Override
    public void setStringArrayAttribute(String _name, String[] _value) {
        HierarchicalName tempHierarchicalName = this.getHierarchicalName(_name);
        ReflectiveBean tempBean = (ReflectiveBean)this.traverseHierarchy(tempHierarchicalName);
        tempBean.setStringArrayAttribute(tempHierarchicalName.name(), _value);
    }

    @Override
    public Object[] getObjectArrayAttribute(String _name) {
        HierarchicalName tempHierarchicalName = this.getHierarchicalName(_name);
        ReflectiveBean tempBean = (ReflectiveBean)this.traverseHierarchy(tempHierarchicalName);
        return tempBean.getObjectArrayAttribute(tempHierarchicalName.name());
    }
    @Override
    public void setObjectArrayAttribute(String _name, Object[] _value) {
        HierarchicalName tempHierarchicalName = this.getHierarchicalName(_name);
        ReflectiveBean tempBean = (ReflectiveBean)this.traverseHierarchy(tempHierarchicalName);
        tempBean.setObjectArrayAttribute(tempHierarchicalName.name(), _value);
    }
}
