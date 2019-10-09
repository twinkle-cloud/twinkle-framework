package com.twinkle.framework.core.lang;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.util.*;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     10/8/19 10:26 PM<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
@Slf4j
public class RangeAttribute extends IntegerAttribute implements IBinaryAttribute {
    private static int type = 8;
    @Getter @Setter
    private int initialValue;
    @Getter @Setter
    private int finalValue;
    @Getter
    private int lastKey;
    @Getter
    private int lastValue;
    private transient TreeMap<Integer, Integer> sortedMap;
    private Vector<Map<Integer, Integer>> rangeVec;

    public RangeAttribute() {
        this.initialValue = -2147483648;
        this.finalValue = 2147483647;
        this.lastKey = 0;
        this.lastValue = 0;
        this.rangeVec = new Vector<>(1, 1);
        this.rangeVec.add(new HashMap<>());
        this.sortedMap = new TreeMap<>();
    }

    public RangeAttribute(int _minValue, int _maxValue) {
        this();
        this.add(_minValue, _maxValue);
    }

    @Override
    public byte[] getByteArray() {
        ByteArrayOutputStream tempStream = new ByteArrayOutputStream();
        DataOutputStream tempDataStream = new DataOutputStream(tempStream);

        try {
            tempDataStream.writeInt(100800921);
            int tempRangeSize = this.rangeVec.size();
            tempDataStream.writeInt(tempRangeSize);

            for(int i = 0; i < tempRangeSize; ++i) {
                HashMap tempMap = (HashMap)this.rangeVec.get(i);
                int tempMapSize = tempMap.size();
                tempDataStream.writeInt(tempMapSize);
                Iterator tempItr = tempMap.entrySet().iterator();

                for(int j = 0; j < tempMapSize; ++j) {
                    Map.Entry tempEntry = (Map.Entry)tempItr.next();
                    tempDataStream.writeInt((Integer)tempEntry.getKey());
                    tempDataStream.writeInt((Integer)tempEntry.getValue());
                }
            }

            tempDataStream.writeInt(this.initialValue);
            tempDataStream.writeInt(this.finalValue);
            tempDataStream.writeInt(this.lastKey);
            tempDataStream.writeInt(this.lastValue);
            return tempStream.toByteArray();
        } catch (IOException e) {
            log.error("Encountered unexpected error while convert to byte array. Exception: {}", e);
            throw new RuntimeException(e);
        } finally {
            try {
                tempDataStream.close();
            } catch (IOException e) {
            }

        }
    }

    @Override
    public void setValue(byte[] _value) {
        this.rangeVec.clear();
        this.sortedMap = null;
        ByteArrayInputStream tempByteInputStream = new ByteArrayInputStream(_value);
        DataInputStream tempDataInputStream = new DataInputStream(tempByteInputStream);

        try {
            tempDataInputStream.readInt();
            //the next four bytes of this input stream
            int tempValue = tempDataInputStream.readInt();

            for(int i = 0; i < tempValue; i++) {
                HashMap tempMap = new HashMap();
                this.rangeVec.add(tempMap);
                int tempRangeSize = tempDataInputStream.readInt();

                for(int j = 0; j < tempRangeSize; j++) {
                    int tempMinValue = tempDataInputStream.readInt();
                    int tempMaxValue = tempDataInputStream.readInt();
                    tempMap.put(new Integer(tempMinValue), new Integer(tempMaxValue));
                }
            }

            this.initialValue = tempDataInputStream.readInt();
            this.finalValue = tempDataInputStream.readInt();
            this.lastKey = tempDataInputStream.readInt();
            this.lastValue = tempDataInputStream.readInt();
        } catch (IOException e) {
            log.error("Encountered unexpected error while convert byte array to Range. Exception: {}", e);
            throw new RuntimeException(e);
        } finally {
            try {
                tempDataInputStream.close();
            } catch (IOException e) {
            }

        }
    }

    public int getType() {
        return type;
    }

    public void setType(int _type) {
        type = _type;
    }

    public static int getTypeID() {
        return type;
    }

    @Override
    public int getPrimitiveType() {
        return BYTE_ARRAY_TYPE;
    }

