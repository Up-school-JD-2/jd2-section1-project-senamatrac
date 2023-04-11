package data;

import java.util.UUID;

public abstract class BaseData {
    private final String id ;

    public BaseData() {
        this.id = UUID.randomUUID().toString();
    }

    public String getId() {
        return id;
    }
}
