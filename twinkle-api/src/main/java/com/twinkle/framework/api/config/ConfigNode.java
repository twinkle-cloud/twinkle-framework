package com.twinkle.framework.api.config;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2019-07-10 11:11<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
@Slf4j
@Data
public class ConfigNode implements Serializable {
    /**
     * Config Node ID.
     */
    private Integer id;
    /**
     * Config Node's Name.
     */
    private String name;
    /**
     * Then Path Name, will be used as bean name.
     */
    private String pathName;

    /**
     * The attribute-values of this node.
     */
    private Map<String, List[]> attributesMap;

    /**
     * The sub-nodes of this node.
     */
    private Map<String, ConfigNode> configNodeMap;

    /**
     * The parent node of this node.
     */
    private ConfigNode parentNode;


}
