package com.itselix99.betterworldoptions.util;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

public class Long2ObjectHashMap {
    private transient Long2ObjectHashMapEntry[] entries = new Long2ObjectHashMapEntry[16];
    private transient int size;
    private int capacity = 12;
    private final float loadFactor = 0.75F;
    private transient volatile int modCount;

    private static int hash(long key) {
        return hash((int)(key ^ key >>> 32));
    }

    private static int hash(int key) {
        key ^= key >>> 20 ^ key >>> 12;
        return key ^ key >>> 7 ^ key >>> 4;
    }

    private static int indexOf(int hash, int arrayLength) {
        return hash & arrayLength - 1;
    }

    @Environment(EnvType.CLIENT)
    public int size() {
        return this.size;
    }

    public Object get(long key) {
        int var3 = hash(key);

        for(Long2ObjectHashMapEntry var4 = this.entries[indexOf(var3, this.entries.length)]; var4 != null; var4 = var4.next) {
            if (var4.key == key) {
                return var4.value;
            }
        }

        return null;
    }

    public boolean contains(long key) {
        return this.getEntry(key) != null;
    }

    final Long2ObjectHashMapEntry getEntry(long key) {
        int var3 = hash(key);

        for(Long2ObjectHashMapEntry var4 = this.entries[indexOf(var3, this.entries.length)]; var4 != null; var4 = var4.next) {
            if (var4.key == key) {
                return var4;
            }
        }

        return null;
    }

    public void put(long key, Object value) {
        int var4 = hash(key);
        int var5 = indexOf(var4, this.entries.length);

        for(Long2ObjectHashMapEntry var6 = this.entries[var5]; var6 != null; var6 = var6.next) {
            if (var6.key == key) {
                var6.value = value;
            }
        }

        ++this.modCount;
        this.addEntry(var4, key, value, var5);
    }

    private void resize(int size) {
        Long2ObjectHashMapEntry[] var2 = this.entries;
        int var3 = var2.length;
        if (var3 == 1073741824) {
            this.capacity = Integer.MAX_VALUE;
        } else {
            Long2ObjectHashMapEntry[] var4 = new Long2ObjectHashMapEntry[size];
            this.transfer(var4);
            this.entries = var4;
            this.capacity = (int) ((float)size * this.loadFactor);
        }
    }

    private void transfer(Long2ObjectHashMapEntry[] entryArray) {
        Long2ObjectHashMapEntry[] var2 = this.entries;
        int var3 = entryArray.length;

        for(int var4 = 0; var4 < var2.length; ++var4) {
            Long2ObjectHashMapEntry var5 = var2[var4];
            if (var5 != null) {
                var2[var4] = null;

                while(true) {
                    Long2ObjectHashMapEntry var6 = var5.next;
                    int var7 = indexOf(var5.hash, var3);
                    var5.next = entryArray[var7];
                    entryArray[var7] = var5;
                    var5 = var6;
                    if (var6 == null) {
                        break;
                    }
                }
            }
        }

    }

    public Object remove(long key) {
        Long2ObjectHashMapEntry var3 = this.removeEntry(key);
        return var3 == null ? null : var3.value;
    }

    final Long2ObjectHashMapEntry removeEntry(long key) {
        int var3 = hash(key);
        int var4 = indexOf(var3, this.entries.length);
        Long2ObjectHashMapEntry var5 = this.entries[var4];

        Long2ObjectHashMapEntry var6;
        Long2ObjectHashMapEntry var7;
        for(var6 = var5; var6 != null; var6 = var7) {
            var7 = var6.next;
            if (var6.key == key) {
                ++this.modCount;
                --this.size;
                if (var5 == var6) {
                    this.entries[var4] = var7;
                } else {
                    var5.next = var7;
                }

                return var6;
            }

            var5 = var6;
        }

        return var6;
    }

    private void addEntry(int hash, long key, Object value, int index) {
        Long2ObjectHashMapEntry var6 = this.entries[index];
        this.entries[index] = new Long2ObjectHashMapEntry(hash, key, value, var6);
        if (this.size++ >= this.capacity) {
            this.resize(2 * this.entries.length);
        }

    }

    static int synthHash(long var0) {
        return hash(var0);
    }
}