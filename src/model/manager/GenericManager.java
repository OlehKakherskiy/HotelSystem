package model.manager;

import java.util.Map;

/**
 * @author Oleh Kakherskyi, IP-31, FICT, NTUU "KPI", olehkakherskiy@gmail.com
 */
public abstract class GenericManager<K, E> {

    Map<K, Class<? extends E>> keyObjectTemplateMap;

    public GenericManager(Map<K, Class<? extends E>> keyObjectTemplateMap) {
        this.keyObjectTemplateMap = keyObjectTemplateMap;
    }

    public abstract <V extends E> V getObject(K key);
}
