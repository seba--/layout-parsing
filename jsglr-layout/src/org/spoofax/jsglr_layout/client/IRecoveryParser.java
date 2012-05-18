package org.spoofax.jsglr_layout.client;


public interface IRecoveryParser {
    IRecoveryResult recover(String text) throws Exception;
    IRecoveryResult recover(String text, String startSymbol) throws Exception;
}
