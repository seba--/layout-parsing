package org.spoofax.jsglr_layout.client.indentation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The {@link LocalVariableManager} manages local variables for the
 * {@link CompilableLayoutNode}. It stores the local variables declared in the
 * method for evaluation the layout constraint. It tries to minimize the number
 * of local variables such that they can be reused if possible to reduce stack
 * use. Although the effect should not be that big.<br>
 * During generation one can require a local variable by calling
 * {@link #getFreeLocalVariable(Class)}. This method returns a reference to a
 * {@link LocalVariable} object which is not used an can be written. When the
 * variable is not used anymore one should call
 * {@link #releaseLocalVariable(LocalVariable)} which marks the variable as not
 * used and it may be reused later. When generating code is finished, one can
 * receive a list of variables which needs to be declared by calling
 * {@link #getDeclaredVariables()}.
 * 
 * @author moritzlichter
 * 
 */
public class LocalVariableManager {

  /**
   * The LocalVariable describes the declaration of a local variable in the
   * method for the generated code to evaluate the layout constraint.
   * 
   * @author moritzlichter
   * 
   */
  public class LocalVariable {

    private Class<?> type;
    private String name;
    private boolean primitive;

    public Class<?> getType() {
      return type;
    }

    public boolean isPrimitive() {
      return primitive;
    }

    public String getName() {
      return name;
    }

    @SuppressWarnings("synthetic-access")
    private LocalVariable(Class<?> type, boolean primitive) {
      this.type = type;
      this.name = "temp" + LocalVariableManager.this.numVariables;
      this.primitive = primitive;
      LocalVariableManager.this.numVariables++;
    }

  }

  private Map<Class<?>, List<LocalVariable>> freeLocalVariables;
  private Map<Class<?>, List<LocalVariable>> lockedLocalVariables;
  private int numVariables;

  /**
   * Creates a new {@link LocalVariableManager} without any declares variables.
   */
  public LocalVariableManager() {
    this.freeLocalVariables = new HashMap<Class<?>, List<LocalVariable>>();
    this.lockedLocalVariables = new HashMap<Class<?>, List<LocalVariable>>();
    this.numVariables = 0;
  }

  /**
   * Returns a reference to a {@link LocalVariable} which is free for the given
   * class type. When a free variable already exists, they are reused, otherwise
   * a new one is created.
   * 
   * @param type
   *          the class type of the variable
   * @param primitive
   *          whether the variable is primitive, than the class only describes
   *          it type (eg Integer.class for int)
   * @return a LocalVariable which can be used
   */
  @SuppressWarnings("synthetic-access")
  private LocalVariable getFreeLocalVariable(Class<?> type, boolean primitive) {
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
      var = new LocalVariable(type, primitive);
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

  /**
   * Returns a reference to a {@link LocalVariable} which is free for the given
   * class type. When a free variable already exists, they are reused, otherwise
   * a new one is created.
   * 
   * @param type
   *          the class type of the variable
   * @return a LocalVariable which can be used
   */
  public LocalVariable getFreeLocalVariable(Class<?> type) {
    return this.getFreeLocalVariable(type, false);
  }

  /**
   * Returns a reference to a {@link LocalVariable} which is free for the given
   * class type. When a free variable already exists, they are reused, otherwise
   * a new one is created. This method only creates a primitive variable. So the
   * given class only describes the type, eg. Integer.class for int.
   * 
   * @param type
   *          the type of the primitive variable
   * @return a LocalVariable which can be used
   */
  public LocalVariable getFreeLocalPrimitiveVariable(Class<?> type) {
    return this.getFreeLocalVariable(type, true);
  }

  /**
   * Releases the given {@link LocalVariable}. This method marks the given
   * variable as free such it can be reused later in the evaluation process.
   * 
   * @param var
   *          the variable to release
   * @throws IllegalStateException
   *           when the variable is not locked (marked as used).
   */
  public void releaseLocalVariable(LocalVariable var) {
    // Remove var from locked variables
    List<LocalVariable> lockedVariables = this.lockedLocalVariables.get(var
        .getType());
    if (lockedVariables == null) {
      throw new IllegalStateException(
          "Cannot release a Variable for a type which was never locked!");
    }
    if (!lockedVariables.contains(var)) {
      throw new IllegalStateException(
          "Cannot release a Variable which is not locked!");
    }
    lockedVariables.remove(var);
    // and add to free
    List<LocalVariable> freeVariables = this.freeLocalVariables.get(var
        .getType());
    if (freeVariables == null || freeVariables.contains(var)) {
      throw new IllegalStateException(
          "Cannot release a Variable which is free!");
    }
    freeVariables.add(var);
  }

  /**
   * Returns a list of all {@link LocalVariable}s which are declared in the
   * {@link LocalVariableManager}. This method checks that all variables are
   * released.
   * 
   * @return a list of all variables declared in the manager
   * @throws IllegalStateException
   *           when there are locked variables
   */
  public List<LocalVariable> getDeclaredVariables() {
    // no locked variable should occur here
    for (List<LocalVariable> vars : this.lockedLocalVariables.values()) {
      if (!vars.isEmpty()) {
        throw new IllegalStateException(
            "When getting declared variables, no locked variables are allowd to exist!");
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
