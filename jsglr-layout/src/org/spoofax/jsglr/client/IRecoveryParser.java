package org.spoofax.jsglr.client;


public interface IRecoveryParser {
    IRecoveryResult recover(String text) throws Exception;
    IRecoveryResult recover(String text, String startSymbol) throws Exception;
}
