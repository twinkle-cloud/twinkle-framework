package com.twinkle.framework.ruleengine.utils;

import com.twinkle.framework.api.constant.ExceptionCode;
import com.twinkle.framework.api.exception.ConfigurationException;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2019-07-18 14:40<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
@Slf4j
public class MapHash {
    private static Map mapHolder = new ConcurrentHashMap();
    private Map mapHash = null;
    private String[] mapArray = null;

    public MapHash(Map _mapHash, String[] _mapArray) {
        this.mapHash = _mapHash;
        this.mapArray = _mapArray;
    }

    public Map getMapHash() {
        return this.mapHash;
    }

    public String[] getMapArray() {
        return this.mapArray;
    }

    public static MapHash createMapHash(String _url, String[] _urlComments, boolean _useArrayFlag, boolean _refreshFalg, String _seperator) throws ConfigurationException {
        if (_url == null) {
            return null;
        } else {
            MapHash tempMapHash = (MapHash) mapHolder.get(_url);
            if (tempMapHash != null && !_refreshFalg) {
                return tempMapHash;
            } else {
                LineNumberReader tempLineReader = null;

                try {
                    URL tempURL = new URL(_url);
                    tempLineReader = new LineNumberReader(new InputStreamReader(tempURL.openStream()));
                } catch (MalformedURLException e) {
                    throw new ConfigurationException(ExceptionCode.RULE_ADN_INVALID_URL, "AdornmentRule.configure(): Invalid URL (" + _url + ") - " + e);
                } catch (IOException ex) {
                    throw new ConfigurationException(ExceptionCode.RULE_ADN_URL_READ_FAILED, "AdornmentRule.configure(): Cannot read from specified URL (" + _url + ") - " + ex);
                }

                String tempLine = "";
                String[] tempValueArray = null;
                Map<String, String> tempMap = new ConcurrentHashMap<>();
                boolean isCommentFlag = false;

                int tempIndex;
                try {
                    while((tempLine = tempLineReader.readLine()) != null) {
                        tempLine = tempLine.trim();
                        if (!tempLine.equals("")) {
                            isCommentFlag = false;
                            if (_urlComments != null) {
                                for(tempIndex = 0; tempIndex < _urlComments.length; ++tempIndex) {
                                    if (tempLine.startsWith(_urlComments[tempIndex])) {
                                        log.debug("Skipping line " + _url + ":" + tempLineReader.getLineNumber() + " - " + tempLine);
                                        isCommentFlag = true;
                                        break;
                                    }
                                }
                            }

                            if (!isCommentFlag) {
                                StringTokenizer tempST = null;
                                if (_seperator == null) {
                                    tempST = new StringTokenizer(tempLine);
                                } else {
                                    tempST = new StringTokenizer(tempLine, _seperator);
                                }

                                String tempKeyToken = tempST.nextToken();
                                String tempValueToken = tempST.nextToken("\n\r");
                                if (_seperator != null) {
                                    tempValueToken = tempValueToken.substring(_seperator.length());
                                }

                                tempMap.put(tempKeyToken, tempValueToken.trim());
                            }
                        }
                    }
                } catch (NoSuchElementException e) {
                    throw new ConfigurationException(ExceptionCode.RULE_ADN_URL_TOKEN_MISSED, "Missing token on line " + _url + ":" + tempLineReader.getLineNumber() + " - " + tempLine);
                } catch (Exception ex) {
                    throw new ConfigurationException(ExceptionCode.RULE_ADN_URL_PARSE_FAILED, "Error parsing token on line " + _url + ":" + tempLineReader.getLineNumber() + " - " + tempLine + "\n" + ex);
                }

                if (_useArrayFlag) {
                    try {
                        tempIndex = 32767;
                        int tempMinIndex = -32768;
                        Iterator<String> tempKeySet = tempMap.keySet().iterator();

                        while(tempKeySet.hasNext()) {
                            int tempIntKey = Integer.parseInt(tempKeySet.next());
                            if (tempIntKey < tempIndex) {
                                tempIndex = tempIntKey;
                            }

                            if (tempIntKey > tempMinIndex) {
                                tempMinIndex = tempIntKey;
                            }
                        }

                        if (tempIndex >= 0 && tempMinIndex <= 32767) {
                            log.debug("Using an array to hold mappings from " + _url);
                            tempValueArray = new String[tempMinIndex + 1];

                            int tempIntKey;
                            String tempIntKeyStr;
                            for(tempKeySet = tempMap.keySet().iterator(); tempKeySet.hasNext(); tempValueArray[tempIntKey] = tempMap.get(tempIntKeyStr)) {
                                tempIntKeyStr = tempKeySet.next();
                                tempIntKey = Integer.parseInt(tempIntKeyStr);
                            }

                            tempMap = null;
                        }
                    } catch (NumberFormatException e) {
                    }
                }

                tempMapHash = new MapHash(tempMap, tempValueArray);
                mapHolder.put(_url, tempMapHash);
                return tempMapHash;
            }
        }
    }
}