    /**
     * Add range into the range maps.
     *
     * @param _minValue
     * @param _maxValue
     * @return
     */
    public boolean add(int _minValue, int _maxValue) {
        boolean tempResultFlag = false;
        Integer tempMinValue = new Integer(_minValue);
        Map<Integer, Integer> tempRangeMap = null;
        int tempIndex = 0;
        int tempRangeSize = this.rangeVec.size();
        if (tempRangeSize > 0) {
            tempRangeMap = this.rangeVec.get(tempRangeSize - 1);
            tempIndex = tempRangeSize - 1;
            Integer tempMaxValue = tempRangeMap.get(tempMinValue);
            if (tempMaxValue != null) {
                if (tempMaxValue == _maxValue) {
                    return tempResultFlag;
                }

                tempRangeMap = new HashMap();
                this.rangeVec.add(tempRangeMap);
                tempResultFlag = true;
            }
        }

        if (tempIndex == 0) {
            if (this.sortedMap == null) {
                this.sortedMap = new TreeMap<>(tempRangeMap);
            }

            this.sortedMap.put(tempMinValue, new Integer(_maxValue));
            log.debug("RangeAttribute.add(). SortedMap is: {} ", this.sortedMap);
        }

        tempRangeMap.put(tempMinValue, new Integer(_maxValue));
        this.lastKey = _minValue;
        this.lastValue = _maxValue;
        return tempResultFlag;
    }

    /**
     * Check the range with given range's min value exists in the range or not?
     *
     * @param _minValue
     * @return
     */
    public boolean contains(int _minValue) {
        if (this.rangeVec.size() == 1) {
            Map<Integer, Integer> tempRangeMap = (HashMap)this.rangeVec.get(0);
            if (tempRangeMap.get(new Integer(_minValue)) != null) {
                return true;
            }
        }

        return false;
    }

    /**
     * Check the given range [_minValue, _maxValue] is contained in the ranges or not?
     *
     * @param _minValue
     * @param _maxValue
     * @return
     */
    public boolean contains(int _minValue, int _maxValue) {
        Integer tempMinValue = new Integer(_minValue);
        Map<Integer, Integer> tempRangeMap = null;

        for(int i = this.rangeVec.size(); i > 0; --i) {
            tempRangeMap = (HashMap)this.rangeVec.get(i - 1);
            Integer tempMaxValue = tempRangeMap.get(tempMinValue);
            if (tempMaxValue != null && tempMaxValue == _maxValue) {
                return true;
            }
        }

        return false;
    }

    /**
     * Check the value is contained in an range or not with delta range.
     * Such as:
     *   _value = 20, _delta =2
     *   Range:[19, 30]
     *   19-2 < 20
     *   19+2 > 20
     *   it means the value is contained with range [19, 30].
     * @param _value
     * @param _delta
     * @return
     */
    public boolean containsWithDelta(int _value, int _delta) {
        Map<Integer, Integer> tempRangeMap = this.rangeVec.get(0);
        if (this.sortedMap == null) {
            this.sortedMap = new TreeMap<>(tempRangeMap);
        }

        Iterator<Integer> tempMapItr = this.sortedMap.keySet().iterator();
        while(tempMapItr.hasNext()) {
            int tempMinValueKey = tempMapItr.next();
            if (tempMinValueKey - _delta <= _value && tempMinValueKey + _delta >= _value) {
                log.debug("RangeAttribute.containsWithDelta: key " + _value + " is in the range " + (tempMinValueKey - _delta) + "," + (tempMinValueKey + _delta));
                return true;
            }
            log.debug("RangeAttribute.containsWithDelta: key " + _value + " is NOT in the range " + (tempMinValueKey - _delta) + "," + (tempMinValueKey + _delta));
        }

        return false;
    }

    /**
     * Check the given Range [InitialValue, FinalValue] is contained in the ranges or not?
     *
     * @return
     */
    public boolean isComplete() {
        if (this.rangeVec.size() <= 1 && this.initialValue != -2147483648 && this.finalValue != 2147483647) {
            int tempInitialMinValue = -2147483648;
            Map<Integer, Integer> tempRangeMap = this.rangeVec.get(0);
            for(int i = this.initialValue; i <= this.finalValue; ++i) {
                Integer tempMaxValue = tempRangeMap.get(new Integer(i));
                if (tempMaxValue == null || tempMaxValue < tempInitialMinValue) {
                    return false;
                }
                tempInitialMinValue = tempMaxValue;
            }
            return true;
        } else {
            return false;
        }
    }

