package main.java.com.epam.project4.manager;

import java.util.Map;

/**
 * @param <K> key for value instancing from manager
 * @param <E> target type, which subtypes will be returned by {@link #getInstance(Object)}
 * @param <T> special intermediate type, from which element of target type can be get (can be the same as target type)
 * @author Oleh Kakherskyi, IP-31, FICT, NTUU "KPI", olehkakherskiy@gmail.com
 */
public abstract class GenericManager<K, E, T> {

    protected Map<K, T> keyObjectTemplateMap;

    public GenericManager(Map<K, T> keyObjectTemplateMap) {
        this.keyObjectTemplateMap = keyObjectTemplateMap;
    }

    public abstract <V extends E> V getInstance(K key);
}