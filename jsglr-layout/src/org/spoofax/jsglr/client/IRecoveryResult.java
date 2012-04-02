package org.spoofax.jsglr.client;

import java.util.Map;

public interface IRecoveryResult {
    public String getResult();
    public Map<Integer, char[]> getSuggestions();
}
