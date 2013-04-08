package org.spoofax.jsglr_layout.client.indentation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LocalVariableManager {

  public class LocalVariable {
    
    private Class<?> type;
    private String name;
    
    public Class<?> getType() {
      return type;
    }
    
    public String getName() {
      return name;
    }
    
    @SuppressWarnings("synthetic-access")
    private LocalVariable(Class<?> type) {
      this.type = type;
      this.name = "temp"+LocalVariableManager.this.numVariables;
      LocalVariableManager.this.numVariables++;
    }
    
  }
  
  private Map<Class<?>, List<LocalVariable>> freeLocalVariables;
  private Map<Class<?>, List<LocalVariable>> lockedLocalVariables;
  private int numVariables;
  
  public LocalVariableManager() {
    this.freeLocalVariables = new HashMap<Class<?>, List<LocalVariable>>();
    this.lockedLocalVariables = new HashMap<Class<?>, List<LocalVariable>>();
    this.numVariables = 0;
  }
  
  @SuppressWarnings("synthetic-access")
  public LocalVariable getFreeLocalVariable(Class<?> type) {
    // Get the list of free variables
    List<LocalVariable> freeVariables = this.freeLocalVariables.get(type);
    if (freeVariables == null) {
      freeVariables = new ArrayList<LocalVariableManager.LocalVariable>();
      this.freeLocalVariables.put(type, freeVariables);
    }
    // Check whether a free variable is available
    LocalVariable var;
    if (freeVariables.size() > 0) {
      // Remove variable from free ones
      var = freeVariables.remove(0);
    } else {
      // Create a new one
      var = new LocalVariable(type);
    }
    // Mark var as used
    List<LocalVariable> lockedVariables = this.lockedLocalVariables.get(type);
    if (lockedVariables == null) {
      lockedVariables = new ArrayList<LocalVariableManager.LocalVariable>();
      this.lockedLocalVariables.put(type, lockedVariables);
    }
    lockedVariables.add(var);
    return var;
  }
  
  public void releaseLocalVariable(LocalVariable var) {
    // Remove var from locked variables
    List<LocalVariable> lockedVariables = this.lockedLocalVariables.get(var.getType());
    if (lockedVariables == null) {
      throw new IllegalStateException("Cannot release a Variable for a type which was never locked!");
    }
    if (!lockedVariables.contains(var)) {
      throw new IllegalStateException("Cannot release a Variable which is not locked!");
    }
    lockedVariables.remove(var);
    // and add to free
    List<LocalVariable> freeVariables = this.freeLocalVariables.get(var.getType());
    if (freeVariables == null || freeVariables.contains(var)) {
      throw new IllegalStateException("Cannot release a Variable which is free!");
    }
    freeVariables.add(var);
  }
  
  public List<LocalVariable> getDeclaredVariables() {
    // no locked variable should occur here
    for (List<LocalVariable> vars : this.lockedLocalVariables.values()) {
      if (!vars.isEmpty()) {
        throw new IllegalStateException("When getting declared variables, no locked variables are allowd to exist!");
      }
    }
    // Merge free variables
    ArrayList<LocalVariable> declaredVariables = new ArrayList<LocalVariableManager.LocalVariable>();
    for (List<LocalVariable> vars : this.freeLocalVariables.values()) {
      declaredVariables.addAll(vars);
    }
    return declaredVariables;
  }
  

}
