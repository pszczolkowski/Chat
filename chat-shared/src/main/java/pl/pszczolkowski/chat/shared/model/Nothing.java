package pl.pszczolkowski.chat.shared.model;

import java.io.Serializable;

/**
 * XML-RPC methods cannot be void, so this class is used as return type
 */
public class Nothing implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final Nothing instance = new Nothing();

    public static Nothing nothing() {
        return instance;
    }

}
