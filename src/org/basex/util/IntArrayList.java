package org.basex.util;

/**
 * This is a simple container for native int array values.
 *
 * @author Workgroup DBIS, University of Konstanz 2005-08, ISC License
 * @author Christian Gruen
 */
public final class IntArrayList {
  /** Value array. */
  public int[][] list = new int[8][];
  /** Current array size. */
  public int size;
  /** Current used buckets - SPECIAL COUNT, NOT GENERAL. **/
  public int nb = 0;
  /** Used for special count of nb. **/
  private boolean sc = false;
  /** Flag for found values in list. **/
  public boolean found = false;
  /**
   * Default constructor.
   */
  public IntArrayList() { }

  /**
   * Constructor with use of special count.
   * @param specialCount enable/disable special count.
   */
  public IntArrayList(final boolean specialCount) { 
    sc = specialCount;
  }

  /**
   * Constructor with use of special count.
   * @param s initial size of list.
   */
  public IntArrayList(final int s) { 
    list = new int[s][];
  }
  
  /**
   * Adds next value.
   * @param v value to be added
   */
  public void add(final int[] v) {
    if(size == list.length) list = Array.extend(list);
    list[size++] = v;
    if(sc) nb += v[0] + v[v[0] + 2] * 8 + 4;
      else nb += v.length;
  }
  
  /**
   * Adds next value.
   * @param v value to be added
   * @param index int index where to add v
   */
  public void addAt(final int[] v, final int index) {
    if(size == list.length) list = Array.extend(list);
    System.arraycopy(list, index, list, index + 1, size - index);
    list[index] = v;
    size++;
    if(sc) nb += v[0] + v[v[0] + 2] * 8 + 4;
      else nb += v.length;
  }
  
  /**
   * Adds next value.
   * @param v value to be added
   * @param keylength length of the key
   * @return index of added value
   */
 public int addSorted(final int[] v, final int keylength) {
    found = false;
    
    // find inserting position
    int l = 0, r = size, m = size / 2;
    int res = -1;
    if (r > 1) {
    while (r > l) {
      m = (r + l) / 2;
      res = cmp(v, list[m], keylength);
      if (res == -1) r = m - 1;
      else if (res == 1) l = m + 1;
      else {
        found = true;
        return m;
      }
    } 
    }
    
    if (l < size) { 
      res = cmp(v, list[l], keylength);
    
      if (res == 0) {
        found = true;
        return l;
      } else {
        if (res == 1) {
          // v > list[m]
          l++;
        } else {
          // v < list[m]
          //m--;  
        }
      }
    }
      if(size == list.length) {
        final int[][] tmp = new int[size << 1][];
        System.arraycopy(list, 0, tmp, 0, l);
        tmp[l] = new int[v.length + 1];
        System.arraycopy(v, 0, tmp[l], 0, v.length);
        tmp[l][v.length] = 1;
        if (size - l > 0) 
          System.arraycopy(list, l, tmp, l + 1, size - l);
        list = tmp;
        size++;  
      } else {
        System.arraycopy(list, l, list, l + 1, size - l);
        list[l] = new int[v.length + 1];
        System.arraycopy(v, 0, list[l], 0, v.length);
        list[l][v.length] = 1;
        size++;
      }
      return l;
    
  }
  
  /**
   * Compares two int[] at spezified length.
   * returns -1 if v1 > v2
   *         1  if v1 < v2
   *         0  if v1 = v2
   * @param v1 value1
   * @param v2 value2 
   * @param kl number of ints to compare
   * @return int result
   */
  public int cmp(final int[] v1, final int[] v2, final int kl) {
    if (kl > v1.length || kl > v2.length) return -2;
    
    for (int i = 0; i < kl; i++) {
      if (v1[i] > v2[i]) return 1;
      if (v1[i] < v2[i]) return -1;
    }
    
    return 0;
  }
  

  /**
   * Finishes the int array.
   * @return int array
   */
  public int[][] finish() {
    return size == list.length ? list : Array.finish(list, size);
  }

  /**
   * Resets the integer list.
   */
  public void reset() {
    size = 0;
    nb = 0;
  }
}
