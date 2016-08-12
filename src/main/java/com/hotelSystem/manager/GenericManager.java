package main.java.com.hotelSystem.manager;

import java.util.HashMap;
import java.util.Map;

/**
 * Class represents root of manager's hierarchy. Manager can manage a lifecycle
 * of specific type hierarchies.
 * <p>
 * Manager can instantiate object of target type, that can be identified by specific key. Also for instantiating
 * manager can need some extra information, that can be stored in special objects of intermediate type. If manager
 * don't need any additional info for instantiation, intermediate type can be the same as target type.
 * </p>
 *
 * @param <K> key for value instancing from manager
 * @param <E> target type, which subtypes will be returned by {@link #getInstance(Object)}
 * @param <T> special intermediate type, from which element of target type can
 *            be get (can be the same as target type)
 * @author Oleh Kakherskyi, IP-31, FICT, NTUU "KPI", olehkakherskiy@gmail.com
 */
abstract class GenericManager<K, E, T> {

    /**
     * contains mapping represented as key/instantiation_extra_info
     */
    protected Map<K, T> keyObjectTemplateMap;

    /**
     * constructor, that inits {@link #keyObjectTemplateMap}.
     *
     * @param keyObjectTemplateMap key/extra_info for instantiation target object map
     */
    public GenericManager(Map<K, T> keyObjectTemplateMap) {
        if (keyObjectTemplateMap == null) {
            this.keyObjectTemplateMap = new HashMap<>();
        } else {
            this.keyObjectTemplateMap = keyObjectTemplateMap;
        }
    }

    /**
     * Instantiate the object of subclass or equal class V,
     * that is mapped to the key parameter
     *
     * @param key key object, whose mapped type instance should be instantiate.
     * @param <V> target instance type (can be equal as <E> or a subclass of it)
     * @return instance of type <V>, which type is mapped to key parameter
     */
    public abstract <V extends E> V getInstance(K key);
}