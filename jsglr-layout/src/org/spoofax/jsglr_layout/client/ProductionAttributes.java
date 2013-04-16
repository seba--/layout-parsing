/*
 * Created on 16.apr.2006
 *
 * Copyright (c) 2005, Karl Trygve Kalleberg <karltk near strategoxt.org>
 *
 * Licensed under the GNU General Public License, v2
 */
package org.spoofax.jsglr_layout.client;

import static org.spoofax.jsglr_layout.client.ProductionType.AVOID;
import static org.spoofax.jsglr_layout.client.ProductionType.PREFER;

import java.io.Serializable;

import org.spoofax.interpreter.terms.IStrategoTerm;
import org.spoofax.jsglr_layout.client.indentation.CompiledLayoutConstraint;
import org.spoofax.jsglr_layout.client.indentation.LayoutFilter;

public class ProductionAttributes implements Serializable {

    private static final long serialVersionUID = 556855017447626835L;

    private final int type;

    private final boolean isRecover;
    private final boolean isIgnoreLayout;
    private final IStrategoTerm layoutConstraintSource;
    private final CompiledLayoutConstraint layoutConstraint;
    private final boolean isNewlineEnforced;
    private final boolean isLongestMatch;

    private final transient IStrategoTerm abstractCtor;

    ProductionAttributes(IStrategoTerm ctor, int type, boolean isRecover, boolean isIgnoreIndent, IStrategoTerm layoutConstraintSource, CompiledLayoutConstraint layoutConstraint, boolean isNewlineEnforced, boolean isLongestMatch) {
        this.type = type;
        this.abstractCtor = ctor;
        this.isRecover = isRecover;
        this.isIgnoreLayout = isIgnoreIndent;
        this.layoutConstraint = layoutConstraint;
        this.isNewlineEnforced = isNewlineEnforced;
        this.isLongestMatch = isLongestMatch;
        if (LayoutFilter.CHECK_LAYOUT_TREE || LayoutFilter.CHECK_RECURSVIE) {
          this.layoutConstraintSource = layoutConstraintSource;
        } else {
          this.layoutConstraintSource = null;
        }
     //   System.out.println("Created " + layoutConstraint + " " + layoutConstraintSource);
    }

    public final int getType() {
        return type;
    }

    public final IStrategoTerm getTerm() {
        return abstractCtor;
    }

    public boolean isRecoverProduction() {
        return isRecover;
    }
    
    public boolean isIgnoreLayout() {
      return isIgnoreLayout;
    }
    
    
    public CompiledLayoutConstraint getLayoutConstraint() {
      return layoutConstraint;
    }
    
    public IStrategoTerm getLayoutConstraintSource() {
      return layoutConstraintSource;
    }
    
    public boolean isNewlineEnforced() {
      return isNewlineEnforced;
    }

    public boolean isMoreEager(ProductionAttributes other) {
        return type != other.type && (type == PREFER || other.type == AVOID);
    }

    public boolean isLongestMatch() {
      return isLongestMatch;
    }
}
