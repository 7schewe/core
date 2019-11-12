package io.schewe.core.util;

public interface Callback {

    interface SimpleCallback{ void execute(); }
    interface CallbackWithParameters{ void execute(Object... args);}
}