    public boolean isComplete(int _value) {
        if (this.rangeVec.size() <= 1 && this.initialValue != -2147483648 && this.finalValue != 2147483647) {
            Map<Integer, Integer> tempRangeMap = this.rangeVec.get(0);
            if (this.sortedMap == null) {
                this.sortedMap = new TreeMap<>(tempRangeMap);
            }
            log.debug("RangeAttribute.isComplete: SortedMap is " + this.sortedMap);
            //
            Object[] tempKeyArray = this.sortedMap.keySet().toArray();
            for(int i = 1; i < tempKeyArray.length; i++) {
                int tempRangeMaxValue = tempRangeMap.get(tempKeyArray[i - 1]);
                int tempMinValueKey = (Integer)tempKeyArray[i];
                if (tempRangeMaxValue - _value > tempMinValueKey || tempRangeMaxValue + _value < tempMinValueKey) {
                    log.debug("RangeAttribute.isComplete: current " + tempMinValueKey + " is NOT in the range " + (tempRangeMaxValue - _value) + "," + (tempRangeMaxValue + _value));
                    return false;
                }
                log.debug("RangeAttribute.isComplete: current " + tempMinValueKey + " is in the range " + (tempRangeMaxValue - _value) + "," + (tempRangeMaxValue + _value));
            }

            return true;
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        StringBuilder tempBuilder = new StringBuilder();
        for(int i = 0; i < this.rangeVec.size(); i++) {
            if (i != 0) {
                tempBuilder.append(",");
            }
            tempBuilder.append(this.rangeVec.get(i).toString());
        }

        tempBuilder.append(",Sorted=");
        tempBuilder.append(this.sortedMap);
        return tempBuilder.toString();
    }

    @Override
    public Object clone() {
        RangeAttribute tempAttr = new RangeAttribute();
        tempAttr.setValue(this);
        return tempAttr;
    }
    @Override
    public void setValue(Attribute _attr) {
        if (this != _attr) {
            RangeAttribute tempAttr = (RangeAttribute)_attr;
            this.initialValue = tempAttr.initialValue;
            this.finalValue = tempAttr.finalValue;
            if (tempAttr.rangeVec != null) {
                this.rangeVec = (Vector)tempAttr.rangeVec.clone();
            }

            this.lastKey = tempAttr.lastKey;
            this.lastValue = tempAttr.lastValue;
            if (tempAttr.sortedMap != null) {
                this.sortedMap = (TreeMap)tempAttr.sortedMap.clone();
            }

        }
    }

    @Override
    public Object getObjectValue() {
        return this.getByteArray();
    }

    public static void main(String[] var0) {
        RangeAttribute tempAttr = new RangeAttribute(10, 1000);
        tempAttr.add(11, 1100);
        tempAttr.add(17, 1700);
        tempAttr.add(19, 1900);
        tempAttr.add(13, 1300);
        tempAttr.add(8, 800);
        tempAttr.add(9, 900);
        tempAttr.add(20, 2000);
        tempAttr.add(12, 1200);
        if (tempAttr.contains(10, 1000) & tempAttr.contains(13, 1300) & tempAttr.contains(20, 2000)) {
            System.out.println("good");
        } else {
            System.out.println("bad");
        }

        if (!tempAttr.contains(21, 1200) & !tempAttr.contains(16, 1600) & !tempAttr.contains(18, 1700)) {
            System.out.println("good");
        } else {
            System.out.println("bad");
        }

        System.out.println(tempAttr.toString());
        tempAttr.setInitialValue(8);
        tempAttr.setFinalValue(20);
        if (!tempAttr.isComplete()) {
            System.out.println("good");
        } else {
            System.out.println("bad");
        }

        tempAttr.add(14, 1400);
        tempAttr.add(15, 1500);
        tempAttr.add(16, 1600);
        tempAttr.add(18, 1800);
        if (tempAttr.isComplete()) {
            System.out.println("Great");
        } else {
            System.out.println("something bad huh...");
        }

        RangeAttribute tempAttr2 = new RangeAttribute(10, 20);
        tempAttr2.add(21, 30);
        tempAttr2.add(30, 41);
        tempAttr2.add(40, 50);
        tempAttr2.add(63, 70);
        System.out.println("Range is " + tempAttr2);
        System.out.println("\nCheck 1 -------------------------");
        if (tempAttr2.containsWithDelta(20, 2)) {
            System.out.println("Ok");
        } else {
            System.out.println("Not Ok");
        }

        System.out.println("---------------------------------");
        System.out.println("\nCheck 2 -------------------------");
        if (tempAttr2.contains(30, 41)) {
            System.out.println("Ok");
        } else {
            System.out.println("Not Ok");
        }

        System.out.println("---------------------------------");
        tempAttr2.setInitialValue(21);
        tempAttr2.setFinalValue(60);
        System.out.println("Range is " + tempAttr2);
        System.out.println("\nCheck 3 -------------------------");
        if (tempAttr2.isComplete(2)) {
            System.out.println("Not Ok");
        } else {
            System.out.println("Ok");
        }

        System.out.println("---------------------------------");
        tempAttr2.add(51, 60);
        System.out.println("Range is " + tempAttr2);
        System.out.println("\nCheck 4 -------------------------");
        if (tempAttr2.isComplete(2)) {
            System.out.println("Not Ok");
        } else {
            System.out.println("Ok");
        }

        System.out.println("---------------------------------");
        System.out.println("\nCheck 5 -------------------------");
        if (tempAttr2.isComplete(4)) {
            System.out.println("Ok");
        } else {
            System.out.println("Not Ok");
        }

        System.out.println("---------------------------------");
    }

}
