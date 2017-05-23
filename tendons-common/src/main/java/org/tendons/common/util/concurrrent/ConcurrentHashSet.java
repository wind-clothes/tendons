package org.tendons.common.util.concurrrent;

import java.io.Serializable;
import java.util.AbstractSet;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author: xcw
 * @date:2017年5月23日 下午12:44:21
 */
public class ConcurrentHashSet<E> extends AbstractSet<E> implements Serializable, Set<E> {

  private static final long serialVersionUID = 7207711008349667925L;

  private static final Object PRESENT = new Object();

  private final ConcurrentHashMap<E, Object> map;

  public ConcurrentHashSet() {
    this.map = new ConcurrentHashMap<>();
  }

  public ConcurrentHashSet(int initialCapacity) {
    this.map = new ConcurrentHashMap<>(initialCapacity);
  }

  public ConcurrentHashSet(Collection<? extends E> c) {
    this.map = new ConcurrentHashMap<>();
    addAll(c);
  }

  @Override
  public Iterator<E> iterator() {
    return map.keySet().iterator();
  }

  @Override
  public int size() {
    return map.size();
  }

  @Override
  public boolean contains(Object o) {
    return map.containsKey(o);
  }

  @Override
  public boolean add(E e) {
    return map.put(e, PRESENT) == null;
  }

  @Override
  public boolean remove(Object o) {
    return map.remove(o) == PRESENT;
  }

  @Override
  public boolean isEmpty() {
    return map.isEmpty();
  }

  @Override
  public void clear() {
    map.clear();
  }

}
