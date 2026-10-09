package com.itselix99.betterworldoptions.util;

class Long2ObjectHashMapEntry {
    final long key;
    Object value;
    Long2ObjectHashMapEntry next;
    final int hash;

    Long2ObjectHashMapEntry(int hash, long key, Object value, Long2ObjectHashMapEntry next) {
        this.value = value;
        this.next = next;
        this.key = key;
        this.hash = hash;
    }

    public final long getKey() {
        return this.key;
    }

    public final Object getValue() {
        return this.value;
    }

    public final boolean equals(Object object) {
        if (!(object instanceof Long2ObjectHashMapEntry)) {
            return false;
        } else {
            Long2ObjectHashMapEntry var2 = (Long2ObjectHashMapEntry)object;
            Long var3 = this.getKey();
            Long var4 = var2.getKey();
            if (var3 == var4 || var3 != null && var3.equals(var4)) {
                Object var5 = this.getValue();
                Object var6 = var2.getValue();
                if (var5 == var6 || var5 != null && var5.equals(var6)) {
                    return true;
                }
            }

            return false;
        }
    }

    public final int hashCode() {
        return Long2ObjectHashMap.synthHash(this.key);
    }

    public final String toString() {
        return this.getKey() + "=" + this.getValue();
    }
}