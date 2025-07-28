package FirstWeek.July26SelfLearning.SimpleDatabaseRevised;

import java.io.Serializable;

public class DataEntry<V> implements Serializable {
    private final V value;
    private final int ttl;
    private final long mills;

    public DataEntry(V value, int ttl, long mills) {
        this.value = value;
        this.ttl = ttl;
        this.mills = mills;
    }

    public V getValue() {
        if(isExpired()){
            return null;
        }
        return value;
    }

    public long getExpiryTime() {
        return mills;
    }

    public int getTtl() {
        return ttl;
    }

    public boolean isExpired(){
        if(ttl == -1) {
            return false;
        }
        return System.currentTimeMillis() > getExpiryTime() + getTtl();
    }

}
